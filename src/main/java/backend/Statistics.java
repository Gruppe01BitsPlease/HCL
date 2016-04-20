package backend;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Olav Husby on 11.04.2016.
 */
public class Statistics {

    private SQL sql;

    public Statistics(){
        this.sql = new SQL();
    }

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
    /*
     *  Orders over tid / Per dag / Måned
     *  Total ordre, subscriptions
     *  Mest populære ingrediens / mat
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
    @Deprecated
    public String getMonthlyPopularIngredient(int year, int month){ // Not fixed, but not used

        String prepString = "SELECT ingredient_id,`Ingredient Name`,Sum(Amount) FROM orders_dates_ingredients WHERE delivery_date BETWEEN ? AND ? GROUP BY ingredient_id ORDER BY sum(Amount) desc;";
        LocalDate startDate = LocalDate.of(year,month,1);
        LocalDate endDate;
        if(month != 12) {
            endDate = LocalDate.of(year, month + 1, 1);
        }
        else endDate = LocalDate.of(year+1, 1, 1);

        try{
            PreparedStatement prep = sql.connection.prepareStatement(prepString);
            prep.setDate(1, Date.valueOf(startDate));
            prep.setDate(2, Date.valueOf(endDate));

            ResultSet res = prep.executeQuery();
            res.next();
            return res.getString(2);

        }
        catch (SQLException e){return "";}

    }
    public String getAllTimePopularFood(){

        String[][] results = sql.getStringTable("SELECT name'Food Name', sum(number) FROM deliveries_foods " +
                "GROUP BY food_id ORDER BY sum(number) DESC;",false);
        try {
            return results[0][0];
        }
        catch (Exception e){return "";}

    }

    public int getGrossIncome(){

        String[][] results = sql.getStringTable("SELECT sum(price) FROM HCL_deliveries " +
                "NATURAL JOIN HCL_order WHERE HCL_deliveries.delivered=1 AND HCL_deliveries.active=1 " +
                "AND HCL_deliveries.completed=1;",false);
        try {
            return Integer.parseInt(results[0][0]);
        }
        catch (Exception e){return 0;}

    }
    public static void main(String[]args){

        Statistics stats = new Statistics();

       /* ArrayList<LocalDate> dates = stats.getDeliveryDates();
        for (LocalDate date : dates) {
            System.out.println(date);
        }*/

        System.out.println("Total Orders: "+stats.getTotalOrders());
        System.out.println("Total Subscriptions: "+stats.getTotalSubscriptions());
        System.out.println("Orders by Day: "+Arrays.toString(stats.getOrdersPerDay()));
        System.out.println("Orders by Month: "+Arrays.toString(stats.getOrdersPerMonth()));
        System.out.println("Avg Orders Per Month This Year: "+stats.getAvgOrdersPerMonthThisYear());
        System.out.println("Orders 2016-04: "+stats.getOrdersAt(2016,4));

        System.out.println("All Time Top Ingredient: "+stats.getAllTimePopularIngredient());
        // System.out.println("Popular Ingredient in month: "+stats.getMonthlyPopularIngredient(2016,4));

        System.out.println("All Time Popular Food: "+stats.getAllTimePopularFood());
        System.out.println("Gross Income: "+stats.getGrossIncome());


        // System.out.println("Popular Popular Food in month: "+stats.getAllTimePopularFood());


      /*  LocalDate date = LocalDate.now();
        //System.out.println(date.getDayOfWeek().ordinal());
        System.out.println(date.toString());*/ // JFreeChart


    }
}
