package backend;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by bahafeld on 14.04.2016...
 * For HCL
 */

public class SQLScriptReader {
    SQL sql;
    public SQLScriptReader(){
    sql = new SQL();
    }

    private String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }


    public void resetDatabaseWithScript(String path) throws SQLException, FileNotFoundException {
        try {
            //fetches script from resources folder
            InputStream in = getClass().getClassLoader().getResourceAsStream(path);
            if(in == null){
                throw new FileNotFoundException("Could not locate file at " + path);
            }

            String script = convertStreamToString(in);

            //removes any line that starts with "--", then removes and whitespace characters
            script = script.replaceAll("(?m)^--.*", "");
            script = script.replace("\r\n", "").replace("\n", "").replace("--", "");

            //Splits on every ";" to separate statements
            String[] sentence = script.split(";");

            Connection connection = sql.connection;
            Statement statement = connection.createStatement();

            for(int i = 0; i<sentence.length; i++) {
                // Removes empty, comment and null statements
                if(!sentence[i].trim().equals("") || !sentence[i].startsWith("--") || sentence[i] != null) {
                    System.out.print(">>"+sentence[i] + "\n");
                    statement.executeUpdate(sentence[i]);
                }
            }
            statement.close();
        }
        catch(SQLException e)
        {
            System.out.println("*** Error : "+e.toString());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
    }
}

