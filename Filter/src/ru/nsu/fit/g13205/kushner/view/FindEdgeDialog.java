package ru.nsu.fit.g13205.kushner.view;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Created by Konstantin on 14.03.2016.
 */
public class FindEdgeDialog extends JDialog{
    private JButton okButton;
    private JButton cancelButton;
    private JTextField textField;
    private JSlider slider;

    public FindEdgeDialog(String title, JFrame owner, int start, int finish, int value) {
        JPanel panel = new JPanel();

        Box totalBox = Box.createVerticalBox();
        Box textBox = Box.createHorizontalBox();

        textBox.add(textField = new JTextField());
        textField.setText("11");
        textField.addFocusListener(focusLostHandler(textField, panel));

        totalBox.add(textBox);
        totalBox.add(Box.createVerticalStrut(10));

        Box sliderBox = Box.createHorizontalBox();
        this.slider = createSlider();
        sliderBox.add(slider);
        totalBox.add(sliderBox);
        Box buttonBox = Box.createHorizontalBox();

        buttonBox.add(okButton = new JButton("OK"));
        buttonBox.add(Box.createHorizontalStrut(20));
        buttonBox.add(cancelButton = new JButton("Close dialog"));
        totalBox.add(buttonBox);

        addListener();

        panel.add(totalBox);

        this.setModal(true);

        this.getContentPane().add(panel);
        this.setLocationRelativeTo(owner);
        this.pack();
    }

    public JButton getOkButton() {
        return okButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public int getThreshold(){
        return Integer.valueOf(textField.getText());
    }


    private JSlider createSlider(){
        int start = 0;
        int end = 360;
        int value = 11;

        final JSlider slider = new JSlider(start, end, value);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(72);
        slider.setMinorTickSpacing(36);

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

    private FocusAdapter focusLostHandler(JTextField field, JPanel owner) {
        return new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    double value = Double.valueOf(field.getText());

                    if (value < 0 || value > 360) {
                        Object[] options = {"OK"};
                        JOptionPane.showOptionDialog(owner,
                                "Значение должно быть в диапозрне от 0 до 360", "Warning",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                        field.setText("0.0");
                        field.requestFocusInWindow();
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
