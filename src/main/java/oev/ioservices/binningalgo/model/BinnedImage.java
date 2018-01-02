package oev.ioservices.binningalgo.model;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class BinnedImage {

    private final Map<PixelKey,ExtendedBinnableColor> bins;
    private final int width;
    private final int height;
    private int imageAmountInBins = 0;

    public BinnedImage(int width, int height) {
        bins = new HashMap<>();
        this.width = width;
        this.height = height;
        // create empty bins
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                bins.put(new PixelKey(x,y), new ExtendedBinnableColor());
            }
        }
    }

    public void addBufferedImageToBins(int frameIndex, BufferedImage image) {
        for (int x = 0; x<width; x++) {
            for (int y = 0; y<height; y++) {
                bins.get(new PixelKey(x,y)).addColorRGB(frameIndex, image.getRGB(x,y));
            }
        }
        imageAmountInBins++;
    }

    public BufferedImage getImageNormalized(int normalizationDividend) {
        BufferedImage image = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x<width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x,y,bins.get(new PixelKey(x,y)).getRgbNormalized(normalizationDividend));
            }
        }
        return image;
    }

    public int getImageAmountInBins() {
        return imageAmountInBins;
    }
}
