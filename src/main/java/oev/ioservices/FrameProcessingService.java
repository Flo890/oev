package oev.ioservices;

import oev.ioservices.binningalgo.properties.LazyProperty;
import oev.model.ColorFunction;
import oev.mvc.Model;

import java.io.File;
import java.util.Map;

/**
 * Created by Flo on 09/09/2016.
 */
public interface FrameProcessingService {

  void setModel(Model model);

  void setOptionsAndPrepareExecution(ColorFunction function, File[] sourceFiles, String resPath);

  void execute();

  boolean cancel(boolean mayInterruptIfRunning);

  //void loadAndProcessAllFrames();

  void loadAndProcessAllFrames(Map<LazyProperty,Object> lazyProperties);

}
