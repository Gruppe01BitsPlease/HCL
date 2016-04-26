package backend;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by trineliseolsen on 20.04.2016.
 */
public class OrderManagerTest {
    OrderManager manager;
    CustomerManager cManager;
    FoodManager fManager;
    DeliveryManager dManager;
    SQL sql = new SQL();


    @Before
    public void setUp() throws Exception {
        manager = new OrderManager(sql);
        cManager = new CustomerManager(sql);
        fManager = new FoodManager(sql);
        dManager = new DeliveryManager(sql);
    }

    @After
    public void tearDown() throws Exception {
        manager = null;
        cManager = null;
        fManager = null;
        dManager = null;
        sql.end();
    }

    @Test
    public void generate() throws Exception {
        //making test-objects
        int custID = cManager.generate("Trinemor", "Trinemor@hotmail.com", 75584788);
        int orderID = manager.generate(custID, 123, "Elgeseter", 7030, "2017-02-02");

        //checks if objects were made/fake objects were not made.
        assertTrue(sql.rowExists("HCL_order", "order_id", orderID));
        assertFalse(sql.rowExists("HCL_order", "order_id", 1010));

        //checking return of fake objects
        assertEquals(-3, manager.generate(custID, -1, "Elgeseter", 7030, "2017-02-02"));
        assertEquals(-3, manager.generate(-1, 123, "Elgeseter", 7030, "2017-02-02"));
        assertEquals(-3, manager.generate(custID, 123, "Elgeseter", 7030, "0000"));

        sql.deleteForGood("HCL_customer", "customer_id", custID);
        sql.deleteForGood("HCL_order", "order_id", orderID);

    }

    @Test
    public void delete() throws Exception {
        //lager customerobjektet Kari
        int cust2ID = cManager.generate("Kari", "kari@hotmail.com", 75583478);
        int order2ID = manager.generate(cust2ID, 123, "Elgeseter", 7030, "2017-02-02");
        int order3ID = manager.generate(cust2ID, 321, "Elgeseter", 7030, "2017-03-02");

        //Sjekker at Kari finnes, sletter Kari, sjekker at Kari faktisk er slettet
        assertTrue(sql.rowExists("HCL_order", "order_id", order2ID));
        manager.delete(order2ID);
        assertFalse(sql.rowExists("HCL_customer", "customer_id", order2ID));

        //tester med gal/ingen ID
        assertEquals(1,manager.delete(order3ID));
        assertEquals(-1,manager.delete(001));

        sql.deleteForGood("HCL_customer", "customer_id", cust2ID);
        sql.deleteForGood("HCL_order", "order_id", order2ID);
        sql.deleteForGood("HCL_order", "order_id", order3ID);

    }

    @Test
    public void addIngredient() throws Exception{
        //making testobjects
        int foodID = fManager.generate("gris", 22);
        int cust3ID = cManager.generate("Marvin", "epost", 12345678);
        int order4ID = manager.generate(cust3ID, 123, "Elgeseter", 7030, "2017-02-02");
        manager.addFood(order4ID, foodID, 10);

        //Schecking that linkobject was made.
        assertTrue(sql.rowExists("HCL_order_food", "order_id","food_id", order4ID, foodID));
        //checking for correct return
        assertEquals(-1, manager.addFood(order4ID, foodID, 22));
        assertEquals(-3, manager.addFood(order4ID, foodID, -1));
        assertEquals(-3, manager.addFood(-1, foodID, 22));
        assertEquals(-3, manager.addFood(order4ID, -1, 12));


        sql.deleteForGood("HCL_order_food", "order_id","food_id", order4ID, foodID);
        sql.deleteForGood("HCL_order", "order_id", order4ID);
        sql.deleteForGood("HCL_food", "food_id", foodID);
        sql.deleteForGood("HCL_customer", "customer_id", cust3ID);

    }

    @Test
    public void isSubscription() throws Exception{
        //making test-objects
        int cust4ID = cManager.generate("Lulu", "epost", 12345678);
        int order5ID = manager.generate(cust4ID, 123, "Elgeseter", 7030, "2017-02-02");

        //checking that order is not a subcription, making subscription, checks again.
        assertEquals(false, manager.isSubscription(order5ID));
        DayOfWeek[] testArray = {DayOfWeek.MONDAY};
        dManager.addDates(order5ID, LocalDate.now(), LocalDate.of(2017,02,10), 2, testArray);
        assertEquals(true, manager.isSubscription(order5ID));

        sql.deleteForGood("HCL_customer", "customer_id", cust4ID);
        sql.deleteForGood("HCL_order", "order_id", order5ID);


    }


}
