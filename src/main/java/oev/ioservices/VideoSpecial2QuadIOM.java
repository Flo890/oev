package oev.ioservices;

import oev.EngineThread;
import oev.mvc.Model;

import java.awt.image.BufferedImage;
import javax.swing.SwingWorker;

public class VideoSpecial2QuadIOM extends SwingWorker{

    static int anzahl;
    static int fktNr;
    static BufferedImage resultImage;
    static int addLaenge;
    static int fileIndex;
    static int fileIndex2;
    static int startFrame;

    static int totalAnzahl;
    static int threadLaenge;
    static int rest;

    static EngineThread thread1;
    static EngineThread thread2;
    static EngineThread thread3;
    static EngineThread thread4;
    
    static String srcPath;
    static String resPath;
    
    static Model model;

    public VideoSpecial2QuadIOM(){

    }

    public static void main(String[] args){
        // 1.Parameter: Anzahl Bilder
        // 2.Parameter: Fkuntions Nummer
        // 3.Parameter: Länge der Additionen
    	// 4.Parameter: Nr StartFrame
        anzahl=Integer.parseInt(args[0]);
        fktNr=Integer.parseInt(args[1]);
        addLaenge=Integer.parseInt(args[2]);
        startFrame=Integer.parseInt(args[3]);

        totalAnzahl=anzahl-addLaenge;
        rest=totalAnzahl%4;
        threadLaenge=(totalAnzahl-rest)/4;

        System.out.println("Threads erstellen");
        thread1 = new EngineThread(1,startFrame,(threadLaenge+startFrame-1),fktNr,addLaenge, srcPath, resPath);
        thread2 = new EngineThread(2,(threadLaenge+startFrame),((2*threadLaenge)+startFrame-1),fktNr,addLaenge, srcPath, resPath);
        thread3 = new EngineThread(3,((2*threadLaenge)+startFrame),((3*threadLaenge)+startFrame-1),fktNr,addLaenge, srcPath, resPath);
        thread4 = new EngineThread(4,((3*threadLaenge)+startFrame),(totalAnzahl+startFrame-1),fktNr,addLaenge, srcPath, resPath);

                      
    }
    
    public void startThreads(){
    	System.out.println("Threads starten");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }
    
    public static void setPaths(String s, String r){
    	srcPath=s;
    	resPath=r;
    }
    
    public static void setModel(Model m){
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