package view;

import javax.swing.*;
import java.awt.*;

public class PreviewRenderer implements ListCellRenderer<PreviewLabel> {

    @Override
    public Component getListCellRendererComponent(JList<? extends PreviewLabel> list, PreviewLabel value, int index, boolean isSelected, boolean cellHasFocus) {
        return value.getLabel();
    }
}
