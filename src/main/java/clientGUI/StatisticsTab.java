package clientGUI;

import backend.MyJFreeChart;
import backend.Statistics;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class StatisticsTab extends JPanel{

    private Statistics stats;

    public StatisticsTab(){

        stats = new Statistics();

        setLayout(new BorderLayout());

        // Center Panel
            JPanel centerPanel = new JPanel();
            centerPanel.setLayout(new BorderLayout());

            // Dropdown

            String[] graphs = {"Orders by Day","Orders by Month"};
            JComboBox<String> dropdown = new JComboBox<>(graphs);
            centerPanel.add(dropdown, BorderLayout.SOUTH);

            // TODO: FIX
            dropdown.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    int selection = dropdown.getSelectedIndex();
                   // centerPanel.remove(0);
                    if(selection == 0) {
                        double[] data = stats.getOrdersPerDay();
                        String[] days = {"Mon", "Tue", "Wed", "Thurs", "Fri", "Sat", "Sun"};
                        MyJFreeChart chart = new MyJFreeChart.Builder().title("Orders per day").dataset("Orders", days, data).build();
                        centerPanel.add(chart, BorderLayout.CENTER);
                    }
                    // Orders by Day

                    if(selection == 1) {
                        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};
                        double[] data2 = stats.getOrdersPerMonth();
                        MyJFreeChart chart2 = new MyJFreeChart.Builder().title("Orders per month").dataset("Orders", months, data2).build();
                        centerPanel.add(chart2, BorderLayout.CENTER);
                    }
                }
            });

        // Dropdown

            add(centerPanel, BorderLayout.CENTER);
        // Center Panel

        JPanel westPanel = new JPanel();
      //  westPanel.setLayout( );
        String[] names = {"Statistic","Value"};
        String[][] statTable = new String[5][2];
        statTable[0][0] = "All Time Popular Food";
        statTable[1][0] = "All Time Popular Ingredient";
        statTable[2][0] = "Total Orders";
        statTable[3][0] = "Average Orders Per Month";
        statTable[4][0] = "Total Subscriptions";
        statTable[0][1] = stats.getAllTimePopularFood();
        statTable[1][1] = stats.getAllTimePopularIngredient();
        statTable[2][1] = Integer.toString(stats.getTotalOrders());
        statTable[3][1] = Double.toString(stats.getAvgOrdersPerMonthThisYear());
        statTable[4][1] = Integer.toString(stats.getTotalSubscriptions());

        DefaultTableModel model = new DefaultTableModel(statTable,names);
        JTable statistics = new JTable(model);

        westPanel.add(statistics);

        add(westPanel,BorderLayout.WEST);


    }
}
