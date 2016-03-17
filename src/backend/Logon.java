package backend;

import java.net.ConnectException;
import java.net.URISyntaxException;

/**
 * Used to log onto the SQL database using info from the Database.ini
 * Im sure posting my SQL username and password on github / giving it to all the customers is a GREAT idea.
 * EDIT: Fixed it so the file uses Reverse-Base64 so at least it's not plaintext
 */
public class Logon {

	private backend.File file;
	private backend.SQL sql;
    private String filename;

	public Logon(File file) {

		this.file  = file;
        filename = file.getFilename();
	}

	//Should read from the file
	public Logon() {
        try {
            file = new File(Logon.class.getResource("Database.ini").toURI().getPath(), true);
        }
        catch (URISyntaxException e){}
        filename = file.getFilename();
	}

	public String getDatabase() {
		return  file.readLineAsBase64(0);
	}

	public String getUser() {
		return file.readLineAsBase64(1);
	}

	public String getPassword() {
		return file.readLineAsBase64(2);
	}


    /**
     * Returns true if the stars align
     */
    public boolean logon() {

		sql = new SQL(this);

		return sql.isConnected; // True if the database, usename, password, and JDBC drivers are all correct, and the servers are online

	}

    /**
     * Clears the file and rewrites the data with the "Database" line changed.
     */
    public void changeDatabase(String newDatabase) {

		String database = getDatabase();
		String user = getUser();
		String pass = getPassword();

		file.clearFile();

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

		file.clearFile();

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

		file.clearFile();

		file.writeLineAsBase64(database);
		file.writeLineAsBase64(user);
		file.writeLineAsBase64(newPass);

	}

	public static void main(String[] args) {
        Logon logon = null;
        try {
            logon = new Logon(new File(Logon.class.getResource("Database.ini").toURI().getPath(), true));
        }
        catch (URISyntaxException e){}
		System.out.println(logon.logon());

		System.out.println(
				"DB: " + logon.getDatabase() + " - User: " + logon.getUser() + " - Passord: " + logon.getPassword());

        logon.changeUser("olavhus");
        logon.changePassword("ost");
        logon.changeDatabase("Ost@ostost.com");

		System.out.println(
				"DB: " + logon.getDatabase() + " - User: " + logon.getUser() + " - Passord: " + logon.getPassword());

        logon.changeUser("olavhus");
        logon.changePassword("CmrXjoQn");
        logon.changeDatabase("jdbc:mysql://mysql.stud.iie.ntnu.no:3306/");

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
