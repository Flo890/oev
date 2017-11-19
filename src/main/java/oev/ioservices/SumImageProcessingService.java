package oev.ioservices;

import oev.ioservices.threads.Engine;
import oev.mvc.Model;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class SumImageProcessingService extends AbstractFrameProcessingService implements FrameProcessingService {

  public SumImageProcessingService(Model model) {
    super(model);
  }

  /**
   * loads each frame after another, processes it, and saves the result image
   */
  public void loadAndProcessAllFrames() {

    model.increaseProgress();

    BufferedImage outputImage = new BufferedImage(jobMetaData.getWidth(), jobMetaData.getHeight(), BufferedImage.TYPE_INT_RGB);
    BufferedImage nextFrame = null;
    while((nextFrame = ioService.getNextFrame())!=null){
      engine.findNewColorForEachPixel(nextFrame, outputImage);
      model.increaseProgress();
    }

    ioService.save(outputImage, "resultImg.png");
  }




}

