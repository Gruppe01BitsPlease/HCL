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


    @Before
    public void setUp() throws Exception {
        manager = new FoodManager(sql);
    }


    @After
    public void tearDown() throws Exception {
        manager = null;
    }

    @Test
    public void generate() {

        manager.generate("Bløtkake", 75);
        manager.generate("Sirupssnipp", 75);

        //Tester om foodobjektene faktisk ble laget.
        assertTrue(sql.rowExists("HCL_food", "name", "Bløtkake"));
        assertTrue(sql.rowExists("HCL_food", "name", "Sirupssnipp"));
        assertFalse(sql.rowExists("HCL_food", "name", "Mølje"));

        //Prøver å lage ukorrekte foodobjekter, og sjekker om generate() sender riktig feilmelding
        assertEquals(manager.generate(" ", 1), -1); //Feil! burde returnet -3
        assertEquals(manager.generate("Pultost", 0), -1); //Feil! burde returnet -3
        manager.generate("Fiskepudding", 75);
        assertEquals(manager.generate("Fiskepudding", 75), -1); //Riktig

    }

    @Test
    public void delete() {
        //Lager testobjekter som kan slettes
        manager.generate("Klubb", 60);
        int idKlubb = sql.getLastID();
        manager.generate("Møsbrømslfse", 75);
        int idMøs = sql.getLastID();

        //Sjekker at Klubb finnes, sletter klubb, og forsikrer seg om at Klubb er slettet
        assertTrue(sql.rowExists("HCL_food", "name", "Klubb"));
        manager.delete(idKlubb);
        assertFalse(sql.rowExists("HCL_food", "name", "Klubb"));

        //Sjekker at sletting gir riktig return.
        assertEquals(manager.delete(idMøs), 1); //Finnes, riktig
        assertEquals(manager.delete(379), -1); //Finnes ikke, riktig

    }

    @Test
    public void addIngredient(){
        IngredientManager iManager = new IngredientManager(sql);
        iManager.generate("Mandel", 5, 56, false, false, true, "kun en", "20160404", "20170506");
        int mandelID = sql.getLastID();

        manager.generate("Julegrøt", 60);
        int grøtID = sql.getLastID();
        manager.addIngredient(grøtID,mandelID,10);
        assertFalse(sql.rowExists("HCL_food_ingredient", "food_id","ingredient_id", grøtID, mandelID)); //feil!!!!!
        assertEquals(manager.addIngredient(grøtID, 379, 40), -3);
        assertEquals(manager.addIngredient(379, mandelID, 40), -3);
        assertEquals(manager.addIngredient(grøtID, mandelID, 0), -3);

    }

}
