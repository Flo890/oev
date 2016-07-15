package oev.ioservices;

import oev.Engine;
import oev.mvc.Model;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import java.awt.image.BufferedImage;
import java.io.*;


public class IOMachine{

    private BufferedImage[] array;
    private Engine engine;
    private int engineNr;
    private int fkt;
    private int startFrame;
    private int amount;
    private String srcPath, resPath;
    private Model model;

    public IOMachine(int am, int f, int sf, int e, String s, String r, Model m){
        array = new BufferedImage[am];
        fkt = f;
        startFrame = sf;
        engineNr = e;
        amount = am;
        srcPath=s;
        resPath=r;
        model=m;
    }

    
    /**
     * Lädt der Reihe nach alle Dateien, verrechnet sie jeweils und speichert das resultImg
     */
    public void loadAndProcessAllFrames(){
        //Benötigt deutlich weniger Arbeitsspeicher als load()
        BufferedImage actualFrame;

        
        	System.out.println("oev.ioservices.IOMachine: Try loading file "+srcPath+"\\"+startFrame+".png");
        	model.setNewAction("IOM: Loading file "+startFrame+".png");
        	
        	try{
        		engine = new Engine(ImageIO.read(new File(srcPath+"\\"+Integer.toString(startFrame)+".png")));  //firstFrame übergeben!
        	}catch(IOException e){
        		showFehlermeldung("Fehler beim Laden von "+startFrame+".png\n"+e.getMessage());
        	}
        	
            model.iterateOperation();



        BufferedImage outputFile = null;
        int filenr=startFrame+1;
        for(int i=0; i<(amount-1); i++){
        	System.out.println("oev.ioservices.IOMachine: Try loading file "+srcPath+"\\"+filenr+".png");
        	model.setNewAction("IOM: Loading file "+filenr+".png");
        		
        		actualFrame = null;
        	try{
            	actualFrame=ImageIO.read(new File(srcPath+"\\"+Integer.toString(filenr)+".png"));
        	}catch(IOException e){
        		showFehlermeldung("Fehler beim Laden von "+filenr+".png\n"+e.getMessage());
        	}
            
            model.iterateOperation();
            filenr++;


            switch(fkt){
                
                case 4:    
                  outputFile=engine.averageValue(actualFrame);
                  System.out.println("FileNr "+filenr);
                  break;
                default:
                  outputFile=engine.maxValue2(fkt,actualFrame);
                  System.out.println("FileNr "+filenr);
                  break;
            }
        }        

        save(outputFile);
    }

    /**
     * outputFile speichern
     */
    private void save(BufferedImage outputFile){
        File saveFile = new File(resPath+"\\resultIMG.png");

        try{
        ImageIO.write(outputFile, "png", saveFile);
        }catch(IOException e){
    		showFehlermeldung("Fehler beim Speichern von resultIMG.png\n"+e.getMessage());
    	}
        
        model.setNewAction("IOM: Saved  file resultIMG.png");
       
        System.out.println("File saved");

    }

    
    /**
     * Gegebenen Text als Fehlermeldung anzeigen
     * @param text
     */
    private void showFehlermeldung(String text){
    	JOptionPane.showMessageDialog(null, text, "IOException while running", JOptionPane.WARNING_MESSAGE);
    }


	
	

}