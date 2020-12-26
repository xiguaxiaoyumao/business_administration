package com.admin;

import com.jdbc.JDBC;
import com.util.Chooser;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class RegisterCancellation extends JPanel {
    Connection connection = null;
    private Font mainFont = new Font("宋体", Font.BOLD, 20);
    private Font minorFont = new Font("宋体", Font.BOLD, 15);
    private JLabel titleLabel = new JLabel("注销登记");

    //基本控件
    public JButton selectButton = new JButton("查询");
    public JTextField queryField = new JTextField();
    private JLabel remindLabel = new JLabel("请输入企业名称或者注册号");
    //表格
    public JTable table = new JTable() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private JScrollPane jScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    private Vector<String> headVector = new Vector<String>();
    private Vector<Vector<String>> dataVector = new Vector<Vector<String>>();
    private DefaultTableModel defaultTableModel = null;

    private JLabel zxyyLabel = new JLabel("注销原因");
    private JTextField zxyyTextField = new JTextField();
    private JLabel zxsmLabel = new JLabel("注销说明");
    private JTextField zxsmTextField = new JTextField();
    private JLabel zxrqLabel = new JLabel("注销日期");
    private JTextField zxrqTextField = new JTextField();
    private JLabel qszcyLabel = new JLabel("清算组成员");
    private JTextField qszcyTextField = new JTextField();
    private JLabel qsfzrLabel = new JLabel("清算负责人");
    private JTextField qsfzrTextField = new JTextField();
    private JLabel qswjqkLabel = new JLabel("清算完结情况");
    private JTextField qswjqkTextField = new JTextField();
    private JLabel qswjrqLabel = new JLabel("清算完结日期");
    private JTextField qswjrqTextField = new JTextField();
    private JLabel zwjcrLabel = new JLabel("债务承接人");
    private JTextField zwjcrTextField = new JTextField();
    private JLabel zqjcrqLabel = new JLabel("债权承接人");
    private JTextField zqjcrqTextField = new JTextField();
    private JLabel barqLabel = new JLabel("备案日期");
    private JTextField barqTextField = new JTextField();

    private JButton cancellButton = new JButton("注销");

    //构造函数
    public RegisterCancellation() {
        this.setLayout(null);
        this.titleLabel.setSize(130, 30);
        this.queryField.setBounds(280, 130, 200, 30);
        this.selectButton.setBounds(580, 130, 100, 30);
        this.remindLabel.setBounds(280, 160, 200, 20);

        this.add(titleLabel);
        this.add(queryField);
        this.add(selectButton);
        this.add(remindLabel);
        this.add(jScrollPane);

        this.cancellButton.setBounds(480, 700, 100, 30);
        this.add(cancellButton);

        Date date = new Date();
        DateFormat dateInstance = new SimpleDateFormat("yyyy-MM-dd");

        this.zxyyLabel.setBounds(180, 250, 100, 30);
        this.zxyyTextField.setBounds(280, 250, 200, 30);
        this.add(zxyyLabel);
        this.add(zxyyTextField);

        this.zxsmLabel.setBounds(580, 250, 100, 30);
        this.zxsmTextField.setBounds(680, 250, 200, 30);
        this.add(zxsmLabel);
        this.add(zxsmTextField);

        this.zxrqLabel.setBounds(180, 350, 100, 30);
        this.zxrqTextField.setBounds(280, 350, 200, 30);
        this.zxrqTextField.setText(dateInstance.format(date));
        Chooser chooser = Chooser.getInstance();
        chooser.register(zxrqTextField);
        this.add(zxrqLabel);
        this.add(zxrqTextField);

        this.qszcyLabel.setBounds(580, 350, 100, 30);
        this.qszcyTextField.setBounds(680, 350, 200, 30);
        this.add(qszcyLabel);
        this.add(qszcyTextField);

        this.qsfzrLabel.setBounds(180, 450, 100, 30);
        this.qsfzrTextField.setBounds(280, 450, 200, 30);
        this.add(qsfzrLabel);
        this.add(qsfzrTextField);

        this.qswjqkLabel.setBounds(580, 450, 100, 30);
        this.qswjqkTextField.setBounds(680, 450, 200, 30);
        this.add(qswjqkLabel);
        this.add(qswjqkTextField);

        this.qswjrqLabel.setBounds(180, 550, 100, 30);
        this.qswjrqTextField.setBounds(280, 550, 200, 30);
        this.qswjrqTextField.setText(dateInstance.format(date));
        Chooser chooser1 = Chooser.getInstance();
        chooser1.register(qswjrqTextField);
        this.add(qswjrqLabel);
        this.add(qswjrqTextField);

        this.zwjcrLabel.setBounds(580, 550, 100, 30);
        this.zwjcrTextField.setBounds(680, 550, 200, 30);
        this.add(zwjcrLabel);
        this.add(zwjcrTextField);

        this.zqjcrqLabel.setBounds(180, 650, 100, 30);
        this.zqjcrqTextField.setBounds(280, 650, 200, 30);
        this.add(zqjcrqLabel);
        this.add(zqjcrqTextField);

        this.barqLabel.setBounds(580, 650, 100, 30);
        this.barqTextField.setBounds(680, 650, 200, 30);
        this.barqTextField.setText(dateInstance.format(date));
        Chooser chooser2 = Chooser.getInstance();
        chooser2.register(barqTextField);
        this.add(barqLabel);
        this.add(barqTextField);

        //表头添加数据
        this.headVector.add("名称");
        this.headVector.add("注册号");
        this.headVector.add("类型");
        this.headVector.add("法定代表人");
        this.headVector.add("注册资本");
        this.headVector.add("处理日期");
        this.headVector.add("住所");
        this.headVector.add("营业期限始");
        this.headVector.add("营业期限至");
        this.headVector.add("经营范围");
        this.headVector.add("登记机关");
        this.headVector.add("核准日期");
        this.headVector.add("登记状态");

        //表模型
        this.defaultTableModel = new DefaultTableModel(dataVector, headVector);
        this.table.setModel(defaultTableModel);
        this.jScrollPane.getViewport().add(table);
        this.jScrollPane.setBounds(10, 20, 1040, 100);
        //设置列宽
        this.table.getColumnModel().getColumn(0).setPreferredWidth(80);
        this.table.getColumnModel().getColumn(1).setPreferredWidth(106);
        this.table.getColumnModel().getColumn(2).setPreferredWidth(80);
        this.table.getColumnModel().getColumn(3).setPreferredWidth(80);
        this.table.getColumnModel().getColumn(4).setPreferredWidth(75);
        this.table.getColumnModel().getColumn(5).setPreferredWidth(86);
        this.table.getColumnModel().getColumn(6).setPreferredWidth(80);
        this.table.getColumnModel().getColumn(7).setPreferredWidth(86);
        this.table.getColumnModel().getColumn(8).setPreferredWidth(86);
        this.table.getColumnModel().getColumn(9).setPreferredWidth(80);
        this.table.getColumnModel().getColumn(10).setPreferredWidth(60);
        this.table.getColumnModel().getColumn(11).setPreferredWidth(86);
        this.table.getColumnModel().getColumn(12).setPreferredWidth(55);

        //列宽不能改变 不能重新排序
        //this.table.getTableHeader().setResizingAllowed(false);
        this.table.getTableHeader().setReorderingAllowed(false);
        //居中
        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        this.table.setDefaultRenderer(Object.class, defaultTableCellRenderer);

        //查询监听事件
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
        //给文本框添加键盘事件
        this.queryField.addKeyListener(listener);

        //注销
        this.cancellButton.addActionListener((e) ->
        {
            this.logoff();
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
        //根据名称查数据
        try {
            Connection connection = JDBC.getConnection();
            dataVector.clear();
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
                    java.sql.Date date = null;
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
                    defaultTableModel.addRow(vector);
                }
            }
            table.setModel(defaultTableModel);
            if (table.getRowCount() == 0) {
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

    //注销
    private void logoff() {
        int value = 0;
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "请选择注销的企业");
            return;
        }
        if (zxrqTextField.getText().isEmpty() || zxsmTextField.getText().isEmpty() || qszcyTextField.getText().isEmpty() || qsfzrTextField.getText().isEmpty() || qswjqkTextField.getText().isEmpty() || zwjcrTextField.getText().isEmpty() || zqjcrqTextField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "资料不完整");
            return;
        }
        try {
            this.connection = JDBC.getConnection();
            String SQL = "insert into cancel_the_condition(ZXYY,ZXSM,ZXRQ,QSZCY,QSFZR,QSWJQK,QSWJRQ,ZWJCR,ZQCJR,BARQ,scztmc) values(?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, "1");
            preparedStatement.setObject(2, zxsmTextField.getText());
            preparedStatement.setObject(3, zxrqTextField.getText());
            preparedStatement.setObject(4, qszcyTextField.getText());
            preparedStatement.setObject(5, qsfzrTextField.getText());
            preparedStatement.setObject(6, qswjqkTextField.getText());
            preparedStatement.setObject(7, qswjrqTextField.getText());
            preparedStatement.setObject(8, zwjcrTextField.getText());
            preparedStatement.setObject(9, zqjcrqTextField.getText());
            preparedStatement.setObject(10, barqTextField.getText());
            preparedStatement.setObject(11, table.getValueAt(row, 0));
            value = preparedStatement.executeUpdate();
            if (value == 1) {
                JOptionPane.showMessageDialog(null, "成功注销");
            } else {
                JOptionPane.showMessageDialog(null, "错误");
                return;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }

        if (value != 1) {
            JOptionPane.showMessageDialog(null, "错误");
            return;
        }
        //将状态改为注销
        try {
            this.connection = JDBC.getConnection();
            String SQL = "update market_subject_information set jgztdm = '4' where scztmc = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, table.getValueAt(row, 0));
            value = preparedStatement.executeUpdate();
            if (value != 1) {
                JOptionPane.showMessageDialog(null, "错误");
                return;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }
    }
}
