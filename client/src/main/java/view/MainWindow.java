package view;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    public final static int WINDOW_WIDTH = 1024;
    public final static int WINDOW_HEIGHT = 768;
    public final static String WINDOW_TITLE = "VNCScreenShare Server";

    public MainWindow(JScrollPane previewsPanel, JScrollPane logsPanel) {
        super(WINDOW_TITLE);
        Container mainContainer = getContentPane();
        
        //Container innerLeftContainer = connectionListPanel.createListScrollPane();
        Container innerRightContainer = new Container();
        innerRightContainer.setLayout(new BorderLayout());
        innerRightContainer.add(logsPanel, BorderLayout.SOUTH);
        innerRightContainer.add(previewsPanel);

        //mainContainer.add(innerLeftContainer, BorderLayout.WEST);
        mainContainer.add(innerRightContainer);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setVisible(true);
    }


}
