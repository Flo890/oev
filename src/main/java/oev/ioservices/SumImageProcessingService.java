package oev.ioservices;

import oev.ioservices.threads.Engine;
import oev.mvc.Model;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class SumImageProcessingService extends SwingWorker implements FrameProcessingService {

    private String resPath;
    private Model model;

    public void setOptionsAndPrepareExecution(Integer function, Integer effectLengthInFrames){
        //Files have be stored in folder "source" named "1.png" to for example "42.png"
        //All files must have the same resolution!!


    }

  /**
   * loads each frame after another, processes it, and saves the result image
   */
  public void loadAndProcessAllFrames(){
    BufferedImage actualFrame;

    System.out.println("oev.ioservices.IOMachine: Try loading file "+srcPath+"\\"+startFrame+".png");
    model.setNewAction("IOM: Loading file "+startFrame+".png");

    try{
      engine = new Engine(ImageIO.read(new File(srcPath+"\\"+Integer.toString(startFrame)+".png")));  //firstFrame übergeben!
    }catch(IOException e){
      showErrorMessage("Fehler beim Laden von "+startFrame+".png\n"+e.getMessage());
    }
    model.increaseProgress();

    BufferedImage outputFile = null;
    int filenr=startFrame; //TODO hier war +1  ; hatte die einen sinn?
    for(int i=0; i<(amount-1); i++){
      System.out.println("oev.ioservices.IOMachine: Try loading file "+srcPath+"\\"+filenr+".png");
      model.setNewAction("IOM: Loading file "+filenr+".png");

      actualFrame = null;
      try{
        actualFrame=ImageIO.read(new File(srcPath+"\\"+Integer.toString(filenr)+".png"));
      } catch(IOException e){
        showErrorMessage("Fehler beim Laden von "+filenr+".png\n"+e.getMessage());
      }

      model.increaseProgress();
      filenr++;

      outputFile = engine.findNewColorForEachPixel(fkt,actualFrame);
      System.out.println("FileNr "+filenr);
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
      showErrorMessage("Fehler beim Speichern von resultIMG.png\n"+e.getMessage());
    }

    model.setNewAction("IOM: Saved  file resultIMG.png");

    System.out.println("File saved");

  }

  private void showErrorMessage(String text){
    JOptionPane.showMessageDialog(null, text, "IOException while running", JOptionPane.WARNING_MESSAGE);
  }
    
    public void setResPath(String r){
    	resPath=r;
    }
    
    public void setModel(Model m){
    	model=m;
    }

  @Override
	protected Object doInBackground() throws Exception {
		loadAndProcessAllFrames();
		return null;
	}
    
}

