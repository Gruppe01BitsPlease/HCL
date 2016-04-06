package backend;

/**
 * Created by Faiter119 on 01.04.2016.
 */
public class Shoppinglist {

    public Shoppinglist(){

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
