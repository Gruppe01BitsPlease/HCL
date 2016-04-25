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

    }

    @Test
    public void addShoppinglist() throws Exception {


    }

    @Test
    public void add() throws Exception {

    }

}