package backend;

import java.awt.*;
import java.util.Arrays;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import javax.swing.*;

public class MyJFreeChart extends JPanel {

    private CategoryDataset dataset;
    private JFreeChart chart;
    private ChartPanel chartPanel;

    private MyJFreeChart(Builder builder) {

        JFreeChart chart = createChart(builder.dataset, builder.title);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 270));

        add(chartPanel);
    }

    public static class Builder{

        private String title = "";

        private CategoryDataset dataset;
        private JFreeChart chart;
        private ChartPanel chartPanel;

        public Builder(){

        }
        public Builder title(String title){
            this.title = title;
            return this;
        }

        public Builder dataset(String title, String[] colomnNames, double[] colomnValues){
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            for(int i=0; i<colomnNames.length; i++){
                dataset.addValue(colomnValues[i],title,colomnNames[i]);
            }
            this.dataset = dataset;
            return this;
        }

        public MyJFreeChart build(){

            return new MyJFreeChart(this);

        }
    }

    /**Creates a JPanel chart with the dataset*/
    private JFreeChart createChart(CategoryDataset dataset, String title) {
        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart(
                title,         // chart title
                "",               // x axis label
                "",                  // y axis label
                dataset,                  // data
                PlotOrientation.VERTICAL, // orientation
                true,                     // include legend
                true,                     // tooltips?
                false                     // URLs?
        );
        return chart;
    }
    public static JPanel getOrdersByDayChart(){
        Statistics stats = new Statistics();
        double[] data = stats.getOrdersPerDay();
        String[] days = {"Mon", "Tue", "Wed", "Thurs", "Fri", "Sat", "Sun"};

        return new MyJFreeChart.Builder().title("Orders per day").dataset("Orders", days, data).build();
    }
    public static JPanel getOrdersByMonthChart(){
        Statistics stats = new Statistics();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};
        double[] data2 = stats.getOrdersPerMonth();

        return new MyJFreeChart.Builder().title("Orders per month").dataset("Orders", months, data2).build();
    }

    public static void main(final String[] args) {

        // http://www.java2s.com/Code/Java/Chart/JFreeChartBarChartDemo.htm

        Statistics stats = new Statistics();

       /* double[] data = stats.getOrdersPerDay();
        String[] days = {"Mon","Tue","Wed","Thurs","Fri","Sat","Sun"};

        MyJFreeChart demo = new MyJFreeChart.Builder().title("Orders per day").dataset("Orders",days,data).build();*/

       // demo.pack();
      //  RefineryUtilities.centerFrameOnScreen(demo);
       // demo.setVisible(true);
        //

        String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sept","Oct","Nov","Dec"};
        double[] data2 = stats.getOrdersPerMonth();
        MyJFreeChart ordersChart = new MyJFreeChart.Builder().title("Orders per month").dataset("Orders",months,data2).build();


        String[] graphs = {"Orders by Day","Orders by Month"};
        JComboBox<String> dropdown = new JComboBox<>(graphs);

        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        panel.add(ordersChart, BorderLayout.CENTER);

        JPanel dropdownPanel = new JPanel();
        dropdownPanel.add(dropdown);
        dropdown.addItemListener(e ->
                ordersChart.setGraph(panel,dropdown.getSelectedIndex())
        );

        panel.add(dropdownPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

    }
    private void setGraph(JPanel panel, int index){

        if(index == 0) {panel.add(MyJFreeChart.getOrdersByDayChart());}
        if(index == 1) {panel.add(MyJFreeChart.getOrdersByMonthChart());}
        panel.repaint();
    }
}
