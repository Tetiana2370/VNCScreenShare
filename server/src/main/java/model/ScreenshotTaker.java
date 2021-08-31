package model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.BlockingQueue;

public class ScreenshotTaker implements Runnable {

    public final static float DEFAULT_SCALE = 1.0f;

    private final BlockingQueue<BufferedImage> screenshots;
    private final ScreenshotParameters screenshotParameters;
    final private int millisBetweenScreenshots;

    public ScreenshotTaker(BlockingQueue<BufferedImage> screenshots, ScreenshotParameters screenshotParameters) {
        this.screenshots = screenshots;
        this.screenshotParameters = screenshotParameters;
        this.millisBetweenScreenshots = 1000 / screenshotParameters.getFramesPerSecond();
    }

    @Override
    public void run() {
        try {
            Robot robot = new Robot();
            Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
            while (Thread.currentThread().isAlive()) {
                takeScreenshotAndAddToQueue(robot, defaultToolkit);
                Thread.sleep(this.millisBetweenScreenshots);
            }
        } catch (Exception e) {
            if (Thread.currentThread().isAlive()) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void takeScreenshotAndAddToQueue(Robot robot, Toolkit defaultToolkit) throws Exception {
        Rectangle shotArea = new Rectangle(defaultToolkit.getScreenSize());
        BufferedImage screenCapture = robot.createScreenCapture(shotArea);
        if (screenshotParameters.getScale() != DEFAULT_SCALE) {
            screenCapture = scaleScreenshot(screenCapture, screenshotParameters.getScale());
        }
        screenshots.put(screenCapture);
    }

    private BufferedImage scaleScreenshot(BufferedImage screenCapture, double scale) {
        int width = (int) (screenCapture.getWidth() * scale);
        int height = (int) (screenCapture.getHeight() * scale);
        Image scaledImage = screenCapture.getScaledInstance(width, height, BufferedImage.SCALE_AREA_AVERAGING);
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        result.createGraphics().drawImage(scaledImage, 0, 0, width, height, null);
        return result;
    }
}
