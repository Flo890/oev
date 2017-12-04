package oev.ioservices.threads;

import oev.colorprocessing.*;
import oev.model.ColorFunction;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class EngineMultiRecentImages extends Engine {

    private final int lastImgsParam;

    private final Map<Integer,BufferedImage> inputImages;
    private int currentInputImgIndex;


    /**
     *
     * @param width
     * @param height
     * @param function
     * @param lastImgsParam for brightness comparison, an avg color of the last lastImgsParam images will be taken into account
     */
    public EngineMultiRecentImages(int width, int height, ColorFunction function, int lastImgsParam) {

        super(width,height,function);

        this.lastImgsParam = lastImgsParam;
        this.inputImages = new HashMap<>();

    }


    public void findNewColorForEachPixel(BufferedImage inputFrame, BufferedImage outputImage) {
        // remove old image from memory map
        if(inputImages.containsKey(currentInputImgIndex-lastImgsParam)) {
            inputImages.remove(currentInputImgIndex - lastImgsParam);
        }

        // /add next image to memory map
        inputImages.put(currentInputImgIndex, inputFrame);

        int counterStart = Math.max(currentInputImgIndex-(lastImgsParam-1),0); // do not start below 0

        int x = 0;
        NrOfAdds++;
        for (int j = 0; j < width; j++) {  //Alle Zeilen durchlaufen
            int y = 0;
            for (int k = 0; k < height; k++) {  //Alle Pixel durchlaufen

                // avg over last 3 images
                //int colorAccumulated = 0;
                int r = 0;
                int g = 0;
                int b = 0;
                for(int i = counterStart; i<=currentInputImgIndex; i++){
                    //colorAccumulated += inputImages.get(i).getRGB(x,y);
                    r+=new Color(inputImages.get(i).getRGB(x,y)).getRed();
                    g+=new Color(inputImages.get(i).getRGB(x,y)).getGreen();
                    b+=new Color(inputImages.get(i).getRGB(x,y)).getBlue();
                }

                // if the input frame is brighter as the avg over n last images, set the new color. Otherwise keep the old one. Never use the avg color!
                //TODO performance
                int brighterColor = colorComparisonFunction.compare(new Color(r/lastImgsParam,g/lastImgsParam,b/lastImgsParam).getRGB(), inputFrame.getRGB(x,y), NrOfAdds);
                if(brighterColor == inputFrame.getRGB(x,y)) {
                    outputImage.setRGB(x, y, inputFrame.getRGB(x,y));
                }
                y++;
            }
            x++;

        }                            //Ende Alle Pixel durchlaufen

        currentInputImgIndex++;

    }

}
