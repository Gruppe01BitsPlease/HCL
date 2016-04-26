package backend;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by bahafeld on 21.04.2016...
 * For FoodPlease
 */
public class ShoppinglistTest {


    SQL sql = new SQL();
    DeliveryManager dManager;
    OrderManager oManager;
    CustomerManager cManager;
    Shoppinglist manager;
    IngredientManager iManager;
    FoodManager fManager;



    @Before
    public void setUp() throws Exception {
        manager = new Shoppinglist(sql);
        oManager = new OrderManager(sql);
        cManager = new CustomerManager(sql);
        iManager = new IngredientManager(sql);
        fManager = new FoodManager(sql);
        dManager = new DeliveryManager(sql);

    }

    @After
    public void tearDown() throws Exception {
        oManager = null;
        cManager = null;
        manager = null;
        iManager = null;
        fManager = null;
        dManager = null;
        sql.end();

    }

    @Test
    public void getShoppinglist() throws Exception {
        String[][] testList = manager.getShoppinglist(10);
        sql.print2dArray(testList);
        //prints out the shoppinglist - which is correct according to the database

    }

    @Test
    public void addShoppinglist() throws Exception {
        String[][] testList = manager.getShoppinglist(10);
        sql.print2dArray(manager.getShoppinglist(10));
        //prints out the shoppinglist - which is correct according to the database

        //adds the shoppinglist
        manager.addShoppinglist(testList);
        sql.print2dArray(manager.getShoppinglist(10));
        //prints out nothing, which is correct



    }

    /* unrealistic test
    public void add() throws Exception {
        String[][] testList2 = manager.getShoppinglist(10);
        sql.print2dArray(manager.getShoppinglist(10));
        manager.add(testList2, 0);//removes the first row
        sql.print2dArray(manager.getShoppinglist(10));
        //prints out correctly - the first row is now removed.

    }
    */

}