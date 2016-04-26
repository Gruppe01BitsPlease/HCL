package backend;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by bahafeld on 18.04.2016...
 * For HCL
 */
public class SettingsFileTest {
    private SettingsFile testFile;

    @Before
    public void setUp() throws Exception {
        testFile = new SettingsFile();
        testFile.setPropValue("firsttime", "0");
        testFile.setPropValue("host", "jdbc:mysql://mysql.stud.iie.ntnu.no:3306/");
        testFile.setPropValue("database", "bjornhaf");
        testFile.setPropValue("user", "bjornhaf");
        testFile.setPropValue("password", "olNWRNkV");
    }

    @After
    public void tearDown() throws Exception {
        testFile.setPropValue("firsttime", "0");
        testFile.setPropValue("host", "jdbc:mysql://mysql.stud.iie.ntnu.no:3306/");
        testFile.setPropValue("database", "bjornhaf");
        testFile.setPropValue("user", "bjornhaf");
        testFile.setPropValue("password", "olNWRNkV");
        testFile = null;
    }

    @Test
    public void getPropValue() throws Exception {
        assertTrue(testFile.getPropValue("firsttime").equals("0"));
        assertTrue(testFile.getPropValue("host").equals("jdbc:mysql://mysql.stud.iie.ntnu.no:3306/"));
        assertTrue(testFile.getPropValue("database").equals("bjornhaf"));
        assertTrue(testFile.getPropValue("user").equals("bjornhaf"));
        assertTrue(testFile.getPropValue("password").equals("olNWRNkV"));
    }

    @Test
    public void setPropValue() throws Exception {
        assertTrue(testFile.setPropValue("firsttime", "1"));
        assertTrue(testFile.setPropValue("host", "testhost"));
        assertTrue(testFile.setPropValue("database", "testbase"));
        assertTrue(testFile.setPropValue("user", "testuser"));
        assertTrue(testFile.setPropValue("password", "testpass"));
        assertTrue(testFile.setPropValue("EmptySetting", ""));
        assertFalse(testFile.setPropValue(null,null));
        assertFalse(testFile.setPropValue("","thisshouldnotwork"));
    }
}