package oev.ioservices;

import oev.ioservices.threads.Engine;
import oev.ioservices.threads.EngineMultiRecentImages;
import oev.model.ColorFunction;
import oev.mvc.Model;

import javax.swing.*;
import java.io.File;

public abstract class AbstractFrameProcessingService extends SwingWorker implements FrameProcessingService {

  protected Model model;
  protected IOService ioService;
  protected Engine engine;

  protected JobMetaData jobMetaData;

  public AbstractFrameProcessingService(Model model) {
    this.model = model;
  }


  public void setOptionsAndPrepareExecution(ColorFunction function, File[] sourceFiles, String resPath) {
    ioService = new IOService(sourceFiles, resPath, model);
    jobMetaData = ioService.fetchJobMetaData();
    engine = new EngineMultiRecentImages(jobMetaData.getWidth(), jobMetaData.getHeight(), function, 3);//TODO this is hardcoded
  }

  public void setModel(Model m) {
    model = m;
  }

  @Override
  protected Object doInBackground() throws Exception {
    loadAndProcessAllFrames();
    return null;
  }

}
