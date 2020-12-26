package com.admin.preapproved;

import com.jdbc.JDBC;
import com.util.Chooser;
import com.combobox.Djjg;
import com.combobox.Hymc;
import com.combobox.Xzqhlx;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NameInfo extends JPanel {
    //JDBC
    Connection connection = null;

    private JLabel gmlxLabel = new JLabel("冠名类型");
    public JTextField gmlxFidld = new JTextField();

    private JLabel xzqhlxLabel = new JLabel("行政区划类型");
    public JComboBox<Xzqhlx> xzqhlxComboBox = new JComboBox<Xzqhlx>();

    private JLabel mczhLable = new JLabel("名称字号");
    public JTextField mczhField = new JTextField();

    private JLabel bxzh1Label = new JLabel("备选字号1");
    public JTextField bxzh1Field = new JTextField();

    private JLabel bxzh2Label = new JLabel("备选字号2");
    public JTextField bxzh2Feild = new JTextField();

    private JLabel nsmcLable = new JLabel("拟设名称");
    public JTextField nsmcField = new JTextField();

    private JLabel zhpyLable = new JLabel("字号拼音");
    public JTextField zhpyField = new JTextField();

    private JLabel hymcLable = new JLabel("行业名称");
    public JComboBox<Hymc> hymcJComboBox = new JComboBox<Hymc>();

    private JLabel djjgLabel = new JLabel("登记机关");
    public JComboBox<Djjg> djjgComboBox = new JComboBox<Djjg>();
    private JLabel JYQXQLable = new JLabel("经营期限起");
    public JTextField JYQXQTextField = new JTextField();
    private JLabel JYQXZLable = new JLabel("经营期限止");
    public JTextField JYQXZTextField = new JTextField();

    public NameInfo() {
        this.setLayout(null);

        this.gmlxLabel.setBounds(180, 50, 100, 30);
        this.gmlxFidld.setBounds(280, 50, 200, 30);
        this.add(gmlxLabel);
        this.add(gmlxFidld);

        this.xzqhlxLabel.setBounds(550, 50, 100, 30);
        this.xzqhlxComboBox.setBounds(650, 50, 200, 30);
        this.add(xzqhlxLabel);
        this.add(xzqhlxComboBox);

        this.mczhLable.setBounds(180, 110, 100, 30);
        this.mczhField.setBounds(280, 110, 200, 30);
        this.add(mczhLable);
        this.add(mczhField);

        this.bxzh1Label.setBounds(180, 150, 100, 30);
        this.bxzh1Field.setBounds(280, 150, 200, 30);
        this.add(bxzh1Label);
        this.add(bxzh1Field);

        this.bxzh2Label.setBounds(180, 190, 100, 30);
        this.bxzh2Feild.setBounds(280, 190, 200, 30);
        this.add(bxzh2Label);
        this.add(bxzh2Feild);

        this.zhpyLable.setBounds(550, 120, 100, 30);
        this.zhpyField.setBounds(650, 120, 200, 30);
        this.add(zhpyLable);
        this.add(zhpyField);

        this.nsmcLable.setBounds(180, 250, 100, 30);
        this.nsmcField.setBounds(280, 250, 200, 30);
        this.add(nsmcField);
        this.add(nsmcLable);

        this.hymcLable.setBounds(550, 190, 100, 30);
        this.hymcJComboBox.setBounds(650, 190, 200, 30);
        this.add(hymcLable);
        this.add(hymcJComboBox);

        this.djjgLabel.setBounds(550, 250, 100, 30);
        this.djjgComboBox.setBounds(650, 250, 200, 30);
        this.add(djjgLabel);
        this.add(djjgComboBox);
        Chooser chooser = Chooser.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.JYQXQTextField.setText(dateFormat.format(new Date()));
        chooser.register(JYQXQTextField);
        this.JYQXQLable.setBounds(180, 300, 100, 30);
        this.JYQXQTextField.setBounds(280, 300, 200, 30);
        this.add(JYQXQLable);
        this.add(JYQXQTextField);

        Chooser chooser2 = Chooser.getInstance();
        this.JYQXZTextField.setText(dateFormat.format(new Date()));
        chooser2.register(JYQXZTextField);
        this.JYQXZLable.setBounds(550, 300, 100, 30);
        this.JYQXZTextField.setBounds(650, 300, 200, 30);
        this.add(JYQXZLable);
        this.add(JYQXZTextField);

        //行政区划类型ComboBox
        try {
            connection = JDBC.getConnection();
            String SQL1 = "select * from xzqhlx";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL1);
            while (resultSet.next()) {
                Xzqhlx xzqhlx = new Xzqhlx();
                xzqhlx.setXzqhlxid(resultSet.getString(1));
                xzqhlx.setXzqhlxname(resultSet.getString(2));
                xzqhlxComboBox.addItem(xzqhlx);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }

        //行业类型ComboBox
        try {
            connection = JDBC.getConnection();
            String SQL = "select * from hydm";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Hymc hymc = new Hymc();
                hymc.setHymcid(resultSet.getString(1));
                hymc.setHymcname(resultSet.getString(2));
                hymcJComboBox.addItem(hymc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }

        //登记机关
        try {
            connection = JDBC.getConnection();
            String SQL = "select * from djjgdm";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Djjg djjg = new Djjg();
                djjg.setDjjgid(resultSet.getString(1));
                djjg.setDjjgname(resultSet.getString(2));
                djjgComboBox.addItem(djjg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }
    }
}
