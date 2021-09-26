import model.*;
import view.ClientMainWindow;

import javax.swing.*;
import java.io.IOException;
import java.net.DatagramPacket;

public class ClientApplication {

    private static ClientMainWindow mainWindow;
    private static Process vncProcess;

    public static void main(String[] args) {
        checkIfVNCServerIsInstalled();
        createClientWindow();
        Runtime.getRuntime().addShutdownHook(new ProcessTerminator(vncProcess, VNCServerProcessBuilder.VNC_SERVER_PROCESS_NAME));

        while (Thread.currentThread().isAlive()) {
            DatagramPacket datagramPacket = MulticastMessageListener.listen();

            if(datagramPacket != null){
                ConnectionParameters connectionParams = getConnectionParamsFromDatagramMessage(datagramPacket);
                updateMainWindowLabel(ClientMainWindow.CONNECTION_ACTIVE_LABEL);
                try{
                    vncProcess = VNCServerProcessBuilder.startProcess(connectionParams.getPasswordForVNC());
                    Runtime.getRuntime().addShutdownHook(new ProcessTerminator(vncProcess, VNCServerProcessBuilder.VNC_SERVER_PROCESS_NAME));
                    PreviewSharingConnection previewSharingConnection = new PreviewSharingConnection(datagramPacket.getAddress().getHostAddress(), connectionParams.getPort(), connectionParams);
                    previewSharingConnection.runServerThreadsAndWaitUntilInterrupted();
                    runVNCProcessTermination();
                } catch (Exception e) {
                    Report.println(e.getMessage());
                }
                updateMainWindowLabel(ClientMainWindow.CONNECTION_LOST_LABEL);
            }
        }
    }

    private static void updateMainWindowLabel(String text) {
        SwingUtilities.invokeLater(() -> mainWindow.setLabelText(text));
    }

    private static void createClientWindow() {
        SwingUtilities.invokeLater(() -> mainWindow = new ClientMainWindow());
    }
    private static void checkIfVNCServerIsInstalled() {
        SoftwareInstalledChecker.showErrorIfNotInstalled(VNCServerProcessBuilder.VNC_SERVER_PROCESS_NAME);
    }
    private static void runVNCProcessTermination(){
        ProcessTerminator.terminateProcess(vncProcess, VNCServerProcessBuilder.VNC_SERVER_PROCESS_NAME);
    }

    private static ConnectionParameters getConnectionParamsFromDatagramMessage(DatagramPacket datagramPacket){
        return ConnectionParameters.from(new String(datagramPacket.getData(), 0, datagramPacket.getLength()));
    }
}
