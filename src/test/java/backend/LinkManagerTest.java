package backend;

/**
 * Created by trineliseolsen on 18.04.2016.
 */

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class LinkManagerTest {

    SQL sql = new SQL();
    LinkManager manager;
    IngredientManager iManager;
    FoodManager fManager;


    @Before
    public void setUp() throws Exception {
        fManager = new FoodManager(sql);
        manager = new LinkManager(sql);
        iManager = new IngredientManager(sql);

    }


    @After
    public void tearDown() throws Exception {
        manager = null;
        sql.end();
    }

   /* @Test
    public void generate() {

        //Lager testobjekter fra food og ingredient-klassen
        int gjørmeID = iManager.generate("gjørme", 5, 56, false, false, true, "æsj", "2016-04-04", "2017-05-06");
        int søleID =  fManager.generate("sølekake", 60);

        //prøver å generere et linkobjekt
        int linkID = manager.generate("HCL_food_ingredient", "food_id","ingredient_id", søleID, gjørmeID, 20);

        //Tester om linkobjektene faktisk ble laget.
        assertTrue(sql.rowExists("HCL_food_ingredient", "food_id","ingredient_id", søleID, gjørmeID));
        //Sjekker at uekte food-objekt ikke eksisterer
        assertFalse(sql.rowExists("HCL_food_ingredient", "food_id","ingredient_id", 1010, 1010));

        //Prøver å lage ukorrekte foodobjekter, og sjekker om generate() sender riktig feilmelding
        assertEquals(-3, manager.generate("", 1));
        assertEquals(-3, manager.generate("Pultost",-1));

        manager.delete(kakeID);
        manager.delete(snippID);
    }

    @Test
    public void delete() {
        //Lager testobjekter som kan slettes
        int idKlubb = manager.generate("Klubb", 60);
        int idMøs = manager.generate("Møsbrømslfse", 75);

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
        int mandelID = iManager.generate("mandel", 5, 56, false, false, true, "kun en", "2016-04-04", "2017-05-06");
        //Lager mat-objekt og henter ID, samt legger mandel i grøten.
        int grøtID =  manager.generate("Grøt", 60);
        manager.addIngredient(grøtID,mandelID,10);

        //Sjekker at mandelen ligger i grøten
        assertTrue(sql.rowExists("HCL_food_ingredient", "food_id","ingredient_id", grøtID, mandelID));
        //Sjekker at alle feilmeldingene fungerer
        assertEquals(-4, manager.addIngredient(grøtID, 379, 40));
        assertEquals(-4, manager.addIngredient(379, mandelID, 40));
        assertEquals(-3, manager.addIngredient(grøtID, mandelID, -1));


        lManager.delete("HCL_food_ingredient", "food_id","ingredient_id", grøtID, mandelID);
        iManager.delete(mandelID);
        manager.delete(grøtID);

    }*/

}

