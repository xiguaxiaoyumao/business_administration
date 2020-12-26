package com.admin;

import com.jdbc.JDBC;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class RegisterChangeRegistration extends JPanel {
    private Connection connection = null;

    private Font mainFont = new Font("宋体", Font.BOLD, 20);
    //基本控件
    private JLabel titleLabel = new JLabel("变更登记");
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
    private DefaultTableModel defaultTableModel;

    private JLabel ntzxmLabel = new JLabel("拟调整项目");
    private JComboBox<String> ntzxmJComboBox = new JComboBox<String>();

    private JLabel ysleLabel = new JLabel("原申请类容");
    private JComboBox<String> ysqlrComboBox = new JComboBox<String>();

    private JLabel ntzlrLabel = new JLabel("拟调整类容");
    private JComboBox<String> ntzlrComboBox = new JComboBox<String>();


    private JButton adjustment = new JButton("确认调整");

    private Map<String, String> map = new HashMap<String, String>();
    private Map<String, String> lxmap = new HashMap<String, String>();
    private Map<String, String> hymap = new HashMap<String, String>();
    private Map<String, String> zlmap = new HashMap<String, String>();
    private Map<String, String> zzxsmap = new HashMap<String, String>();
    private Map<String, String> bzmap = new HashMap<String, String>();

    //构造函数
    public RegisterChangeRegistration() {
        this.setLayout(null);
        //初始化键值对函数
        this.mapInit();

        this.titleLabel.setSize(130, 30);
        this.titleLabel.setFont(mainFont);
        this.queryField.setBounds(280, 130, 200, 30);
        this.selectButton.setBounds(580, 130, 100, 30);
        this.remindLabel.setBounds(280, 160, 200, 20);

        this.add(titleLabel);
        this.add(queryField);
        this.add(selectButton);
        this.add(remindLabel);

        this.ntzxmJComboBox.addItem("冠名类型");
        this.ntzxmJComboBox.addItem("行政区划类型");
        this.ntzxmJComboBox.addItem("名称字号");
        this.ntzxmJComboBox.addItem("名称拼音");
        this.ntzxmJComboBox.addItem("市场主体名称");
        this.ntzxmJComboBox.addItem("行业名称");
        this.ntzxmJComboBox.addItem("企业类型");
        this.ntzxmJComboBox.addItem("企业种类");
        this.ntzxmJComboBox.addItem("住所");
        this.ntzxmJComboBox.addItem("组织形式");
        this.ntzxmJComboBox.addItem("联系电话");
        this.ntzxmJComboBox.addItem("注册资本");
        this.ntzxmJComboBox.addItem("注册币种");
        this.ntzxmJComboBox.addItem("经营范围");
        this.ntzxmJComboBox.addItem("经营期限止");
        this.ntzxmJComboBox.addItem("邮政编码");
        this.ntzxmJComboBox.addItem("门头招牌");
        this.ntzxmJComboBox.addItem("所在建筑名称");
        this.ntzxmJComboBox.addItem("建筑内位置");
        this.ntzxmJComboBox.addItem("所在街道名称");
        this.ntzxmJComboBox.addItem("门牌号");

        this.ntzxmLabel.setBounds(180, 300, 100, 30);
        this.ntzxmJComboBox.setBounds(280, 300, 200, 30);
        this.ysleLabel.setBounds(180, 500, 100, 30);
        this.ysqlrComboBox.setBounds(280, 500, 200, 30);
        this.ntzlrLabel.setBounds(580, 500, 100, 30);
        this.ntzlrComboBox.setBounds(680, 500, 200, 30);

        this.add(ntzxmLabel);
        this.add(ntzxmJComboBox);
        this.add(ysleLabel);
        this.add(ysqlrComboBox);
        this.add(ntzlrLabel);
        this.add(ntzlrComboBox);

        this.adjustment.setBounds(480, 700, 100, 30);
        this.add(adjustment);

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
        this.add(jScrollPane);

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
        this.selectButton.addActionListener((e) ->
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

        //确认调整
        this.adjustment.addActionListener((e) ->
        {
            adjustment();
        });

        //初始状态
        this.ntzlrComboBox.setEditable(true);
        this.ntzxmJComboBox.addActionListener((e) ->
        {
            change();
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

    //调整函数
    private void adjustment() {
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


        //如果调整成功就在变更登记表记录一条
        try {
            if (value != 1) {
                return;
            }
            this.connection = JDBC.getConnection();
            String SQL = "insert into change_the_situation(BGSX,BGQNR,BGHNR,BGRQ,SCZTMC)values(?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, this.ntzxmJComboBox.getSelectedItem());
            preparedStatement.setObject(2, this.ysqlrComboBox.getSelectedItem());
            preparedStatement.setObject(3, this.ntzlrComboBox.getSelectedItem());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            java.util.Date date = new java.util.Date();
            preparedStatement.setObject(4, simpleDateFormat.format(date));
            preparedStatement.setObject(5, this.table.getValueAt(row, 0));
            int i = preparedStatement.executeUpdate();
            if (i != 1) {
                JOptionPane.showMessageDialog(null, "错误");
                return;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }

    }

    //键值对初始化
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
                int row = table.getSelectedRow();
                if (row == -1) {
                    return;
                }
                try {
                    connection = JDBC.getConnection();
                    String SQL = "SELECT hydm.hydmNAME FROM market_subject_information , hydm WHERE market_subject_information.hydm = hydm.hyDMID AND market_subject_information.SCZTMC = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setObject(1, table.getValueAt(row, 0));
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
                int row = table.getSelectedRow();
                if (row == -1) {
                    return;
                }
                try {
                    connection = JDBC.getConnection();
                    String SQL = "SELECT lxdm.LXDMNAME FROM market_subject_information , lxdm WHERE market_subject_information.LXDM = lxdm.LXDMID AND market_subject_information.SCZTMC = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setObject(1, table.getValueAt(row, 0));
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

                int row = table.getSelectedRow();
                if (row == -1) {
                    return;
                }
                try {
                    connection = JDBC.getConnection();
                    String SQL = "SELECT zldm.zlDMNAME FROM market_subject_information , zldm WHERE market_subject_information.zlDM = zldm.zlDMID AND market_subject_information.SCZTMC = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setObject(1, table.getValueAt(row, 0));
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

                int row = table.getSelectedRow();
                if (row == -1) {
                    return;
                }
                try {
                    connection = JDBC.getConnection();
                    String SQL = "SELECT zzxs.zzxsNAME FROM market_subject_information , zzxs WHERE market_subject_information.zlDM = zzxs.zzxsID AND market_subject_information.SCZTMC = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setObject(1, table.getValueAt(row, 0));
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

                int row = table.getSelectedRow();
                if (row == -1) {
                    return;
                }
                try {
                    connection = JDBC.getConnection();
                    String SQL = "SELECT tzbz.tzbzNAME FROM market_subject_information , tzbz WHERE market_subject_information.tzbz = tzbz.tzbzID AND market_subject_information.SCZTMC = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setObject(1, table.getValueAt(row, 0));
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

                int row = table.getSelectedRow();
                if (row == -1) {
                    return;
                }
                try {
                    connection = JDBC.getConnection();
                    String SQL = "SELECT " + map.get(ntzxmJComboBox.getSelectedItem()) + " from market_subject_information WHERE SCZTMC = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setObject(1, table.getValueAt(row, 0));
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
