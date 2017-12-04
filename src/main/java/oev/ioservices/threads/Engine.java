package oev.ioservices.threads;

import oev.colorprocessing.*;
import oev.model.ColorFunction;

import java.awt.image.BufferedImage;


public class Engine {

  private final int width;
  private final int height;
  private final ColorComparisonFunction colorComparisonFunction;


  public Engine(int width, int height, ColorFunction function) {

    this.width = width;
    this.height = height;

    switch (function) {
      case PHYSICAL_BRIGHTNESS:
        colorComparisonFunction = new PhysicalBrightnessCCF();
        break;
      case HUMAN_PERCEIVED_BRIGHTNESS:
        colorComparisonFunction = new HumanPerceivedBrightnessCCF();
        break;
      case LIGHTED_BRIGHTNESS:
        colorComparisonFunction = new LightedBrightnessCCF();
        break;
      default:
        colorComparisonFunction = null;
        throw new IllegalStateException("function unknown: "+function);
    }

  }


  public void findNewColorForEachPixel(BufferedImage inputFrame, BufferedImage outputImage) {

    int x = 0;
    for (int j = 0; j < width; j++) {  //Alle Zeilen durchlaufen
      int y = 0;
      for (int k = 0; k < height; k++) {  //Alle Pixel durchlaufen
        if(colorComparisonFunction.compare(outputImage.getRGB(x, y), inputFrame.getRGB(x, y))) {
          outputImage.setRGB(x, y, inputFrame.getRGB(x,y));
        }
        y++;
      }
      x++;

    }                            //Ende Alle Pixel durchlaufen

  }

}