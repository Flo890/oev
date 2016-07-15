import java.awt.image.ComponentColorModel;
import java.awt.*;

public class Test{

    static TestProzess th1 = new TestProzess();
    static TestProzess th2 = new TestProzess();

    public Test(){

    }

    public static void main(String[] args){
       // th1.start();
               
        //th2.start();

        for(int i=0; i<5; i++){
            System.out.println(i);
        }
        
       System.out.println( new Color(0,0,0).getRGB());
        
    }

}