package oev;

import oev.mvc.Model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;


public class TunnelEngineThread extends Thread{
	
	 int threadNr;
	    int start;
	    int end;
	    int fktNr;
	    Engine engine;
	    int addLaenge;
	    int anzahlAdditionen;

	    int fileIndex;
	    int fileIndex2;
	    BufferedImage resultImage;
	    
	    String srcPath;
	    String resPath;
	    
	    Model model;
	
	public TunnelEngineThread(int tn, int s, int e, int f, int al, String sP, String rP){
        threadNr = tn;
        start = s;
        end = e;
        fktNr = f;
        addLaenge = al;
        srcPath = sP;
        resPath = rP;
        anzahlAdditionen = ((end-start)/addLaenge);
        System.out.println(threadNr+": from "+start+" to "+end+" with fkt "+fktNr+" and AddLaenge "+addLaenge+"Additionen: "+anzahlAdditionen);
    }
	
	public void setModel(Model m) {
		model=m;
	}
	
	public void run(){
		try{
			load();
		}
		catch(IOException e){
			showFehlermeldung("IOException in Thread "+threadNr+": "+e.getMessage());
			e.printStackTrace();
		}
	}

	private void load() throws IOException{
		fileIndex=start;
		
		int test2 = 0;
		for(int i=0; i<anzahlAdditionen+1; i++){
			test2++;
			try{
				engine = new Engine(ImageIO.read(new File(srcPath+"\\"+Integer.toString(fileIndex)+".png")));
				System.out.println("T"+threadNr+" loading "+fileIndex+".png for creating new oev.Engine");
			}
			catch(IOException e){
				System.out.println("Thread "+threadNr+" at Addition "+test2+" trying to open "+fileIndex+".png :");
			}
			
			fileIndex2=fileIndex+1;
			
			switch(fktNr){
			case 4:
				for(int j=0; j<addLaenge-1; j++){			
				
					resultImage=engine.averageValue(ImageIO.read(new File(srcPath+"\\"+Integer.toString(fileIndex2)+".png")));
					fileIndex2++;				
				}
				save();
				fileIndex=fileIndex+addLaenge;
				break;
			
			default:
				int test = 0;
				for(int j=0; j<addLaenge-1; j++){			
					
					resultImage=engine.maxValue2(fktNr, ImageIO.read(new File(srcPath+"\\"+Integer.toString(fileIndex2)+".png")));
					System.out.println("T"+threadNr+" loading "+fileIndex2+".png");
					fileIndex2++;
					test++;
				}
				System.out.println("Innere Schleife (addLaenge) "+test+" mal durchlaufen");
				save();
				fileIndex=fileIndex+addLaenge;
				break;
			}
		}
		System.out.println("�u�ere Schleife (anzahlAdditionen) "+test2+" mal durchlaufen");
	}
	
	
	
	/**
	 * resultImage speichern
	 * @throws IOException
	 */
	private void save() throws IOException{
		File saveFile = new File(resPath+"\\resultIMG"+((fileIndex-1)/addLaenge)+".png");

        ImageIO.write(resultImage, "png", saveFile);
        System.out.println(threadNr+": File "+((fileIndex-1)/addLaenge)+" saved");
        model.setNewAction("Thread "+threadNr+": Saved img resultIMG"+fileIndex+".png");
        model.iterateOperation();
	}
	
	
	
	
	 /**
     * Gegebenen Text als Fehlermeldung anzeigen
     * @param text
     */
    private static void showFehlermeldung(String text){
    	JOptionPane.showMessageDialog(null, text, "IOException while running", JOptionPane.WARNING_MESSAGE);
    }

}
