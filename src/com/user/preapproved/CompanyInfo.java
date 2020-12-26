package com.user.preapproved;

import com.jdbc.JDBC;
import com.combobox.Bz;
import com.combobox.Qylx;
import com.combobox.Qyzl;
import com.combobox.Zzxs;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CompanyInfo extends JPanel {
    private Connection connection = null;

    private JLabel qylxLabel = new JLabel("企业类型");
    public JComboBox<Qylx> qylxComboBox = new JComboBox<Qylx>();
    public JComboBox<Qyzl> qyzlComboBox = new JComboBox<Qyzl>();

    private JLabel zsLabel = new JLabel("住所所在地址");
    public JTextField zsszdzField = new JTextField();

    private JLabel zzxsLabel = new JLabel("组织形式");
    public JComboBox<Zzxs> zzxsComboBox = new JComboBox<Zzxs>();

    private JLabel lxdhLabel = new JLabel("联系电话");
    public JTextField lxdhfield = new JTextField();

    private JLabel yzbmLabel = new JLabel("邮政编码");
    public JTextField yzbmfidld = new JTextField();

    private JLabel zczbLabel = new JLabel("注册资本");
    public JTextField zczbfield = new JTextField();

    private JLabel zczbbzLabel = new JLabel("注册资本币种");
    public JComboBox<Bz> zczbbzComboBox = new JComboBox<Bz>();

    private JLabel jyfwLabel = new JLabel("经营范围");
    public JTextPane jyfwFeild = new JTextPane();

    public CompanyInfo() {
        this.setLayout(null);
        this.qylxLabel.setBounds(380, 50, 100, 30);
        this.qylxComboBox.setBounds(480, 50, 100, 30);
        this.qyzlComboBox.setBounds(580, 50, 100, 30);

        this.zsLabel.setBounds(380, 110, 100, 30);
        this.zsszdzField.setBounds(480, 110, 200, 30);

        this.zzxsLabel.setBounds(380, 170, 100, 30);
        this.zzxsComboBox.setBounds(480, 170, 200, 30);

        this.lxdhLabel.setBounds(380, 230, 100, 30);
        this.lxdhfield.setBounds(480, 230, 200, 30);

        this.yzbmLabel.setBounds(380, 290, 100, 30);
        this.yzbmfidld.setBounds(480, 290, 200, 30);

        this.zczbLabel.setBounds(380, 350, 100, 30);
        this.zczbfield.setBounds(480, 350, 200, 30);

        this.zczbbzLabel.setBounds(380, 410, 100, 30);
        this.zczbbzComboBox.setBounds(480, 410, 200, 30);

        this.jyfwLabel.setBounds(380, 500, 100, 30);
        this.jyfwFeild.setBounds(480, 495, 200, 90);

        this.add(qylxLabel);
        this.add(qylxComboBox);
        this.add(qyzlComboBox);

        this.add(zsLabel);
        this.add(zsszdzField);

        this.add(zzxsLabel);
        this.add(zzxsComboBox);

        this.add(lxdhLabel);
        this.add(lxdhfield);

        this.add(yzbmLabel);
        this.add(yzbmfidld);

        this.add(zczbLabel);
        this.add(zczbfield);

        this.add(zczbbzLabel);
        this.add(zczbbzComboBox);

        this.add(jyfwLabel);
        this.add(jyfwFeild);

        //企业类型ComboBox
        try {
            this.connection = JDBC.getConnection();
            String SQL = "select * from lxdm";
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Qylx qylx = new Qylx();
                qylx.setQylxid(resultSet.getString(1));
                qylx.setQylxname(resultSet.getString(2));
                this.qylxComboBox.addItem(qylx);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }

        //企业种类ComboBox
        try {
            this.connection = JDBC.getConnection();
            String SQL = "select * from zldm";
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Qyzl qyzl = new Qyzl();
                qyzl.setQyzlid(resultSet.getString(1));
                qyzl.setQyzlname(resultSet.getString(2));
                this.qyzlComboBox.addItem(qyzl);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(this.connection);
        }

        //组织形式ComboBox
        try {
            this.connection = JDBC.getConnection();
            String SQL = "select * from zzxs";
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Zzxs zzxs = new Zzxs();
                zzxs.setZzxsid(resultSet.getString(1));
                zzxs.setZzxsname(resultSet.getString(2));
                this.zzxsComboBox.addItem(zzxs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(this.connection);
        }

        //注册币种ComboBox
        try {
            this.connection = JDBC.getConnection();
            String SQL = "select * from tzbz";
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Bz bz = new Bz();
                bz.setBzid(resultSet.getString(1));
                bz.setBzname(resultSet.getString(2));
                this.zczbbzComboBox.addItem(bz);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(this.connection);
        }
    }
}
