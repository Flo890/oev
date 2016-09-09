package oev.ioservices.threads;

import oev.colorprocessing.*;

import java.awt.*;
import java.awt.image.BufferedImage;


public class Engine{

    private BufferedImage outputImage;
    private int width;
    private int height;
    private BufferedImage firstFrame;
    
    int NrOfAdds;


    

    public Engine(BufferedImage ff){

        firstFrame=ff;
        outputImage=firstFrame;
        NrOfAdds = 0;

          width=ff.getWidth();
           System.out.println("Width:"+width);
          height=ff.getHeight();
           System.out.println("Height:"+height);
    }

    
    
    public BufferedImage findNewColorForEachPixel(int fkt, BufferedImage inpf){
        
        BufferedImage inputFrame = inpf;
        ColorComparisonFunction colorComparisonFunction = null;
        switch(fkt) {
            case 1:
                colorComparisonFunction = new PhysicalBrightnessCCF();
                break;
            case 2:
                colorComparisonFunction = new HumanPerceivedBrightnessCCF();
                break;
            case 3:
                colorComparisonFunction = new LightedBrightnessCCF();
                break;
            case 4:
                colorComparisonFunction = new AverageBrightnessCCF();
                break;
        }

        
            int x=0;
        NrOfAdds++;
                for(int j=0; j<width; j++){  //Alle Zeilen durchlaufen
                    int y=0;
                    for(int k=0; k<height; k++){  //Alle Pixel durchlaufen
                    	
                    	outputImage.setRGB(x, y, colorComparisonFunction.compare(outputImage.getRGB(x,y),inputFrame.getRGB(x,y),NrOfAdds));
                    	y++;
                    }
                    x++;  
                    
                }                            //Ende Alle Pixel durchlaufen

        return outputImage;
        
    }

}