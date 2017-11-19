package oev.ioservices;

import oev.model.ColorFunction;
import oev.mvc.Model;

import java.io.File;

/**
 * Created by Flo on 09/09/2016.
 */
public interface FrameProcessingService {

  void setModel(Model model);

  void setOptionsAndPrepareExecution(ColorFunction function, File[] sourceFiles, String resPath);

  void execute();

  boolean cancel(boolean mayInterruptIfRunning);

  void loadAndProcessAllFrames();

}
