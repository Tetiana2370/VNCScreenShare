package controller;

import javax.swing.*;
import java.awt.*;

public class LogsPanelController {

    public static final String WINDOW_LABEL = "Rejestr zdarzeń";
    public static final int DEFAULT_HEIGHT = 300;
    private final JScrollPane logsPanel;
    private final JTextArea logsTextArea;

    public LogsPanelController() {
        this.logsTextArea = new JTextArea();
        this.logsTextArea.setEditable(false);
        this.logsPanel = new JScrollPane(logsTextArea);
        Dimension textAreaSize = logsPanel.getPreferredSize();
        textAreaSize.height = DEFAULT_HEIGHT;
        this.logsPanel.setPreferredSize(textAreaSize);
        this.logsPanel.setBorder(BorderFactory.createTitledBorder(WINDOW_LABEL));
    }

    JScrollPane getPanel() {
        return this.logsPanel;
    }

    void logEvent(String event, String ipAddress){
        this.logsTextArea.append(ipAddress + " : " + event + "\n");
    }

    void logEvent(String event){
        this.logsTextArea.append(event + "\n");
    }
}
