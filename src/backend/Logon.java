package backend;

import java.net.ConnectException;

/**
 * Used to log onto the SQL database using info from the Database.ini
 * Im sure posting my SQL username and password on github / giving it to all the customers is a GREAT idea.
 * EDIT: Fixed it so the file uses Base64 so atealst it's not plaintext
 */
public class Logon {

	private backend.File file;
	private backend.SQL sql;
	private String filename;

	public Logon(String filename) {
		this.filename = filename;
		file = new File(filename, true);
	}

	public Logon() {
		filename = System.getProperty("user.dir")+"/src/backend/Database.ini";
		file = new File(filename, true);
	}

	public String getDatabase() {
		String database = file.readLineAsBase64(0);
		return database;
	}

	public String getUser() {
		String user = file.readLineAsBase64(1);
		return user;
	}

	public String getPassword() {
		String pass = file.readLineAsBase64(2);
		return pass;
	}

	public void clearFile(String filename) {
		file.clearFile();
	}

    /**
     * Returns true if the stars align
     */
    public boolean logon() {

		String database = getDatabase();
		String user = getUser();
		String password = getPassword();

		//System.out.println(database + user + password);

		sql = new SQL(database, user, password);

		return sql.connect(); // True if the database, usename, password, and JDBC drivers are all correct, and the servers are online

	}

    /**
     * Clears the file and rewrites the data with the "Database" line changed.
     */
    public void changeDatabase(String newDatabase) {

		String database = getDatabase();
		String user = getUser();
		String pass = getPassword();

		clearFile(filename);

		file.writeLineAsBase64(newDatabase);
		file.writeLineAsBase64(user);
		file.writeLineAsBase64(pass);

	}

    /**
     * Clears the file and rewrites the data with the "User" line changed.
     */
	public void changeUser(String newUser) {
		String database = getDatabase();
		String user = getUser();
		String pass = getPassword();

		clearFile(filename);

		file.writeLineAsBase64(database);
		file.writeLineAsBase64(newUser);
		file.writeLineAsBase64(pass);
	}

    /**
     * Clears the file and rewrites the data with the "Password" line changed.
     */
    public void changePassword(String newPass) {

		String database = getDatabase();
		String user = getUser();
		String pass = getPassword();

		clearFile(filename);

		file.writeLineAsBase64(database);
		file.writeLineAsBase64(user);
		file.writeLineAsBase64(newPass);

	}

	public static void main(String[] args) {

		Logon logon = new Logon(System.getProperty("user.dir")+"/src/backend/Database.ini");
		System.out.println(logon.logon());

		System.out.println(
				"DB: " + logon.getDatabase() + " - User: " + logon.getUser() + " - Passord: " + logon.getPassword());

		//		logon.changeUser("olavhus");
		//		System.out.println(logon.getUser());
		//		System.out.println(logon.getDatabase());
		//		logon.changePassword("ost");
		//		System.out.println(logon.getPassword());
		//		logon.changeDatabase("Ost@ostost.com");
		System.out.println(
				"DB: " + logon.getDatabase() + " - User: " + logon.getUser() + " - Passord: " + logon.getPassword());
	}
}
/*
Create table HCL_users(
user_id integer auto_increment not null,
user_name varchar(50),
user_role integer not null,
user_salt varchar(50),
user_pass varchar(50),
primary key(user_id)
);
*/
