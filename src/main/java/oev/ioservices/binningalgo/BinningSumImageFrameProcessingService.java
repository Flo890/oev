package oev.ioservices.binningalgo;

import oev.ioservices.binningalgo.model.BinnedImage;
import oev.mvc.Model;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class BinningSumImageFrameProcessingService extends AbstractBinningFrameProcessingService {
    public BinningSumImageFrameProcessingService(Model model) {
        super(model);
    }

    @Override
    public void loadAndProcessAllFrames() {
        model.increaseProgress();

        BinnedImage binnedImage = new BinnedImage(jobMetaData.getWidth(), jobMetaData.getHeight());

        BufferedImage nextFrame = null;
        int frameCounter = 0;
        while((nextFrame = ioService.getNextFrame())!=null){
            binnedImage.addBufferedImageToBins(frameCounter,nextFrame);
            frameCounter++;
            model.increaseProgress();
        }

        int normalizationFactor = promptForBrightness(binnedImage.getImageAmountInBins());

        ioService.save(binnedImage.getImageNormalized(normalizationFactor), "resultImg.png");

        model.showJobFinishedMessage();

    }

    public Integer promptForBrightness(int imageAmountInBins) {
        JSlider slider = new JSlider(1,imageAmountInBins,imageAmountInBins);
        int[] valueArray = {imageAmountInBins};
        slider.addChangeListener(e -> valueArray[0] = ((JSlider)e.getSource()).getValue());
        JOptionPane.showMessageDialog(null, slider);
        System.out.println("user inputed brightness "+valueArray[0]);
        return valueArray[0];
    }
}
