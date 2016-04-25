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
        sql.end();
        manager = null;
    }


    @Test
    public void query() throws Exception {
        //"INSERT INTO "+CURRENT_TABLE+CURRENT_TABLE_GENERATE_ARGUMENTS+" VALUES(?,?,?)
        //int idUlf = manager.generate("Ulf-Sture", "Sturegutten@hotmail.com", 75584788);
        //ResultSet rs = sql.query("SELECT customer_name FROM HCL_customer WHERE customer_id = " + idUlf);
        //Array a = rs.getArray(1);
        //String[] nullable = (String[])a.getArray();
        //sql.deleteForGood("HCL_customer", "customer_id", idUlf);

    }


    @Test
    public void update() throws Exception {
        int kine = manager.generate("Kine-Sture", "Sturejenta@hotmail.com", 75584788);
        String idKine = kine + "";
        String førSetning = "SELECT * from HCL_customer where customer_id = " + idKine;
        String[][] utskrift1  = sql.getStringTable(  førSetning , false  );
        for(int i = 0; i < utskrift1[0].length; i++){
            System.out.println(utskrift1[0][i]);
        }

        sql.update("HCL_customer", "customer_name", "customer_id", idKine, "Kine");

        String etterSetning = "SELECT * from HCL_customer where customer_id = " + idKine;
        String[][] utskrift2  = sql.getStringTable(  etterSetning , false  );
        for(int i = 0; i < utskrift2[0].length; i++){
            System.out.println(utskrift2[0][i]);
        }
        sql.deleteForGood("HCL_customer", "customer_id", kine);

    }

    @Test
    public void update1() throws Exception {

        int idKine = manager.generate("Kine-Sture", "Sturejenta@hotmail.com", 75584788);
        String førSetning = "SELECT * from HCL_customer where customer_id = " + idKine;
        String[][] utskrift1  = sql.getStringTable(  førSetning , false  );
        for(int i = 0; i < utskrift1[0].length; i++){
            System.out.println(utskrift1[0][i]);
        }

        sql.update("HCL_customer", "customer_name", "customer_id", "Kine", idKine);

        String etterSetning = "SELECT * from HCL_customer where customer_id = " + idKine;
        String[][] utskrift2  = sql.getStringTable(  etterSetning , false  );
        for(int i = 0; i < utskrift2[0].length; i++){
            System.out.println(utskrift2[0][i]);
        }

        sql.deleteForGood("HCL_customer", "customer_id", idKine);

    }

    @Test
    public void update2() throws Exception {


        int trineID = manager.generate("Trinemor", "Trinemor@hotmail.com", 75584788);
        int bestillingID = oManager.generate(trineID, 123, "Elgeseter", 7030, "2017-02-02");
        LocalDate date = LocalDate.parse("2019-01-01");

        String førSetning = "SELECT * from HCL_order where order_id = " + bestillingID;
        String[][] utskrift1  = sql.getStringTable(  førSetning , false  );
        for(int i = 0; i < utskrift1[0].length; i++){
            System.out.println(utskrift1[0][i]);
        }

        sql.update("HCL_order", "order_date","order_id", bestillingID + "", Date.valueOf(date));

        String etterSetning = "SELECT * from HCL_order where order_id = " + bestillingID;
        String[][] utskrift2  = sql.getStringTable(  etterSetning , false  );
        for(int i = 0; i < utskrift2[0].length; i++){
            System.out.println(utskrift2[0][i]);
        }


        sql.deleteForGood("HCL_order", "order_id", bestillingID);
        sql.deleteForGood("HCL_customer", "customer_id", trineID);


    }

    @Test
    public void update3() throws Exception {

        int ingredientID = iManager.generate("Brunost", 5, 56, false, false, false, "kul mat", "2016-04-04", "2017-05-06");
        String førSetning = "SELECT nuts from HCL_ingredient where ingredient_id = " + ingredientID;
        String[][] utskrift1  = sql.getStringTable(  førSetning , false  );
        for(int i = 0; i < utskrift1[0].length; i++){
            System.out.println(utskrift1[0][i]);
        }

        sql.update("HCL_ingredient", "nuts","ingredient_id", ingredientID + "", true);

        String etterSetning = "SELECT nuts from HCL_ingredient where ingredient_id = " + ingredientID;
        String[][] utskrift2  = sql.getStringTable(  etterSetning , false  );
        for(int i = 0; i < utskrift2[0].length; i++){
            System.out.println(utskrift2[0][i]);
        }

        sql.deleteForGood("HCL_ingredient", "ingredient_id", ingredientID);

    }

    @Test
    public void rowExists() throws Exception {

        assertFalse(sql.rowExists("HCL_customer", "customer_id", 99999999));
        int idYlva = manager.generate("Ylva", "ylvan@hotmail.com", 75584788);
        assertTrue(sql.rowExists("HCL_customer", "customer_id", idYlva));

        sql.deleteForGood("HCL_customer", "customer_id", idYlva);

    }

    @Test
    public void rowExists1() throws Exception {
        int stoneID = iManager.generate("stein", 5, 56, false, false, true, "kun en", "2016-04-04", "2017-05-06");
        int bucketID =  fManager.generate("bøtte", 60);
        assertFalse(sql.rowExists("HCL_food_ingredient", "food_id","ingredient_id", bucketID, stoneID));
        fManager.addIngredient(bucketID,stoneID,10);
        assertTrue(sql.rowExists("HCL_food_ingredient", "food_id","ingredient_id", bucketID, stoneID));

        sql.deleteForGood("HCL_food_ingredient", "food_id","ingredient_id", bucketID, stoneID);
        sql.deleteForGood("HCL_food", "food_id", bucketID);
        sql.deleteForGood("HCL_ingredient", "ingredient_id", stoneID);

    }

    @Test
    public void rowExists2() throws Exception {
        assertFalse(sql.rowExists("HCL_customer", "customer_id", 99999999));
        int idYlva = manager.generate("Ylva", "ylvan@hotmail.com", 75584788);
        String ylva = idYlva + "";
        assertTrue(sql.rowExists("HCL_customer", "customer_id", ylva));

        sql.deleteForGood("HCL_customer", "customer_id", idYlva);

    }

    @Test
    public void getStringTable() throws Exception {
        int ida  = manager.generate("Ida", "mail", 98765432);
        String query = "SELECT customer_name from HCL_customer where customer_id = " + ida;
        String[][] utskrift  = sql.getStringTable(  query , false  );
        sql.print2dArray(utskrift);
        utskrift = sql.getStringTable(  query , true  );
        sql.print2dArray(utskrift);
        sql.deleteForGood("HCL_customer", "customer_id", ida);
    }

    @Test
    public void getColumnNames() throws Exception {
        int oda  = manager.generate("Oda", "mail", 98765432);
        String query = "SELECT customer_name from HCL_customer where customer_id = " + oda;
        String[] utskrift  = sql.getColumnNames( query );
        for (int i = 0; i < utskrift.length; i++){
            System.out.println(utskrift[i]);
        }
        sql.deleteForGood("HCL_customer", "customer_id", oda);

    }



    @Test
    public void print2dArray() throws Exception {
        int julie  = manager.generate("Julie", "mail", 98765432);
        String query = "SELECT * from HCL_customer where customer_id = " + julie;
        String[][] utskrift  = sql.getStringTable(  query , true  );
        sql.print2dArray(utskrift);
        sql.deleteForGood("HCL_customer", "customer_id", julie);


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

        int oda  = manager.generate("Oda", "mail", 98765432);
        String query = "SELECT * from HCL_customer where customer_id = " + oda;
        String[] utskrift  = sql.getColumn( query , 1);
        for (int i = 0; i < utskrift.length; i++){
            System.out.println(utskrift[i]);
        }
        sql.deleteForGood("HCL_customer", "customer_id", oda);


    }

    @Test
    public void getRow(){

        int rutt  = manager.generate("Rutt", "mail", 98765432);
        String query = "SELECT * from HCL_customer where customer_id = " + rutt;
        String[] utskrift  = sql.getRow( query );
        for (int i = 0; i < utskrift.length; i++){
            System.out.println(utskrift[i]);
        }
        sql.deleteForGood("HCL_customer", "customer_id", rutt);


    }

    @Test
    public void deleteForGood(){
        int ingID = iManager.generate("tran", 1, 60, false, false, false, "tas med teskje", "2017-06-06", "2018-06-06");
        int ing2ID = iManager.generate("nordlands", 1, 56, false, true, false, "vitaminer", "2017-06-06", "2018-06-06");

        //Sjekker at tran finnes, sletter tran, og forsikrer seg om at tran er slettet
        assertTrue(sql.rowExists("HCL_ingredient", "name", "tran"));
        sql.deleteForGood("HCL_ingredient", "ingredient_id", ingID);
        assertFalse(sql.rowExists("HCL_ingredient", "name", "tran"));




    }

    @Test
    public void deleteForGood2(){

        int kakestrøID = iManager.generate("kakestrø", 5, 56, false, false, true, "æsj", "2016-04-04", "2017-05-06");
        int kakeID =  fManager.generate("kake", 60);
        lManager.generate("HCL_food_ingredient", "food_id","ingredient_id", kakeID, kakestrøID, 20);


        //Sjekker at kake-kakestrø finnes, sletter kake-kaktrø, og forsikrer seg om at kake-kakestrø er slettet
        assertTrue(sql.rowExists("HCL_food_ingredient", "food_id","ingredient_id", kakeID, kakestrøID));
        sql.deleteForGood("HCL_food_ingredient", "food_id","ingredient_id", kakeID, kakestrøID);
        assertFalse(sql.rowExists("HCL_food_ingredient", "food_id","ingredient_id", kakeID, kakestrøID));




        sql.deleteForGood("HCL_ingredient", "ingredient_id", kakestrøID);
        sql.deleteForGood("HCL_food", "food_id", kakeID);

    }

}