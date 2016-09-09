package oev.ioservices;

import oev.ioservices.threads.EngineThread;
import oev.mvc.Model;

import javax.swing.SwingWorker;

public class VideoSpecialProcessingServiceMultithreaded extends SwingWorker implements FrameProcessingService{

    private int amountFrames;
    private int fktNr;
    private int effectLengthInFrames;
    private int startFrame;

    private int totalAmountFrames;
    private int amountFramesPerThread;
    private int rest;

    private EngineThread thread1;
    private EngineThread thread2;
    private EngineThread thread3;
    private EngineThread thread4;
    
    private String srcPath;
    private String resPath;
    
    private Model model;

    public VideoSpecialProcessingServiceMultithreaded(){

    }

    /**
     * sets the given options and creates 4 threads, each with a fourth of the frames to process
     * @param amountFrames
     * @param aStartFrame
     * @param function
     * @param aEffectLengthInFrames
     */
    public void setOptionsAndPrepareExecution(Integer amountFrames, Integer aStartFrame, Integer function, Integer aEffectLengthInFrames){

        this.amountFrames =amountFrames;
        fktNr=function;
        effectLengthInFrames = aEffectLengthInFrames;
        startFrame=aStartFrame;

        totalAmountFrames = this.amountFrames - aEffectLengthInFrames;
        rest= totalAmountFrames %4;
        amountFramesPerThread =(totalAmountFrames -rest)/4;

        System.out.println("Threads erstellen");
        thread1 = new EngineThread(1,startFrame,(amountFramesPerThread +startFrame-1),fktNr, effectLengthInFrames, srcPath, resPath);
        thread2 = new EngineThread(2,(amountFramesPerThread +startFrame),((2* amountFramesPerThread)+startFrame-1),fktNr, effectLengthInFrames, srcPath, resPath);
        thread3 = new EngineThread(3,((2* amountFramesPerThread)+startFrame),((3* amountFramesPerThread)+startFrame-1),fktNr, effectLengthInFrames, srcPath, resPath);
        thread4 = new EngineThread(4,((3* amountFramesPerThread)+startFrame),(totalAmountFrames +startFrame-1),fktNr, effectLengthInFrames, srcPath, resPath);

    }
    
    public void startThreads(){
    	System.out.println("Threads starten");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }
    
    public void setPaths(String s, String r){
    	srcPath=s;
    	resPath=r;
    }
    
    public void setModel(Model m){
    	model = m;
    	thread1.setModel(model);
    	thread2.setModel(model);
    	thread3.setModel(model);
    	thread4.setModel(model);
    }

	@Override
	protected Object doInBackground() throws Exception {
		startThreads();
		return null;
	}
    
}