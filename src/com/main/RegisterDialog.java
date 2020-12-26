package com.main;

import com.jdbc.JDBC;
import com.util.IdentifyCode;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterDialog extends JDialog {
    private final String NO_LIMIT_LOGIN_STRING = "1";

    private ImageIcon titleIcon = new ImageIcon(getClass().getResource("/com/image/business.jpg"));

    private ImageIcon imgYes = new ImageIcon(this.getClass().getResource("/com/image/yes.png"));

    private Font font = new Font("微软雅黑", Font.BOLD, 15);
    private Font remindFont = new Font("宋体", Font.PLAIN, 10);
    private Connection connection = null;
    private JLabel loginInfo = new JLabel("设置登录信息");
    private JLabel usernameLabel = new JLabel("用户名");
    private JTextField usernameField = new JTextField();
    private JLabel usernameRemind = new JLabel();

    private JLabel passwordLabel = new JLabel("密码");
    private JPasswordField passwordField = new JPasswordField();
    private JLabel passwordRemind = new JLabel();
    private JLabel passwordlengthLabel = new JLabel("输入6-12位密码");
    private JLabel confirmPasswordLabel = new JLabel("确认密码");
    private JPasswordField confirmPasswordField = new JPasswordField();
    private JLabel confirmPasswordRemind = new JLabel();

    private JLabel realInfo = new JLabel("设置账户信息");
    private JLabel nameLabel = new JLabel("真实姓名");
    private JTextField nameField = new JTextField();
    private JLabel nameRemind = new JLabel();

    private JLabel zjhmLabel = new JLabel("身份证号");
    private JTextField zjhmField = new JTextField();
    private JLabel zjhmRemindLabel = new JLabel();
    private JLabel zjhmHavaLabel = new JLabel();

    private JLabel phonenumberLabel = new JLabel("电话");
    private JTextField phonenumberField = new JTextField();
    private JLabel phonenumbeRemind = new JLabel();

    private JLabel identifycodeLabel = new JLabel("验证码");
    private JTextField identifycodeField = new JTextField();
    private JLabel identifycode = new JLabel();
    StringBuffer stringBuffer;

    private JButton registerButton = new JButton("注册");


    //构造函数
    public RegisterDialog() {
        this.setTitle("注册");
        this.setSize(300, 500);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setModal(true);
        this.setResizable(false);
        this.setIconImage(this.titleIcon.getImage());
        this.loginInfo.setFont(this.font);
        this.stringBuffer = new IdentifyCode().getIdentifyCode();
        this.identifycode.setText(this.stringBuffer.toString());
        //用户登录信息
        this.loginInfo.setBounds(50, 28, 100, 20);
        this.usernameLabel.setBounds(40, 50, 90, 30);
        this.usernameField.setBounds(110, 50, 150, 30);
        this.usernameRemind.setBounds(110, 80, 150, 10);
        this.usernameRemind.setFont(this.remindFont);

        this.passwordLabel.setBounds(40, 100, 90, 30);
        this.passwordField.setBounds(110, 100, 150, 30);
        this.passwordRemind.setBounds(260, 100, 30, 30);

        this.passwordlengthLabel.setBounds(110, 130, 150, 10);
        this.passwordlengthLabel.setFont(this.remindFont);
        this.confirmPasswordLabel.setBounds(40, 150, 90, 30);
        this.confirmPasswordField.setBounds(110, 150, 150, 30);
        this.confirmPasswordRemind.setBounds(260, 150, 30, 30);
        //用户真实信息
        this.realInfo.setFont(this.font);
        this.realInfo.setBounds(50, 178, 100, 20);
        this.nameLabel.setBounds(40, 200, 90, 30);
        this.nameField.setBounds(110, 200, 150, 30);
        this.nameRemind.setBounds(260, 200, 30, 30);

        this.zjhmLabel.setBounds(40, 250, 90, 30);
        this.zjhmField.setBounds(110, 250, 150, 30);
        this.zjhmRemindLabel.setBounds(260, 250, 40, 30);
        this.zjhmHavaLabel.setBounds(110, 281, 150, 10);
        this.zjhmHavaLabel.setFont(this.remindFont);

        this.phonenumberLabel.setBounds(40, 300, 90, 30);
        this.phonenumberField.setBounds(110, 300, 150, 30);
        this.phonenumbeRemind.setBounds(260, 300, 40, 30);

        this.identifycodeLabel.setBounds(40, 350, 60, 30);
        this.identifycodeField.setBounds(110, 350, 75, 30);
        this.identifycode.setBounds(190, 350, 75, 30);

        this.registerButton.setBounds(120, 400, 60, 30);

        this.add(this.loginInfo);
        this.add(this.usernameLabel);
        this.add(this.usernameField);
        this.add(this.usernameRemind);

        this.add(this.passwordLabel);
        this.add(this.passwordField);
        this.add(this.passwordRemind);

        this.add(this.passwordlengthLabel);
        this.add(this.confirmPasswordLabel);
        this.add(this.confirmPasswordField);
        this.add(this.confirmPasswordRemind);

        this.add(this.realInfo);
        this.add(this.nameLabel);
        this.add(this.nameField);
        this.add(this.nameRemind);

        this.add(this.zjhmLabel);
        this.add(this.zjhmField);
        this.add(this.zjhmRemindLabel);
        this.add(this.zjhmHavaLabel);

        this.add(this.phonenumberLabel);
        this.add(this.phonenumberField);
        this.add(this.phonenumbeRemind);

        this.add(this.identifycodeLabel);
        this.add(this.identifycodeField);
        this.add(this.identifycode);

        this.add(this.registerButton);
        //密码长度的判断
        this.passwordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                if (passwordField.getText().length() < 6 || passwordField.getText().length() > 12) {
                    passwordRemind.setText("×");
                } else {
                    passwordRemind.setText("√");
                }
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                if (passwordField.getText().length() < 6 || passwordField.getText().length() > 12) {
                    passwordRemind.setText("×");
                } else {
                    passwordRemind.setText("√");
                }
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {

            }
        });

        //确认秘密的判断
        this.confirmPasswordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                if (confirmPasswordField.getText().equals(passwordField.getText())) {
                    confirmPasswordRemind.setText("√");
                } else {
                    confirmPasswordRemind.setText("×");
                }
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                if (confirmPasswordField.getText().equals(passwordField.getText())) {
                    confirmPasswordRemind.setText("√");
                } else {
                    confirmPasswordRemind.setText("×");
                }
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {

            }

        });

        //电话号码的判断
        this.phonenumberField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                String regex = "^1[3456789]\\d{9}$";
                Pattern pattern = Pattern.compile(regex);
                String target = phonenumberField.getText();
                //用户填写的电话
                Matcher matcher = pattern.matcher(target);
                if (matcher.find()) {
                    phonenumbeRemind.setText("√");
                } else {
                    phonenumbeRemind.setText("×");
                }
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                String regex = "^1[3456789]\\d{9}$";
                Pattern pattern = Pattern.compile(regex);
                String target = phonenumberField.getText();
                //用户填写的电话
                Matcher matcher = pattern.matcher(target);
                if (matcher.find()) {
                    phonenumbeRemind.setText("√");
                } else {
                    phonenumbeRemind.setText("×");
                }
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {

            }
        });

        //证件号码判断
        this.zjhmField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                if (zjhmField.getText().length() != 18) {
                    zjhmRemindLabel.setText("×");
                } else {
                    zjhmRemindLabel.setText("√");
                }
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                if (zjhmField.getText().length() != 18) {
                    zjhmRemindLabel.setText("×");
                } else {
                    zjhmRemindLabel.setText("√");
                }
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {

            }
        });

        //姓名判断
        this.nameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                if (nameField.getText().length() == 0) {
                    nameRemind.setText("×");
                } else {
                    nameRemind.setText("√");
                }
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                if (nameField.getText().length() == 0) {
                    nameRemind.setText("×");
                } else {
                    nameRemind.setText("√");
                }
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {

            }
        });

        //注册的监听事件
        this.registerButton.addActionListener((e) ->
        {
            if (usernameField.getText().isEmpty()) {
                usernameRemind.setText("用户名不能为空");
                return;
            }
            if (passwordField.getText().isEmpty()) {
                passwordRemind.setText("×");
                return;
            }
            if (confirmPasswordField.getText().isEmpty()) {
                confirmPasswordRemind.setText("×");
                return;
            }
            if (phonenumberField.getText().isEmpty()) {
                phonenumbeRemind.setText("×");
                return;
            }
            if (nameField.getText().isEmpty()) {
                nameRemind.setText("×");
                return;
            }
            if (zjhmField.getText().isEmpty()) {
                zjhmRemindLabel.setText("×");
                return;
            }
            //判断用户名是否重复
            try {
                usernameRemind.setText("");
                connection = JDBC.getConnection();
                String SQL = "select username from user where username = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setObject(1, usernameField.getText());
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    usernameRemind.setText("用户名已存在");
                    usernameField.setText("");
                    return;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                JDBC.returnConnection(connection);
            }
            //判断身份是否重复
            try {
                zjhmHavaLabel.setText("");
                connection = JDBC.getConnection();
                String SQL = "select username from user where USERIDENTIFY = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setObject(1, zjhmField.getText());
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    zjhmHavaLabel.setText("该身份证号已注册");
                    zjhmField.setText("");
                    return;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                JDBC.returnConnection(connection);
            }

            if (!(identifycode.getText().equalsIgnoreCase(identifycodeField.getText()))) {
                StringBuffer stringBuffer2 = new IdentifyCode().getIdentifyCode();
                JOptionPane.showMessageDialog(null, "验证码错误", "验证码错误", JOptionPane.OK_OPTION);
                identifycode.setText(stringBuffer2.toString());
                return;
            }

            //将数据存到数据库
            try {
                connection = JDBC.getConnection();
                String SQL = "insert into user (USERNAME, USERPASSWORD,USERPHONENUMBER,NAME ,USERIDENTIFY,USERLIMIT) values(?,?,?,?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setObject(1, usernameField.getText());
                preparedStatement.setObject(2, passwordField.getText());
                preparedStatement.setObject(3, phonenumberField.getText());
                preparedStatement.setObject(4, nameField.getText());
                preparedStatement.setObject(5, zjhmField.getText());
                preparedStatement.setObject(6, NO_LIMIT_LOGIN_STRING);
                System.out.println(preparedStatement);
                int n = preparedStatement.executeUpdate();
                if (n == 1) {
                    JOptionPane.showMessageDialog(null, "注册成功", "注册成功", JOptionPane.OK_OPTION, imgYes);
                    this.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(null, "注册失败", "注册失败", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                JDBC.returnConnection(connection);
            }
        });
        this.setForeColor(Color.RED);
        setVisible(true);

    }

    private void setForeColor(Color color) {
        this.loginInfo.setForeground(Color.RED);
        this.usernameLabel.setForeground(color);
        this.usernameRemind.setForeground(color);
        this.passwordLabel.setForeground(color);
        this.passwordRemind.setForeground(color);
        this.passwordlengthLabel.setForeground(color);
        this.confirmPasswordLabel.setForeground(color);
        this.confirmPasswordRemind.setForeground(color);
        this.realInfo.setForeground(Color.RED);
        this.nameLabel.setForeground(color);
        this.nameRemind.setForeground(color);
        this.zjhmLabel.setForeground(color);
        this.zjhmRemindLabel.setForeground(color);
        this.zjhmHavaLabel.setForeground(color);
        this.phonenumberLabel.setForeground(color);
        this.phonenumbeRemind.setForeground(color);
        this.identifycode.setForeground(color);
        this.identifycodeLabel.setForeground(color);
    }
}
