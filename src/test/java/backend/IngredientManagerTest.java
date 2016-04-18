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
        //sql.end();
        manager = null;
    }

    @Test
    public void generate() {

        //Lager ingrediens, sjekker at den finnes, sjekker at ingrediens med falsk id ikke eksisterer
        int brunostID = manager.generate("Brunost", 5, 56, false, false, true, "kul mat", "2016-04-04", "2017-05-06");
        assertTrue(sql.rowExists("HCL_ingredient", "ingredient_id", brunostID));
        assertFalse(sql.rowExists("HCL_ingredient", "ingredient_id", 001));


        //Prøver å lage ukorrekte ingrediensobjekter, og sjekker om generate() sender riktig feilmelding
        assertEquals(-3,manager.generate (" ", 5, 56, false, false, true, "mac er best", "2016-04-04", "2017-05-06"));
        assertEquals(-3, manager.generate("Kyllingvinge ", -1, 40, false, false, true, "mac er best", "2016-04-04", "2017-05-06"));

        //Sjekker at et objekt ikke kan bli laget to ganger
        int ostID = manager.generate("Rødost", 5, 56, false, false, true, "mac er best", "2016-04-04", "2017-05-06");
        assertEquals(-1, manager.generate("Rødost", 5, 56, false, false, true, "mac er best", "2016-04-04", "2017-05-06"));

        manager.delete(brunostID);
        manager.delete(ostID);

    }

    @Test
    //Lager ingredient-objekter og henter ID
    public void edit() throws Exception {
        int vinID = manager.generate("riesling cabinett", 1, 104, false, true, false, "hei", "2017-06-06", "2018-06-06");
        int ølID = manager.generate("nordlands", 1, 32, false, true, false, "hei", "2017-06-06", "2018-06-06");

        //henter informasjon om vinen før og etter edit.
        String førSetning = "SELECT * from HCL_ingredient where ingredient_id = " + vinID;
        String[][] utskrift1  = sql.getStringTable(  førSetning , false  );
        for(int i = 0; i < utskrift1[0].length; i++){
            System.out.println(utskrift1[0][i]);
        } //Skriver ut "68, riesling cabinett, 1, 104, 0, 1, 0, hei, 2017-06-06, 2018-06-06"


            manager.edit(vinID, 2,99, "god vin!");

            String etterSetning = "SELECT * from HCL_ingredient where ingredient_id = " + vinID;
            String[][] utskrift2  = sql.getStringTable(  etterSetning , false  );
            for(int i = 0; i < utskrift2[0].length; i++){
                System.out.println(utskrift2[0][i]);
            } //Skriver ut "68, riesling cabinett, 2, 99, 0, 1, 0, god vin!, 2017-06-06, 2018-06-06"

            //Tester at ikke metoden kan bli brukt med feil
            assertEquals(-1, manager.edit(000, 10, 80, "hallo"));
            assertEquals(-3, manager.edit(ølID, -1, 80, "hallo" ));
            assertEquals(-3, manager.edit(ølID, 10, -1, "hallo" ));
            assertEquals(-3, manager.edit(ølID, 10, 80, "" ));


            manager.delete(ølID);
            manager.delete(vinID);
        }


/*
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
    }*/

}



