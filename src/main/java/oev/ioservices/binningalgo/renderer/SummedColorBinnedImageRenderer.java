package oev.ioservices.binningalgo.renderer;

import oev.ioservices.binningalgo.model.BinnableColor;
import oev.ioservices.binningalgo.properties.LazyProperty;

import java.util.Map;

public class SummedColorBinnedImageRenderer extends AbstractBinnedImageRenderer {

    public SummedColorBinnedImageRenderer(int width, int height) {
        super(width, height);
    }

    @Override
    public int renderColor(BinnableColor binnableColor, Map<LazyProperty,Object> lazyProperties) {
        int red = 0;
        for(Integer colorVal : binnableColor.reds){
            red += colorVal;
        }
        int green = 0;
        for(Integer colorVal : binnableColor.greens){
            green += colorVal;
        }
        int blue = 0;
        for(Integer colorVal : binnableColor.blues){
            blue += colorVal;
        }

        int normalizationDividend = (int)lazyProperties.get(LazyProperty.NORMALIZATION_DIVIDEND);

        int rNormalized = Math.min(red/normalizationDividend,255);
        int gNormalized = Math.min(green/normalizationDividend,255);
        int bNormalized = Math.min(blue/normalizationDividend,255);
        // unwrapped from Color constructor
        return ((255 & 0xFF) << 24) |
                ((rNormalized & 0xFF) << 16) |
                ((gNormalized & 0xFF) << 8)  |
                ((bNormalized & 0xFF) << 0);
    }
}
