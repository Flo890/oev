package oev.mvc;

import oev.model.ColorFunction;
import oev.model.Mode;

import java.awt.Desktop;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import javax.swing.*;


public class Controller implements ActionListener {

  private final View view;
  private final Model model;
  private final Properties properties;

  public Controller(Properties prop) {
    properties = prop;
    view = new View(this);
    model = new Model();
    model.addObserver(view);
    view.setVisible(true);
  }


  public void actionPerformed(ActionEvent e) {

    String actionCommand = e.getActionCommand();

    if (actionCommand.equals("srcFilesSelected")) {
      JFileChooser fileChooser = (JFileChooser) e.getSource();
      model.setSourceFiles(fileChooser.getSelectedFiles());
    }

    //set result path if there was chosen one in the file chooser or entered directly in the textfield
    if (actionCommand.equals("resPathSelected")) {
      JFileChooser resultPathChooser = (JFileChooser) e.getSource();
      model.setResPath(resultPathChooser.getSelectedFile());
    }

    //set mode
    if (actionCommand.equals("Mode") || actionCommand.equals(Mode.SUMMIMAGE.getActionCommand()) || actionCommand.equals(Mode.SUMVIDEO.getActionCommand()) || actionCommand.equals(Mode.TRAILVIDEO.getActionCommand())) {
      ButtonModel jrb = view.modeGroup.getSelection();
      if (jrb.getActionCommand().equals(Mode.SUMMIMAGE.getActionCommand())) {
        model.setMode(Mode.SUMMIMAGE);
      }
      if (jrb.getActionCommand().equals(Mode.SUMVIDEO.getActionCommand())) {
        model.setMode(Mode.SUMVIDEO);
      }
      if (jrb.getActionCommand().equals(Mode.TRAILVIDEO.getActionCommand())) {
        model.setMode(Mode.TRAILVIDEO);
      }
    }

    //start processing
    if (actionCommand.equals("Start")) {

      if (view.fktBox.getSelectedIndex() == -1) {
        showErrorMessage("function is not selected");
      } else {
        model.setFkt((ColorFunction) view.fktBox.getSelectedItem());
      }

      try {
        model.setEffectLengthInFrames(Integer.parseInt(view.getNachziehendeFrames()));
      } catch (Exception g) {
        if (model.getMode() == Mode.TRAILVIDEO) {
          showErrorMessage("addition amount is not set");
        }
      }

      //set progress bar to zero
      model.setProgressState(0);
      //start
      model.run();
    }


    //Anhalten
    if (actionCommand.equals("Stop")) {
      model.setProgressState(0);
      model.stop();
    }

    //exit app
    if (actionCommand.equals("Exit")) {
      System.exit(0);
    }

    //open src folder
    if (actionCommand.equals("ShowSrc")) {
      model.showSrcPath();
    }

    //open result folder
    if (actionCommand.equals("ShowRes")) {
      model.showResPath();
    }

    //Info anzeigen
    if (actionCommand.equals("Info")) {
      JLabel infoText = new JLabel("Version "+getProperties().getProperty("oevVersion")+" from "+getProperties().getProperty("releaseDate")+"    -   http://www.tf-fotovideo.de/oev/    -  by Florian Bemmann");
      infoText.addMouseListener(new MouseListener() {

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
          try {
            Desktop.getDesktop().browse(new URI("http://www.tf-fotovideo.de/oev/"));
          } catch (IOException | URISyntaxException e1) {
            e1.printStackTrace();
          }
        }
      });

      JOptionPane.showMessageDialog(null, infoText, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    if (actionCommand.equals(view.useNewAlgoToggle.getText())) {
      model.setUseNewAlgorithm(((JToggleButton) e.getSource()).isSelected());
    }
  }

  private void showErrorMessage(String string) {
    JOptionPane.showMessageDialog(null, string, "wrong input", JOptionPane.WARNING_MESSAGE);
    return;
  }

  public Properties getProperties() {
    return properties;
  }

  public Model getModel() {
    return model;
  }
}
