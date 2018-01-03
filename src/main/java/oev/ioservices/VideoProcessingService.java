package oev.ioservices;

import oev.mvc.Model;

import java.awt.image.BufferedImage;

@Deprecated
public class VideoProcessingService extends AbstractFrameProcessingService implements FrameProcessingService {

    private BufferedImage lastResultImage;

    public VideoProcessingService(Model model) {
        super(model);
    }

    public void loadAndProcessAllFrames() {

        // save the first frame unedited, and then use it as previous frame
        lastResultImage = ioService.getNextFrame();
        ioService.save(lastResultImage);
        model.increaseProgress();

        BufferedImage nextFrame = null;
        while ((nextFrame = ioService.getNextFrame()) != null) {

            engine.findNewColorForEachPixel(nextFrame, lastResultImage);

            ioService.save(lastResultImage);
            model.increaseProgress();
        }

        model.showJobFinishedMessage();
    }


}