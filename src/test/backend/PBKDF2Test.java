package backend;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by bahafeld on 30.03.2016...
 */
public class PBKDF2Test {
    private PBKDF2 testCrypt;
    private byte[] salt;

    @Before
    public void setUp() throws Exception {
        testCrypt = new PBKDF2();
        salt = testCrypt.generateSalt();
    }

    @After
    public void tearDown() throws Exception {
        PBKDF2 testCrypt = null;
        byte[] salt = null;

    }

    @Test
    public void authenticate() throws Exception {

        byte[] p = testCrypt.getEncryptedPassword("ThisIsCorrect", salt);
        byte[] wrongsalt = testCrypt.generateSalt();

        assertTrue(testCrypt.authenticate("ThisIsCorrect", p, salt));

        assertFalse(testCrypt.authenticate("This is false", p, salt));
        assertFalse(testCrypt.authenticate("", p, salt));
        assertFalse(testCrypt.authenticate("ThisIsCorrect", p, wrongsalt));

    }

    @Test
    public void getEncryptedPassword() throws Exception {
        boolean beEqual = true;
        boolean notEqual = false;
        byte[] test1 = testCrypt.getEncryptedPassword("ThisIsAPassword", salt);
        byte[] test2 = testCrypt.getEncryptedPassword("ThisIsAPassword", salt);
        byte[] test3 = testCrypt.getEncryptedPassword("ThisIsNot", salt);
            for (int i = 0; i < test1.length ; i++) {
                if(test1[i] != test2[i]){
                    beEqual = false;
                }
                if(test1[i] != test3[i]){
                    notEqual = true;
                }
                assertTrue(beEqual);
                assertTrue(notEqual);
            }
    }

    @Test
    /**
     Hard to test completely since it's a RNG, and thus should never be the same.
     but this test should compare the byte[] 1000000 times.
    */
    public void generateSalt() throws Exception {
        boolean notEqual = false;
        for (int j = 0; j < 1000000; j++) {
            byte[] test1 = testCrypt.generateSalt();
            for (int i = 0; i < test1.length ; i++) {
                if(test1[i] != salt[i]){
                    notEqual = true;
                }
                assertTrue(notEqual);
            }
        }
    }
}