package backend;

import javax.xml.transform.Result;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Faiter119 on 01.04.2016.
 */
public class Shoppinglist {

    private SQL sql;

    public Shoppinglist(){
        this.sql = new SQL();
    }

    /**
     * @return Shopping list as String[]
     * @param interval Days from now until end of shopping list
     */
    public String[][] getShoppinglist(int interval){

        String prepString = "SELECT ingredient_id,`Ingredient Name`,sum(`Total to buy this date`)-Stock FROM " +
                "ingredients_to_buy_summed WHERE `Total to buy this date`-Stock > 0 AND delivery_date " +
                "BETWEEN CURDATE() AND CURDATE() + INTERVAL ? DAY GROUP BY ingredient_id;";

                ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();

        try {
            PreparedStatement prep = sql.connection.prepareStatement(prepString);
            prep.setInt(1,interval);

            System.out.println(prep.toString());

            ResultSet res = prep.executeQuery();
            ResultSetMetaData meta = res.getMetaData();

            int row = 0;
            int colomns = meta.getColumnCount();

            while(res.next()){
                row++;
                list.add(new ArrayList<String>());

                for(int i = 1; i<=colomns; i++) {
                    list.get(row-1).add(res.getString(i));
                }
            }

            String[][] out = new String[list.size()][];

            for (int i = 0; i < list.size(); i++) {
                ArrayList<String> rad = list.get(i);
                out[i] = rad.toArray(new String[rad.size()]);
            }
            return out;
        }
        catch (SQLException e){
            return null;
        }
    }

    /**
     * Adds all the stock required from the shopping list to the stock.
     * @return
     *  1: OK
     * -1: Does not exists
     * -2: SQL Exception
     * -3: Wrong parameters
     */
    public int addShoppinglist(String[][] shoppinglist){

        String[][] list = shoppinglist.clone();
        IngredientManager manager = new IngredientManager(sql);
        int out = 1;
        try {
            sql.connection.setAutoCommit(false);

            for (String[] row : list) {
                out = manager.addStock(Integer.parseInt(row[0]), Integer.parseInt(row[2]));
                if (out < 0) return out; // Hvis vi får en feilkode
            }
            sql.connection.commit();
            return out;
        }
        catch (SQLException e){
            try {
                sql.connection.rollback();
                return -2;
            }
            catch (SQLException f){}
        }
        catch (NumberFormatException f){
            try {
                sql.connection.rollback();
                return -3;
            }
            catch (SQLException d){}
        }
        finally {
            try{
                sql.connection.setAutoCommit(true);
            }
            catch (SQLException g){}
        }
        return out;
     }

    /**
     * Adds the spesified amount from that index to the DB
     * If you update the table it will be different!
     *
     * @return
     *  1: OK
     * -1: Does not exists
     * -2: SQL Exception
     * -3: Wrong parameters
     */
    public int add(String[][] shoppinglist, int index){

        String[] table = shoppinglist[index];

        try {
            int ingredient_id = Integer.parseInt(table[0]);
            int amount = Integer.parseInt(table[2]);

            IngredientManager manager = new IngredientManager(sql);

            return manager.addStock(ingredient_id, amount);

        }
        catch (NumberFormatException e){return -3;}

    }

    public static void main(String[]args){

        SQL sql = new SQL();
        Shoppinglist list = new Shoppinglist();

        String[][] array = list.getShoppinglist(2000);

        sql.print2dArray(array);

        /*int add = list.addShoppinglist(array);
        System.out.println(add);

        String[][] array2 = list.getShoppinglist(30);
        sql.print2dArray(array2);*/

        //list.addShoppinglist(365); // Adds the contents of the spesified shopping list to the database
    }
}
