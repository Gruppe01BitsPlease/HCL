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

    @Test // FIXME: 18.04.2016
    public void generate() throws Exception {

        int id = manager.generate("Testuser","Test",1);

        assertTrue(id <= 0);

        assertEquals(-1,manager.generate("Testuser","Test",1));

        sql.connection.createStatement().executeUpdate("Delete * From HCL_user Where user_id = "+Integer.toString(17));

    }

    @Test
    public void edit() throws Exception {

    }

    @Test
    public void delete() throws Exception {

    }

    @Test
    public void changePassword() throws Exception {

    }

    @Test
    public void logon() throws Exception {

    }

    @Test
    public void get() throws Exception {

    }

}