package oev.ioservices;

import oev.ioservices.binningalgo.properties.LazyProperty;
import oev.ioservices.threads.Engine;
import oev.model.ColorFunction;
import oev.model.SumAlgo;
import oev.mvc.Model;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

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

    createEngine(function);
  }

  public void setModel(Model m) {
    model = m;
  }

  @Override
  protected Object doInBackground() throws Exception {
   // loadAndProcessAllFrames();
    return null;
  }

  protected void createEngine(ColorFunction function){
    engine = new Engine(jobMetaData.getWidth(), jobMetaData.getHeight(), function, model.isMultithreadingEnabled() ? model.getAmountThreads() : 1);
  }

  @Override
  public void loadAndProcessAllFrames(Map<LazyProperty, Object> lazyProperties) {

  }
}
