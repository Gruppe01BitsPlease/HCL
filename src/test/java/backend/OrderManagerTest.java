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
        int trineID = cManager.generate("Trinemor", "Trinemor@hotmail.com", 75584788);
        int bestillingID = manager.generate(trineID, 123, "Elgeseter", 7030, "2017-02-02");

        //Tester om Customer-objektene faktisk ble laget.
        assertTrue(sql.rowExists("HCL_order", "order_id", bestillingID));
        assertFalse(sql.rowExists("HCL_order", "order_id", 1010));

        //Prøver å lage ukorrekte Customer-objekter, og sjekker om generate() sender riktig feilmelding
        assertEquals(-3, manager.generate(trineID, -1, "Elgeseter", 7030, "2017-02-02"));
        assertEquals(-3, manager.generate(-1, 123, "Elgeseter", 7030, "2017-02-02"));
        assertEquals(-3, manager.generate(trineID, 123, "Elgeseter", 7030, "0000"));

        cManager.delete(trineID);
        manager.delete(bestillingID);

    }

    @Test
    public void delete() throws Exception {
        //lager customerobjektet Kari
        int karlID = cManager.generate("Kari", "kari@hotmail.com", 75583478);
        int bestilling2ID = manager.generate(karlID, 123, "Elgeseter", 7030, "2017-02-02");
        int bestilling3ID = manager.generate(karlID, 321, "Elgeseter", 7030, "2017-03-02");

        //Sjekker at Kari finnes, sletter Kari, sjekker at Kari faktisk er slettet
        assertTrue(sql.rowExists("HCL_order", "order_id", bestilling2ID));
        manager.delete(bestilling2ID);
        assertFalse(sql.rowExists("HCL_customer", "customer_id", bestilling2ID));

        //tester med gal/ingen ID
        assertEquals(1,manager.delete(bestilling3ID));
        assertEquals(-1,manager.delete(001));

    }

    @Test
    public void addIngredient() throws Exception{
        //Lager ny ingrediens, henter ID
        int grisID = fManager.generate("gris", 22);
        int marvinID = cManager.generate("Marvin", "epost", 12345678);
        int bestilling4ID = manager.generate(marvinID, 123, "Elgeseter", 7030, "2017-02-02");
        manager.addFood(bestilling4ID, grisID, 10);

        //Sjekker at mandelen ligger i grøten
        assertTrue(sql.rowExists("HCL_order_food", "order_id","food_id", bestilling4ID, grisID));
        //Sjekker at alle feilmeldingene fungerer
        assertEquals(-1, manager.addFood(bestilling4ID, grisID, 22));
        assertEquals(-3, manager.addFood(bestilling4ID, grisID, -1));
        assertEquals(-3, manager.addFood(-1, grisID, 22));
        assertEquals(-3, manager.addFood(bestilling4ID, -1, 12));



        manager.delete(bestilling4ID);
        cManager.delete(marvinID);

    }

    @Test
    public void isSubscription() throws Exception{
        int luluID = cManager.generate("Lulu", "epost", 12345678);
        int bestilling5ID = manager.generate(luluID, 123, "Elgeseter", 7030, "2017-02-02");
        System.out.println(luluID  + " " + bestilling5ID);
        assertEquals(false, manager.isSubscription(bestilling5ID));
        DayOfWeek[] testArray = {DayOfWeek.MONDAY, DayOfWeek.FRIDAY, DayOfWeek.WEDNESDAY};
        dManager.addDates(bestilling5ID, LocalDate.now(), LocalDate.of(2017,12,24), 2, testArray);
        assertEquals(true, manager.isSubscription(bestilling5ID));


    }


}
