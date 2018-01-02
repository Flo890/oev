package oev.ioservices.binningalgo.model;

import java.util.HashMap;
import java.util.Map;

public class ExtendedBinnableColor {

    private Map<Integer,Integer> reds = new HashMap<>();
    private Map<Integer,Integer> greens = new HashMap<>();
    private Map<Integer,Integer> blues = new HashMap<>();

    public void addColorRGB(int frameindex, int rgb) {
        // unwrapped getRed(), ... from Color class
        reds.put(frameindex, (rgb >> 16) & 0xFF);
        greens.put(frameindex, (rgb >> 8) & 0xFF);
        blues.put(frameindex, (rgb >> 0) & 0xFF);
    }

    public int getRgbNormalized(int normalizationDividend) {
        int red = 0;
        for(Integer colorVal : reds.values()){
            red += colorVal;
        }
        int green = 0;
        for(Integer colorVal : greens.values()){
            green += colorVal;
        }
        int blue = 0;
        for(Integer colorVal : blues.values()){
            blue += colorVal;
        }
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
