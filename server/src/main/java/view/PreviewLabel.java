package view;

import javax.swing.*;
import java.util.Objects;

public class PreviewLabel {

    public static final int DEFAULT_BORDER_SIZE = 10;
    public static final int DEFAULT_HORIZONTAL_TEXT_POSITION = SwingConstants.CENTER;
    public static final int DEFAULT_VERTICAL_TEXT_POSITION = SwingConstants.TOP;

    private final String ipAddress;
    private final JLabel label;

    public PreviewLabel(String ipAddress, ImageIcon imageIcon) {
        this.ipAddress = ipAddress;
        this.label = new JLabel();
        this.label.setText(ipAddress);
        this.label.setIcon(imageIcon);
        setDefaultStyle();
    }

    public void setDefaultStyle() {
        this.label.setBorder(BorderFactory.createEmptyBorder(DEFAULT_BORDER_SIZE, DEFAULT_BORDER_SIZE, DEFAULT_BORDER_SIZE, DEFAULT_BORDER_SIZE));
        this.label.setHorizontalTextPosition(DEFAULT_HORIZONTAL_TEXT_POSITION);
        this.label.setVerticalTextPosition(DEFAULT_VERTICAL_TEXT_POSITION);
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public JLabel getLabel() {
        return this.label;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        PreviewLabel that = (PreviewLabel) object;
        return this.ipAddress.equals(that.ipAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.ipAddress);
    }
}
