package backend;

import java.util.Arrays;

/**
 *
 */
public class Routeplanner {

    private SQL sql;

    public Routeplanner(){
        this.sql = new SQL();
    }

    public String[][] getRoute(){
        String[][] ordersToday = sql.getStringTable("SELECT customer_name, tlf, adress, postnr " +
                "FROM HCL_customer NATURAL JOIN HCL_order NATURAL JOIN HCL_deliveries " +
                "WHERE HCL_deliveries.delivery_date = CURDATE() " +
                "AND active = 1 AND HCL_deliveries.delivered = 0 ORDER BY postnr asc;",false);

        return ordersToday;
    }


    public static void main(String[]args){

        Routeplanner planner = new Routeplanner();

        System.out.println(Arrays.deepToString(planner.getRoute()));

    }
}
