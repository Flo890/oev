package oev.ioservices.binningalgo.renderer;

import oev.ioservices.binningalgo.model.BinnableColor;
import oev.ioservices.binningalgo.model.BinnedImage;
import oev.ioservices.binningalgo.properties.LazyProperty;

import java.awt.image.BufferedImage;
import java.util.Map;

public abstract class AbstractBinnedImageRenderer implements BinnedImageRenderer {

    private final int width;
    private final int height;

    protected AbstractBinnedImageRenderer(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public BufferedImage renderImage(BinnedImage binnedImage, Map<LazyProperty,Object> lazyProperties) {
        BufferedImage image = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x<width; x++) {
            for (int y = 0; y < height; y++) {
                BinnableColor binnableColor = binnedImage.getBins().get(BinnedImage.getPixelHash(x,y));
                image.setRGB(x,y,renderColor(binnableColor, lazyProperties));
            }
        }
        return image;
    }

}
