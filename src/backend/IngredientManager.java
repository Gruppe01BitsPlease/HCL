package backend;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Faiter119 on 15.03.2016.
 */
public class IngredientManager {

    private SQL sql;
    public static final String CURRENT_TABLE = "HCL_ingredient";
    public static final String CURRENT_TABLE_GENERATE_ARGUMENTS = "(name, stock, purchase_price, nuts, gluten, lactose, other, purchase_date, expiration_date)";
    public static final String CURRENT_TABLE_DELETE_ARGUMENTS = "(name)";

    public IngredientManager(SQL sql){
        this.sql = sql;
    }

    public boolean generate(String name, int stock, int purchase_price, boolean nuts, boolean gluten, boolean lactose, String other, String purchase_date, String expiration_date) {

        if(!(name.trim().length() > 0) || !(stock >= 0) ) return false;

        try {
            Date date1 = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(purchase_date).getTime());
            Date date2 = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(expiration_date).getTime());

            String sqlPrep = "INSERT INTO "+CURRENT_TABLE+CURRENT_TABLE_GENERATE_ARGUMENTS+" VALUES(?,?,?,?,?,?,?,?,?)";
            PreparedStatement prep = sql.connection.prepareStatement(sqlPrep);

            prep.setString(1,name);
            prep.setInt(2,stock);
            prep.setInt(3,purchase_price);
            prep.setBoolean(4,nuts);
            prep.setBoolean(5,gluten);
            prep.setBoolean(6,lactose);
            prep.setString(7,other);
            prep.setDate(8,date1);
            prep.setDate(9,date2);

            prep.executeUpdate();
            return true;
        }
        catch (SQLException e){return false;}
        catch (ParseException e){return false;}
    }

    public boolean edit(String name, int newStock,int newPurchase_price, String newOther){
        if(name.trim().equals("")) return false;

        if(newStock >= 0)
            sql.update(CURRENT_TABLE,"stock","name",name,newStock);
        if(newPurchase_price >= 0)
            sql.update(CURRENT_TABLE,"purchase_price","name",name,newPurchase_price);
        if(!newOther.trim().equals(""))
            sql.update(CURRENT_TABLE,"other","name",name,newOther);
        return true;
    }


    public boolean delete(String name) {
        try {
            String sqlPrep = "DELETE FROM "+CURRENT_TABLE+" WHERE "+CURRENT_TABLE_DELETE_ARGUMENTS+" = ?";
            PreparedStatement prep = sql.connection.prepareStatement(sqlPrep);
            prep.setString(1,name);
            int row = prep.executeUpdate();
            if(row == 0)
                return false;

            return true;
        }
        catch (SQLException e){return false;}
    }

    public static void main(String[]args){
        File file = null;

        try {
            file = new File(IngredientManager.class.getResource("Database.ini").toURI().getPath(), true);
        }
        catch (Exception e){}

        Logon logon = new Logon(file);
        SQL sql = new SQL(logon);
        IngredientManager ingredient = new IngredientManager(sql);

      //  System.out.println(ingredient.generate("Ost",100,10,false,false,true,"","2016-03-15","2016-03-16"));
        System.out.println(ingredient.delete("Ost"));
       // System.out.println(ingredient.edit("Cheese",10,200,""));
    }
}
