package com.user.preapproved;

import com.jdbc.JDBC;
import com.combobox.Djjg;
import com.combobox.Gj;
import com.combobox.Tzrlx;
import com.combobox.Zjlx;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private DefaultTableModel defaultTableModel = null;
    private DefaultTableCellRenderer defaultTableCellRenderer = null;

    public JButton addInvestorButton = new JButton("添加投资人");
    public JRadioButton addRadioButton = new JRadioButton("添加");
    public JButton modifyInvestorButton = new JButton("修改投资人");
    public JRadioButton modifyRadioButton = new JRadioButton("修改");
    public JButton deleteInvestorButton = new JButton("删除投资人");
    public JRadioButton deleteRadioButton = new JRadioButton("删除");
    public ButtonGroup buttonGroup1 = new ButtonGroup();

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

        this.defaultTableModel = new DefaultTableModel(dataVector, headVector);
        this.table.setModel(defaultTableModel);
        this.table.getColumnModel().getColumn(0).setPreferredWidth(100);
        this.table.getColumnModel().getColumn(1).setPreferredWidth(100);
        this.table.getColumnModel().getColumn(2).setPreferredWidth(100);
        this.table.getColumnModel().getColumn(3).setPreferredWidth(160);
        this.table.getColumnModel().getColumn(4).setPreferredWidth(60);
        this.table.getColumnModel().getColumn(5).setPreferredWidth(60);
        this.table.getColumnModel().getColumn(6).setPreferredWidth(100);
        this.table.getColumnModel().getColumn(7).setPreferredWidth(60);
        this.table.getColumnModel().getColumn(8).setPreferredWidth(100);
        this.table.getColumnModel().getColumn(9).setPreferredWidth(160);
        this.jScrollPane.getViewport().add(table);
        this.jScrollPane.setBounds(10, 50, 1060, 250);
        this.table.getTableHeader().setReorderingAllowed(false);
        this.defaultTableCellRenderer = new DefaultTableCellRenderer();
        this.defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        this.table.setDefaultRenderer(Object.class, defaultTableCellRenderer);
        this.add(jScrollPane);

        this.addInvestorButton.setBounds(330, 320, 100, 30);
        this.addRadioButton.setBounds(150, 300, 60, 20);
        this.modifyInvestorButton.setBounds(480, 320, 100, 30);
        this.modifyRadioButton.setBounds(150, 330, 60, 20);
        this.deleteInvestorButton.setBounds(630, 320, 100, 30);
        this.deleteRadioButton.setBounds(150, 360, 60, 20);
        this.add(addInvestorButton);
        this.add(addRadioButton);
        this.add(modifyInvestorButton);
        this.add(modifyRadioButton);
        this.add(deleteInvestorButton);
        this.add(deleteRadioButton);
        this.buttonGroup1.add(addRadioButton);
        this.buttonGroup1.add(modifyRadioButton);
        this.buttonGroup1.add(deleteRadioButton);

        this.tzrnameLabel.setBounds(180, 390, 100, 30);
        this.tzrnameFidld.setBounds(280, 390, 200, 30);
        this.add(tzrnameLabel);
        this.add(tzrnameFidld);

        this.tzrlxLabel.setBounds(580, 390, 390, 30);
        this.tzrlxComboBox.setBounds(680, 390, 200, 30);
        this.add(tzrlxLabel);
        this.add(tzrlxComboBox);

        this.zjlxLabel.setBounds(180, 440, 100, 30);
        this.zjlxComboBox.setBounds(280, 440, 200, 30);
        this.add(zjlxLabel);
        this.add(zjlxComboBox);

        this.zjhmLabel.setBounds(580, 440, 100, 30);
        this.zjhmField.setBounds(680, 440, 200, 30);
        this.zjhmRmindLabel.setBounds(880, 440, 50, 30);
        this.add(zjhmLabel);
        this.add(zjhmField);
        this.add(zjhmRmindLabel);

        this.czblLabel.setBounds(180, 490, 100, 30);
        this.czblField.setBounds(280, 490, 200, 30);
        this.add(czblLabel);
        this.add(czblField);

        this.xbLabel.setBounds(580, 490, 100, 30);
        this.maleRadioButton.setBounds(680, 490, 100, 30);
        this.femaleRadioButton.setBounds(780, 490, 100, 30);
        this.add(xbLabel);
        this.buttonGroup.add(maleRadioButton);
        this.buttonGroup.add(femaleRadioButton);
        this.add(maleRadioButton);
        this.add(femaleRadioButton);
        this.maleRadioButton.setSelected(true);

        this.lxdhLabel.setBounds(180, 540, 100, 30);
        this.lxdhField.setBounds(280, 540, 200, 30);
        this.lxdhRemindLabel.setBounds(480, 540, 50, 30);
        this.add(lxdhLabel);
        this.add(lxdhField);
        this.add(lxdhRemindLabel);

        this.gjLabel.setBounds(580, 540, 100, 30);
        this.gjComboBox.setBounds(680, 540, 200, 30);
        this.add(gjLabel);
        this.add(gjComboBox);

        this.zsLabel.setBounds(180, 590, 100, 30);
        this.zsField.setBounds(280, 590, 200, 30);
        this.add(zsLabel);
        this.add(zsField);

        this.djjgLabel.setBounds(580, 590, 100, 30);
        this.djjgComboBox.setBounds(680, 590, 200, 30);
        this.add(djjgLabel);
        this.add(djjgComboBox);
        this.addRadioButton.setSelected(true);
        this.addInvestorButton.setEnabled(true);
        this.modifyInvestorButton.setEnabled(false);
        this.deleteInvestorButton.setEnabled(false);
        this.table.setRowSelectionAllowed(false);

        //投资人类型ComboBox
        try {
            this.connection = JDBC.getConnection();
            String SQL = "select * from tzrlx";
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Tzrlx tzrlx = new Tzrlx();
                tzrlx.setTzrid(resultSet.getString(1));
                tzrlx.setTzrname(resultSet.getString(2));
                this.tzrlxComboBox.addItem(tzrlx);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }

        //证件类型ComboBox
        try {
            this.connection = JDBC.getConnection();
            String SQL = "select * from zjlx";
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Zjlx zjlx = new Zjlx();
                zjlx.setZjlxid(resultSet.getString(1));
                zjlx.setZjlxname(resultSet.getString(2));
                this.zjlxComboBox.addItem(zjlx);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(this.connection);
        }

        //国籍ComboBox
        try {
            this.connection = JDBC.getConnection();
            String SQL = "select * from gj";
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Gj gj = new Gj();
                gj.setGjid(resultSet.getString(1));
                gj.setGjname(resultSet.getString(2));
                this.gjComboBox.addItem(gj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(this.connection);
        }

        //登记机关ComboBox
        try {
            this.connection = JDBC.getConnection();
            String SQL = "select * from djjgdm";
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Djjg djjg = new Djjg();
                djjg.setDjjgid(resultSet.getString(1));
                djjg.setDjjgname(resultSet.getString(2));
                this.djjgComboBox.addItem(djjg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(this.connection);
        }
        //添加投资人
        this.addInvestorButton.addActionListener((e) ->
        {
            if (this.tzrnameFidld.getText().isEmpty() || this.zjhmField.getText().isEmpty() || this.czblField.getText().isEmpty() || this.lxdhField.getText().isEmpty() || this.zsField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "投资人资料不完整", "投资人资料不完整", JOptionPane.OK_OPTION);
                return;
            }
            if (this.lxdhRemindLabel.getText().equals("×")) {
                JOptionPane.showMessageDialog(null, "资料信息有误", "资料信息有误", JOptionPane.OK_OPTION);
                return;
            }
            Vector<String> vector = new Vector<>();
            vector.add(this.tzrnameFidld.getText());
            vector.add(this.tzrlxComboBox.getSelectedItem().toString());
            vector.add(this.zjlxComboBox.getSelectedItem().toString());
            vector.add(this.zjhmField.getText());
            vector.add(this.czblField.getText());
            String sex;
            if (this.maleRadioButton.isSelected()) {
                sex = "男";
            } else {
                sex = "女";
            }
            vector.add(sex);
            vector.add(this.lxdhField.getText());
            vector.add(this.gjComboBox.getSelectedItem().toString());
            vector.add(this.zsField.getText());
            vector.add(this.djjgComboBox.getSelectedItem().toString());
            this.dataVector.add(vector);
            this.table.setModel(this.defaultTableModel);
            this.jScrollPane.getViewport().add(this.table);
            JOptionPane.showMessageDialog(null, "添加成功", "添加成功", JOptionPane.OK_OPTION);
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

        //删除投资人
        this.deleteInvestorButton.addActionListener((e) ->
        {
            int row = this.table.getSelectedRow();
            this.dataVector.remove(row);
            this.table.setModel(this.defaultTableModel);
            this.jScrollPane.getViewport().add(this.table);
        });
        //修改投资人
        this.modifyInvestorButton.addActionListener((e) ->
        {
            if (this.tzrnameFidld.getText().isEmpty() || this.zjhmField.getText().isEmpty() || this.czblField.getText().isEmpty() || this.lxdhField.getText().isEmpty() || this.zsField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "投资人资料不完整", "投资人资料不完整", JOptionPane.OK_OPTION);
                return;
            }
            if (this.lxdhRemindLabel.getText().equals("×")) {
                JOptionPane.showMessageDialog(null, "资料信息有误", "资料信息有误", JOptionPane.OK_OPTION);
                return;
            }
            Vector<String> vector = new Vector<>();
            vector.add(this.tzrnameFidld.getText());
            vector.add(this.tzrlxComboBox.getSelectedItem().toString());
            vector.add(this.zjlxComboBox.getSelectedItem().toString());
            vector.add(this.zjhmField.getText());
            vector.add(this.czblField.getText());
            String sex;
            if (this.maleRadioButton.isSelected()) {
                sex = "男";
            } else {
                sex = "女";
            }
            vector.add(sex);
            vector.add(this.lxdhField.getText());
            vector.add(this.gjComboBox.getSelectedItem().toString());
            vector.add(this.zsField.getText());
            vector.add(this.djjgComboBox.getSelectedItem().toString());
            int row = this.table.getSelectedRow();
            this.dataVector.set(row, vector);
            this.table.setModel(this.defaultTableModel);
            this.jScrollPane.getViewport().add(this.table);
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

        //联系电话判断
        this.lxdhField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                String regex = "^1[3456789]\\d{9}$";
                Pattern pattern = Pattern.compile(regex);
                String target = lxdhField.getText();
                //用户填写的电话
                Matcher matcher = pattern.matcher(target);
                if (matcher.find()) {
                    lxdhRemindLabel.setText("√");
                } else {
                    lxdhRemindLabel.setText("×");
                }
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                String regex = "^1[3456789]\\d{9}$";
                Pattern pattern = Pattern.compile(regex);
                String target = lxdhField.getText();
                //用户填写的电话
                Matcher matcher = pattern.matcher(target);
                if (matcher.find()) {
                    lxdhRemindLabel.setText("√");
                } else {
                    lxdhRemindLabel.setText("×");
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
                    zjhmRmindLabel.setText("×");
                } else {
                    zjhmRmindLabel.setText("√");
                }
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                if (zjhmField.getText().length() != 18) {
                    zjhmRmindLabel.setText("×");
                } else {
                    zjhmRmindLabel.setText("√");
                }
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {

            }
        });

        this.addRadioButton.addActionListener((e) ->
        {
            this.addInvestorButton.setEnabled(true);
            this.modifyInvestorButton.setEnabled(false);
            this.deleteInvestorButton.setEnabled(false);
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

        this.modifyRadioButton.addActionListener((e) ->
        {
            this.addInvestorButton.setEnabled(false);
            this.modifyInvestorButton.setEnabled(true);
            this.deleteInvestorButton.setEnabled(false);
        });

        this.deleteRadioButton.addActionListener((e) ->
        {
            this.addInvestorButton.setEnabled(false);
            this.modifyInvestorButton.setEnabled(false);
            this.deleteInvestorButton.setEnabled(true);
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

        this.table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!modifyRadioButton.isSelected()) {
                    return;
                }
                if (e.getClickCount() == 1) {
                    int row = table.getSelectedRow();
                    tzrnameFidld.setText(table.getValueAt(row, 0).toString());
                    for (int i = 0; i < tzrlxComboBox.getItemCount(); i++) {
                        if (table.getValueAt(row, 1).toString().equals(tzrlxComboBox.getItemAt(i).toString())) {
                            tzrlxComboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                    for (int i = 0; i < zjlxComboBox.getItemCount(); i++) {
                        if ((table.getValueAt(row, 2).toString()).equals(zjlxComboBox.getItemAt(i).toString())) {
                            zjlxComboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                    zjhmField.setText(table.getValueAt(row, 3).toString());
                    czblField.setText(table.getValueAt(row, 4).toString());
                    if (table.getValueAt(row, 5).toString().equals("男")) {
                        maleRadioButton.setSelected(true);
                    } else {
                        femaleRadioButton.setSelected(true);
                    }
                    lxdhField.setText(table.getValueAt(row, 6).toString());
                    for (int i = 0; i < gjComboBox.getItemCount(); i++) {
                        if (table.getValueAt(row, 7).toString().equals(gjComboBox.getItemAt(i).toString())) {
                            gjComboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                    zsField.setText(table.getValueAt(row, 8).toString());
                    for (int i = 0; i < djjgComboBox.getItemCount(); i++) {
                        if (table.getValueAt(row, 9).toString().equals(djjgComboBox.getItemAt(i).toString())) {
                            djjgComboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        });
    }
}
