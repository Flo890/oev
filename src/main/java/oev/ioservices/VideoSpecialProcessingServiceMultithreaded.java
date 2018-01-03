package oev.ioservices;

import oev.ioservices.threads.Engine;
import oev.ioservices.threads.EngineThread;
import oev.model.ColorFunction;
import oev.mvc.Model;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Deprecated
public class VideoSpecialProcessingServiceMultithreaded extends AbstractFrameProcessingService implements FrameProcessingService {

    private int effectLengthInFrames;

    private int totalAmountFrames;
    private int amountFramesPerThread;
    private int rest;

    private EngineThread thread1;
    private EngineThread thread2;
    private EngineThread thread3;
    private EngineThread thread4;

    private Set<Integer> finishedThreads;

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
        thread1 = new EngineThread(1, 0, (amountFramesPerThread + 0 - 1), effectLengthInFrames, engine, ioService, this);
        thread2 = new EngineThread(2, (amountFramesPerThread + 0), ((2 * amountFramesPerThread) + 0 - 1), effectLengthInFrames, engine, ioService, this);
        thread3 = new EngineThread(3, ((2 * amountFramesPerThread) + 0), ((3 * amountFramesPerThread) + 0 - 1), effectLengthInFrames, engine, ioService, this);
        thread4 = new EngineThread(4, ((3 * amountFramesPerThread) + 0), (totalAmountFrames + 0 - 1), effectLengthInFrames, engine, ioService, this);

        finishedThreads = new HashSet<>();

    }

    public void loadAndProcessAllFrames() {
        System.out.println("Threads starten");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }

    public void setModel(Model m) {
        super.setModel(m);
        thread1.setModel(model);
        thread2.setModel(model);
        thread3.setModel(model);
        thread4.setModel(model);
    }

    public void threadHasFinished(int threadNr){
        finishedThreads.add(threadNr);
        if(finishedThreads.size() == 4){
            model.showJobFinishedMessage();
        }
    }

    @Override
    protected void createEngine(ColorFunction function) {
        // do not use the new multithreading in special mode, it has its own faster implementation
        engine = new Engine(jobMetaData.getWidth(), jobMetaData.getHeight(), function,1);
    }
}