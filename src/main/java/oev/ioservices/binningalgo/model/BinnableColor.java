package oev.ioservices.binningalgo.model;

public class BinnableColor {

    public final int[] reds;
    public final int[] greens;
    public final int[] blues;

    public BinnableColor(int maxAmountImages) {
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

}
