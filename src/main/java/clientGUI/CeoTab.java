package clientGUI;

import backend.SQL;

import javax.swing.*;
import java.awt.*;

/**
 * Creates the JPanel that is used as a tab in TabbedMenu
 */
class CeoTab extends JPanel {
    private JTabbedPane tabs;
    private EmployeeTab empTab;
    private StatisticsTab statTab;
    private int role;
    private SQL sql;
    private String statTitle = "Statistics";
    public CeoTab(int role, SQL sql) {
        this.role = role;
        this.sql = sql;
        setLayout(new BorderLayout());

        tabs = new JTabbedPane();

        // User Tab
        empTab = new EmployeeTab(sql, role);
        tabs.addTab("Employees", empTab);
        // User Tab

        // Stats Panel
        statTab = new StatisticsTab(sql, this);
        tabs.addTab(statTitle,statTab);
        // Stats Panel

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> refresh());
        add(tabs,BorderLayout.CENTER);
    }
    public void refreshStats() {
        //Hacky way of refreshing the statistics tab, since nothing else worked
        int selected = tabs.getSelectedIndex();
        tabs.removeTabAt(tabs.indexOfTab(statTitle));
        statTab = new StatisticsTab(sql, this);
        tabs.addTab(statTitle,statTab);
        if (selected != -1) {
            tabs.setSelectedIndex(selected);
        }
    }
    public void refresh() {
        empTab.refresh();
        refreshStats();
    }
}

