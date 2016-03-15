package backend;

import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.ECField;
import java.security.spec.InvalidKeySpecException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class UserManager extends SQL {

	private PBKDF2 crypt = new PBKDF2();
    private Logon logon;

    /**
	 * Extends SQL, because it's SQL with more features
	 */
	public UserManager(Logon logon) {
		super(logon);
        this.logon = logon;
		connect();
	}

	/**
	 * Makes and inserts an user into the database
     * Now uses prepared statements, hopefully safe
	 */
	public boolean generateUser(String username, String password, int role) {

		if (!connect())
			return false;

		try {
            byte[] salt = crypt.generateSalt();
			byte[] pass = crypt.getEncryptedPassword(password, salt);

			String salt2 = Base64.encode(salt);
			String pass2 = Base64.encode(pass);


            String insertTableSQL =
                    "INSERT INTO HCL_users(user_name, user_role, user_salt, user_pass) values(?,?,'" + salt2 + "', '" + pass2 + "');";
            try {
                PreparedStatement prep = getConnection().prepareStatement(insertTableSQL);
                prep.setString(1, username);
                prep.setInt(2, role);
                prep.executeUpdate();
                return true;
            }
            catch(SQLException e){
                return false;
            }
        }
		catch (NoSuchAlgorithmException e) {
			return false;
		}
		catch (InvalidKeySpecException e) {
			return false;
		}
	}
    public boolean editUser(){
        return false;
    }
    public boolean deleteUser(){
        return false;
    }
    public boolean changePassword(String username, String oldpass, String newpass){

        if(logon(username,oldpass) >= 0){

            String insertTableSQL = "Select user_salt, user_pass  from HCL_users where user_name = ?;";

            String userSalt = "";
            String userPass = "";
            try {
                PreparedStatement prep  = getConnection().prepareStatement(insertTableSQL);
                prep.setString(1, username);
                ResultSet res  = prep.executeQuery();
                if(res.next()) {
                    userSalt = res.getString(1);
                    userPass = res.getString(2);
                }
            }
            catch(SQLException e){
                return false;
            }
            String newSalt2 = "";
            String newPass2 = "";
            try{
                byte[] newSalt = crypt.generateSalt();
                byte[] newPass = crypt.getEncryptedPassword(newpass,newSalt);
                newSalt2 = Base64.encode(newSalt);
                newPass2 = Base64.encode(newPass);
            }
            catch (Exception e){return false;}

            update("HCL_users","user_pass",userPass,newPass2);
            update("HCL_users","user_salt",userSalt,newSalt2);
            return true;
        }
        return false;
    }

	/**
	 * @return -2: Not Connected, -1: Denied, 0: CEO, 1: Salesperson, 2: Cheff,
	 *         3: Driver
	 */
	public int logon(String username, String password) {

		if (!connect())
			return -2;

        String insertTableSQL = "Select user_salt, user_pass, user_role from HCL_users where user_name = ?;";

        String userSalt;
        String userPass;
        String userRole;
        try {
            PreparedStatement prep  = getConnection().prepareStatement(insertTableSQL);
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
        Logon logon = null;
        try{
        logon = new Logon(new File(UserManager.class.getResource("Database.ini").toURI().getPath(),true));
        }
        catch (URISyntaxException e){}
        UserManager u = new UserManager(logon);

		//u.generateUser("olavhus", "olavhus", 3); //Username, psw, role, 0 CEO

		int rolle = u.logon("Magisk", "ost");
		System.out.println(rolle);
     /*   //System.out.println(u.update("HCL_users","user_name","ost","Magisk"));
        System.out.println(u.changePassword("Magisk","olavhus","ost"));*/

    }
}
