package backend;

/**
 * Created by trineliseolsen on 21.04.2016.
 */

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class DeliveryManagerTest {

    DeliveryManager manager;
    OrderManager oManager;
    CustomerManager cManager;
    FoodManager fManager;

    SQL sql = new SQL();

    @Before
    public void setUp() throws Exception {
        manager = new DeliveryManager(sql);
        oManager = new OrderManager(sql);
        cManager = new CustomerManager(sql);
        fManager = new FoodManager(sql);

    }

    @After
    public void tearDown() throws Exception {
        manager = null;
        sql.end();
    }

    @Test
    public void delete(){
        int rydigerID = cManager.generate("Rydiger", "rydiger@hotmail.com", 75584788);
        int bestillingID = oManager.generate(rydigerID, 123, "Voll", 7030, "2017-02-02");
        int leveringID = manager.addDate(bestillingID, "2016-05-05");
        int levering2ID = manager.addDate(bestillingID, "2016-05-05");

        assertTrue(sql.rowExists("HCL_deliveries", "delivery_id", leveringID));
        manager.delete(leveringID);
        assertFalse(sql.rowExists("HCL_deliveries", "delivery_id", leveringID));

        //tester med gal/ingen ID
        assertEquals(1,manager.delete(levering2ID));
        assertEquals(-1,manager.delete(0101010));

        sql.deleteForGood("HCL_deliveries", "delivery_id", leveringID);
        sql.deleteForGood("HCL_deliveries", "delivery_id", levering2ID);
        sql.deleteForGood("HCL_order", "order_id", bestillingID);
        sql.deleteForGood("HCL_customer", "customer_id", rydigerID);

    }

    @Test
    public void addDate(){
        int annaID = cManager.generate("Anna", "anna@hotmail.com", 75584788);
        int bestilling2ID = oManager.generate(annaID, 123, "Moholt", 7030, "2017-02-02");
        int levering3ID = manager.addDate(bestilling2ID, "2016-05-05");
        int levering4ID = manager.addDate(bestilling2ID, "2016-05-05");

        //Tester om Customer-objektene faktisk ble laget.
        assertTrue(sql.rowExists("HCL_deliveries", "delivery_id", levering3ID));
        assertTrue(sql.rowExists("HCL_deliveries", "delivery_id", levering4ID));
        assertFalse(sql.rowExists("HCL_deliveries", "delivery_id", 0010111));

        //Sjekker at alle feilmeldingene fungerer
        assertEquals(-3, manager.addDate(102948, "2016-05-05"));
        assertEquals(-5, manager.addDate(bestilling2ID, "12345-05-05"));

        sql.deleteForGood("HCL_deliveries", "delivery_id", levering3ID);
        sql.deleteForGood("HCL_deliveries", "delivery_id", levering4ID);
        sql.deleteForGood("HCL_order", "order_id", bestilling2ID);
        sql.deleteForGood("HCL_customer", "customer_id", annaID);

    }

    @Test
    public void removeDate(){

    }

    @Test
    public void contains(){

    }

    @Test
    public void addDates(){

    }

    @Test
    public void getDatesToBeAdded(){

    }

    @Test
    public void deliver(){

    }

    @Test
    public void complete(){

    }


}
