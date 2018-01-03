package oev.ioservices.binningalgo;

import oev.colorprocessing.ColorComparisonFunction;
import oev.ioservices.binningalgo.model.BinnedImage;
import oev.ioservices.binningalgo.properties.LazyProperty;
import oev.ioservices.binningalgo.renderer.BrightestColorBinnedImageRenderer;
import oev.model.ColorFunction;
import oev.model.SumAlgo;
import oev.mvc.Model;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;
import java.util.Map;

public class BinningSumImageFrameProcessingService extends AbstractBinningFrameProcessingService {

    public BinningSumImageFrameProcessingService(Model model, SumAlgo sumAlgo) {
        super(model, sumAlgo);
    }

    @Override
    public void loadAndProcessAllFrames(Map<LazyProperty,Object> lazyProperties) {
        model.increaseProgress();

        BinnedImage binnedImage = new BinnedImage(jobMetaData.getWidth(), jobMetaData.getHeight(), ioService.getSourceFramesAmount());

        BufferedImage nextFrame = null;
        int frameCounter = 0;
        long times = 0;
        while((nextFrame = ioService.getNextFrame())!=null){
            Long starttime = new Date().getTime();
            binnedImage.addBufferedImageToBins(frameCounter,nextFrame);
            frameCounter++;
            model.increaseProgress();
            times += new Date().getTime()-starttime;
        }
        System.out.println("avg image time: "+times/frameCounter);

        ioService.save(binnedImageRenderer.renderImage(binnedImage, lazyProperties), "resultImg.png");

        model.showJobFinishedMessage();

    }


}
