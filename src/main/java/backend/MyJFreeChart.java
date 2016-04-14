package backend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
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
        //super(builder.title);

        JFreeChart chart = createChart(builder.dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 270));
       // setContentPane(chartPanel);
    }

    /*
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(chartPanel);
     */

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

    /**
     * Creates a sample chart.
     *
     * @param dataset  the dataset.
     *
     * @return The chart.
     */
    private JFreeChart createChart(CategoryDataset dataset) {

        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart(
                "",         // chart title
                "",               // x axis label
                "",                  // y axis label
                dataset,                  // data
                PlotOrientation.VERTICAL, // orientation
                true,                     // include legend
                true,                     // tooltips?
                false                     // URLs?
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        // set the range axis to display integers only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        // set up gradient paints for series...
        GradientPaint gp0 = new GradientPaint(
                0.0f, 0.0f, Color.blue,
                0.0f, 0.0f, Color.lightGray
        );
        GradientPaint gp1 = new GradientPaint(
                0.0f, 0.0f, Color.green,
                0.0f, 0.0f, Color.lightGray
        );
        GradientPaint gp2 = new GradientPaint(
                0.0f, 0.0f, Color.red,
                0.0f, 0.0f, Color.lightGray
        );
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );
        // OPTIONAL CUSTOMISATION COMPLETED.

        return chart;

    }

    public static void main(final String[] args) {

        // http://www.java2s.com/Code/Java/Chart/JFreeChartBarChartDemo.htm

        Statistics stats = new Statistics();

        double[] data = stats.getOrdersPerDay();
        String[] days = {"Mon","Tue","Wed","Thurs","Fri","Sat","Sun"};

        MyJFreeChart demo = new MyJFreeChart.Builder().title("Orders per day").dataset("Orders",days,data).build();

       // demo.pack();
      //  RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
        //

      /*  String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sept","Oct","Nov","Dec"};
        double[] data2 = stats.getOrdersPerMonth();

        MyJFreeChart demo2 = new MyJFreeChart.Builder().title("Orders per month").dataset("Orders",months,data2).build();
        demo2.pack();
        RefineryUtilities.centerFrameOnScreen(demo2);
        demo2.setVisible(true);*/
    }
}
