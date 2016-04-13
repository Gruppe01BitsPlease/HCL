package backend;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by trineliseolsen on 13.04.2016.
 */

public class IngredientManagerTest {

    IngredientManager manager;
    SQL sql = new SQL();


    @Before
    public void setUp() throws Exception {
        manager = new IngredientManager(sql);
    }


    @After
    public void tearDown() throws Exception {
        manager = null;
    }

    @Test
    public void generate() {

        manager.generate("Brunost", 5, 56, false, false, true, "kul mat", "20160404", "20170506");
        //assertTrue(sql.rowExists("HCL_ingredient", "name", "Brunost"));
        assertFalse(sql.rowExists("HCL_ingredient", "name", "vlu"));


        //Prøver å lage ukorrekte ingrediensobjekter, og sjekker om generate() sender riktig feilmelding
        assertEquals(manager.generate(" ", 5, 56, false, false, true, "mac er best", "20160404", "20170506"), -3);
        //assertEquals(manager.generate("Kyllingvinge ", 0, 40, false, false, true, "mac er best", "20160404", "20170506"), -3);

        manager.generate("Rødost", 5, 56, false, false, true, "mac er best", "20160404", "20170506");
        //assertEquals(manager.generate("Rødost", 5, 56, false, false, true, "mac er best", "20160404", "20170506"), -1); //Riktig

    }

   /* @Test
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
    }*/

}



