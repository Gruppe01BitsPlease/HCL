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
        manager.generate("Møsbrømslfse", 75);

        //Sjekker at Klubb finnes, sletter klubb, og forsikrer seg om at Klubb er slettet
        assertTrue(sql.rowExists("HCL_food", "name", "Klubb"));
        manager.delete("Klubb");
        assertFalse(sql.rowExists("HCL_food", "name", "Klubb"));

        //Sjekker at sletting gir riktig return.
        assertEquals(manager.delete("Møsbrømlefse"), 1); //Finnes, riktig
        assertEquals(manager.delete("Fuglefrø"), -1); //Finnes ikke, riktig

    }

    @Test
    public void addIngredient(){
        manager.generate("Julegrøt", 60);
        manager.generate("Lunsj", 75);

        //manager.addIngredient(int food_id, int ingredient_id, int gram)
    }

}
