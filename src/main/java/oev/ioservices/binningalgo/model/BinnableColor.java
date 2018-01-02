package oev.ioservices.binningalgo.model;

public class BinnableColor {

    private final int[] reds;
    private final int[] greens;
    private final int[] blues;

    BinnableColor(int maxAmountImages) {
        reds = new int[maxAmountImages];
        greens = new int[maxAmountImages];
        blues = new int[maxAmountImages];
    }

    public void addColorRGB(int frameindex, int rgb) {
        // unwrapped getRed(), ... from Color class
        reds[frameindex] = (rgb >> 16) & 0xFF;
        greens[frameindex] = (rgb >> 8) & 0xFF;
        blues[frameindex] = (rgb >> 0) & 0xFF;
    }

    public int getRgbNormalized(int normalizationDividend) {
        int red = 0;
        for(Integer colorVal : reds){
            red += colorVal;
        }
        int green = 0;
        for(Integer colorVal : greens){
            green += colorVal;
        }
        int blue = 0;
        for(Integer colorVal : blues){
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
