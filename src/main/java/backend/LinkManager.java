package backend;

import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Olav Husby on 18.03.2016.
 * Creates links between databses by inserting into the help-tables
 */
public class LinkManager {

    private SQL sql;

    public static String food_ingredient = "HCL_food_ingredient";
    public static String order_food = "HCL_order_food";
    public static String order_package = "HCL_order_pacakge";
    public static String subscription_dates = "HCL_subscription_dates";

    public LinkManager(SQL sql) {
        this.sql = sql;
    }

    /**
     * @return
     *  1: OK
     * -1: Already exists
     * -2: SQL Exception / Foreign key constraint fail
     * -3: Wrong parameters
     */
    public int generate(String table, String PK1, String PK2, int value1, int value2, int amount){

        //if(sql.rowExists(table,PK1,value1) && sql.rowExists(table,PK2,value2)) return -1;
        if(table.trim().equals("") || PK1.trim().equals("") || PK2.trim().equals("") || value1 <0 || value2 <0) return -3;
        String sqlPrep = "Insert into "+table+"("+PK1+", "+PK2+",number) values(?,?,?)";

        try {
            PreparedStatement prep = sql.connection.prepareStatement(sqlPrep);
            // prep.setString(1,PK1);
            //prep.setString(2,PK2);
            prep.setInt(1,value1);
            prep.setInt(2,value2);
            prep.setInt(3,amount);
            prep.execute();
            return 1;
        }
        catch (SQLException e){return -2;} //Error in syntax
    }
    /**
     * @return 1: OK
     * -1: Does not exist
     * -2: SQL Exception
     * -3: Wrong parameters
     */
    public int delete(String table,String PK1, String PK2, int value1, int value2){

        if(!sql.rowExists(table,PK1,PK2,value1,value2)) return -1;

        String sqlPrep = "Update "+table+" SET active = 0 WHERE "+PK1+" = ? AND "+PK2+" = ?;";

        try {
            PreparedStatement prep = sql.connection.prepareStatement(sqlPrep);
            prep.setInt(1,value1);
            prep.setInt(2,value2);

            boolean success =  prep.execute();

            return success ? 1 : -2;
        }
        catch (SQLException e) {
            System.out.println(e.toString());
            return -2;
        }
    }

    /**
     * @return
     *  1: OK
     * -1: Does not exist
     * -2: SQL Exception
     * -3: Wrong parameters
     */
    public int editNumber(String table, String PK1, String PK2, int value1, int value2, int newNumber){

        if(!sql.rowExists(table,PK1,PK2,value1,value2)) return -1;

        String prepString = "UPDATE "+table+" SET number = ? WHERE "+PK1+" = ? AND "+PK2+" = ?;";

        try{
            PreparedStatement prep = sql.connection.prepareStatement(prepString);

            prep.setInt(1,newNumber);
            prep.setInt(2,value1);
            prep.setInt(3,value2);

            return prep.executeUpdate();

        }
        catch (SQLException e){return -2;}

    }

    public static void main(String[]args){

        SQL sql = new SQL();
        LinkManager link = new LinkManager(sql);
/*
        int i =  link.generate("HCL_food_ingredient","food_id","ingredient_id",2,3); //TODO: Integrate this into the UI somehow via some lists mby
        System.out.println(i);*/
        // link.delete("HCL_food_ingredient","food_id","ingredient_id",1,3);
        //DELETE FROM HCL_food_ingredient WHERE food_id = 1 AND ingredient_id = 2

        //link.delete("HCL_food_ingredient","food_id","ingredient_id",3,3);
        //link.generate();
        //link.editNumber("HCL_food_ingredient","food_id","ingredient_id",200,1,5);
        System.out.println(link.delete("HCL_food_ingredient","food_id","ingredient_id",207,31));

    }
}
