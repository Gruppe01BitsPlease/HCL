package backend;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

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
        testCrypt = null;
        salt = null;
    }

    @Test
    public void authenticate() throws Exception {
        byte[] p = testCrypt.getEncryptedPassword("ThisIsCorrect", salt);
        byte[] wrongSalt = testCrypt.generateSalt();

        assertTrue(testCrypt.authenticate("ThisIsCorrect", p, salt));

        assertFalse(testCrypt.authenticate("This is false", p, salt));
        assertFalse(testCrypt.authenticate("", p, salt));
        assertFalse(testCrypt.authenticate("ThisIsCorrect", p, wrongSalt));
    }

    @Test
    public void getEncryptedPassword() throws Exception {
        byte[] test1 = testCrypt.getEncryptedPassword("ThisIsAPassword", salt);
        byte[] test2 = testCrypt.getEncryptedPassword("ThisIsAPassword", salt);
        byte[] test3 = testCrypt.getEncryptedPassword("ThisIsNot", salt);
                assertTrue(Arrays.equals(test1, test2));
                assertTrue(!Arrays.equals(test1, test3));
    }

    @Test
    /**
     Hard to test completely since it's a RNG, and thus should never be the same.
     but this test should compare the byte[] 1000000 times. Max amount of combinations should be 2^64.
     In the end we find this testing good enough for our usecase considering the relative small number
     of people that would be using it. In addition you also need the correct pw to break the encryption.
    */
    public void generateSalt() throws Exception {
        for (int j = 0; j < 1000000; j++) {
            byte[] test1 = testCrypt.generateSalt();
            assertTrue(!Arrays.equals(salt, test1));
        }
    }
}