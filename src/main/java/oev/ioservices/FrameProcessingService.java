package oev.ioservices;

import oev.mvc.Model;

/**
 * Created by Flo on 09/09/2016.
 */
public interface FrameProcessingService {

    void setPaths(String srcPath, String resPath);

    void setModel(Model model);

    void setOptionsAndPrepareExecution(Integer amountFrames, Integer startFrame, Integer function, Integer effectLengthInFrames);

    void execute();

    boolean cancel(boolean mayInterruptIfRunning);

}
