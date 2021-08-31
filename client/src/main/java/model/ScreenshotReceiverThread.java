package model;

import controller.MainWindowController;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ScreenshotReceiverThread implements Runnable {
    private final String ipAddress;
    private final ObjectInputStream inputStream;
    private final MainWindowController mainWindowController;

    public ScreenshotReceiverThread(String ipAddress, ObjectInputStream inputStream, MainWindowController mainWindowController) {
        this.ipAddress = ipAddress;
        this.inputStream = inputStream;
        this.mainWindowController = mainWindowController;
    }

    public void run() {
        while (true) {
            try {
                byte[] screenShot = (byte[]) inputStream.readObject();
                showScreenShot(screenShot);
            } catch (IOException | ClassNotFoundException e) {
                SwingUtilities.invokeLater(this::removePreview);
                break;
            }
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            System.out.println("ObjectInputStream close failed -> " + e.getMessage());
        }
    }

    private void showScreenShot(byte[] screenShot) {
        SwingUtilities.invokeLater(() -> {
            mainWindowController.updatePreview(ipAddress, new ImageIcon(screenShot));
        });
    }

    private void removePreview() {
        mainWindowController.removePreview(ipAddress);
    }
}
