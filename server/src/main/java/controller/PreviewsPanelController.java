package controller;

import model.ConnectionParams;
import view.PreviewRenderer;
import view.PreviewLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PreviewsPanelController {

    public static final String PREVIEWS_PANEL_LABEL = "Panel podglądów";

    private final DefaultListModel<PreviewLabel> previewsListModel;
    private final JScrollPane previewsPanel;
    private final MainWindowController mainWindowController;
    private final ConnectionParams connectionParams;


    public PreviewsPanelController(MainWindowController mainWindowController, ConnectionParams connectionParams) {
        this.previewsListModel = new DefaultListModel<>();
        this.previewsPanel = new JScrollPane(createPreviewsList());
        this.previewsPanel.setBorder(BorderFactory.createTitledBorder(PREVIEWS_PANEL_LABEL));
        this.mainWindowController = mainWindowController;
        this.connectionParams = connectionParams;
    }

    private JList<PreviewLabel> createPreviewsList() {
        JList<PreviewLabel> list = new JList<>(this.previewsListModel);
        list.setCellRenderer(new PreviewRenderer());
        list.setVisibleRowCount(0);
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (clickedOnListElement(list, event)) {
                    System.out.println(mainWindowController.ipAddressToStateMap.toString());
                    PreviewLabel previewLabel = previewsListModel.get(list.getSelectedIndex());
                    if (previewLabel.getIpAddress().equals(mainWindowController.getActiveVNCConnection())) {
                        return;
                    }
                    if (!mainWindowController.ipAddressToStateMap.containsKey(previewLabel.getIpAddress())) {
                        mainWindowController.ipAddressToStateMap.put(previewLabel.getIpAddress(), new ConnectionParams());
                        mainWindowController.setActiveVNCConnection(previewLabel.getIpAddress());
                    }
                    runVncProcess(previewLabel.getIpAddress());
                }
            }
        });
        return list;
    }

    private boolean clickedOnListElement(JList<PreviewLabel> list, MouseEvent e) {
        Rectangle r = list.getCellBounds(0, list.getLastVisibleIndex());
        return r != null && r.contains(e.getPoint());
    }

    JScrollPane getPreviewsPanel() {
        return this.previewsPanel;
    }

    void removePreview(String ipAddress) {
        for (int i = 0; i < previewsListModel.size(); i++) {
            if (ipAddress.equals(previewsListModel.get(i).getIpAddress())) {
                previewsListModel.removeElementAt(i);
                break;
            }
        }
    }

    void updatePreview(String ipAddress, ImageIcon imageIcon) {
        boolean previewExists = false;
        for (int i = 0; i < previewsListModel.size(); i++) {
            PreviewLabel previewLabel = previewsListModel.get(i);
            if (previewLabel.getIpAddress().equals(ipAddress)) {
                JLabel label = previewLabel.getLabel();
                label.setPreferredSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight()));
                label.setIcon(imageIcon);
                previewsPanel.updateUI();
                previewExists = true;
            }
        }
        if (!previewExists) {
            previewsListModel.addElement(new PreviewLabel(ipAddress, imageIcon));
        }
    }

    private void runVncProcess(String ipAddress){
        SwingUtilities.invokeLater(() -> mainWindowController.runVncProcess(ipAddress));
    }
}
