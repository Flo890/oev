import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class VideoIOM extends SwingWorker{

    static BufferedImage[]partInputArray=new BufferedImage[2];
    static BufferedImage[]totalInputArray;
    static int fktNr;
    static int anzahlBilder;
    static int startFrame;
    static BufferedImage lastResultImage;
    static int fileIndex;
    static String srcPath;
    static String resPath;
    static Model model;

    public VideoIOM(){

    }

    public static void main(String[]args){
        //Files have to be stored in folder VidSource, named "1.png" to for example "42.png"
        //All files must have the same resolution!!
        //1. Parameter: Anzahl Bilder
        //2. Paramtert: Fkt Nr
        anzahlBilder=Integer.parseInt(args[0]);
        totalInputArray=new BufferedImage[anzahlBilder];

        fktNr=Integer.parseInt(args[1]);
        startFrame=Integer.parseInt(args[2]);

    }

    

    public static void load2(){
        //ben√∂tigt weniger Arbeitsspeicher
        fileIndex = startFrame;
        
       
        Engine engine = null;
		try {
			engine = new Engine(lastResultImage=ImageIO.read(new File(srcPath+"\\"+Integer.toString(startFrame)+".png")) );
		} catch (IOException e) {
			showFehlermeldung("Could not load file "+srcPath+"\\"+Integer.toString(startFrame)+".png");
			e.printStackTrace();
		}
        
        
        System.out.println("Loading first file "+startFrame+".png");
        model.setNewAction("VideoIOM: loading "+startFrame+".png");
        save();
        fileIndex = startFrame+1;

        for(int i = 0; i<(anzahlBilder-1); i++){
        	
        	switch(fktNr){
        	//Default f¸r normale Verrechnungs fkt, case 4 f¸r die Durchschnittsmethode
        	case 4:
        		
        		try {
					lastResultImage=engine.averageValue(ImageIO.read(new File(srcPath+"\\"+Integer.toString(fileIndex)+".png")));
				} catch (IOException e) {
					showFehlermeldung("Could not load file "+srcPath+"\\"+Integer.toString(fileIndex)+".png");
					e.printStackTrace();
				}
        		
        		System.out.println("Loading file "+(fileIndex)+".png");
                model.setNewAction("VideoIOM: Loading file "+Integer.toString(fileIndex)+".png");
                save();
                fileIndex++;
                break;
        	
        	default:
            	try {
					lastResultImage=engine.maxValue2(fktNr, ImageIO.read(new File(srcPath+"\\"+Integer.toString(fileIndex)+".png")));
				} catch (IOException e) {
					showFehlermeldung("Could not read file "+srcPath+"\\"+Integer.toString(fileIndex)+".png");
					e.printStackTrace();
				}
            	
            	System.out.println("Loading file "+(fileIndex)+".png");
            	model.setNewAction("VideoIOM: Loading file "+Integer.toString(fileIndex)+".png");
            	save();
            	fileIndex++;
            	break;
        	}
        }

    }

    

    public static void save(){
        File saveFile = new File(resPath+"\\resultIMG"+(fileIndex-startFrame+1)+".png");
        
        try {
			ImageIO.write(lastResultImage, "png", saveFile);
		} catch (IOException e) {
			showFehlermeldung("Could not save file "+resPath+"\\resultIMG"+(fileIndex-startFrame+1)+".png");
			e.printStackTrace();
		}
        
        System.out.println("File "+(fileIndex-startFrame+1)+" saved");
        model.setNewAction("VideoIOM: Saving file "+(fileIndex-startFrame+1)+".png");
        model.iterateOperation();

    }
    
    
    public static void setPaths(String s, String r){
    	srcPath=s;
    	resPath=r;
    }
    
    
    public static void setModel(Model m){
    	model = m;
    }

    /**
     * Muss statt load2() aufgerufen werden damit die Berechnung im Hintergrund l‰uft und GUI nicht einfriert
     */
	@Override
	protected Object doInBackground() throws Exception {
		load2();
		return null;
	}
	
	
	 /**
     * Gegebenen Text als Fehlermeldung anzeigen
     * @param text
     */
    private static void showFehlermeldung(String text){
    	JOptionPane.showMessageDialog(null, text, "IOException while running", JOptionPane.WARNING_MESSAGE);
    }
	
	
}