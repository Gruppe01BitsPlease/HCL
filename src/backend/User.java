package backend;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.PreparedStatement;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException;

public class User extends SQL {

	private PBKDF2 crypt = new PBKDF2();
	private boolean connected;


	/**
	 * Extends SQL, because it's SQL with more features
	 */
	public User(String databasenavn, String brukernavn, String passord) {
		super(databasenavn, brukernavn, passord);
		connected = connect();
	}

	/**
	 * Makes and inserts an user into the database, probably prone to exploits..
	 */
	public boolean generateUser(String username, String password, int role) {

        Logon logon = new Logon(System.getProperty("user.dir")+"/src/backend/Database.ini");

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

	/**
	 * @return -2: Not Connected, -1: Denied, 0: CEO, 1: Salesperson, 2: Cheff,
	 *         3: Driver
	 */
	public int logon(String username, String password) {

		if (!connected)
			return -2;

        String insertTableSQL = "Select user_salt, user_pass, user_role from HCL_users where user_name = ?;";

        PreparedStatement prep;
        ResultSet res;
        String userSalt;
        String userPass;
        String userRole;
        try {
            prep = getConnection().prepareStatement(insertTableSQL);
            prep.setString(1, username);
            res  = prep.executeQuery();
            if(res.next()) {
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

        Logon logon = new Logon(System.getProperty("user.dir")+"/src/backend/Database.ini");

		User u = new User(logon.getDatabase(),logon.getUser(),logon.getPassword());

		//u.generateUser("preparedTest", "test", 1); //Username, psw, role, 0 CEO

		int rolle = u.logon("olavhus", "ostost");
		System.out.println(rolle);
	}
}
