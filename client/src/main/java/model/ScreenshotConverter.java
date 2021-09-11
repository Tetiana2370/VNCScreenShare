package model;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class ScreenshotConverter implements Runnable {
    private final BlockingQueue<BufferedImage> screenshots;
    private final BlockingQueue<byte[]> convertedImages;
    private final ScreenshotParameters screenshotParameters;

    public ScreenshotConverter(BlockingQueue<BufferedImage> screenshots, BlockingQueue<byte[]> convertedImages, ScreenshotParameters screenshotParameters) {
        this.screenshots = screenshots;
        this.convertedImages = convertedImages;
        this.screenshotParameters = screenshotParameters;
    }

    @Override
    public void run() {
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
        ImageWriteParam defaultWriteParam = writer.getDefaultWriteParam();
        defaultWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        writer.setOutput(new MemoryCacheImageOutputStream(bout));
        while (!Thread.currentThread().isInterrupted()) {
            try {
                try {
                    defaultWriteParam.setCompressionQuality(screenshotParameters.getQuality());
                    writer.write(null, new IIOImage(screenshots.take(), null, null), defaultWriteParam);
                    bout.flush();
                    convertedImages.add(bout.toByteArray());
                    bout.reset();

                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
