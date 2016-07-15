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
	
	View view;
	Model model;
	
	
	
	public Controller(){
		view = new View(this);
		model = new Model();
		model.addObserver(view);
		view.setVisible(true);
		
	}

	
	
	
	public void actionPerformed(ActionEvent e){
		
		String cmd = e.getActionCommand();
		
		//src path setzen wenn �ber FileChooser gewaehlt
		if(cmd.equals("selectSrc")){
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES);
			fileChooser.showOpenDialog(null);
			model.setSrcPath(fileChooser.getSelectedFile().getAbsolutePath());
			return;
		}
		
		//result path setzen wenn �ber FileChooser gewaehlt
		if(cmd.equals("selectRes")){
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES);
			fileChooser.showOpenDialog(null);			
			
			//Overwrite warning					
			int eingabe = JOptionPane.showConfirmDialog(null, "Existing *.png files and Log.txt can be overwritten when you continue!", "Warning", JOptionPane.WARNING_MESSAGE);
			System.out.println(eingabe);
			if(eingabe==2){
				model.setResPath("");
				return;	
			}
			model.setResPath(fileChooser.getSelectedFile().getAbsolutePath());
			
		}
			
		
		//SrcPath setzen wenn direkt in Textfeld gesetzt
		if(cmd.equals("srcTextFeld")){
			model.setSrcPath(view.getSrcPath());
		}
		
		
		//ResPath setzen wenn direkt in Textfeld gesetzt
		if(cmd.equals("resTextFeld")){
			
			//Overwrite warning					
			int eingabe = JOptionPane.showConfirmDialog(null, "Existing *.png files and Log.txt can be overwritten when you continue!", "Warning", JOptionPane.WARNING_MESSAGE);
			if(eingabe==2){
				model.setResPath("");
				return;
			}
			
			model.setResPath(view.getResPath());
			
		}
		
		
		//Modus setzen
		if(cmd.equals("Mode")||cmd.equals("Head")||cmd.equals("Vid")||cmd.equals("VidSpecial2Quad")||cmd.equals("Tunnel")){
			ButtonModel jrb = view.modeGroup.getSelection();
			if(jrb.getActionCommand().equals("Head")){
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
		
		
		//starten		
		if(cmd.equals("Start")){
			
			//Funktion auslesen
			if(view.fktBox.getSelectedIndex()==-1){
				showFehlermeldung("function is not selected");				
			}else{
				model.setFkt(view.fktBox.getSelectedIndex()+1);
			}
			
			
			
			//amount of frames auslesen
			try{
				model.setAnzahlFrames(Integer.parseInt(view.getAmountFrames()));
			}
			catch(Exception f){
				showFehlermeldung("amount of frames is not set");				
			}
			
			
			
			//amount nachziehende Frames auslesen
			try{
				model.setNachziehendeFrames(Integer.parseInt(view.getNachziehendeFrames()));
			}
			catch(Exception g){
				if(model.getMode()==4||model.getMode()==5){
					showFehlermeldung("addition amount is not set");					
				}
			}
			
			
			//startFrame auslesen
			try{
				model.setStartFrame(Integer.parseInt(view.getStartFrame()));
			}
			catch(Exception h){
				showFehlermeldung("startFrame is not set");				
			}
			
			
			
			//model.setSrcRes(view.getSrcPath(),view.getResPath());	
			
			//Ladebalken auf 0 setzen
			model.setOperation(0);
			//starten
			model.run();
		}
		
		
		
		//Anhalten
		if(cmd.equals("Stop")){
			model.setOperation(0);
			model.stop();
		}
		
		//Schlie�en
		if(cmd.equals("Exit")){
			System.exit(0);
		}
		
		//SrcFolder im Explorer �ffnen
		if(cmd.equals("ShowSrc")){
			model.showSrcPath();
		}
		
		//resultFolder im Explorer �ffnen
		if(cmd.equals("ShowRes")){
			model.showResPath();
		}
		
		//Info anzeigen
		if(cmd.equals("Info")){
			JLabel infoText = new JLabel("Version 1.3.1 from 16.05.2016    -   http://www.tf-fotovideo.de/oev/    -  by Florian Bemmann");
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
						Desktop.getDesktop().browse(new URI("www.cip.ifi.lmu.de/~bemmannf"));
					} catch (IOException | URISyntaxException e1) {						
						e1.printStackTrace();
					}					
				}
			});
			
			JOptionPane.showMessageDialog(null, infoText, "About",JOptionPane.INFORMATION_MESSAGE);
		}
	}

	
	/**
	 * Gibt Fehlermeldung an Nutzer aus
	 * @param string anzuzeigender Text
	 */
	private void showFehlermeldung(String string) {
		JOptionPane.showMessageDialog(null, string, "wrong input", JOptionPane.WARNING_MESSAGE);
		return;
	}
	
}
