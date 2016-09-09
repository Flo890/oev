package oev.ioservices;

import oev.mvc.Model;

import javax.swing.SwingWorker;


public class SumImageProcessingService extends SwingWorker implements FrameProcessingService {

    private IOMachine iomachine;
    private String srcPath;
    private String resPath;
    private Model model;

    public SumImageProcessingService(){
    }

    
    public void setOptionsAndPrepareExecution(Integer amountFrames, Integer startFrame, Integer function, Integer effectLengthInFrames){
        //Files have be stored in folder "source" named "1.png" to for example "42.png"
        //All files must have the same resolution!!

        iomachine = new IOMachine(amountFrames,function ,startFrame,srcPath,resPath,model);
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
		iomachine.loadAndProcessAllFrames();
		return null;
	}
    
}

