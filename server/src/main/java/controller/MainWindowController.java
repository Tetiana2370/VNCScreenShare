package controller;

import model.ConnectionParameters;
import model.Event;
import model.ProcessTerminator;
import model.VNCViewerProcess;
import view.MainWindow;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainWindowController {

    private DefaultListModel<String> ipAddressListModel;
    private List<String> connectedIpAddressList;
    private JList<String> connectedIpList;
    private MainWindow mainWindow;

    private String activeVNCConnection;
    Map<String, ConnectionParameters> ipAddressToStateMap;
    private PreviewsPanelController previewsPanelController;
    private LogsPanelController logsPanelController;
    private VNCViewerProcess vncViewerProcess;
    private ConnectionParameters connectionParams;

    public MainWindowController(ConnectionParameters connectionParams) {
        this.connectionParams = connectionParams;
        this.ipAddressListModel = new DefaultListModel<>();
        this.connectedIpAddressList = new ArrayList<>();
        this.ipAddressToStateMap = new HashMap<>();
        this.connectedIpList = new JList<>(ipAddressListModel);
        this.ipAddressToStateMap = new HashMap<>();
        this.previewsPanelController = new PreviewsPanelController(this, connectionParams);
        this.logsPanelController = new LogsPanelController();
        this.mainWindow = new MainWindow(this.previewsPanelController.getPreviewsPanel(), this.logsPanelController.getPanel());
    }

    public void updatePreview(String ipAddress, ImageIcon preview) {
        if (!ipAddressListModel.contains(ipAddress)) {
            logEvent(Event.CONNECTED, ipAddress);
            ipAddressListModel.addElement(ipAddress);
        }
        this.previewsPanelController.updatePreview(ipAddress, preview);
    }

    // TODO check why list element moves to top when host disconnects (not last in list)
    public void removePreview(String ipAddress) {
        removeConnection(ipAddress);
        connectedIpAddressList.remove(ipAddress);
        if (ipAddressListModel.removeElement(ipAddress)) {
            logEvent(Event.DISCONNECTED, ipAddress);
        }
    }

    public void setConnectionState(String ipAddress, ConnectionParameters connectionParams) {
        ipAddressToStateMap.put(ipAddress, connectionParams);
    }

    public void addNewConnectionToList(String ipAddress) {
        connectedIpAddressList.add(ipAddress);
    }

    public boolean isConnected(String ipAddress) {
        return connectedIpAddressList.contains(ipAddress);
    }

    void removeConnection(String ipAddress) {
        if (this.activeVNCConnection != null && this.activeVNCConnection.equals(ipAddress)) {
            this.activeVNCConnection = null;
        }
        this.ipAddressToStateMap.remove(ipAddress);
        this.previewsPanelController.removePreview(ipAddress);
    }

    public String getActiveVNCConnection() {
        return this.activeVNCConnection;
    }

    void setActiveVNCConnection(String ipAddress) {
        this.activeVNCConnection = ipAddress;
    }

    void logEvent(String event, String ipAddress){
        this.logsPanelController.logEvent(event, ipAddress);
    }

    void runVncProcess(String ipAddress) {
        if (vncViewerProcess != null && vncViewerProcess.getProcess().isAlive()) {
            if (!vncViewerProcess.getIpAddress().equals(ipAddress)) {
                this.vncViewerProcess.getProcess().destroy();
            } else {
                return;
            }
        }
        try {
            this.vncViewerProcess = new VNCViewerProcess(ipAddress, connectionParams);
            Process process = this.vncViewerProcess.start();
            Runtime.getRuntime().addShutdownHook(new ProcessTerminator(process, VNCViewerProcess.COMMAND_CMD));
        } catch (IOException e) {
            logEvent("Connection failed -> " + e.getMessage(), ipAddress);
        }
    }
}
