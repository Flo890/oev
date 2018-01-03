package oev.ioservices.binningalgo;

import oev.colorprocessing.ColorComparisonFunction;
import oev.colorprocessing.HumanPerceivedBrightnessCCF;
import oev.colorprocessing.LightedBrightnessCCF;
import oev.colorprocessing.PhysicalBrightnessCCF;
import oev.ioservices.AbstractFrameProcessingService;
import oev.ioservices.binningalgo.properties.LazyProperty;
import oev.ioservices.binningalgo.renderer.BinnedImageRenderer;
import oev.ioservices.binningalgo.renderer.BrightestColorBinnedImageRenderer;
import oev.ioservices.binningalgo.renderer.SummedColorBinnedImageRenderer;
import oev.model.ColorFunction;
import oev.model.SumAlgo;
import oev.mvc.Model;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBinningFrameProcessingService extends AbstractFrameProcessingService {

    protected ColorComparisonFunction colorComparisonFunction;

    private final SumAlgo sumAlgo;
    protected BinnedImageRenderer binnedImageRenderer;

    public AbstractBinningFrameProcessingService(Model model, SumAlgo sumAlgo) {
        super(model);
        this.sumAlgo = sumAlgo;
    }

    /*
     * TODO how to manage algo-dependant properties, like color function or normalizationDividend?
     */
    @Override
    public void setOptionsAndPrepareExecution(ColorFunction function, File[] sourceFiles, String resPath) {
        super.setOptionsAndPrepareExecution(function, sourceFiles, resPath);
        switch (function) {
            case PHYSICAL_BRIGHTNESS:
                colorComparisonFunction = new PhysicalBrightnessCCF();
                break;
            case HUMAN_PERCEIVED_BRIGHTNESS:
                colorComparisonFunction = new HumanPerceivedBrightnessCCF();
                break;
            case LIGHTED_BRIGHTNESS:
                colorComparisonFunction = new LightedBrightnessCCF();
                break;
            default:
                colorComparisonFunction = null;
                throw new IllegalStateException("function unknown: "+function);
        }

        switch (sumAlgo){
            case KEEP_BRIGHTEST:
                binnedImageRenderer = new BrightestColorBinnedImageRenderer(jobMetaData.getWidth(), jobMetaData.getHeight());
                break;
            case SUM_RGB:
                binnedImageRenderer = new SummedColorBinnedImageRenderer(jobMetaData.getWidth(), jobMetaData.getHeight());
                break;
             default:
                 binnedImageRenderer = null;
                 throw new IllegalArgumentException("unknown sum algo: "+sumAlgo);
        }
    }

    @Override
    protected void createEngine(ColorFunction function) {
        // do not create engine
    }

    public Integer promptForBrightness(int imageAmountInBins) {
        JSlider slider = new JSlider(1,imageAmountInBins,imageAmountInBins);
        int[] valueArray = {imageAmountInBins};
        slider.addChangeListener(e -> valueArray[0] = ((JSlider)e.getSource()).getValue());
        JOptionPane.showMessageDialog(null, slider);
        System.out.println("user inputed brightness "+valueArray[0]);
        return valueArray[0];
    }

    @Override
    protected Object doInBackground() throws Exception {
        try {
            Map<LazyProperty, Object> lazyProperties = new HashMap<>();
            //TODO maybe collect these properties somewhere else
            if (SumAlgo.KEEP_BRIGHTEST.equals(model.getSumAlgo())) {
                lazyProperties.put(LazyProperty.COLOR_FUNCTION, colorComparisonFunction);
            }
            if (SumAlgo.SUM_RGB.equals(model.getSumAlgo())) {
                lazyProperties.put(LazyProperty.NORMALIZATION_DIVIDEND, promptForBrightness(jobMetaData.getAmountFrames()));
            }

            loadAndProcessAllFrames(lazyProperties);

        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
