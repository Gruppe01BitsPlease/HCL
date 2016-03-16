package backend;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Faiter119 on 15.03.2016.
 */
public class FoodManager {

    private SQL sql;
    public  static final String CURRENT_TABLE = "HCL_food";
    public static final String CURRENT_TABLE_ARGUMENTS = "(name,price)";

    public FoodManager(SQL sql){
        this.sql = sql;
    }

    public boolean generate(String name, int price) {

        if(!(name.trim().length() > 0) &&!(price >= 0)) return false;

        try {
            String sqlPrep = "INSERT INTO "+CURRENT_TABLE+" "+CURRENT_TABLE_ARGUMENTS+" VALUES(?,?)";
            PreparedStatement prep = sql.connection.prepareStatement(sqlPrep);

            prep.setString(1,name);
            prep.setInt(2,price);

            prep.executeUpdate();
            return true;
        }
        catch (SQLException e){return false;}
    }


    public boolean delete(String name) {
        try {
            String sqlPrep = "DELETE FROM HCL_food WHERE name = ?";
            PreparedStatement prep = sql.connection.prepareStatement(sqlPrep);
            prep.setString(1,name);
            prep.executeUpdate();
            return true;
        }
        catch (SQLException e){return false;}
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

        food.generate("Ost",10);
        food.delete("Ost");
    }
}
