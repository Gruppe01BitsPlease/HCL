package clientGUI;

import backend.Statistics;

import javax.swing.*;
import java.awt.*;

import static clientGUI.MyJFreeChart.getStats;

/**
 * Creates the JPanel and adds the chart that is used as a tab in tabbedMenu
 */
public class StatisticsTab extends JPanel{

    public StatisticsTab(){ // Thought about using an enum for the charts, but it just made it more complicated

        Statistics stats = new Statistics();

        final String DAYS = "Orders By Day";
        final String MONTHS = "Orders By Month";

        // Orders By Day Chart
        double[] data = stats.getOrdersPerDay();
        String[] days = {"Mon","Tue","Wed","Thurs","Fri","Sat","Sun"};
        MyJFreeChart ordersByDayChart = new MyJFreeChart.Builder().title(DAYS).dataset("Orders",days,data).build();
        // End Orders By Day Chart

        // Orders By Month Chart
        String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        double[] data2 = stats.getOrdersPerMonth();
        MyJFreeChart ordersByMonthChart = new MyJFreeChart.Builder().title(MONTHS).dataset("Orders",months,data2).build();
        // End Orders By Month Chart

        // Card Panel, add "cards" here
        JPanel cards = new JPanel(new CardLayout());
        JPanel cardPanel = new JPanel(new BorderLayout());
        JPanel cardPanel2 = new JPanel(new BorderLayout());
        cardPanel.add(ordersByDayChart,BorderLayout.CENTER);
        cardPanel2.add(ordersByMonthChart, BorderLayout.CENTER);
        cards.add(cardPanel, DAYS);
        cards.add(cardPanel2, MONTHS);
        // End Card Panel, add "cards" here

        String[] graphs = {DAYS,MONTHS};

        JComboBox<String> dropdown = new JComboBox<>(graphs);
        dropdown.addItemListener(e -> {
            CardLayout cl = (CardLayout)(cards.getLayout());
            cl.show(cards, (String)e.getItem());
        });

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(cards,BorderLayout.CENTER);
        centerPanel.add(dropdown,BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(getStats(), BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
    }
    public static void main(String[]args){

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        panel.add(new StatisticsTab(),BorderLayout.CENTER);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

    }
}
