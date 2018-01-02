package oev.ioservices.binningalgo.model;

import java.util.Objects;

public class PixelKey {

    private final int x;
    private final int y;


    public PixelKey(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PixelKey pixelKey = (PixelKey) o;
        return x == pixelKey.x &&
                y == pixelKey.y;
    }

    @Override
    public int hashCode() {

        return Objects.hash(x, y);
    }
}
