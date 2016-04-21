package backend;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by bahafeld on 20.04.2016...
 * Can't come up with a good testpattern for this class, since we only have one database to work with.
 * Running all the scripts will delete all our data.
 */
public class SQLScriptReaderTest {


    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void runDatabaseScript() throws Exception {
        SQLScriptReader test = new SQLScriptReader();
    }

}