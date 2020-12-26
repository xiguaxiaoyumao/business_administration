package com.admin.nameindirect;

import com.jdbc.JDBC;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Vector;

public class NotePrint extends JPanel {
    private Connection connection = null;
    private Font mainFont = new Font("宋体", Font.BOLD, 20);
    private Font minorFont = new Font("宋体", Font.BOLD, 15);

    public JButton selectButton = new JButton("查询");
    public JTextField queryField = new JTextField();
    private JLabel remindLabel = new JLabel("请输入企业名称或者注册号");

    public JComboBox<String> jComboBox = new JComboBox<String>();

    private ImageIcon note = new ImageIcon(this.getClass().getResource("/com/image/note.png"));
    private JLabel jLabel = new JLabel(note);
    private JLabel wshLabel = new JLabel();
    private JLabel tzrLabel = new JLabel();
    private JLabel zczbLabel = new JLabel();
    private JLabel bzLabel = new JLabel();
    private JLabel zsLabel = new JLabel();
    private JLabel scztmcLabel = new JLabel();
    private JLabel rqLabel = new JLabel();
    private JLabel yearLabel = new JLabel();
    private JLabel monthLabel = new JLabel();
    private JLabel dayLabel = new JLabel();

    private JLabel titleLabel = new JLabel("文书打印");
    private JButton printButton = new JButton("打印");

    public NotePrint() {
        this.setLayout(null);

        this.queryField.setBounds(155, 680, 200, 30);
        this.selectButton.setBounds(405, 680, 100, 30);
        this.remindLabel.setBounds(155, 710, 200, 20);

        this.add(queryField);
        this.add(selectButton);
        this.add(remindLabel);

        this.setSize(1060, 800);

        this.jComboBox.setBounds(555, 680, 200, 30);
        this.add(jComboBox);

        this.titleLabel.setBounds(0, 0, 130, 30);
        this.titleLabel.setFont(mainFont);
        this.add(titleLabel);

        this.printButton.setBounds(805, 680, 100, 30);
        this.add(printButton);

        jLabel.setBounds(85, 0, 907, 650);
        this.add(jLabel);

        this.wshLabel.setBounds(426, 49, 10, 10);
        this.jLabel.add(wshLabel);
        this.wshLabel.setFont(minorFont);

        this.tzrLabel.setBounds(240, 222, 500, 20);
        this.jLabel.add(tzrLabel);
        this.tzrLabel.setFont(minorFont);

        this.zczbLabel.setBounds(240, 305, 150, 20);
        this.jLabel.add(zczbLabel);
        this.zczbLabel.setFont(minorFont);

        this.bzLabel.setBounds(440, 305, 50, 20);
        this.jLabel.add(bzLabel);

        this.zsLabel.setBounds(240, 358, 200, 20);
        this.jLabel.add(zsLabel);
        this.zsLabel.setFont(minorFont);

        this.scztmcLabel.setBounds(240, 415, 200, 20);
        this.jLabel.add(scztmcLabel);
        this.scztmcLabel.setFont(minorFont);

        this.rqLabel.setBounds(355, 474, 115, 20);
        this.jLabel.add(rqLabel);
        this.rqLabel.setFont(minorFont);

        Date date = new Date();
        this.yearLabel.setBounds(610, 575, 50, 20);
        this.jLabel.add(yearLabel);
        this.yearLabel.setText(String.valueOf(date.getYear() + 1900));

        this.monthLabel.setBounds(700, 575, 50, 20);
        this.jLabel.add(monthLabel);
        this.monthLabel.setText(String.valueOf(date.getMonth() + 1));

        this.dayLabel.setBounds(770, 575, 50, 20);
        this.jLabel.add(dayLabel);
        this.dayLabel.setText(String.valueOf(date.getDate()));


        //jComboBox获取项目；
        select();

        //投资人数据
        try {
            connection = JDBC.getConnection();
            String SQL = "select tzr from tzrqy where tzqymc = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, jComboBox.getSelectedItem());
            ResultSet resultSet = preparedStatement.executeQuery();
            String str = "";
            while (resultSet.next()) {
                str = str + " " + resultSet.getString(1);
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
            String SQL = "select zczb,(select tzbz.tzbzname from tzbz where tzbz.tzbzid=tzbz),zs,scztmc,jyqxz ,wybs from market_subject_information where scztmc = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, jComboBox.getSelectedItem());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                zczbLabel.setText(resultSet.getString(1));
                bzLabel.setText(resultSet.getString(2));
                zsLabel.setText(resultSet.getString(3));
                scztmcLabel.setText(resultSet.getString(4));
                java.sql.Date date1 = resultSet.getDate(5);
                DateFormat dateFormat = DateFormat.getDateInstance();
                if (date1 == null) {
                    rqLabel.setText("");
                } else {
                    rqLabel.setText(dateFormat.format(date1));
                }
                wshLabel.setText(resultSet.getString(6));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }

        this.jComboBox.addActionListener((o) ->
        {
            //投资人数据
            try {
                connection = JDBC.getConnection();
                String SQL = "select tzr from tzrqy where tzqymc = ? ";
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setObject(1, jComboBox.getSelectedItem());
                ResultSet resultSet = preparedStatement.executeQuery();
                String str = "";
                while (resultSet.next()) {
                    str = str + " " + resultSet.getString(1);
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
                String SQL = "select zczb,(select tzbz.tzbzname from tzbz where tzbz.tzbzid=tzbz),zs,scztmc, jyqxz,wybs from market_subject_information where scztmc = ? ";
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setObject(1, jComboBox.getSelectedItem());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    zczbLabel.setText(resultSet.getString(1));
                    bzLabel.setText(resultSet.getString(2));
                    zsLabel.setText(resultSet.getString(3));
                    scztmcLabel.setText(resultSet.getString(4));
                    java.sql.Date date1 = resultSet.getDate(5);
                    DateFormat dateFormat = DateFormat.getDateInstance();
                    if (date1 == null) {
                        rqLabel.setText("");
                    } else {
                        rqLabel.setText(dateFormat.format(date1));
                    }
                    wshLabel.setText(resultSet.getString(6));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                JDBC.returnConnection(connection);
            }
        });
        this.selectButton.addActionListener((e) ->
        {
            select();
        });
    }

    private void select() {
        Vector<String> SCZTMC = new Vector<String>();
        this.jComboBox.removeAllItems();
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
            this.wshLabel.setText("");
            this.tzrLabel.setText("");
            this.zczbLabel.setText("");
            this.bzLabel.setText("");
            this.zsLabel.setText("");
            this.scztmcLabel.setText("");
            this.rqLabel.setText("");
        }
    }
}
