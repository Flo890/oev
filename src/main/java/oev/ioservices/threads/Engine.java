package oev.ioservices.threads;

import oev.colorprocessing.*;
import oev.model.ColorFunction;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
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

    ExecutorService executor = Executors.newFixedThreadPool(threadsAmount);
    List<FutureTask<Void>> taskList = new ArrayList<FutureTask<Void>>();

    for(int threadIndex = 0; threadIndex<threadsAmount; threadIndex++) {
      final int thisThreadsIndex = threadIndex;
      FutureTask<Void> futureTask_1 = new FutureTask<Void>(new EngineThreadV2(inputFrame, outputImage, width, height, colorComparisonFunction ,new Function(){
        /**
        a individual function for each thread, which tells whether an y (=image row) is this threads' work
         @param o Integer, the y index (= a row index)
         @return boolean. true if this y index should be processed by this thread
         */
        @Override
        public Object apply(Object o) {
          return (((Integer) o) % threadsAmount == thisThreadsIndex);
        }
      }));
      taskList.add(futureTask_1);
      executor.execute(futureTask_1);
    }


    // wait until all threads finished. get is a blocking call
    for(FutureTask<Void> aTask : taskList){
      try {
        aTask.get();
      } catch (InterruptedException |  ExecutionException e) {
       throw new RuntimeException(e);
      }
    }
    executor.shutdown();

  }

}