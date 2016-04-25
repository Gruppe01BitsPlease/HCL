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
        int rydigerID = cManager.generate("Rydiger", "rydiger@hotmail.com", 75584788);
        int bestillingID = oManager.generate(rydigerID, 123, "Voll", 7030, "2017-02-02");
        int leveringID = manager.addDate(bestillingID, "2016-05-05");
        int levering2ID = manager.addDate(bestillingID, "2016-05-05");

        assertTrue(sql.rowExists("HCL_deliveries", "delivery_id", leveringID));
        manager.removeDate(leveringID);
        assertFalse(sql.rowExists("HCL_deliveries", "delivery_id", leveringID));

        //tester med gal/ingen ID
        assertEquals(1,manager.removeDate(levering2ID));
        assertEquals(-1,manager.removeDate(99999999));

        sql.deleteForGood("HCL_deliveries", "delivery_id", leveringID);
        sql.deleteForGood("HCL_deliveries", "delivery_id", levering2ID);
        sql.deleteForGood("HCL_order", "order_id", bestillingID);
        sql.deleteForGood("HCL_customer", "customer_id", rydigerID);

    }

    @Test
    public void addDate() throws Exception{
        int annaID = cManager.generate("Anna", "anna@hotmail.com", 75584788);
        int bestilling2ID = oManager.generate(annaID, 123, "Moholt", 7030, "2017-02-02");
        int levering3ID = manager.addDate(bestilling2ID, "2016-05-05");
        int levering4ID = manager.addDate(bestilling2ID, "2016-06-05");

        //Tester om Customer-objektene faktisk ble laget.
        assertTrue(sql.rowExists("HCL_deliveries", "delivery_id", levering3ID));
        assertTrue(sql.rowExists("HCL_deliveries", "delivery_id", levering4ID));
        assertFalse(sql.rowExists("HCL_deliveries", "delivery_id", 99999999));

        //Sjekker at alle feilmeldingene fungerer
        assertEquals(-3, manager.addDate(102948, "2016-05-05"));
        assertEquals(-5, manager.addDate(bestilling2ID, "12345-05-05"));

        sql.deleteForGood("HCL_deliveries", "delivery_id", levering3ID);
        sql.deleteForGood("HCL_deliveries", "delivery_id", levering4ID);
        sql.deleteForGood("HCL_order", "order_id", bestilling2ID);
        sql.deleteForGood("HCL_customer", "customer_id", annaID);

    }



    @Test
    public void getDatesToBeAdded() throws Exception{

        int regineID = cManager.generate("Regine", "anna@hotmail.com", 75584788);
        int bestilling3ID = oManager.generate(regineID, 123, "Lade", 7030, "2017-02-02");


        DayOfWeek[] testArray = {DayOfWeek.MONDAY, DayOfWeek.FRIDAY };

        //Lager teststring hvor getDatesToBeAdded henter ut datoene og skriver ut.
        String[] testArray2 = manager.getDatesToBeAdded(bestilling3ID,LocalDate.of(2016,12,01), LocalDate.of(2016,12,22), 2, testArray);
        for (int i = 0; i < testArray2.length; i++){
            System.out.println(testArray2[i]);
        }
        //skrev ut "2016-12-02" og "2016-12-05", noe som er riktig

        //Sjekker at metoden returnerer tom string for feile parametere
        String[] feilstring1 = manager.getDatesToBeAdded(99999999,LocalDate.of(2016,12,01), LocalDate.of(2016,12,22), 2, testArray);
        assertEquals(0, feilstring1.length);
        String[] feilstring2 = manager.getDatesToBeAdded(bestilling3ID,null, LocalDate.of(2016,12,22), 2, testArray);
        assertEquals(0, feilstring1.length);
        String[] feilstring3 = manager.getDatesToBeAdded(bestilling3ID,LocalDate.of(2016,12,01), null, 2, testArray);
        assertEquals(0, feilstring1.length);
        String[] feilstring4 = manager.getDatesToBeAdded(bestilling3ID,LocalDate.of(2016,12,01), LocalDate.of(2016,12,22), 0, testArray);
        assertEquals(0, feilstring1.length);
        String[] feilstring5 = manager.getDatesToBeAdded(bestilling3ID,LocalDate.of(2016,12,01), LocalDate.of(2016,12,22), 2, new DayOfWeek[0]);
        assertEquals(0, feilstring1.length);


        sql.deleteForGood("HCL_order", "order_id", bestilling3ID);
        sql.deleteForGood("HCL_customer", "customer_id", regineID);

    }

    @Test
    public void addDates(){
        //voidmetode, fette umulig!!!!!

    }

    @Test
    public void deliver() throws Exception{
        //Lager testobjekter
        int knutID = cManager.generate("Knut", "knut@hotmail.com", 75584788);
        int bestilling4ID = oManager.generate(knutID, 123, "Moholt", 7030, "2017-02-02");
        int levering4ID = manager.addDate(bestilling4ID, "2016-05-05");


        //sjekker om leveranse er levert og får negativ, leverer, sjekker igjen og får positiv.
        String [][] active = sql.getStringTable("SELECT delivered FROM HCL_deliveries where delivery_id = " + levering4ID, false);
        assertEquals(0, Integer.parseInt(active[0][0]));
        manager.deliver(levering4ID);
        active = sql.getStringTable("SELECT delivered FROM HCL_deliveries where delivery_id = " + levering4ID, false);
        assertEquals(1, Integer.parseInt(active[0][0]));
        assertEquals(-1, manager.deliver(11111));

        sql.deleteForGood("HCL_deliveries", "delivery_id", levering4ID);
        sql.deleteForGood("HCL_order", "order_id", bestilling4ID);
        sql.deleteForGood("HCL_customer", "customer_id", knutID);


    }



    @Test
    public void complete() throws Exception{
        //lager testobjekter
        int lunaID = cManager.generate("Luna", "luna@hotmail.com", 75584788);
        int bestilling5ID = oManager.generate(lunaID, 123, "Orkanger", 7030, "2017-02-02");
        int levering5ID = manager.addDate(bestilling5ID, "2016-05-05");

        //Sjekker om levering 5 er completed, og får til før svar om at den ikke er det, etterpå at den er det.
        String [][] active = sql.getStringTable("SELECT completed FROM HCL_deliveries where delivery_id = " + levering5ID, false);
        assertEquals(0, Integer.parseInt(active[0][0]));
        manager.complete(levering5ID);
        active = sql.getStringTable("SELECT completed FROM HCL_deliveries where delivery_id = " + levering5ID, false);
        assertEquals(1, Integer.parseInt(active[0][0]));
        assertEquals(-1, manager.deliver(99999999));

        sql.deleteForGood("HCL_deliveries", "delivery_id", levering5ID);
        sql.deleteForGood("HCL_order", "order_id", bestilling5ID);
        sql.deleteForGood("HCL_customer", "customer_id", lunaID);

    }


}
