package oev.colorprocessing;

import java.awt.*;

/**
 * Created by Flo on 09/09/2016.
 */
public class LightedBrightnessCCF implements ColorComparisonFunction {
    @Override
    public Integer compare(Integer color1, Integer color2, Integer nrOfAdds) {
        //Beleuchtung
        Color c5 = new Color(color1);
        Color c6 = new Color(color2);


        if(((Math.max(c5.getRed(),Math.max(c5.getGreen(),c5.getBlue()))+Math.min(c5.getRed(),Math.min(c5.getGreen(),c5.getBlue())))/2) < ((Math.max(c6.getRed(),Math.max(c6.getGreen(),c6.getBlue()))+Math.min(c6.getRed(),Math.min(c6.getGreen(),c6.getBlue())))/2)){
            return c6.getRGB();
        }
        else{
            return c5.getRGB();
        }
    }
}
