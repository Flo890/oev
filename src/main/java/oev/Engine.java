package oev;

import java.awt.image.BufferedImage;


public class Engine{

    //BufferedImage[] inputArray;
    BufferedImage outputImage;
    int width;
    int height;
    BufferedImage firstFrame;
    
    int NrOfAdds;


    

    public Engine(BufferedImage ff){
    //    inputArray = a;
//        length=inputArray.length;
    	
        firstFrame=ff;
        outputImage=firstFrame;
        NrOfAdds = 0;

   //     width=inputArray[0].getWidth();
          width=ff.getWidth();
           System.out.println("Width:"+width);
    //    height=inputArray[0].getHeight();
          height=ff.getHeight();
           System.out.println("Height:"+height);
    }

    
    
    public BufferedImage maxValue2(int fkt, BufferedImage inpf){
        
        BufferedImage inputFrame = inpf;
       
        
            int x=0;

            switch(fkt){
            case 1:               
                
                for(int j=0; j<width; j++){  //Alle Zeilen durchlaufen
                    int y=0;
                    for(int k=0; k<height; k++){  //Alle Pixel durchlaufen
                    	
                    	outputImage.setRGB(x, y, FktLibrary.comparison1(outputImage.getRGB(x,y),inputFrame.getRGB(x,y)));
                    	y++;
                    }
                    x++;  
                    
                }                            //Ende Alle Pixel durchlaufen

                          	 
                    
            	break;
            	
            case 2:
            	for(int j=0; j<width; j++){  //Alle Zeilen durchlaufen
                    int y=0;
                    for(int k=0; k<height; k++){  //Alle Pixel durchlaufen
                    	
                    	outputImage.setRGB(x, y, FktLibrary.comparison2(outputImage.getRGB(x,y),inputFrame.getRGB(x,y)));
                    	 y++;
                    }
                    x++;
                   
                }                            //Ende Alle Pixel durchlaufen

                
            	break;
            	
            case 3:
            	for(int j=0; j<width; j++){  //Alle Zeilen durchlaufen
                    int y=0;
                    for(int k=0; k<height; k++){  //Alle Pixel durchlaufen
                    	
                    	outputImage.setRGB(x, y, FktLibrary.comparison3(outputImage.getRGB(x,y),inputFrame.getRGB(x,y)));
                    	y++;
                    }
                    x++;
                    
                }                            //Ende Alle Pixel durchlaufen

                
            	break;
            	
            }
            //Ende Alle Zeilen durchlaufen
            
         
        //System.out.println("ReturnImage:("+outputImage.getWidth()+"/"+outputImage.getHeight()+")");
        return outputImage;
        
    }
    
    public BufferedImage averageValue(BufferedImage inpf){
    	  System.out.println("averageValue aufgerufen");
    	BufferedImage inputFrame = inpf;
    	int x=0;
    	NrOfAdds++;
    	  System.out.println("NrOfAdds: "+NrOfAdds);
    	        
        for(int j=0; j<width; j++){  //Alle Zeilen durchlaufen
            int y=0;
            for(int k=0; k<height; k++){  //Alle Pixel durchlaufen
                
                                      
                    outputImage.setRGB(x, y, FktLibrary.comparison4(outputImage.getRGB(x,y),inputFrame.getRGB(x,y),NrOfAdds));
                    //System.out.println();
                    
                    
                   
                
                //System.out.println("Coordinate:("+x+"/"+y+")");
                y++;
            }                            //Ende Alle Pixel durchlaufen

            x++;
        }                             //Ende Alle Zeilen durchlaufen
        
     
    //System.out.println("ReturnImage:("+outputImage.getWidth()+"/"+outputImage.getHeight()+")");
    	return outputImage;
    }
    
}