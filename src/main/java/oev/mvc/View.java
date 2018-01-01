package oev.mvc;

import oev.model.ColorFunction;
import oev.model.Mode;

import java.awt.event.*;
import java.util.Observer;

import java.util.Observable;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class View extends JFrame implements Observer {

    Controller controller;

    // JTextField srcPath
    JTextField resPath, amountNachziehen;
    JComboBox fktBox;
    ButtonGroup modeGroup;
    JLabel lastAction1, lastAction2, lastAction3;
    JLabel selectSrcInfo;
    JProgressBar progressBar;
    private JPanel nachziehen;

    public View(Controller c) {
        controller = c;
        setTitle("OEV " + c.getProperties().getProperty("oevVersion"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuLeiste = new JMenuBar();
        JMenu file = new JMenu("Datei");
        JMenu about = new JMenu("About");

        JMenuItem showSrc = new JMenuItem("Show source folder");
        showSrc.addActionListener(controller);
        showSrc.setActionCommand("ShowSrc");
        JMenuItem showRes = new JMenuItem("Show destination folder");
        showRes.addActionListener(controller);
        showRes.setActionCommand("ShowRes");
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(controller);
        exit.setActionCommand("Exit");

        JMenuItem info = new JMenuItem("Info");
        info.addActionListener(controller);
        info.setActionCommand("Info");

        file.add(showSrc);
        file.add(showRes);
        file.add(exit);

        about.add(info);

        menuLeiste.add(file);
        menuLeiste.add(about);


        JPanel mainArea = new JPanel(new BorderLayout());

        mainArea.add(menuLeiste, BorderLayout.NORTH);

        //selectFolder things
        JPanel selectFolders = new JPanel(new GridLayout(1, 3));
        JPanel selectSource = new JPanel(new FlowLayout());


        selectSrcInfo = new JLabel();
        //new multi file selector:
        JButton selectInputFilesButton = new JButton("select input files");
        selectInputFilesButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "PNG Images", "png");
            chooser.setFileFilter(filter);
            chooser.setMultiSelectionEnabled(true);
            int returnVal = chooser.showOpenDialog(selectSource);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                controller.actionPerformed(new ActionEvent(chooser, 1, "srcFilesSelected"));
            }
        });
        selectSource.add(selectSrcInfo);
        selectSource.add(selectInputFilesButton);

        JPanel selectResult = new JPanel(new FlowLayout());
        JButton selectRes = new JButton("select output folder");
        selectRes.addActionListener(controller);
        selectRes.addActionListener(e -> {
            String path = null;
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int returnVal = fileChooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                path = fileChooser.getSelectedFile().getAbsolutePath();
                //Overwrite warning
                int returnValConfirmation = JOptionPane.showConfirmDialog(null, "Existing *.png files and Log.txt can be overwritten when you continue!", "Warning", JOptionPane.OK_CANCEL_OPTION);
                if (returnValConfirmation == JOptionPane.OK_OPTION) {
                    controller.actionPerformed(new ActionEvent(fileChooser, 1, "resPathSelected"));
                }
            }


        });
        selectResult.add(selectRes);
        resPath = new JTextField(System.getProperty("user.home"), 25);
        resPath.addActionListener(controller);
        resPath.setActionCommand("resTextFeld");
        selectResult.add(resPath);


        selectFolders.add(selectSource);
        selectFolders.add(selectResult);


        mainArea.add(selectFolders, BorderLayout.CENTER);


        JPanel untenArea = new JPanel(new BorderLayout());
        JSeparator line = new JSeparator();
        untenArea.add(line, BorderLayout.NORTH);
        JPanel settings = new JPanel(new GridLayout(1, 2));
        JPanel mode = new JPanel(new GridLayout(5, 1));

        //Mode selection
        JLabel modeTitle = new JLabel("Select mode:");

        modeGroup = new ButtonGroup();
        mode.add(modeTitle);
        for (Mode aMode : Mode.values()) {
            JRadioButton mode1 = new JRadioButton(aMode.getDisplayName());
            mode1.addActionListener(controller);
            mode1.setActionCommand(aMode.getActionCommand());
            if(aMode.equals(Mode.SUMMIMAGE)){
                mode1.setSelected(true);
            }
            modeGroup.add(mode1);
            mode.add(mode1);
        }
        settings.add(mode);

        //funktion selection
        JPanel fkt = new JPanel(new GridLayout(4, 1));
        JLabel fktLabel = new JLabel("Function for brightness comparison:");
        fkt.add(fktLabel);

        fktBox = new JComboBox(ColorFunction.values());
        fktBox.setSelectedIndex(1);
        fkt.add(fktBox);



        fkt.add(new JLabel(""));
        fkt.add(new JLabel(""));

        nachziehen = new JPanel(new FlowLayout());
        JLabel nachziehenLabel = new JLabel("light-trail length: ");
        nachziehen.add(nachziehenLabel);
        amountNachziehen = new JTextField(6);
        nachziehen.add(amountNachziehen);
        nachziehen.setVisible(false); // will get visible when trailvideo mode gets selected
        fkt.add(nachziehen);

        JCheckBox multithreadingCheckbox = new JCheckBox("use multithreading ("+controller.getModel().getAmountThreads()+" cores detected)");
        multithreadingCheckbox.setSelected(controller.getModel().isMultithreadingEnabled());
        multithreadingCheckbox.addItemListener(e -> controller.getModel().setMultithreadingEnabled(((JCheckBox)e.getItem()).isSelected()));
        fkt.add(multithreadingCheckbox);

        settings.add(fkt);

        untenArea.add(settings, BorderLayout.CENTER);

        //Read documentation Zeile
        JPanel ganzUnten = new JPanel(new BorderLayout());
        JPanel bilderPreview = new JPanel();


        JLabel previewSpace = new JLabel("read documentation on http://www.tf-fotovideo.de/oev/");
        previewSpace.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent arg0) {
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
            }

            @Override
            public void mouseClicked(MouseEvent arg0) {

                try {
                    Desktop.getDesktop().browse(new URI("http://www.tf-fotovideo.de/oev/"));
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }

            }
        });


        bilderPreview.add(previewSpace);
        ganzUnten.add(bilderPreview, BorderLayout.NORTH);


        //Trennlinie
        JSeparator line2 = new JSeparator();
        ganzUnten.add(line2, BorderLayout.CENTER);

        //start,stop,ladebalken area
        JPanel startArea = new JPanel(new GridLayout(1, 2));
        JPanel progress = new JPanel();

        //Progress Bar
        startArea.add(progress);
        JPanel progressBarArea = new JPanel(new BorderLayout());
        progressBar = new JProgressBar(0, 59);
        progressBar.setStringPainted(true);
        progressBarArea.add(progressBar, BorderLayout.CENTER);
        progress.add(progressBar);

        //Fortschrittstext
        JPanel actions = new JPanel(new BorderLayout());
        lastAction1 = new JLabel("Waiting for Start...");
        actions.add(lastAction1, BorderLayout.NORTH);
        lastAction2 = new JLabel("LA2");
        actions.add(lastAction2, BorderLayout.CENTER);
        lastAction3 = new JLabel("LA3");
        actions.add(lastAction3, BorderLayout.SOUTH);
        progress.add(actions);
        startArea.add(progress);

        //Start Button
        JButton start = new JButton("Start");
        start.addActionListener(controller);
        start.setActionCommand("Start");
        startArea.add(start);

        //Stop Button
        JButton stop = new JButton("Stop");
        stop.addActionListener(controller);
        stop.setActionCommand("Stop");
        startArea.add(stop);
        ganzUnten.add(startArea, BorderLayout.SOUTH);

        untenArea.add(ganzUnten, BorderLayout.SOUTH);

        mainArea.add(untenArea, BorderLayout.SOUTH);

        add(mainArea);
        pack();
    }

    public void update(Observable o, Object obj) {
        Model m = (Model) o;
        selectSrcInfo.setText(m.getSourceFilesInfo());
        resPath.setText(m.getResPath());
        lastAction1.setText(m.getLastAction1());
        lastAction2.setText(m.getLastAction2());
        lastAction3.setText(m.getLastAction3());
        progressBar.setMaximum(m.getMaxOperations());
        progressBar.setValue(m.getProgress());
        nachziehen.setVisible(m.getMode().equals(Mode.TRAILVIDEO));
    }

    public String getNachziehendeFrames() {
        return amountNachziehen.getText();
    }

}
