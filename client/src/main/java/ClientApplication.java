import model.*;
import view.ServerMainWindow;

import javax.swing.*;
import java.net.DatagramPacket;

public class ClientApplication {

    private static ServerMainWindow mainWindow;
    private static Process vncProcess;

    public static void main(String[] args) {
        createClientWindow();
        Runtime.getRuntime().addShutdownHook(new ProcessTerminator(vncProcess, VNCServerProcessBuilder.VNC_SERVER_PROCESS_NAME));

        while (Thread.currentThread().isAlive()) {
            DatagramPacket datagramPacket = MulticastMessageListener.listen();

            if(datagramPacket != null){
                ConnectionParams connectionParams = getConnectionParamsFromDatagramMessage(datagramPacket);
                updateMainWindowLabel(ServerMainWindow.CONNECTION_ACTIVE_LABEL);
                try{
                    vncProcess = VNCServerProcessBuilder.startProcess(connectionParams.getPasswordForVNC());
                    PreviewSharingConnection previewSharingConnection = new PreviewSharingConnection(datagramPacket.getAddress().getHostAddress(), connectionParams.getPort(), connectionParams);
                    previewSharingConnection.runServerThreadsAndWaitUntilInterrupted();
                    runVNCProcessTermination();
                } catch (Exception e) {
                    Report.println(e.getMessage());
                }
                updateMainWindowLabel(ServerMainWindow.CONNECTION_LOST_LABEL);
            }

        }
    }

    private static void updateMainWindowLabel(String text) {
        SwingUtilities.invokeLater(() -> mainWindow.setLabelText(text));
    }

    private static void createClientWindow() {
        SwingUtilities.invokeLater(() -> mainWindow = new ServerMainWindow());
    }

    private static void runVNCProcessTermination(){
        new ProcessTerminator(vncProcess, VNCServerProcessBuilder.VNC_SERVER_PROCESS_NAME).start();
    }

    private static ConnectionParams getConnectionParamsFromDatagramMessage(DatagramPacket datagramPacket){
        return ConnectionParams.from(new String(datagramPacket.getData(), 0, datagramPacket.getLength()));
    }
}