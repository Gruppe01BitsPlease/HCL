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
        Statistics stats = new Statistics();
        double[] data = stats.getOrdersPerDay();
        String[] days = {"Mon","Tue","Wed","Thurs","Fri","Sat","Sun"};
        MyJFreeChart chart = new MyJFreeChart.Builder().title("Orders per day").dataset("Orders",days,data).build();
        add(chart, BorderLayout.CENTER);
        // Center graph


    }
}
