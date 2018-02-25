package ru.nsu.fit.g13205.kushner.view;

import ru.nsu.cg.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;

/**
 * Created by Konstantin on 24.03.2016.
 */
public class MyMainFrame extends MainFrame {

    public MyMainFrame(int x, int y, String title) {
        super(x, y, title);
        toolBar.setFloatable(false);
    }

    public void addCheckBoxMenuItem(String title, String tooltip, int mnemonic, String icon, String actionMethod, JLabel statusBar) throws SecurityException, NoSuchMethodException
    {
        MenuElement element = getParentMenuElement(title);
        if(element == null)
            throw new InvalidParameterException("Menu path not found: "+title);
        JMenuItem item = createCheckBoxMenuItem(getMenuPathName(title), tooltip, mnemonic, icon, actionMethod, statusBar);
        if(element instanceof JMenu)
            ((JMenu)element).add(item);
        else if(element instanceof JPopupMenu)
            ((JPopupMenu)element).add(item);
        else
            throw new InvalidParameterException("Invalid menu path: "+title);
    }


    public void addCheckBoxMenuItem(String title, String tooltip, int mnemonic, String actionMethod, JLabel statusBar) throws SecurityException, NoSuchMethodException
    {
        addCheckBoxMenuItem(title, tooltip, mnemonic, null, actionMethod, statusBar);
    }

    public JMenuItem createCheckBoxMenuItem(String title, String tooltip, int mnemonic, String icon, String actionMethod, JLabel statusBar) throws SecurityException, NoSuchMethodException
    {
        JCheckBoxMenuItem item = new JCheckBoxMenuItem(title);

        item.setMnemonic(mnemonic);
        item.setToolTipText(tooltip);
        if(icon != null)
            item.setIcon(new ImageIcon(getClass().getResource("./resources/"+icon), title));
        final Method method = getClass().getMethod(actionMethod);
        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    method.invoke(MyMainFrame.this);
                } catch (Exception e) {
                    //throw new RuntimeException(e);
                }
            }
        });

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                statusBar.setText(item.getToolTipText());
            }
        });

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                statusBar.setText("Status bar");
            }
        });

        return item;
    }

    public JButton createToolBarButton(JMenuItem item, String iconPath, JLabel statusBar) {
        JButton button = new JButton(new ImageIcon(iconPath));
        for(ActionListener listener: item.getActionListeners())
            button.addActionListener(listener);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                statusBar.setText(item.getToolTipText());
            }
        });

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                statusBar.setText("Status bar");
            }
        });



        button.setToolTipText(item.getToolTipText());
        return button;
    }

    public JButton createToolBarButton(String menuPath, String iconPath, JLabel statusBar)
    {
        JMenuItem item = (JMenuItem)getMenuElement(menuPath);
        if(item == null)
            throw new InvalidParameterException("Menu path not found: "+menuPath);
        return createToolBarButton(item, iconPath, statusBar);
    }


    public JButton addToolBarButton(String menuPath, String iconPath, JLabel statusBar)
    {
        JButton tmp = createToolBarButton(menuPath, iconPath, statusBar);
        toolBar.add(tmp);
        return tmp;
    }


    /////////////////////////////////

    public JToggleButton createToolBarToggleButton(JMenuItem item, String iconPath, JLabel statusBar) {
        JToggleButton button = new JToggleButton(new ImageIcon(iconPath));
        for(ActionListener listener: item.getActionListeners())
            button.addActionListener(listener);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                statusBar.setText(item.getToolTipText());
            }
        });

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                statusBar.setText("Status bar");
            }
        });


        button.setToolTipText(item.getToolTipText());

        return button;
    }

    public JToggleButton createToolBarToggleButton(String menuPath, String iconPath, JLabel statusBar)
    {
        JMenuItem item = (JMenuItem)getMenuElement(menuPath);
        if(item == null)
            throw new InvalidParameterException("Menu path not found: "+menuPath);
        return createToolBarToggleButton(item, iconPath, statusBar);
    }


    public JToggleButton addToolBarToggleButton(String menuPath, String iconPath, JLabel statusBar)
    {
        JToggleButton button = createToolBarToggleButton(menuPath, iconPath, statusBar);
        toolBar.add(button);
        return button;
    }
    /////////////////////////////


    public void addMenuItem(String title, String tooltip, int mnemonic, String icon, String actionMethod, JLabel statusBar) throws SecurityException, NoSuchMethodException
    {
        MenuElement element = getParentMenuElement(title);
        if(element == null)
            throw new InvalidParameterException("Menu path not found: "+title);
        JMenuItem item = createMenuItem(getMenuPathName(title), tooltip, mnemonic, icon, actionMethod);
        if(element instanceof JMenu)
            ((JMenu)element).add(item);
        else if(element instanceof JPopupMenu)
            ((JPopupMenu)element).add(item);
        else
            throw new InvalidParameterException("Invalid menu path: "+title);

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                statusBar.setText(item.getToolTipText());
            }
        });

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                statusBar.setText("Status bar");
            }
        });


    }

    /**
     * Creates menu item (without icon) and adds it to the specified menu location
     * @param title - menu item title with full path
     * @param tooltip - floating tooltip describing menu item
     * @param mnemonic - mnemonic key to activate item via keyboard
     * @param actionMethod - String containing method name which will be called when menu item is activated (method should not take any parameters)
     * @throws NoSuchMethodException - when actionMethod method not found
     * @throws SecurityException - when actionMethod method is inaccessible
     * @throws InvalidParameterException - when specified menu location not found
     */
    public void addMenuItem(String title, String tooltip, int mnemonic, String actionMethod, JLabel statusBar) throws SecurityException, NoSuchMethodException
    {
        addMenuItem(title, tooltip, mnemonic, null, actionMethod, statusBar);
    }
}
