package oev.mvc;

import oev.ioservices.*;

import java.io.*;
import java.util.Observable;


import javax.swing.JOptionPane;

public class Model extends Observable {

	//processing properties:
	private String srcPath;
	private String resPath;
	private int mode;
	private int fkt;
	private int amountFrames;
	private int startFrame;
	private int effectLengthInFrames;//the amount of frames used in the mode Special Video (=> higher amount of frames means longer light-trails)

	//to display progress / last action:
	private String lastAction1;
	private String lastAction2;
	private String lastAction3;
	private int operation;
	
//	private SumImageProcessingService head;
//	private VideoProcessingService videoIom;
//	private VideoSpecialProcessingServiceMultithreaded vidSpec2QuadIom;
//	private TunnelFrameProcessingService tunneliom;
	private FrameProcessingService frameProcessingService;
	
	
	
	
	public Model(){
		
		//set default values for input fields
		srcPath=System.getProperty("user.home");
		resPath=System.getProperty("user.home");
		fkt=1;
		amountFrames = effectLengthInFrames =0;
		startFrame=1;
		lastAction1="Waiting for start...";
		lastAction2="-";
		lastAction3="-";
		operation=0;
		
	}
	

	public void setSrcPath(String s){
		
		//if the chosen path is a file, remove the filename. We need a directory path
		if(!new File(s).isDirectory()){
			s = s.substring(0,s.lastIndexOf("\\"));			
		}		
		
		srcPath=s;
		System.out.println("New SrcPath: "+srcPath);		
		setChanged();
		notifyObservers();
	}
	
	public String getSrcPath(){
		return srcPath;
	}


	public void setResPath(String s){

		//if the chosen path is a file, remove the filename. We need a directory path
		if(!new File(s).isDirectory()){
			s = s.substring(0,s.lastIndexOf("\\"));			
		}
				
		resPath=s;
		System.out.println("New ResPath: "+resPath);
		setChanged();
		notifyObservers();
	}
	
	public String getResPath(){
		return resPath;
	}
	
	public void setMode(int m){
		mode=m;
		System.out.println("Mode Changed to "+mode);
		setChanged();
		notifyObservers();
	}
	
	public void setFkt(int f){
		fkt=f;
		System.out.println("Fkt changed to "+fkt);
		setChanged();
		notifyObservers();
	}
	
	public void setAmountFrames(int a){
		amountFrames =a;
		System.out.println("Neue Anzahl Frames: "+ amountFrames);
		setChanged();
		notifyObservers();
	}
	
	public void setStartFrame(int s){
		startFrame=s;
		System.out.println("Neues Start Frame: "+startFrame);
		setChanged();
		notifyObservers();
	}
	
	public void setEffectLengthInFrames(int a){
		effectLengthInFrames =a;
		System.out.println("Neue Anzahl nachziehender Frames: "+ effectLengthInFrames);
		setChanged();
		notifyObservers();
	}

	/**
	 * shows a new action near the progress bar and lets the older actions float down one line
	 * @param ls
	 */
	public void setNewAction(String ls){
		lastAction3=lastAction2;
		lastAction2=lastAction1;
		lastAction1=ls;
		setChanged();
		notifyObservers();
	}
	
	public String getLastAction1(){
		return lastAction1;
	}
	
	public String getLastAction2(){
		return lastAction2;
	}
	
	public String getLastAction3(){
		return lastAction3;
	}
	
	
	/**
	 *
	 * @return total amount of frames to process (needed to calculate progress percentage)
	 */
	public int getMaxOperations(){
		switch(mode){
		case 5:
			try{
				return amountFrames / effectLengthInFrames;
			}
			catch(ArithmeticException e){
				System.out.println("Division by zero in getMaxOperation()");
				return 0;
			}
		default:
			return amountFrames - effectLengthInFrames;
			
		}
	}
	
	
	/**
	 * sets progress bar to new position
	 * @param o
	 */
	public void setProgressState(int o){
		operation=o;
		setChanged();
		notifyObservers();
	}
	
	
	/**
	 * increases the value of the progressbar by 1
	 */
	public void increaseProgress(){
		operation++;
		setChanged();
		notifyObservers();
	}
	
	
	/**
	 *
	 * @return postion of progress bar
	 */
	public int getProgress(){
		return operation;
	}
	
	
	

	public void run(){
		writeLogFile();
		checkPaths();
		switch(mode){
			case 1:
				frameProcessingService = new SumImageProcessingService();
				break;
			case 2:
				frameProcessingService = new VideoProcessingService();
				break;
			case 4:
				frameProcessingService = new VideoSpecialProcessingServiceMultithreaded();
				break;
			case 5:
				frameProcessingService = new TunnelFrameProcessingService();
		}

		frameProcessingService.setPaths(srcPath, resPath);
		try {
			frameProcessingService.setModel(this);
		} catch(Exception e){
			//some implementations need setting the model before the options; others the other way round TODO thats crap
		}

		frameProcessingService.setOptionsAndPrepareExecution(
				amountFrames,
				fkt,
				startFrame,
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
		int totalFrameAmount = amountFrames;
		int i = startFrame-1;
		while(i<totalFrameAmount){
			i++;
			File f = new File(srcPath+"/"+i+".png");
			if(!f.exists()){
				showErrorMessage("input file missing: "+i+".png");
				return;				
			}
			
		}
		
		
		//check output folder
		try {
			File f = new File(resPath+"/resultIMG1.png");
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
	


	public void stop(){
		frameProcessingService.cancel(true);
	}
	
	
	/**
	 * opens selected input folder
	 */
	public void showSrcPath(){
		try{
			Runtime.getRuntime().exec("explorer.exe "+srcPath);
		}
		catch(IOException e){
			JOptionPane.showMessageDialog(null, "Source Destination not found: "+srcPath, "oev.mvc.Model: showSrcPath()",JOptionPane.INFORMATION_MESSAGE);
		}
	}


	/**
	 * opens selected output folder
	 */
	public void showResPath(){
		try{
			Runtime.getRuntime().exec("explorer.exe "+resPath);
		} catch(IOException e){
			JOptionPane.showMessageDialog(null, "Result Destination not found: "+resPath, "oev.mvc.Model: showResPath()",JOptionPane.INFORMATION_MESSAGE);
		}
	}
	

	public void writeLogFile(){
		try{
		File file = new File(resPath+"\\Log.txt");
		PrintWriter log = new PrintWriter(file);
		
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("DD.mm.yyyy HH.mm.ss");
		String datum = sdf.format(now);
		
		log.println(datum);
		log.println("source path: "+srcPath);
		log.println("result path: "+resPath);
		log.println("mode: "+mode);
		log.println("function: "+fkt);
		log.println("amount Frames: "+ amountFrames);
		log.println("start Frame: "+startFrame);
		log.println("effect length in frames: "+ effectLengthInFrames);
		log.close();
		} catch(IOException e){
			System.out.println("writing logfile failed:");
			e.printStackTrace();
		}
	}

	public int getMode() {		
		return mode;
	}
	
}
