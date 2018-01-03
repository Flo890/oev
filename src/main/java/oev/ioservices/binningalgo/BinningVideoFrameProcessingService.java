package oev.ioservices.binningalgo;

import oev.ioservices.binningalgo.model.BinnedImage;
import oev.ioservices.binningalgo.properties.LazyProperty;
import oev.model.SumAlgo;
import oev.mvc.Model;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Map;

public class BinningVideoFrameProcessingService extends AbstractBinningFrameProcessingService {
    public BinningVideoFrameProcessingService(Model model, SumAlgo sumAlgo) {
        super(model, sumAlgo);
    }

    @Override
    public void loadAndProcessAllFrames(Map<LazyProperty,Object> lazyProperties) {
        model.increaseProgress();

        BinnedImage binnedImage = new BinnedImage(jobMetaData.getWidth(), jobMetaData.getHeight(), ioService.getSourceFramesAmount());

        // save the first frame unedited, and then use it as previous frame
        BufferedImage firstFrame = ioService.getNextFrame();
        int frameCounter = 0;
        binnedImage.addBufferedImageToBins(frameCounter,firstFrame);
        frameCounter++;
        ioService.save(firstFrame);
        model.increaseProgress();

        BufferedImage nextFrame = null;

        long times = 0;
        while((nextFrame = ioService.getNextFrame())!=null){
            Long starttime = new Date().getTime();
            binnedImage.addBufferedImageToBins(frameCounter,nextFrame);
            ioService.save(binnedImageRenderer.renderImage(binnedImage, lazyProperties));
            frameCounter++;
            model.increaseProgress();
            times += new Date().getTime()-starttime;
        }
        System.out.println("avg image time: "+(times/(frameCounter-1)));

        model.showJobFinishedMessage();
    }
}
