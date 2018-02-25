package ru.nsu.fit.g13205.kushner.view;


import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

/**
 * Created by Konstantin on 14.02.2016.
 */
public class AssociatedSliderAndTextField extends JPanel {

    private final JSlider slider;
    private final JTextField  textField;

    private final int max;
    private final int min;
    private final String title;

    public AssociatedSliderAndTextField(int min, int max, int value, String title) {
        this.max = max;
        this.min = min;
        this.title = title;
        this.setBorder(new TitledBorder(title));

        slider = createSlider(min, max, value);
        textField = createTextField(value);
        addListener();

        this.add(adjustLocation());
    }

    public JSlider getSlider() {
        return slider;
    }

    public JTextField getTextField() {
        return textField;
    }

   public int getValue(){
        return slider.getValue();
    }

    private JSlider createSlider(int min, int max, int value){
        JSlider slider = new JSlider(min, max, value);
        slider.setMajorTickSpacing((max - min) );
        slider.setMinorTickSpacing((max - min) / 20);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        return slider;
    }

    private JTextField createTextField(int value){
        NumberFormat integerFieldFormatter = NumberFormat.getIntegerInstance();
        integerFieldFormatter.setMaximumIntegerDigits(max);
        integerFieldFormatter.setMinimumFractionDigits(min);

        //JFormattedTextField textField = new JFormattedTextField(integerFieldFormatter );
        JTextField textField = new JTextField();
        //textField.setValue(value);
        textField.setText(String.valueOf(value));
        textField.setColumns(5);
        textField.setFont(MainWindow.font);

        return textField;
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
                    System.out.println("неправильный формат");
                }
            }
        });
    }

    private JComponent adjustLocation(){
        Box totalBox = Box.createVerticalBox();

        Box box1 = Box.createHorizontalBox();
        box1.add(textField);
        box1.add(Box.createHorizontalGlue());
        totalBox.add(box1);

        totalBox.add(Box.createVerticalStrut(20));

        Box box2 = Box.createHorizontalBox();
        box2.add(slider);
        totalBox.add(box2);

        return totalBox;
    }
}
