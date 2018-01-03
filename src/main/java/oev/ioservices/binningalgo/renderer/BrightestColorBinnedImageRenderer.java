package oev.ioservices.binningalgo.renderer;

import oev.colorprocessing.ColorComparisonFunction;
import oev.ioservices.binningalgo.model.BinnableColor;
import oev.ioservices.binningalgo.properties.LazyProperty;

import java.awt.*;
import java.util.Map;

public class BrightestColorBinnedImageRenderer extends AbstractBinnedImageRenderer {

    private static final int BLACK = -16777216;

    public BrightestColorBinnedImageRenderer(int width, int height) {
        super(width, height);
    }

    public int renderColor(BinnableColor binnableColor, Map<LazyProperty,Object> lazyProperties){
        int brightestColor = BLACK;

        for(int i = 0; i<binnableColor.reds.length; i++){
            int color = ((255 & 0xFF) << 24) |
                    ((binnableColor.reds[i] & 0xFF) << 16) |
                    ((binnableColor.greens[i] & 0xFF) << 8)  |
                    ((binnableColor.blues[i] & 0xFF) << 0);

            if (brightestColor == BLACK || ((ColorComparisonFunction)lazyProperties.get(LazyProperty.COLOR_FUNCTION)).compare(brightestColor, color)) {
                brightestColor = color;
            }
        }

        return brightestColor;
    }
}
