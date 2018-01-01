package oev.ioservices.threads;

import oev.colorprocessing.ColorComparisonFunction;

import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class EngineThreadV2 implements Callable<Void> {

    private final int width;
    private final int height;
    private final ColorComparisonFunction colorComparisonFunction;
    private final Function<Integer,Boolean> isThisMyYDeterminer;
    private final BufferedImage inputFrame;
    private final BufferedImage outputFrame;

    public EngineThreadV2(BufferedImage inputFrame, BufferedImage outputFrame, int width, int height, ColorComparisonFunction colorComparisonFunction, Function<Integer, Boolean> isThisMyYDeterminer) {
        this.width = width;
        this.height = height;
        this.isThisMyYDeterminer = isThisMyYDeterminer;
        this.colorComparisonFunction = colorComparisonFunction;
        this.inputFrame = inputFrame;
        this.outputFrame = outputFrame;
    }

    @Override
    public Void call() throws Exception {
            int y = 0;
            for (int k = 0; k < height; k++) {
                if(isThisMyYDeterminer.apply(y)) {
                    int x = 0;
                    for (int j = 0; j < width; j++) {
                    if (colorComparisonFunction.compare(outputFrame.getRGB(x, y), inputFrame.getRGB(x, y))) {
                        outputFrame.setRGB(x, y, inputFrame.getRGB(x, y));
                    }
                }
                x++;
            }
            y++;
        }

        return null;
    }
}
