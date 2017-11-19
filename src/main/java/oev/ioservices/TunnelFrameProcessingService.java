package oev.ioservices;

import oev.ioservices.threads.TunnelEngineThread;
import oev.mvc.Model;

import javax.swing.SwingWorker;

public class TunnelFrameProcessingService extends SwingWorker implements FrameProcessingService{

	private int fktNr;
	private int addLaenge;
	private String resPath;
	private Model model;
	
	//for multithreading:
	private int totalFrameAmount;
	private int rest;
	private int rest2;
	private int threadLaenge;
	private int inputThreadLaenge;
	
	private TunnelEngineThread thread1, thread2, thread3, thread4;
	
	public void setResPath(String rP) {
				resPath=rP;
	}

	public void setOptionsAndPrepareExecution(Integer function, Integer effectLengthInFrames){


        fktNr=function;
        addLaenge=effectLengthInFrames;


        	System.out.println("Anzahl input: "+amountFrames+" , effectLengthInFrames: "+addLaenge);
        	
        rest=amountFrames%addLaenge;        //Wird hier nicht weiterverarbeitet
        totalFrameAmount =(amountFrames-rest)/addLaenge;      //Anzahl zu berechnende Frames, geht mit effectLengthInFrames auf
        	System.out.println("Anzahl modulo effectLengthInFrames = "+rest+" input frames werden weggelassen");
        	System.out.println("Insg. zu erzeugende Frames: "+ totalFrameAmount);
       
        rest2= totalFrameAmount %4;               //Aufteilung auf die 4 Threads
        	System.out.println("rest2: "+rest2+" zu erzeugende Frames werden dem 4. Thread zus�tzlich zugeordnet");
        threadLaenge=(totalFrameAmount -rest2)/4;    //Anzahl zu berechnender Frames pro Thread
        	System.out.println("Jeder Thread erzeugt "+threadLaenge+" Frames");
        inputThreadLaenge=threadLaenge*addLaenge;  //Anzahl der Frames die jeder Thread zum verarbeiten bekommt
        	System.out.println("Jeder Thread bekommt "+inputThreadLaenge+" frames");

        
        System.out.println("Threads erstellen");
        thread1 = new TunnelEngineThread(1,startFrame,(inputThreadLaenge+startFrame-1),fktNr,addLaenge, srcPath, resPath);
        thread2 = new TunnelEngineThread(2,(inputThreadLaenge+startFrame),((2*inputThreadLaenge)+startFrame-1),fktNr,addLaenge, srcPath, resPath);
        thread3 = new TunnelEngineThread(3,((2*inputThreadLaenge)+startFrame),((3*inputThreadLaenge)+startFrame-1),fktNr,addLaenge, srcPath, resPath);
        thread4 = new TunnelEngineThread(4,((3*inputThreadLaenge)+startFrame),((inputThreadLaenge*4)+startFrame-1+(rest2*addLaenge)),fktNr,addLaenge, srcPath, resPath);

                      
    }
    
    public void startThreads(){
    	System.out.println("Threads starten");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
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
