import controller.MainWindowController;
import controller.SettingsWindowController;
import model.ConnectionParams;
import model.ScreenshotReceiverThread;
import model.ServerConnection;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;

public class ServerApplication {
    private static MainWindowController mainWindowController;

    public static void main(String[] args) throws Exception {

        ConnectionParams connectionParams = runSettingsWindowAndWaitForDataEnter();
        ServerConnection serverConnection = new ServerConnection(connectionParams);
        ServerSocket serverSocket = serverConnection.getServerSocket();

        createAndShowMainWindow(connectionParams);

        while (true) {
            Socket socket = serverSocket.accept();
            String ipAddress = socket.getInetAddress().toString().substring(1);
            if (mainWindowController.isConnected(ipAddress)) {
                socket.close();
                continue;
            }
            startThreadsForNewConnection(socket, ipAddress, connectionParams);
            mainWindowController.addNewConnectionToList(ipAddress);
            mainWindowController.setConnectionState(ipAddress, connectionParams);
        }
    }

    private static ConnectionParams runSettingsWindowAndWaitForDataEnter() throws InterruptedException, ParseException {
        ConnectionParams connectionParams = new ConnectionParams();
        SettingsWindowController settingsWindowController = new SettingsWindowController(connectionParams);
        while (!settingsWindowController.isUpdated()) {
            Thread.sleep(1000);
        }
        return settingsWindowController.getConnectionParams();
    }

    private static void createAndShowMainWindow(ConnectionParams connectionParams) throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> mainWindowController = new MainWindowController(connectionParams));
    }

    private static void startThreadsForNewConnection(Socket socket, String ipAddress, ConnectionParams connectionParams) throws IOException {
        ObjectInputStream inputStream = createInputStream(socket);
        new Thread(new ScreenshotReceiverThread(ipAddress, inputStream, mainWindowController)).start();
    }

    private static ObjectInputStream createInputStream(Socket socket) throws IOException {
        return new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
    }
}
