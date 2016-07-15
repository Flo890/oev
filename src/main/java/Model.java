import java.io.*;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.WatchService;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.Observable;
import java.util.Set;

import javax.swing.JOptionPane;

public class Model extends Observable{

	String srcPath;
	String resPath;
	int mode;
	int fkt;
	int anzahlFrames;
	int startFrame;
	int nachziehendeFrames;
	//Für Fortschrittsanzeige
	String lastAction1;
	String lastAction2;
	String lastAction3;
	int operation;
	
	Head head;
	VideoIOM videoIom;
	VideoSpecial2QuadIOM vidSpec2QuadIom;
	TunnelIOM tunneliom;
	
	
	
	
	public Model(){
		
		//Default Werte der Eingabefelder setzen
		srcPath=System.getProperty("user.home");
		resPath=System.getProperty("user.home");
		//srcPath = "C:\\Users\\Florian\\SkyDrive\\ImageEditor\\Projekt3\\ImageEditorGUI\\src\\source";
		//resPath = "C:\\Users\\Florian\\SkyDrive\\ImageEditor\\Projekt3\\ImageEditorGUI\\src\\result";		
		fkt=1;
		anzahlFrames=nachziehendeFrames=0;
		startFrame=1;
		lastAction1="Waiting for start...";
		lastAction2="LA2";
		lastAction3="LA3";
		operation=0;
		
	}
	
	
	/**
	 * Setzt srcPath im Model und entfernt dabei evtl den Dateinamen
	 * @param s
	 */
	public void setSrcPath(String s){
		
		//Evtl. Dateiname aus Path entfernen
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
	
	
	/**
	 * Setzt resPath im Model und entfernt dabei evtl den Dateinamen
	 * @param s
	 */
	public void setResPath(String s){
		
		//Evtl. Dateiname aus Path entfernen
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
	
	public void setAnzahlFrames(int a){
		anzahlFrames=a;
		System.out.println("Neue Anzahl Frames: "+anzahlFrames);		
		setChanged();
		notifyObservers();
	}
	
	public void setStartFrame(int s){
		startFrame=s;
		System.out.println("Neues Start Frame: "+startFrame);
		setChanged();
		notifyObservers();
	}
	
	public void setNachziehendeFrames(int a){
		nachziehendeFrames=a;
		System.out.println("Neue Anzahl nachziehender Frames: "+nachziehendeFrames);
		setChanged();
		notifyObservers();
	}
	
	public void setSrcRes(String s, String r){
		srcPath=s;
		resPath=r;
		System.out.println("Paths changed: Src: "+srcPath+" , Res: "+resPath);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Zeigt eine neue Zeile in der Fortschrittsanzeige an und lässt die vorherigen eine Zeile runter rutschen
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
	 * Gibt die Anzahl der zu berechnenden Frames aus, damit der Ladebalken weiß "wie lang er sein muss"
	 * @return
	 */
	public int getMaxOperations(){
		//Für ProgressBar
		switch(mode){
		case 5:
			try{
				return anzahlFrames/nachziehendeFrames;
			}
			catch(ArithmeticException e){
				System.out.println("Division by zero in getMaxOperation()");
				return 0;
			}
		default:
			return anzahlFrames-nachziehendeFrames;
			
		}
	}
	
	
	/**
	 * Setzt den Ladebalken auf eine neue Position
	 * @param o
	 */
	public void setOperation(int o){
		operation=o;
		setChanged();
		notifyObservers();
	}
	
	
	/**
	 * Erhöht Position des Ladebalken um 1
	 */
	public void iterateOperation(){  //Nach jeder Addition aufrufen um zähler zu erhöhen
		operation++;
		setChanged();
		notifyObservers();
	}
	
	
	/**
	 * Gibt die Position des Ladebalken zurück
	 * @return
	 */
	public int getOperation(){
		return operation;
	}
	
	
	
	/**
	 * Liest die Eingaben aus nachdem Start gedrückt wurde und startet den richtigen Prozess
	 */
	public void run(){
		writeLog();
		switch(mode){
		case 1:			
			String[]input = new String[3];
			input[0]=String.valueOf(anzahlFrames);
			input[1]=String.valueOf(fkt);
			input[2]=String.valueOf(startFrame);
			checkPaths();
			
			head = new Head();
			head.setPaths(srcPath, resPath);
			head.setModel(this);
			
			head.main(input);			
						
			head.execute();			
			break;
		case 2:
			checkPaths();
			String[]input2 = new String[3];
			input2[0]=String.valueOf(anzahlFrames);
			input2[1]=String.valueOf(fkt);
			input2[2]=String.valueOf(startFrame);
			checkPaths();
			
			videoIom = new VideoIOM();
			videoIom.setPaths(srcPath, resPath);
			videoIom.setModel(this);	
			
			videoIom.main(input2);
			
			
			videoIom.execute();
			break;
		case 3:
			//Gibts nicht mehr
			break;
		
		case 4:
			String[]input4 = new String[4];
			input4[0]=String.valueOf(anzahlFrames);
			input4[1]=String.valueOf(fkt);
			input4[2]=String.valueOf(nachziehendeFrames);
			input4[3]=String.valueOf(startFrame);
			checkPaths();
			
			vidSpec2QuadIom = new VideoSpecial2QuadIOM();
			vidSpec2QuadIom.setPaths(srcPath, resPath);
			
			
				vidSpec2QuadIom.main(input4);
				System.out.println("Model: run(): vidSpec2QuadIom.main(input)");
			
			vidSpec2QuadIom.setModel(this);
			vidSpec2QuadIom.execute();
			break;
			
		case 5:
			String[]input5 = new String[4];
			input5[0]=String.valueOf(anzahlFrames);
			input5[1]=String.valueOf(fkt);
			input5[2]=String.valueOf(nachziehendeFrames);
			input5[3]=String.valueOf(startFrame);
			checkPaths();
			
			tunneliom = new TunnelIOM();
			tunneliom.setPaths(srcPath, resPath);
			
			
				tunneliom.main(input5);
				System.out.println("Model: run(): tunneliom(input5)");
			
			tunneliom.setModel(this);
			tunneliom.execute();
		}
		
		
			
	}
	
	
	/**
	 * Überprüft ob genug Dateien im input Ordner vorhanden sind
	 * und ob der output Ordner existiert
	 */
	private void checkPaths() {
		
		//anzahl der vorhandenen input Dateien berechnen
		int totalFrameAnzahl = anzahlFrames;
		/*if(mode==4||mode==5){
			totalFrameAnzahl = totalFrameAnzahl;
		}*/
		
		
		//input Dateien prüfen
		int i = startFrame-1;
		while(i<totalFrameAnzahl){
			i++;
			File f = new File(srcPath+"/"+i+".png");
			if(!f.exists()){
				showFehlermeldung("input file missing: "+i+".png");
				return;				
			}
			
		}
		
		
		//Output folder prüfen		
		try {
			File f = new File(resPath+"/resultIMG1.png");
			f.createNewFile();
			f.delete();
		} catch (IOException e) {
			showFehlermeldung("result folder does not exist or cannot be written to");
			e.printStackTrace();
			return;
		}
			
			
	}
	
	
	
	
	/**
	 * Gibt eine Fehlermeldung mit dem geg. Text an den Nutzer aus
	 * @param string
	 */
	private void showFehlermeldung(String string) {
		JOptionPane.showMessageDialog(null, string, "wrong input", JOptionPane.WARNING_MESSAGE);
		return;
	}
	

	
	/**
	 * Bricht im Hintergrund laufende Berechnung ab
	 */
	public void stop(){
		switch(mode){
		case 1: head.cancel(true);
				break;
		case 2: videoIom.cancel(true);
				break;
		case 4: vidSpec2QuadIom.cancel(true);
				break;
		case 5: tunneliom.cancel(true);
		}
	}
	
	
	/**
	 * Oeffnet gewählten inputOrdner im Explorer
	 */
	public void showSrcPath(){
		try{
		Runtime.getRuntime().exec("explorer.exe "+srcPath);
		}
		catch(IOException e){
			JOptionPane.showMessageDialog(null, "Source Destination not found: "+srcPath, "Model: showSrcPath()",JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	
	/**
	 * Öffnet gewählten Output Ordner im Explorer
	 */
	public void showResPath(){
		try{
			Runtime.getRuntime().exec("explorer.exe "+resPath);
			}
			catch(IOException e){
				JOptionPane.showMessageDialog(null, "Result Destination not found: "+resPath, "Model: showResPath()",JOptionPane.INFORMATION_MESSAGE);
			}
	}
	
	
	/**
	 * Schreibt das LogFile
	 */
	public void writeLog(){
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
		log.println("funktion: "+fkt);
		log.println("anzahl Frames: "+anzahlFrames);
		log.println("start Frame: "+startFrame);
		log.println("nachziehende Frames: "+nachziehendeFrames);
		log.close();
		}
		catch(IOException e){
			System.out.println("Method writeLog():");
			e.printStackTrace();
		}
	}

	
	/**
	 * Gibt den Modus auf den das Model zur Zeit gesetzt ist zurueck
	 * @return gewaehlter Modus
	 */
	public int getMode() {		
		return mode;
	}
	
}
