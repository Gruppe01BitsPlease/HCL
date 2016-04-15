package clientGUI;

import backend.MyJFreeChart;
import backend.Statistics;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Faiter119 on 14.04.2016.
 */
public class StatisticsTab extends JPanel{

    public StatisticsTab(){

        BorderLayout layout = new BorderLayout();
        setLayout(layout);

        // Center graph
        JPanel centerPanel = new JPanel();
        BorderLayout centerLayout = new BorderLayout();
        centerPanel.setLayout(centerLayout);

        Statistics stats = new Statistics();
        // Orders by Day
        double[] data = stats.getOrdersPerDay();
        String[] days = {"Mon","Tue","Wed","Thurs","Fri","Sat","Sun"};
        MyJFreeChart chart = new MyJFreeChart.Builder().title("Orders per day").dataset("Orders",days,data).build();
        centerPanel.add(chart, BorderLayout.CENTER);
        // Orders by Day
        add(centerPanel, BorderLayout.CENTER);

        // Center graph

        // West
       /* JPanel westPanel = new JPanel();
        BoxLayout box = new BoxLayout(westPanel, BoxLayout.LINE_AXIS);
        westPanel.setLayout(box);

        String[] graphs = {"Orders by Day","Orders by Month"};
        JComboBox<String> popup = new JComboBox<>(graphs);
        box.addLayoutComponent(popup, JPanel.LEFT_ALIGNMENT);

        add(westPanel,BorderLayout.WEST);*/
        // West


    }
}
