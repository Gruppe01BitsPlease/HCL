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

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        boolean shouldFill = false;
        boolean shouldWeightX = true;
        if (shouldFill) {
            //natural height, maximum width
            c.fill = GridBagConstraints.HORIZONTAL;
        }

        Statistics stats = new Statistics();
        double[] data = stats.getOrdersPerDay();
        String[] days = {"Mon","Tue","Wed","Thurs","Fri","Sat","Sun"};

        setLayout(new GridLayout());

        add(new MyJFreeChart.Builder().title("Orders per day").dataset("Orders",days,data).build());

    }

}
