package backend;

/**
 * Created by trineliseolsen on 13.04.2016.
 */

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class FoodManagerTest {

    FoodManager manager;
    SQL sql = new SQL();


    @Before
    public void setUp() throws Exception {
        manager = new FoodManager(sql);
    }


    @After
    public void tearDown() throws Exception {
        manager = null;
    }

    @Test
    public void generate() throws Exception {
        manager.generate("Bløtkake", 200);
        manager.generate("Sirupssnipp", 20);
        manager.generate("Fiskepudding", 75);

        assertTrue(sql.rowExists("HCL_food", "name", "Bløtkake"));
        assertTrue(sql.rowExists("HCL_food", "name", "Sirupssnipp"));
        assertTrue(sql.rowExists("HCL_food", "name", "Fiskepudding"));

        assertEquals(manager.generate("", 1), -3);
        assertEquals(manager.generate("Pultost", 0), -3);
        assertEquals(manager.generate("Fiskepudding", 75), -3);

    }

    @Test
    public void edit() throws Exception {

    }

    @Test
    public void delete() throws Exception {

    }
}
