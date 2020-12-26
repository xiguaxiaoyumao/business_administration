package com.util;

import javax.swing.*;
import java.awt.*;

/**
 * @author zht
 * @date 2020/5/8 11:26
 */
public class JTextFieldUser extends JTextField {
    private ImageIcon icon;

    public JTextFieldUser() {
        icon = new ImageIcon(getClass().getResource("/com/image/username.png"));
        this.setHorizontalAlignment(JTextField.CENTER);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        icon.paintIcon(this, g, 0, 0);
    }
}