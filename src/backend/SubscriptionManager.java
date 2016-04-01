package backend;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Faiter119 on 15.03.2016.
 */
public class SubscriptionManager {

    private SQL sql;
    public static final String CURRENT_TABLE = "HCL_subscription";
    public static final String CURRENT_TABLE_LINK_DATE = "HCL_subscription_date";
    public static final String CURRENT_TABLE_LINK_ORDER = "HCL_order";

    public static final String CURRENT_TABLE_GENERATE_ARGUMENTS = "(order_id)";
    public static final String CURRENT_TABLE_DELETE_ARGUMENTS = "(order_id)";
    public static final String CURRENT_TABLE_ADD_DATE_ARGUMENTS = "(order_id, dato)";

    public SubscriptionManager(SQL sql){
        this.sql = sql;
    }

    /**
     * @return 1: OK
     * -1: Already exists
     * -2: SQL Exception
     * -3: Wrong parameters
     */
    public int generate(int order_id) {

        if(!(order_id >=0)) return -3;

        try {

            sql.connection.setAutoCommit(false);

            String sqlPrep = "INSERT INTO "+CURRENT_TABLE+CURRENT_TABLE_GENERATE_ARGUMENTS+" VALUES (?)";
            PreparedStatement prep = sql.connection.prepareStatement(sqlPrep);

            prep.setInt(1,order_id);
            prep.executeUpdate();

            sql.connection.commit();
            sql.connection.setAutoCommit(true);

            return 1;
        }
        catch (SQLException e){
            try {
                sql.connection.rollback();
            }
            catch (SQLException f){return -2;}
            return -2;}
    }

    /**
     * @return 1: OK
     * -1: Row does not  exist
     * -2: SQL Exception
     */
    public int delete(int order_id) {
        try {
            if(!sql.rowExists(CURRENT_TABLE,CURRENT_TABLE_DELETE_ARGUMENTS,order_id)) return -1;

            String sqlPrep = "DELETE FROM "+CURRENT_TABLE+" WHERE "+CURRENT_TABLE_DELETE_ARGUMENTS+" = ?";
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
     * -1: Already exist
     * -2: SQL Exception
     * -3: Wrong Parameters
     * -4: File not found
     * -5: Wrong date formatting
     */
    public int addDate(int order_id, String date){
        // Init
        File file = null;
        try {
            file = new File(FoodManager.class.getResource("/Database.ini").toURI().getPath(), true);
        }
        catch (Exception e){}
        if (file == null) return -4;
        Logon logon = new Logon(file);
        SQL sql = new SQL(logon);
        LinkManager link = new LinkManager(sql);
        // End Init
        Date date1 = null;
        try {
            date1 = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime());
        }
        catch (ParseException e){return -5;}

        if(order_id <0 || date.equals("") )return -3;

        String prepString = "Insert into "+CURRENT_TABLE_LINK_DATE+CURRENT_TABLE_ADD_DATE_ARGUMENTS+" values(?,?)";
        try {
            sql.connection.setAutoCommit(false);

            PreparedStatement prep = sql.connection.prepareStatement(prepString);

            prep.setInt(1,order_id);
            prep.setDate(2,date1);

            prep.executeUpdate();

            sql.connection.commit();
            sql.connection.setAutoCommit(true);

            return 1;
        }
        catch (SQLException e){
            try{
                sql.connection.rollback();
            }
            catch (SQLException f){return -2;};
            return -2;
        }
    }
    /**
     * Possible to add several deliveries on the same day
     *@param date Formatted as yyyy-MM-dd
     * @return
     * 1: OK
     * -1: Does not exist
     * -2: SQL Exception
     * -3: Wrong Parameters
     * -4: File not found
     * -5: Wrong date formatting
     */
    public int removeDate(int order_id, String date){

        if(order_id < 0 || date.equals("")) return -3;

        Date date1 = null;
        try {
            date1 = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime());
        }
        catch (ParseException e){return -5;}

        String prepString = "Delete from "+CURRENT_TABLE_LINK_DATE+" where order_id = ? AND dato = ?";
        try {
            sql.connection.setAutoCommit(false);

            PreparedStatement prep = sql.connection.prepareStatement(prepString);

            prep.setInt(1,order_id);
            prep.setDate(2,date1);

            prep.executeUpdate();

            sql.connection.commit();
            sql.connection.setAutoCommit(true);

            return 1;
        }
        catch (SQLException e){
            try{
                sql.connection.rollback();
            }
            catch (SQLException f){return -2;};
            return -2;
        }


    }

    public static void main(String[]args){
        File file = null;

        try {
            file = new File(OrderManager.class.getResource("/Database.ini").toURI().getPath(), true);
        }
        catch (Exception e){}

        Logon logon = new Logon(file);
        SQL sql = new SQL(logon);

        SubscriptionManager manager = new SubscriptionManager(sql);

        manager.generate(4);

        manager.addDate(4,"2011-04-03");

    }
}
