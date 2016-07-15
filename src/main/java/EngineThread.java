import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class EngineThread extends Thread{

    int threadNr;
    int start;
    int end;
    int fktNr;
    Engine engine;
    int addLaenge;

    int fileIndex;
    int fileIndex2;
    BufferedImage resultImage;
    
    String srcPath;
    String resPath;
    
    Model model;

    public EngineThread(int tn, int s, int e, int f, int al, String sP, String rP){
        threadNr = tn;
        start = s;
        end = e;
        fktNr = f;
        addLaenge = al;
        srcPath = sP;
        resPath = rP;
        System.out.println(threadNr+": from "+start+" to "+end+" with fkt "+fktNr+" and AddLaenge "+addLaenge);
    }

    public void run(){
        try{
            load();
        }
        catch(IOException e){
        	System.out.println(threadNr+": IOException caught in run()");
        	System.out.println("     srcPath: "+srcPath);
        	showFehlermeldung("IOException in thread "+threadNr+": "+e.getMessage());
        }
    }

    public void load() throws IOException{
    	System.out.println(threadNr+": load() called");
        fileIndex = start;
        for(int i = start; i<(end+1); i++){

            engine = new Engine(ImageIO.read(new File(srcPath+"\\"+Integer.toString(fileIndex)+".png")));
            //model.setNewAction("Thread "+threadNr+": Loading file"+Integer.toString(fileIndex)+".png");

            fileIndex2=fileIndex+1;;
            
            
            switch(fktNr){
            case 4:
            	for(int j = 0; j<addLaenge; j++){
            		resultImage=engine.averageValue(ImageIO.read(new File(srcPath+"\\"+Integer.toString(fileIndex2)+".png")));
                    fileIndex2++;
            	}
            	break;
            default:
            	for(int j = 0; j<addLaenge; j++){
            		resultImage=engine.maxValue2(fktNr,ImageIO.read(new File(srcPath+"\\"+Integer.toString(fileIndex2)+".png")));
            		fileIndex2++;
            	}
            	break;
            }

            save();
            fileIndex++;
        }

    }

    public void save() throws IOException{
        File saveFile = new File(resPath+"\\resultIMG"+fileIndex+".png");

        ImageIO.write(resultImage, "png", saveFile);
        System.out.println(threadNr+": File "+fileIndex+" saved");
        model.setNewAction("Thread "+threadNr+": Saved img resultIMG"+fileIndex+".png");
        model.iterateOperation();
    }
    
    public void setModel(Model m){
    	model = m;
    }
    
    
    
    /**
     * Gegebenen Text als Fehlermeldung anzeigen
     * @param text
     */
    private static void showFehlermeldung(String text){
    	JOptionPane.showMessageDialog(null, text, "IOException while running", JOptionPane.WARNING_MESSAGE);
    }

}