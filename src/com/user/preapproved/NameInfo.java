package com.user.preapproved;

import com.jdbc.JDBC;
import com.combobox.Hymc;
import com.combobox.Xzqhlx;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.sql.*;

public class NameInfo extends JPanel {
    private Connection connection = null;

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
    public JLabel nsmcRemindLabel = new JLabel("");

    private JLabel zhpyLable = new JLabel("字号拼音");
    public JTextField zhpyField = new JTextField();

    private JLabel hymcLable = new JLabel("行业名称");
    public JComboBox<Hymc> hymcJComboBox = new JComboBox<Hymc>();

    public NameInfo() {
        this.setLayout(null);

        this.gmlxLabel.setBounds(380, 50, 100, 30);
        this.gmlxFidld.setBounds(480, 50, 200, 30);
        this.add(gmlxLabel);
        this.add(gmlxFidld);

        this.xzqhlxLabel.setBounds(380, 130, 100, 30);
        this.xzqhlxComboBox.setBounds(480, 130, 200, 30);
        this.add(xzqhlxLabel);
        this.add(xzqhlxComboBox);

        this.mczhLable.setBounds(380, 210, 100, 30);
        this.mczhField.setBounds(480, 210, 200, 30);
        this.add(mczhLable);
        this.add(mczhField);

        this.bxzh1Label.setBounds(380, 250, 100, 30);
        this.bxzh1Field.setBounds(480, 250, 200, 30);
        this.add(bxzh1Label);
        this.add(bxzh1Field);

        this.bxzh2Label.setBounds(380, 290, 100, 30);
        this.bxzh2Feild.setBounds(480, 290, 200, 30);
        this.add(bxzh2Label);
        this.add(bxzh2Feild);

        this.zhpyLable.setBounds(380, 370, 100, 30);
        this.zhpyField.setBounds(480, 370, 200, 30);
        this.add(zhpyLable);
        this.add(zhpyField);

        this.nsmcLable.setBounds(380, 450, 100, 30);
        this.nsmcField.setBounds(480, 450, 200, 30);
        this.nsmcRemindLabel.setBounds(480, 480, 200, 20);
        this.add(nsmcField);
        this.add(nsmcLable);
        this.add(nsmcRemindLabel);

        this.hymcLable.setBounds(380, 530, 100, 30);
        this.hymcJComboBox.setBounds(480, 530, 200, 30);
        this.add(hymcLable);
        this.add(hymcJComboBox);

        //行政区划类型ComboBox
        try {
            this.connection = JDBC.getConnection();
            String SQL = "select * from xzqhlx";
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Xzqhlx xzqhlx = new Xzqhlx();
                xzqhlx.setXzqhlxid(resultSet.getString(1));
                xzqhlx.setXzqhlxname(resultSet.getString(2));
                this.xzqhlxComboBox.addItem(xzqhlx);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(this.connection);
        }

        //行业类型ComboBox
        try {
            this.connection = JDBC.getConnection();
            String SQL = "select * from hydm";
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Hymc hymc = new Hymc();
                hymc.setHymcid(resultSet.getString(1));
                hymc.setHymcname(resultSet.getString(2));
                this.hymcJComboBox.addItem(hymc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(this.connection);
        }

        //名称文本框的监听事件
        this.nsmcField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                if (MCRemind()) {
                    nsmcRemindLabel.setText("");
                } else {
                    nsmcRemindLabel.setText("名称已存在或已被申请");
                }
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                if (MCRemind()) {
                    nsmcRemindLabel.setText("");
                } else {
                    nsmcRemindLabel.setText("名称已存在或已被申请");
                }
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {

            }
        });
    }

    public boolean MCRemind() {
        try {
            this.connection = JDBC.getConnection();
            String SQL = "select scztmc from market_subject_information where lcztdm != '2' and scztmc = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setObject(1, this.nsmcField.getText());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(this.connection);
        }
        return true;
    }
}
