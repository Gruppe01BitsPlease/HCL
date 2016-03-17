package backend;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Faiter119 on 15.03.2016.
 */
public class CustomerManager {

    private SQL sql;
    public static final String CURRENT_TABLE = "HCL_customer";
    public static final String CURRENT_TABLE_GENERATE_ARGUMENTS = "(name, epost, tlf)";
    public static final String CURRENT_TABLE_DELETE_ARGUMENTS = "(name)";

    public CustomerManager(SQL sql){
        this.sql = sql;
    }

    /**
     * @return 1: OK
     * -1: Already exists
     * -2: SQL Exception
     * -3: Wrong parameters
     */
    public int generate(String name, String epost, int tlf) {

        if(sql.rowExists(CURRENT_TABLE, "name",name)) return -1;
        if(!(name.trim().length() > 0) || !(tlf >= 0) || epost.trim().equals("")) return -3; //invalid parameters

        try {

            String sqlPrep = "INSERT INTO "+CURRENT_TABLE+CURRENT_TABLE_GENERATE_ARGUMENTS+" VALUES(?,?,?)";
            PreparedStatement prep = sql.connection.prepareStatement(sqlPrep);

            prep.setString(1,name);
            prep.setString(2,epost);
            prep.setInt(3,tlf);

            prep.executeUpdate();
            return 1;
        }
        catch (SQLException e){return -2;}
    }

    /**
     * @return 1: OK
     * -1: Already exists
     * -2: SQL Exception
     * -3: Wrong parameters
     */
    public int edit(String name, String nyEpost, int nyTlf){

        if(!sql.rowExists(CURRENT_TABLE,"name",name))return -1;
        if(name.trim().equals("") || nyEpost.trim().equals("") || nyTlf < 0) return -3;

        sql.update(CURRENT_TABLE,"epost","name",name,nyEpost);
        sql.update(CURRENT_TABLE,"tlf","name",name,nyTlf);
        return 1;
    }

    /**
     * @return 1: OK
     * -1: Already exists
     * -2: SQL Exception
     * -3: Wrong parameters
     */
    public int delete(String name) {
        try {
            String sqlPrep = "DELETE FROM "+CURRENT_TABLE+" WHERE "+CURRENT_TABLE_DELETE_ARGUMENTS+" = ?";
            PreparedStatement prep = sql.connection.prepareStatement(sqlPrep);
            prep.setString(1,name);
            int row = prep.executeUpdate();

            if(row == 0)
                return -1;

            return 1;
        }
        catch (SQLException e){return -2;}
    }

    public static void main(String[]args){
        File file = null;

        try {
            file = new File(CustomerManager.class.getResource("Database.ini").toURI().getPath(), true);
        }
        catch (Exception e){}

        Logon logon = new Logon(file);
        SQL sql = new SQL(logon);
        CustomerManager c = new CustomerManager(sql);

        //  System.out.println(ingredient.generate("Ost",100,10,false,false,true,"","2016-03-15","2016-03-16"));
        c.generate("Ostost","Swag@gmail.com",145678);
        //System.out.println(c.delete("Microsoft"));
       // System.out.println(c.edit("Cheese",10,200,""));
    }
}
