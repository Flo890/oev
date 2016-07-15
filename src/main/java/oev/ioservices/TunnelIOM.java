package oev.ioservices;

import oev.TunnelEngineThread;
import oev.mvc.Model;

import javax.swing.SwingWorker;

public class TunnelIOM extends SwingWorker{

	int anzahl;
	int fktNr;
	int addLaenge;
	int startFrame;
	String srcPath;
	String resPath;
	Model model;
	
	//F�r Multithreading:
	int totalAnzahl;
	int rest;
	int rest2;
	int threadLaenge;
	int inputThreadLaenge;
	
	TunnelEngineThread thread1, thread2, thread3, thread4;
	
	public void setPaths(String sP, String rP) {
				srcPath=sP;
				resPath=rP;
	}

	public void main(String[] args){
		// 1.Parameter: Anzahl Bilder
        // 2.Parameter: Fkuntions Nummer
        // 3.Parameter: Länge der Additionen
    	// 4.Parameter: Nr StartFrame
        anzahl=Integer.parseInt(args[0]);
        fktNr=Integer.parseInt(args[1]);
        addLaenge=Integer.parseInt(args[2]);
        startFrame=Integer.parseInt(args[3]);

        	System.out.println("Anzahl input: "+anzahl+" , addLaenge: "+addLaenge);
        	
        rest=anzahl%addLaenge;        //Wird hier nicht weiterverarbeitet
        totalAnzahl=(anzahl-rest)/addLaenge;      //Anzahl zu berechnende Frames, geht mit addLaenge auf
        	System.out.println("Anzahl modulo addLaenge = "+rest+" input frames werden weggelassen");
        	System.out.println("Insg. zu erzeugende Frames: "+totalAnzahl);
       
        rest2=totalAnzahl%4;               //Aufteilung auf die 4 Threads
        	System.out.println("rest2: "+rest2+" zu erzeugende Frames werden dem 4. Thread zus�tzlich zugeordnet");
        threadLaenge=(totalAnzahl-rest2)/4;    //Anzahl zu berechnender Frames pro Thread
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
