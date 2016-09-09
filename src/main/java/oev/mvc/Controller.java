package oev.mvc;

import java.awt.Desktop;
import java.awt.event.*; 
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.ButtonModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class Controller implements ActionListener {
	
	private final View view;
	private final Model model;
	
	public Controller(){
		view = new View(this);
		model = new Model();
		model.addObserver(view);
		view.setVisible(true);
	}
	
	
	public void actionPerformed(ActionEvent e){
		
		String actionCommand = e.getActionCommand();
		
		//set source path if there was chosen one in the file chooser or entered directly in the textfield
		if(actionCommand.equals("selectSrc") || actionCommand.equals("srcTextFeld")){
			String path = null;
			if(actionCommand.equals("selectSrc")) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				fileChooser.showOpenDialog(null);
				path = fileChooser.getSelectedFile().getAbsolutePath();
			} else {
				path = view.getSrcPath();
			}
			model.setSrcPath(path);
			return;
		}

		//set result path if there was chosen one in the file chooser or entered directly in the textfield
		if(actionCommand.equals("selectRes") || actionCommand.equals("resTextFeld")){
			String path = null;
			if(actionCommand.equals("selectRes")){
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES);
				fileChooser.showOpenDialog(null);
				path = fileChooser.getSelectedFile().getAbsolutePath();
			} else {
				path = view.getResPath();
			}
			
			//Overwrite warning					
			int eingabe = JOptionPane.showConfirmDialog(null, "Existing *.png files and Log.txt can be overwritten when you continue!", "Warning", JOptionPane.WARNING_MESSAGE);
			System.out.println(eingabe);
			if(eingabe==2){
				model.setResPath("");
				return;	
			}
			model.setResPath(path);
		}


		
		//set mode
		if(actionCommand.equals("Mode")||actionCommand.equals("oev.ioservices.SumImageProcessingService")||actionCommand.equals("Vid")||actionCommand.equals("VidSpecial2Quad")||actionCommand.equals("Tunnel")){
			ButtonModel jrb = view.modeGroup.getSelection();
			if(jrb.getActionCommand().equals("oev.ioservices.SumImageProcessingService")){
				model.setMode(1);
			}
			if(jrb.getActionCommand().equals("Vid")){
				model.setMode(2);
			}
			if(jrb.getActionCommand().equals("VidSpecial")){
				model.setMode(3);
			}
			if(jrb.getActionCommand().equals("VidSpecial2Quad")){
				model.setMode(4);
			}
			if(jrb.getActionCommand().equals("Tunnel")){
				model.setMode(5);
			}
			
		}		
		
		
		//start processing
		if(actionCommand.equals("Start")){

			if(view.fktBox.getSelectedIndex()==-1){
				showErrorMessage("function is not selected");
			}else{
				model.setFkt(view.fktBox.getSelectedIndex()+1);
			}

			try{
				model.setAmountFrames(Integer.parseInt(view.getAmountFrames()));
			}
			catch(Exception f){
				showErrorMessage("amount of frames is not set");
			}

			try{
				model.setEffectLengthInFrames(Integer.parseInt(view.getNachziehendeFrames()));
			}
			catch(Exception g){
				if(model.getMode()==4||model.getMode()==5){
					showErrorMessage("addition amount is not set");
				}
			}

			try{
				model.setStartFrame(Integer.parseInt(view.getStartFrame()));
			}
			catch(Exception h){
				showErrorMessage("startFrame is not set");
			}
			

			
			//set progress bar to zero
			model.setProgressState(0);
			//start
			model.run();
		}
		
		
		
		//Anhalten
		if(actionCommand.equals("Stop")){
			model.setProgressState(0);
			model.stop();
		}
		
		//exit app
		if(actionCommand.equals("Exit")){
			System.exit(0);
		}
		
		//open src folder
		if(actionCommand.equals("ShowSrc")){
			model.showSrcPath();
		}
		
		//open result folder
		if(actionCommand.equals("ShowRes")){
			model.showResPath();
		}
		
		//Info anzeigen
		if(actionCommand.equals("Info")){
			JLabel infoText = new JLabel("Version 1.3.2 from 10.09.2016    -   http://www.tf-fotovideo.de/oev/    -  by Florian Bemmann");
			infoText.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {}
				
				@Override
				public void mousePressed(MouseEvent e) {}
				
				@Override
				public void mouseExited(MouseEvent e) {}
				
				@Override
				public void mouseEntered(MouseEvent e) {}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					try {
						Desktop.getDesktop().browse(new URI("http://www.tf-fotovideo.de/oev/"));
					} catch (IOException | URISyntaxException e1) {						
						e1.printStackTrace();
					}					
				}
			});
			
			JOptionPane.showMessageDialog(null, infoText, "About",JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void showErrorMessage(String string) {
		JOptionPane.showMessageDialog(null, string, "wrong input", JOptionPane.WARNING_MESSAGE);
		return;
	}
	
}
