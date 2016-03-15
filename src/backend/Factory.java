package backend;

/**
 * Created by Faiter119 on 13.03.2016.
 * Consolidates all the other classes
 */
public interface Factory {

    boolean createUser(String username, String password, int role);
    boolean editUser();
    boolean deleteUser(); //False if user does not exist or something
    boolean logonUser(String username, String password);

    boolean changeDatabase(String newDatabase,String newUsername, String newPassword);
    String getDatabaseInfo();

    String[] getUsers();


    String[][] getFinancialReport();

    String toString();

}
