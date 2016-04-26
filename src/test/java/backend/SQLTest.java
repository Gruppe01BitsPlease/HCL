package backend;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.*;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by bahafeld on 31.03.2016...
 */
public class SQLTest {
    SQL sql;
    CustomerManager manager;
    FoodManager fManager;
    LinkManager lManager;
    IngredientManager iManager;
    OrderManager oManager;

    @Before
    public void setUp() throws Exception {
        sql = new SQL();
        manager = new CustomerManager(sql);
        fManager = new FoodManager(sql);
        lManager = new LinkManager(sql);
        iManager = new IngredientManager(sql);
        oManager = new OrderManager(sql);



    }

    @After
    public void tearDown() throws Exception {
        manager = null;
        fManager = null;
        lManager = null;
        iManager = null;
        oManager = null;
        sql.end();
    }




    @Test
    public void update() throws Exception {
        int cus = manager.generate("Kine-Sture", "Sturejenta@hotmail.com", 75584788);
        String cusID = cus + "";
        String førSetning = "SELECT * from HCL_customer where customer_id = " + cusID;
        String[][] utskrift1  = sql.getStringTable(  førSetning , false  );
        for(int i = 0; i < utskrift1[0].length; i++){
            System.out.println(utskrift1[0][i]);
        }

        sql.update("HCL_customer", "customer_name", "customer_id", cusID, "Kine");

        String etterSetning = "SELECT * from HCL_customer where customer_id = " + cusID;
        String[][] utskrift2  = sql.getStringTable(  etterSetning , false  );
        for(int i = 0; i < utskrift2[0].length; i++){
            System.out.println(utskrift2[0][i]);
        }
        sql.deleteForGood("HCL_customer", "customer_id", cus);

    }

    @Test
    public void update1() throws Exception {

        int cus2ID = manager.generate("Kine-Sture", "Sturejenta@hotmail.com", 75584788);
        String førSetning = "SELECT * from HCL_customer where customer_id = " + cus2ID;
        String[][] utskrift1  = sql.getStringTable(  førSetning , false  );
        for(int i = 0; i < utskrift1[0].length; i++){
            System.out.println(utskrift1[0][i]);
        }

        sql.update("HCL_customer", "customer_name", "customer_id", "Kine", cus2ID);

        String etterSetning = "SELECT * from HCL_customer where customer_id = " + cus2ID;
        String[][] utskrift2  = sql.getStringTable(  etterSetning , false  );
        for(int i = 0; i < utskrift2[0].length; i++){
            System.out.println(utskrift2[0][i]);
        }

        sql.deleteForGood("HCL_customer", "customer_id", cus2ID);

    }

    @Test
    public void update2() throws Exception {


        int cus3ID = manager.generate("Trinemor", "Trinemor@hotmail.com", 75584788);
        int orderID = oManager.generate(cus3ID, 123, "Elgeseter", 7030, "2017-02-02");
        LocalDate date = LocalDate.parse("2019-01-01");

        String førSetning = "SELECT * from HCL_order where order_id = " + orderID;
        String[][] utskrift1  = sql.getStringTable(  førSetning , false  );
        for(int i = 0; i < utskrift1[0].length; i++){
            System.out.println(utskrift1[0][i]);
        }

        sql.update("HCL_order", "order_date","order_id", orderID + "", Date.valueOf(date));

        String etterSetning = "SELECT * from HCL_order where order_id = " + orderID;
        String[][] print2  = sql.getStringTable(  etterSetning , false  );
        sql.print2dArray(print2);

        sql.deleteForGood("HCL_order", "order_id", orderID);
        sql.deleteForGood("HCL_customer", "customer_id", cus3ID);


    }

    @Test
    public void update3() throws Exception {

        int ingID = iManager.generate("Brunost", 5, 56, false, false, false, "kul mat", "2016-04-04", "2017-05-06");
        String førSetning = "SELECT nuts from HCL_ingredient where ingredient_id = " + ingID;
        String[][] utskrift1  = sql.getStringTable(  førSetning , false  );
        for(int i = 0; i < utskrift1[0].length; i++){
            System.out.println(utskrift1[0][i]);
        }

        sql.update("HCL_ingredient", "nuts","ingredient_id", ingID + "", true);

        String etterSetning = "SELECT nuts from HCL_ingredient where ingredient_id = " + ingID;
        String[][] utskrift2  = sql.getStringTable(  etterSetning , false  );
        for(int i = 0; i < utskrift2[0].length; i++){
            System.out.println(utskrift2[0][i]);
        }

        sql.deleteForGood("HCL_ingredient", "ingredient_id", ingID);

    }

    @Test
    public void rowExists() throws Exception {

        assertFalse(sql.rowExists("HCL_customer", "customer_id", 99999999));
        int cus4ID = manager.generate("Ylva", "ylvan@hotmail.com", 75584788);
        assertTrue(sql.rowExists("HCL_customer", "customer_id", cus4ID));

        sql.deleteForGood("HCL_customer", "customer_id", cus4ID);

    }

    @Test
    public void rowExists1() throws Exception {
        int ing2ID = iManager.generate("stein", 5, 56, false, false, true, "kun en", "2016-04-04", "2017-05-06");
        int foodID =  fManager.generate("bøtte", 60);
        assertFalse(sql.rowExists("HCL_food_ingredient", "food_id","ingredient_id", foodID, ing2ID));
        fManager.addIngredient(foodID,ing2ID,10);
        assertTrue(sql.rowExists("HCL_food_ingredient", "food_id","ingredient_id", foodID, ing2ID));

        sql.deleteForGood("HCL_food_ingredient", "food_id","ingredient_id", foodID, ing2ID);
        sql.deleteForGood("HCL_food", "food_id", foodID);
        sql.deleteForGood("HCL_ingredient", "ingredient_id", ing2ID);

    }

    @Test
    public void rowExists2() throws Exception {
        assertFalse(sql.rowExists("HCL_customer", "customer_id", 99999999));
        int cus5ID = manager.generate("Ylva", "ylvan@hotmail.com", 75584788);
        String cus5 = cus5ID + "";
        assertTrue(sql.rowExists("HCL_customer", "customer_id", cus5));

        sql.deleteForGood("HCL_customer", "customer_id", cus5ID);

    }

    @Test
    public void getStringTable() throws Exception {
        int cus6ID  = manager.generate("Ida", "mail", 98765432);
        String query = "SELECT customer_name from HCL_customer where customer_id = " + cus6ID;
        String[][] utskrift  = sql.getStringTable(  query , false  );
        sql.print2dArray(utskrift);
        utskrift = sql.getStringTable(  query , true  );
        sql.print2dArray(utskrift);
        sql.deleteForGood("HCL_customer", "customer_id", cus6ID);
    }

    @Test
    public void getColumnNames() throws Exception {
        int cus7ID  = manager.generate("Oda", "mail", 98765432);
        String query = "SELECT customer_name from HCL_customer where customer_id = " + cus7ID;
        String[] utskrift  = sql.getColumnNames( query );
        for (int i = 0; i < utskrift.length; i++){
            System.out.println(utskrift[i]);
        }
        sql.deleteForGood("HCL_customer", "customer_id", cus7ID);

    }



    @Test
    public void print2dArray() throws Exception {
        int cus8ID  = manager.generate("Julie", "mail", 98765432);
        String query = "SELECT * from HCL_customer where customer_id = " + cus8ID;
        String[][] utskrift  = sql.getStringTable(  query , true  );
        sql.print2dArray(utskrift);
        sql.deleteForGood("HCL_customer", "customer_id", cus8ID);


    }

    @Test
    public void getLastID(){
        int makeID  = manager.generate("Julie", "mail", 98765432);
        int lastID = sql.getLastID();
        assertEquals(makeID, lastID);
        sql.deleteForGood("HCL_customer", "customer_id", makeID);

    }

    @Test
    public void getColumn(){

        int cus9ID  = manager.generate("Oda", "mail", 98765432);
        String query = "SELECT * from HCL_customer where customer_id = " + cus9ID;
        String[] utskrift  = sql.getColumn( query , 1);
        for (int i = 0; i < utskrift.length; i++){
            System.out.println(utskrift[i]);
        }
        sql.deleteForGood("HCL_customer", "customer_id", cus9ID);


    }

    @Test
    public void getRow(){

        int cus10ID  = manager.generate("Rutt", "mail", 98765432);
        String query = "SELECT * from HCL_customer where customer_id = " + cus10ID;
        String[] utskrift  = sql.getRow( query );
        for (int i = 0; i < utskrift.length; i++){
            System.out.println(utskrift[i]);
        }
        sql.deleteForGood("HCL_customer", "customer_id", cus10ID);


    }

    @Test
    public void deleteForGood(){
        int ingID = iManager.generate("tran", 1, 60, false, false, false, "tas med teskje", "2017-06-06", "2018-06-06");
        int ing2ID = iManager.generate("nordlands", 1, 56, false, true, false, "vitaminer", "2017-06-06", "2018-06-06");

        //Sjekker at tran finnes, sletter tran, og forsikrer seg om at tran er slettet
        assertTrue(sql.rowExists("HCL_ingredient", "name", "tran"));
        sql.deleteForGood("HCL_ingredient", "ingredient_id", ingID);
        assertFalse(sql.rowExists("HCL_ingredient", "name", "tran"));

        sql.deleteForGood("HCL_ingredient", "ingredient_id", ing2ID);




    }

    @Test
    public void deleteForGood2(){

        int ingID = iManager.generate("kakestrø", 5, 56, false, false, true, "æsj", "2016-04-04", "2017-05-06");
        int foodID =  fManager.generate("kake", 60);
        lManager.generate("HCL_food_ingredient", "food_id","ingredient_id", foodID, ingID, 20);


        //Sjekker at kake-kakestrø finnes, sletter kake-kaktrø, og forsikrer seg om at kake-kakestrø er slettet
        assertTrue(sql.rowExists("HCL_food_ingredient", "food_id","ingredient_id", foodID, ingID));
        sql.deleteForGood("HCL_food_ingredient", "food_id","ingredient_id", foodID, ingID);
        assertFalse(sql.rowExists("HCL_food_ingredient", "food_id","ingredient_id", foodID, ingID));



        sql.deleteForGood("HCL_ingredient", "ingredient_id", ingID);
        sql.deleteForGood("HCL_food", "food_id", foodID);

    }

}