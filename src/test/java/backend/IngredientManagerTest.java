package backend;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by trineliseolsen on 13.04.2016.
 */

public class IngredientManagerTest {

    IngredientManager manager;
    SQL sql = new SQL();


    @Before
    public void setUp() throws Exception {
        manager = new IngredientManager(sql);
    }


    @After
    public void tearDown() throws Exception {
        manager = null;
        sql.end();
    }

    @Test
    public void generate() throws Exception{

        //making test-objects
        int ingID = manager.generate("Brunost", 5, 56, false, false, true, "kul mat", "2016-04-04", "2017-05-06");
        assertTrue(sql.rowExists("HCL_ingredient", "ingredient_id", ingID));
        assertFalse(sql.rowExists("HCL_ingredient", "ingredient_id", 010101));


        //Checking if fake objects return correctly
        assertEquals(-3,manager.generate (" ", 5, 56, false, false, true, "mac er best", "2016-04-04", "2017-05-06"));
        assertEquals(-3, manager.generate("Kyllingvinge ", -1, 40, false, false, true, "mac er best", "2016-04-04", "2017-05-06"));

        //checking that an object can't be duplicated.
        int ing2ID = manager.generate("Rødost", 5, 56, false, false, true, "mac er best", "2016-04-04", "2017-05-06");
        assertEquals(-1, manager.generate("Rødost", 5, 56, false, false, true, "mac er best", "2016-04-04", "2017-05-06"));

        sql.deleteForGood("HCL_ingredient", "ingredient_id", ingID);
        sql.deleteForGood("HCL_ingredient", "ingredient_id", ing2ID);

    }

    @Test
    //Making test-objects
    public void edit() throws Exception {
        int ing3ID = manager.generate("riesling cabinett", 1, 104, false, true, false, "hei", "2017-06-06", "2018-06-06");
        int ing4ID = manager.generate("nordlands", 1, 32, false, true, false, "hei", "2017-06-06", "2018-06-06");

        //printing information before and after edit.
        String førSetning = "SELECT * from HCL_ingredient where ingredient_id = " + ing3ID;
        String[][] utskrift1  = sql.getStringTable(  førSetning , false  );
        for(int i = 0; i < utskrift1[0].length; i++){
            System.out.println(utskrift1[0][i]);
        } //printing "id, riesling cabinett, 1, 104, 0, 1, 0, hei, 2017-06-06, 2018-06-06"


            manager.edit(ing3ID, 2,99, "god vin!");

            String etterSetning = "SELECT * from HCL_ingredient where ingredient_id = " + ing3ID;
            String[][] utskrift2  = sql.getStringTable(  etterSetning , false  );
            for(int i = 0; i < utskrift2[0].length; i++){
                System.out.println(utskrift2[0][i]);
            } //printing "id, riesling cabinett, 2, 99, 0, 1, 0, god vin!, 2017-06-06, 2018-06-06"

            //Testing if right return when wrong parameters
            assertEquals(-1, manager.edit(99999999, 10, 80, "hallo"));
            assertEquals(-3, manager.edit(ing4ID, -1, 80, "hallo" ));
            assertEquals(-3, manager.edit(ing4ID, 10, -1, "hallo" ));
            assertEquals(-3, manager.edit(ing4ID, 10, 80, "" ));


        sql.deleteForGood("HCL_ingredient", "ingredient_id", ing3ID);
        sql.deleteForGood("HCL_ingredient", "ingredient_id", ing4ID);
        }



   @Test
    public void delete() throws Exception {
        //making test-objects
       int ing5ID = manager.generate("tran", 1, 60, false, false, false, "tas med teskje", "2017-06-06", "2018-06-06");
       int ing6ID = manager.generate("nordlands", 1, 56, false, true, false, "vitaminer", "2017-06-06", "2018-06-06");

        //checks that objects exists, deletes it and checks again.
        assertTrue(sql.rowExists("HCL_ingredient", "name", "tran"));
        manager.delete(ing5ID);
        assertFalse(sql.rowExists("HCL_ingredient", "name", "tran"));

        //checking if deleting gets right return.
        assertEquals(1, manager.delete(ing6ID));
        assertEquals(-1, manager.delete(99999999));

       sql.deleteForGood("HCL_ingredient", "ingredient_id", ing6ID);
       sql.deleteForGood("HCL_ingredient", "ingredient_id", ing5ID);

    }

    @Test
    public void addStock() throws Exception{

        int ing7ID = manager.generate("rose", 5, 50, false, false, false, "liten bukett", "2017-06-06", "2018-06-06");
        int ing8ID = manager.generate("tulipan", 7, 50, false, false, false, "liten bukett", "2017-06-06", "2018-06-06");

        //gets information before and after edit
        String førSetning = "SELECT stock, name from HCL_ingredient where ingredient_id = " + ing7ID;
        String[][] utskrift1  = sql.getStringTable(  førSetning , false  );
        for(int i = 0; i < utskrift1[0].length; i++){
            System.out.println(utskrift1[0][i]);
        } //prints "5, rose"


        manager.addStock(ing7ID, 14);

        String etterSetning = "SELECT stock, name from HCL_ingredient where ingredient_id = " + ing7ID;
        String[][] utskrift2  = sql.getStringTable(  etterSetning , false  );
        for(int i = 0; i < utskrift2[0].length; i++){
            System.out.println(utskrift2[0][i]);
        } //prints "19, rose"


        //checking for correct return
        assertEquals(1, manager.addStock(ing8ID, 14)); //Finnes, riktig
        assertEquals(-1, manager.addStock(99999999, 14)); //Finnes ikke, riktig
        assertEquals(-3, manager.addStock(ing8ID, 0)); //Feil parameter, riktig


        sql.deleteForGood("HCL_ingredient", "ingredient_id", ing7ID);
        sql.deleteForGood("HCL_ingredient", "ingredient_id", ing8ID);

    }


    @Test
    public void removeStock() throws Exception{

        int ing9ID = manager.generate("hestehov", 5, 50, false, false, false, "liten bukett", "2017-06-06", "2018-06-06");
        int ing10ID = manager.generate("løvetann", 7, 50, false, false, false, "liten bukett", "2017-06-06", "2018-06-06");

        //gets information about the number of objects before and after removineStock
        String førSetning = "SELECT stock, name from HCL_ingredient where ingredient_id = " + ing9ID;
        String[][] utskrift1  = sql.getStringTable(  førSetning , false  );
        for(int i = 0; i < utskrift1[0].length; i++){
            System.out.println(utskrift1[0][i]);
        } //prints "5, hestehov"


        manager.removeStock(ing9ID, 2);

        String etterSetning = "SELECT stock, name from HCL_ingredient where ingredient_id = " + ing9ID;
        String[][] utskrift2  = sql.getStringTable(  etterSetning , false  );
        for(int i = 0; i < utskrift2[0].length; i++){
            System.out.println(utskrift2[0][i]);
        } //prints "3, hestehov"


        //checking for correct return
        assertEquals(1, manager.removeStock(ing10ID, 1));
        assertEquals(-1, manager.removeStock(1010, 14));
        assertEquals(-3, manager.removeStock(ing10ID, 0));
        assertEquals(-3, manager.removeStock(ing10ID, 20));


        sql.deleteForGood("HCL_ingredient", "ingredient_id", ing10ID);
        sql.deleteForGood("HCL_ingredient", "ingredient_id", ing9ID);

    }





}



