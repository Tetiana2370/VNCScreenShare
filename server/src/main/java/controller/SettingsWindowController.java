package controller;

import model.ConnectionParameters;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.util.Locale;

public class SettingsWindowController extends JFrame {

    public final static int DEFAULT_FIELD_WIDTH = 6;
    public final static int DEFAULT_WINDOW_WIDTH = 350;
    public final static int DEFAULT_WINDOW_HEIGHT = 300;
    public final static String TITLE_PREFIX = "Ustawienia ";
    public final static String QUALITY_LABEL = "Jakość obrazu (0.1 - 1.0): ";
    public final static String PREVIEW_SCALE_LABEL = "Skala podglądu (0.1 - 0.5): ";
    public final static String FRAMES_PER_SECOND_LABEL = "FPS (1 - 20): ";
    public final static String SAVE_BUTTON_LABEL = "Zapisz";

    public final static double PREVIEW_SCALE_MIN = 0.1;
    public final static double PREVIEW_SCALE_MAX = 0.5;
    public final static double QUALITY_SCALE_MIN = 0.1;
    public final static double QUALITY_SCALE_MAX = 1.0;
    public final static double FRAMES_PER_SECOND_MIN = 1;
    public final static double FRAMES_PER_SECOND_MAX = 20;

    private final ConnectionParameters connectionParams;
    private boolean stateUpdated = false;
    private final JFormattedTextField qualityField;
    private final JFormattedTextField previewScaleField;
    private final JFormattedTextField framesPerSecondField;
    private final JLabel qualityLabel = new JLabel(QUALITY_LABEL);
    private final JLabel previewScaleLabel = new JLabel(PREVIEW_SCALE_LABEL);
    private final JLabel framesPerSecondLabel = new JLabel(FRAMES_PER_SECOND_LABEL);
    private final JButton saveButton;
    private final Insets INSETS = new Insets(5, 10, 5, 10);
    private boolean errorsOccurred = false;

    public SettingsWindowController(ConnectionParameters connectionParams) throws ParseException {
        super(TITLE_PREFIX);
        this.connectionParams = connectionParams;
        this.qualityField = createNumericField(QUALITY_SCALE_MIN, QUALITY_SCALE_MAX, 1);
        this.previewScaleField = createNumericField(PREVIEW_SCALE_MIN, PREVIEW_SCALE_MAX, 1);
        this.framesPerSecondField = createNumericField(FRAMES_PER_SECOND_MIN, FRAMES_PER_SECOND_MAX, 0);
        this.saveButton = createSaveButton();

        this.qualityField.setText(String.valueOf(ConnectionParameters.DEFAULT_SCREENSHOT_QUALITY));
        this.previewScaleField.setText(String.valueOf(ConnectionParameters.DEFAULT_SCREENSHOT_SCALE));
        this.framesPerSecondField.setText(String.valueOf(ConnectionParameters.DEFAULT_FRAMES_PER_SECOND));
        setLayout(new GridBagLayout());
        setComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT));
        setVisible(true);
    }

    private JFormattedTextField createNumericField(Double minValue, Double maxValue, int precision) throws ParseException {
        MaskFormatter maskFormatter = new MaskFormatter(precision == 0 ? "##" : "#.#");
        JFormattedTextField textField = new JFormattedTextField(maskFormatter);
        textField.setColumns(DEFAULT_FIELD_WIDTH);
        textField.setFocusLostBehavior(JFormattedTextField.PERSIST);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                JTextField textField = (JTextField) e.getComponent();
                if (e.getKeyChar() == KeyEvent.VK_TAB) {
                    validateNumericValue(textField, minValue, maxValue, precision);
                } else if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    textField.transferFocus();
                } else {
                    textField.setBackground(Color.WHITE);
                }
            }
        });
        return textField;
    }

    private JButton createSaveButton() {
        JButton button = new JButton(SAVE_BUTTON_LABEL);
        button.addActionListener(event -> setEnteredValues());
        return button;
    }

    private void setComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = INSETS;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addLabelAndFieldToGridBag(gbc, this.qualityLabel, this.qualityField);
        addLabelAndFieldToGridBag(gbc, this.previewScaleLabel, this.previewScaleField);
        addLabelAndFieldToGridBag(gbc, this.framesPerSecondLabel, this.framesPerSecondField);
        addComponent(gbc, this.saveButton);
    }

    private void addLabelAndFieldToGridBag(GridBagConstraints gbc, JLabel label, JTextField textField) {
        gbc.gridx = 0;
        gbc.gridy++;
        add(label, gbc);
        gbc.gridx++;
        add(textField, gbc);
    }

    private void addComponent(GridBagConstraints gbc, Component component) {
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        add(component, gbc);
    }

    private void setEnteredValues() {
        try {
            errorsOccurred = false;
            validateNumericValue(this.qualityField, QUALITY_SCALE_MIN, QUALITY_SCALE_MAX, 1);
            validateNumericValue(this.previewScaleField, PREVIEW_SCALE_MIN, PREVIEW_SCALE_MAX, 1);
            validateNumericValue(this.framesPerSecondField, FRAMES_PER_SECOND_MIN, FRAMES_PER_SECOND_MAX, 0);
            Float quality = parseFloatIfNotBlank(this.qualityField);
            Float previewScale = parseFloatIfNotBlank(this.previewScaleField);
            int framesPerSecond = Integer.parseInt(this.framesPerSecondField.getText().replaceAll(" ", ""));

            if (!errorsOccurred) {
                this.connectionParams.setQuality(quality);
                this.connectionParams.setScale(previewScale);
                this.connectionParams.setFramesPerSecond(framesPerSecond);
                super.dispose();
                this.stateUpdated = true;
            }
            errorsOccurred = true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validateNumericValue(JTextField textField, Double minValue, Double maxValue, int precision) {
        Float value = parseFloatIfNotBlank(textField);
        String numberFormat = "%." + precision + "f\n";
        try {
            if (value != null) {
                if (minValue != null && value < minValue) {
                    throw new Exception("Wartość musi być > " + String.format(numberFormat, minValue));
                }
                if (maxValue != null && value > maxValue) {
                    throw new Exception("Wartość musi być < " + String.format(numberFormat, maxValue));
                }
            } else {
                throw new Exception("Należy podać wartość");
            }
        } catch (Exception e) {
            e.printStackTrace();
            textField.setToolTipText(e.getMessage());
            textField.setBackground(Color.PINK);
            textField.grabFocus();
            errorsOccurred = true;
        }
    }

    public ConnectionParameters getConnectionParams() {
        return connectionParams;
    }

    Float parseFloatIfNotBlank(JTextField textField) {
        if (textField.getText() != null && !textField.getText().isBlank()) {
            return Float.parseFloat(textField.getText());
        } else {
            return null;
        }
    }

    private void setActualValues() {
        qualityField.setText(String.format(Locale.US, "%.2f", connectionParams.getQuality()));
        previewScaleField.setText(String.format(Locale.US, "%.2f", connectionParams.getScale()));
    }

    public boolean isUpdated() {
        return this.stateUpdated;
    }
}
