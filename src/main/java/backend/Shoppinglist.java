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

        String prepString = "SELECT * FROM ingredients_to_buy_over_zero WHERE delivery_date BETWEEN CURDATE() AND CURDATE() + INTERVAL ? DAY;";
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
    public int addShoppinglist(int interval){

        String[][] list = getShoppinglist(interval);
        IngredientManager manager = new IngredientManager(sql);

        for(String[] row : list){
            manager.edit(row[0],Integer.parseInt(row[1]),Integer.parseInt(row[3]));
        }
        return 1;
     }

    public static void main(String[]args){


        SQL sql = new SQL();
        Shoppinglist list = new Shoppinglist();

        String[][] array = list.getShoppinglist(365);

        System.out.println(Arrays.deepToString(array));

        for(String[] row : array){
            System.out.println();
            for(String colomn : row){
                System.out.print(colomn+" ");
            }

        }

    }

}
