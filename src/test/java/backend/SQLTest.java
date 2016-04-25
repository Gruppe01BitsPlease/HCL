package backend;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.*;

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

    @Before
    public void setUp() throws Exception {
        sql = new SQL();
        manager = new CustomerManager(sql);
        fManager = new FoodManager(sql);
        lManager = new LinkManager(sql);
        iManager = new IngredientManager(sql);



    }

    @After
    public void tearDown() throws Exception {
        sql.end();
        manager = null;
    }

    @Test
    public void end() throws Exception {
        int id = manager.generate("Kari", "hei@nei.no", 12345678);
        assertEquals(1, manager.edit(id, "ja@nei.no", 12345679));
        sql.end();
        assertEquals(-2, manager.generate("Kari", "hei@nei.no", 12345678));
        sql.connect();
        assertEquals(1, sql.deleteForGood("HCL_customer", "customer_id", id));

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

    }
    @Test
    public void update3() throws Exception {

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

    }

    @Test
    public void getColumnNames() throws Exception {

    }

    @Test
    public void arrayWithCorrectSize() throws Exception {

    }

    @Test
    public void print2dArray() throws Exception {

    }

}