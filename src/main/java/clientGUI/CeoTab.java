package clientGUI;

import backend.SQL;

import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;


class CeoTab extends JPanel {
    private EmployeeTab empTab;
    public CeoTab(int role, SQL sql) {

        setLayout(new BorderLayout());

        // JPanel panel = new JPanel(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();

        // User Tab
        empTab = new EmployeeTab(sql, role);
        tabs.addTab("Employees", empTab);
        // User Tab

        // Stats Panel
        tabs.addTab("Statistics",new StatisticsTab());
        // Stats Panel

        // panel.add(tabs, BorderLayout.CENTER);

        add(tabs,BorderLayout.CENTER);

    }
    public void refresh() {
        empTab.refresh();
    }
}

