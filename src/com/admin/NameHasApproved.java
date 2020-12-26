package com.admin;

import com.jdbc.JDBC;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class NameHasApproved extends JPanel {
    private Connection connection = null;

    private Font mainFont = new Font("宋体", Font.BOLD, 20);

    private JLabel titleLabel = new JLabel("已核调整");
    private JLabel scztmcLabel = new JLabel("已核准名称");
    private JTextField scztmcField = new JTextField();

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

    private JButton adjustment = new JButton("确认调整");

    private JButton noAdjustment = new JButton("不予调整");

    private Map<String, String> map = new HashMap<String, String>();
    private Map<String, String> lxmap = new HashMap<String, String>();
    private Map<String, String> hymap = new HashMap<String, String>();
    private Map<String, String> zlmap = new HashMap<String, String>();
    private Map<String, String> zzxsmap = new HashMap<String, String>();
    private Map<String, String> bzmap = new HashMap<String, String>();

    public NameHasApproved() {
        this.setLayout(null);
        this.mapInit();

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
        ntzxmJComboBox.addItem("注册资本");
        ntzxmJComboBox.addItem("注册币种");
        ntzxmJComboBox.addItem("经营范围");
        ntzxmJComboBox.addItem("经营期限止");
        ntzxmJComboBox.addItem("邮政编码");

        this.setSize(1060, 800);

        this.titleLabel.setBounds(0, 0, 130, 30);
        this.titleLabel.setFont(mainFont);
        this.add(titleLabel);
        this.titleLabel.setBounds(0, 0, 130, 30);
        this.titleLabel.setFont(mainFont);
        this.add(titleLabel);

        this.scztmcLabel.setBounds(180, 150, 100, 30);
        this.scztmcField.setBounds(280, 150, 200, 30);
        this.add(scztmcLabel);
        this.add(scztmcField);

        this.headVector.add("市场主体名称");
        this.headVector.add("拟调整项目");
        this.headVector.add("原申请内容");
        this.headVector.add("拟调整内容");
        this.headVector.add("处理状态");

        DefaultTableModel defaultTableModel = new DefaultTableModel(dataVector, headVector);
        this.table.setModel(defaultTableModel);
        this.jScrollPane.getViewport().add(table);
        this.table.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        this.table.setDefaultRenderer(Object.class, defaultTableCellRenderer);
        this.jScrollPane.setBounds(180, 350, 700, 300);
        this.add(jScrollPane);

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

        this.adjustment.setBounds(350, 700, 100, 30);
        this.noAdjustment.setBounds(550, 700, 100, 30);
        this.add(adjustment);
        this.add(noAdjustment);

        //表格初始化事件
        Vector<String> WYBS = new Vector<String>();
        try {
            connection = JDBC.getConnection();
            String SQL = "select WYBS from approved_adjustment where CLJG = '2'";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                WYBS.add(resultSet.getString(1));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }
        for (String string : WYBS) {
            try {
                connection = JDBC.getConnection();
                String SQL = "select SCZTMC,ntzxm,ysqlr,ntzlr,(select cljg.cljgname from cljg , approved_adjustment where cljg.cljgid = approved_adjustment.cljg and approved_adjustment.WYBS = ? ) from approved_adjustment  where WYBS = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setObject(1, string);
                preparedStatement.setObject(2, string);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    Vector<String> vector = new Vector<String>();
                    vector.add(resultSet.getString(1));
                    vector.add(resultSet.getString(2));
                    vector.add(resultSet.getString(3));
                    vector.add(resultSet.getString(4));
                    vector.add(resultSet.getString(5));
                    defaultTableModel.addRow(vector);
                }
                this.table.setModel(defaultTableModel);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                JDBC.returnConnection(connection);
            }
        }

        //调整的监听事件
        this.adjustment.addActionListener((e) ->
        {
            try {
                connection = JDBC.getConnection();
                String SQL = "update approved_adjustment set CLJG = '1' where scztmc = ? and ntzxm = ? and CLJG = '2'";
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setObject(1, this.scztmcField.getText());
                preparedStatement.setObject(2, this.ntzxmJComboBox.getSelectedItem().toString());
                int i = preparedStatement.executeUpdate();
                if (i == 1) {
                    table.setValueAt("已处理", table.getSelectedRow(), 4);
                    System.out.println("approved_adjustment");
                } else {
                    System.out.println("approved_adjustment  no");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                JDBC.returnConnection(connection);
            }
            this.adjust();
        });


        //不予调整
        noAdjustment.addActionListener((e) ->
        {
            try {
                JDBC jdbc = new JDBC();
                connection = jdbc.getConnection();
                String SQL = "update approved_adjustment set CLJG = '3' where  scztmc = ? and ntzxm = ? and CLJG = '2'";
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setObject(1, this.scztmcField.getText());
                preparedStatement.setObject(2, this.ntzxmJComboBox.getSelectedItem().toString());
                int i = preparedStatement.executeUpdate();
                if (i == 1) {
                    dataVector.remove(table.getSelectedRow());
                    this.table.setModel(defaultTableModel);
                    JOptionPane.showMessageDialog(null, "设置成功");
                    table.setValueAt("不予处理", table.getSelectedRow(), 4);
                } else {
                    JOptionPane.showMessageDialog(null, "设置成功");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        //表格的 监听事件；
        this.table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int row = table.getSelectedRow();
                    scztmcField.setText((String) table.getValueAt(row, 0));
                    for (int i = 0; i < ntzxmJComboBox.getItemCount(); i++) {
                        if (ntzxmJComboBox.getItemAt(i).equals(table.getValueAt(row, 1))) {
                            ntzxmJComboBox.setSelectedIndex(i);
                        }
                    }
                }
            }
        });

        this.ntzxmJComboBox.addActionListener((e) ->
        {
            change();
        });
    }

    //项目改变
    private void change() {
        switch (ntzxmJComboBox.getSelectedItem().toString()) {
            case "行业名称": {
                this.ntzlrComboBox.removeAllItems();
                this.ysqlrComboBox.removeAllItems();
                int row = table.getSelectedRow();
                this.ntzlrComboBox.addItem((String) table.getValueAt(row, 3));
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
                //添加原始类容
                this.ysqlrComboBox.addItem((String) table.getValueAt(row, 2));
                break;
            }
            case "企业类型": {
                int row = table.getSelectedRow();
                this.ntzlrComboBox.removeAllItems();
                this.ysqlrComboBox.removeAllItems();
                this.ntzlrComboBox.addItem((String) table.getValueAt(row, 3));
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

                //添加原始类容
                this.ysqlrComboBox.addItem((String) table.getValueAt(row, 2));
                break;
            }
            case "企业种类": {
                int row = table.getSelectedRow();
                this.ntzlrComboBox.removeAllItems();
                this.ysqlrComboBox.removeAllItems();
                this.ntzlrComboBox.addItem((String) table.getValueAt(row, 3));
                this.ntzlrComboBox.addItem("服务型企业");
                this.ntzlrComboBox.addItem("生产型企业");
                this.ntzlrComboBox.addItem("科技型企业");
                this.ntzlrComboBox.addItem("贸易型企业");
                this.ntzlrComboBox.addItem("工程型企业");

                //添加原始类容
                this.ysqlrComboBox.addItem((String) table.getValueAt(row, 2));
                break;
            }
            case "组织形式": {
                int row = table.getSelectedRow();
                this.ntzlrComboBox.removeAllItems();
                this.ysqlrComboBox.removeAllItems();
                this.ntzlrComboBox.addItem((String) table.getValueAt(row, 3));
                this.ntzlrComboBox.addItem("个人独资企业");
                this.ntzlrComboBox.addItem("合伙企业");
                this.ntzlrComboBox.addItem("公司制企业");

                //添加原始类容
                this.ysqlrComboBox.addItem((String) table.getValueAt(row, 2));
                break;
            }
            case "注册币种": {
                int row = table.getSelectedRow();
                this.ntzlrComboBox.removeAllItems();
                this.ysqlrComboBox.removeAllItems();
                this.ntzlrComboBox.addItem((String) table.getValueAt(row, 3));
                this.ntzlrComboBox.addItem("美元");
                this.ntzlrComboBox.addItem("人民币");
                this.ntzlrComboBox.addItem("欧元");
                this.ntzlrComboBox.addItem("澳元");
                this.ntzlrComboBox.addItem("港币");

                //添加原始类容
                this.ysqlrComboBox.addItem((String) table.getValueAt(row, 2));
                break;
            }
            default: {
                int row = table.getSelectedRow();
                this.ntzlrComboBox.removeAllItems();
                this.ysqlrComboBox.removeAllItems();
                this.ntzlrComboBox.addItem((String) table.getValueAt(row, 3));

                //添加原始类容
                this.ysqlrComboBox.addItem((String) table.getValueAt(row, 2));
                break;
            }
        }
    }

    //调整
    private void adjust() {
        int value = 0;
        int row = this.table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "请先选中需要调整企业");
            return;
        }
        if (ntzlrComboBox.getSelectedItem().toString().isEmpty() || ysqlrComboBox.getSelectedItem().toString().isEmpty()) {
            JOptionPane.showMessageDialog(null, "类容不能为空");
            return;
        }
        switch (ntzxmJComboBox.getSelectedItem().toString()) {
            case "行业名称": {
                try {
                    this.connection = JDBC.getConnection();
                    String SQL = "update market_subject_information set " + map.get(this.ntzxmJComboBox.getSelectedItem()) + " = ? where scztmc = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setObject(1, hymap.get(ntzlrComboBox.getSelectedItem()));
                    preparedStatement.setObject(2, table.getValueAt(row, 0));
                    value = preparedStatement.executeUpdate();
                    if (value == 1) {
                        JOptionPane.showMessageDialog(null, "调整成功");
                    } else {
                        JOptionPane.showMessageDialog(null, "调整失败");
                        return;
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    JDBC.returnConnection(connection);
                }
                break;
            }
            case "企业类型": {
                try {
                    this.connection = JDBC.getConnection();
                    String SQL = "update market_subject_information set " + map.get(this.ntzxmJComboBox.getSelectedItem()) + " = ? where scztmc = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setObject(1, lxmap.get(ntzlrComboBox.getSelectedItem()));
                    preparedStatement.setObject(2, table.getValueAt(row, 0));
                    value = preparedStatement.executeUpdate();
                    if (value == 1) {
                        JOptionPane.showMessageDialog(null, "调整成功");
                    } else {
                        JOptionPane.showMessageDialog(null, "调整失败");
                        return;
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    JDBC.returnConnection(connection);
                }
                break;
            }
            case "企业种类": {
                try {
                    this.connection = JDBC.getConnection();
                    String SQL = "update market_subject_information set " + map.get(this.ntzxmJComboBox.getSelectedItem()) + " = ? where scztmc = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setObject(1, zlmap.get(ntzlrComboBox.getSelectedItem()));
                    preparedStatement.setObject(2, table.getValueAt(row, 0));
                    value = preparedStatement.executeUpdate();
                    if (value == 1) {
                        JOptionPane.showMessageDialog(null, "调整成功");
                    } else {
                        JOptionPane.showMessageDialog(null, "调整失败");
                        return;
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    JDBC.returnConnection(connection);
                }
                break;
            }
            case "组织形式": {
                try {
                    this.connection = JDBC.getConnection();
                    String SQL = "update market_subject_information set " + map.get(this.ntzxmJComboBox.getSelectedItem()) + " = ? where scztmc = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setObject(1, zzxsmap.get(ntzlrComboBox.getSelectedItem()));
                    preparedStatement.setObject(2, table.getValueAt(row, 0));
                    value = preparedStatement.executeUpdate();
                    if (value == 1) {
                        JOptionPane.showMessageDialog(null, "调整成功");
                    } else {
                        JOptionPane.showMessageDialog(null, "调整失败");
                        return;
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    JDBC.returnConnection(connection);
                }
                break;
            }
            case "注册币种": {
                try {
                    this.connection = JDBC.getConnection();
                    String SQL = "update market_subject_information set " + map.get(this.ntzxmJComboBox.getSelectedItem()) + " = ? where scztmc = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setObject(1, bzmap.get(ntzlrComboBox.getSelectedItem()));
                    preparedStatement.setObject(2, table.getValueAt(row, 0));
                    value = preparedStatement.executeUpdate();
                    if (value == 1) {
                        JOptionPane.showMessageDialog(null, "调整成功");
                    } else {
                        JOptionPane.showMessageDialog(null, "调整失败");
                        return;
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    JDBC.returnConnection(connection);
                }
                break;
            }
            default: {
                try {
                    this.connection = JDBC.getConnection();
                    String SQL = "update market_subject_information set " + map.get(this.ntzxmJComboBox.getSelectedItem()) + " = ? where scztmc = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setObject(1, ntzlrComboBox.getSelectedItem());
                    preparedStatement.setObject(2, table.getValueAt(row, 0));
                    value = preparedStatement.executeUpdate();
                    if (value == 1) {
                        JOptionPane.showMessageDialog(null, "调整成功");
                    } else {
                        JOptionPane.showMessageDialog(null, "调整失败");
                        return;
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    JDBC.returnConnection(connection);
                }
                break;
            }
        }
    }

    private void mapInit() {
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

        //行业名称键值对
        this.lxmap.put("内资企业", "100");
        this.lxmap.put("国有企业", "110");
        this.lxmap.put("集体企业", "120");
        this.lxmap.put("股份合作企业", "130");
        this.lxmap.put("联营企业", "140");
        this.lxmap.put("有限责任公司", "150");
        this.lxmap.put("股份有限公司", "160");
        this.lxmap.put("私营企业", "170");
        this.lxmap.put("港澳台投资企业", "200");
        this.lxmap.put("中外合资", "310");
        this.lxmap.put("中外合作", "320");
        this.lxmap.put("外资企业", "330");
        this.lxmap.put("外资投资股份有限公司", "340");
        this.lxmap.put("外国企业", "350");
        this.lxmap.put("个体经营", "400");
        this.lxmap.put("个体工商户", "410");
        this.lxmap.put("个人合伙", "420");
        this.lxmap.put("非企业单位", "500");
        this.lxmap.put("事业单位", "510");
        this.lxmap.put("民办非企业单位", "520");
        this.lxmap.put("国家机关", "530");
        this.lxmap.put("政党机关", "540");
        this.lxmap.put("社会团体", "550");
        this.lxmap.put("基层群众自治组织", "560");

        //行业名称键值对
        this.hymap.put("农业", "01");
        this.hymap.put("林业", "03");
        this.hymap.put("畜牧业", "04");
        this.hymap.put("渔业", "05");
        this.hymap.put("水利业", "06");
        this.hymap.put("煤炭采选业", "08");
        this.hymap.put("石油和天然气开采业", "09");
        this.hymap.put("黑色金属矿采选业", "10");
        this.hymap.put("有色金属矿采选业", "11");
        this.hymap.put("采盐业", "13");
        this.hymap.put("食品制造业", "17");
        this.hymap.put("饮料制造业", "18");
        this.hymap.put("烟草加工业", "19");
        this.hymap.put("家具制造业", "27");
        this.hymap.put("印刷业", "29");
        this.hymap.put("文教体育用品制造业", "30");
        this.hymap.put("工业美术品制造业", "31");
        this.hymap.put("石油加工业", "34");
        this.hymap.put("化学工业", "36");
        this.hymap.put("医学工业", "37");
        this.hymap.put("机械工业", "53");
        this.hymap.put("交通运输设备制造业", "56");
        this.hymap.put("电子机械及器材制造业", "58");
        this.hymap.put("商业", "75");
        this.hymap.put("公共饮食业", "76");
        this.hymap.put("房地产管理业", "80");
        this.hymap.put("公共事业", "81");

        //种类键值对
        this.zlmap.put("服务型企业", "1");
        this.zlmap.put("生产型企业", "2");
        this.zlmap.put("科技型企业", "3");
        this.zlmap.put("贸易型企业", "4");
        this.zlmap.put("工程型企业", "5");

        //组织形式键值对
        this.zzxsmap.put("个人独资企业", "1");
        this.zzxsmap.put("合伙企业", "2");
        this.zzxsmap.put("公司制企业", "3");

        //币种
        this.bzmap.put("美元", "1");
        this.bzmap.put("人民币", "2");
        this.bzmap.put("欧元", "3");
        this.bzmap.put("澳元", "4");
        this.bzmap.put("港币", "5");
    }
}
