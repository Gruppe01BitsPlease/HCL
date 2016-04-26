package backend;

/**
 * Testklasse for klassen FoodManager
 * Created by trineliseolsen on 13.04.2016.
 */

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class FoodManagerTest {

    FoodManager manager;
    SQL sql = new SQL();
    LinkManager lManager;
    IngredientManager iManager;


    @Before
    public void setUp() throws Exception {
        manager = new FoodManager(sql);
        lManager = new LinkManager(sql);
        iManager = new IngredientManager(sql);

    }


    @After
    public void tearDown() throws Exception {
        manager = null;
        lManager = null;
        iManager = null;
        sql.end();
    }

    @Test
    public void generate() throws Exception{

        //making test-objects
        int foodID = manager.generate("Bløtkake", 75);
        int food2ID = manager.generate("Sirupssnipp", 75);

        //Testing if the objects actually was made..
        assertTrue(sql.rowExists("HCL_food", "food_id", foodID));
        assertTrue(sql.rowExists("HCL_food", "food_id", food2ID));
        //checking that fake objects doesn't exist.
        assertFalse(sql.rowExists("HCL_food", "food_id", 99999999));

        //Checking if wrong parameters give correct return
        assertEquals(-3, manager.generate("", 1));
        assertEquals(-3, manager.generate("Pultost",-1));

        sql.deleteForGood("HCL_food", "food_id", foodID);
        sql.deleteForGood("HCL_food", "food_id", food2ID);
    }

    @Test
    public void delete() throws Exception {
        //making test objects
        int food3ID = manager.generate("Klubb", 60);
        int food4ID = manager.generate("Møsbrømslfse", 75);

        //checking if the object exist before and after delete
        assertTrue(sql.rowExists("HCL_food", "food_id", food3ID));
        manager.delete(food3ID);
        assertFalse(sql.rowExists("HCL_food", "food_id", food3ID));

        //checking for correct return.
        assertEquals(1, manager.delete(food4ID)); //Finnes, riktig
        assertEquals(-1, manager.delete(99999999)); //Finnes ikke, riktig

        sql.deleteForGood("HCL_food", "food_id", food3ID);
        sql.deleteForGood("HCL_food", "food_id", food4ID);


    }

    @Test
    public void addIngredient() throws Exception{
        //Making test objects
        int foodID = iManager.generate("mandel", 5, 56, false, false, true, "kun en", "2016-04-04", "2017-05-06");
        int ingID =  manager.generate("Grøt", 60);
        manager.addIngredient(foodID,ingID,10);

        //checking that the link-object was correctly made.
        assertTrue(sql.rowExists("HCL_food_ingredient", "food_id","ingredient_id", foodID, ingID));
        //checking returns
        assertEquals(-4, manager.addIngredient(foodID, 99999999, 40));
        assertEquals(-4, manager.addIngredient(99999999, ingID, 40));
        assertEquals(-3, manager.addIngredient(foodID, ingID, -1));


        sql.deleteForGood("HCL_food_ingredient", "food_id","ingredient_id", foodID, ingID);
        sql.deleteForGood("HCL_food", "food_id", foodID);
        sql.deleteForGood("HCL_ingredient", "ingredient_id", ingID);


    }

}
