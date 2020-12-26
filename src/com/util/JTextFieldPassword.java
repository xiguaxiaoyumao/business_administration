package com.util;

import javax.swing.*;
import java.awt.*;

/**
 * @author zht
 * @date 2020/5/8 11:47
 */

public class JTextFieldPassword extends JPasswordField {
    private ImageIcon icon;

    public JTextFieldPassword() {
        icon = new ImageIcon(getClass().getResource("/com/image/password.png"));
        this.setHorizontalAlignment(JTextField.CENTER);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        icon.paintIcon(this, g, 0, 0);
    }
}

