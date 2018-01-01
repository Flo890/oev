package oev.ioservices.threads;

import oev.colorprocessing.ColorComparisonFunction;

import java.awt.image.BufferedImage;

public class EngineThreadV2 extends Thread {

    private final int width;
    private final int height;
    private final ColorComparisonFunction colorComparisonFunction;
    private final int startY;
    private final int endY;
    private final BufferedImage inputFrame;
    private final BufferedImage outputFrame;
    private final Engine engine;
    private final int myThreadIndex;

    public EngineThreadV2(BufferedImage inputFrame, BufferedImage outputFrame, int width, int height, ColorComparisonFunction colorComparisonFunction, int startY, int endY, Engine engine, int myThreadIndex) {
        this.width = width;
        this.height = height;
        this.startY = startY;
        this.endY = endY;
        this.colorComparisonFunction = colorComparisonFunction;
        this.inputFrame = inputFrame;
        this.outputFrame = outputFrame;
        this.engine = engine;
        this.myThreadIndex = myThreadIndex;
    }

    @Override
    public void run() {
        for (int y = startY; y < endY; y++) {
                for (int x = 0; x < width; x++) {
                    if (colorComparisonFunction.compare(outputFrame.getRGB(x, y), inputFrame.getRGB(x, y))) {
                        outputFrame.setRGB(x, y, inputFrame.getRGB(x, y));
                    }
                }
            }
            engine.threadFinishedCallback(this);
    }


}
