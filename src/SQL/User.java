package SQL;

import java.io.EOFException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

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
	 * TODO Make it so only the CEO can do this
	 */
	public boolean generateUser(String username, String password, int role) {
		if (!connected)
			return false;

		PBKDF2 crypt = new PBKDF2();

		try {
			byte[] salt = crypt.generateSalt();
			byte[] pass = crypt.getEncryptedPassword(password, salt);

			String salt2 = Base64.encode(salt);
			String pass2 = Base64.encode(pass);

			insert("INSERT INTO HCL_users(user_name, user_role, user_salt, user_pass) values('" + username + "'," + role
					+ ",'" + salt2 + "','" + pass2 + "');");
			return true;
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

		String[][] results = getStringTable(
				"Select user_salt, user_pass, user_role from HCL_users where user_name = '" + username + "'");

		byte[] salt = Base64.decode(results[1][0]); //0 row is titles
		byte[] pass = Base64.decode(results[1][1]);
		int rolle = Integer.parseInt(results[1][2]);

		try {
			if (crypt.authenticate(password, pass, salt)) {
				return rolle;
			}
		}
		catch (Exception e) {
			return -1;
		}
		return -1;
	}

	public static void main(String[] args) {

		User u = new User("jdbc:mysql://mysql.stud.iie.ntnu.no:3306/", "olavhus", "CmrXjoQn");

		//u.generateUser("bj�rn", "m�dahamat", 1); //Username, psw, role, 0 CEO

		int rolle = u.logon("bj�rn", "m�dahamat");
		System.out.println(rolle);
	}
}
