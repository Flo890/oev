package oev.ioservices.threads;

import oev.colorprocessing.*;
import oev.model.ColorFunction;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;


public class Engine {

  private final int width;
  private final int height;
  private final ColorComparisonFunction colorComparisonFunction;
  private final int threadsAmount;


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

    ExecutorService executor = Executors.newFixedThreadPool(threadsAmount);
    Map<FutureTask<int[]>,Integer> taskList = new HashMap<>();



    for(int threadIndex = 0; threadIndex<threadsAmount; threadIndex++) {
      int startY = threadIndex * blocksize;
      int endY = startY + blocksize;
      if (threadIndex == threadsAmount-1) {
        endY = height;
      }
      FutureTask<int[]> futureTask_1 = new FutureTask<int[]>(new EngineThreadV2(inputFrame, outputImage, width, height, colorComparisonFunction, startY, endY));
      taskList.put(futureTask_1,startY);
      executor.execute(futureTask_1);
    }


    // wait until all threads finished. get is a blocking call
    for(Map.Entry<FutureTask<int[]>,Integer> aTask : taskList.entrySet()){
      try {
        int[] rgbArray = aTask.getKey().get();
        outputImage.setRGB(0, aTask.getValue(), width, blocksize, rgbArray, 0, width);
      } catch (InterruptedException |  ExecutionException e) {
        throw new RuntimeException(e);
      }
    }
    executor.shutdown();
    System.out.println("ms taken for frame: "+(new Date().getTime()-starttime));

  }

}