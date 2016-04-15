package backend;


        import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by bahafeld on 14.04.2016...
 * For HCL
 */

public class DatabaseReset {
    SQL sql;
    public DatabaseReset(){
    sql = new SQL();
    }

    private String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }


    public void resetDatabaseWithScript(String path) throws SQLException
    {
        String s;
        StringBuffer sb = new StringBuffer();

        try
        {
            InputStream in = getClass().getClassLoader().getResourceAsStream(path);
            String myString = convertStreamToString(in);
            myString = myString.replaceAll("(?m)^--.*", "");
            myString = myString.replace("\r\n", "").replace("\n", "").replace("--", "");

            //System.out.println(myString);

            // here is our splitter ! We use ";" as a delimiter for each request
            // then we are sure to have well formed statements
            String[] inst = myString.split(";");

            Connection c = sql.connection;
            Statement st = c.createStatement();

            for(int i = 0; i<inst.length; i++)
            {
                // we ensure that there is no spaces before or after the request string
                // in order to not execute empty statements
                if(!inst[i].trim().equals("") || !inst[i].startsWith("--") || inst[i] != null)
                {
                    System.out.println(">>"+inst[i]);
                    st.executeUpdate(inst[i]);

                }
            }
            st.close();

        }
        catch(Exception e)
        {
            System.out.println("*** Error : "+e.toString());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        DatabaseReset test = new DatabaseReset();
        String path = "sqlscripts/tables.sql";
        test.resetDatabaseWithScript(path);
        path = "sqlscripts/views.sql";
        test.resetDatabaseWithScript(path);
        path = "sqlscripts/randomdata.sql";
        test.resetDatabaseWithScript(path);
    }
}

