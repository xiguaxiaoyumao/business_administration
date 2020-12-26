package com.admin.preapproved;

import com.jdbc.JDBC;
import com.combobox.Djjg;
import com.combobox.Gj;
import com.combobox.Tzrlx;
import com.combobox.Zjlx;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class InvestorInfo extends JPanel {
    private Connection connection = null;

    public JTable table = new JTable() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    public JScrollPane jScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    public Vector<String> headVector = new Vector<String>();
    public Vector<Vector<String>> dataVector = new Vector<Vector<String>>();

    public JButton modifyInvestorButton = new JButton("确认修改");
    ;

    private JLabel tzrnameLabel = new JLabel("名称或姓名");
    public JTextField tzrnameFidld = new JTextField();

    private JLabel tzrlxLabel = new JLabel("投资人类型");
    public JComboBox<Tzrlx> tzrlxComboBox = new JComboBox<Tzrlx>();

    private JLabel zjlxLabel = new JLabel("证件类型");
    public JComboBox<Zjlx> zjlxComboBox = new JComboBox<Zjlx>();

    private JLabel zjhmLabel = new JLabel("证件号码");
    public JTextField zjhmField = new JTextField();
    private JLabel zjhmRmindLabel = new JLabel();

    private JLabel czblLabel = new JLabel("出资比例");
    public JTextField czblField = new JTextField();

    private JLabel xbLabel = new JLabel("性别");
    public JRadioButton maleRadioButton = new JRadioButton("男");
    public JRadioButton femaleRadioButton = new JRadioButton("女");
    private ButtonGroup buttonGroup = new ButtonGroup();

    private JLabel lxdhLabel = new JLabel("联系电话");
    public JTextField lxdhField = new JTextField();
    private JLabel lxdhRemindLabel = new JLabel();
    private JLabel gjLabel = new JLabel("国籍");
    public JComboBox gjComboBox = new JComboBox();

    private JLabel zsLabel = new JLabel("住所");
    public JTextField zsField = new JTextField();

    private JLabel djjgLabel = new JLabel("登记机关");
    public JComboBox<Djjg> djjgComboBox = new JComboBox<Djjg>();

    public InvestorInfo() {
        this.setLayout(null);

        //表头
        this.headVector.add("名称或姓名");
        this.headVector.add("投资人类型");
        this.headVector.add("证件类型");
        this.headVector.add("证件号码");
        this.headVector.add("出资比例");
        this.headVector.add("性别");
        this.headVector.add("联系电话");
        this.headVector.add("国籍");
        this.headVector.add("住所");
        this.headVector.add("登记机关");

        DefaultTableModel defaultTableModel = new DefaultTableModel(dataVector, headVector);
        table.setModel(defaultTableModel);
        jScrollPane.getViewport().add(table);
        jScrollPane.setBounds(30, 50, 1000, 250);
        this.table.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        this.table.setDefaultRenderer(Object.class, defaultTableCellRenderer);
        this.add(jScrollPane);

        this.modifyInvestorButton.setBounds(500, 680, 100, 30);
        this.add(modifyInvestorButton);

        this.tzrnameLabel.setBounds(150, 390, 100, 30);
        this.tzrnameFidld.setBounds(250, 390, 200, 30);
        this.add(tzrnameLabel);
        this.add(tzrnameFidld);

        this.tzrlxLabel.setBounds(600, 390, 390, 30);
        this.tzrlxComboBox.setBounds(700, 390, 200, 30);
        this.add(tzrlxLabel);
        this.add(tzrlxComboBox);

        this.zjlxLabel.setBounds(150, 440, 100, 30);
        this.zjlxComboBox.setBounds(250, 440, 200, 30);
        this.add(zjlxLabel);
        this.add(zjlxComboBox);

        this.zjhmLabel.setBounds(600, 440, 100, 30);
        this.zjhmField.setBounds(700, 440, 200, 30);
        this.zjhmRmindLabel.setBounds(900, 440, 50, 30);
        this.add(zjhmLabel);
        this.add(zjhmField);
        this.add(zjhmRmindLabel);

        this.czblLabel.setBounds(150, 490, 100, 30);
        this.czblField.setBounds(250, 490, 200, 30);
        this.add(czblLabel);
        this.add(czblField);

        this.xbLabel.setBounds(600, 490, 100, 30);
        this.maleRadioButton.setBounds(700, 490, 100, 30);
        this.femaleRadioButton.setBounds(800, 490, 100, 30);
        this.add(xbLabel);
        this.buttonGroup.add(maleRadioButton);
        this.buttonGroup.add(femaleRadioButton);
        this.add(maleRadioButton);
        this.add(femaleRadioButton);
        this.maleRadioButton.setSelected(true);

        this.lxdhLabel.setBounds(150, 540, 100, 30);
        this.lxdhField.setBounds(250, 540, 200, 30);
        this.lxdhRemindLabel.setBounds(450, 540, 50, 30);
        this.add(lxdhLabel);
        this.add(lxdhField);
        this.add(lxdhRemindLabel);

        this.gjLabel.setBounds(600, 540, 100, 30);
        this.gjComboBox.setBounds(700, 540, 200, 30);
        this.add(gjLabel);
        this.add(gjComboBox);

        this.zsLabel.setBounds(150, 590, 100, 30);
        this.zsField.setBounds(250, 590, 200, 30);
        this.add(zsLabel);
        this.add(zsField);

        this.djjgLabel.setBounds(600, 590, 100, 30);
        this.djjgComboBox.setBounds(700, 590, 200, 30);
        this.add(djjgLabel);
        this.add(djjgComboBox);

        //投资人类型ComboBox
        try {
            connection = JDBC.getConnection();
            String SQL = "select * from tzrlx";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Tzrlx tzrlx = new Tzrlx();
                tzrlx.setTzrid(resultSet.getString(1));
                tzrlx.setTzrname(resultSet.getString(2));
                tzrlxComboBox.addItem(tzrlx);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }

        //证件类型ComboBox
        try {
            connection = JDBC.getConnection();
            String SQL = "select * from zjlx";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Zjlx zjlx = new Zjlx();
                zjlx.setZjlxid(resultSet.getString(1));
                zjlx.setZjlxname(resultSet.getString(2));
                zjlxComboBox.addItem(zjlx);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }

        //国籍ComboBox
        try {
            connection = JDBC.getConnection();
            String SQL = "select * from gj";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Gj gj = new Gj();
                gj.setGjid(resultSet.getString(1));
                gj.setGjname(resultSet.getString(2));
                gjComboBox.addItem(gj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }

        //登记机关ComboBox
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
        //表格
        this.table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int row = table.getSelectedRow();
                    tzrnameFidld.setText((String) table.getValueAt(row, 0));
                    for (int i = 0; i < tzrlxComboBox.getItemCount(); i++) {
                        if (table.getValueAt(row, 1).equals(tzrlxComboBox.getItemAt(i).toString())) ;
                        {
                            tzrlxComboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                    for (int i = 0; i < zjlxComboBox.getItemCount(); i++) {
                        if (table.getValueAt(row, 2).equals(zjlxComboBox.getItemAt(i).toString())) ;
                        {
                            zjlxComboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                    zjhmField.setText((String) table.getValueAt(row, 3));
                    czblField.setText((String) table.getValueAt(row, 4));
                    if (table.getValueAt(row, 5).equals("男")) {
                        maleRadioButton.setSelected(true);
                    } else {
                        femaleRadioButton.setSelected(true);
                    }
                    lxdhField.setText((String) table.getValueAt(row, 6));
                    for (int i = 0; i < gjComboBox.getItemCount(); i++) {
                        if (table.getValueAt(row, 7).equals(gjComboBox.getItemAt(i).toString())) ;
                        {
                            gjComboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                    zsField.setText((String) table.getValueAt(row, 8));
                    for (int i = 0; i < djjgComboBox.getItemCount(); i++) {
                        if (table.getValueAt(row, 7).equals(djjgComboBox.getItemAt(i).toString())) ;
                        {
                            djjgComboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        });
        //修改
        this.modifyInvestorButton.addActionListener((e) ->
        {
            if (tzrnameFidld.getText().isEmpty() || zjhmField.getText().isEmpty() || czblField.getText().isEmpty() || lxdhField.getText().isEmpty() || zsField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "投资人资料不完整", "投资人资料不完整", JOptionPane.OK_OPTION);
                return;
            }
            if (lxdhRemindLabel.getText().equals("×")) {
                JOptionPane.showMessageDialog(null, "资料信息有误", "资料信息有误", JOptionPane.OK_OPTION);
                return;
            }
            Vector<String> vector = new Vector<>();
            vector.add(tzrnameFidld.getText());
            vector.add(tzrlxComboBox.getSelectedItem().toString());
            vector.add(zjlxComboBox.getSelectedItem().toString());
            vector.add(zjhmField.getText());
            vector.add(czblField.getText());
            String sex;
            if (maleRadioButton.isSelected()) {
                sex = "男";
            } else {
                sex = "女";
            }
            vector.add(sex);
            vector.add(lxdhField.getText());
            vector.add(gjComboBox.getSelectedItem().toString());
            vector.add(zsField.getText());
            vector.add(djjgComboBox.getSelectedItem().toString());
            int row = table.getSelectedRow();
            dataVector.set(row, vector);
            table.setModel(defaultTableModel);
            jScrollPane.getViewport().add(table);
            JOptionPane.showMessageDialog(null, "修改成功", "修改成功", JOptionPane.OK_OPTION);
            this.tzrnameFidld.setText("");
            this.tzrlxComboBox.setSelectedIndex(0);
            this.zjlxComboBox.setSelectedIndex(0);
            this.zjhmField.setText("");
            this.czblField.setText("");
            this.maleRadioButton.setSelected(true);
            this.lxdhField.setText("");
            this.gjComboBox.setSelectedIndex(0);
            this.zsField.setText("");
            this.djjgComboBox.setSelectedIndex(0);
        });
    }
}
