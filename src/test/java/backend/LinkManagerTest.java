package backend;

/**
 * Created by trineliseolsen on 18.04.2016.
 */

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class LinkManagerTest {

    SQL sql = new SQL();
    LinkManager manager;
    IngredientManager iManager;
    FoodManager fManager;


    @Before
    public void setUp() throws Exception {
        fManager = new FoodManager(sql);
        manager = new LinkManager(sql);
        iManager = new IngredientManager(sql);

    }


    @After
    public void tearDown() throws Exception {
        manager = null;
        fManager = null;
        iManager = null;
        sql.end();
    }

    @Test
    public void generate() throws Exception {

        //making test-objects
        int ingrID = iManager.generate("gjørme", 5, 56, false, false, true, "æsj", "2016-04-04", "2017-05-06");
        int foodID =  fManager.generate("sølekake", 60);
        manager.generate("HCL_food_ingredient", "food_id","ingredient_id", foodID, ingrID, 20);

        //Testing if linkobject is ok
        assertTrue(sql.rowExists("HCL_food_ingredient", "food_id","ingredient_id", foodID, ingrID));
        //checking that fake linkobject doesn't exist.
        assertFalse(sql.rowExists("HCL_food_ingredient", "food_id","ingredient_id", 99999999, ingrID));
        assertFalse(sql.rowExists("HCL_food_ingredient", "food_id","ingredient_id", foodID, 99999999));

        //Checking if correct return for fake objects.
        assertEquals(-3, manager.generate("", "food_id","ingredient_id", foodID, ingrID, 20));
        assertEquals(-3, manager.generate("HCL_food_ingredient", "","ingredient_id", foodID, ingrID, 20));
        assertEquals(-3, manager.generate("HCL_food_ingredient", "food_id","", foodID, ingrID, 20));
        assertEquals(-3, manager.generate("HCL_food_ingredient", "food_id","ingredient_id", -1, ingrID, 20));
        assertEquals(-3, manager.generate("HCL_food_ingredient", "food_id","ingredient_id", foodID, -1, 20));



        sql.deleteForGood("HCL_food_ingredient", "food_id","ingredient_id", foodID, ingrID);
        sql.deleteForGood("HCL_ingredient", "ingredient_id", ingrID);
        sql.deleteForGood("HCL_food", "food_id", foodID);
    }

    @Test
    public void delete() throws Exception {
        int ing2ID = iManager.generate("kakestrø", 5, 56, false, false, true, "æsj", "2016-04-04", "2017-05-06");
        int ing3ID = iManager.generate("muffinsstrø", 5, 56, false, false, true, "æsj", "2016-04-04", "2017-05-06");
        int food2ID =  fManager.generate("kake", 60);
        int food3ID =  fManager.generate("muffins", 60);
        manager.generate("HCL_food_ingredient", "food_id","ingredient_id", food2ID, ing2ID, 20);


        //Testing if linkobject exist, deletes it and chacks again.
        assertTrue(sql.rowExists("HCL_food_ingredient", "food_id","ingredient_id", food2ID, ing2ID));
        manager.delete("HCL_food_ingredient", "food_id","ingredient_id", food2ID, ing2ID);
        // NOT WORKING FOR LINK TABLES; AS LONG AS THEY EXIST THEY WILL RETURN rowExists() will return true regardless of active status
        // assertFalse(sql.rowExists("HCL_food_ingredient", "food_id","ingredient_id", food2ID, ing2ID));


        //checking objects for correct return
        assertEquals(-1, manager.delete("HCL_food_ingredient", "food_id","ingredient_id", food3ID, ing3ID));
        assertEquals(-3, manager.delete("", "food_id","ingredient_id", food3ID, ing3ID));
        assertEquals(-3, manager.delete("HCL_food_ingredient", "","ingredient_id", food3ID, ing3ID));
        assertEquals(-3, manager.delete("HCL_food_ingredient", "food_id","", food3ID, ing3ID));
        assertEquals(-3, manager.delete("HCL_food_ingredient", "food_id","ingredient_id", -1, ing3ID));
        assertEquals(-3, manager.delete("HCL_food_ingredient", "food_id","ingredient_id", food3ID, -1));
        manager.generate("HCL_food_ingredient", "food_id","ingredient_id", food3ID, ing3ID, 20);
        assertEquals(1, manager.delete("HCL_food_ingredient", "food_id","ingredient_id", food3ID, ing3ID));

        sql.deleteForGood("HCL_food_ingredient", "food_id","ingredient_id", food2ID, ing2ID);
        sql.deleteForGood("HCL_food_ingredient", "food_id","ingredient_id", food3ID, ing3ID);
        sql.deleteForGood("HCL_ingredient", "ingredient_id", ing2ID);
        sql.deleteForGood("HCL_food", "food_id", food2ID);
        sql.deleteForGood("HCL_ingredient", "ingredient_id", ing3ID);
        sql.deleteForGood("HCL_food", "food_id", food3ID);


    }

    @Test
    public void editNumber() throws Exception{
        //making test-objects
        int ing4ID = iManager.generate("frø", 5, 56, false, false, true, "æsj", "2016-04-04", "2017-05-06");
        int food4ID =  fManager.generate("frøbrød", 60);
        manager.generate("HCL_food_ingredient", "food_id","ingredient_id", food4ID, ing4ID, 5);

        //checking linkobject before edit
        String førSetning = "SELECT number from HCL_food_ingredient where ingredient_id = " + ing4ID + " AND food_id = " + food4ID;
        String[][] utskrift  = sql.getStringTable(  førSetning , false  );
        for(int i = 0; i < utskrift[0].length; i++){
            System.out.println(utskrift[0][i]);
        } //Prints "5"

        manager.editNumber("HCL_food_ingredient", "food_id","ingredient_id", food4ID, ing4ID, 10);

        //checking linkobject after edit
        String etterSetning = "SELECT number from HCL_food_ingredient where ingredient_id = " + ing4ID + " AND food_id = " + food4ID;
        String[][] utskrift2  = sql.getStringTable(  etterSetning , false  );
        for(int i = 0; i < utskrift2[0].length; i++){
            System.out.println(utskrift2[0][i]);
        } //prints "10"

        sql.deleteForGood("HCL_food_ingredient", "food_id","ingredient_id", food4ID, ing4ID);
        sql.deleteForGood("HCL_food", "food_id", food4ID);
        sql.deleteForGood("HCL_ingredient", "ingredient_id", ing4ID);



    }

}

