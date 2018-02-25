package ru.nsu.fit.g13205.kushner.view;

import ru.nsu.fit.g13205.kushner.Settings;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Created by Konstantin on 17.03.2016.
 */
public class DitheringDialog extends JDialog {

    private JButton okButton;
    private JButton cancelButton;

    private JTextField redField;
    private JTextField greenField;
    private JTextField blueField;

    public DitheringDialog(String title, JFrame owner, int start, int finish) {
        super();
        JPanel panel = new JPanel();

        Box totalBox = Box.createVerticalBox();

        Box redBox = createTextBox(Settings.DEFAULT_R_DITHERING, "Nr:", redField = new JTextField());
        Box greenBox = createTextBox(Settings.DEFAULT_G_DITHERING, "Ng:", greenField = new JTextField());
        Box blueBox = createTextBox(Settings.DEFAULT_B_DITHERING, "Nb:", blueField = new JTextField());

        redField.addFocusListener(focusLostHandler(redField, panel, start, finish, Settings.DEFAULT_R_DITHERING));
        greenField.addFocusListener(focusLostHandler(greenField, panel, start, finish, Settings.DEFAULT_G_DITHERING));
        blueField.addFocusListener(focusLostHandler(blueField, panel, start, finish, Settings.DEFAULT_B_DITHERING));

        totalBox.add(redBox);
        totalBox.add(Box.createVerticalStrut(5));
        totalBox.add(greenBox);
        totalBox.add(Box.createVerticalStrut(5));
        totalBox.add(blueBox);
        totalBox.add(Box.createVerticalStrut(5));

        Box buttonBox = Box.createHorizontalBox();

        buttonBox.add(okButton = new JButton("OK"));
        buttonBox.add(Box.createHorizontalStrut(20));
        buttonBox.add(cancelButton = new JButton("Close dialog"));

        totalBox.add(Box.createVerticalStrut(10));
        totalBox.add(buttonBox);

        panel.add(totalBox);

        this.setModal(true);

        this.getContentPane().add(panel);
        this.setLocationRelativeTo(owner);
        this.pack();
    }

    private Box createTextBox(int defaultValue, String name, JTextField textField){

        Box textBox = Box.createHorizontalBox();
        textBox.add(Box.createHorizontalStrut(10));
        textBox.add(new JLabel(name));
        textBox.add(Box.createHorizontalStrut(10));
        textField.setColumns(4);
        textBox.add(textField);
        textField.setText(String.valueOf(defaultValue));
        textBox.add(Box.createHorizontalStrut(10));

        return textBox;
    }

    public JButton getOkButton() {
        return okButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
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

    public int getRed(){
        return Integer.valueOf(redField.getText());
    }

    public int getGreen(){
        return Integer.valueOf(greenField.getText());
    }

    public int getBlue(){
        return Integer.valueOf(blueField.getText());
    }

}
