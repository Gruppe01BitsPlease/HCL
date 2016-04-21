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

    @Test
    public void generate() throws Exception {

        //Lager testobjekter fra food og ingredient-klassen
        int gjørmeID = iManager.generate("gjørme", 5, 56, false, false, true, "æsj", "2016-04-04", "2017-05-06");
        int søleID =  fManager.generate("sølekake", 60);

        //prøver å generere et linkobjekt
        manager.generate("HCL_food_ingredient", "food_id","ingredient_id", søleID, gjørmeID, 20);

        //Tester om linkobjektene faktisk ble laget.
        assertTrue(sql.rowExists("HCL_food_ingredient", "food_id","ingredient_id", søleID, gjørmeID));
        //Sjekker at uekte link-objekter ikke eksisterer
        assertFalse(sql.rowExists("HCL_food_ingredient", "food_id","ingredient_id", 1010, gjørmeID));
        assertFalse(sql.rowExists("HCL_food_ingredient", "food_id","ingredient_id", søleID, 1010));

        //Prøver å lage ukorrekte linkobjekter, og sjekker om generate() sender riktig feilmelding
        assertEquals(-3, manager.generate("", "food_id","ingredient_id", søleID, gjørmeID, 20));
        assertEquals(-3, manager.generate("HCL_food_ingredient", "","ingredient_id", søleID, gjørmeID, 20));
        assertEquals(-3, manager.generate("HCL_food_ingredient", "food_id","", søleID, gjørmeID, 20));
        assertEquals(-3, manager.generate("HCL_food_ingredient", "food_id","ingredient_id", -1, gjørmeID, 20));
        assertEquals(-3, manager.generate("HCL_food_ingredient", "food_id","ingredient_id", søleID, -1, 20));



        sql.deleteForGood("HCL_food_ingredient", "food_id","ingredient_id", søleID, gjørmeID);
        sql.deleteForGood("HCL_ingredient", "ingredient_id", gjørmeID);
        sql.deleteForGood("HCL_food", "food_id", gjørmeID);
    }

    @Test
    public void delete() throws Exception {
        int kakestrøID = iManager.generate("kakestrø", 5, 56, false, false, true, "æsj", "2016-04-04", "2017-05-06");
        int muffinsstrøID = iManager.generate("muffinsstrø", 5, 56, false, false, true, "æsj", "2016-04-04", "2017-05-06");
        int kakeID =  fManager.generate("kake", 60);
        int muffinsID =  fManager.generate("muffins", 60);
        manager.generate("HCL_food_ingredient", "food_id","ingredient_id", kakeID, kakestrøID, 20);


        //Sjekker at kake-kakestrø finnes, sletter kake-kaktrø, og forsikrer seg om at kake-kakestrø er slettet
        //assertTrue(sql.rowExists("HCL_food_ingredient", "food_id","ingredient_id", kakeID, kakestrøID));
        //manager.delete("HCL_food_ingredient", "food_id","ingredient_id", kakeID, kakestrøID);
        //assertFalse(sql.rowExists("HCL_food_ingredient", "food_id","ingredient_id", kakeID, kakestrøID));

        //Sjekker at sletting gir riktig return.
        assertEquals(-1, manager.delete("HCL_food_ingredient", "food_id","ingredient_id", muffinsID, muffinsstrøID)); //Finnes ikke, riktig
        assertEquals(-3, manager.delete("", "food_id","ingredient_id", muffinsID, muffinsstrøID));
        assertEquals(-3, manager.delete("HCL_food_ingredient", "","ingredient_id", muffinsID, muffinsstrøID));
        assertEquals(-3, manager.delete("HCL_food_ingredient", "food_id","", muffinsID, muffinsstrøID));
        assertEquals(-3, manager.delete("HCL_food_ingredient", "food_id","ingredient_id", -1, muffinsstrøID));
        assertEquals(-3, manager.delete("HCL_food_ingredient", "food_id","ingredient_id", muffinsID, -1));
        manager.generate("HCL_food_ingredient", "food_id","ingredient_id", muffinsID, muffinsstrøID, 20);
        assertEquals(1, manager.delete("HCL_food_ingredient", "food_id","ingredient_id", muffinsID, muffinsstrøID)); //Feilmelding!!!!!

        sql.deleteForGood("HCL_food_ingredient", "food_id","ingredient_id", kakeID, kakestrøID);
        sql.deleteForGood("HCL_food_ingredient", "food_id","ingredient_id", muffinsID, muffinsstrøID);
        sql.deleteForGood("HCL_ingredient", "ingredient_id", kakestrøID);
        sql.deleteForGood("HCL_food", "food_id", kakeID);
        sql.deleteForGood("HCL_ingredient", "ingredient_id", muffinsstrøID);
        sql.deleteForGood("HCL_food", "food_id", muffinsID);


    }

    @Test
    public void editNumber() throws Exception{
        int frøID = iManager.generate("frø", 5, 56, false, false, true, "æsj", "2016-04-04", "2017-05-06");
        int brødID =  fManager.generate("frøbrød", 60);
        System.out.println(frøID + "   " + brødID);
        manager.generate("HCL_food_ingredient", "food_id","ingredient_id", brødID, frøID, 5);

        String førSetning = "SELECT number from HCL_food_ingredient where ingredient_id = " + frøID + "AND food_id = " + brødID;
        String[][] utskrift  = sql.getStringTable(  førSetning , false  );
        for(int i = 0; i < utskrift[0].length; i++){
            System.out.println(utskrift[0][i]);
        } //Skriver ut "19, rose"

        manager.editNumber("HCL_food_ingredient", "food_id","ingredient_id", brødID, frøID, 10);

        String etterSetning = "SELECT number from HCL_food_ingredient where ingredient_id = " + frøID + "AND food_id = " + brødID;
        String[][] utskrift2  = sql.getStringTable(  etterSetning , false  );
        for(int i = 0; i < utskrift2[0].length; i++){
            System.out.println(utskrift2[0][i]);
        } //Skriver ut "19, rose"



    }

}

