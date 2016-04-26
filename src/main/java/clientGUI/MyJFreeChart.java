package clientGUI;

import java.awt.*;

import backend.SQL;
import backend.Statistics;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Creates the chart
 */
public class MyJFreeChart extends JPanel {

    private CategoryDataset dataset;
    private JFreeChart chart;
    private ChartPanel chartPanel;

    private MyJFreeChart(Builder builder) {
        setLayout(new BorderLayout());
        JFreeChart chart = createChart(builder.dataset, builder.title);
        ChartPanel chartPanel = new ChartPanel(chart);
        //chartPanel.setPreferredSize(new Dimension(500, 270));

        add(chartPanel, BorderLayout.CENTER);
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
        return ChartFactory.createBarChart(
                title,         // chart title
                "",               // x axis label
                "",                  // y axis label
                dataset,                  // data
                PlotOrientation.VERTICAL, // orientation
                true,                     // include legend
                true,                     // tooltips?
                false                     // URLs?
        );
    }
    public static JPanel getOrdersByDayChart(SQL sql){
        Statistics stats = new Statistics(sql);
        double[] data = stats.getOrdersPerDay();
        String[] days = {"Mon", "Tue", "Wed", "Thurs", "Fri", "Sat", "Sun"};

        return new MyJFreeChart.Builder().title("Orders per day").dataset("Orders", days, data).build();
    }
    public static JPanel getOrdersByMonthChart(SQL sql){
        Statistics stats = new Statistics(sql);
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};
        double[] data2 = stats.getOrdersPerMonth();

        return new MyJFreeChart.Builder().title("Orders per month").dataset("Orders", months, data2).build();
    }
    public static JScrollPane getStats(SQL sql){
        Statistics stats = new Statistics(sql);

        String[] names = {"Statistic","Value"};

        String[][] statTable = {
                {"Gross Income",Integer.toString(stats.getGrossIncome())+"kr"},
                {"Total Orders",Integer.toString(stats.getTotalOrders())},
                {"Total Deliveries Today",Integer.toString(stats.getDeliveriesToday())},
                {"Total Subscriptions",Integer.toString(stats.getTotalSubscriptions())},
                {"Average Orders Per Month This Year",String.format("%.5s",Double.toString(stats.getAvgOrdersPerMonthThisYear()))},
                {"Popular Food All Time",stats.getAllTimePopularFood()},
                {"Popular Food This Month",stats.getMonthlyPopularFood()},
                {"Popular Ingredient All Time",stats.getAllTimePopularIngredient()},
                {"Popular Ingredient This Month",stats.getMonthlyPopularIngredient()},
                {"Number Of Customers",Integer.toString(stats.getNumberOfCustomers())},
                {"Biggest Customer",stats.getBiggestCustomer()},
                {"Biggest Customer This Month",stats.getBiggestCustomerThisMonth()}
        };

        DefaultTableModel model = new DefaultTableModel(statTable,names);

        return new JScrollPane(new JTableHCL(model));
    }
    public static DefaultTableModel getNewDefaultTableModel(SQL sql){

        Statistics stats = new Statistics(sql);

        String[] names = {"Statistic","Value"};

        String[][] statTable = {
                {"Gross Income",Integer.toString(stats.getGrossIncome())+"kr"},
                {"Total Orders",Integer.toString(stats.getTotalOrders())},
                {"Total Deliveries Today",Integer.toString(stats.getDeliveriesToday())},
                {"Total Subscriptions",Integer.toString(stats.getTotalSubscriptions())},
                {"Average Orders Per Month This Year",String.format("%.5s",Double.toString(stats.getAvgOrdersPerMonthThisYear()))},
                {"Popular Food All Time",stats.getAllTimePopularFood()},
                {"Popular Food This Month",stats.getMonthlyPopularFood()},
                {"Popular Ingredient All Time",stats.getAllTimePopularIngredient()},
                {"Popular Ingredient This Month",stats.getMonthlyPopularIngredient()},
                {"Number Of Customers",Integer.toString(stats.getNumberOfCustomers())},
                {"Biggest Customer",stats.getBiggestCustomer()},
                {"Biggest Customer This Month",stats.getBiggestCustomerThisMonth()}
        };

        return new DefaultTableModel(statTable,names);
    }
}
