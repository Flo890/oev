package oev.colorprocessing;

import java.awt.*;

/**
 * Created by Flo on 09/09/2016.
 */
public class PhysicalBrightnessCCF implements ColorComparisonFunction {
    @Override
    public Integer compare(Integer color1, Integer color2, Integer nrOfAdds) {
        //physische Helligkeit, nicht sehr nah an Wahrnehmung

        Color c1 = new Color(color1);
        Color c2 = new Color(color2);

        if( ((c1.getRed()+c1.getGreen()+c1.getBlue())/3) < ((c2.getRed()+c2.getGreen()+c2.getBlue())/3) )
        {
            return c2.getRGB();
        }
        else{
            return c1.getRGB();
        }
    }
}
