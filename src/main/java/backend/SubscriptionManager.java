package backend;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.Period;
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
     *@param date Formatted as yyyy-MM-dd
     * @return
     *  1: OK
     * -1: Does not exist
     * -2: SQL Exception
     * -3: Wrong Parameters
     * -5: Wrong date formatting
     */
    public int removeDate(int order_id, String date){

        if(order_id < 0 || date== null || date.equals("")) return -3;
        if(!sql.rowExists(CURRENT_TABLE,CURRENT_TABLE_PK,order_id)) return -1;

        String prepString = "UPDATE "+CURRENT_TABLE+" SET active = FALSE WHERE "+CURRENT_TABLE_PK+" = ? AND dato = ?";

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

    public static void main(String[]args){

        SQL sql = new SQL();

        SubscriptionManager manager = new SubscriptionManager(sql);

        // manager.generate(4);

        // manager.addDate(4,"2011-04-03");

        LocalDate first = LocalDate.now();
        manager.addDates(5,first,15,30);

    }
}
