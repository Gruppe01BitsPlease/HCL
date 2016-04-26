package backend;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Is responsible for managing delivery entries in the database
 */
public class DeliveryManager {

    private SQL sql;
    public static final String CURRENT_TABLE = "HCL_deliveries";
    public static final String CURRENT_TABLE_LINK_ORDER = "HCL_order";

    public static final String CURRENT_TABLE_GENERATE_ARGUMENTS = "(order_id)";
    public static final String CURRENT_TABLE_PK = "(delivery_id)";
    public static final String CURRENT_TABLE_ADD_DATE_ARGUMENTS = "(order_id, delivery_date)";

    public DeliveryManager(SQL sql){
        this.sql = sql;
    }

    /**
     * @return
     *  1: OK
     * -1: Row does not  exist
     * -2: SQL Exception
     */
    public int delete(int delivery_id) {
        try {
            if(!sql.rowExists(CURRENT_TABLE,CURRENT_TABLE_PK,delivery_id)) return -1;

            String sqlPrep = "UPDATE "+CURRENT_TABLE+" SET active = FALSE WHERE "+CURRENT_TABLE_PK+" = ?";
            PreparedStatement prep = sql.connection.prepareStatement(sqlPrep);

            prep.setInt(1,delivery_id);
            prep.executeUpdate();
            return 1;
        }
        catch (SQLException e){return -2;}
    }
    /**
     * Adds a date/delivery to an order at the spesified date
     * Possible to add several deliveries on the same day
     *@param date Formatted as yyyy-MM-dd
     * @return
     * ID: OK, id of created delivery
     * -2: SQL Exception
     * -3: Invalid order ID
     * -5: Wrong date formatting
     */
    public int addDate(int order_id, String date){

        if(!sql.rowExists("HCL_order","order_id",order_id)) return -3;

        String prepString = "Insert into "+CURRENT_TABLE+CURRENT_TABLE_ADD_DATE_ARGUMENTS+" values(?,?)";
        try {
            LocalDate date1 = LocalDate.parse(date);
            PreparedStatement prep = sql.connection.prepareStatement(prepString);

            prep.setInt(1,order_id);
            prep.setDate(2,Date.valueOf(date1));

            prep.executeUpdate();

            return sql.getLastID();
        }
        catch (SQLException e){
            System.out.println(e.toString() + ": " + e.getMessage());
            return -2;
        }
        catch (DateTimeParseException f) {
            System.out.println(f.toString() + ": " + f.getMessage());
            return -5;
        }
    }
    /**
     * Removes a delivery date
     * @return
     *  1: OK
     * -1: Does not exist
     * -2: SQL Exception
     * -3: Wrong Parameters
     * -5: Wrong date formatting
     */
    public int removeDate(int delivery_id){

        if(!sql.rowExists(CURRENT_TABLE,CURRENT_TABLE_PK,delivery_id)) return -3;

        String prepString = "UPDATE "+CURRENT_TABLE+" SET active = FALSE WHERE "+CURRENT_TABLE_PK+" = ?";

        try {

            PreparedStatement prep = sql.connection.prepareStatement(prepString);

            prep.setInt(1,delivery_id);

            prep.executeUpdate();

            return 1;
        }
        catch (SQLException e){
            return -2;
        }
        catch (DateTimeParseException f){
            return -5;
        }
    }
    @Deprecated
    private boolean contains(int[] dates, int date){

        for(int value : dates){
            if(value == date) return true;
        }
        return false;
    }
    /**
     * -1 Invalid dates
     * Use the other one instead
     */
    @Deprecated
    public int addDates(int order_id, LocalDate start, int ... dates){

        for(int date : dates){

            if(date > 31 )return -1;
        }
        // dates contains dates in the month after the startdate to be added to the dates-list
        // 1 Year subscription

        LocalDate cur = LocalDate.parse(start.toString()); // Deep copy
        int counter = 0;
        while(counter < 365){

            cur = cur.plusDays(1);
            counter++;

            if(contains(dates,cur.getDayOfMonth())){
                if( addDate(order_id,cur.toString()) == 1){  System.out.println("Adding: "+cur);}
            }

        }
        return 1;
    }

    /**
     * Same as addDates(), but does not actaully add the dates, just returns a String[] of the dates that are
     *  supposed to be added; used for GUI.
     */
    public String[] getDatesToBeAdded(int order_id,LocalDate start, LocalDate end, int interval, DayOfWeek[] days){

        if(!sql.rowExists("HCL_order","order_id",order_id) || days.length == 0) return new String[0];

        LocalDate current = LocalDate.of(start.getYear(),start.getMonth(),start.getDayOfMonth()); // "Deep" copy

        int weekCounter = 0;

        ArrayList<String> dates = new ArrayList<>();

        while(!Period.between(current,end).isNegative()){

            for(DayOfWeek day : days){
                if(current.getDayOfWeek().equals(day)){ dates.add(current.toString());}
            }

            weekCounter++;
            current = current.plusDays(1);

            if(weekCounter == 7 && interval > 1){
                weekCounter = 0;
                current = current.plusDays(7*interval); // Skips weeks
            }
        }
        String[] array = new String[dates.size()];
        for(int i=0; i<array.length; i++){array[i]=dates.get(i);}
        return array;
    }

    /**
     * Adds the dates from the "start" to the "end" at the weekdays spesified by the DayOfWeek-objects in the
     *  days-table. Use getDatesToBeAdded() if you want to know what these dates are.
     * @param interval How often: 1 = per week, 2 = every other week, 3 = every third week etc
     * @param days A DayOfWeek table filled with the weekdays you want deliveries on. Fks {DayOfWeek.MONDAY} will
     *             add deliveries only on mondays.
     */
    public void addDates(int order_id,LocalDate start, LocalDate end, int interval, DayOfWeek[] days/*boolean mon, boolean tues, boolean wed, boolean thur, boolean fri, boolean sat, boolean sun*/){

        if(!sql.rowExists("HCL_order","order_id",order_id) || days.length == 0) return;

        LocalDate current = LocalDate.of(start.getYear(),start.getMonth(),start.getDayOfMonth()); // "Deep" copy

        int weekCounter = 0;

        while(!Period.between(current,end).isNegative()){

            for(DayOfWeek day : days){
                if(current.getDayOfWeek().equals(day)){ addDate(order_id,current.toString());}
            }

            weekCounter++;
            current = current.plusDays(1);

            if(weekCounter == 7 && interval > 1){
                weekCounter = 0;
                current = current.plusDays(7*interval); // Skips weeks
            }
        }
    }

    /**
     * Sets a delivery as "delivered", meaning it's been delivered to the customer in a satisfying manner and have
     *  been payed for.
     * @return
     *  1: OK
     * -1: Does not exist
     * -2: SQL Exception
     * */
    public int deliver(int delivery_id){

        if(!sql.rowExists(CURRENT_TABLE,CURRENT_TABLE_PK,delivery_id)) return -1;

        String prepString = "Update "+CURRENT_TABLE+" SET delivered = true WHERE delivery_id = ?;";

        try{
            PreparedStatement prep = sql.connection.prepareStatement(prepString);

            prep.setInt(1,delivery_id);

            prep.executeUpdate();
            return 1;
        }
        catch (SQLException e){return -2;}

    }

    /**
     * Sets a delivery as "completed", meaning the cheffs have finished the order and it is ready to be delivered
     * @return
     *  1: OK
     * -1: Does not exist
     * -2: SQL Exception
     */
    public int complete(int delivery_id){

        if(!sql.rowExists(CURRENT_TABLE,CURRENT_TABLE_PK,delivery_id)) return -1;

        String prepString = "Update "+CURRENT_TABLE+" SET completed = true WHERE delivery_id = ?;";

        try{
            PreparedStatement prep = sql.connection.prepareStatement(prepString);

            prep.setInt(1,delivery_id);

            prep.executeUpdate();
            return 1;
        }
        catch (SQLException e){return -2;}

    }

    public static void main(String[]args){

        SQL sql = new SQL();

        DeliveryManager manager = new DeliveryManager(sql);

        // manager.generate(4);

        // manager.addDate(4,"2011-04-03");
/*
        LocalDate first = LocalDate.now();
        manager.addDates(5,first,15,30);*/
        //System.out.println(manager.removeDate(4,3008));

        DayOfWeek[] days = {DayOfWeek.MONDAY, DayOfWeek.FRIDAY};
        System.out.println(days.toString());
        System.out.println("Dates:"+ Arrays.toString(manager.getDatesToBeAdded(130,LocalDate.now(),LocalDate.of(2016,6,1),2, days)));
        //manager.addDates(11, LocalDate.now(), LocalDate.of(2017,12,24), 2, days);
        System.out.println("Send Help");


    }
}
