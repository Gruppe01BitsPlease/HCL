package backend;

/**
 * Used to log onto the SQL database using info from the Database.ini
 * Im sure posting my SQL username and password on github / giving it to all the customers is a GREAT idea.
 * TODO: Encrypt my password so it's not stolen :(
 */
public class Logon {

	private backend.File file;
	private backend.SQL sql;
	private String filename;

	public Logon(String filename) {
		this.filename = filename;
		file = new File(filename, true);
	}

	public String getDatabase() {
		String database = file.readLine(0);
		return database;
	}

	public String getUser() {
		String user = file.readLine(1);
		return user;
	}

	public String getPassword() {
		String pass = file.readLine(2);
		return pass;
	}

	public void clearFile(String filename) {
		file.clearFile();
	}

	public boolean logon() {

		String database = file.readLine(0);
		String user = file.readLine(1);
		String password = file.readLine(2);

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

		file.writeLine(newDatabase);
		file.writeLine(user);
		file.writeLine(pass);

	}

    /**
     * Clears the file and rewrites the data with the "User" line changed.
     */
	public void changeUser(String newUser) {
		String database = getDatabase();
		String user = getUser();
		String pass = getPassword();

		clearFile(filename);

		file.writeLine(database);
		file.writeLine(newUser);
		file.writeLine(pass);
	}

    /**
     * Clears the file and rewrites the data with the "Password" line changed.
     */
    public void changePassword(String newPass) {

		String database = getDatabase();
		String user = getUser();
		String pass = getPassword();

		clearFile(filename);

		file.writeLine(database);
		file.writeLine(user);
		file.writeLine(newPass);

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
