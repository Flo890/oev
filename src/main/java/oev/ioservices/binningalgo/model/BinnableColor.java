package oev.ioservices.binningalgo.model;

public class BinnableColor {

    private int r;
    private int g;
    private int b;

    public void addColorRGB(int rgb) {
        // unwrapped getRed(), ... from Color class
        r += (rgb >> 16) & 0xFF;
        g += (rgb >> 8) & 0xFF;
        b += (rgb >> 0) & 0xFF;
    }

    public int getRgbNormalized(int normalizationDividend) {
        int rNormalized = Math.min(r/normalizationDividend,255);
        int gNormalized = Math.min(g/normalizationDividend,255);
        int bNormalized = Math.min(b/normalizationDividend,255);
        // unwrapped from Color constructor
        return ((255 & 0xFF) << 24) |
                ((rNormalized & 0xFF) << 16) |
                ((gNormalized & 0xFF) << 8)  |
                ((bNormalized & 0xFF) << 0);
    }
}
