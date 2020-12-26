package com.admin;

import com.combobox.*;
import com.jdbc.JDBC;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.DateFormat;
import java.util.Vector;

public class DataChange extends JPanel {
    //连接对象
    private Connection connection = null;

    //字体和标题
    private Font mainFont = new Font("宋体", Font.BOLD, 20);
    private JLabel titleLabel = new JLabel("业务数据修改");

    //ComboBox
    private JComboBox<Xzqhlx> xzqhlxComboBox = new JComboBox<Xzqhlx>();
    private JComboBox<Hymc> hymcJComboBox = new JComboBox<Hymc>();
    private JComboBox<Qylx> qylxComboBox = new JComboBox<Qylx>();
    private JComboBox<Qyzl> qyzlComboBox = new JComboBox<Qyzl>();
    private JComboBox<Zzxs> zzxsComboBox = new JComboBox<Zzxs>();
    private JComboBox<Bz> zczbbzComboBox = new JComboBox<Bz>();
    private JComboBox<Djjg> djjgComboBox = new JComboBox<Djjg>();
    private JComboBox<Ywlx> ywlxComboBox = new JComboBox<Ywlx>();
    private JComboBox<Lcztdm> lcztdmComboBox = new JComboBox<Lcztdm>();
    private JComboBox<Jgztdm> jgztdmComboBox = new JComboBox<Jgztdm>();
    private JComboBox<Zdhy> zdhyflComboBox = new JComboBox<Zdhy>();


    //查询控件
    private JButton selectButton = new JButton("查询");
    private JTextField queryField = new JTextField();
    private JLabel remindLabel = new JLabel("请输入企业名称");

    //查询表
    private JTable table = new JTable() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private JScrollPane jScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    private Vector<String> headVector = new Vector<String>();
    private Vector<Vector<String>> dataVector = new Vector<Vector<String>>();
    private DefaultTableModel defaultTableModel;
    private DefaultTableCellRenderer defaultTableCellRenderer;
    //数据表
    public JTable changeDataTable = new JTable() {
        @Override
        public boolean isCellEditable(int row, int column) {
            if (column != 2 || row == 0) {
                return false;
            }
            return true;
        }

        @Override
        public TableCellEditor getCellEditor(int row, int column) {
            //行政区划类型
            if (row == 3 && column == 2) {
                return new DefaultCellEditor(xzqhlxComboBox);
            }
            //行业名称
            if (row == 9 && column == 2) {
                return new DefaultCellEditor(hymcJComboBox);
            }
            //类型代码
            if (row == 10 && column == 2) {
                return new DefaultCellEditor(qylxComboBox);
            }
            //种类代码
            if (row == 11 && column == 2) {
                return new DefaultCellEditor(qyzlComboBox);
            }
            //组织形式
            if (row == 14 && column == 2) {
                return new DefaultCellEditor(zzxsComboBox);
            }
            //币种
            if (row == 17 && column == 2) {
                return new DefaultCellEditor(zczbbzComboBox);
            }
            //登记机关
            if (row == 23 && column == 2) {
                return new DefaultCellEditor(djjgComboBox);
            }
            if (row == 25 && column == 2) {
                return new DefaultCellEditor(ywlxComboBox);
            }
            if (row == 26 && column == 2) {
                return new DefaultCellEditor(lcztdmComboBox);
            }
            if (row == 27 && column == 2) {
                return new DefaultCellEditor(jgztdmComboBox);
            }
            if (row == 28 && column == 2) {
                return new DefaultCellEditor(zdhyflComboBox);
            }
            return super.getCellEditor(row, column);
        }
    };
    private JScrollPane changeDataJScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    private Vector<String> changeDataHeadVector = new Vector<String>();
    private Vector<Vector<Object>> changeDataVector = new Vector<Vector<Object>>();
    private DefaultTableModel changeDatadefaultTableModel;
    private String[] projectType = {"唯一标识", "补充标识", "冠名类型", "行政区划类型", "字号", "备选字号1", "备选字号2", "字号拼音", "市场主体名称", "行业代码", "类型代码", "种类代码", "注册号", "住所", "组织形式", "联系电话", "注册资本", "投资币种", "经营范围", "处理日期", "核准日期", "经营期限起", "经营期限止", "登记机关代码", "邮政编码", "业务类型", "流程状态代码", "监管状态代码", "重点行业分类", "门头招牌", "所在建筑名称", "建筑内位置", "所在街道名称", "门牌号", "备用", "备用", "备用", "登记用户名"};
    private JButton change = new JButton("确认修改");

    public DataChange() {
        this.setLayout(null);
        //初始化comboBox
        this.comboBoxInit();

        this.titleLabel.setBounds(0, 0, 130, 30);
        this.titleLabel.setFont(mainFont);
        this.add(titleLabel);

        this.queryField.setBounds(250, 160, 200, 30);
        this.selectButton.setBounds(600, 160, 150, 30);
        this.remindLabel.setBounds(250, 190, 100, 30);

        this.add(titleLabel);
        this.add(queryField);
        this.add(selectButton);
        this.add(remindLabel);

        //表头添加数据
        this.headVector.add("名称");
        this.headVector.add("注册号");
        this.headVector.add("类型");
        this.headVector.add("法定代表人");
        this.headVector.add("注册资本");
        this.headVector.add("成立日期");
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
        this.table.getTableHeader().setReorderingAllowed(false);
        this.defaultTableCellRenderer = new DefaultTableCellRenderer();
        this.defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        this.table.setDefaultRenderer(Object.class, defaultTableCellRenderer);
        this.jScrollPane.setBounds(10, 50, 1060, 100);
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

        //表头添加数据
        this.changeDataHeadVector.add("项目");
        this.changeDataHeadVector.add("原项目类容");
        this.changeDataHeadVector.add("新项目类容");

        //第二个表模型
        this.changeDatadefaultTableModel = new DefaultTableModel(changeDataVector, changeDataHeadVector);
        this.changeDataTable.setModel(changeDatadefaultTableModel);
        this.changeDataJScrollPane.getViewport().add(changeDataTable);
        this.changeDataTable.setRowHeight(30);
        this.changeDataTable.getTableHeader().setReorderingAllowed(false);
        this.changeDataTable.getTableHeader().setResizingAllowed(false);
        this.changeDataJScrollPane.setBounds(10, 230, 1060, 450);
        this.add(changeDataJScrollPane);
        this.changeDataTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        this.changeDataTable.getColumnModel().getColumn(1).setPreferredWidth(450);
        this.changeDataTable.getColumnModel().getColumn(2).setPreferredWidth(450);

        this.change.setBounds(450, 720, 100, 30);
        this.add(change);

        //查询
        selectButton.addActionListener((e) ->
        {
            this.select();
        });

        //查询表格点击的监听事件
        this.table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    tableClick();
                }
            }
        });

        //确认修改的
        this.change.addActionListener((e) ->
        {
            this.change();
        });
    }

    //comboBox 和 键值对 初始化函数
    private void comboBoxInit() {
        //行政区划类型
        try {
            connection = JDBC.getConnection();
            String SQL = "select * from xzqhlx";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Xzqhlx xzqhlx = new Xzqhlx();
                xzqhlx.setXzqhlxid(resultSet.getString(1));
                xzqhlx.setXzqhlxname(resultSet.getString(2));
                xzqhlxComboBox.addItem(xzqhlx);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }

        //行业类型ComboBox
        try {
            connection = JDBC.getConnection();
            String SQL = "select * from hydm";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Hymc hymc = new Hymc();
                hymc.setHymcid(resultSet.getString(1));
                hymc.setHymcname(resultSet.getString(2));
                hymcJComboBox.addItem(hymc);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }

        //企业类型ComboBox
        try {
            connection = JDBC.getConnection();
            String SQL = "select * from lxdm";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Qylx qylx = new Qylx();
                qylx.setQylxid(resultSet.getString(1));
                qylx.setQylxname(resultSet.getString(2));
                qylxComboBox.addItem(qylx);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }

        //企业种类ComboBox
        try {
            connection = JDBC.getConnection();
            String SQL = "select * from zldm";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Qyzl qyzl = new Qyzl();
                qyzl.setQyzlid(resultSet.getString(1));
                qyzl.setQyzlname(resultSet.getString(2));
                qyzlComboBox.addItem(qyzl);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }

        //组织形式ComboBox
        try {
            connection = JDBC.getConnection();
            String SQL = "select * from zzxs";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Zzxs zzxs = new Zzxs();
                zzxs.setZzxsid(resultSet.getString(1));
                zzxs.setZzxsname(resultSet.getString(2));
                zzxsComboBox.addItem(zzxs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }

        //注册币种ComboBox
        try {
            connection = JDBC.getConnection();
            String SQL = "select * from tzbz";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Bz bz = new Bz();
                bz.setBzid(resultSet.getString(1));
                bz.setBzname(resultSet.getString(2));
                zczbbzComboBox.addItem(bz);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }
        //登记机关
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

        //业务类型
        try {
            connection = JDBC.getConnection();
            String SQL = "select * from ywlx";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Ywlx ywlx = new Ywlx();
                ywlx.setYwlxid(resultSet.getString(1));
                ywlx.setYwlxname(resultSet.getString(2));
                ywlxComboBox.addItem(ywlx);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }
        //流程状态
        try {
            connection = JDBC.getConnection();
            String SQL = "select * from lczt";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Lcztdm lcztdm = new Lcztdm();
                lcztdm.setLcztdmid(resultSet.getString(1));
                lcztdm.setLcztdmname(resultSet.getString(2));
                lcztdmComboBox.addItem(lcztdm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }
        //监管状态
        try {
            connection = JDBC.getConnection();
            String SQL = "select * from jgzt";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Jgztdm jgztdm = new Jgztdm();
                jgztdm.setJgztdmid(resultSet.getString(1));
                jgztdm.setJgztdmname(resultSet.getString(2));
                jgztdmComboBox.addItem(jgztdm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }
        //重点行业
        try {
            connection = JDBC.getConnection();
            String SQL = "select * from zdhyfl";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Zdhy zdhy = new Zdhy();
                zdhy.setZdhyid(resultSet.getString(1));
                zdhy.setZdhyname(resultSet.getString(2));
                zdhyflComboBox.addItem(zdhy);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }
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
            String SQL = "select SCZTMC from market_subject_information where SCZTMC like ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, "%" + queryField.getText() + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            ;
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
            this.dataVector.clear();
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
                    Date date;
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
            this.table.setModel(defaultTableModel);
            if (table.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "未找到相关企业", "未找到相关企业", JOptionPane.OK_OPTION);
                queryField.setText("");
                return;
            } else {
                JOptionPane.showMessageDialog(null, "查询成功", "查询成功", JOptionPane.OK_OPTION);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }
    }

    //查询表格点击函数
    private void tableClick() {
        try {
            this.changeDataVector.clear();
            int row = table.getSelectedRow();
            String SCZTMC = table.getValueAt(row, 0).toString();
            connection = JDBC.getConnection();
            String SQL = "select * from market_subject_information where SCZTMC = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, SCZTMC);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                for (int i = 1; i <= 38; i++) {
                    Vector<Object> vector = new Vector<Object>();
                    switch (i) {
                        case 4: {
                            if (resultSet.getString(i) == null) {
                                vector.add(projectType[i - 1]);
                                vector.add(null);
                                changeDataVector.add(vector);
                                break;
                            }
                            for (int j = 0; j < xzqhlxComboBox.getItemCount(); j++) {
                                if (resultSet.getString(i).equals(xzqhlxComboBox.getItemAt(j).getXzqhlxid())) {
                                    vector.add(projectType[i - 1]);
                                    vector.add(xzqhlxComboBox.getItemAt(j));
                                    changeDataVector.add(vector);
                                    break;
                                }
                            }
                            break;
                        }
                        case 10: {
                            if (resultSet.getString(i) == null) {
                                vector.add(projectType[i - 1]);
                                vector.add(null);
                                changeDataVector.add(vector);
                                break;
                            }
                            for (int j = 0; j < hymcJComboBox.getItemCount(); j++) {
                                if (resultSet.getString(i).equals(hymcJComboBox.getItemAt(j).getHymcid())) {
                                    vector.add(projectType[i - 1]);
                                    vector.add(hymcJComboBox.getItemAt(j));
                                    changeDataVector.add(vector);
                                    break;
                                }
                            }
                            break;
                        }
                        case 11: {
                            if (resultSet.getString(i) == null) {
                                vector.add(projectType[i - 1]);
                                vector.add(null);
                                changeDataVector.add(vector);
                                break;
                            }
                            for (int j = 0; j < qylxComboBox.getItemCount(); j++) {
                                if (resultSet.getString(i).equals(qylxComboBox.getItemAt(j).getQylxid())) {
                                    vector.add(projectType[i - 1]);
                                    vector.add(qylxComboBox.getItemAt(j));
                                    changeDataVector.add(vector);
                                    break;
                                }
                            }
                            break;
                        }
                        case 12: {
                            if (resultSet.getString(i) == null) {
                                vector.add(projectType[i - 1]);
                                vector.add(null);
                                changeDataVector.add(vector);
                                break;
                            }
                            for (int j = 0; j < qyzlComboBox.getItemCount(); j++) {
                                if (resultSet.getString(i).equals(qyzlComboBox.getItemAt(j).getQyzlid())) {
                                    vector.add(projectType[i - 1]);
                                    vector.add(qyzlComboBox.getItemAt(j));
                                    changeDataVector.add(vector);
                                    break;
                                }
                            }
                            break;
                        }
                        case 15: {
                            if (resultSet.getString(i) == null) {
                                vector.add(projectType[i - 1]);
                                vector.add(null);
                                changeDataVector.add(vector);
                                break;
                            }
                            for (int j = 0; j < zzxsComboBox.getItemCount(); j++) {
                                if (resultSet.getString(i).equals(zzxsComboBox.getItemAt(j).getZzxsid())) {
                                    vector.add(projectType[i - 1]);
                                    vector.add(zzxsComboBox.getItemAt(j));
                                    changeDataVector.add(vector);
                                    break;
                                }
                            }
                            break;
                        }
                        case 18: {
                            if (resultSet.getString(i) == null) {
                                vector.add(projectType[i - 1]);
                                vector.add(null);
                                changeDataVector.add(vector);
                                break;
                            }
                            for (int j = 0; j < zczbbzComboBox.getItemCount(); j++) {
                                if (resultSet.getString(i).equals(zczbbzComboBox.getItemAt(j).getBzid())) {
                                    vector.add(projectType[i - 1]);
                                    vector.add(zczbbzComboBox.getItemAt(j));
                                    changeDataVector.add(vector);
                                    break;
                                }
                            }
                            break;
                        }
                        case 24: {
                            if (resultSet.getString(i) == null) {
                                vector.add(projectType[i - 1]);
                                vector.add(null);
                                changeDataVector.add(vector);
                                break;
                            }
                            for (int j = 0; j < djjgComboBox.getItemCount(); j++) {
                                if (resultSet.getString(i).equals(djjgComboBox.getItemAt(j).getDjjgid())) {
                                    vector.add(projectType[i - 1]);
                                    vector.add(djjgComboBox.getItemAt(j));
                                    changeDataVector.add(vector);
                                    break;
                                }
                            }
                            break;
                        }
                        case 26: {
                            if (resultSet.getString(i) == null) {
                                vector.add(projectType[i - 1]);
                                vector.add(null);
                                changeDataVector.add(vector);
                                break;
                            }
                            for (int j = 0; j < ywlxComboBox.getItemCount(); j++) {
                                if (resultSet.getString(i).equals(ywlxComboBox.getItemAt(j).getYwlxid())) {
                                    vector.add(projectType[i - 1]);
                                    vector.add(ywlxComboBox.getItemAt(j));
                                    changeDataVector.add(vector);
                                    break;
                                }
                            }
                            break;
                        }
                        case 27: {
                            if (resultSet.getString(i) == null) {
                                vector.add(projectType[i - 1]);
                                vector.add(null);
                                changeDataVector.add(vector);
                                break;
                            }
                            for (int j = 0; j < lcztdmComboBox.getItemCount(); j++) {
                                if (resultSet.getString(i).equals(lcztdmComboBox.getItemAt(j).getLcztdmid())) {
                                    vector.add(projectType[i - 1]);
                                    vector.add(lcztdmComboBox.getItemAt(j));
                                    changeDataVector.add(vector);
                                    break;
                                }
                            }
                            break;
                        }
                        case 28: {
                            if (resultSet.getString(i) == null) {
                                vector.add(projectType[i - 1]);
                                vector.add(null);
                                changeDataVector.add(vector);
                                break;
                            }
                            for (int j = 0; j < jgztdmComboBox.getItemCount(); j++) {
                                if (resultSet.getString(i).equals(jgztdmComboBox.getItemAt(j).getJgztdmid())) {
                                    vector.add(projectType[i - 1]);
                                    vector.add(jgztdmComboBox.getItemAt(j));
                                    changeDataVector.add(vector);
                                    break;
                                }
                            }
                            break;
                        }
                        case 29: {
                            if (resultSet.getString(i) == null) {
                                vector.add(projectType[i - 1]);
                                vector.add(null);
                                changeDataVector.add(vector);
                                break;
                            }
                            for (int j = 0; j < zdhyflComboBox.getItemCount(); j++) {

                                if (resultSet.getString(i).equals(zdhyflComboBox.getItemAt(j).getZdhyid())) {
                                    vector.add(projectType[i - 1]);
                                    vector.add(zdhyflComboBox.getItemAt(j));
                                    changeDataVector.add(vector);
                                    break;
                                }
                            }
                            break;
                        }
                        default: {
                            if (resultSet.getString(i) == null) {
                                vector.add(projectType[i - 1]);
                                vector.add(null);
                                changeDataVector.add(vector);
                                break;
                            }
                            vector.add(projectType[i - 1]);
                            vector.add(resultSet.getString(i));
                            changeDataVector.add(vector);
                        }

                    }

                }
                changeDatadefaultTableModel = new DefaultTableModel(changeDataVector, changeDataHeadVector);
                changeDataTable.setModel(changeDatadefaultTableModel);
            }
            changeDataTable.getColumnModel().getColumn(0).setPreferredWidth(100);
            changeDataTable.getColumnModel().getColumn(1).setPreferredWidth(450);
            changeDataTable.getColumnModel().getColumn(2).setPreferredWidth(450);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }
    }

    //确认函数
    private void change() {
        //表格替换
        for (int i = 0; i < 38; i++) {
            if (changeDataTable.getValueAt(i, 2) == null) {
                continue;
            }
            changeDataTable.setValueAt(changeDataTable.getValueAt(i, 2), i, 1);
            changeDataTable.setValueAt(null, i, 2);
        }

        connection = JDBC.getConnection();
        String SQL = "update market_subject_information set BCBS =?,  GMLX =?, XZQHLX =?, ZH =?, BXZH1 = ?," +
                " BXZH2 =?, ZHPY = ?, SCZTMC = ?,  HYDM = ?, LXDM =?, ZLDM =?, ZCH =?, ZS =?, " +
                " ZZXS =?, LXDH =?, ZCZB =?, TZBZ = ?, JYFW = ? , CLRQ = ?,  HZRQ =?, JYQXQ =?," +
                " JYQXZ =?, DJJGDM =?, YZBM =?, YWLX = ?, LCZTDM =?, JGZTDM =?, ZDHYFL =?," +
                " MTZP =?,  SZJZMC =?, JZLWZ =?,  SZJDMC =?, MPH =?, A =?, B =?, C =?, username =? where WYBS = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, changeDataTable.getValueAt(1, 1));
            preparedStatement.setObject(2, changeDataTable.getValueAt(2, 1));
            if (changeDataTable.getValueAt(3, 1) == null) {
                preparedStatement.setObject(3, null);
            } else {
                preparedStatement.setObject(3, ((Xzqhlx) changeDataTable.getValueAt(3, 1)).getXzqhlxid());
            }

            preparedStatement.setObject(4, changeDataTable.getValueAt(4, 1));
            preparedStatement.setObject(5, changeDataTable.getValueAt(5, 1));
            preparedStatement.setObject(6, changeDataTable.getValueAt(6, 1));
            preparedStatement.setObject(7, changeDataTable.getValueAt(7, 1));
            preparedStatement.setObject(8, changeDataTable.getValueAt(8, 1));
            if (changeDataTable.getValueAt(9, 1) == null) {
                preparedStatement.setObject(9, null);
            } else {
                preparedStatement.setObject(9, ((Hymc) changeDataTable.getValueAt(9, 1)).getHymcid());
            }
            preparedStatement.setObject(10, ((Qylx) changeDataTable.getValueAt(10, 1)).getQylxid());
            preparedStatement.setObject(11, ((Qyzl) changeDataTable.getValueAt(11, 1)).getQyzlid());
            preparedStatement.setObject(12, changeDataTable.getValueAt(12, 1));
            preparedStatement.setObject(13, changeDataTable.getValueAt(13, 1));
            preparedStatement.setObject(14, ((Zzxs) changeDataTable.getValueAt(14, 1)).getZzxsid());
            preparedStatement.setObject(15, changeDataTable.getValueAt(15, 1));
            preparedStatement.setObject(16, changeDataTable.getValueAt(16, 1));
            preparedStatement.setObject(17, ((Bz) changeDataTable.getValueAt(17, 1)).getBzid());
            preparedStatement.setObject(18, changeDataTable.getValueAt(18, 1));
            preparedStatement.setObject(19, changeDataTable.getValueAt(19, 1));
            preparedStatement.setObject(20, changeDataTable.getValueAt(20, 1));
            preparedStatement.setObject(21, changeDataTable.getValueAt(21, 1));
            preparedStatement.setObject(22, changeDataTable.getValueAt(22, 1));
            if (changeDataTable.getValueAt(23, 1) == null) {
                preparedStatement.setObject(23, null);
            } else {
                preparedStatement.setObject(23, ((Djjg) changeDataTable.getValueAt(23, 1)).getDjjgid());
            }

            preparedStatement.setObject(24, changeDataTable.getValueAt(24, 1));
            preparedStatement.setObject(25, ((Ywlx) changeDataTable.getValueAt(25, 1)).getYwlxid());
            preparedStatement.setObject(26, ((Lcztdm) changeDataTable.getValueAt(26, 1)).getLcztdmid());
            preparedStatement.setObject(27, ((Jgztdm) changeDataTable.getValueAt(27, 1)).getJgztdmid());
            if (changeDataTable.getValueAt(28, 1) == null) {
                preparedStatement.setObject(28, null);
            } else {
                preparedStatement.setObject(28, ((Zdhy) changeDataTable.getValueAt(28, 1)).getZdhyid());
            }

            preparedStatement.setObject(29, changeDataTable.getValueAt(29, 1));
            preparedStatement.setObject(30, changeDataTable.getValueAt(30, 1));
            preparedStatement.setObject(31, changeDataTable.getValueAt(31, 1));
            preparedStatement.setObject(32, changeDataTable.getValueAt(32, 1));
            preparedStatement.setObject(33, changeDataTable.getValueAt(33, 1));
            preparedStatement.setObject(34, changeDataTable.getValueAt(34, 1));
            preparedStatement.setObject(35, changeDataTable.getValueAt(35, 1));
            preparedStatement.setObject(36, changeDataTable.getValueAt(36, 1));
            preparedStatement.setObject(37, changeDataTable.getValueAt(37, 1));
            preparedStatement.setObject(38, changeDataTable.getValueAt(0, 1));
            System.out.println(preparedStatement);
            int i = preparedStatement.executeUpdate();
            if (i == 1) {
                JOptionPane.showMessageDialog(null, "修改成功");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }
    }
}

