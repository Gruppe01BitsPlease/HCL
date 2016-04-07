package backend;

import javax.xml.transform.Result;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

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
     * TODO: Make from scratch with prepared statements..
     */
    public String[][] getShoppinglist(int interval){

        String prepString = "";
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();

        try {
            PreparedStatement prep = sql.connection.prepareStatement(prepString);
            prep.setInt(1,interval);

            ResultSet res = prep.executeQuery();
            ResultSetMetaData meta = res.getMetaData();

            int row = -1;
            int colomns = meta.getColumnCount();

            while(res.next()){
                row++;
                for(int i = 0; i<colomns; i++) {
                    list.get(row).add(res.getString(i));
                }
            }


            String[][] out = new String[colomns][row];

            for(int i=0; i<list.size(); i++){

                for(int j=0; j<colomns; j++){

                    out[i][j] = list.get(i).get(j);

                }

            }
            return out;
        }
        catch (SQLException e){return null;}
    }


    public static void main(String[]args){

        //
        int lowestStock = 5;

        SQL sql = new SQL();

        String[][] list = sql.getStringTable("Select * from HCL_food NATURAL JOIN HCL_food_ingredient NATURAL JOIN " +
                "HCL_ingredient", false);

        sql.print2dArray(list);

    }

}
