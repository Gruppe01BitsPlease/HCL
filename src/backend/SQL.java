package backend;

import java.net.URISyntaxException;
import java.sql.*;

public class SQL {

	private String username;
	private String password;
	private String databasedriver = "com.mysql.jdbc.Driver";
	private String databasename;
	private String database;
	public  Connection connection;
	private Statement sentence;
	private ResultSet res;
    public boolean isConnected = false;

	public static int colomns = 0;

	public SQL(Logon logon) {
		this.databasename = logon.getDatabase();
		this.username = logon.getUser();
		this.password = logon.getPassword();
		this.database = databasename + username + "?user=" + username + "&password=" + password;
        this.connection = connect();
        if(connection != null){
            isConnected = true;
        }
	}

	public SQL() {
		Logon logon = new Logon(new File());
		this.databasename = logon.getDatabase();
		this.username = logon.getUser();
		this.password = logon.getPassword();
		this.database = databasename + username + "?user=" + username + "&password=" + password;
		this.connection = connect();
		if(connection != null){
			isConnected = true;
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
			//setning = forbindelse.createStatement();
			return connection;
		}
		catch (SQLException e) {
			return null;
		}
	}


	/**
	 * Attemts to end the link with the database
     * TODO: Make this not shit
	 */
	public boolean end() {

		try {
            res.close();
            connection.close();
			//setning.close();
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
     * TODO Make useful
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
	 * Updates a value in a table
     * @param primaryKey The primary key of the spesified table, can also be any other colomn in the table, but then you could get duplicates.
     *                        primaryKeyValue: The value of the primary key
	 */
	public boolean update(String table, String colomnName, String primaryKey, String primaryKeyValue, String newValue) {
        //UPDATE HCL_users SET user_name =  'ost' WHERE user_name LIKE  'Mat'
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
            return false;
        }
	}
    public boolean update(String table, String colomnName, String primaryKey, String primaryKeyValue, int newValue) {
        //UPDATE HCL_users SET user_name =  'ost' WHERE user_name LIKE/=  'Mat'

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
            return false;
        }
    }
    public boolean update(String table, String colomnName, String primaryKey, String primaryKeyValue, Date newDate) {
        //UPDATE HCL_users SET user_name =  'ost' WHERE user_name LIKE  'Mat'
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
            return false;
        }
    }

    /**
     *@return True if the searched value "primaryKeyValue" exists in the colomn "primaryKey" in the specified table
     *
     */
    public boolean rowExists(String table, String primaryKey, String primaryKeyValue){
        if(table.split(" \"\':;").length > 1){ //Prevents sql-injection
           // System.out.println("Ostost");
            return false;
        }

        try {
            String sqlPrep = "Select * from " + table + " where "+primaryKey+" = ?";
            PreparedStatement prep = connection.prepareStatement(sqlPrep);

          //  prep.setString(1, primaryKey);
            prep.setString(1, primaryKeyValue);

            ResultSet res = prep.executeQuery();

            //System.out.println(prep.toString()+"\n"+res.toString());

            if(res.next()){
               return true;
            }
        }
        catch (SQLException e){
            System.out.println(e.toString());
            return false;}

        return false;
    }

    public boolean rowExists(String table, String primaryKey, int primaryKeyValue){
        if(table.split(" \"\':;").length > 1){ //Prevents sql-injection
            // System.out.println("Ostost");
            return false;
        }

        try {
            String sqlPrep = "Select * from " + table + " where "+primaryKey+" = ?";
            PreparedStatement prep = connection.prepareStatement(sqlPrep);

            //  prep.setString(1, primaryKey);
            prep.setInt(1, primaryKeyValue);

            ResultSet res = prep.executeQuery();

            //System.out.println(prep.toString()+"\n"+res.toString());

            if(res.next()){
                return true;
            }
        }
        catch (SQLException e){
            System.out.println(e.toString());
            return false;}

        return false;
    }

	/**
     * Note: Very prone to SQL injection (v.v)
	 * @return The query as a handy-dandy String[][], titles of columbs are in the
	 *         first row, data in the others
     *
	 */
	public String[][] getStringTable(String query, boolean header){
		if (query == null || query.trim().equals("")) {
			return null;
		}
		try {
			ResultSet res = query(query);
			ResultSetMetaData meta = res.getMetaData();
			colomns = meta.getColumnCount();

			String[][] out = null;

			if(header){
				out = arrayWithCorrectSize(query, true);
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
            System.out.println("Error: "+e.toString());
			return null;
		}

	}

	//Returns only the column names from the database
	public String[] getColumnNames(String query) {
		return getStringTable(query, true)[0];
	}
	/**
	 * Returns an array with the correct size for the specified query Returns
	 * and null if something goes wrong
	 */
	public String[][] arrayWithCorrectSize(String query, boolean header) {

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

        Logon logon = null;
        try {
            logon = new Logon(new File(SQL.class.getResource("Database.ini").toURI().getPath(),true));
        }
        catch (URISyntaxException e){}

		SQL sql = new SQL(logon);
		//SQL sql = new SQL("jdbc:mysql://mysql.stud.iie.ntnu.no:3306/","olavhus","CmrXjoQn");
		//System.out.println(sql.connect());
		if (sql.isConnected) {

			String[][] tabell = sql.getStringTable("Select * from HCL_users", true);
			//System.out.println("End: " + sql.end());
            sql.print2dArray(tabell);
		}
		else {
			System.out.println("Could not contact database @ " + logon.getDatabase());
        }
        //sql.update("HCL_users","user_tlf","user_name","Magisk",123456789);
        System.out.println(sql.rowExists("HCL_users","user_name","Trine"));
        //System.out.println( sql.update("HCL_users","user_name","user_ID","9","Oste"));
	}
}