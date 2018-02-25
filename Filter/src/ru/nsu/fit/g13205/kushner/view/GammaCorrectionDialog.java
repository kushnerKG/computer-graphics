package ru.nsu.fit.g13205.kushner.view;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Created by Konstantin on 15.03.2016.
 */
public class GammaCorrectionDialog extends JDialog {
    private JButton okButton;
    private JButton cancelButton;

    private JTextField textField;
    private JSlider slider;

    public GammaCorrectionDialog(JFrame owner, MainArea mainArea) {
        JPanel panel = new JPanel();

        Box totalBox = Box.createVerticalBox();

        Box textBox = Box.createHorizontalBox();

        textBox.add(textField = new JTextField());
        textField.setText("1.0");
        textField.addFocusListener(focusLostHandler(textField, panel));

        totalBox.add(textBox);
        totalBox.add(Box.createVerticalStrut(10));

        Box sliderBox = Box.createHorizontalBox();
        this.slider = createSlider();
        sliderBox.add(slider);
        totalBox.add(sliderBox);
        totalBox.add(Box.createVerticalStrut(10));

        Box buttonBox = Box.createHorizontalBox();

        buttonBox.add(okButton = new JButton("OK"));
        buttonBox.add(Box.createHorizontalStrut(20));
        buttonBox.add(cancelButton = new JButton("Cancel"));
        totalBox.add(buttonBox);

        panel.add(totalBox);

        addListener();

        this.setModal(true);

        this.getContentPane().add(panel);
        this.setLocationRelativeTo(owner);
        this.pack();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                mainArea.setIsGammaCorrection(false);
            }
        });
    }

    public JButton getOkButton() {
        return okButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public double getGamma(){

        return Double.valueOf(textField.getText());
    }

    private JSlider createSlider(){
        int start = 0;
        int end = 1000;
        int value = 1;

        final JSlider slider = new JSlider(start, end, value);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(200);
        slider.setMinorTickSpacing(50);
        Dictionary<Integer, JLabel> labels = new Hashtable<>();
        for (int i = start; i <= end; i += 200) {
            String text = String.format("%3.2f", i / 100.0);
            labels.put(i, new JLabel(text));
        }

        slider.setLabelTable(labels);

        return slider;
    }

    private void addListener(){
        slider.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e) {
                textField.setText(String.valueOf(slider.getValue()/100d));
            }
        });
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double value = Double.valueOf(textField.getText());
                    slider.setValue((int)(value * 100));
                }catch (java.lang.NumberFormatException exp){

                }
            }
        });
    }

    private FocusAdapter focusLostHandler(JTextField field, JPanel owner) {
        return new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    double value = Double.valueOf(field.getText());

                    if (value < 0 || value > 10) {
                        Object[] options = {"OK"};
                        JOptionPane.showOptionDialog(owner,
                                "Значение должно быть в диапозрне от 0 до 10", "Warning",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                        field.setText("0.0");
                        field.requestFocusInWindow();
                    }
                    try {
                        if (field.getText().split(".")[1].length() > 2) {
                            Object[] options = {"OK"};
                            JOptionPane.showOptionDialog(owner,
                                    "Значение не должно содержать больше двух знаков после запятой", "Warning",
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                            field.setText("0.0");
                            field.requestFocusInWindow();
                        }
                    }catch (IndexOutOfBoundsException ignored){

                    }
                }catch (NumberFormatException exp){
                    Object[] options = {"OK"};
                    JOptionPane.showOptionDialog(owner,
                            "Значение должно быть числом", "Warning",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                    field.setText("0.0");
                    field.requestFocusInWindow();
                }
            }
        };
    }

}
