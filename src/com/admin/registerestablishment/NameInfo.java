package com.admin.registerestablishment;

import com.combobox.Djjg;
import com.combobox.Xzqhlx;
import com.jdbc.JDBC;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class NameInfo extends JPanel {
    //JDBC
    Connection connection = null;

    private JLabel gmlxLabel = new JLabel("冠名类型");
    public JTextField gmlxFidld = new JTextField();

    private JLabel xzqhlxLabel = new JLabel("行政区划类型");
    public JComboBox<Xzqhlx> xzqhlxComboBox = new JComboBox<Xzqhlx>();

    private JLabel mczhLable = new JLabel("名称字号");
    public JTextField mczhField = new JTextField();

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

        this.xzqhlxLabel.setBounds(580, 50, 100, 30);
        this.xzqhlxComboBox.setBounds(680, 50, 200, 30);
        this.add(xzqhlxLabel);
        this.add(xzqhlxComboBox);

        this.mczhLable.setBounds(180, 175, 100, 30);
        this.mczhField.setBounds(280, 175, 200, 30);
        this.add(mczhLable);
        this.add(mczhField);


        this.djjgLabel.setBounds(580, 175, 100, 30);
        this.djjgComboBox.setBounds(680, 175, 200, 30);
        this.add(djjgLabel);
        this.add(djjgComboBox);

        this.JYQXQLable.setBounds(180, 300, 100, 30);
        this.JYQXQTextField.setBounds(280, 300, 200, 30);
        this.add(JYQXQLable);
        this.add(JYQXQTextField);

        this.JYQXZLable.setBounds(580, 300, 100, 30);
        this.JYQXZTextField.setBounds(680, 300, 200, 30);
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
