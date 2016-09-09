package oev.colorprocessing;

import java.awt.*;

/**
 * Created by Flo on 09/09/2016.
 */
public class AverageBrightnessCCF implements ColorComparisonFunction {
    @Override
    public Integer compare(Integer color1, Integer color2, Integer nrOfAdds) {

        Color c7 = new Color(color1);
        Color c8 = new Color(color2);

        return new Color(
                (int)Math.round(((nrOfAdds/(nrOfAdds+1.0))*c7.getRed())+((1.0/(nrOfAdds+1.0))*c8.getRed())),
                (int)Math.round(((nrOfAdds/(nrOfAdds+1.0))*c7.getGreen())+((1.0/(nrOfAdds+1.0))*c8.getGreen())),
                (int)Math.round(((nrOfAdds/(nrOfAdds+1.0))*c7.getBlue())+((1.0/(nrOfAdds+1.0))*c8.getBlue()))).getRGB();
    }
}
