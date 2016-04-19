package backend;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeParseException;

/**
 * Created by Faiter119 on 15.03.2016.
 */
public class SubscriptionManager {

    private SQL sql;
    public static final String CURRENT_TABLE = "HCL_subscription";
    public static final String CURRENT_TABLE_LINK_DATE = "HCL_subscription_date";
    public static final String CURRENT_TABLE_LINK_ORDER = "HCL_order";

    public static final String CURRENT_TABLE_GENERATE_ARGUMENTS = "(order_id)";
    public static final String CURRENT_TABLE_PK = "(order_id)";
    public static final String CURRENT_TABLE_ADD_DATE_ARGUMENTS = "(order_id, dato)";

    public SubscriptionManager(SQL sql){
        this.sql = sql;
    }

    /**
     * @return
     *  1: OK
     * -1: Already exists
     * -2: SQL Exception
     * -3: Wrong parameters
     */
    public int generate(int order_id) {

        if(order_id < 0) return -3;

        try {

            String sqlPrep = "INSERT INTO "+CURRENT_TABLE+CURRENT_TABLE_GENERATE_ARGUMENTS+" VALUES (?)";
            PreparedStatement prep = sql.connection.prepareStatement(sqlPrep);

            prep.setInt(1,order_id);
            prep.executeUpdate();

            return sql.getLastID();
        }
        catch (SQLException e){
            return -2;}
    }

    /**
     * @return
     *  1: OK
     * -1: Row does not  exist
     * -2: SQL Exception
     */
    public int delete(int order_id) {
        try {
            if(!sql.rowExists(CURRENT_TABLE,CURRENT_TABLE_PK,order_id)) return -1;

            String sqlPrep = "UPDATE "+CURRENT_TABLE+" SET active = FALSE WHERE "+CURRENT_TABLE_PK+" = ?";
            PreparedStatement prep = sql.connection.prepareStatement(sqlPrep);

            prep.setInt(1,order_id);
            prep.executeUpdate();
            return 1;
        }
        catch (SQLException e){return -2;}
    }
    /**
     * Possible to add several deliveries on the same day
     *@param date Formatted as yyyy-MM-dd
     * @return
     * 1: OK
     * -1: Does not exist
     * -2: SQL Exception
     * -3: Wrong Parameters
     * -5: Wrong date formatting
     */
    public int addDate(int order_id, String date){

        if(order_id <0 || date.equals("") )return -3;
        if(!sql.rowExists(CURRENT_TABLE,CURRENT_TABLE_PK,order_id)) return -1;

        String prepString = "Insert into "+CURRENT_TABLE_LINK_DATE+CURRENT_TABLE_ADD_DATE_ARGUMENTS+" values(?,?)";
        try {
            LocalDate date1 = LocalDate.parse(date);
            PreparedStatement prep = sql.connection.prepareStatement(prepString);

            prep.setInt(1,order_id);
            prep.setDate(2,Date.valueOf(date1));

            prep.executeUpdate();

            return 1;
        }
        catch (SQLException e){
            return -2;
        }
    }
    /**
     * Possible to add several deliveries on the same day
     * @return
     *  1: OK
     * -1: Does not exist
     * -2: SQL Exception
     * -3: Wrong Parameters
     * -5: Wrong date formatting
     */
    public int removeDate(int order_id, int date_id){

        if(order_id < 0 || date_id < 0  ) return -3;

        if(!sql.rowExists(CURRENT_TABLE,CURRENT_TABLE_PK,order_id)) return -1;

        String prepString = "UPDATE "+CURRENT_TABLE+"_date"+" SET active = FALSE WHERE "+CURRENT_TABLE_PK+" = ? AND date_id = ?";

        try {

            PreparedStatement prep = sql.connection.prepareStatement(prepString);

            prep.setInt(1,order_id);
            prep.setInt(2,date_id);

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
    private boolean contains(int[] dates, int date){

        for(int value : dates){
            if(value == date) return true;
        }
        return false;
    }
    /**
     * -1 Invalid dates
     */
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
     * @param interval How often: 1 = per week, 2 = every other week, 3 = every third week etc
     */
    public void addDates(int order_id,LocalDate start, LocalDate end, int interval, boolean mon, boolean tues, boolean wed, boolean thur, boolean fri, boolean sat, boolean sun){

        LocalDate current = LocalDate.of(start.getYear(),start.getMonth(),start.getDayOfMonth());
        int weekCounter = 0;
        while(Period.between(current,end).getDays() != 0){
            System.out.println(current);
            if(current.getDayOfWeek() == DayOfWeek.MONDAY && mon){ addDate(order_id,current.toString());}
            if(current.getDayOfWeek() == DayOfWeek.TUESDAY && tues){ addDate(order_id,current.toString());}
            if(current.getDayOfWeek() == DayOfWeek.WEDNESDAY && wed){ addDate(order_id,current.toString());}
            if(current.getDayOfWeek() == DayOfWeek.THURSDAY && thur){ addDate(order_id,current.toString());}
            if(current.getDayOfWeek() == DayOfWeek.FRIDAY && fri){ addDate(order_id,current.toString());}
            if(current.getDayOfWeek() == DayOfWeek.SATURDAY && sat){ addDate(order_id,current.toString());}
            if(current.getDayOfWeek() == DayOfWeek.SUNDAY && sun){ addDate(order_id,current.toString());}

            weekCounter++;
            current = current.plusDays(1);

            if(weekCounter == 7 && interval != 1){
                weekCounter = 0;
                current = current.plusDays(7*interval); // Skips weeks
            }
        }
    }

    /**
     * @return
     *  1: OK
     * -1: Does not exist
     * -2: SQL Exception
     * -3: Wrong Parameters
     */
    public int deliver(int date_id){

        if(!sql.rowExists(CURRENT_TABLE_LINK_DATE,"date_id",date_id)) return -1;

        String prepString = "Update "+CURRENT_TABLE_LINK_DATE+" SET delivered = true WHERE date_id = ?;";

        try{
            PreparedStatement prep = sql.connection.prepareStatement(prepString);

            prep.setInt(1,date_id);

            prep.executeUpdate();
            return 1;
        }
        catch (SQLException e){return -2;}

    }

    public static void main(String[]args){

        SQL sql = new SQL();

        SubscriptionManager manager = new SubscriptionManager(sql);

        // manager.generate(4);

        // manager.addDate(4,"2011-04-03");
/*
        LocalDate first = LocalDate.now();
        manager.addDates(5,first,15,30);*/
        //System.out.println(manager.removeDate(4,3008));

        manager.addDates(2,LocalDate.now(),LocalDate.of(2016,6,1),2,true,true,true,false,false,true,true);


    }
}
