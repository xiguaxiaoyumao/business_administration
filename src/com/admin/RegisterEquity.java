package com.admin;

import com.combobox.Djjg;
import com.combobox.Gj;
import com.combobox.Tzrlx;
import com.combobox.Zjlx;
import com.jdbc.JDBC;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.DateFormat;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterEquity extends JPanel {
    //连接
    private Connection connection = null;
    //标题
    private Font mainFont = new Font("宋体", Font.BOLD, 20);
    private JLabel titleLabel = new JLabel("股权分配");
    //查询表
    public JTable tableSelect = new JTable() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private JScrollPane jScrollPaneSelect = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    private Vector<String> headVectorSelect = new Vector<String>();
    private Vector<Vector<String>> dataVectorSelect = new Vector<Vector<String>>();
    private DefaultTableModel defaultTableModelSelect = null;

    public JButton selectButton = new JButton("查询");
    public JTextField queryField = new JTextField();
    private JLabel remindLabel = new JLabel("请输入企业名称或者注册号");

    //投资人表
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

    Vector<String> TZRMC = new Vector<String>();

    private JButton confirmButton = new JButton("确认");

    //构造函数
    public RegisterEquity() {
        this.setLayout(null);
        //标题
        this.titleLabel.setBounds(0, 0, 130, 30);
        this.titleLabel.setFont(mainFont);
        this.add(titleLabel);

        //表头添加数据
        this.headVectorSelect.add("名称");
        this.headVectorSelect.add("注册号");
        this.headVectorSelect.add("类型");
        this.headVectorSelect.add("法定代表人");
        this.headVectorSelect.add("注册资本");
        this.headVectorSelect.add("处理日期");
        this.headVectorSelect.add("住所");
        this.headVectorSelect.add("营业期限始");
        this.headVectorSelect.add("营业期限至");
        this.headVectorSelect.add("经营范围");
        this.headVectorSelect.add("登记机关");
        this.headVectorSelect.add("核准日期");
        this.headVectorSelect.add("登记状态");

        this.defaultTableModelSelect = new DefaultTableModel(dataVectorSelect, headVectorSelect);
        this.tableSelect.setModel(defaultTableModelSelect);
        this.jScrollPaneSelect.getViewport().add(tableSelect);

        this.tableSelect.getColumnModel().getColumn(0).setPreferredWidth(80);
        this.tableSelect.getColumnModel().getColumn(1).setPreferredWidth(106);
        this.tableSelect.getColumnModel().getColumn(2).setPreferredWidth(80);
        this.tableSelect.getColumnModel().getColumn(3).setPreferredWidth(80);
        this.tableSelect.getColumnModel().getColumn(4).setPreferredWidth(75);
        this.tableSelect.getColumnModel().getColumn(5).setPreferredWidth(86);
        this.tableSelect.getColumnModel().getColumn(6).setPreferredWidth(80);
        this.tableSelect.getColumnModel().getColumn(7).setPreferredWidth(86);
        this.tableSelect.getColumnModel().getColumn(8).setPreferredWidth(86);
        this.tableSelect.getColumnModel().getColumn(9).setPreferredWidth(80);
        this.tableSelect.getColumnModel().getColumn(10).setPreferredWidth(60);
        this.tableSelect.getColumnModel().getColumn(11).setPreferredWidth(86);

        this.queryField.setBounds(300, 128, 200, 30);
        this.selectButton.setBounds(600, 128, 100, 30);
        this.remindLabel.setBounds(300, 158, 200, 20);
        this.jScrollPaneSelect.setBounds(10, 20, 1060, 100);

        this.add(queryField);
        this.add(selectButton);
        this.add(remindLabel);
        this.add(jScrollPaneSelect);

        //投资人表
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
        this.jScrollPane.setBounds(10, 180, 1060, 220);
        this.table.getTableHeader().setReorderingAllowed(false);
        this.defaultTableCellRenderer = new DefaultTableCellRenderer();
        this.defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        this.table.setDefaultRenderer(Object.class, defaultTableCellRenderer);
        this.add(jScrollPane);

        this.addInvestorButton.setBounds(280, 420, 100, 30);
        this.addRadioButton.setBounds(180, 400, 60, 20);
        this.modifyInvestorButton.setBounds(480, 420, 100, 30);
        this.modifyRadioButton.setBounds(180, 430, 60, 20);
        this.deleteInvestorButton.setBounds(680, 420, 100, 30);
        this.deleteRadioButton.setBounds(180, 460, 60, 20);

        this.add(addInvestorButton);
        this.add(addRadioButton);
        this.add(modifyInvestorButton);
        this.add(modifyRadioButton);
        this.add(deleteInvestorButton);
        this.add(deleteRadioButton);
        this.buttonGroup1.add(addRadioButton);
        this.buttonGroup1.add(modifyRadioButton);
        this.buttonGroup1.add(deleteRadioButton);

        //信息控件
        this.tzrnameLabel.setBounds(180, 490, 100, 30);
        this.tzrnameFidld.setBounds(280, 490, 200, 30);
        this.add(tzrnameLabel);
        this.add(tzrnameFidld);

        this.tzrlxLabel.setBounds(580, 490, 390, 30);
        this.tzrlxComboBox.setBounds(680, 490, 200, 30);
        this.add(tzrlxLabel);
        this.add(tzrlxComboBox);

        this.zjlxLabel.setBounds(180, 540, 100, 30);
        this.zjlxComboBox.setBounds(280, 540, 200, 30);
        this.add(zjlxLabel);
        this.add(zjlxComboBox);

        this.zjhmLabel.setBounds(580, 540, 100, 30);
        this.zjhmField.setBounds(680, 540, 200, 30);
        this.zjhmRmindLabel.setBounds(880, 540, 50, 30);
        this.add(zjhmLabel);
        this.add(zjhmField);
        this.add(zjhmRmindLabel);

        this.czblLabel.setBounds(180, 590, 100, 30);
        this.czblField.setBounds(280, 590, 200, 30);
        this.add(czblLabel);
        this.add(czblField);

        this.xbLabel.setBounds(580, 590, 100, 30);
        this.maleRadioButton.setBounds(680, 590, 100, 30);
        this.femaleRadioButton.setBounds(780, 590, 100, 30);
        this.add(xbLabel);
        this.buttonGroup.add(maleRadioButton);
        this.buttonGroup.add(femaleRadioButton);
        this.add(maleRadioButton);
        this.add(femaleRadioButton);
        this.maleRadioButton.setSelected(true);

        this.lxdhLabel.setBounds(180, 640, 100, 30);
        this.lxdhField.setBounds(280, 640, 200, 30);
        this.lxdhRemindLabel.setBounds(480, 640, 50, 30);
        this.add(lxdhLabel);
        this.add(lxdhField);
        this.add(lxdhRemindLabel);

        this.gjLabel.setBounds(580, 640, 100, 30);
        this.gjComboBox.setBounds(680, 640, 200, 30);
        this.add(gjLabel);
        this.add(gjComboBox);

        this.zsLabel.setBounds(180, 690, 100, 30);
        this.zsField.setBounds(280, 690, 200, 30);
        this.add(zsLabel);
        this.add(zsField);

        this.djjgLabel.setBounds(580, 690, 100, 30);
        this.djjgComboBox.setBounds(680, 690, 200, 30);
        this.add(djjgLabel);
        this.add(djjgComboBox);
        this.addRadioButton.setSelected(true);
        this.addInvestorButton.setEnabled(true);
        this.modifyInvestorButton.setEnabled(false);
        this.deleteInvestorButton.setEnabled(false);
        this.table.setRowSelectionAllowed(false);

        this.confirmButton.setBounds(480, 730, 100, 30);
        this.add(confirmButton);

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

        //查询表监听事件
        this.tableSelect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    TZRMC.clear();
                    int row = tableSelect.getSelectedRow();
                    try {
                        connection = JDBC.getConnection();
                        String SQL = "SELECT tzrqy.TZR FROM tzrqy WHERE tzrqy.TZQYMC =?";
                        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                        preparedStatement.setObject(1, tableSelect.getValueAt(row, 0));
                        ResultSet resultSet = preparedStatement.executeQuery();
                        while (resultSet.next()) {
                            TZRMC.add(resultSet.getString(1));
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    } finally {
                        JDBC.returnConnection(connection);
                    }

                    //通过名称获取信息
                    for (String string : TZRMC) {
                        try {
                            connection = JDBC.getConnection();
                            String SQL = "select TZRMC,(SELECT TZRLX.TZRLXNAME FROM tzrlx,investor_information WHERE tzrlx.TZRLXID = investor_information.TZRLX AND TZRMC = ?),(SELECT zjlx.ZJLXNAME FROM zjlx,investor_information WHERE ZJLX.ZJLXID = investor_information.ZJLX AND TZRMC = ?),ZJHM,CZBL,(SELECT xb.XBNAME FROM xb ,investor_information WHERE xb.XBID = investor_information.XB AND TZRMC = ?),LXDH,(SELECT GJ.GJNAME FROM gj ,investor_information WHERE gj.GJID = investor_information.GJ AND TZRMC = ?),ZS,(SELECT djjgdm.DJJGDMNAME FROM djjgdm,investor_information WHERE djjgdm.DJJGDMID = investor_information.DJJG AND TZRMC = ?) from investor_information where tzrmc = ?";
                            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                            preparedStatement.setObject(1, string);
                            preparedStatement.setObject(2, string);
                            preparedStatement.setObject(3, string);
                            preparedStatement.setObject(4, string);
                            preparedStatement.setObject(5, string);
                            preparedStatement.setObject(6, string);
                            ResultSet resultSet = preparedStatement.executeQuery();
                            while (resultSet.next()) {
                                Vector<String> vector = new Vector<String>();
                                vector.add(resultSet.getString(1));
                                vector.add(resultSet.getString(2));
                                vector.add(resultSet.getString(3));
                                vector.add(resultSet.getString(4));
                                vector.add(resultSet.getString(5));
                                vector.add(resultSet.getString(6));
                                vector.add(resultSet.getString(7));
                                vector.add(resultSet.getString(8));
                                vector.add(resultSet.getString(9));
                                vector.add(resultSet.getString(10));
                                dataVector.add(vector);
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        } finally {
                            JDBC.returnConnection(connection);
                        }
                    }
                    DefaultTableModel defaultTableModel1 = new DefaultTableModel(dataVector, headVector);
                    table.setModel(defaultTableModel1);
                    if (table.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(null, "信息加载失败");
                    } else {
                        JOptionPane.showMessageDialog(null, "信息加载成功");
                    }
                }
            }
        });
        //查询
        selectButton.addActionListener((e) ->
        {
            this.select();
        });
        //键盘事件
        KeyListener listener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {

            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                if (keyEvent.getKeyChar() == KeyEvent.VK_ENTER) {
                    select();
                }
            }
        };

        //确认调整
        this.confirmButton.addActionListener((e) ->
        {
            int row = tableSelect.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "请选择企业");
                return;
            }
            int num = 0;
            for (int i = 0; i < this.table.getRowCount(); i++) {
                if (this.table.getValueAt(i, 1).equals("企业法人")) {
                    num++;
                }
            }
            if (num != 1) {
                JOptionPane.showMessageDialog(null, "一个企业法人", "一个企业法人", JOptionPane.OK_OPTION);
                return;
            }
            //更新原有投资人 插入新投资人
            for (int i = 0; i < table.getRowCount(); i++) {
                connection = JDBC.getConnection();
                String SQL;
                PreparedStatement preparedStatement = null;
                if (TZRMC.indexOf(table.getValueAt(i, 0)) != -1) {
                    try {
                        SQL = "update investor_information set TZRLX= ?,ZJLX= ?,ZJHM= ?,CZBL= ?,XB= ?,LXDH= ?,GJ= ?,ZS= ?,DJJG= ? where TZRMC = ?";
                        preparedStatement = connection.prepareStatement(SQL);
                        preparedStatement.setObject(10, tableSelect.getValueAt(row, 0));
                        for (int j = 0; j < tzrlxComboBox.getItemCount(); j++) {
                            if (table.getValueAt(i, 1).equals(tzrlxComboBox.getItemAt(j).toString())) {
                                preparedStatement.setObject(1, ((Tzrlx) tzrlxComboBox.getItemAt(j)).getTzrid());
                                break;
                            }
                        }
                        for (int j = 0; j < zjlxComboBox.getItemCount(); j++) {
                            if (table.getValueAt(i, 2).equals(zjlxComboBox.getItemAt(j).toString())) {
                                preparedStatement.setObject(2, ((Zjlx) (zjlxComboBox.getItemAt(j))).getZjlxid());
                                break;
                            }
                        }
                        preparedStatement.setObject(3, table.getValueAt(i, 3));
                        preparedStatement.setObject(4, table.getValueAt(i, 4));
                        if (table.getValueAt(i, 4).equals("男")) {
                            preparedStatement.setObject(5, "1");
                        } else {
                            preparedStatement.setObject(5, "2");
                        }
                        preparedStatement.setObject(6, table.getValueAt(i, 6));

                        for (int j = 0; j < gjComboBox.getItemCount(); j++) {
                            if (table.getValueAt(i, 7).equals(gjComboBox.getItemAt(j).toString())) {
                                preparedStatement.setObject(7, ((Gj) (gjComboBox.getItemAt(j))).getGjid());
                                break;
                            }
                        }
                        preparedStatement.setObject(8, table.getValueAt(i, 8));
                        for (int j = 0; j < djjgComboBox.getItemCount(); j++) {
                            if (table.getValueAt(i, 9).equals(djjgComboBox.getItemAt(j).toString())) {
                                preparedStatement.setObject(9, djjgComboBox.getItemAt(j).getDjjgid());
                                break;
                            }
                        }
                        preparedStatement.executeUpdate();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    } finally {
                        JDBC.returnConnection(connection);
                    }
                    int value = TZRMC.indexOf(table.getValueAt(i, 0));
                    TZRMC.remove(value);
                } else {
                    try {
                        SQL = "insert into investor_information (TZRMC,TZRLX,ZJLX,ZJHM,CZBL,XB,LXDH,GJ,ZS,DJJG) values (?,?,?,?,?,?,?,?,?,?)";
                        preparedStatement = this.connection.prepareStatement(SQL);
                        preparedStatement.setObject(1, this.table.getValueAt(i, 0));
                        for (int j = 0; j < tzrlxComboBox.getItemCount(); j++) {
                            if (table.getValueAt(i, 1).equals(tzrlxComboBox.getItemAt(j).toString())) {
                                preparedStatement.setObject(2, ((Tzrlx) (tzrlxComboBox.getItemAt(j))).getTzrid());
                            }
                        }
                        for (int j = 0; j < zjlxComboBox.getItemCount(); j++) {
                            if (this.table.getValueAt(i, 2).equals(this.zjlxComboBox.getItemAt(j).toString())) {
                                preparedStatement.setObject(3, ((Zjlx) (this.zjlxComboBox.getItemAt(j))).getZjlxid());
                            }
                        }
                        preparedStatement.setObject(4, this.table.getValueAt(i, 3));
                        preparedStatement.setObject(5, this.table.getValueAt(i, 4));
                        if (this.table.getValueAt(i, 4).equals("男")) {
                            preparedStatement.setObject(6, "1");
                        } else {
                            preparedStatement.setObject(6, "2");
                        }
                        preparedStatement.setObject(7, this.table.getValueAt(i, 6));

                        for (int j = 0; j < this.gjComboBox.getItemCount(); j++) {
                            if (this.table.getValueAt(i, 7).equals(this.gjComboBox.getItemAt(j).toString())) {
                                preparedStatement.setObject(8, ((Gj) (this.gjComboBox.getItemAt(j))).getGjid());
                            }
                        }
                        preparedStatement.setObject(9, this.table.getValueAt(i, 8));
                        for (int j = 0; j < this.djjgComboBox.getItemCount(); j++) {
                            if (this.table.getValueAt(i, 9).equals(this.djjgComboBox.getItemAt(j).toString())) {
                                preparedStatement.setObject(10, this.djjgComboBox.getItemAt(j).getDjjgid());
                                break;
                            }
                        }
                        int c = preparedStatement.executeUpdate();
                        //调整多对多关系表
                        if (c == 1) {
                            try {
                                this.connection = JDBC.getConnection();
                                String SQL2 = "insert into tzrqy (tzr,tzqymc) values(?,?)";
                                PreparedStatement preparedStatement2 = this.connection.prepareStatement(SQL2);
                                preparedStatement2.setObject(1, this.table.getValueAt(i, 0));
                                preparedStatement2.setObject(2, this.tableSelect.getValueAt(row, 0));
                                preparedStatement2.executeUpdate();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            } finally {
                                JDBC.returnConnection(this.connection);
                            }

                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    } finally {

                        JDBC.returnConnection(connection);
                    }
                }
                JOptionPane.showMessageDialog(null, "成功");
            }

            //删除的投资人
            for (String string : TZRMC) {
                try {
                    connection = JDBC.getConnection();
                    String SQL = "delete from tzrqy where tzr = ? and tzqymc =? ";
                    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setObject(1, string);
                    preparedStatement.setObject(2, tableSelect.getValueAt(row, 0));
                    preparedStatement.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    JDBC.returnConnection(connection);
                }
            }
        });
    }

    //查询函数
    private void select() {
        Vector<String> SCZTMC = new Vector<String>();
        if (queryField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "不能为空", "不能为空", JOptionPane.OK_OPTION);
            return;
        }
        try {
            connection = JDBC.getConnection();
            String SQL = "select SCZTMC from market_subject_information where SCZTMC like ? and JGZTDM != ? and lcztdm = '4'";
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
        //根据名称查数据
        try {
            Connection connection = JDBC.getConnection();
            dataVectorSelect.clear();
            for (String string : SCZTMC) {
                String SQL = "select ZCH,(SELECT lxdm.LXDMNAME FROM market_subject_information , lxdm WHERE market_subject_information.LXDM = lxdm.LXDMID AND market_subject_information.SCZTMC = ?),(SELECT tzrqy.TZR  FROM tzrqy WHERE tzrqy.TZQYMC = ? AND EXISTS (SELECT investor_information.TZRMC FROM investor_information WHERE TZRLX = '3' AND tzrqy.TZR = investor_information.TZRMC)),ZCZB,CLRQ,ZS,JYQXQ,JYQXZ,JYFW,(SELECT djjgdm.DJJGDMNAME FROM market_subject_information , djjgdm WHERE market_subject_information.DJJGDM = DJJGDM.DJJGDMID AND market_subject_information.SCZTMC = ?),HZRQ,(SELECT jgzt.JGZTDMNAME FROM market_subject_information , jgzt WHERE market_subject_information.JGZTDM = jgzt.JGZTDMID AND market_subject_information.SCZTMC = ?)from market_subject_information where SCZTMC = ? ";
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setObject(1, string);
                preparedStatement.setObject(2, string);
                preparedStatement.setObject(3, string);
                preparedStatement.setObject(4, string);
                preparedStatement.setObject(5, string);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Vector vector = new Vector();
                    Date date = null;
                    DateFormat dateFormat = DateFormat.getDateInstance();
                    vector.add(string);
                    vector.add(resultSet.getString(1));
                    vector.add(resultSet.getString(2));
                    vector.add(resultSet.getString(3));
                    vector.add(resultSet.getInt(4));
                    date = resultSet.getDate(5);
                    if (date == null) {
                        vector.add("");
                    } else {
                        vector.add(dateFormat.format(date));
                    }
                    vector.add(resultSet.getString(6));
                    date = resultSet.getDate(7);
                    if (date == null) {
                        vector.add("");
                    } else {
                        vector.add(dateFormat.format(date));
                    }
                    date = resultSet.getDate(8);
                    if (date == null) {
                        vector.add("");
                    } else {
                        vector.add(dateFormat.format(date));
                    }
                    vector.add(resultSet.getString(9));
                    vector.add(resultSet.getString(10));
                    date = resultSet.getDate(11);
                    if (date == null) {
                        vector.add("");
                    } else {
                        vector.add(dateFormat.format(date));
                    }
                    vector.add(resultSet.getString(12));
                    defaultTableModelSelect.addRow(vector);
                }
            }
            tableSelect.setModel(defaultTableModelSelect);
            if (tableSelect.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "未找到相关企业", "未找到相关企业", JOptionPane.OK_OPTION);
                queryField.setText("");
                return;
            } else {
                //JOptionPane.showMessageDialog(null,"查询成功","查询成功",JOptionPane.OK_OPTION);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }
    }
}
