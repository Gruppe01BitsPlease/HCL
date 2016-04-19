package backend;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Created by Olav Husby on 15.03.2016.
 */
public class OrderManager {

    private SQL sql;
    public static final String CURRENT_TABLE = "HCL_order";
    public static final String CURRENT_TABLE_LINK_FOOD = "HCL_order_food";

    public static final String CURRENT_TABLE_GENERATE_ARGUMENTS = "(customer_id,price,adress,postnr,order_date)";
    public static final String CURRENT_TABLE_PK = "(order_id)";
    public static final String CURRENT_TABLE_ADD_FOOD_ARGUMENTS = "(order_id, food_id, number)";


    public OrderManager(SQL sql){
        this.sql = sql;
    }

    /**
     * @return 1: OK
     * -1: Already exists
     * -2: SQL Exception
     * -3: Wrong parameters
     */
    public int generate(int customer_id, int price, String adress, int postnr, String order_date) {

        if(price < 0 || customer_id < 0) return -3;

        try {
            System.out.println("Order date:" + order_date);
            LocalDate date1 = LocalDate.parse(order_date);

            String sqlPrep = "INSERT INTO "+CURRENT_TABLE+CURRENT_TABLE_GENERATE_ARGUMENTS+" VALUES(?,?,?,?,?)";
            PreparedStatement prep = sql.connection.prepareStatement(sqlPrep);

            prep.setInt(1,customer_id);
            prep.setInt(2,price);
            prep.setString(3,adress);
            prep.setInt(4,postnr);
            prep.setDate(5,Date.valueOf(date1));
            prep.executeUpdate();

            return sql.getLastID();
        }
        catch (SQLException e){
            System.out.println(e);
            return -2;}
        catch (DateTimeParseException e){
            System.out.println(e.toString() + ": " + e.getMessage());
            return -3;}
    }

    /**
     * @return 1: OK
     * -1: Row does not  exist
     * -2: SQL Exception
     */
    public int delete(int nr) {

        if(!sql.rowExists(CURRENT_TABLE,CURRENT_TABLE_PK ,nr)) return -1;

        try {

            String sqlPrep = "UPDATE "+CURRENT_TABLE+" SET active = FALSE WHERE "+CURRENT_TABLE_PK+" = ?";
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
     *  1: OK
     * -1: Already exist
     * -2: SQL Exception
     * -3: Wrong Parameters
     */
    public int addFood(int order_id, int food_id, int number){

        LinkManager link = new LinkManager(sql);

        if(food_id <0 || order_id <0 || number <0 )return -3;
        if(sql.rowExists(CURRENT_TABLE_LINK_FOOD, "order_id","food_id",order_id,food_id)) return -1;

        return link.generate(CURRENT_TABLE_LINK_FOOD,"order_id","food_id",order_id,food_id,number);

    }

    /**
     * @return true if the order_id is valid and the order has >1 deliveries
     */
    public boolean isSubscription(int order_id){

        if(!sql.rowExists(CURRENT_TABLE,CURRENT_TABLE_PK,order_id)) return false;

        String[][] results = sql.getStringTable("SELECT count(*) FROM HCL_order NATURAL JOIN HCL_deliveries WHERE active = 1 AND delivered = 0 AND order_id = "+order_id,false);

        return Integer.parseInt(results[0][0]) > 1;

    }
    public static void main(String[]args){

        SQL sql = new SQL();
        OrderManager order = new OrderManager(sql);

        int res = order.generate(10,111,"Ostehaug",1911,"2015-01-01");
        System.out.println(res);
        //order.addFood(2,200,5);
        /*int p = order.addPackage(2,1);
        System.out.println(p);*/
       // order.delete(3);
       // order.delete("Ost");
       // System.out.println(order.generate(70,150,"Testing",1000,"2016-04-19","2016-04-29"));
        System.out.println(order.isSubscription(4));
    }
}
