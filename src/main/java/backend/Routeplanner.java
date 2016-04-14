package backend;

import java.util.Arrays;

/**
 * Created by Faiter119 on 11.04.2016.
 */
public class Routeplanner {

    private SQL sql;

    public Routeplanner(){
        this.sql = new SQL();
    }

    public String[][] getRoute(){
        String[][] ordersToday = sql.getStringTable("SELECT postnr, adress FROM HCL_order WHERE delivery_date = CURDATE() AND active = 1 ORDER BY postnr desc;",false);

        return ordersToday;
    }


    public static void main(String[]args){

        Routeplanner planner = new Routeplanner();

        System.out.println(Arrays.deepToString(planner.getRoute()));

    }
}
