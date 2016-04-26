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

        manager = null;
        sql.end();
    }

    @Test
    public void generate() throws Exception {


        int userID = manager.generate("Nils", "nils", 1);
        int user2ID = manager.generate("Oda", "oda", 1);

        //checks that the users were made.
        assertTrue(sql.rowExists("HCL_user", "user_id", userID));
        assertTrue(sql.rowExists("HCL_user", "user_id", user2ID));
        assertFalse(sql.rowExists("HCL_user", "user_id", 99999999));

        //checks return on fake objects.
        assertEquals(-3, manager.generate("", "hei", 1));
        assertEquals(-3, manager.generate("Astrid Anker-Hansen", "",1));
        assertEquals(-3, manager.generate("Jens-August Anker-Hansen","kulemeg@hotelcaesar.no", 5));

        sql.deleteForGood("HCL_user", "user_id", userID);
        sql.deleteForGood("HCL_user", "user_id", user2ID);

    }

    @Test
    public void edit() throws Exception {

        int userID = manager.generate("Randi", "randi", 1);

        //checks the information about the user before and after edit
        String førSetning = "SELECT * from HCL_user where user_id = " + userID;
        String[][] utskrift1  = sql.getStringTable(  førSetning , false  );
        for(int i = 0; i < utskrift1[0].length; i++){
            System.out.println(utskrift1[0][i]);
        } //prints Randi, 1 + kryptert passord og masse null-verdier

        manager.edit("Randi",1, "Randi", "Bottolfsen", "jobbmail@hotmail.com", 75584789, "Tormods vei 1", 7030, "2016-05-05");

        String etterSetning = "SELECT * from HCL_user where user_id = " + userID;
        String[][] utskrift2  = sql.getStringTable(  etterSetning , false  );
        for(int i = 0; i < utskrift2[0].length; i++){
            System.out.println(utskrift2[0][i]);
        } //prints "Randi, 1, kryptert passord, Randi, Bottolfsen, jobbmail@hotmai.com, 75584788, Tormods vei 1, 7030, 2016-05-05, 1


        //Testing that wrong parameters give correct return.
        assertEquals(-1, manager.edit("", 1, "Randi", "Bottolfsen", "jobbmail@hotmail.com", 75584789, "Tormods vei 1", 7030, "2016-05-05"));
        assertEquals(-3, manager.edit("Randi",1, "Randi", "Bottolfsen", "jobbmail@hotmail.com", 75584789, "Tormods vei 1", 7030, "0"));


        sql.deleteForGood("HCL_user", "user_id", userID);

    }

    @Test
    public void delete() throws Exception {
        int user3ID = manager.generate("Morten", "morten", 1);


        //checks if the object exists before and after delete.
        assertTrue(sql.rowExists("HCL_user", "user_id", user3ID));
        manager.delete("Morten");
        assertFalse(sql.rowExists("HCL_user", "user_id", user3ID));

        //tests with wrong/no ID
        assertEquals(-1,manager.delete("Morten"));
        assertEquals(-1,manager.delete(""));

        sql.deleteForGood("HCL_user", "user_id", user3ID);

    }

    @Test
    public void changePassword() throws Exception {
        //making test-object
        int user4ID = manager.generate("Per", "per", 1);
        //tries to changes password twice
        manager.changePassword("Per", "per", "rep");
        manager.changePassword("Per", "rep", "per");

        //checks for correct return
        assertEquals(-1,manager.changePassword("", "rep", "per"));
        assertEquals(1,manager.changePassword("Per", "per", "rep"));
        assertEquals(-3,manager.changePassword("Per", "august", "juni"));

        sql.deleteForGood("HCL_user", "user_id", user4ID);

    }

    @Test
    public void logon() throws Exception {
        int user5ID = manager.generate("Kris", "kris", 1);
        int user6ID = manager.generate("Kristine", "kristine", 2);

        //checks for correct return
        assertEquals(1,manager.logon("Kris", "kris"));
        assertEquals(2,manager.logon("Kristine", "kristine"));
        assertEquals(-1,manager.logon("", "kris"));
        assertEquals(-1,manager.logon("Kris", "suppe"));

        sql.deleteForGood("HCL_user", "user_id", user5ID);
        sql.deleteForGood("HCL_user", "user_id", user6ID);

    }

    @Test
    public void get() throws Exception {


        String[][] testTrue  = manager.get(true);
        for(int i = 0; i < testTrue.length; i++){
            for (int j = 0; j < testTrue[i].length; j++){
                System.out.println(testTrue[i][j]);
            }
        }

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
        //prints out a random word

    }

    @Test
    public void generateRandomPassword(){
        //making test-object
        int user7ID = manager.generate("Mai", "mai", 1);

        //making a new, random password and sees if it can be used.
        String password = manager.generateRandomPassword("Mai");
        manager.changePassword("Mai", password, "mai");

        sql.deleteForGood("HCL_user", "user_id", user7ID);
    }

}