package model;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class PreviewSharingConnection {

    private final BlockingQueue<BufferedImage> screenshots = new LinkedBlockingDeque<>(1);
    private final BlockingQueue<byte[]> convertedScreenshots = new LinkedBlockingDeque<>();
    private final String ipAddress;
    private final int port;
    private final Thread screenshotTaker;
    private final Thread screenshotConverter;
    private final Thread screenshotSender;
    public PreviewSharingConnection(String ipAddress, int port, ConnectionParameters connectionParams) throws IOException {
        this.ipAddress = ipAddress;
        this.port = port;
        Socket socket = createSocket();
        ObjectOutputStream outputStream = createOutputStream(socket);
        ScreenshotParameters screenshotParameters = ScreenshotParameters.from(connectionParams);
        this.screenshotTaker = new Thread(new ScreenshotTaker(this.screenshots, screenshotParameters));
        this.screenshotConverter = new Thread(new ScreenshotConverter(this.screenshots, this.convertedScreenshots, screenshotParameters));
        this.screenshotSender = new Thread(new ScreenshotSender(this.convertedScreenshots, outputStream));
        Report.println(Report.CONNECTED_WITH, ipAddress);
    }

    private Socket createSocket() throws IOException {
        return new Socket(this.ipAddress, this.port);
    }

    public void runServerThreadsAndWaitUntilInterrupted() {
        try {
            screenshotTaker.start();
            screenshotConverter.start();
            screenshotSender.start();
            screenshotSender.join();
        } catch (InterruptedException e) {
            Report.println(Report.DISCONNECTED, this.ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stopScreenshotThreads(screenshotTaker, screenshotConverter);
            clearQueues();
        }
    }

    private void stopScreenshotThreads(Thread screenshotTaker, Thread screenshotConverter) {
        screenshotTaker.interrupt();
        screenshotConverter.interrupt();
    }

    private void clearQueues() {
        screenshots.clear();
        convertedScreenshots.clear();
    }

    private static ObjectOutputStream createOutputStream(Socket socket) {
        try {
            return new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
