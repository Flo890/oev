package oev.ioservices.threads;

import oev.colorprocessing.*;
import oev.model.ColorFunction;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;


public class Engine {

  private final int width;
  private final int height;
  private final ColorComparisonFunction colorComparisonFunction;
  private final int threadsAmount;
  private final Map<EngineThreadV2,Boolean> threads = new HashMap<>();
  private boolean everyThreadFinished = false;


  public Engine(int width, int height, ColorFunction function, int threads) {
    this.width = width;
    this.height = height;
    this.threadsAmount = threads;

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
    long starttime = new Date().getTime();

    int blocksize = height/threadsAmount;

    for(int threadIndex = 0; threadIndex<threadsAmount; threadIndex++) {
      int startY = threadIndex * blocksize;
      int endY = startY + blocksize;
      if (threadIndex == threadsAmount-1) {
        endY = height;
      }
      EngineThreadV2 thread = new EngineThreadV2(inputFrame, outputImage, width, height, colorComparisonFunction, startY, endY, this, threadIndex);
      threads.put(thread,false);
      thread.start();
    }

    while(true){
      if(everyThreadFinished){
        break;
      } else {
        try {
          Thread.sleep(50);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }

    System.out.println("ms taken for frame: "+(new Date().getTime()-starttime));
  }

  public void threadFinishedCallback(EngineThreadV2 thread){
    threads.put(thread,true);
    boolean everyThreadFinished = false;
    for(Boolean aThreadHasFinished : threads.values()){
      everyThreadFinished = aThreadHasFinished;
    }
    this.everyThreadFinished = everyThreadFinished;
  }

}