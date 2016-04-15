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

        //lager food-objekter
        manager.generate("Bløtkake", 75);
        manager.generate("Sirupssnipp", 75);

        //Tester om foodobjektene faktisk ble laget.
        assertTrue(sql.rowExists("HCL_food", "name", "Bløtkake"));
        assertTrue(sql.rowExists("HCL_food", "name", "Sirupssnipp"));
        //Sjekker at uekte food-objekt ikke eksisterer
        assertFalse(sql.rowExists("HCL_food", "name", "Mølje"));

        //Prøver å lage ukorrekte foodobjekter, og sjekker om generate() sender riktig feilmelding
        assertEquals(-3, manager.generate("", 1));
        assertEquals(-3, manager.generate("Pultost",-1));
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
        assertEquals(1, manager.delete(idMøs)); //Finnes, riktig
        assertEquals(-1, manager.delete(379)); //Finnes ikke, riktig

    }

    @Test
    public void addIngredient(){
        //Lager ny ingrediens, henter ID
        IngredientManager iManager = new IngredientManager(sql);
        int mandelID = iManager.generate("Mandel", 5, 56, false, false, true, "kun en", "2016-04-04", "2017-05-06");
        //Lager mat-objekt og henter ID, samt legger mandel i grøten.
        int grøtID =  manager.generate("Julegrøt", 60);
        manager.addIngredient(grøtID,mandelID,10);

        System.out.println(mandelID+" - "+grøtID);

        assertTrue(sql.rowExists("HCL_food_ingredient", "food_id","ingredient_id", grøtID, mandelID)); //feil!!!!!
        assertEquals(-4, manager.addIngredient(grøtID, 379, 40));
        assertEquals(-4, manager.addIngredient(379, mandelID, 40));
        assertEquals(-4, manager.addIngredient(grøtID, mandelID, 0));

    }

}
