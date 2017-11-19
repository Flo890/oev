package oev.ioservices;

import oev.mvc.Model;

import java.io.File;

/**
 * Created by Flo on 09/09/2016.
 */
public interface FrameProcessingService {

  void setResPath(String resPath);

  void setModel(Model model);

  void setOptionsAndPrepareExecution(Integer function, Integer effectLengthInFrames);

  void execute();

  boolean cancel(boolean mayInterruptIfRunning);

  void setSourceFiles(File[] sourceFiles);
}
