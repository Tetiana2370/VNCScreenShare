package view;

import javax.swing.*;
import java.awt.*;

public class ServerMainWindow extends JFrame {
    public final static int DEFAULT_WINDOW_WIDTH = 500;
    public final static int DEFAULT_WINDOW_HEIGHT = 300;

    public final static String WINDOW_TITLE = "VNCScreenShare Server";
    public final static String CLIENT_AWAITS_FOR_CONNECTION_LABEL = "Program oczekuje na połączenia wchodządce";
    public final static String CONNECTION_LOST_LABEL = "Program utracił połączenie z klientem. Trwa próba ponownego połączenia";
    public final static String CONNECTION_ACTIVE_LABEL = "Program nawiązał połączenie z klientem. " +
            "Trwa przekazywanie obrazu i kontroli nad myszą i klawiaturą na bieżąco. " +
            "Aby zakończyć przekazywanie, wyłącz aplikację";

    private final JLabel textLabel;

    public ServerMainWindow() {
        super(WINDOW_TITLE);
        this.textLabel = new JLabel(prepareLabelHtml(CLIENT_AWAITS_FOR_CONNECTION_LABEL), SwingConstants.CENTER);
        this.textLabel.setPreferredSize(new Dimension(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);

        Panel panel = new Panel();
        add(this.textLabel, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setLabelText(String text){
        this.textLabel.setText(prepareLabelHtml(text));
    }

    public static String prepareLabelHtml(String label){
        return "<html>" +  label.replaceAll("\\.", "\\. <br>") + "</html>";
    }

}
