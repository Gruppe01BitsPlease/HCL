package backend;

/**
 * Created by trineliseolsen on 21.04.2016.
 */

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class DeliveryManagerTest {

    DeliveryManager manager;
    OrderManager oManager;
    CustomerManager cManager;


    SQL sql = new SQL();

    @Before
    public void setUp() throws Exception {
        manager = new DeliveryManager(sql);
        oManager = new OrderManager(sql);
        cManager = new CustomerManager(sql);

    }

    @After
    public void tearDown() throws Exception {
        manager = null;
        oManager = null;
        cManager = null;
        sql.end();
    }

    @Test
    public void removeDate() throws Exception{

        //Making test-objects
        int custID = cManager.generate("Rydiger", "rydiger@hotmail.com", 75584788);
        int orderID = oManager.generate(custID, 123, "Voll", 7030, "2017-02-02");
        int deliveryID = manager.addDate(orderID, "2016-05-05");
        int delivery2ID = manager.addDate(orderID, "2016-05-05");

        //checks that a ddelivery exists, removes it, checks if it was removed.
        assertTrue(sql.rowExists("HCL_deliveries", "delivery_id", deliveryID));
        manager.removeDate(deliveryID);
        assertFalse(sql.rowExists("HCL_deliveries", "delivery_id", deliveryID));

        //testing with both real and fake ID.
        assertEquals(1,manager.removeDate(delivery2ID));
        assertEquals(-3,manager.removeDate(99999999));

        sql.deleteForGood("HCL_deliveries", "delivery_id", deliveryID);
        sql.deleteForGood("HCL_deliveries", "delivery_id", delivery2ID);
        sql.deleteForGood("HCL_order", "order_id", orderID);
        sql.deleteForGood("HCL_customer", "customer_id", custID);

    }

    @Test
    public void addDate() throws Exception{
        int cust2ID = cManager.generate("Anna", "anna@hotmail.com", 75584788);
        int order2ID = oManager.generate(cust2ID, 123, "Moholt", 7030, "2017-02-02");
        int delivery3ID = manager.addDate(order2ID, "2016-05-05");
        int delivery4ID = manager.addDate(order2ID, "2016-06-05");

        //Testing that the customer objects actually was made.
        assertTrue(sql.rowExists("HCL_deliveries", "delivery_id", delivery3ID));
        assertTrue(sql.rowExists("HCL_deliveries", "delivery_id", delivery4ID));
        assertFalse(sql.rowExists("HCL_deliveries", "delivery_id", 99999999));

        //Testing that correct returns are given
        assertEquals(-3, manager.addDate(102948, "2016-05-05"));
        assertEquals(-5, manager.addDate(order2ID, "12345-05-05"));

        sql.deleteForGood("HCL_deliveries", "delivery_id", delivery3ID);
        sql.deleteForGood("HCL_deliveries", "delivery_id", delivery4ID);
        sql.deleteForGood("HCL_order", "order_id", order2ID);
        sql.deleteForGood("HCL_customer", "customer_id", cust2ID);

    }



    /* NOT USED IN PROJECT ANYMORE TEST NOT WORKING EITHER
    public void getDatesToBeAdded() throws Exception{

        //Making test-objects
        int cust3ID = cManager.generate("Regine", "anna@hotmail.com", 75584788);
        int order3ID = oManager.generate(cust3ID, 123, "Lade", 7030, "2017-02-02");
        DayOfWeek[] testArray = {DayOfWeek.MONDAY, DayOfWeek.FRIDAY };

        //Making a test-String and prints out the dates
        String[] testArray2 = manager.getDatesToBeAdded(order3ID,LocalDate.of(2016,12,01), LocalDate.of(2016,12,22), 2, testArray);
        for (int i = 0; i < testArray2.length; i++){
            System.out.println(testArray2[i]);
        }
        //printed "2016-12-02" og "2016-12-05", which is correct

        //Checking that wrong parameters give empty strings
        String[] feilstring1 = manager.getDatesToBeAdded(99999999,LocalDate.of(2016,12,01), LocalDate.of(2016,12,22), 2, testArray);
        assertEquals(0, feilstring1.length);
        String[] feilstring3 = manager.getDatesToBeAdded(order3ID,LocalDate.of(2016,12,01), LocalDate.of(2016,12,22), 2, testArray);
        assertEquals(0, feilstring3.length);
        String[] feilstring4 = manager.getDatesToBeAdded(order3ID,LocalDate.of(2016,12,01), LocalDate.of(2016,12,22), 0, testArray);
        assertEquals(0, feilstring4.length);
        String[] feilstring5 = manager.getDatesToBeAdded(order3ID,LocalDate.of(2016,12,01), LocalDate.of(2016,12,22), 2, new DayOfWeek[0]);
        assertEquals(0, feilstring5.length);


        sql.deleteForGood("HCL_order", "order_id", order3ID);
        sql.deleteForGood("HCL_customer", "customer_id", cust3ID);

    }
    */

    @Test
    public void deliver() throws Exception{
        //Making test-objects
        int cust4ID = cManager.generate("Knut", "knut@hotmail.com", 75584788);
        int order4ID = oManager.generate(cust4ID, 123, "Moholt", 7030, "2017-02-02");
        int delivery5ID = manager.addDate(order4ID, "2016-05-05");


        //checking if delivery is made and gets false, make delivery and checks again.
        String [][] active = sql.getStringTable("SELECT delivered FROM HCL_deliveries where delivery_id = " + delivery5ID, false);
        assertEquals(0, Integer.parseInt(active[0][0]));
        manager.deliver(delivery5ID);
        active = sql.getStringTable("SELECT delivered FROM HCL_deliveries where delivery_id = " + delivery5ID, false);
        assertEquals(1, Integer.parseInt(active[0][0]));
        //checks with wrong ID
        assertEquals(-1, manager.deliver(99999999));

        sql.deleteForGood("HCL_deliveries", "delivery_id", delivery5ID);
        sql.deleteForGood("HCL_order", "order_id", order4ID);
        sql.deleteForGood("HCL_customer", "customer_id", cust4ID);


    }



    @Test
    public void complete() throws Exception{
        //making test-objects
        int lunaID = cManager.generate("Luna", "luna@hotmail.com", 75584788);
        int bestilling5ID = oManager.generate(lunaID, 123, "Orkanger", 7030, "2017-02-02");
        int levering5ID = manager.addDate(bestilling5ID, "2016-05-05");

        //Checks if delivery5 is completed, complete it, and then checks again.
        String [][] active = sql.getStringTable("SELECT completed FROM HCL_deliveries where delivery_id = " + levering5ID, false);
        assertEquals(0, Integer.parseInt(active[0][0]));
        manager.complete(levering5ID);
        active = sql.getStringTable("SELECT completed FROM HCL_deliveries where delivery_id = " + levering5ID, false);
        assertEquals(1, Integer.parseInt(active[0][0]));
        //Checks with wrong parameters
        assertEquals(-1, manager.deliver(99999999));

        sql.deleteForGood("HCL_deliveries", "delivery_id", levering5ID);
        sql.deleteForGood("HCL_order", "order_id", bestilling5ID);
        sql.deleteForGood("HCL_customer", "customer_id", lunaID);

    }


}
