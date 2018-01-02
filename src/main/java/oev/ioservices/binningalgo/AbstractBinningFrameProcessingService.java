package oev.ioservices.binningalgo;

import oev.ioservices.AbstractFrameProcessingService;
import oev.model.ColorFunction;
import oev.mvc.Model;

public abstract class AbstractBinningFrameProcessingService extends AbstractFrameProcessingService {
    public AbstractBinningFrameProcessingService(Model model) {
        super(model);
    }

    @Override
    protected void createEngine(ColorFunction function) {

    }
}
