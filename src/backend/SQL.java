package backend;

import java.sql.*;

public class SQL {

	private String username;// = "olavhus";
	private String password;// = "CmrXjoQn";
	private String databasedriver = "com.mysql.jdbc.Driver";
	private String databasename;//"jdbc:mysql://mysql.stud.iie.ntnu.no:3306/olavhus?user=olavhus&password=CmrXjoQn;
	private String database;
	private Connection connection;
	private Statement sentence;
	private ResultSet res;

	public static int colomns = 0;

	public SQL(String databasename, String username, String password) {
		this.databasename = databasename;
		this.username = username;
		this.password = password;
		this.database = databasename + username + "?user=" + username + "&password=" + password;
	}

	/**
	 * True if it managed to connect to specified database, false otherwise
	 */
	public boolean connect() {

		try {
			Class.forName(databasedriver);
		}
		catch (ClassNotFoundException e) {
			return false;
		}

		try {
			connection = DriverManager.getConnection(database);
			//setning = forbindelse.createStatement();
			return true;
		}
		catch (SQLException e) {
			return false;
		}
	}
    public Connection getConnection(){
        return connection;
    }

	/**
	 * Attemts to end the link with the database
     * TODO: Make this not shit
	 */
	public boolean end() {

		try {
			connection.close();
			//setning.close();
			res.close();
			return true;
		}
		catch (SQLException e) {
			return false;
		}
	}

	/**
	 * @return A ResultSet object
     * Cant close the ResultSet object here; if you do, you can't do anything with the object because reasons
	 */
	public ResultSet query(String query) {
		if (query == null || query.trim().equals("")) {
			return null;
		}
        ResultSet out;
		try { //Can't use try-with, because you cant do stuff with a closed ResultSet object
            Statement setning = connection.createStatement();
            ResultSet res = setning.executeQuery(query);
            return res;


		}
		catch (SQLException e) {
            System.out.println("Error: "+e.toString());
			return null;
		}
    }

	/**
	 * Inserts something into the database through the specified query sentence
	 * 
	 * @return True if it worked, false otherwise
     * TODO Use try-with
	 */
	public boolean insert(String query) {
		if (query == null || query.trim().equals("")) {
			return false;
		}
		else {
			try {
				sentence = connection.createStatement();
				sentence.execute(query);
				return true;
			}
			catch (SQLException e) {
				return false;
			}
			finally {
				try {
					sentence.close();
				}
				catch (SQLException e) {
				}
			}
		}
	}

	/**
	 * Updates a value in a table, when I've coded it V('.')V
     * TODO: Make this
	 */
	public boolean update(String update) {

		//		try{
		//			forbindelse.setAutoCommit(false);
		//		}
		//		catch (SQLException e) {}

		try {
			sentence = connection.createStatement();

			if (sentence.executeUpdate(update) != 0) {
				return true;
			}
			else {
				return false;
			}
		}
		catch (SQLException e) {
			return false;
		}

	}

	/**
	 * @return The query as a handy-dandy String[][], titles of columbs are in the
	 *         first row, data in the others
	 */
	public String[][] getStringTable(String query) {
		if (query == null || query.trim().equals("")) {
			return null;
		}
		try {
			ResultSet res = query(query);
			ResultSetMetaData meta = res.getMetaData();
			colomns = meta.getColumnCount();

			String[][] out = arrayWithCorrectSize(query);

            for (int i = 1; i <= colomns; i++) {
				out[0][i - 1] = meta.getColumnName(i); // Legger inn s�ylenavnene i �verste rad? hopefully
			}
			int i = 1;
			while (res.next() && i < out.length) {

				for (int j = 1; j <= colomns; j++) {
					out[i][j - 1] = res.getString(j);
				}
				i++;
			}
			return out;
		}
		catch (SQLException e) {
            System.out.println("Error: "+e.toString());
			return null;
		}

	}

	/**
	 * Returns an array with the correct size for the specified query Returns
	 * and null if something goes wrong
	 */
	public String[][] arrayWithCorrectSize(String query) {

		try {
			ResultSet res = query(query);
			ResultSetMetaData meta = res.getMetaData();
			int rows = 0;
			colomns = meta.getColumnCount();
			String[][] out;

			while (res.next()) {
				rows++;
			}
			out = new String[rows + 1][colomns]; // +1 for overskrifter
			return out;
		}
		catch (SQLException e) {
			return null;
		}
	}

	/**
	 * Prints a generic [][] array. Neat Can take all types off arrays cuz fancy
	 */
	public <T> void print2dArray(T[][] array) {
		if (array != null) {
			System.out.println();
			for (int i = 0; i < colomns; i++) {
				System.out.printf("%20s", array[0][i]);
			}
			System.out.println();
			System.out.println();
			for (int i = 1; i < array.length; i++) {

				for (int j = 0; j < colomns; j++) {

					System.out.printf("%20s", array[i][j]);

				}
				System.out.println();
			}
			System.out.println();
		}
	}

	public static void main(String[] args) throws Exception {

		Logon logon = new Logon(System.getProperty("user.dir")+"/src/backend/Database.ini");
		SQL sql = new SQL(logon.getDatabase(), logon.getUser(), logon.getPassword());
		//SQL sql = new SQL("jdbc:mysql://mysql.stud.iie.ntnu.no:3306/","olavhus","CmrXjoQn");
		//System.out.println(sql.connect());
		if (sql.connect()) {

			String[][] tabell = sql.getStringTable("select * from bok");
			//System.out.println("End: " + sql.end());
            sql.print2dArray(tabell);
		}
		else {
			System.out.println("Could not contact database @ " + logon.getDatabase());
        }
	}
}