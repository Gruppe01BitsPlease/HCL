package backend;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Creates the data for the Jchart
 * Extracts data from the database and compiles it into meaningful statistics
 */
public class Statistics {

    private SQL sql;

    public Statistics(){
        this.sql = new SQL();
    }

    /**
     * @return An ArrayList of LocalDate containing all the delivery-dates, also inactive ones.
     */
    private ArrayList<LocalDate> getDeliveryDates(){

        ArrayList<LocalDate> dates = new ArrayList<LocalDate>();

        String[][] datesString = sql.getStringTable("SELECT delivery_date FROM HCL_deliveries;",false);

        for(int i=0; i<datesString.length; i++){
            try {
                LocalDate date = LocalDate.parse(datesString[i][0]);
                dates.add(date);
            }
            catch (DateTimeParseException e){return null;}
        }
        return dates;
    }
    public ArrayList<String[]> getIngredientsInAllOrders(){

        ArrayList<String[]> ingredients = new ArrayList<String[]>();

        String[][] results = sql.getStringTable("SELECT delivery_date, delivery_id, ingredient_id, " +
                "`Ingredient Name`, `Ingredient Amount`*`Food Number`, `Ingredient Price`, `Ingredient Stock` " +
                "FROM deliveries_foods_ingredients;",false);

        for(String[] row : results){
            ingredients.add(row);
            System.out.println(Arrays.toString(row));
        }
        return ingredients;
    }

    /**
     * Total amount of subscriptions, using the deliverymanager.isSubscription method
     */
    public int getTotalSubscriptions(){

        OrderManager man = new OrderManager(sql);
        int count = 0;

        String[][] orders = sql.getStringTable("Select order_id from HCL_order;",false);
        for(String[] row : orders){

            int order_id = Integer.parseInt(row[0]);

            if(man.isSubscription(order_id)) count++;
        }
        return count;
    }

    /**
     * @return Total amount of orders, inactive and not.
     */
    public int getTotalOrders(){

        String[][] results = sql.getStringTable("SELECT COUNT(*) FROM HCL_order;",false);

        if(results.length > 0 || results[0].length > 0) {
            return Integer.parseInt(results[0][0]);
        }
        return 0;
    }

    /**
     * Table of which days get the deliverables
     * @return double[]: 0 = monday, 6 = sunday etc
     */
    public double[] getOrdersPerDay(){

        double[] days = new double[7];

        ArrayList<LocalDate> dates = getDeliveryDates(); // Gets delivery dates for all orders

        for (LocalDate date : dates) {
            days[date.getDayOfWeek().ordinal()]++;
        }
        return days;
    }

    /**
     * @return double[] with deliverables per months, 0 = January, 11 = Desember etc, over all years
     */
    public double[] getOrdersPerMonth(){
        double[] months = new double[12];

        ArrayList<LocalDate> dates = getDeliveryDates();

        for(LocalDate date : dates){
            months[date.getMonth().ordinal()]++;
        }

        return months;
    }

    /**
     * @return Orders placed stated year, month
     * @param month 1-12
     */
    public int getOrdersAt(int year, int month){

        int sum = 0;

        ArrayList<LocalDate> dates = getDeliveryDates();

        for(LocalDate date : dates){
            if(date.getMonth().ordinal() == month-1 && date.getYear() == year){
                sum++;
            }
        }
        return sum;
    }
    public int getDeliveriesToday(){
        int sum = 0;

        ArrayList<LocalDate> dates = getDeliveryDates();
        LocalDate now = LocalDate.now();

        for(LocalDate date : dates){
            if(date.equals(now)){
                sum++;
            }
        }
        return sum;
    }

    public double getAvgOrdersPerMonthThisYear(){

        int sum = 0;
        ArrayList<LocalDate> dates = getDeliveryDates();

        for (LocalDate date : dates) {
            //System.out.println(date.getYear() +" "+ LocalDate.now().getYear());

            if (date.getYear() == LocalDate.now().getYear()){
                // System.out.println(sum+1);
                sum++;
            }
        }

        return (double) sum / 12;
    }

    /**
     * @return ID of all time most sold ingredient, -1 if no results
     */
    public String getAllTimePopularIngredient(){

        String[][] results = sql.getStringTable("SELECT `Ingredient Name`,sum(`Total Ingredients`) " +
                "FROM deliveries_ingredients_total GROUP BY ingredient_id ORDER BY sum(`Total Ingredients`) DESC ;",false);
        try {
            return results[0][0];
        }
        catch (Exception e){return "";}
    }
    /**
     * @return ID of most sold ingredient in specified month and year, -1 if no results
     */
    public String getMonthlyPopularIngredient(){

        String prepString = "SELECT ingredient_id, `Ingredient Name`, Sum(`Total Ingredients`) " +
                "FROM deliveries_ingredients_total WHERE delivery_date " +
                "BETWEEN ? AND ? GROUP BY ingredient_id ORDER BY sum(`Total Ingredients`) DESC;";

        LocalDate now = LocalDate.now();
        LocalDate startDate = LocalDate.of(now.getYear(),now.getMonthValue(),1);
        LocalDate endDate = startDate.plusMonths(1);

        try{
            PreparedStatement prep = sql.connection.prepareStatement(prepString);
            prep.setDate(1, Date.valueOf(startDate));
            prep.setDate(2, Date.valueOf(endDate));

            ResultSet res = prep.executeQuery();
            res.next();
            return res.getString(2);

        }
        catch (Exception e){return "";} // SQLException and NullPointer
    }
    public String getMonthlyPopularFood(){

        String prepString = "SELECT food_id,name,Sum(number) FROM deliveries_foods WHERE delivery_date " +
                "BETWEEN ? AND ? GROUP BY food_id ORDER BY Sum(number) DESC;";

        LocalDate now = LocalDate.now();
        LocalDate startDate = LocalDate.of(now.getYear(),now.getMonthValue(),1);
        LocalDate endDate = startDate.plusMonths(1);

        try{
            PreparedStatement prep = sql.connection.prepareStatement(prepString);
            prep.setDate(1, Date.valueOf(startDate));
            prep.setDate(2, Date.valueOf(endDate));

            ResultSet res = prep.executeQuery();
            res.next();
            return res.getString(2);

        }
        catch (Exception e){return "";} // SQLException and NullPointer
    }
    public String getAllTimePopularFood(){

        String[][] results = sql.getStringTable("SELECT name'Food Name', sum(number) FROM deliveries_foods " +
                "GROUP BY food_id ORDER BY sum(number) DESC;",false);
        try {
            return results[0][0];
        }
        catch (Exception e){return "";} // SQLException and NullPointer

    }

    /**
     * @return Cost off all completed and delivered orders.
     */
    public int getGrossIncome(){

        String[][] results = sql.getStringTable("SELECT sum(price) FROM HCL_deliveries NATURAL JOIN HCL_order " +
                "WHERE HCL_deliveries.delivered=1 AND HCL_deliveries.completed=1;",false);
        try {
            return Integer.parseInt(results[0][0]);
        }
        catch (Exception e){return 0;} // SQLException, NumberFormat and NullPointer

    }
    public int getNumberOfCustomers(){
        String[][] result = sql.getStringTable("SELECT count(*) FROM HCL_customer WHERE active=1;",false);
        try{
            return Integer.parseInt(result[0][0]);
        }
        catch (Exception e){return -1;} // SQLException, NumberFormat and NullPointer
    }
    public String getBiggestCustomer(){

        String[][] results = sql.getStringTable("SELECT customer_name, customer_id,count(customer_id) FROM HCL_customer " +
                "NATURAL JOIN HCL_order NATURAL JOIN HCL_deliveries WHERE delivered=1 GROUP BY customer_id " +
                "ORDER BY count(customer_id) DESC ;",false);

        try{
            return results[0][0];
        }
        catch (Exception e){return "";} // -||-

    }
    public String getBiggestCustomerThisMonth(){

        LocalDate now = LocalDate.now();
        LocalDate thisMonth = LocalDate.of(now.getYear(),now.getMonthValue(),1);
        LocalDate nextMonth = thisMonth.plusMonths(1);

        String prepString = "SELECT customer_name,customer_id,count(customer_id) " +
                "FROM HCL_customer NATURAL JOIN HCL_order NATURAL JOIN HCL_deliveries " +
                "WHERE delivered=1 AND delivery_date BETWEEN ? AND ? " +
                "GROUP BY customer_id ORDER BY count(customer_id) DESC;";

        try{
            PreparedStatement prep = sql.connection.prepareStatement(prepString);

            prep.setDate(1,Date.valueOf(thisMonth));
            prep.setDate(2,Date.valueOf(nextMonth));

            ResultSet res = prep.executeQuery();
            res.next();
            return res.getString(1);
        }
        catch (Exception e){return "";} // -||-

    }
    public static void main(String[]args){

        Statistics stats = new Statistics();
        LocalDate now = LocalDate.now();

       /* ArrayList<LocalDate> dates = stats.getDeliveryDates();
        for (LocalDate date : dates) {
            System.out.println(date);
        }*/

        System.out.println("Total Orders: "+stats.getTotalOrders());
        System.out.println("Total Subscriptions: "+stats.getTotalSubscriptions());
        System.out.println("Orders by Day: "+Arrays.toString(stats.getOrdersPerDay()));
        System.out.println("Orders by Month: "+Arrays.toString(stats.getOrdersPerMonth()));
        System.out.println("Avg Orders Per Month This Year: "+stats.getAvgOrdersPerMonthThisYear());
        // System.out.println("Orders 2016-04: "+stats.getOrdersAt(2016,4));

        System.out.println("All Time Top Ingredient: "+stats.getAllTimePopularIngredient());
        System.out.println("Popular Ingredient in month: "+stats.getMonthlyPopularIngredient());

        System.out.println("All Time Popular Food: "+stats.getAllTimePopularFood());
        System.out.println("Gross Income: "+stats.getGrossIncome());
        System.out.println("Monthly Popular Food: "+stats.getMonthlyPopularFood());
        // System.out.println("Popular Popular Food in month: "+stats.getAllTimePopularFood());

        System.out.println("Number of customers: "+stats.getNumberOfCustomers());
        System.out.println("Biggest Customer: "+stats.getBiggestCustomer());
        System.out.println("Biggest Customer This Month: "+stats.getBiggestCustomerThisMonth());
        System.out.println("Deliveries today: "+ stats.getDeliveriesToday());

      /*  LocalDate date = LocalDate.now();
        //System.out.println(date.getDayOfWeek().ordinal());
        System.out.println(date.toString());*/ // JFreeChart

    }
}
