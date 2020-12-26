package com.main;

import com.alee.laf.WebLookAndFeel;
import com.jdbc.JDBC;
import com.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginView extends JFrame {

    private ImageIcon icon = new ImageIcon(this.getClass().getResource("/com/image/background.jpg"));
    private JPanel backPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(icon.getImage(), 0, 0, 800, 496, this);
        }
    };
    private JLayeredPane jLayeredPane;
    private final String LIMIT_LOGIN_STRING = "0";
    //JDBC
    private Connection connection = null;

    private JTextField usernameField = new JTextFieldUser();
    private JPasswordField passwordField = new JTextFieldPassword();

    private JRadioButton userRadioButton = new JRadioButton("用户");
    private JRadioButton adminRadioButton = new JRadioButton("管理员");
    private ButtonGroup limitGroup = new ButtonGroup();

    private JButton loginButton = new JButton("登录");
    private JButton registerButton = new JButton("注册");

    private ImageIcon titleIcon = new ImageIcon(getClass().getResource("/com/image/business.jpg"));

    //构造函数
    public LoginView() {
        this.setLayout(null);
        this.setTitle("工商管理系统登录");
        this.setSize(800, 534);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(this.titleIcon.getImage());
        this.setResizable(false);
        this.backPanel.setBounds(0, 0, 800, 496);
        this.jLayeredPane = new JLayeredPane();
        this.setLayeredPane(this.jLayeredPane);

        this.usernameField.setBounds(500, 150, 200, 32);
        this.usernameField.addFocusListener(new JTextFieldHintListener(this.usernameField, "请输入用户名"));
        this.passwordField.setBounds(500, 200, 200, 32);
        this.passwordField.addFocusListener(new JPasswordFieldHintListener(this.passwordField, "请输入密码"));
        this.usernameField.setOpaque(false);
        this.passwordField.setOpaque(false);
        this.usernameField.setForeground(Color.RED);
        this.passwordField.setForeground(Color.RED);
        this.userRadioButton.setBounds(560, 235, 80, 20);
        this.adminRadioButton.setBounds(640, 235, 80, 20);
        this.userRadioButton.setOpaque(false);
        this.adminRadioButton.setOpaque(false);
        this.userRadioButton.setForeground(Color.red);
        this.adminRadioButton.setForeground(Color.red);
        this.limitGroup.add(this.userRadioButton);
        this.limitGroup.add(this.adminRadioButton);
        this.loginButton.setBounds(500, 260, 60, 32);
        this.registerButton.setBounds(640, 260, 60, 32);
        this.userRadioButton.setSelected(true);
        this.loginButton.setOpaque(false);
        this.loginButton.setForeground(Color.RED);
        this.registerButton.setOpaque(false);
        this.registerButton.setForeground(Color.RED);


        this.jLayeredPane.add(this.backPanel, JLayeredPane.DEFAULT_LAYER);
        this.jLayeredPane.add(this.usernameField, JLayeredPane.MODAL_LAYER);
        this.jLayeredPane.add(this.passwordField, JLayeredPane.MODAL_LAYER);
        this.jLayeredPane.add(this.userRadioButton, JLayeredPane.MODAL_LAYER);
        this.jLayeredPane.add(this.adminRadioButton, JLayeredPane.MODAL_LAYER);
        this.jLayeredPane.add(this.loginButton, JLayeredPane.MODAL_LAYER);
        this.jLayeredPane.add(this.registerButton, JLayeredPane.MODAL_LAYER);

        //登录的监听事件
        loginButton.addActionListener((e) ->
        {
            this.login();
        });

        this.usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    login();
                }
            }
        });

        this.passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    login();
                }
            }
        });

        //注册的监听事件
        registerButton.addActionListener((e) ->
        {
            new RegisterDialog();
        });

        setVisible(true);
    }

    //登录
    private void login() {
        //判断填写的用户名是否为空
        if (usernameField.getText().isEmpty() || usernameField.getText().equals("请输入用户名")) {
            return;
        }
        if (passwordField.getText().isEmpty() || passwordField.getText().equals("请输入密码")) {
            return;
        }
        //JDBC
        try {
            connection = JDBC.getConnection();
            String SQL;
            if (userRadioButton.isSelected()) {
                SQL = "select * from user where username = ? and userpassword = ?";
            } else {
                SQL = "select * from admin where adminname = ? and adminpassword = ?";
            }
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, usernameField.getText());
            preparedStatement.setObject(2, passwordField.getText());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                if (userRadioButton.isSelected()) {
                    if (resultSet.getString(8).equals(LIMIT_LOGIN_STRING)) {
                        JOptionPane.showMessageDialog(null, "您已被限制登录", "系统警告", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    new UserFrame(usernameField.getText());
                    Connection connection1 = JDBC.getConnection();
                    try {
                        String SQL2 = "update user set LASTLOGINTIME = ? where username = ?";
                        PreparedStatement preparedStatement2 = connection1.prepareStatement(SQL2);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = dateFormat.format(new Date());
                        preparedStatement2.setObject(1, date);
                        preparedStatement2.setObject(2, usernameField.getText());
                        preparedStatement2.executeUpdate();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    } finally {
                        JDBC.returnConnection(connection1);
                    }
                    this.setVisible(false);
                } else {
                    if (resultSet.getString(6).equals(LIMIT_LOGIN_STRING)) {
                        JOptionPane.showMessageDialog(null, "您已被限制登录", "系统警告", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    new AdminFrame(usernameField.getText(), resultSet.getString(6));
                    Connection connection1 = JDBC.getConnection();
                    try {
                        String SQL2 = "update admin set LASTLOGINTIME = ? where adminname = ?";
                        PreparedStatement preparedStatement2 = connection1.prepareStatement(SQL2);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = dateFormat.format(new Date());
                        preparedStatement2.setObject(1, date);
                        preparedStatement2.setObject(2, usernameField.getText());
                        preparedStatement2.executeUpdate();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    } finally {
                        JDBC.returnConnection(connection1);
                    }
                    this.setVisible(false);
                }
            } else {
                JOptionPane.showMessageDialog(null, "用户名或密码错误", "登录失败", JOptionPane.ERROR_MESSAGE);
                usernameField.setText("请输入用户名");
                passwordField.setText("请输入密码");
                passwordField.setEchoChar('\0');
                usernameField.setForeground(Color.red);
                passwordField.setForeground(Color.red);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }
    }

    //主方法
    public static void main(String[] args) {
        WebLookAndFeel.install();
        LoginView loginView = new LoginView();
        loginView.requestFocus();
    }
}
