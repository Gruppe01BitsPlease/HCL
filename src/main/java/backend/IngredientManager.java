package backend;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Is responsible for managing ingredient entries in the database
 */
public class IngredientManager {

    private SQL sql;
    public static final String CURRENT_TABLE = "HCL_ingredient";
    public static final String CURRENT_TABLE_GENERATE_ARGUMENTS = "(name, stock, purchase_price, nuts, gluten, lactose, other, purchase_date, expiration_date)";
    public static final String CURRENT_TABLE_PK = "(ingredient_id)";

    public IngredientManager(SQL sql){
        this.sql = sql;
    }

    /**
     * @return ID: OK
     * -1: Already exists
     * -2: SQL Exception
     * -3: Wrong parameters / Wrong Date format yyyy-MM-dd
     */
    public int generate(String name, int stock, int purchase_price, boolean nuts, boolean gluten, boolean lactose, String other, String purchase_date, String expiration_date) {

        if(!(name.trim().length() > 0) || !(stock >= 0) || !(purchase_price>=0) ) return -3;
        if(sql.rowExists(CURRENT_TABLE,"name",name)) return -1;

        try {
            Date date1 = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(purchase_date).getTime());
            Date date2 = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(expiration_date).getTime());

            String sqlPrep = "INSERT INTO "+CURRENT_TABLE+CURRENT_TABLE_GENERATE_ARGUMENTS+" VALUES(?,?,?,?,?,?,?,?,?)";
            PreparedStatement prep = sql.connection.prepareStatement(sqlPrep);

            prep.setString(1,name);
            prep.setInt(2,stock);
            prep.setInt(3,purchase_price);
            prep.setBoolean(4,nuts);
            prep.setBoolean(5,gluten);
            prep.setBoolean(6,lactose);
            prep.setString(7,other);
            prep.setDate(8,date1);
            prep.setDate(9,date2);

            prep.executeUpdate();
            return sql.getLastID();
        }
        catch (SQLException e){return -2;}
        catch (ParseException e){return -3;}
    }

    /**
     * @return 1: OK
     * -1: Does not exist
     * -2: SQL Exception
     * -3: Wrong parameters
     */
    public int edit(int id, int newStock,int newPurchase_price, String newOther){

        if (!sql.rowExists(CURRENT_TABLE,CURRENT_TABLE_PK, id)) return -1;
        if (!(newStock >= 0)|| !(newPurchase_price >= 0) || (newOther.trim().equals(""))){return -3;}

        try {
            sql.connection.setAutoCommit(false);

            if ((newStock >= 0)) sql.update(CURRENT_TABLE, "stock", CURRENT_TABLE_PK, Integer.toString(id), newStock);
            if ((newPurchase_price >= 0)) sql.update(CURRENT_TABLE, "purchase_price", CURRENT_TABLE_PK, Integer.toString(id), newPurchase_price);
            if (!newOther.trim().equals("")) sql.update(CURRENT_TABLE, "other", CURRENT_TABLE_PK, Integer.toString(id), newOther);

            sql.connection.commit();
            return 1;
        }
        catch (SQLException e){
            try {
                sql.connection.rollback();
                return -2;
            }
            catch (SQLException f){return -2;}
        }
        finally {
            try {
                sql.connection.setAutoCommit(true);
            }
            catch (SQLException f){}
        }
    }
    /**
     * ADDS to the current stock!!
     *
     * @return
     *  1: OK
     * -1: Does not exists
     * -2: SQL Exception
     * -3: Wrong parameters
     */
    public int addStock(int ingredient_id,int addStock){

        if(!sql.rowExists(CURRENT_TABLE,CURRENT_TABLE_PK,ingredient_id)) return -1;
        if((addStock == 0))return -3;

        try {
            sql.connection.setAutoCommit(false);

            String prepString = "Select stock from HCL_ingredient where ingredient_id = ?;";

            PreparedStatement prep = sql.connection.prepareStatement(prepString);
            prep.setInt(1,ingredient_id);

            ResultSet res = prep.executeQuery();
            res.next();
            int currentStock = res.getInt(1);

            sql.update(CURRENT_TABLE,"stock",CURRENT_TABLE_PK, Integer.toString(ingredient_id),currentStock+addStock);
            sql.connection.commit();
            return 1;
        }
        catch (Exception e){
            try {
                sql.connection.rollback();
            }catch (SQLException i){}
            System.out.println(e.toString());
            return -2;

        }
        finally {
            try {
                sql.connection.setAutoCommit(true);
            }catch (SQLException i){}
        }

    }

    /**
     * Reverses the amount and uses addStock ^v^
     * @return
     *  1: OK
     * -1: Does not exists
     * -2: SQL Exception
     * -3: Wrong parameters
     */
    public int removeStock(int ingredient_id, int amount){

        if(!sql.rowExists(CURRENT_TABLE,CURRENT_TABLE_PK,ingredient_id)) return -1;

        String setning = "SELECT stock from HCL_ingredient where ingredient_id = " + ingredient_id;
        String[][] utskrift  = sql.getStringTable(  setning , false  );
        int currentStock = Integer.parseInt(utskrift[0][0]);
        if ((currentStock < amount) || (amount == 0)) return -3;

        int out;
        if(amount > 0) {
            out = addStock(ingredient_id, -amount);
        }
        else out = addStock(ingredient_id, amount);

        return out;
    }

    /**
     * @return 1: OK
     * -1: Did not exists
     * -2: SQL Exception
     * -3: Wrong parameters
     */
    public int delete(int id) {
        try {
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
        IngredientManager ingredient = new IngredientManager(sql);

      //  System.out.println(ingredient.generate("Ost",100,10,false,false,true,"","2016-03-15","2016-03-16"));
      //  System.out.println(ingredient.delete("Ost"));
        //System.out.println(ingredient.edit("Cheese",5,100,"Dropped it on the floor"));
        //System.out.println(ingredient.addStock(1,5));

        // Comment color: 63,155,155 rgb
    }
}
