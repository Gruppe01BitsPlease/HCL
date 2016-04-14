package backend;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Faiter119 on 15.03.2016.
 */
public class PackageManager {

    private SQL sql;
    public static final String CURRENT_TABLE = "HCL_package";
    public static final String CURRENT_TABLE_LINK_FOOD = "HCL_package_food";


    public static final String CURRENT_TABLE_GENERATE_ARGUMENTS = "(name, price)";
    public static final String CURRENT_TABLE_PK = "(package_id)";
    public static final String CURRENT_TABLE_ADD_FOOD_ARGUMENTS = "(package_id, food_id, number)";


    public PackageManager(SQL sql){
        this.sql = sql;
    }

    /**
     * @return
     *  1: OK
     * -1: Already exists
     * -2: SQL Exception
     * -3: Wrong parameters
     */
    public int generate(String name, int price) {

        if(!(name.trim().length() > 0) || !(price >= 0) ) return -3;

        try {

            String sqlPrep = "INSERT INTO "+CURRENT_TABLE+CURRENT_TABLE_GENERATE_ARGUMENTS+" VALUES(?,?)";
            PreparedStatement prep = sql.connection.prepareStatement(sqlPrep);

            prep.setString(1,name);
            prep.setInt(2,price);
            prep.executeUpdate();
            return sql.getLastID();
        }
        catch (SQLException e){return -2;}
    }

    /**
     * @return 1: OK
     * -1: Row does not  exist
     * -2: SQL Exception
     */
    public int delete(int package_id) {

        if(!sql.rowExists(CURRENT_TABLE,CURRENT_TABLE_PK,package_id)) return -1;

        try {

            String sqlPrep = "DELETE FROM "+CURRENT_TABLE+" WHERE "+CURRENT_TABLE_PK+" = ?";
            PreparedStatement prep = sql.connection.prepareStatement(sqlPrep);
            prep.setInt(1,package_id);
            prep.executeUpdate();
            return 1;
        }
        catch (SQLException e){
            e.printStackTrace();
            return -2;
        }
    }
    /**
     * TODO: Linkmanager
     * @return
     * 1: OK
     * -1: Already exist
     * -2: SQL Exception
     * -3: Wrong Parameters
     */
    public int addFood(int package_id, int food_id, int number){

        LinkManager link = new LinkManager(sql);

        if(food_id <0 || package_id <0 || number <0 )return -3;
        if(sql.rowExists(CURRENT_TABLE_LINK_FOOD, "package_id","food_id",package_id,food_id)) return -1;

        String prepString = "Insert into "+CURRENT_TABLE_LINK_FOOD+CURRENT_TABLE_ADD_FOOD_ARGUMENTS+" values(?,?,?)";
        try {
            sql.connection.setAutoCommit(false);

            PreparedStatement prep = sql.connection.prepareStatement(prepString);

            prep.setInt(1,package_id);
            prep.setInt(2,food_id);
            prep.setInt(3,number);

            prep.executeUpdate();

            sql.connection.commit();
            sql.connection.setAutoCommit(true);

            return 1;
        }
        catch (SQLException e){
            try{
                sql.connection.rollback();
            }
            catch (SQLException f){return -2;};
            return -2;
        }
    }

    public static void main(String[]args){

        SQL sql = new SQL();
        PackageManager manager = new PackageManager(sql);

        //manager.generate("Soup-Package",1000);
        manager.addFood(2,218,5);
        manager.addFood(2,232,3);
        manager.addFood(2,246,5);
        manager.addFood(2,258,5);
        manager.addFood(2,262,5);
        manager.addFood(2,275,5);
        manager.addFood(2,283,5);






    }
}
