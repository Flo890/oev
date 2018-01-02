package oev.ioservices.threads;

import oev.colorprocessing.ColorComparisonFunction;

import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;

public class EngineThreadV2 implements Callable<int[]> {

    private final int width;
    private final int height;
    private final ColorComparisonFunction colorComparisonFunction;
    private final int startY;
    private final int endY;
    private final BufferedImage inputFrame;
    private final BufferedImage outputFrame;

    public EngineThreadV2(BufferedImage inputFrame, BufferedImage outputFrame, int width, int height, ColorComparisonFunction colorComparisonFunction, int startY, int endY) {
        this.width = width;
        this.height = height;
        this.colorComparisonFunction = colorComparisonFunction;
        this.inputFrame = inputFrame;
        this.outputFrame = outputFrame;
        this.startY = startY;
        this.endY = endY;
    }

    @Override
    public int[] call() {
        try {
            int[] rgbArray = new int[width * (endY - startY)];
            for (int y = startY; y < endY; y++) {
                for (int x = 0; x < width; x++) {
                    if (colorComparisonFunction.compare(outputFrame.getRGB(x, y), inputFrame.getRGB(x, y))) {
                        rgbArray[(y-startY)*width + x] = inputFrame.getRGB(x, y);
                    } else {
                        rgbArray[(y-startY)*width + x] = outputFrame.getRGB(x, y);
                    }
                }
            }
            return rgbArray;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
