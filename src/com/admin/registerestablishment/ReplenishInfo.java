package com.admin.registerestablishment;

import com.combobox.Zdhy;
import com.jdbc.JDBC;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReplenishInfo extends JPanel {
    //JDBC
    Connection connection = null;

    private JLabel zdhyflLaBel = new JLabel("重点行业分类");
    public JComboBox<Zdhy> zdhyflComboBox = new JComboBox<Zdhy>();

    private JLabel mtzpLabel = new JLabel("门头招牌");
    public JTextField mtzpField = new JTextField();

    private JLabel szjzmcLabel = new JLabel("所在建筑名称");
    public JTextField szjzmcField = new JTextField();

    private JLabel jznwzLabel = new JLabel("建筑内位置");
    public JTextField jznwzField = new JTextField();

    private JLabel szjdmcLabel = new JLabel("所在街道名称");
    public JTextField szjdmcField = new JTextField();

    private JLabel mphLabel = new JLabel("门牌号");
    public JTextField mphField = new JTextField();

    public ReplenishInfo() {
        this.setLayout(null);

        this.zdhyflLaBel.setBounds(180, 100, 100, 30);
        this.zdhyflComboBox.setBounds(280, 100, 200, 30);
        this.add(zdhyflLaBel);
        this.add(zdhyflComboBox);

        this.mtzpLabel.setBounds(580, 100, 100, 30);
        this.mtzpField.setBounds(680, 100, 200, 30);
        this.add(mtzpLabel);
        this.add(mtzpField);

        this.szjzmcLabel.setBounds(180, 250, 100, 30);
        this.szjzmcField.setBounds(280, 250, 200, 30);
        this.add(szjzmcLabel);
        this.add(szjzmcField);


        this.jznwzLabel.setBounds(580, 250, 100, 30);
        this.jznwzField.setBounds(680, 250, 200, 30);
        this.add(jznwzLabel);
        this.add(jznwzField);

        this.szjdmcLabel.setBounds(180, 450, 100, 30);
        this.szjdmcField.setBounds(280, 450, 200, 30);
        this.add(szjdmcLabel);
        this.add(szjdmcField);

        this.mphLabel.setBounds(580, 450, 100, 30);
        this.mphField.setBounds(680, 450, 200, 30);
        this.add(mphLabel);
        this.add(mphField);

        //重点行业ComboBox初始化
        try {
            this.connection = JDBC.getConnection();
            String SQL = "select * from zdhyfl";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Zdhy zdhy = new Zdhy();
                zdhy.setZdhyid(resultSet.getString(1));
                zdhy.setZdhyname(resultSet.getString(2));
                this.zdhyflComboBox.addItem(zdhy);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }
    }
}
