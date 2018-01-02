package oev.ioservices.binningalgo.model;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BinnedImage {

    private final Map<Integer,BinnableColor> bins;
    private final int width;
    private final int height;
    private int imageAmountInBins = 0;

    public BinnedImage(int width, int height, int maxBinSize) {
        bins = new HashMap<>();
        this.width = width;
        this.height = height;
        // create empty bins
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                bins.put(getPixelHash(x,y), new BinnableColor(maxBinSize));
            }
        }
    }

    public void addBufferedImageToBins(int frameIndex, BufferedImage image) {
        long starttime = new Date().getTime();

        for (int x = 0; x<width; x++) {
            for (int y = 0; y<height; y++) {
                bins.get(getPixelHash(x,y)).addColorRGB(frameIndex, image.getRGB(x,y));
            }
        }

        System.out.println("ms taken for frame "+frameIndex+": "+(new Date().getTime()-starttime));
        imageAmountInBins++;
    }

    public BufferedImage getImageNormalized(int normalizationDividend) {
        BufferedImage image = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x<width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x,y,bins.get(getPixelHash(x,y)).getRgbNormalized(normalizationDividend));
            }
        }
        return image;
    }

    public int getImageAmountInBins() {
        return imageAmountInBins;
    }

    public static int getPixelHash(int x, int y){
        /*
        10000 (decimal) has 14 digits when written binary. So assuming that x and y are smaller than 10000, we can sum them by shifting x 14 times left
        http://vojtechruzicka.com/bit-manipulation-java-bitwise-bit-shift-operations/
         */
        return (x << 14) + y;
    }
}
