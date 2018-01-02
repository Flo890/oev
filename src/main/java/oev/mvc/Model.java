package oev.mvc;

import oev.ioservices.*;
import oev.ioservices.binningalgo.BinningSumImageFrameProcessingService;
import oev.model.ColorFunction;
import oev.model.Mode;
import oev.model.SumAlgo;

import java.io.*;
import java.util.Observable;
import java.util.logging.Logger;


import javax.swing.JOptionPane;

public class Model extends Observable {

    private static final Logger LOGGER = Logger.getLogger("Model");

    //processing properties:
    private File[] sourceFiles;
    private String resPath;
    private Mode mode;
    private ColorFunction fkt;
    private int effectLengthInFrames;//the amount of frames used in the mode Special Video (=> higher amount of frames means longer light-trails)
    private SumAlgo sumAlgo = SumAlgo.KEEP_BRIGHTEST;

    //to display progress / last action:
    private String lastAction1;
    private String lastAction2;
    private String lastAction3;
    private int operation;

    private FrameProcessingService frameProcessingService;

    private int amountThreads;
    private boolean multithreadingEnabled;


    public Model() {

        //set default values for input fields
        resPath = System.getProperty("user.home");
        fkt = ColorFunction.HUMAN_PERCEIVED_BRIGHTNESS;
        lastAction1 = "Waiting for start...";
        lastAction2 = "-";
        lastAction3 = "-";
        operation = 0;
        mode = Mode.SUMMIMAGE;

        amountThreads = Runtime.getRuntime().availableProcessors();
        multithreadingEnabled = true;
    }

    public void setSourceFiles(File[] files) {
        sourceFiles = files;

        LOGGER.info("source files selected");

        setChanged();
        notifyObservers();
    }

    public void setResPath(File file) {
        resPath = file.getAbsolutePath();
        System.out.println("New ResPath: " + resPath);
        setChanged();
        notifyObservers();
    }

    public String getResPath() {
        return resPath;
    }

    public void setMode(Mode m) {
        mode = m;
        System.out.println("Mode Changed to " + mode);
        setChanged();
        notifyObservers();
    }

    public void setFkt(ColorFunction f) {
        fkt = f;
        System.out.println("Fkt changed to " + fkt);
        setChanged();
        notifyObservers();
    }

    public void setEffectLengthInFrames(int a) {
        effectLengthInFrames = a;
        System.out.println("Neue Anzahl nachziehender Frames: " + effectLengthInFrames);
        setChanged();
        notifyObservers();
    }

    /**
     * shows a new action near the progress bar and lets the older actions float down one line
     *
     * @param ls
     */
    public void setNewAction(String ls) {
        lastAction3 = lastAction2;
        lastAction2 = lastAction1;
        lastAction1 = ls;
        setChanged();
        notifyObservers();
    }

    public String getLastAction1() {
        return lastAction1;
    }

    public String getLastAction2() {
        return lastAction2;
    }

    public String getLastAction3() {
        return lastAction3;
    }


    /**
     * @return total amount of frames to process (needed to calculate progress percentage)
     */
    public int getMaxOperations() {
        if(sourceFiles != null) {
            return sourceFiles.length - effectLengthInFrames;
        }
        return 0;
    }


    /**
     * sets progress bar to new position
     *
     * @param o
     */
    public void setProgressState(int o) {
        operation = o;
        setChanged();
        notifyObservers();
    }


    /**
     * increases the value of the progressbar by 1
     */
    public void increaseProgress() {
        operation++;
        setChanged();
        notifyObservers();
    }


    /**
     * @return postion of progress bar
     */
    public int getProgress() {
        return operation;
    }


    public void run() {
        writeLogFile();
        checkPaths();
        switch (mode) {
            case SUMMIMAGE:
                if (SumAlgo.KEEP_BRIGHTEST.equals(sumAlgo)) {
                    frameProcessingService = new SumImageProcessingService(this);
                } else if (SumAlgo.SUM_RGB.equals(sumAlgo)) {
                    frameProcessingService = new BinningSumImageFrameProcessingService(this);
                } else {
                    throw new IllegalArgumentException("unknown sumalgo "+sumAlgo);
                }

                frameProcessingService.setOptionsAndPrepareExecution(
                        fkt,
                        sourceFiles,
                        resPath
                );
                break;
            case SUMVIDEO:
                frameProcessingService = new VideoProcessingService(this);
                frameProcessingService.setOptionsAndPrepareExecution(
                        fkt,
                        sourceFiles,
                        resPath
                );
                break;
            case TRAILVIDEO:
                frameProcessingService = new VideoSpecialProcessingServiceMultithreaded(this);
                ((VideoSpecialProcessingServiceMultithreaded) frameProcessingService).setOptionsAndPrepareExecution(
                        fkt,
                        effectLengthInFrames,
                        sourceFiles,
                        resPath
                );
                break;
        }

        frameProcessingService.setModel(this);
        frameProcessingService.execute();
    }


    /**
     * checks if selected paths and files exist
     */
    private void checkPaths() {

        //check input files
        for (File file : sourceFiles) {
            if (!file.exists() || !file.canRead()) {
                showErrorMessage("input file does not exist or is not readable: " + file.getAbsolutePath());
                return;
            }
        }

        //check output folder
        try {
            File f = new File(resPath + "/resultImg1.png");
            f.createNewFile();
            f.delete();
        } catch (IOException e) {
            showErrorMessage("result folder does not exist or cannot be written to");
            e.printStackTrace();
            return;
        }
    }

    public void showErrorMessage(String string) {
        JOptionPane.showMessageDialog(null, string, "A problem occured", JOptionPane.WARNING_MESSAGE);
        return;
    }


    public void stop() {
        frameProcessingService.cancel(true);
    }


    /**
     * opens selected input folder
     */
    public void showSrcPath() {
        try {
            Runtime.getRuntime().exec("explorer.exe " + getSourceFolderPath());
        } catch (Exception e) {
            showErrorMessage("could not open source folder in explorer");
        }
    }

    private String getSourceFolderPath() {
        if (sourceFiles != null && sourceFiles.length > 0) {
            return sourceFiles[0].getParentFile().getAbsolutePath();
        }
        return null;
    }


    /**
     * opens selected output folder
     */
    public void showResPath() {
        try {
            Runtime.getRuntime().exec("explorer.exe " + resPath);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Result Destination not found: " + resPath, "oev.mvc.Model: showResPath()", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    public void writeLogFile() {
        try {
            File file = new File(resPath + "\\Log.txt");
            PrintWriter log = new PrintWriter(file);

            java.util.Date now = new java.util.Date();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("DD.mm.yyyy HH.mm.ss");
            String datum = sdf.format(now);

            log.println(datum);
            log.println("result path: " + resPath);
            log.println("mode: " + mode);
            log.println("function: " + fkt);
            log.println("effect length in frames: " + effectLengthInFrames);
            log.close();
        } catch (IOException e) {
            System.out.println("writing logfile failed:");
            e.printStackTrace();
        }
    }

    public Mode getMode() {
        return mode;
    }

    public String getSourceFilesInfo() {
        if (sourceFiles != null && sourceFiles.length > 0) {
            String firstFilePath = sourceFiles[0].getAbsolutePath();
            String directory = firstFilePath.substring(0, firstFilePath.lastIndexOf("\\"));
            return sourceFiles.length + " files in " + directory;
        } else {
            return "click button to select input files";
        }
    }

    public void showJobFinishedMessage(){
        JOptionPane.showMessageDialog(null, "execution has completed");
    }

    public SumAlgo getSumAlgo() {
        return sumAlgo;
    }

    public void setSumAlgo(SumAlgo sumAlgo) {
        this.sumAlgo = sumAlgo;
        System.out.println("set sumalgo to "+sumAlgo);
    }
    public boolean isMultithreadingEnabled() {
        return multithreadingEnabled;
    }

    public void setMultithreadingEnabled(boolean multithreadingEnabled) {
        this.multithreadingEnabled = multithreadingEnabled;
        LOGGER.info("multithreading checkbox changed: "+this.multithreadingEnabled);
    }

    public int getAmountThreads() {
        return amountThreads;
    }
}
