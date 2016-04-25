package backend;

import java.util.Arrays;

public class Routeplanner {

    private SQL sql;

    public Routeplanner(SQL sql){
        this.sql = sql;
    }


    /**
     * @return The deliveres ordered by zip-code.
     */
    public String[][] getRoute(){
        String[][] ordersToday = sql.getStringTable("SELECT customer_name, tlf, adress, postnr " +
                "FROM HCL_customer NATURAL JOIN HCL_order NATURAL JOIN HCL_deliveries " +
                "WHERE HCL_deliveries.delivery_date = CURDATE() " +
                "AND active = 1 AND HCL_deliveries.delivered = 0 ORDER BY postnr asc;",false);

        return ordersToday;
    }


    public static void main(String[]args){
        SQL sql = new SQL();
        Routeplanner planner = new Routeplanner(sql);

        System.out.println(Arrays.deepToString(planner.getRoute()));

    }
}
