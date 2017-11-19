package oev.mvc;

import oev.ioservices.*;

import java.io.*;
import java.util.Observable;
import java.util.logging.Logger;


import javax.swing.JOptionPane;

public class Model extends Observable {

  private static final Logger LOGGER = Logger.getLogger("Model");

  public static final int MODE_SUMIMAGE = 1;
  public static final int MODE_SUMVIDEO = 2;
  public static final int MODE_TRAILVID = 4;
  public static final int MODE_TUNNEL = 5;

  //processing properties:
  @Deprecated
  private String srcPath;
  private File[] sourceFiles;
  private String resPath;
  private int mode;
  private int fkt;
  private int effectLengthInFrames;//the amount of frames used in the mode Special Video (=> higher amount of frames means longer light-trails)

  //to display progress / last action:
  private String lastAction1;
  private String lastAction2;
  private String lastAction3;
  private int operation;

  private FrameProcessingService frameProcessingService;


  public Model() {

    //set default values for input fields
    resPath = System.getProperty("user.home");
    fkt = 1;
    lastAction1 = "Waiting for start...";
    lastAction2 = "-";
    lastAction3 = "-";
    operation = 0;

  }


  @Deprecated
  public void setSrcPath(String s) {

    //if the chosen path is a file, remove the filename. We need a directory path
    if (!new File(s).isDirectory()) {
      s = s.substring(0, s.lastIndexOf("\\"));
    }

    srcPath = s;
    System.out.println("New SrcPath: " + srcPath);
    setChanged();
    notifyObservers();
  }

  public void setSourceFiles(File[] files){
    sourceFiles = files;

    LOGGER.info("source files selected");

    setChanged();
    notifyObservers();
  }

  @Deprecated
  public String getSrcPath() {
    return srcPath;
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

  public void setMode(int m) {
    mode = m;
    System.out.println("Mode Changed to " + mode);
    setChanged();
    notifyObservers();
  }

  public void setFkt(int f) {
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
    switch (mode) {
      case MODE_TUNNEL:
        try {
          return sourceFiles.length / effectLengthInFrames;
        } catch (ArithmeticException e) {
          System.out.println("Division by zero in getMaxOperation()");
          return 0;
        }
      default:
        return sourceFiles.length - effectLengthInFrames;

    }
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
      case MODE_SUMIMAGE:
        frameProcessingService = new SumImageProcessingService();
        break;
      case MODE_SUMVIDEO:
        frameProcessingService = new VideoProcessingService();
        break;
      case MODE_TRAILVID:
        frameProcessingService = new VideoSpecialProcessingServiceMultithreaded();
        break;
      case MODE_TUNNEL:
        frameProcessingService = new TunnelFrameProcessingService();
    }

    frameProcessingService.setSourceFiles(sourceFiles);
    frameProcessingService.setResPath(resPath);
    try {
      frameProcessingService.setModel(this);
    } catch (Exception e) {
      //some implementations need setting the model before the options; others the other way round TODO thats crap
    }

    frameProcessingService.setOptionsAndPrepareExecution(
            fkt,
            effectLengthInFrames
    );
    frameProcessingService.setModel(this);
    frameProcessingService.execute();
  }


  /**
   * checks if selected paths and files exist
   */
  private void checkPaths() {

    //check input files
    for(File file : sourceFiles){
      if (!file.exists() || !file.canRead()) {
        showErrorMessage("input file does not exist or is not readable: "+file.getAbsolutePath());
        return;
      }
    }

    //check output folder
    try {
      File f = new File(resPath + "/resultIMG1.png");
      f.createNewFile();
      f.delete();
    } catch (IOException e) {
      showErrorMessage("result folder does not exist or cannot be written to");
      e.printStackTrace();
      return;
    }
  }

  private void showErrorMessage(String string) {
    JOptionPane.showMessageDialog(null, string, "wrong input", JOptionPane.WARNING_MESSAGE);
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
      Runtime.getRuntime().exec("explorer.exe " + srcPath);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, "Source Destination not found: " + srcPath, "oev.mvc.Model: showSrcPath()", JOptionPane.INFORMATION_MESSAGE);
    }
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

  public int getMode() {
    return mode;
  }

  public String getSourceFIlesInfo(){
    if(sourceFiles != null && sourceFiles.length>0){
      String firstFilePath = sourceFiles[0].getAbsolutePath();
      String directory = firstFilePath.substring(0, firstFilePath.lastIndexOf("\\"));
      return sourceFiles.length + " files in "+directory;
    } else {
      return "click button to select input files";
    }
  }

}
