package backend;

import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Faiter119 on 18.03.2016.
 * Creates links between databses by inserting into the help-tables
 */
public class LinkManager {

    private SQL sql;
    private String food_ingredient = "HCL_food_ingredient";
    private String order_food = "HCL_order_food";
    private String order_package = "HCL_order_pacakge";
    private String subscription_dates = "HCL_subscription_dates";

    public LinkManager(SQL sql) {
        this.sql = sql;
    }

    /**
     * @return 1: OK
     * -1: Already exists
     * -2: SQL Exception / Foreign key constraint fail
     * -3: Wrong parameters
     */
    public int generate(String table, String PK1, String PK2, int value1, int value2){

      //  if(sql.rowExists(table,PK1,value1) && sql.rowExists(table,PK2,value2)) return -1;
        if(table.trim().equals("") || PK1.trim().equals("") || PK2.trim().equals("") || value1 <0 || value2 <0) return -3;
        String sqlPrep = "Insert into "+table+"("+PK1+", "+PK2+") values(?,?)";

        try {
            PreparedStatement prep = sql.connection.prepareStatement(sqlPrep);
            // prep.setString(1,PK1);
            //prep.setString(2,PK2);
            prep.setInt(1,value1);
            prep.setInt(2,value2);
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

        if(!sql.rowExists(table,PK1,value1) || !sql.rowExists(table,PK2,value2)) return -1;

        if(table.trim().equals("") || PK1.trim().equals("") || PK2.trim().equals("") || value1 <0 || value2 <0) return -3;

        String sqlPrep = "Delete from "+table+" where "+PK1+" = ? AND "+PK2+" = ?";
        try {
            PreparedStatement prep = sql.connection.prepareStatement(sqlPrep);
            prep.setString(1,PK1);
            prep.setInt(2,value1);
            prep.setString(3,PK2);
            prep.setInt(4,value2);
            System.out.println(prep.toString());
            boolean i =  prep.execute();
            System.out.println(i);


            return 1;
        }
        catch (SQLException e){return -2;}
    }

    public static void main(String[]args){

        File file = null;
        try {
            file = new File(LinkManager.class.getResource("Database.ini").toURI().getPath(), true);
        }
        catch (URISyntaxException e){}
        Logon logon = new Logon(file);
        SQL sql = new SQL(logon);
        LinkManager link = new LinkManager(sql);

        int i =  link.generate("HCL_food_ingredient","food_id","ingredient_id",2,3); //TODO: Integrate this into the UI somehow via some lists mby
        System.out.println(i);
        link.delete("HCL_food_ingredient","food_id","ingredient_id",1,3);
        //DELETE FROM HCL_food_ingredient WHERE food_id = 1 AND ingredient_id = 2

        //link.delete("HCL_food_ingredient","food_id","ingredient_id",3,3);
        //link.generate();

    }
}
