package backend;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by bahafeld on 30.03.2016...
 */
public class CustomerManagerTest {

    CustomerManager manager;
    SQL sql = new SQL();


    @Before
    public void setUp() throws Exception {
        manager = new CustomerManager(sql);
    }

    @After
    public void tearDown() throws Exception {
        manager = null;
        sql.end();
    }

    @Test
    public void generate() throws Exception{
        //Making test-object
        int custID = manager.generate("Kjell-Sture", "Sturegutten@hotmail.com", 75584788);

        //Testing if the Customerobject actually exist.
        assertTrue(sql.rowExists("HCL_customer", "customer_id", custID));
        //Testing that a fake object does not exist.
        assertFalse(sql.rowExists("HCL_customer", "customer_name", 99999999));

        //Trying to make fake objects and check if it return what is expected.
        assertEquals(-3, manager.generate("", "hei@nei.no", 12345678));
        assertEquals(-3, manager.generate("Astrid Anker-Hansen", "",87654321));
        assertEquals(-3, manager.generate("Jens-August Anker-Hansen","kulemeg@hotelcaesar.no", 0));

        sql.deleteForGood("HCL_customer", "customer_id", custID);



    }

    @Test
    //Lager customer-objekter og henter ID
    public void edit() throws Exception {
        //Making test-objects
        int cust2ID = manager.generate("LillePer", "Storegutten@hotmail.com", 75584788);
        int cust3ID = manager.generate("StorePer", "lillegutten@hotmail.com", 75584061);

        //Getting information about the
        String førSetning = "SELECT * from HCL_customer where customer_id = " + lilleperID;
        String[][] utskrift1  = sql.getStringTable(  førSetning , false  );
        for(int i = 0; i < utskrift1[0].length; i++){
            System.out.println(utskrift1[0][i]);
        } //Skriver ut "LillePer, storegutten@hotmail.com, 75584788

        manager.edit(lilleperID, "jobbmail@hotmail.com", 75584789);

        String etterSetning = "SELECT * from HCL_customer where customer_id = " + lilleperID;
        String[][] utskrift2  = sql.getStringTable(  etterSetning , false  );
                for(int i = 0; i < utskrift2[0].length; i++){
                    System.out.println(utskrift2[0][i]);
                } //Skriver ut "LillePer, jobbmail@hotmail.com, 75584789

        //Tester at ikke metoden kan bli brukt med feil
        assertEquals(-1, manager.edit(000, "mailadresse@mail.com", 75584788));
        assertEquals(-3, manager.edit(storeperID, "", 75584788));
        assertEquals(-3, manager.edit(storeperID, "mailadresse@mail.com", 0));

        sql.deleteForGood("HCL_customer", "customer_id", lilleperID);
        sql.deleteForGood("HCL_customer", "customer_id", storeperID);
    }

    @Test
    public void delete() throws Exception {
        //lager customerobjektet Kari
        int kariID = manager.generate("Kari", "kari@hotmail.com", 75583478);


        //Sjekker at Kari finnes, sletter Kari, sjekker at Kari faktisk er slettet
        assertTrue(sql.rowExists("HCL_customer", "customer_id", kariID));
        manager.delete(kariID);
        assertFalse(sql.rowExists("HCL_customer", "customer_id", kariID));

        //tester med gal/ingen ID
        assertEquals(-1,manager.delete(0));
        assertEquals(-1,manager.delete(99999999));

        sql.deleteForGood("HCL_customer", "customer_id", kariID);

    }
}