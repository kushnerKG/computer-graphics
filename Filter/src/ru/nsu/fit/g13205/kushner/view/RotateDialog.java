package ru.nsu.fit.g13205.kushner.view;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;

/**
 * Created by Konstantin on 18.03.2016.
 */
public class RotateDialog extends JDialog {
    private JButton okButton;
    private JButton cancelButton;
    private JTextField textField;
    private JSlider slider;

    public RotateDialog(String title, JFrame owner, int start, int finish, int value, MainArea mainArea) {
        JPanel panel = new JPanel();

        Box totalBox = Box.createVerticalBox();
        Box textBox = Box.createHorizontalBox();

        textBox.add(textField = new JTextField());
        textField.setText(String.valueOf(value));
        textField.addFocusListener(focusLostHandler(textField, panel, start, finish, value));

        totalBox.add(textBox);
        totalBox.add(Box.createVerticalStrut(10));

        Box sliderBox = Box.createHorizontalBox();
        this.slider = createSlider(start, finish, value);
        sliderBox.add(slider);
        totalBox.add(sliderBox);
        Box buttonBox = Box.createHorizontalBox();

        buttonBox.add(okButton = new JButton("OK"));
        buttonBox.add(Box.createHorizontalStrut(20));
        buttonBox.add(cancelButton = new JButton("Cancel"));
        totalBox.add(buttonBox);

        addListener();

        panel.add(totalBox);

        this.setModal(true);

        this.getContentPane().add(panel);
        this.setLocationRelativeTo(owner);
        this.pack();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                mainArea.setRotate(false);
            }
        });

    }

    public JButton getOkButton() {
        return okButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public int getAngle(){
        return Integer.valueOf(textField.getText());
    }


    private JSlider createSlider(int start, int end, int value){

        final JSlider slider = new JSlider(start, end, value);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(60);
        slider.setMinorTickSpacing(30);

        return slider;
    }

    private void addListener(){
        slider.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e) {
                textField.setText(String.valueOf(slider.getValue()));
            }
        });
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int value = Integer.valueOf(textField.getText());
                    slider.setValue(value);
                }catch (java.lang.NumberFormatException exp){

                }
            }
        });
    }

    private FocusAdapter focusLostHandler(JTextField field, JPanel owner, int start, int finish, int defaultValue) {
        return new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    double value = Double.valueOf(field.getText());

                    if (value < start || value > finish) {
                        Object[] options = {"OK"};
                        JOptionPane.showOptionDialog(owner,
                                String.format("Значение должно быть в диапозрне от %d до %d", start, finish), "Warning",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                        field.setText(String.valueOf(defaultValue));
                        field.requestFocusInWindow();
                    }
                }catch (NumberFormatException exp){
                    Object[] options = {"OK"};
                    JOptionPane.showOptionDialog(owner,
                            "Значение должно быть числом", "Warning",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                    field.setText(String.valueOf(defaultValue));
                    field.requestFocusInWindow();
                }
            }
        };
    }

}
