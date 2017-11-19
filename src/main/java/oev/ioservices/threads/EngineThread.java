package oev.ioservices.threads;

import oev.ioservices.IOService;
import oev.mvc.Model;

import java.awt.image.BufferedImage;
import java.io.*;

public class EngineThread extends Thread {

    private final int threadNr;
    private final int start;
    private final int end;

    private final int addLaenge;

    private int fileIndex;
    private int fileIndex2;
    private BufferedImage lastImage;

    Model model;

    private final Engine engine;
    private final IOService ioService;

    public EngineThread(int tn, int s, int e, int al, Engine aEngine, IOService aIOService) {
        threadNr = tn;
        start = s;
        end = e;
        addLaenge = al;
        engine = aEngine;
        ioService = aIOService;
        System.out.println(threadNr + ": from " + start + " to " + end + " with AddLaenge " + addLaenge);
    }

    public void run() {
        try {
            load();
        } catch (IOException e) {
            model.showErrorMessage("IOException in thread " + threadNr + ": " + e.getMessage());
        }
    }

    public void load() throws IOException {
        System.out.println(threadNr + ": load() called");
        fileIndex = start;
        for (int i = start; i < (end + 1); i++) {

            lastImage = ioService.getFrameByIndex(fileIndex);

            fileIndex2 = fileIndex;

            for (int j = 0; j < addLaenge; j++) {
                engine.findNewColorForEachPixel(ioService.getFrameByIndex(fileIndex2), lastImage);
                fileIndex2++;
            }

            ioService.save(lastImage, "resultImg"+fileIndex+".png");
            fileIndex++;
            model.increaseProgress();
        }

    }

    public void setModel(Model m) {
        model = m;
    }

}