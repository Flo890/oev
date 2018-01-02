package oev.ioservices.binningalgo;

import oev.ioservices.FrameProcessingService;
import oev.ioservices.binningalgo.model.BinnedImage;
import oev.mvc.Model;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
        while((nextFrame = ioService.getNextFrame())!=null){
            binnedImage.addBufferedImageToBins(nextFrame);
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
        String[] options = {"OK"};
        JOptionPane.showMessageDialog(null, slider);//, "Adjust brightness",
       //         JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
           //     null, options, options[0]);
        System.out.println("user inputed brightness "+valueArray[0]);
        return valueArray[0];
    }
}
