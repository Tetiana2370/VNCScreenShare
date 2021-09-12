package model;

public class ScreenshotParameters {

    private final double scale;
    private final float quality;
    private final int framesPerSecond;

    public ScreenshotParameters(double scale, float quality, int framesPerSecond) {
        this.scale = scale;
        this.quality = quality;
        this.framesPerSecond = framesPerSecond;
    }

    public double getScale() {
        return scale;
    }

    public float getQuality() {
        return quality;
    }

    public int getFramesPerSecond() {
        return framesPerSecond;
    }
    
    public static ScreenshotParameters from(ConnectionParameters connectionParameters) {
        double scale = connectionParameters.getScale();
        float quality = connectionParameters.getQuality();
        int framesPerSecond = connectionParameters.getFramesPerSecond();
        return new ScreenshotParameters(scale, quality, framesPerSecond);
    }
}
