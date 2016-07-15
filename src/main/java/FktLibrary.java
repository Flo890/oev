import java.awt.*;


public class FktLibrary{

    

    public static int comparison1(int intc1, int intc2){
        //physische Helligkeit, nicht sehr nah an Wahrnehmung

        Color c1 = new Color(intc1);
        Color c2 = new Color(intc2);

        //System.out.println("C1 Red:"+c1.getRed()+" Green:"+c1.getGreen()+" Blue:"+c1.getBlue());
        //System.out.println("C2 Red:"+c2.getRed()+" Green:"+c2.getGreen()+" Blue:"+c2.getBlue());
        //System.out.println();

        if( ((c1.getRed()+c1.getGreen()+c1.getBlue())/3) < ((c2.getRed()+c2.getGreen()+c2.getBlue())/3) )
        {
            return c2.getRGB();
        }
        else{
            return c1.getRGB();
        }
        
    }

    public static int comparison2(int intc3, int intc4){
        //Wahrgenommen, Variante 1

        if(((((intc3 >> 16) & 0xFF)*299)+(((intc3 >> 8) & 0xFF)*587)+(((intc3 >> 0) & 0xFF)*114)) < ((((intc4 >> 16) & 0xFF)*299)+(((intc4 >> 8) & 0xFF)*587)+(((intc4 >> 0) & 0xFF)*114))){
            return intc4;
        }
        else{
            return intc3;
        }

    }



    public static int comparison3(int intc5, int intc6){
        //Beleuchtung
        Color c5 = new Color(intc5);
        Color c6 = new Color(intc6);


        if(((Math.max(c5.getRed(),Math.max(c5.getGreen(),c5.getBlue()))+Math.min(c5.getRed(),Math.min(c5.getGreen(),c5.getBlue())))/2) < ((Math.max(c6.getRed(),Math.max(c6.getGreen(),c6.getBlue()))+Math.min(c6.getRed(),Math.min(c6.getGreen(),c6.getBlue())))/2)){
            return c6.getRGB();
        }
        else{
            return c5.getRGB();
        }
       

    }
    
    public static int comparison4(int intc7, int intc8, int NOA){
    	//Durchschnittlicher Farbwert
    	
    	Color c7 = new Color(intc7);
    	Color c8 = new Color(intc8);

    	
    	double NrOfAdd = NOA;
    	
    	return new Color((int)Math.round(((NrOfAdd/(NrOfAdd+1.0))*c7.getRed())+((1.0/(NrOfAdd+1.0))*c8.getRed())),(int)Math.round(((NrOfAdd/(NrOfAdd+1.0))*c7.getGreen())+((1.0/(NrOfAdd+1.0))*c8.getGreen())),(int)Math.round(((NrOfAdd/(NrOfAdd+1.0))*c7.getBlue())+((1.0/(NrOfAdd+1.0))*c8.getBlue()))).getRGB();
    	    
    }


}