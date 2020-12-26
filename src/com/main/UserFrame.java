package com.main;

import com.user.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;

public class UserFrame extends JFrame {
    private JPanel mainPanel;
    private JSplitPane mainSplitPane;
    private JPanel startPanelRight;
    private JPanel startPanelLeft;
    private JScrollPane contentsScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    private JTree contentTree = new JTree();
    private StartPage start = new StartPage();

    private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("系统菜单");
    private DefaultMutableTreeNode startPageNode = new DefaultMutableTreeNode("开始页面");
    private DefaultMutableTreeNode nameBusinessNode = new DefaultMutableTreeNode("企业名称登记");
    private DefaultMutableTreeNode enterpriseQueryNode = new DefaultMutableTreeNode("企业查询");
    private DefaultMutableTreeNode preApprovedNode = new DefaultMutableTreeNode("预先核准");
    private DefaultMutableTreeNode hasApprovedNode = new DefaultMutableTreeNode("已核调整");
    private DefaultMutableTreeNode applicationStatusNode = new DefaultMutableTreeNode("状态查询");
    private DefaultMutableTreeNode manageIdeaNode = new DefaultMutableTreeNode("办理意见");
    private DefaultMutableTreeNode noteprintNode = new DefaultMutableTreeNode("文书打印");
    private DefaultTreeModel treeModel;
    DefaultTreeCellRenderer defaultTreeCellRenderer;
    private ImageIcon imageIcon = new ImageIcon(getClass().getResource("/com/image/business.jpg"));

    public UserFrame(String username) {
        //基本设置
        this.setTitle("工商管理系统  当前用户: " + username);
        this.setResizable(false);
        this.setSize(1280, 800);
        this.setContentPane(mainPanel);
        this.setLocationRelativeTo(null);
        this.mainSplitPane.setDividerLocation(200);
        this.mainSplitPane.setEnabled(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mainSplitPane.setRightComponent(this.start);
        this.setIconImage(this.imageIcon.getImage());
        //树模型设计

        this.rootNode.add(this.startPageNode);
        this.rootNode.add(this.nameBusinessNode);
        this.nameBusinessNode.add(this.enterpriseQueryNode);
        this.nameBusinessNode.add(this.preApprovedNode);
        this.nameBusinessNode.add(this.hasApprovedNode);
        this.nameBusinessNode.add(this.noteprintNode);
        this.nameBusinessNode.add(this.applicationStatusNode);
        this.nameBusinessNode.add(this.manageIdeaNode);

        //树模型
        this.treeModel = new DefaultTreeModel(this.rootNode);
        this.contentTree.setModel(this.treeModel);
        this.contentsScrollPane.getViewport().add(this.contentTree);
        this.mainSplitPane.setLeftComponent(this.contentsScrollPane);
        this.defaultTreeCellRenderer = new DefaultTreeCellRenderer();
        this.contentTree.setCellRenderer(this.defaultTreeCellRenderer);
        this.contentTree.setBackground(Color.decode("#1d124e"));

        //设置默认选中
        this.contentTree.setSelectionPath(new TreePath(this.startPageNode.getPath()));
        //树额监听事件
        this.contentTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) contentTree.getLastSelectedPathComponent();
                if (node == null) {
                    return;
                }
                switch (node.toString()) {
                    case "企业查询": {
                        EnterpriseQuery query = new EnterpriseQuery();
                        mainSplitPane.setRightComponent(query);
                        mainSplitPane.setDividerLocation(200);
                        break;
                    }
                    case "预先核准": {
                        PreApproved approved = new PreApproved(username);
                        mainSplitPane.setRightComponent(approved);
                        mainSplitPane.setDividerLocation(200);
                        break;
                    }
                    case "已核调整": {
                        HasApproved has = new HasApproved(username);
                        mainSplitPane.setRightComponent(has);
                        mainSplitPane.setDividerLocation(200);
                        break;
                    }
                    case "办理意见": {
                        ManageIdea idea = new ManageIdea(username);
                        mainSplitPane.setRightComponent(idea);
                        mainSplitPane.setDividerLocation(200);
                        break;
                    }
                    case "开始页面": {
                        mainSplitPane.setRightComponent(start);
                        mainSplitPane.setDividerLocation(200);
                        break;
                    }
                    case "状态查询": {
                        StatusQuery status = new StatusQuery();
                        mainSplitPane.setRightComponent(status);
                        mainSplitPane.setDividerLocation(200);
                        break;
                    }
                    case "文书打印": {
                        NotePrint notePrint = new NotePrint(username);
                        mainSplitPane.setRightComponent(notePrint);
                        mainSplitPane.setDividerLocation(200);
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        });
        setVisible(true);
    }
}
