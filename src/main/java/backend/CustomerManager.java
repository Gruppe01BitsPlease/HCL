package backend;

import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 * Is responsible for managing customer entries in the database
 */
public class CustomerManager {

    private SQL sql;
    public static final String CURRENT_TABLE = "HCL_customer";
    public static final String CURRENT_TABLE_GENERATE_ARGUMENTS = "(customer_name, epost, tlf)";
    public static final String CURRENT_TABLE_PK = "(customer_id)";

    public CustomerManager(SQL sql){
        this.sql = sql;
    }

    /**
     * @return
     *  OK - ID of added element
     * -2: SQL Exception
     * -3: Wrong parameters
     */
    public int generate(String name, String epost, int tlf) {

        if(name.trim().length() <= 0 || tlf <= 0 || epost.trim().equals("")) return -3; //invalid parameters

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
            return -2;}
    }

    /**
     * @return
     *  1: OK
     * -1: Does not exists
     * -2: SQL Exception
     * -3: Wrong parameters
     */
    public int edit(int id, String nyEpost, int nyTlf){

        if (!sql.rowExists(CURRENT_TABLE, CURRENT_TABLE_PK, id)) return -1;
        if ( nyEpost.trim().equals("") || nyTlf <= 0) return -3;

        try {
            sql.connection.setAutoCommit(false);

            sql.update(CURRENT_TABLE, "epost", CURRENT_TABLE_PK, Integer.toString(id), nyEpost);
            sql.update(CURRENT_TABLE, "tlf", CURRENT_TABLE_PK, Integer.toString(id), nyTlf);

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
     * @return
     *  1: OK
     * -1: Does not exist/Wrong parameter
     * -2: SQL Exception
     */
    public int delete(int id) {
        if(!sql.rowExists(CURRENT_TABLE,CURRENT_TABLE_PK,id)) return -1;
        try {

            //String sqlPrep = "DELETE FROM "+CURRENT_TABLE+" WHERE "+CURRENT_TABLE_DELETE_ARGUMENTS+" = ?";
            String sqlPrep = "UPDATE "+CURRENT_TABLE+" SET active = FALSE WHERE "+CURRENT_TABLE_PK+" = ?";
            PreparedStatement prep = sql.connection.prepareStatement(sqlPrep);
            prep.setInt(1,id);
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
        int i = c.generate("Test","test@gmail.com",86132139);
        System.out.println(i);
        System.out.println(c.delete(i));
    }
}
