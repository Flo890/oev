package oev.ioservices;

import oev.ioservices.threads.Engine;
import oev.mvc.Model;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class VideoProcessingService extends SwingWorker implements FrameProcessingService{

    private int fktNr;
    private BufferedImage lastResultImage;
    private int fileIndex;
    private String resPath;
    private Model model;

    public VideoProcessingService(){

    }

    public void setOptionsAndPrepareExecution(Integer function, Integer effectLengthInFrames){
        //Files have to be stored in folder VidSource, named "1.png" to for example "42.png"
        //All files must have the same resolution!!


        fktNr=function;

    }
    

    public void load2(){

        fileIndex = startFrame;
       
        Engine engine = null;
		try {
			engine = new Engine(lastResultImage=ImageIO.read(new File(srcPath+"\\"+Integer.toString(startFrame)+".png")) );
		} catch (IOException e) {
			showFehlermeldung("Could not load file "+srcPath+"\\"+Integer.toString(startFrame)+".png");
			e.printStackTrace();
		}
        
        
        System.out.println("Loading first file "+startFrame+".png");
        model.setNewAction("oev.ioservices.VideoProcessingService: loading "+startFrame+".png");
        save();
        fileIndex = startFrame;//TODO hier war +1 ; hatte die einen sinn?

        for(int i = 0; i<(anzahlBilder-1); i++){

        	try {
				lastResultImage=engine.findNewColorForEachPixel(fktNr, ImageIO.read(new File(srcPath+"\\"+Integer.toString(fileIndex)+".png")));
			} catch (IOException e) {
				showFehlermeldung("Could not read file "+srcPath+"\\"+Integer.toString(fileIndex)+".png");
				e.printStackTrace();
			}
            	
            System.out.println("Loading file "+(fileIndex)+".png");
            model.setNewAction("oev.ioservices.VideoProcessingService: Loading file "+Integer.toString(fileIndex)+".png");
            save();
            fileIndex++;
        }

    }

    

    public void save(){
        File saveFile = new File(resPath+"\\resultIMG"+(fileIndex-startFrame+1)+".png");
        
        try {
			ImageIO.write(lastResultImage, "png", saveFile);
		} catch (IOException e) {
			showFehlermeldung("Could not save file "+resPath+"\\resultIMG"+(fileIndex-startFrame+1)+".png");
			e.printStackTrace();
		}
        
        System.out.println("File "+(fileIndex-startFrame+1)+" saved");
        model.setNewAction("oev.ioservices.VideoProcessingService: Saving file "+(fileIndex-startFrame+1)+".png");
        model.increaseProgress();

    }
    
    
    public void setResPath(String r){
    	resPath=r;
    }
    
    
    public void setModel(Model m){
    	model = m;
    }

    /**
     * Muss statt loadAndProcessAllFrames() aufgerufen werden damit die Berechnung im Hintergrund l�uft und GUI nicht einfriert
     */
	@Override
	protected Object doInBackground() throws Exception {
		load2();
		return null;
	}
	
	
	 /**
     * Gegebenen Text als Fehlermeldung anzeigen
     * @param text
     */
    private void showFehlermeldung(String text){
    	JOptionPane.showMessageDialog(null, text, "IOException while running", JOptionPane.WARNING_MESSAGE);
    }
	
	
}