package ru.nsu.fit.g13205.kushner.view;

import ru.nsu.fit.g13205.kushner.utils.Function;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Created by Konstantin on 24.03.2016.
 */
public class SettingsDialog extends JDialog {

    private JTextField kTextField;
    private JTextField mTextField;
    private JTextField aTextField;
    private JTextField bTextField;
    private JTextField cTextField;
    private JTextField dTextField;


    private JButton okButton;
    private JButton cancelButton;

    public SettingsDialog(String title, JFrame owner, int currentK, int currentM, double currentA, double currentB, double currentC,
                          double currentD) {
        JPanel panel = new JPanel();
        Box totalBox = Box.createVerticalBox();

        Box kBox = createTextBox(String.valueOf(currentK), "K:", kTextField = new JTextField());
        Box mBox = createTextBox(String.valueOf(currentM), "M:", mTextField = new JTextField());
        Box aBox = createTextBox(String.valueOf(currentA), "A:", aTextField = new JTextField());
        Box bBox = createTextBox(String.valueOf(currentB), "B:", bTextField = new JTextField());
        Box cBox = createTextBox(String.valueOf(currentC), "C:", cTextField = new JTextField());
        Box dBox = createTextBox(String.valueOf(currentD), "D:", dTextField = new JTextField());

        //focusLostHandlerForABCD(JTextField field1, JTextField field2, JPanel owner, int defaultValue) {
        aTextField.addFocusListener(focusLostHandlerForABCD(aTextField, aTextField, bTextField, panel, String.valueOf(Function.A)));
        bTextField.addFocusListener(focusLostHandlerForABCD(bTextField, aTextField, bTextField, panel, String.valueOf(Function.B)));
        cTextField.addFocusListener(focusLostHandlerForABCD(cTextField, cTextField, dTextField, panel, String.valueOf(Function.C)));
        dTextField.addFocusListener(focusLostHandlerForABCD(dTextField, cTextField, dTextField, panel, String.valueOf(Function.D)));
        kTextField.addFocusListener(focusLostHandlerForKM(kTextField, panel, String.valueOf(currentK)));
        mTextField.addFocusListener(focusLostHandlerForKM(mTextField, panel, String.valueOf(currentM)));

        totalBox.add(kBox);
        totalBox.add(mBox);
        totalBox.add(aBox);
        totalBox.add(bBox);
        totalBox.add(cBox);
        totalBox.add(dBox);

        Box buttonBox = Box.createHorizontalBox();

        buttonBox.add(okButton = new JButton("OK"));
        buttonBox.add(Box.createHorizontalStrut(20));
        buttonBox.add(cancelButton = new JButton("Close"));

        totalBox.add(Box.createVerticalStrut(10));
        totalBox.add(buttonBox);

        panel.add(totalBox);

        this.setModal(true);
        this.getContentPane().add(panel);
        this.setLocationRelativeTo(owner);
        this.pack();
    }

    private Box createTextBox(String defaultValue, String name, JTextField textField){

        Box textBox = Box.createHorizontalBox();
        textBox.add(Box.createHorizontalStrut(10));
        textBox.add(new JLabel(name));
        textBox.add(Box.createHorizontalStrut(10));
        textField.setColumns(4);
        textBox.add(textField);
        textField.setText(defaultValue);
        textBox.add(Box.createHorizontalStrut(10));

        return textBox;
    }

    public JButton getOkButton() {
        return okButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public int getK(){
        return Integer.valueOf(kTextField.getText());
    }

    public int getM(){
        return Integer.valueOf(mTextField.getText());
    }

    public double getA(){
        return Double.valueOf(aTextField.getText());
    }

    public double getB(){
        return Double.valueOf(bTextField.getText());
    }

    public double getC(){
        return Double.valueOf(cTextField.getText());
    }

    public double getD(){
        return Double.valueOf(dTextField.getText());
    }

    private FocusAdapter focusLostHandlerForABCD(JTextField textField,JTextField field1, JTextField field2, JPanel owner, String defaultValue) {
        return new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {

                try {
                    double value1 = Double.valueOf(field1.getText());
                    double value2 = Double.valueOf(field2.getText());



                    if (value1 > value2) {
                        Object[] options = {"OK"};
                        JOptionPane.showOptionDialog(owner,
                                String.format("A, B должно быть меньше C, D соотвественно"), "Warning",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                        textField.setText(defaultValue);

                    }
                }catch (NumberFormatException exp){
                    Object[] options = {"OK"};
                    JOptionPane.showOptionDialog(owner,
                            "Значение должно быть числом", "Warning",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                    textField.setText(defaultValue);
                    textField.requestFocusInWindow();
                }catch (Exception exp1){
                    exp1.printStackTrace();
                }
            }
        };
    }

    private FocusAdapter focusLostHandlerForKM(JTextField textField, JPanel owner, String defaultValue) {
        return new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {

                try {
                    double value = Double.valueOf(textField.getText());

                    if (value <= 0) {
                        Object[] options = {"OK"};
                        JOptionPane.showOptionDialog(owner,
                                String.format("Значение должно быть больше нуля"), "Warning",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                        textField.setText(defaultValue);

                    }
                }catch (NumberFormatException exp){
                    Object[] options = {"OK"};
                    JOptionPane.showOptionDialog(owner,
                            "Значение должно быть числом", "Warning",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                    textField.setText(defaultValue);
                    textField.requestFocusInWindow();
                }catch (Exception exp1){
                    exp1.printStackTrace();
                }
            }
        };
    }

}
