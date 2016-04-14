package backend;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Faiter119 on 15.03.2016.
 */
public class FoodManager {

    private SQL sql;
    public static final String CURRENT_TABLE = "HCL_food";
    public static final String CURRENT_TABLE_LINK= "HCL_food_ingredient";
    public static final String CURRENT_TABLE_GENERATE_ARGUMENTS = "(name,price)";
    public static final String CURRENT_TABLE_PK = "(food_id)";
    public static final String CURRENT_TABLE_ADD_INGREDIENTS_ARGUMENTS = "(food_id, ingredient_id, number)";



    public FoodManager(SQL sql){
        this.sql = sql;
    }

    /**
     * @return
     *  1: OK
     * -2: SQL Exception
     * -3: Wrong parameters
     */
    public int generate(String name, int price) {

        if(!(name.trim().length() > 0) &&!(price >= 0)) return -3;

        try {
            String sqlPrep = "INSERT INTO "+CURRENT_TABLE+CURRENT_TABLE_GENERATE_ARGUMENTS+" VALUES(?,?)";
            PreparedStatement prep = sql.connection.prepareStatement(sqlPrep);

            prep.setString(1,name);
            prep.setInt(2,price);

            prep.executeUpdate();

            return sql.getLastID();
        }
        catch (SQLException e){return -2;}
    }

    /**
     * @return
     *  1: OK
     * -1: Did not exists
     * -2: SQL Exception
     * -3: Wrong parameters
     */
    public int delete(int id) {
        if(!sql.rowExists(CURRENT_TABLE,CURRENT_TABLE_PK,id)) return -1;

        try {
            String sqlPrep = "UPDATE "+CURRENT_TABLE+" SET active = FALSE WHERE "+CURRENT_TABLE_PK+" = ?";
            PreparedStatement prep = sql.connection.prepareStatement(sqlPrep);

            prep.setInt(1,id);

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
     * -4: Food or Ingredient does not exist
     */
    public int addIngredient(int food_id, int ingredient_id, int amount){

        LinkManager link = new LinkManager(sql);

        if(food_id <0 || ingredient_id <0 || amount <0 )return -3;
        if(!sql.rowExists(CURRENT_TABLE,"food_id",food_id) || !sql.rowExists("HCL_ingredient","ingredient_id",ingredient_id)) return -4;

        int result = link.generate(CURRENT_TABLE_LINK,"food_id","ingredient_id",food_id,ingredient_id,amount);

        return result;
    }

    public static void main(String[]args){

        SQL sql = new SQL();
        FoodManager food = new FoodManager(sql);

        System.out.println(food.addIngredient(216,1,3));
/*
        System.out.println(food.addIngredient(201,2,1));
        System.out.println(food.addIngredient(201,3,1));
        System.out.println(food.addIngredient(201,1,1));*/

        // System.out.println(food.delete(229));


    }
}
