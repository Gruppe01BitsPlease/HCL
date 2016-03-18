package backend;

import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Faiter119 on 18.03.2016.
 * Creates links between databses by inserting into the help-tables
 */
public class LinkManager {

    private SQL sql;
    private String food_ingredient = "HCL_food_ingredient";
    private String order_food = "HCL_order_food";
    private String order_package = "HCL_order_pacakge";
    private String subscription_dates = "HCL_subscription_dates";

    public LinkManager(SQL sql) {
        this.sql = sql;
    }

    /**
     * @return 1: OK
     * -1: Already exists
     * -2: SQL Exception
     * -3: Wrong parameters
     */
    public int generate(String table, String PK1, String PK2, int value1, int value2){

        if(sql.rowExists(table,PK1,value1) && sql.rowExists(table,PK2,value2)) return -1;
        if(table.trim().equals("") || PK1.trim().equals("") || PK2.trim().equals("") || value1 <0 || value2 <0) return -3;

        String sqlPrep = "Insert into "+table+"(?,?) values(?,?)";
        try {
            PreparedStatement prep = sql.connection.prepareStatement(sqlPrep);
            prep.setString(1,PK1);
            prep.setString(2,PK2);
            prep.setInt(3,value1);
            prep.setInt(4,value2);
            prep.execute();
            return 1;
        }
        catch (SQLException e){return -2;}
    }
    public int delete(){

    }

    public static void main(String[]args){

        File file = null;
        try {
            file = new File(LinkManager.class.getResource("Database.ini").toURI().getPath(), true);
        }
        catch (URISyntaxException e){}
        Logon logon = new Logon(file);
        SQL sql = new SQL(logon);
        LinkManager link = new LinkManager(sql);

        //link.generate();

    }
}
