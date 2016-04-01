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
    public static final String CURRENT_TABLE_DELETE_ARGUMENTS = "(name)";
    public static final String CURRENT_TABLE_ADD_INGREDIENTS_ARGUMENTS = "(food_id, ingredient_id, gram)";



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

    /**
     *
     * @return
     * 1: OK
     * -1: Already exist
     * -2: SQL Exception
     * -3: Wrong Parameters
     * -4: File not found
     */
    public int addIngredient(int food_id, int ingredient_id, int gram){
        // Init
        File file = null;
        try {
            file = new File(FoodManager.class.getResource("/Database.ini").toURI().getPath(), true);
        }
        catch (Exception e){}
        if (file == null) return -4;
        Logon logon = new Logon(file);
        SQL sql = new SQL(logon);
        LinkManager link = new LinkManager(sql);
        // End Init

        if(sql.rowExists("HCL_food_ingredient","food_id","ingredient_id",food_id,ingredient_id)) return -1;
        if(food_id <0 || ingredient_id <0 || gram <0 )return -3;

        String prepString = "Insert into "+CURRENT_TABLE_LINK+CURRENT_TABLE_ADD_INGREDIENTS_ARGUMENTS+" values(?,?,?)";
        try {
            sql.connection.setAutoCommit(false);

            PreparedStatement prep = sql.connection.prepareStatement(prepString);

            prep.setInt(1,food_id);
            prep.setInt(2,ingredient_id);
            prep.setInt(3,gram);

            prep.executeUpdate();

            sql.connection.commit();
            sql.connection.setAutoCommit(true);

            return 1;

        }
        catch (SQLException e){
            try{
                sql.connection.rollback();
            }
            catch (SQLException f){return -2;};
            return -2;
        }




    }

    public static void main(String[]args){
        File file = null;

        try {
            file = new File(FoodManager.class.getResource("/Database.ini").toURI().getPath(), true);
        }
        catch (Exception e){}

        Logon logon = new Logon(file);
        SQL sql = new SQL(logon);
        FoodManager food = new FoodManager(sql);

       System.out.println(food.addIngredient(200,1,200));

    }
}
