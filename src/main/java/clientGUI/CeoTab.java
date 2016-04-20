package clientGUI;

import backend.SQL;

import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;


class CeoTab extends JPanel {

    public CeoTab(int role) {

        setLayout(new BorderLayout());

        // JPanel panel = new JPanel(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();

        // User Tab
        tabs.addTab("Employees", new EmployeeTab(new SQL(),role));
        // User Tab

        // Stats Panel
        tabs.addTab("Statistics",new StatisticsTab());
        // Stats Panel

        // panel.add(tabs, BorderLayout.CENTER);

        add(tabs,BorderLayout.CENTER);

    }
}

