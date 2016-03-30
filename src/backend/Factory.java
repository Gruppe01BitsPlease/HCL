package backend;

/**
 * Created by Faiter119 on 13.03.2016.
 * Consolidates all the other classes
 * LinkManager should be used where needed to connect the databases
 */
public interface Factory {

    boolean editDatabase(String newDatabase,String newUsername, String newPassword);
    // String getDatabaseInfo();

    String[] getUsers();
    String[] getCustomers();
    String[] getFoods();
    String[] getOrders();
    String[] getPackages();
    String[] getIngredients();

    boolean createUser(String username, String password, int role);
    boolean editUser();
    boolean deleteUser(); //False if user does not exist or something
    boolean logonUser(String username, String password);

    boolean createCustomer();
    boolean editCustomer();
    boolean deleteCustomer();

    boolean createFood();
    boolean editFood();
    boolean deleteFood();

    boolean createOrder();
    boolean editOrder();
    boolean deleteOrder();

    boolean createIngredient();
    boolean editIngredient();
    boolean deleteIngredient();

    //String[][] getFinancialReport();

    //String toString();

}
