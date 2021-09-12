import controller.MainWindowController;
import controller.SettingsWindowController;
import model.ConnectionParameters;
import model.ScreenshotReceiverThread;
import model.ClientConnection;

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

        ConnectionParameters connectionParams = runSettingsWindowAndWaitForDataEnter();
        ClientConnection clientConnection = new ClientConnection(connectionParams);
        ServerSocket serverSocket = clientConnection.getServerSocket();

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

    private static ConnectionParameters runSettingsWindowAndWaitForDataEnter() throws InterruptedException, ParseException {
        ConnectionParameters connectionParams = new ConnectionParameters();
        SettingsWindowController settingsWindowController = new SettingsWindowController(connectionParams);
        while (!settingsWindowController.isUpdated()) {
            Thread.sleep(1000);
        }
        return settingsWindowController.getConnectionParams();
    }

    private static void createAndShowMainWindow(ConnectionParameters connectionParams) throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> mainWindowController = new MainWindowController(connectionParams));
    }

    private static void startThreadsForNewConnection(Socket socket, String ipAddress, ConnectionParameters connectionParams) throws IOException {
        ObjectInputStream inputStream = createInputStream(socket);
        new Thread(new ScreenshotReceiverThread(ipAddress, inputStream, mainWindowController)).start();
    }

    private static ObjectInputStream createInputStream(Socket socket) throws IOException {
        return new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
    }
}
