package oev.ioservices;

import oev.ioservices.threads.EngineThread;
import oev.model.ColorFunction;
import oev.mvc.Model;

import java.io.File;

public class VideoSpecialProcessingServiceMultithreaded extends AbstractFrameProcessingService implements FrameProcessingService {

    private int effectLengthInFrames;

    private int totalAmountFrames;
    private int amountFramesPerThread;
    private int rest;

    private EngineThread thread1;
    private EngineThread thread2;
    private EngineThread thread3;
    private EngineThread thread4;

    public VideoSpecialProcessingServiceMultithreaded(Model model) {
        super(model);
    }

    /**
     * sets the given options and creates 4 threads, each with a fourth of the frames to process
     */
    public void setOptionsAndPrepareExecution(ColorFunction function, Integer aEffectLengthInFrames, File[] sourceFiles, String resPath) {

        super.setOptionsAndPrepareExecution(function, sourceFiles, resPath);

        this.effectLengthInFrames = aEffectLengthInFrames;

        totalAmountFrames = jobMetaData.getAmountFrames() - effectLengthInFrames;
        rest = totalAmountFrames % 4;
        amountFramesPerThread = (totalAmountFrames - rest) / 4;

        System.out.println("Threads erstellen");
        thread1 = new EngineThread(1, 0, (amountFramesPerThread + 0 - 1), effectLengthInFrames, engine, ioService);
        thread2 = new EngineThread(2, (amountFramesPerThread + 0), ((2 * amountFramesPerThread) + 0 - 1), effectLengthInFrames, engine, ioService);
        thread3 = new EngineThread(3, ((2 * amountFramesPerThread) + 0), ((3 * amountFramesPerThread) + 0 - 1), effectLengthInFrames, engine, ioService);
        thread4 = new EngineThread(4, ((3 * amountFramesPerThread) + 0), (totalAmountFrames + 0 - 1), effectLengthInFrames, engine, ioService);

    }

    public void loadAndProcessAllFrames() {
        System.out.println("Threads starten");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }

    public void setModel(Model m) {
        model = m;
        thread1.setModel(model);
        thread2.setModel(model);
        thread3.setModel(model);
        thread4.setModel(model);
    }

}