package oev.colorprocessing;

/**
 * Created by Flo on 09/09/2016.
 */
public interface ColorComparisonFunction {

    /**
     * compares the given color by a specific rule and returns one of them (or a new created one, as the average function does)
     * @param color1
     * @param color2
     * @param nrOfAdds a sum value, used for average calculations
     * @return
     */
    Integer compare(Integer color1, Integer color2, Integer nrOfAdds);

}
