package com.user;

import com.jdbc.JDBC;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class HasApproved extends JPanel {
    private Connection connection = null;

    private Font mainFont = new Font("宋体", Font.BOLD, 20);

    private JLabel titleLabel = new JLabel("已核调整");
    private JLabel scztmcLabel = new JLabel("已核准名称");
    private JComboBox<Object> scztmcJComboBox = new JComboBox<Object>();

    private JLabel ntzxmLabel = new JLabel("拟调整项目");
    private JComboBox<String> ntzxmJComboBox = new JComboBox<String>();

    private JLabel ysleLabel = new JLabel("原申请类容");
    private JComboBox<String> ysqlrComboBox = new JComboBox<String>();

    private JLabel ntzlrLabel = new JLabel("拟调整类容");
    private JComboBox<String> ntzlrComboBox = new JComboBox<String>();

    public JTable table = new JTable() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private JScrollPane jScrollPane = new JScrollPane(22, 32);
    private Vector<String> headVector = new Vector<String>();
    private Vector<Vector<String>> dataVector = new Vector<Vector<String>>();
    private DefaultTableModel defaultTableModel = null;
    private DefaultTableCellRenderer defaultTableCellRenderer = null;

    JLabel historyLabel = new JLabel("历史申请");
    public JTable historyTable = new JTable() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private JScrollPane historyJScrollPane = new JScrollPane(22, 32);
    private Vector<String> historyHeadVector = new Vector<String>();
    private Vector<Vector<String>> historyDataVector = new Vector<Vector<String>>();
    DefaultTableModel historydefaultTableModel = null;
    DefaultTableCellRenderer historyDefaultTableCellRenderer = null;

    private ButtonGroup buttonGroup = new ButtonGroup();
    private JButton delete = new JButton("删除项目");
    public JRadioButton deleteRadioButton = new JRadioButton("删除");
    private JButton modify = new JButton("修改项目");
    public JRadioButton modifyRadioButton = new JRadioButton("修改");
    private JButton add = new JButton("增加项目");
    public JRadioButton addRadioButton = new JRadioButton("增加");

    private JButton apply = new JButton("申请调整");
    private Map<String, String> map = new HashMap<String, String>();

    //构造函数
    public HasApproved(String username) {
        this.setLayout(null);

        //类型字段键值对
        this.map.put("冠名类型", "GMLX");
        this.map.put("行政区划类型", "XZQHLX");
        this.map.put("名称字号", "ZH");
        this.map.put("字号拼音", "ZHPY");
        this.map.put("市场主体名称", "SCZTMC");
        this.map.put("行业名称", "HYDM");
        this.map.put("企业类型", "LXDM");
        this.map.put("企业种类", "ZLDM");
        this.map.put("住所", "ZS");
        this.map.put("组织形式", "ZZXS");
        this.map.put("联系电话", "LXDH");
        this.map.put("注册资本", "ZCZB");
        this.map.put("注册币种", "BZ");
        this.map.put("经营范围", "JYFW");
        this.map.put("经营期限止", "JYQXZ");
        this.map.put("邮政编码", "YZBM");
        this.map.put("门头招牌", "MTZP");
        this.map.put("所在建筑名称", "SZJZMC");
        this.map.put("建筑内位置", "JZLWZ");
        this.map.put("所在街道名称", "SZJDMC");
        this.map.put("门牌号", "MPH");

        this.titleLabel.setBounds(0, 0, 130, 30);
        this.titleLabel.setFont(mainFont);
        this.add(titleLabel);
        this.scztmcLabel.setBounds(180, 150, 100, 30);
        this.scztmcJComboBox.setBounds(280, 150, 200, 30);
        this.add(scztmcLabel);
        this.add(scztmcJComboBox);
        this.headVector.add("市场主体名称");
        this.headVector.add("拟调整项目");
        this.headVector.add("原申请内容");
        this.headVector.add("拟调整内容");
        this.defaultTableModel = new DefaultTableModel(this.dataVector, this.headVector);
        this.table.setModel(this.defaultTableModel);
        this.jScrollPane.getViewport().add(this.table);
        this.table.getTableHeader().setResizingAllowed(false);
        this.table.getTableHeader().setReorderingAllowed(false);
        this.defaultTableCellRenderer = new DefaultTableCellRenderer();
        this.defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        this.table.setDefaultRenderer(Object.class, this.defaultTableCellRenderer);
        this.jScrollPane.setBounds(180, 300, 700, 150);
        this.add(jScrollPane);

        this.historyLabel.setBounds(180, 450, 100, 30);
        this.add(historyLabel);

        this.historyHeadVector.add("市场主体名称");
        this.historyHeadVector.add("拟调整项目");
        this.historyHeadVector.add("原申请内容");
        this.historyHeadVector.add("拟调整内容");
        this.historyHeadVector.add("处理结果");
        this.historydefaultTableModel = new DefaultTableModel(historyDataVector, historyHeadVector);
        this.historyTable.setModel(historydefaultTableModel);
        this.historyJScrollPane.getViewport().add(historyTable);
        this.historyTable.getTableHeader().setResizingAllowed(false);
        this.historyTable.getTableHeader().setReorderingAllowed(false);
        this.historyDefaultTableCellRenderer = new DefaultTableCellRenderer();
        this.historyDefaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        this.historyTable.setDefaultRenderer(Object.class, historyDefaultTableCellRenderer);
        this.historyJScrollPane.setBounds(180, 480, 700, 100);
        this.add(historyJScrollPane);


        this.delete.setBounds(280, 620, 100, 30);
        this.deleteRadioButton.setBounds(180, 600, 60, 20);
        this.modify.setBounds(480, 620, 100, 30);
        this.modifyRadioButton.setBounds(180, 620, 60, 20);
        this.add.setBounds(680, 620, 100, 30);
        this.addRadioButton.setBounds(180, 640, 60, 20);
        this.add(delete);
        this.add(deleteRadioButton);
        this.add(modify);
        this.add(modifyRadioButton);
        this.add(add);
        this.add(addRadioButton);
        this.buttonGroup.add(addRadioButton);
        this.buttonGroup.add(modifyRadioButton);
        this.buttonGroup.add(deleteRadioButton);
        this.addRadioButton.setSelected(true);
        this.delete.setEnabled(false);
        this.modify.setEnabled(false);

        ntzxmJComboBox.setEditable(true);
        ntzxmJComboBox.getEditor().getEditorComponent().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                String s = ntzxmJComboBox.getEditor().getItem().toString();
                int key = e.getKeyChar(); //注意此处
                if (key == KeyEvent.VK_ENTER) {
                    for (int i = 0; i < ntzxmJComboBox.getItemCount(); i++) {
                        if (ntzxmJComboBox.getItemAt(i).startsWith(s)) {
                            ntzxmJComboBox.setSelectedIndex(i);
                        }
                    }
                    ntzxmJComboBox.showPopup();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }
        });

        ntzxmJComboBox.addItem("冠名类型");
        ntzxmJComboBox.addItem("行政区划类型");
        ntzxmJComboBox.addItem("名称字号");
        ntzxmJComboBox.addItem("名称拼音");
        ntzxmJComboBox.addItem("市场主体名称");
        ntzxmJComboBox.addItem("行业名称");
        ntzxmJComboBox.addItem("企业类型");
        ntzxmJComboBox.addItem("企业种类");
        ntzxmJComboBox.addItem("住所");
        ntzxmJComboBox.addItem("组织形式");
        ntzxmJComboBox.addItem("联系电话");
        ntzxmJComboBox.addItem("邮政编码");
        ntzxmJComboBox.addItem("注册资本");
        ntzxmJComboBox.addItem("注册币种");
        ntzxmJComboBox.addItem("经营范围");
        ntzxmJComboBox.addItem("经营期限止");

        this.ntzxmLabel.setBounds(180, 200, 100, 30);
        this.ntzxmJComboBox.setBounds(280, 200, 200, 30);
        this.ysleLabel.setBounds(180, 250, 100, 30);
        this.ysqlrComboBox.setBounds(280, 250, 200, 30);
        this.ntzlrLabel.setBounds(580, 250, 100, 30);
        this.ntzlrComboBox.setBounds(680, 250, 200, 30);
        this.add(ntzxmLabel);
        this.add(ntzxmJComboBox);
        this.add(ysleLabel);
        this.add(ysqlrComboBox);
        this.add(ntzlrLabel);
        this.add(ntzlrComboBox);

        this.apply.setBounds(480, 700, 100, 30);
        this.add(apply);

        //单选按钮的监听事件
        addRadioButton.addActionListener((e) ->
        {
            this.add.setEnabled(true);
            this.delete.setEnabled(false);
            this.modify.setEnabled(false);
            this.scztmcJComboBox.setSelectedIndex(0);
            this.ntzxmJComboBox.setSelectedIndex(0);
            this.ntzlrComboBox.removeAllItems();
            this.ysqlrComboBox.removeAllItems();
        });

        this.deleteRadioButton.addActionListener((e) ->
        {
            this.delete.setEnabled(true);
            this.modify.setEnabled(false);
            this.add.setEnabled(false);
            this.scztmcJComboBox.setSelectedIndex(0);
            this.ntzxmJComboBox.setSelectedIndex(0);
            this.ntzlrComboBox.removeAllItems();
            this.ysqlrComboBox.removeAllItems();
        });

        this.modifyRadioButton.addActionListener((e) ->
        {
            this.modify.setEnabled(true);
            this.delete.setEnabled(false);
            this.add.setEnabled(false);
        });

        this.ntzxmJComboBox.addActionListener((e) ->
        {
            this.change();
        });
        this.table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!modifyRadioButton.isSelected()) {
                    return;
                }
                if (e.getClickCount() == 1) {
                    int row = table.getSelectedRow();
                    for (int i = 0; i < scztmcJComboBox.getItemCount(); i++) {
                        if (scztmcJComboBox.getItemAt(i).equals(table.getValueAt(row, 0).toString())) {
                            scztmcJComboBox.setSelectedIndex(i);
                        }
                    }
                    for (int i = 0; i < ntzxmJComboBox.getItemCount(); i++) {
                        if (ntzxmJComboBox.getItemAt(i).equals(table.getValueAt(row, 1).toString())) {
                            ntzxmJComboBox.setSelectedIndex(i);
                        }
                    }
                    change();
                }
            }
        });

        //添加项目
        add.addActionListener((e) ->
        {
            if (ntzxmJComboBox.getSelectedItem().toString().isEmpty() || ntzlrComboBox.getSelectedItem().toString().isEmpty() || ysqlrComboBox.getSelectedItem().toString().isEmpty()) {
                JOptionPane.showMessageDialog(null, "资料不完整", "资料不完整", JOptionPane.OK_OPTION);
                return;
            }
            Vector<String> vector = new Vector<String>();
            vector.add(scztmcJComboBox.getSelectedItem().toString());
            vector.add(ntzxmJComboBox.getSelectedItem().toString());
            vector.add((String) ysqlrComboBox.getSelectedItem());
            vector.add((String) ntzlrComboBox.getSelectedItem());

            this.dataVector.add(vector);
            this.table.setModel(defaultTableModel);
            this.jScrollPane.getViewport().add(table);

            JOptionPane.showMessageDialog(null, "添加成功", "添加成功", JOptionPane.OK_OPTION);

            this.scztmcJComboBox.setSelectedIndex(0);
            this.ntzxmJComboBox.setSelectedIndex(0);
            this.ysqlrComboBox.removeAllItems();
            this.ntzlrComboBox.removeAllItems();
        });
        //删除项目
        this.delete.addActionListener((e) ->
        {
            int row = table.getSelectedRow();

            this.dataVector.remove(row);
            this.table.setModel(defaultTableModel);
            this.jScrollPane.getViewport().add(table);

            JOptionPane.showMessageDialog(null, "删除成功", "删除成功", JOptionPane.OK_OPTION);
        });
        //修改项目
        this.modify.addActionListener((e) ->
        {
            int row = table.getSelectedRow();
            Vector<String> vector = new Vector<String>();
            vector.add(scztmcJComboBox.getSelectedItem().toString());
            vector.add(ntzxmJComboBox.getSelectedItem().toString());
            vector.add((String) ysqlrComboBox.getSelectedItem());
            vector.add((String) ntzlrComboBox.getSelectedItem());

            this.dataVector.set(row, vector);
            this.table.setModel(defaultTableModel);
            this.jScrollPane.getViewport().add(table);

            JOptionPane.showMessageDialog(null, "修改成功", "修改成功", JOptionPane.OK_OPTION);

            this.scztmcJComboBox.setSelectedIndex(0);
            this.ysqlrComboBox.removeAllItems();
            this.ntzlrComboBox.removeAllItems();
        });

        //申请
        this.apply.addActionListener((e) ->
        {
            if (table.getRowCount() == 0) {
                return;
            }
            try {
                this.connection = JDBC.getConnection();
                String SQL;
                PreparedStatement preparedStatement = null;
                int i = 0;
                for (i = 0; i < table.getRowCount(); i++) {
                    Connection connection1 = JDBC.getConnection();
                    try {
                        String SQL1 = "select * from approved_adjustment where scztmc = ? and ntzxm = ?";
                        PreparedStatement preparedStatement1 = connection1.prepareStatement(SQL1);
                        preparedStatement1.setObject(1, table.getValueAt(i, 0));
                        preparedStatement1.setObject(2, table.getValueAt(i, 1));
                        ResultSet resultSet1 = preparedStatement1.executeQuery();
                        if (resultSet1.next()) {
                            SQL = "update approved_adjustment set YSQLR = ? ,NTZLR = ?,CLJG ='2' where scztmc = ? and ntzxm = ?";
                            preparedStatement = connection.prepareStatement(SQL);
                            preparedStatement.setObject(1, table.getValueAt(i, 2));
                            preparedStatement.setObject(2, table.getValueAt(i, 3));
                            preparedStatement.setObject(3, table.getValueAt(i, 0));
                            preparedStatement.setObject(4, table.getValueAt(i, 1));
                        } else {
                            SQL = "insert into approved_adjustment(USERNAME,SCZTMC,NTZXM,YSQLR,NTZLR,CLJG) values(?,?,?,?,?,?)";
                            preparedStatement = connection.prepareStatement(SQL);
                            preparedStatement.setObject(1, username);
                            preparedStatement.setObject(2, table.getValueAt(i, 0));
                            preparedStatement.setObject(3, table.getValueAt(i, 1));
                            preparedStatement.setObject(4, table.getValueAt(i, 2));
                            preparedStatement.setObject(5, table.getValueAt(i, 3));
                            preparedStatement.setObject(6, "2");
                        }
                        preparedStatement.executeUpdate();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    } finally {
                        JDBC.returnConnection(connection1);
                    }
                }
                if (i == table.getRowCount()) {
                    JOptionPane.showMessageDialog(null, "申请成功", "申请成功", JOptionPane.OK_OPTION);
                } else {
                    JOptionPane.showMessageDialog(null, "申请失败", "申请失败", JOptionPane.OK_OPTION);
                }
            } finally {
                JDBC.returnConnection(connection);
            }

            dataVector.clear();
            table.setModel(defaultTableModel);
        });

        //表格初始化事件
        Vector<String> WYBS = new Vector<String>();
        try {
            connection = JDBC.getConnection();
            String SQL = "select WYBS from approved_adjustment where username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                WYBS.add(resultSet.getString(1));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }

        for (String string : WYBS
        ) {
            try {
                this.connection = JDBC.getConnection();
                String SQL = "select SCZTMC,ntzxm,ysqlr,ntzlr,(select cljg.cljgname from cljg , approved_adjustment where cljg.cljgid = approved_adjustment.cljg and approved_adjustment.WYBS = ? ) from approved_adjustment  where username = ? and  WYBS = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setObject(1, string);
                preparedStatement.setObject(2, username);
                preparedStatement.setObject(3, string);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    Vector<String> vector = new Vector<String>();
                    vector.add(resultSet.getString(1));
                    vector.add(resultSet.getString(2));
                    vector.add(resultSet.getString(3));
                    vector.add(resultSet.getString(4));
                    vector.add(resultSet.getString(5));
                    this.historydefaultTableModel.addRow(vector);
                }
                this.historyTable.setModel(historydefaultTableModel);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                JDBC.returnConnection(connection);
            }
        }

        //市场主体名称初始化;
        try {
            this.connection = JDBC.getConnection();
            String SQL = "select SCZTMC from market_subject_information where username = ? and ywlx != ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, username);
            preparedStatement.setObject(2, "2");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                this.scztmcJComboBox.addItem(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }
        //第一次加载
        this.change();
    }

    //项目改变
    private void change() {
        switch (ntzxmJComboBox.getSelectedItem().toString()) {
            case "行业名称": {
                this.ntzlrComboBox.setEditable(false);
                this.ntzlrComboBox.removeAllItems();
                this.ysqlrComboBox.removeAllItems();
                this.ntzlrComboBox.addItem("农业");
                this.ntzlrComboBox.addItem("林业");
                this.ntzlrComboBox.addItem("畜牧业");
                this.ntzlrComboBox.addItem("渔业");
                this.ntzlrComboBox.addItem("水利业");
                this.ntzlrComboBox.addItem("煤炭采选业");
                this.ntzlrComboBox.addItem("石油和天然气开采业");
                this.ntzlrComboBox.addItem("黑色金属矿采选业");
                this.ntzlrComboBox.addItem("有色金属矿采选业");
                this.ntzlrComboBox.addItem("采盐业");
                this.ntzlrComboBox.addItem("食品制造业");
                this.ntzlrComboBox.addItem("饮料制造业");
                this.ntzlrComboBox.addItem("烟草加工业");
                this.ntzlrComboBox.addItem("家具制造业");
                this.ntzlrComboBox.addItem("印刷业");
                this.ntzlrComboBox.addItem("文教体育用品制造业");
                this.ntzlrComboBox.addItem("工业美术品制造业");
                this.ntzlrComboBox.addItem("石油加工业");
                this.ntzlrComboBox.addItem("化学工业");
                this.ntzlrComboBox.addItem("医学工业");
                this.ntzlrComboBox.addItem("机械工业");
                this.ntzlrComboBox.addItem("交通运输设备制造业");
                this.ntzlrComboBox.addItem("电子机械及器材制造业");
                this.ntzlrComboBox.addItem("商业");
                this.ntzlrComboBox.addItem("公共饮食业");
                this.ntzlrComboBox.addItem("房地产管理业");
                this.ntzlrComboBox.addItem("公共事业");

                //查询原始类容
                try {
                    connection = JDBC.getConnection();
                    String SQL = "SELECT hydm.hydmNAME FROM market_subject_information , hydm WHERE market_subject_information.hydm = hydm.hyDMID AND market_subject_information.SCZTMC = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setObject(1, scztmcJComboBox.getSelectedItem());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        ysqlrComboBox.addItem(resultSet.getString(1));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    JDBC.returnConnection(connection);
                }
                break;
            }
            case "企业类型": {
                this.ntzlrComboBox.setEditable(false);
                this.ntzlrComboBox.removeAllItems();
                this.ysqlrComboBox.removeAllItems();
                this.ntzlrComboBox.addItem("内资企业");
                this.ntzlrComboBox.addItem("国有企业");
                this.ntzlrComboBox.addItem("集体企业");
                this.ntzlrComboBox.addItem("股份合作企业");
                this.ntzlrComboBox.addItem("联营企业");
                this.ntzlrComboBox.addItem("有限责任公司");
                this.ntzlrComboBox.addItem("股份有限公司");
                this.ntzlrComboBox.addItem("私营企业");
                this.ntzlrComboBox.addItem("港澳台投资企业");
                this.ntzlrComboBox.addItem("中外合资");
                this.ntzlrComboBox.addItem("中外合作");
                this.ntzlrComboBox.addItem("外资企业");
                this.ntzlrComboBox.addItem("外资投资股份有限公司");
                this.ntzlrComboBox.addItem("外国企业");
                this.ntzlrComboBox.addItem("个体经营");
                this.ntzlrComboBox.addItem("个体工商户");
                this.ntzlrComboBox.addItem("个人合伙");
                this.ntzlrComboBox.addItem("非企业单位");
                this.ntzlrComboBox.addItem("事业单位");
                this.ntzlrComboBox.addItem("民办非企业单位");
                this.ntzlrComboBox.addItem("国家机关");
                this.ntzlrComboBox.addItem("政党机关");
                this.ntzlrComboBox.addItem("社会团体");
                this.ntzlrComboBox.addItem("基层群众自治组织");

                //查询原始类容
                try {
                    connection = JDBC.getConnection();
                    String SQL = "SELECT lxdm.LXDMNAME FROM market_subject_information , lxdm WHERE market_subject_information.LXDM = lxdm.LXDMID AND market_subject_information.SCZTMC = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setObject(1, scztmcJComboBox.getSelectedItem());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        ysqlrComboBox.addItem(resultSet.getString(1));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    JDBC.returnConnection(connection);
                }
                break;
            }
            case "企业种类": {
                this.ntzlrComboBox.setEditable(false);
                this.ntzlrComboBox.removeAllItems();
                this.ysqlrComboBox.removeAllItems();
                this.ntzlrComboBox.addItem("服务型企业");
                this.ntzlrComboBox.addItem("生产型企业");
                this.ntzlrComboBox.addItem("科技型企业");
                this.ntzlrComboBox.addItem("贸易型企业");
                this.ntzlrComboBox.addItem("工程型企业");
                try {
                    connection = JDBC.getConnection();
                    String SQL = "SELECT zldm.zlDMNAME FROM market_subject_information , zldm WHERE market_subject_information.zlDM = zldm.zlDMID AND market_subject_information.SCZTMC = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setObject(1, scztmcJComboBox.getSelectedItem());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        ysqlrComboBox.addItem(resultSet.getString(1));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    JDBC.returnConnection(connection);
                }
                break;
            }
            case "组织形式": {
                this.ntzlrComboBox.setEditable(false);
                this.ntzlrComboBox.removeAllItems();
                this.ysqlrComboBox.removeAllItems();
                this.ntzlrComboBox.addItem("个人独资企业");
                this.ntzlrComboBox.addItem("合伙企业");
                this.ntzlrComboBox.addItem("公司制企业");
                try {
                    connection = JDBC.getConnection();
                    String SQL = "SELECT zzxs.zzxsNAME FROM market_subject_information , zzxs WHERE market_subject_information.zlDM = zzxs.zzxsID AND market_subject_information.SCZTMC = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setObject(1, scztmcJComboBox.getSelectedItem());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        ysqlrComboBox.addItem(resultSet.getString(1));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    JDBC.returnConnection(connection);
                }
                break;
            }
            case "注册币种": {
                this.ntzlrComboBox.setEditable(false);
                this.ntzlrComboBox.removeAllItems();
                this.ysqlrComboBox.removeAllItems();
                this.ntzlrComboBox.addItem("美元");
                this.ntzlrComboBox.addItem("人民币");
                this.ntzlrComboBox.addItem("欧元");
                this.ntzlrComboBox.addItem("澳元");
                this.ntzlrComboBox.addItem("港币");

                try {
                    connection = JDBC.getConnection();
                    String SQL = "SELECT tzbz.tzbzNAME FROM market_subject_information , tzbz WHERE market_subject_information.tzbz = tzbz.tzbzID AND market_subject_information.SCZTMC = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setObject(1, scztmcJComboBox.getSelectedItem());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        ysqlrComboBox.addItem(resultSet.getString(1));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    JDBC.returnConnection(connection);
                }
                break;
            }
            default: {
                this.ntzlrComboBox.setEditable(true);
                this.ntzlrComboBox.removeAllItems();
                this.ysqlrComboBox.removeAllItems();
                try {
                    connection = JDBC.getConnection();
                    String SQL = "SELECT " + map.get(ntzxmJComboBox.getSelectedItem()) + " from market_subject_information WHERE SCZTMC = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setObject(1, scztmcJComboBox.getSelectedItem());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        ysqlrComboBox.addItem(resultSet.getString(1));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    JDBC.returnConnection(connection);
                }
                break;
            }
        }
    }
}
