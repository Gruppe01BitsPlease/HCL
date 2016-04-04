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
    public static final String CURRENT_TABLE_LINK_FOOD = "HCL_order_food";
    public static final String CURRENT_TABLE_LINK_PACKAGE = "HCL_order_package";


    public static final String CURRENT_TABLE_GENERATE_ARGUMENTS = "(customer_id,price,adress,postnr,order_date,delivery_date)";
    public static final String CURRENT_TABLE_DELETE_ARGUMENTS = "(order_id)";
    public static final String CURRENT_TABLE_ADD_FOOD_ARGUMENTS = "(order_id, food_id, number)";
    public static final String CURRENT_TABLE_ADD_PACKAGE_ARGUMENTS = "(order_id, package_id)";


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
    /**
     *
     * @return
     * 1: OK
     * -1: Already exist
     * -2: SQL Exception
     * -3: Wrong Parameters
     */
    public int addFood(int order_id, int food_id, int number){
        // Init
        SQL sql = new SQL();
        LinkManager link = new LinkManager(sql);
        // End Init

        if(food_id <0 || order_id <0 || number <0 )return -3;
        if(sql.rowExists(CURRENT_TABLE_LINK_FOOD, "order_id","food_id",order_id,food_id)) return -1;

        String prepString = "Insert into "+CURRENT_TABLE_LINK_FOOD+CURRENT_TABLE_ADD_FOOD_ARGUMENTS+" values(?,?,?)";
        try {

            PreparedStatement prep = sql.connection.prepareStatement(prepString);

            prep.setInt(1,order_id);
            prep.setInt(2,food_id);
            prep.setInt(3,number);

            prep.executeUpdate();

            return 1;
        }
        catch (SQLException e){
            return -2;
        }
    }
    /**
     *
     * @return
     * 1: OK
     * -1: Already exist
     * -2: SQL Exception
     * -3: Wrong Parameters
     */
    public int addPackage(int order_id, int package_id){
        // Init

        SQL sql = new SQL();
        LinkManager link = new LinkManager(sql);
        // End Init

        if(package_id <0 || order_id <0 )return -3;
        if(sql.rowExists(CURRENT_TABLE_LINK_PACKAGE, "order_id","package_id",order_id,package_id)) return -1;

        String prepString = "Insert into "+CURRENT_TABLE_LINK_PACKAGE+CURRENT_TABLE_ADD_PACKAGE_ARGUMENTS+" values(?,?)";
        try {
            PreparedStatement prep = sql.connection.prepareStatement(prepString);

            prep.setInt(1,order_id);
            prep.setInt(2,package_id);

            prep.executeUpdate();

            return 1;
        }
        catch (SQLException e){
            e.printStackTrace();
            return -2;
        }
    }

    public static void main(String[]args){

        SQL sql = new SQL();
        OrderManager order = new OrderManager(sql);

        //order.generate(2,100,"Ostehaug",1911,"2015-01-01","2015-02-02");
        //order.addFood(2,200,5);
        int p = order.addPackage(2,1);
        System.out.println(p);
       // order.delete(3);
       // order.delete("Ost");
    }
}
