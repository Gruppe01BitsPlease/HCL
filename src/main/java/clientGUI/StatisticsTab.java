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

import static backend.MyJFreeChart.getStats;

public class StatisticsTab extends JPanel{

    private Statistics stats;

    public StatisticsTab(){

        Statistics stats = new Statistics();

        final String DAYS = "Orders By Day";
        final String MONTHS = "Orders By Month";

        double[] data = stats.getOrdersPerDay();
        String[] days = {"Mon","Tue","Wed","Thurs","Fri","Sat","Sun"};
        MyJFreeChart ordersByDayChart = new MyJFreeChart.Builder().title("Orders per day").dataset("Orders",days,data).build();

        String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        double[] data2 = stats.getOrdersPerMonth();
        MyJFreeChart ordersByMonthChart = new MyJFreeChart.Builder().title("Orders per month").dataset("Orders",months,data2).build();

        // Card Panek, add "cards" here
        JPanel cards = new JPanel(new CardLayout());
        cards.add(ordersByDayChart,DAYS);
        cards.add(ordersByMonthChart,MONTHS);
        // Card Panek, add "cards" here


        // Dropdown Panel
        JPanel dropdownPanel = new JPanel();

        String[] graphs = {DAYS,MONTHS};
        JComboBox<String> dropdown = new JComboBox<>(graphs);
        dropdown.addItemListener(e -> {
            CardLayout cl = (CardLayout)(cards.getLayout());
            cl.show(cards, (String)e.getItem());
        });

        dropdownPanel.add(dropdown,BorderLayout.NORTH);
        // Dropdown Panel


        JPanel panel = new JPanel(/*new BorderLayout()*/);  // The main panel in the frame

        JPanel centerPanel = new JPanel(new BorderLayout());
        JPanel westPanel = new JPanel();

        centerPanel.add(cards,BorderLayout.CENTER);
        centerPanel.add(dropdownPanel,BorderLayout.SOUTH);

        westPanel.add(getStats());

        panel.add(westPanel/*,BorderLayout.WEST*/);
        panel.add(centerPanel/*,BorderLayout.CENTER*/);


        add(panel);
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
