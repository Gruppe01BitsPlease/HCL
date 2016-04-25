package clientGUI;

import backend.SQL;

import javax.swing.*;
import java.awt.*;

/**
 * Creates the JPanel that is used as a tab in tabbedMenu
 */
class CeoTab extends JPanel {
    private EmployeeTab empTab;
    private StatisticsTab statTab;
    public CeoTab(int role, SQL sql) {

        setLayout(new BorderLayout());

        // JPanel panel = new JPanel(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();

        // User Tab
        empTab = new EmployeeTab(sql, role);
        tabs.addTab("Employees", empTab);
        // User Tab

        // Stats Panel
        statTab = new StatisticsTab(sql);
        tabs.addTab("Statistics",statTab);
        // Stats Panel

        // panel.add(tabs, BorderLayout.CENTER);

        add(tabs,BorderLayout.CENTER);

    }
    public void refresh() {
        //statTab.refreshStats();
        empTab.refresh();
    }
}

