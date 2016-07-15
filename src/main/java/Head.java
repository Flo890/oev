
import javax.swing.SwingWorker;


public class Head extends SwingWorker{
//public class Head{

    static IOMachine iomachine;
    static String srcPath;
    static String resPath;
    static Model model;

    public Head(){        
    }

    
    public static void main(String[] args){  
        //Files have be stored in folder "source" named "1.png" to for example "42.png"
        //All files must have the same resolution!!
        //Erster Parameter: Anzahl der Bilder
        //Zweiter: Nr der Vgl.Fkt(1: für physisch, 2 für Wahrnehmungsnah [recommended], 3 für Beleuchtung)
    	//Dritter: StartFrame

        iomachine = new IOMachine(Integer.parseInt(args[0]),Integer.parseInt(args[1]) ,Integer.parseInt(args[2]),1,srcPath,resPath,model);
        //iomachine.load2();
    }
    
    public void setPaths(String s, String r){
    	srcPath=s;
    	resPath=r;
    }
    
    public void setModel(Model m){
    	model=m;
    }


	
    @Override
	protected Object doInBackground() throws Exception {
		iomachine.load2();
		return null;
	}
	
   
    
}

