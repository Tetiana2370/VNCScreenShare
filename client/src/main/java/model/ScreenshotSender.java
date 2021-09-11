package model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingQueue;

public class ScreenshotSender implements Runnable {
    private final BlockingQueue<byte[]> convertedScreenshots;
    private final ObjectOutputStream outputStream;

    public ScreenshotSender(BlockingQueue<byte[]> convertedScreenshots, ObjectOutputStream outputStream) {
        this.convertedScreenshots = convertedScreenshots;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        try {
            while (Thread.currentThread().isAlive()) {
                sendToServer(convertedScreenshots.take(), outputStream);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Report.println("Connection closed");
        }
    }

    private void sendToServer(Object object, ObjectOutputStream outputStream) throws IOException {
        if (object != null) {
            outputStream.writeObject(object);
            outputStream.reset();
            outputStream.flush();
        }
    }
}
