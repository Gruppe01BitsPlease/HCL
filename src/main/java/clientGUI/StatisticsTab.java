package clientGUI;

import backend.SQL;
import backend.Statistics;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import static clientGUI.MyJFreeChart.getStats;

/**
 * Creates the JPanel and adds the chart that is used as a tab in tabbedMenu
 */
public class StatisticsTab extends JPanel{
    private SQL sql;
    private JPanel westPanel;
    private JScrollPane table;

    public StatisticsTab(SQL sql){ // Thought about using an enum for the charts, but it just made it more complicated
        this.sql = sql;
        Statistics stats = new Statistics(sql);

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
        //
        table = MyJFreeChart.getStats(sql);
        westPanel = new JPanel(new BorderLayout());
        westPanel.add(MyJFreeChart.getStats(sql),BorderLayout.CENTER);
        //
        JButton refreshButton = new JButton("Refresh");

        refreshButton.addActionListener(e ->
                refreshStats()
        );
        westPanel.add(refreshButton, BorderLayout.SOUTH);
        //
        setLayout(new BorderLayout());
        add(westPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
    }
    public JPanel getStatsPanel(){
        return westPanel;
    }
    public JScrollPane getStatsPane(){
        return table;
    }
    public void refreshStats(){

        westPanel.remove(table);
        table = MyJFreeChart.getStats(sql);
        westPanel.add(table,BorderLayout.CENTER);

    }
    public static void main(String[]args){
        SQL sql = new SQL();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        panel.add(new StatisticsTab(sql),BorderLayout.CENTER);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

    }
}
