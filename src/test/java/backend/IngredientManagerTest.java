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
        sql.end();
        manager = null;
    }

    @Test
    public void generate() {

        //Lager ingrediens, sjekker at den finnes, sjekker at ingrediens med falsk id ikke eksisterer
        int brunostID = manager.generate("Brunost", 5, 56, false, false, true, "kul mat", "2016-04-04", "2017-05-06");
        assertTrue(sql.rowExists("HCL_ingredient", "ingredient_id", brunostID));
        assertFalse(sql.rowExists("HCL_ingredient", "ingredient_id", 010101));


        //Prøver å lage ukorrekte ingrediensobjekter, og sjekker om generate() sender riktig feilmelding
        assertEquals(-3,manager.generate (" ", 5, 56, false, false, true, "mac er best", "2016-04-04", "2017-05-06"));
        assertEquals(-3, manager.generate("Kyllingvinge ", -1, 40, false, false, true, "mac er best", "2016-04-04", "2017-05-06"));

        //Sjekker at et objekt ikke kan bli laget to ganger
        int ostID = manager.generate("Rødost", 5, 56, false, false, true, "mac er best", "2016-04-04", "2017-05-06");
        assertEquals(-1, manager.generate("Rødost", 5, 56, false, false, true, "mac er best", "2016-04-04", "2017-05-06"));

        sql.deleteForGood("HCL_ingredient", "ingredient_id", brunostID);
        sql.deleteForGood("HCL_ingredient", "ingredient_id", ostID);

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


        sql.deleteForGood("HCL_ingredient", "ingredient_id", vinID);
        sql.deleteForGood("HCL_ingredient", "ingredient_id", ølID);
        }



   @Test
    public void delete() {
        //Lager testobjekter som kan slettes
       int tranID = manager.generate("tran", 1, 60, false, false, false, "tas med teskje", "2017-06-06", "2018-06-06");
       int sanasolID = manager.generate("nordlands", 1, 56, false, true, false, "vitaminer", "2017-06-06", "2018-06-06");

        //Sjekker at tran finnes, sletter tran, og forsikrer seg om at tran er slettet
        assertTrue(sql.rowExists("HCL_ingredient", "name", "tran"));
        manager.delete(tranID);
        assertFalse(sql.rowExists("HCL_ingredient", "name", "tran"));

        //Sjekker at sletting gir riktig return.
        assertEquals(1, manager.delete(sanasolID)); //Finnes, riktig
        assertEquals(-1, manager.delete(5550)); //Finnes ikke, riktig

       sql.deleteForGood("HCL_ingredient", "ingredient_id", sanasolID);
       sql.deleteForGood("HCL_ingredient", "ingredient_id", tranID);

    }

    @Test
    public void addStock(){

        int roseID = manager.generate("rose", 5, 50, false, false, false, "liten bukett", "2017-06-06", "2018-06-06");
        int tulipanID = manager.generate("tulipan", 7, 50, false, false, false, "liten bukett", "2017-06-06", "2018-06-06");

        //henter informasjon om antall roser før og etter edit.
        String førSetning = "SELECT stock, name from HCL_ingredient where ingredient_id = " + roseID;
        String[][] utskrift1  = sql.getStringTable(  førSetning , false  );
        for(int i = 0; i < utskrift1[0].length; i++){
            System.out.println(utskrift1[0][i]);
        } //Skriver ut "5, rose"


        manager.addStock(roseID, 14);

        String etterSetning = "SELECT stock, name from HCL_ingredient where ingredient_id = " + roseID;
        String[][] utskrift2  = sql.getStringTable(  etterSetning , false  );
        for(int i = 0; i < utskrift2[0].length; i++){
            System.out.println(utskrift2[0][i]);
        } //Skriver ut "19, rose"


        //Sjekker at det sendes siktig return
        assertEquals(1, manager.addStock(tulipanID, 14)); //Finnes, riktig
        assertEquals(-1, manager.addStock(1010, 14)); //Finnes ikke, riktig
        assertEquals(-3, manager.addStock(tulipanID, 0)); //Feil parameter, riktig


        sql.deleteForGood("HCL_ingredient", "ingredient_id", roseID);
        sql.deleteForGood("HCL_ingredient", "ingredient_id", tulipanID);

    }


    @Test
    public void removeStock(){

        int hestehovID = manager.generate("hestehov", 5, 50, false, false, false, "liten bukett", "2017-06-06", "2018-06-06");
        int løvetannID = manager.generate("løvetann", 7, 50, false, false, false, "liten bukett", "2017-06-06", "2018-06-06");

        //henter informasjon om antall hetehover før og etter edit.
        String førSetning = "SELECT stock, name from HCL_ingredient where ingredient_id = " + hestehovID;
        String[][] utskrift1  = sql.getStringTable(  førSetning , false  );
        for(int i = 0; i < utskrift1[0].length; i++){
            System.out.println(utskrift1[0][i]);
        } //Skriver ut "5, hestehov"


        manager.removeStock(hestehovID, 2);

        String etterSetning = "SELECT stock, name from HCL_ingredient where ingredient_id = " + hestehovID;
        String[][] utskrift2  = sql.getStringTable(  etterSetning , false  );
        for(int i = 0; i < utskrift2[0].length; i++){
            System.out.println(utskrift2[0][i]);
        } //Skriver ut "3, hestehov"


        //Sjekker at det sendes siktig return
        assertEquals(1, manager.removeStock(løvetannID, 1)); //Finnes, riktig
        assertEquals(-1, manager.removeStock(1010, 14)); //Finnes ikke, riktig
        assertEquals(-3, manager.removeStock(løvetannID, 0)); //Ugyldig parameter, riktig
        assertEquals(-3, manager.removeStock(løvetannID, 20)); //Ugyldig parameter, riktig


        sql.deleteForGood("HCL_ingredient", "ingredient_id", løvetannID);
        sql.deleteForGood("HCL_ingredient", "ingredient_id", hestehovID);

    }





}



