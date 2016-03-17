package backend;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Faiter119 on 15.03.2016.
 */
public class FoodManager {

    private SQL sql;
    public static final String CURRENT_TABLE = "HCL_food";
    public static final String CURRENT_TABLE_GENERATE_ARGUMENTS = "(name,price)";
    public static final String CURRENT_TABLE_DELETE_ARGUMENTS = "(name)";


    public FoodManager(SQL sql){
        this.sql = sql;
    }

    /**
     * @return 1: OK
     * -1: Already exists
     * -2: SQL Exception
     * -3: Wrong parameters
     */
    public int generate(String name, int price) {

        if(!(name.trim().length() > 0) &&!(price >= 0)) return -3;

        if(sql.rowExists(CURRENT_TABLE,"name",name)) return -1;

        try {
            String sqlPrep = "INSERT INTO "+CURRENT_TABLE+CURRENT_TABLE_GENERATE_ARGUMENTS+" VALUES(?,?)";
            PreparedStatement prep = sql.connection.prepareStatement(sqlPrep);

            prep.setString(1,name);
            prep.setInt(2,price);

            prep.executeUpdate();
            return 1;
        }
        catch (SQLException e){return -2;}
    }

    /**
     * @return 1: OK
     * -1: Did not exists
     * -2: SQL Exception
     * -3: Wrong parameters
     */
    public int delete(String name) {
        if(!sql.rowExists(CURRENT_TABLE,"name",name)) return -1;

        try {
            String sqlPrep = "DELETE FROM "+CURRENT_TABLE+" WHERE "+CURRENT_TABLE_DELETE_ARGUMENTS+" = ?";
            PreparedStatement prep = sql.connection.prepareStatement(sqlPrep);
            prep.setString(1,name);
            prep.executeUpdate();
            return 1;
        }
        catch (SQLException e){return -2;}
    }

    public static void main(String[]args){
        File file = null;

        try {
            file = new File(FoodManager.class.getResource("Database.ini").toURI().getPath(), true);
        }
        catch (Exception e){}

        Logon logon = new Logon(file);
        SQL sql = new SQL(logon);
        FoodManager food = new FoodManager(sql);

       System.out.println( food.generate("Ost",10));
        System.out.println(food.delete("Ost"));
    }
}
