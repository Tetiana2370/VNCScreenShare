package model;

import java.io.*;
import java.util.Base64;

public class ConnectionParameters implements Serializable {
    

    public static final float DEFAULT_SCREENSHOT_SCALE = 0.2f;
    public static final float DEFAULT_SCREENSHOT_QUALITY = 0.5f;
    public static final int DEFAULT_FRAMES_PER_SECOND = 2;
    private final int VNC_PASSWORD_LENGTH = 10;
    private static final String VALID_CLIENT_PACKAGE_REQUIRED_STRING = "ScreenShareClient";
    private static final String VALID_SERVER_PACKAGE_REQUIRED_STRING = "ScreenShareServer";
    private final String passwordForVNC;

    private double scale;
    private int framesPerSecond;
    private float quality;
    private int port;
    private boolean hasChanged;

    public ConnectionParameters() {
        this.scale = DEFAULT_SCREENSHOT_SCALE;
        this.framesPerSecond = DEFAULT_FRAMES_PER_SECOND;
        this.hasChanged = true;
        this.passwordForVNC = PasswordGenerator.generatePassword(VNC_PASSWORD_LENGTH);
        this.quality = DEFAULT_SCREENSHOT_QUALITY;
    }

    public void setQuality(float quality) {
        this.quality = quality;
        hasChanged = true;
    }

    public void setScale(double scale) {
        this.scale = scale;
        hasChanged = true;
    }

    public void setFramesPerSecond(int framesPerSecond) {
        this.framesPerSecond = framesPerSecond;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public double getScale() {
        return scale;
    }

    public int getFramesPerSecond() {
        return framesPerSecond;
    }

    public float getQuality() {
        return quality;
    }

    public boolean changed() {
        return hasChanged;
    }

    public String getPasswordForVNC() {
        return this.passwordForVNC;
    }

    public static String convertToString(ConnectionParameters connectionParameters) {
        try (final ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(connectionParameters);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (final IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ConnectionParameters from(final String connectionParamsString) {
        final byte[] data = Base64.getDecoder().decode(connectionParamsString);
        try (final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (ConnectionParameters) ois.readObject();
        } catch (final IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "ConnectionParams{" +
                "hasChanged=" + hasChanged +
                ", scale=" + scale +
                ", framesPerSecond=" + framesPerSecond +
                ", quality=" + quality +
                ", passwordForVNC='" + passwordForVNC + '\'' +
                ", VNC_PASSWORD_LENGTH=" + VNC_PASSWORD_LENGTH +
                ", port=" + port +
                '}';
    }


}
