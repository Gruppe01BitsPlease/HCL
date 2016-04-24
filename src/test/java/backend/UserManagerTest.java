package backend;

import org.junit.*;

import static org.junit.Assert.*;

/**
 * Created by Faiter119 on 18.04.2016.
 */
public class UserManagerTest {

    SQL sql;
    UserManager manager;

    @Before
    public void setUp() throws Exception {
        sql = new SQL();
        manager = new UserManager(sql);

    }

    @After
    public void tearDown() throws Exception {
        sql.end();
        manager = null;
    }

    @Test
    public void generate() throws Exception {


        int idNils = manager.generate("Nils", "nils", 1);
        int idOda = manager.generate("Oda", "oda", 1);

        //Tester om Customer-objektene faktisk ble laget.
        assertTrue(sql.rowExists("HCL_user", "user_id", idNils));
        assertTrue(sql.rowExists("HCL_user", "user_id", idOda));
        assertFalse(sql.rowExists("HCL_user", "user_id", 1111111));

        //Prøver å lage ukorrekte Customer-objekter, og sjekker om generate() sender riktig feilmelding
        assertEquals(-3, manager.generate("", "hei", 1));
        assertEquals(-3, manager.generate("Astrid Anker-Hansen", "",1));
        assertEquals(-3, manager.generate("Jens-August Anker-Hansen","kulemeg@hotelcaesar.no", 5));

        sql.deleteForGood("HCL_user", "user_id", idNils);
        sql.deleteForGood("HCL_user", "user_id", idOda);

    }

    @Test
    public void edit() throws Exception {

        int idRandi = manager.generate("Randi", "randi", 1);

        //henter informasjon om LillePer før og etter edit.
        String førSetning = "SELECT * from HCL_user where user_id = " + idRandi;
        String[][] utskrift1  = sql.getStringTable(  førSetning , false  );
        for(int i = 0; i < utskrift1[0].length; i++){
            System.out.println(utskrift1[0][i]);
        } //Skriver ut Randi, 1 + kryptert passord og masse null-verdier

        manager.edit("Randi",1, "Randi", "Bottolfsen", "jobbmail@hotmail.com", 75584789, "Tormods vei 1", 7030, "2016-05-05");

        String etterSetning = "SELECT * from HCL_user where user_id = " + idRandi;
        String[][] utskrift2  = sql.getStringTable(  etterSetning , false  );
        for(int i = 0; i < utskrift2[0].length; i++){
            System.out.println(utskrift2[0][i]);
        } //Skriver ut "Randi, 1, kryptert passord, Randi, Bottolfsen, jobbmail@hotmai.com, 75584788, Tormods vei 1, 7030, 2016-05-05, 1


        //Tester at ikke metoden kan bli brukt med feil
        assertEquals(-1, manager.edit("", 1, "Randi", "Bottolfsen", "jobbmail@hotmail.com", 75584789, "Tormods vei 1", 7030, "2016-05-05"));
        assertEquals(-3, manager.edit("Randi",1, "Randi", "Bottolfsen", "jobbmail@hotmail.com", 75584789, "Tormods vei 1", 7030, "0"));



        sql.deleteForGood("HCL_user", "user_id", idRandi);

    }

    @Test
    public void delete() throws Exception {
        int idMorten = manager.generate("Morten", "morten", 1);


        //Sjekker at Kari finnes, sletter Kari, sjekker at Kari faktisk er slettet
        assertTrue(sql.rowExists("HCL_user", "user_id", idMorten));
        manager.delete("Morten");
        assertFalse(sql.rowExists("HCL_customer", "customer_id", idMorten));

        //tester med gal/ingen ID
        assertEquals(-1,manager.delete("Morten"));
        assertEquals(-1,manager.delete(""));

        sql.deleteForGood("HCL_customer", "customer_id", idMorten);

    }

    @Test
    public void changePassword() throws Exception {
        int idPer = manager.generate("Per", "per", 1);
        manager.changePassword("Per", "per", "rep");
        manager.changePassword("Per", "rep", "per");

        assertEquals(-1,manager.changePassword("", "rep", "per"));
        assertEquals(1,manager.changePassword("Per", "per", "rep"));
        assertEquals(-3,manager.changePassword("Per", "august", "juni"));

        sql.deleteForGood("HCL_customer", "customer_id", idPer);

    }

    @Test
    public void logon() throws Exception {
        int idKris = manager.generate("Kris", "kris", 1);
        int idKristine = manager.generate("Kristine", "kristine", 2);

        assertEquals(1,manager.logon("Kris", "kris"));
        assertEquals(2,manager.logon("Kristine", "kristine"));
        assertEquals(-1,manager.logon("", "kris"));
        assertEquals(-1,manager.logon("Kris", "suppe"));

        sql.deleteForGood("HCL_customer", "customer_id", idKris);
        sql.deleteForGood("HCL_customer", "customer_id", idKristine);

    }

    @Test
    public void get() throws Exception {

        manager.get(true);
        String[][] testTrue  = manager.get(true);
        for(int i = 0; i < testTrue.length; i++){
            for (int j = 0; j < testTrue[i].length; j++){
                System.out.println(testTrue[i][j]);
            }
        }

        manager.get(false);
        String[][] testFalse  = manager.get(false);
        for(int i = 0; i < testFalse.length; i++){
            for (int j = 0; j < testFalse[i].length; j++){
                System.out.println(testFalse[i][j]);
            }
        }


    }

    @Test
    public void getRandomString(){
        System.out.println(manager.getRandomString());

    }

    @Test
    public void generateRandomPassword(){
        int idMai = manager.generate("Mai", "mai", 1);
        String password = manager.generateRandomPassword("Mai");
        manager.changePassword("Mai", password, "mai");

        sql.deleteForGood("HCL_customer", "customer_id", idMai);
    }

}