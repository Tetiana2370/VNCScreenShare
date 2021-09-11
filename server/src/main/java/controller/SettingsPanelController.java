package controller;

import javax.swing.*;
import java.awt.*;

//TODO: decide if we need it
public class SettingsPanelController {

    public static final String WINDOW_LABEL = "Ustawienia";
    public static final int DEFAULT_HEIGHT = 300;
    private final JScrollPane logsPanel;
    private JTextArea logsTextArea;
    private final MainWindowController mainWindowController;

    SettingsPanelController(MainWindowController mainWindowController) {

        this.mainWindowController = mainWindowController;
        Container mainContainer = new Container();
        mainContainer.setLayout(new GridLayout(3, 1));

        Container previewSettingsContainer = new Container();
        mainContainer.setLayout(new BoxLayout(previewSettingsContainer, BoxLayout.PAGE_AXIS));
        
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

    void addText(String text) {
        this.logsTextArea.append(text + "\n");
    }
}
