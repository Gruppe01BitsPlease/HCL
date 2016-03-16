package backend;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Faiter119 on 15.03.2016.
 */
public class FoodManager {

    private SQL sql;

    public FoodManager(SQL sql){
        this.sql = sql;
    }

    public boolean generate(String name, String cost) {

        try {

            String sqlStatement = "INSERT INTO HCL_food(food_name, food_cost) ";
            PreparedStatement prep = sql.connection.prepareStatement(sqlStatement);


            prep.executeUpdate();
        }
        catch (SQLException e){return false;}

        return false;
    }


    public boolean edit() {
        return false;
    }


    public boolean delete() {
        return false;
    }
}
