package oev.colorprocessing;

/**
 * Created by Flo on 09/09/2016.
 */
public class HumanPerceivedBrightnessCCF implements ColorComparisonFunction {

    /**
     * returns the brightest color (=where the components rgb value sum is higher) with the following weighting of the 3 components:
     * red: 0,299
     * green: 0,587
     * blue: 0,114
     * weighted like this, the components sum describes a brightness value which seems very natural to humans perception
     * @param color1
     * @param color2
     * @return
     */
    @Override
    public Integer compare(Integer color1, Integer color2, Integer nrOfAdds) {

        //just unwrapped some method-code of the java Color class to improve performance (=> don't need to create Color objects anymore)

        if(((((color1 >> 16) & 0xFF)*299)+(((color1 >> 8) & 0xFF)*587)+(((color1 >> 0) & 0xFF)*114)) < ((((color2 >> 16) & 0xFF)*299)+(((color2 >> 8) & 0xFF)*587)+(((color2 >> 0) & 0xFF)*114))){
            return color2;
        }
        else{
            return color1;
        }

    }
}
