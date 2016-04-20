package backend;

import java.awt.*;
import clientGUI.JTableHCL;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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
    public static JScrollPane getStats(){
        Statistics stats = new Statistics();

        String[] names = {"Statistic","Value"};

        String[][] statTable = {
                {"Gross Income",Integer.toString(stats.getGrossIncome())+"kr"},
                {"Total Orders",Integer.toString(stats.getTotalOrders())},
                {"Total Subscriptions",Integer.toString(stats.getTotalSubscriptions())},
                {"Average Orders Per Month This Year",String.format("%.5s",Double.toString(stats.getAvgOrdersPerMonthThisYear()))},
                {"Popular Food All Time",stats.getAllTimePopularFood()},
                {"Popular Ingredient All Time ",stats.getAllTimePopularIngredient()}
        };
        // 2 Stats til, der du må skrive inn

        DefaultTableModel model = new DefaultTableModel(statTable,names);

        return new JScrollPane(new JTableHCL(model));
    }

    public static void main(final String[] args) { // http://www.java2s.com/Code/Java/Chart/JFreeChartBarChartDemo.htm

        Statistics stats = new Statistics();

        final String DAYS = "Orders By Day";
        final String MONTHS = "Orders By Month";

        double[] data = stats.getOrdersPerDay();
        String[] days = {"Mon","Tue","Wed","Thurs","Fri","Sat","Sun"};
        MyJFreeChart ordersByDayChart = new MyJFreeChart.Builder().title("Orders per day").dataset("Orders",days,data).build();

        String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        double[] data2 = stats.getOrdersPerMonth();
        MyJFreeChart ordersByMonthChart = new MyJFreeChart.Builder().title("Orders per month").dataset("Orders",months,data2).build();

        // Add the charts here
        JPanel cards = new JPanel(new CardLayout());
        cards.add(ordersByDayChart,DAYS);
        cards.add(ordersByMonthChart,MONTHS);


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

        //
        JFrame frame = new JFrame();  // The frame itself
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();  // The main panel in the frame

        JPanel centerPanel = new JPanel(new BorderLayout());
        JPanel westPanel = new JPanel();
        //

        centerPanel.add(cards,BorderLayout.CENTER);
        centerPanel.add(dropdownPanel,BorderLayout.SOUTH);

        westPanel.add(getStats());
        westPanel.setPreferredSize(new Dimension(500,122));
        //
        panel.add(westPanel,BorderLayout.WEST);
        panel.add(centerPanel,BorderLayout.CENTER);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
