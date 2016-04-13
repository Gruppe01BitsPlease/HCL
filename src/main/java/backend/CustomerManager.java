package backend;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Faiter119 on 15.03.2016.
 */
public class CustomerManager {

    private SQL sql;
    public static final String CURRENT_TABLE = "HCL_customer";
    public static final String CURRENT_TABLE_GENERATE_ARGUMENTS = "(name, epost, tlf)";
    public static final String CURRENT_TABLE_DELETE_ARGUMENTS = "(customer_id)";

    public CustomerManager(SQL sql){
        this.sql = sql;
    }

    /**
     * @return 1: OK
     * -1: Already exists
     * -2: SQL Exception
     * -3: Wrong parameters
     */
    public int generate(String name, String epost, int tlf) {

        if(!(name.trim().length() > 0) || !(tlf >= 0) || epost.trim().equals("")) return -3; //invalid parameters

        try {

            String sqlPrep = "INSERT INTO "+CURRENT_TABLE+CURRENT_TABLE_GENERATE_ARGUMENTS+" VALUES(?,?,?)";
            PreparedStatement prep = sql.connection.prepareStatement(sqlPrep);

            prep.setString(1,name);
            prep.setString(2,epost);
            prep.setInt(3,tlf);

            prep.executeUpdate();

            return sql.getLastID();
        }
        catch (SQLException e){
            System.out.println(e.toString());
            return -2;}
    }

    /**
     * @return
     *  1: OK
     * -1: Already exists
     * -2: SQL Exception
     * -3: Wrong parameters
     */
    public int edit(int id, String name, String nyEpost, int nyTlf){
        try {
            sql.connection.setAutoCommit(false);

            if (!sql.rowExists(CURRENT_TABLE, "customer_id", id)) return -1;
            if (name.trim().equals("") || nyEpost.trim().equals("") || nyTlf < 0) return -3;

            sql.update(CURRENT_TABLE, "epost", "customer_id", Integer.toString(id), nyEpost);
            sql.update(CURRENT_TABLE, "tlf", "customer_id", Integer.toString(id), nyTlf);

            sql.connection.commit();
            return 1;
        }
        catch (SQLException e){
            try {
                sql.connection.rollback();
            }
            catch (SQLException a){
                return -2;
            }
            return -2;
        }
        finally {
            try {
                sql.connection.setAutoCommit(true);
            }
            catch (SQLException e){}
        }
    }

    /**
     * @return 1: OK
     * -1: Already exists
     * -2: SQL Exception
     * -3: Wrong parameters
     */
    public int delete(int name) {
        try {

            //String sqlPrep = "DELETE FROM "+CURRENT_TABLE+" WHERE "+CURRENT_TABLE_DELETE_ARGUMENTS+" = ?";
            String sqlPrep = "UPDATE "+CURRENT_TABLE+" SET active = FALSE WHERE "+CURRENT_TABLE_DELETE_ARGUMENTS+" = ?";
            PreparedStatement prep = sql.connection.prepareStatement(sqlPrep);
            prep.setInt(1,name);
            int row = prep.executeUpdate();

            if(row == 0)
                return -1;

            return 1;
        }
        catch (SQLException e){return -2;}
    }

    public static void main(String[]args){

        SQL sql = new SQL();
        CustomerManager c = new CustomerManager(sql);

        // c.edit(1,"Grandma","Grandma@gmail.com",34567656);
        //  System.out.println(ingredient.generate("Ost",100,10,false,false,true,"","2016-03-15","2016-03-16"));
        //c.generate("Ostost","Swag@gmail.com",145678);
        // c.delete("Grandma");
        //System.out.println(c.delete("Microsoft"));
       // System.out.println(c.edit("Cheese",10,200,""));
        int i = c.generate("Per","olavh96@gmail.com",93240605);
        System.out.println(i);
    }
}
