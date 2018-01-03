package oev.ioservices.binningalgo.renderer;

import oev.ioservices.binningalgo.model.BinnableColor;
import oev.ioservices.binningalgo.model.BinnedImage;
import oev.ioservices.binningalgo.properties.LazyProperty;

import java.awt.image.BufferedImage;
import java.util.Map;

public interface BinnedImageRenderer {

    BufferedImage renderImage(BinnedImage binnedImage, Map<LazyProperty,Object> lazyProperties);

    int renderColor(BinnableColor binnableColor, Map<LazyProperty,Object> lazyProperties);

}
