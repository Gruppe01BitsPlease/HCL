package backend;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;

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

    public ArrayList<LocalDate> getDates(){

        ArrayList<LocalDate> dates = new ArrayList<LocalDate>();

        String[][] datesString = sql.getStringTable("SELECT delivery_date FROM HCL_order;",false);

        for(int i=0; i<datesString.length; i++){
            try {
                LocalDate date = LocalDate.parse(datesString[i][0]); // LocalDate >>>>>>> Date // java.time forever
                dates.add(date);
            }catch (DateTimeParseException e){return null;}
        }

        return dates;
    }

    /**
     * Table of which days get the most orders, aka delivery day
     * @return int[]: 0 = monday, 6 = sunday etc
     */
    public int[] ordersByDay(){

        int[] days = new int[7];

        ArrayList<LocalDate> dates = getDates(); // Gets delivery dates for all orders

        for (LocalDate date : dates) {

            days[date.getDayOfWeek().ordinal()]++;

        }
        return days;
    }

    /**
     * @return int[] with orders per months, 0 = January, 11 = Desember etc
     */
    public int[] ordersByMonth(){
        int[] months = new int[12];

        ArrayList<LocalDate> dates = getDates();

        for(LocalDate date : dates){
            months[date.getMonthValue()-1]++;
        }

        return months;
    }




    public static void main(String[]args){

        Statistics stats = new Statistics();

        // System.out.println("Total Orders: "+stats.getTotalOrders());

        ArrayList<LocalDate> dates = stats.getDates();

        for(int i=0; i<dates.size(); i++){
            System.out.println(dates.get(i));
        }

        System.out.println("Orders by Day: "+Arrays.toString(stats.ordersByDay()));
        System.out.println("Orders by Month: "+Arrays.toString(stats.ordersByMonth()));


      /*  LocalDate date = LocalDate.now();
        //System.out.println(date.getDayOfWeek().ordinal());
        System.out.println(date.toString());*/


    }
}
