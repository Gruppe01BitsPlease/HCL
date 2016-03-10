package backend;

public class Logon {

	backend.File file;
	backend.SQL sql;
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

		return sql.connect();
	}

	public void changeDatabase(String newDatabase) {

		String database = getDatabase();
		String user = getUser();
		String pass = getPassword();

		clearFile(filename);

		file.writeLine(newDatabase);
		file.writeLine(user);
		file.writeLine(pass);

	}

	public void changeUser(String newUser) {
		String database = getDatabase();
		String user = getUser();
		String pass = getPassword();

		clearFile(filename);

		file.writeLine(database);
		file.writeLine(newUser);
		file.writeLine(pass);
	}

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

		Logon logon = new Logon("Database.ini");
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
