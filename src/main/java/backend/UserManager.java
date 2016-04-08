package backend;

import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.ECField;
import java.security.spec.InvalidKeySpecException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class UserManager{

    public final static String CURRENT_TABLE = "HCL_users";
    public final static String CURRENT_TABLE_GENERATE_ARGUMENTS = "(user_name, user_role, user_salt, user_pass)";
    public final static String CURRENT_TABLE_DELETE_ARGUMENTS = "user_name";
	private PBKDF2 crypt = new PBKDF2();
    private SQL sql;

    /**
	 * Extends SQL, because it's SQL with more features
	 */
	public UserManager(SQL sql) {
		this.sql = sql;
	}

	/**
	 * Makes and inserts an user into the database
     * Now uses prepared statements, hopefully safe
     * @return 1: OK
     * -1: Already exists
     * -2: SQL Exception
	 */
	public int generate(String username, String password, int role) {

        if(sql.rowExists(CURRENT_TABLE,"user_name",username)) return -1; // User already exists

		try {
            byte[] salt = crypt.generateSalt();
			byte[] pass = crypt.getEncryptedPassword(password, salt);

			String salt2 = Base64.encode(salt);
			String pass2 = Base64.encode(pass);

            String insertTableSQL =
                    "INSERT INTO "+CURRENT_TABLE+CURRENT_TABLE_GENERATE_ARGUMENTS+" values(?,?,'" + salt2 + "', '" + pass2 + "');";
            try {
                PreparedStatement prep = sql.connection.prepareStatement(insertTableSQL);
                prep.setString(1, username);
                prep.setInt(2, role);
                prep.executeUpdate();
                return 1;
            }
            catch(SQLException e){
                return -2;
            }
        }
		catch (NoSuchAlgorithmException e) {
			return -2;
		}
		catch (InvalidKeySpecException e) {
			return -2;
		}
	}

    /**
     * Edits any of the information in the Users-table
     * @return
     *  1: OK
     * -1: Already exists
     * -2: SQL Exception
     */
    public int edit(String username,int role, String firstname, String lastname, String email, int tlf, String adress, int postnr, String start){
        //UPDATE  `olavhus`.`HCL_users` SET  `user_firstname` =  'Olav' WHERE  `HCL_users`.`user_id` =1;

        if(!(sql.rowExists(CURRENT_TABLE,"user_name",username))) return -1; //if the user does not exist

        try {

            sql.connection.setAutoCommit(false);

            if (!firstname.trim().equals("")) sql.update("HCL_users", "user_firstname", "user_name", username, firstname);
            if (role >= -1) sql.update("HCL_users", "user_role", "user_name", username, role);
            if (!lastname.trim().equals("")) sql.update("HCL_users", "user_lastname", "user_name", username, lastname);
            if (!email.trim().equals("")) sql.update("HCL_users", "user_email", "user_name", username, email);
            if (tlf > 0) sql.update("HCL_users", "user_tlf", "user_name", username, tlf);
            if (!adress.trim().equals("")) sql.update("HCL_users", "user_adress", "user_name", username, adress);
            if (postnr > 0) sql.update("HCL_users", "user_postnr", "user_name", username, postnr);
            if (start != null) {
                try {
                    Date date = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(start).getTime());
                    sql.update("HCL_users", "user_start", "user_name", username, date);
                }
                catch (Exception e) {}
            }

            sql.connection.commit();
            return 1;
        }
        catch (SQLException e){return -2;}
        finally {
            try {
                sql.connection.setAutoCommit(true);
            }
            catch (SQLException f){}
        }
    }

    /**
     * @return 1: OK
     * -1: Already exists
     * -2: SQL Exception
     */
    public int delete(String username){

        if(!(sql.rowExists(CURRENT_TABLE,"user_name",username))) return -1; //if the user does not exist

        String insertTableSQL = "DELETE FROM "+CURRENT_TABLE+" WHERE "+CURRENT_TABLE_DELETE_ARGUMENTS+" = ?";

        try {
            PreparedStatement prep = sql.connection.prepareStatement(insertTableSQL);
            prep.setString(1, username);
            prep.execute();
            return 1;
        }
        catch (Exception e){return -2;}
    }

    /**
     * @return 1: OK
     * -1: Already exists
     * -2: Encryption Exception
     * -3: Wrong Old Password
     */
    public int changePassword(String username, String oldpass, String newpass){

        if(!(sql.rowExists(CURRENT_TABLE,"user_name",username))) return -1; //if the user does not exist

        if(logon(username,oldpass) >= 0){

            String newSalt2;
            String newPass2;
            try{
                byte[] newSalt = crypt.generateSalt();
                byte[] newPass = crypt.getEncryptedPassword(newpass,newSalt);
                newSalt2 = Base64.encode(newSalt);
                newPass2 = Base64.encode(newPass);
            }
            catch (Exception e){return -2;}

            sql.update("HCL_users","user_pass","user_name",username,newPass2);
            sql.update("HCL_users","user_salt","user_name",username,newSalt2);
            return 1;
        }
        return -3;
    }

	/**
	 * @return -2: Not Connected, -1: Denied, 0: CEO, 1: Salesperson, 2: Cheff,
	 *         3: Driver
	 */
	public int logon(String username, String password) {

		if (!sql.isConnected())
			return -2;

        String insertTableSQL = "Select user_salt, user_pass, user_role from HCL_users where user_name = ?;";

        String userSalt;
        String userPass;
        String userRole;
        try {
            PreparedStatement prep  = sql.connection.prepareStatement(insertTableSQL);
            prep.setString(1, username);
            ResultSet res  = prep.executeQuery();
            if(res.next()) { //Only checks the first line, so if there are several results it only checks the first one
                userSalt = res.getString(1);
                userPass = res.getString(2);
                userRole = res.getString(3);
            }
            else return -1;
        }
        catch(SQLException e){
            return -1;
        }

		//String[][] results = getStringTable(prep.toString().split(":")[1]); // But it defeats the purpose? nah
		byte[] salt = Base64.decode(userSalt);
		byte[] pass = Base64.decode(userPass);
        int role = Integer.parseInt(userRole);

		try {
			if (crypt.authenticate(password, pass, salt)) {
				return role;
			}
		}
		catch (Exception e) {
			return -1;
		}
		return -1;
	}

    /**
     * Abstractation of SQL.getStringTable,
     */
    public String[][] get(boolean titles){

        String[][] out = sql.getStringTable("Select * from "+CURRENT_TABLE,titles);

        return out;

    }


	public static void main(String[] args) {

        //Logon logon = new Logon(System.getProperty("user.dir")+"/src/backend/Database.ini");
		//User u = new User(logon.getDatabase(),logon.getUser(),logon.getPassword());
        //Properties properties = new Properties();
        //properties.
       /* ClassLoader classLoader = new ClassLoader();
        URL url = classLoader.getResource("./src/backend/Database.ini");
        InputStream is = User.class.getResourceAsStream("/Database.ini");

        System.out.println(url.getPath());*/

        //Logon logon = new Logon(User.class.getResource("Database.ini").getPath());
        SQL sql = new SQL();
        UserManager u = new UserManager(sql);

		//u.generateUser("olavhus", "olavhus", 3); //Username, psw, role, 0 CEO
        u.changePassword("olavhus","ostost","faiter119");

		System.out.println(u.logon("olavhus", "ostost"));
        System.out.println(u.logon("olavhus", "faiter119"));

     /*   //System.out.println(u.update("HCL_users","user_name","ost","Magisk"));
        System.out.println(u.changePassword("Magisk","olavhus","ost"));*/
      //  u.deleteUser("testteswt");
        //u.editUser("Magisk",2,"Olav","Husby","OlavH96@gmail.com",93240605,"Bøkveien 11A",7059,200,20,new Date(System.currentTimeMillis()));

      // System.out.println(u.generate("Jens","jens",0));

     /*   try {
                u.edit("Trine", 0, "Trine", "Olsen", "TrineLise@gmail.com", 65678, "Atmed Elgan", 20, 20, 20, "2014-02-02");
        }
        catch (Exception e){}*/
        //u.delete("Bjørn");
       // System.out.println(u.changePassword("Olav","ostost","ostost"));
    }
}
