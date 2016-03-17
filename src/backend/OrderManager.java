package backend;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Faiter119 on 15.03.2016.
 */
public class OrderManager {

    private SQL sql;
    public static final String CURRENT_TABLE = "HCL_order";
    public static final String CURRENT_TABLE_GENERATE_ARGUMENTS = "(customer_id,price,adress,postnr,order_date,delivery_date)";
    public static final String CURRENT_TABLE_DELETE_ARGUMENTS = "(order_id)";

    public OrderManager(SQL sql){
        this.sql = sql;
    }

    /**
     * @return 1: OK
     * -1: Already exists
     * -2: SQL Exception
     * -3: Wrong parameters
     */
    public int generate(int customer_id, int price, String adress, int postnr, String order_date, String delivery_date) {

        if(!(order_date.trim().length() > 0) || !(price >= 0) || !(customer_id >=0)) return -3;

        try {

            Date date1 = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(order_date).getTime());
            Date date2 = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(delivery_date).getTime());

            String sqlPrep = "INSERT INTO "+CURRENT_TABLE+CURRENT_TABLE_GENERATE_ARGUMENTS+" VALUES(?,?,?,?,?,?)";
            PreparedStatement prep = sql.connection.prepareStatement(sqlPrep);

            prep.setInt(1,customer_id);
            prep.setInt(2,price);
            prep.setString(3,adress);
            prep.setInt(4,postnr);
            prep.setDate(5,date1);
            prep.setDate(6,date2);
            prep.executeUpdate();
            return 1;
        }
        catch (SQLException e){return -2;}
        catch (ParseException e){return -2;}
    }

    /**
     * @return 1: OK
     * -1: Row does not  exist
     * -2: SQL Exception
     */
    public int delete(int nr) {
        try {
            if(!sql.rowExists(CURRENT_TABLE,"customer_id",nr)) return -1;

            String sqlPrep = "DELETE FROM "+CURRENT_TABLE+" WHERE "+CURRENT_TABLE_DELETE_ARGUMENTS+" = ?";
            PreparedStatement prep = sql.connection.prepareStatement(sqlPrep);
            prep.setInt(1,nr);
            prep.executeUpdate();
            return 1;
        }
        catch (SQLException e){return -2;}
    }

    public static void main(String[]args){
        File file = null;

        try {
            file = new File(OrderManager.class.getResource("Database.ini").toURI().getPath(), true);
        }
        catch (Exception e){}

        Logon logon = new Logon(file);
        SQL sql = new SQL(logon);
        OrderManager order = new OrderManager(sql);

        //order.generate(2,100,"Ostehaug",1911,"2015-01-01","2015-02-02");
        order.delete(3);
       // order.delete("Ost");
    }
}
