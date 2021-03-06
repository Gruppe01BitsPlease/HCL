package backend;

import java.io.FileNotFoundException;
import java.sql.*;

/**
 * Is responsible for managing the database connection, and common utilities used for the database
 */
public class SQL {

	private final static String databasedriver = "com.mysql.jdbc.Driver";

	private SettingsFile settings;
	private String username;
	private String password;
	private String host;
	private String database;
	public  Connection connection;
	private static int colomns = 0;

	public SQL() {
		try {this.settings = new SettingsFile();}
		catch (FileNotFoundException e){ System.out.println("Database settings could not be found" + e);}
		this.host = settings.getPropValue("host");
		this.database = settings.getPropValue("database");
		this.username = settings.getPropValue("user");
		this.password = settings.getPropValue("password");
		this.database = host + database + "?user=" + username + "&password=" + password;
        this.connection = connect();

	}

	/**For testing SQLconnection based on input from end user
	 * @param host url for host
	 */
	public SQL(String host, String database, String username, String password) {
		try {this.settings = new SettingsFile();}
		catch (FileNotFoundException e){ System.out.println("Database settings could not be found" + e);}
		this.host = host;
		this.database = database;
		this.username = username;
		this.password = password;
		this.database = host + database + "?user=" + username + "&password=" + password;
		this.connection = connect();
		}

	/**
	 * @return True if connected to the internet and the database
     */
	public boolean isConnected(){
		try {
            if(connection == null) connection = connect();

			return connection != null && connection.isValid(3);
		}
		catch (SQLException e){
		 	return false;
		}
	}


	/**
	 * True if it managed to connect to specified database, false otherwise
	 */
	private Connection connect() {

		try {
			Class.forName(databasedriver);
		}
		catch (ClassNotFoundException e) {

			return null;
		}
		try {
			connection = DriverManager.getConnection(database);
			return connection;
		}

		catch (SQLException e) { return null;}

	}


	/**
	 * Ends all connections with the database. Closes Connection and ResultSet
	 */
	public void end() {

		if(connection != null){
			try { connection.close();} catch (SQLException ignored){}
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
		try { //Can't use try-with, because you cant do stuff with a closed ResultSet object
            Statement setning = connection.createStatement();
			return setning.executeQuery(query);

		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Update   s a value in a table, PRIMARYKEYVALUE CAN ALSO BE INT
     * @param primaryKey The primary key of the spesified table, can also be any other colomn in the table, but then you could get duplicates.
     *                        primaryKeyValue: The value of the primary key.
     * @return true if it managed to update the specified value, false otherwise
	 */
	public boolean update(String table, String colomnName, String primaryKey, String primaryKeyValue, String newValue) {
        //UPDATE HCL_user SET user_name =  'ost' WHERE user_name LIKE  'Mat'
        if(table.split(" \"\'").length > 1 || colomnName.split(" \"\'").length > 1 || primaryKey.split(" \"\'").length > 1 ||
                !rowExists(table,primaryKey,primaryKeyValue)){
            return false;
        }
        String pString = "UPDATE "+table+" SET "+colomnName+" = ? WHERE "+primaryKey+" = ?";

        try {
            PreparedStatement prep = connection.prepareStatement(pString);

            prep.setString(1, newValue);
            prep.setString(2, primaryKeyValue);

            System.out.println(prep.toString());

            prep.executeUpdate();
            return true;
        }
        catch(SQLException e){
			e.printStackTrace();
			return false;
        }
	}
	/**
	 * Updates a value in a table, PRIMARYKEYVALUE CAN ALSO BE INT
	 * @param primaryKey The primary key of the spesified table, can also be any other colomn in the table, but then you could get duplicates.
	 *                        primaryKeyValue: The value of the primary key
	 */
    public boolean update(String table, String colomnName, String primaryKey, String primaryKeyValue, int newValue) {
        //UPDATE HCL_user SET user_name =  'ost' WHERE user_name LIKE/=  'Mat'

        if(table.split(" \"\'").length > 1 || colomnName.split(" \"\'").length > 1 || primaryKey.split(" \"\'").length > 1 ||
                !rowExists(table,primaryKey,primaryKeyValue)){
            return false;
        }

        String pString = "UPDATE "+table+" SET "+colomnName+" = ? WHERE "+primaryKey+" = ?";

        try {
            PreparedStatement prep = connection.prepareStatement(pString);

            prep.setInt(1, newValue);
            prep.setString(2, primaryKeyValue);

            System.out.println(prep.toString());

            prep.executeUpdate();
            return true;
        }
        catch(SQLException e){
			e.printStackTrace();
			return false;
        }
    }
	/**
	 * Updates a value in a table, PRIMARYKEYVALUE CAN ALSO BE INT
	 * @param primaryKey The primary key of the spesified table, can also be any other colomn in the table, but then you could get duplicates.
	 *                        primaryKeyValue: The value of the primary key
	 */
    public boolean update(String table, String colomnName, String primaryKey, String primaryKeyValue, Date newDate) {
        //UPDATE HCL_user SET user_name =  'ost' WHERE user_name LIKE  'Mat'
        if(table.split(" \"\'").length > 1 || colomnName.split(" \"\'").length > 1 || primaryKey.split(" \"\'").length > 1 ||
                !rowExists(table,primaryKey,primaryKeyValue)){
            return false;
        }
        String pString = "UPDATE "+table+" SET "+colomnName+" = ? WHERE "+primaryKey+" = ?";

        try {
            PreparedStatement prep = connection.prepareStatement(pString);

            prep.setDate(1, newDate);
            prep.setString(2, primaryKeyValue);

            System.out.println(prep.toString());

            prep.executeUpdate();
            return true;
        }
        catch(SQLException e){
			e.printStackTrace();
            return false;
        }
    }
	/**
	 * Updates a value in a table, PRIMARYKEYVALUE CAN ALSO BE INT
	 * @param primaryKey The primary key of the spesified table, can also be any other colomn in the table, but then you could get duplicates.
	 *                        primaryKeyValue: The value of the primary key
	 */
	public boolean update(String table, String colomnName, String primaryKey, String primaryKeyValue, boolean newBoolean) {
		//UPDATE HCL_user SET user_name =  'ost' WHERE user_name LIKE  'Mat'
		if(table.split(" \"\'").length > 1 || colomnName.split(" \"\'").length > 1 || primaryKey.split(" \"\'").length > 1 ||
				!rowExists(table,primaryKey,primaryKeyValue)){
			return false;
		}
		String pString = "UPDATE "+table+" SET "+colomnName+" = ? WHERE "+primaryKey+" = ?";

		try {
			PreparedStatement prep = connection.prepareStatement(pString);

			prep.setBoolean(1, newBoolean);
			prep.setString(2, primaryKeyValue);

			System.out.println(prep.toString());

			prep.executeUpdate();
			return true;
		}
		catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}

    /**
     *@return True if the searched value "primaryKeyValue" exists in the colomn "primaryKey" in the specified table
     */
    public boolean rowExists(String table, String primaryKey, String primaryKeyValue){
       /* if(table.split(" \"\':;").length > 1){ //Prevents sql-injection
           // System.out.println("Ostost");
            return false;
        }*/

        try {
            String sqlPrep = "Select * from " + table + " where "+primaryKey+" = ? AND active = 1";
            PreparedStatement prep = connection.prepareStatement(sqlPrep);

          //  prep.setString(1, primaryKey);
            prep.setString(1, primaryKeyValue);

            ResultSet res = prep.executeQuery();

            //System.out.println(prep.toString()+"\n"+res.toString());

			return res.next();
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
		}
    }

    /**
     *@return True if the searched value "primaryKeyValue" exists in the colomn "primaryKey" in the specified table
     */
    public boolean rowExists(String table, String primaryKey, int primaryKeyValue){
       /* if(table.split(" \"\':;").length > 1){ //Prevents sql-injection
            // System.out.println("Ostost");
            return false;
        }*/

        try {
            String sqlPrep = "Select * from " + table + " where "+primaryKey+" = ? AND active = 1";
            PreparedStatement prep = connection.prepareStatement(sqlPrep);

            //  prep.setString(1, primaryKey);
            prep.setInt(1, primaryKeyValue);

            ResultSet res = prep.executeQuery();

            //System.out.println(prep.toString()+"\n"+res.toString());

			return res.next();
        }
        catch (SQLException e){
			e.printStackTrace();
            return false;
		}
    }

    /**
     *@return True if the searched value "primaryKeyValue" exists in the colomn "primaryKey" in the specified table
     * Used for link-tables only
     */
	public boolean rowExists(String table, String PK1,String PK2, int v1, int v2){

		try {
			String sqlPrep = "Select * from " + table + " where "+PK1+" = ? AND "+PK2+" = ?;";

			PreparedStatement prep = connection.prepareStatement(sqlPrep);

			//  prep.setString(1, primaryKey);
			prep.setInt(1, v1);
			prep.setInt(2, v2);

			ResultSet res = prep.executeQuery();

			//System.out.println(prep.toString()+"\n"+res.toString());

			return res.next();

		}
		catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}


	/**
     * Note: Very prone to SQL injection (v.v)
	 * @return The query as a handy-dandy String[][], titles of columbs are in the
	 *         first row, data in the others
	 * @param
	 * query Literally the query that will be sent to the DB, use with caution.
	 * @param header if true [0] will be titles else [0] will be first line of data
	 */
	public String[][] getStringTable(String query, boolean header){
		if (query == null || query.trim().equals("")) {
			return null;
		}
		try {
			ResultSet res = query(query);
			ResultSetMetaData meta = res.getMetaData();
			colomns = meta.getColumnCount();

			String[][] out;

			if(header){
				out = arrayWithCorrectSize(query, true);
				for (int i = 0; i < colomns; i++) {
					out[0][i] = meta.getColumnName(i + 1); // Legger inn s�ylenavnene i �verste rad? hopefully
				}
				int i = 1;
				while (res.next() && i < out.length) {

					for (int j = 1; j <= colomns; j++) {
						out[i][j - 1] = res.getString(j);
					}
					i++;
				}
				return out;
			}else {
				out = arrayWithCorrectSize(query, false);
				int i = 0;
				while (res.next() && i < out.length) {

					for (int j = 1; j <= colomns; j++) {
						out[i][j - 1] = res.getString(j);
					}
					i++;
				}
				return out;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * @return ID last inserted into the datasbase, or -1 if no result
     */
	public int getLastID(){
		ResultSet res = query("SELECT LAST_INSERT_ID();");
		try {
			res.next();
			return res.getInt(1);
		}
		catch (SQLException e){return -1;}
	}


	/**
	 * Returns only the column names from the database
     */
	public String[] getColumnNames(String query) {
		//return getStringTable(query, true)[0];
		try {
			ResultSet res = query(query);
			ResultSetMetaData meta = res.getMetaData();
			int columns = meta.getColumnCount();
			String[] ret = new String[columns];
			for (int i = 0; i < ret.length; i++) {
				ret[i] = meta.getColumnName(i + 1);
			}
			return ret;
		}
		catch (SQLException e) {
			System.out.println(e.toString());
			return null;
		}
	}

    /**
     *
     * @param query
     * @param column
     * @return Only the spesified colomn from the query. Same as getStringTable[][colomn]
     */
	public String[] getColumn(String query, int column) {
		String[][] queryAsTable = getStringTable(query, false);
		String[] result = new String[queryAsTable.length];
		for (int i = 0; i < queryAsTable.length; i++) {
			result[i] = queryAsTable[i][column];
		}
		return result;
	}

    /**
     * @return The first row of values from the query, literally getStringTable()[0];
     */
	public String[] getRow(String query) {
		return getStringTable(query, false)[0];
	}
	/**
	 * Returns an array with the correct size for the specified query
	 * and null if something goes wrong
	 */
	private String[][] arrayWithCorrectSize(String query, boolean header) {

		try {
			ResultSet res = query(query);
			ResultSetMetaData meta = res.getMetaData();
			int rows = 0;
			colomns = meta.getColumnCount();
			String[][] out;

			while (res.next()) {
				rows++;
			}
			if(header){
				out = new String[rows + 1][colomns]; // +1 for overskrifter
			}else {
				out = new String[rows][colomns];
			}
			return out;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Prints a generic [][] array. Can take all types off arrays cuz fancy
	 */
	public <T> void print2dArray(T[][] array) {
		if (array != null && array.length != 0) {
			System.out.println();
			for (int i = 0; i < array[0].length; i++) {
				System.out.printf("%20s", array[0][i]);
			}
			System.out.println();
			System.out.println();
			for (int i = 1; i < array.length; i++) {

				for (int j = 0; j < array[i].length; j++) {

					System.out.printf("%20s", array[i][j]);

				}
				System.out.println();
			}
			System.out.println();
		}

	}

	/**
	 * @return
	 * 1: OK, -1: SQLException
     * ONLY USED FOR TESTING
	 */
	public int deleteForGood(String table, String PK1, int v1) {

		try {
			String sqlPrep = "DELETE FROM " + table + " where "+PK1+ " = ? ;";

			PreparedStatement prep = connection.prepareStatement(sqlPrep);

			//  prep.setString(1, primaryKey);
			prep.setInt(1, v1);

			prep.execute();

			return 1;

		}
		catch (SQLException e){
			e.printStackTrace();
			return -1;
		}

	}
    /**
     * @return
     * 1: OK, -1: SQLException
     * ONLY USED FOR TESTING
     */
	public int deleteForGood(String table, String PK1, String PK2, int v1, int v2) {

		try {
			String sqlPrep = "DELETE FROM " + table + " where "+PK1+ " = ? AND " +PK2+ " = ? ;";

			PreparedStatement prep = connection.prepareStatement(sqlPrep);

			prep.setInt(1, v1);
			prep.setInt(2, v2);

			prep.execute();

			return 1;

		}
		catch (SQLException e){
			e.printStackTrace();
			return -1;
		}

	}

	public static void main(String[] args) throws Exception {

        SettingsFile db = new SettingsFile();

		SQL sql = new SQL();
		//System.out.println(sql.connect());
		System.out.println("SQL is connected? : "+sql.isConnected());
		if (sql.isConnected()) {

			String[][] tabell = sql.getStringTable("Select * from HCL_user", true);
			//System.out.println("End: " + sql.end());
            sql.print2dArray(tabell);
		}
		else {
			System.out.println("Could not contact database @ " + db.getPropValue("database"));
        }
		System.out.println(sql.rowExists("HCL_food_ingredient","food_id","ingredient_id",207,31));
        //sql.update("HCL_user","user_tlf","user_name","Magisk",123456789);
       // System.out.println(sql.rowExists("HCL_user","user_name","Trine"));
        //System.out.println( sql.update("HCL_user","user_name","user_ID","9","Oste"));
		sql.end();
	}
}