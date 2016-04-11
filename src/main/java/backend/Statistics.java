package backend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Faiter119 on 11.04.2016.
 */
public class Statistics {

    private SQL sql;

    public Statistics(){
        this.sql = new SQL();
    }

    /*
     *  Orders over tid / Per dag / Måned
     *  Total ordre, subscriptions
     */
    public int getTotalOrders(){

        String[][] results = sql.getStringTable("SELECT COUNT(*) FROM HCL_order;",false);

        return Integer.parseInt(results[0][0]);
    }

    public ArrayList<Date> getDates(){

        ArrayList<Date> dates = new ArrayList<Date>();

        String[][] datesString = sql.getStringTable("SELECT order_date FROM HCL_order;",false);
        System.out.println(Arrays.deepToString(datesString) + " - .length : "+datesString.length+" - [0].length : "+datesString[0].length);

        for(int i=0; i<datesString.length; i++){
            try {
                Date date = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(datesString[i][0]).getTime());
                System.out.println(date);
                dates.add(date);
            }catch (ParseException e){return null;}
        }

        return dates;
    }




    public static void main(String[]args){

        Statistics stats = new Statistics();

        // System.out.println("Total Orders: "+stats.getTotalOrders());

        ArrayList<Date> dates = stats.getDates();

        for(int i=0; i<dates.size(); i++){
            System.out.println(dates.get(i));
        }
    }
}
