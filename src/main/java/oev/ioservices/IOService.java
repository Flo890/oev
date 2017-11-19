package oev.ioservices;

import oev.mvc.Model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class IOService {

  private final File[] sourceFiles;
  private final String resPath;
  private final Model model;

  // working data
  private int fileIndex = 0;

  public IOService(File[] sourceFiles, String resPath, Model model) {
    this.sourceFiles = sourceFiles;
    this.resPath = resPath;
    this.model = model;
  }

  public JobMetaData fetchJobMetaData(){
    BufferedImage bufferedImage = null;
    try {
      bufferedImage = ImageIO.read(sourceFiles[0]);
    } catch (IOException e) {
      model.showErrorMessage("could not read file "+sourceFiles[0].getName()+" "+e.getMessage());
    }
    JobMetaData jobMetaData = new JobMetaData(
            bufferedImage.getWidth(),
            bufferedImage.getHeight(),
            sourceFiles.length
    );
    return jobMetaData;
  }

  /**
   *
   * @return null if no more frame left
   */
  public BufferedImage getNextFrame(){
    return getFrameByIndex(fileIndex++);
  }

  public BufferedImage getFrameByIndex(int frameIndex){
    if(frameIndex < sourceFiles.length){
      File file = sourceFiles[frameIndex];
      model.setNewAction("reading file "+file.getName());
      try {
        return ImageIO.read(file);
      } catch (IOException e) {
        model.showErrorMessage("could not read file "+file.getName()+" "+e.getMessage());
      }
    }
    return null;
  }

  /**
   * outputFile speichern
   */
  public void save(BufferedImage outputImage, String filename) {
    File saveFile = new File(resPath + "\\" + filename);

    try {
      ImageIO.write(outputImage, "png", saveFile);
    } catch (IOException e) {
      model.showErrorMessage("Fehler beim Speichern von resultIMG.png\n" + e.getMessage());
    }

    model.setNewAction("Saved  file resultIMG.png");
  }

  /**
   * filename is automatically incremented according to the current fileIndex
   * @param outputImage
   */
  public void save(BufferedImage outputImage) {
    String fileName = "resultImg"+fileIndex+".png";
    save(outputImage,fileName);
  }

}
