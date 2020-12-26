package com.admin.registerindirect;

import com.jdbc.JDBC;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Vector;

public class LicensePrint extends JPanel {
    private Connection connection = null;

    private Font minorFont = new Font("宋体", Font.BOLD, 15);

    private JButton selectButton = new JButton("查询");
    private JTextField queryField = new JTextField();
    private JLabel remindLabel = new JLabel("请输入企业名称");
    private JComboBox<String> jComboBox = new JComboBox<String>();

    private ImageIcon note = new ImageIcon(this.getClass().getResource("/com/image/license.png"));

    private JLabel jLabel = new JLabel(note);
    private JLabel scztmcLabel = new JLabel();
    private JLabel lxLabel = new JLabel();
    private JLabel tzrLabel = new JLabel();
    private JLabel rqLabel = new JLabel();
    private JLabel zsLabel = new JLabel();
    private JLabel djjgLabel = new JLabel();


    private JLabel yearLabel = new JLabel();
    private JLabel monthLabel = new JLabel();
    private JLabel dayLabel = new JLabel();

    private JButton printButton = new JButton("打印");

    public LicensePrint() {
        this.setLayout(null);

        this.queryField.setBounds(133, 670, 200, 30);
        this.selectButton.setBounds(380, 670, 100, 30);
        this.remindLabel.setBounds(133, 700, 200, 20);
        this.jComboBox.setBounds(580, 670, 200, 30);
        this.printButton.setBounds(827, 670, 100, 30);
        this.add(queryField);
        this.add(selectButton);
        this.add(remindLabel);
        this.add(jComboBox);
        this.add(printButton);

        this.jLabel.setBounds(77, 0, 907, 650);
        this.add(jLabel);

        this.scztmcLabel.setBounds(200, 270, 200, 20);
        this.jLabel.add(scztmcLabel);
        this.scztmcLabel.setFont(minorFont);

        this.lxLabel.setBounds(200, 305, 200, 20);
        this.jLabel.add(lxLabel);
        this.lxLabel.setFont(minorFont);

        this.tzrLabel.setBounds(200, 337, 200, 20);
        this.jLabel.add(tzrLabel);
        this.tzrLabel.setFont(minorFont);

        this.zsLabel.setBounds(605, 337, 200, 20);
        this.jLabel.add(zsLabel);
        this.zsLabel.setFont(minorFont);

        this.rqLabel.setBounds(605, 305, 200, 20);
        this.jLabel.add(rqLabel);
        this.rqLabel.setFont(minorFont);

        this.djjgLabel.setBounds(680, 485, 200, 20);
        this.jLabel.add(djjgLabel);
        this.djjgLabel.setFont(minorFont);

        Date date = new Date();
        this.yearLabel.setBounds(610, 532, 50, 20);
        this.jLabel.add(yearLabel);
        this.yearLabel.setFont(minorFont);
        this.yearLabel.setText(String.valueOf(date.getYear() + 1900));

        this.monthLabel.setBounds(700, 532, 50, 20);
        this.jLabel.add(monthLabel);
        this.monthLabel.setFont(minorFont);
        this.monthLabel.setText(String.valueOf(date.getMonth() + 1));

        this.dayLabel.setBounds(770, 532, 50, 20);
        this.jLabel.add(dayLabel);
        this.dayLabel.setFont(minorFont);
        this.dayLabel.setText(String.valueOf(date.getDate()));

        //jComboBox监听
        this.jComboBox.addActionListener((e) ->
        {
            //获取信息
            this.infoSelect();
        });
        //查询按钮
        this.selectButton.addActionListener((e) ->
        {
            //获取名称
            this.select();
        });

        this.printButton.addActionListener((e) ->
        {
            //打印图片
            try {
                this.printImg();
            } catch (AWTException ex) {
                ex.printStackTrace();
            }
        });
    }

    //查询函数
    private void select() {
        Vector<String> SCZTMC = new Vector<String>();
        //清空所有选项
        this.jComboBox.removeAllItems();
        //获取名称
        try {
            connection = JDBC.getConnection();
            String SQL = "select SCZTMC from market_subject_information where SCZTMC like ? and JGZTDM != ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, "%" + queryField.getText() + "%");
            preparedStatement.setObject(2, "9");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                SCZTMC.add(resultSet.getString(1));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }

        for (String string : SCZTMC) {
            System.out.println(string);
            jComboBox.addItem(string);
        }
        if (jComboBox.getItemCount() == 0) {
            this.lxLabel.setText("");
            this.tzrLabel.setText("");
            this.djjgLabel.setText("");
            this.zsLabel.setText("");
            this.scztmcLabel.setText("");
            this.rqLabel.setText("");
        }
    }

    //信息函数
    private void infoSelect() {
        //投资人数据
        try {
            connection = JDBC.getConnection();
            String SQL = "select tzr from tzrqy where tzqymc = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, jComboBox.getSelectedItem());
            ResultSet resultSet = preparedStatement.executeQuery();
            String str = "";
            while (resultSet.next()) {
                str = str + resultSet.getString(1) + " ";
            }
            tzrLabel.setText(str);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }
        //基本信息
        try {
            connection = JDBC.getConnection();
            String SQL = "select scztmc,(SELECT lxdm.LXDMNAME FROM market_subject_information , lxdm WHERE market_subject_information.LXDM = lxdm.LXDMID AND market_subject_information.SCZTMC = ?), jyqxq, zs,(SELECT djjgdm.DJJGDMNAME FROM market_subject_information , djjgdm WHERE market_subject_information.DJJGDM = DJJGDM.DJJGDMID AND market_subject_information.SCZTMC = ?) from market_subject_information where scztmc = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, jComboBox.getSelectedItem());
            preparedStatement.setObject(2, jComboBox.getSelectedItem());
            preparedStatement.setObject(3, jComboBox.getSelectedItem());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                scztmcLabel.setText(resultSet.getString(1));
                lxLabel.setText(resultSet.getString(2));
                java.sql.Date date1 = resultSet.getDate(3);
                DateFormat dateFormat = DateFormat.getDateInstance();
                if (date1 == null) {
                    rqLabel.setText("");
                } else {
                    rqLabel.setText(dateFormat.format(date1));
                }
                zsLabel.setText(resultSet.getString(4));
                djjgLabel.setText(resultSet.getString(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }
    }

    //打印函数
    private void printImg() throws AWTException {
        Robot robot = new Robot();
        BufferedImage bim = robot.createScreenCapture(new Rectangle(jLabel.getX() + 343, jLabel.getY() + 72, jLabel.getWidth(), jLabel.getHeight()));
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.showDialog(new JLabel(), "选择");
        File file = jfc.getSelectedFile();
        try {
            ImageIO.write(bim, "jpg", new File(file.getAbsolutePath() + "\\" + jComboBox.getSelectedItem() + ".jpg"));
            JOptionPane.showMessageDialog(null, "保存成功", "保存成功", JOptionPane.OK_OPTION);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
