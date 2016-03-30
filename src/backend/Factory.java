package backend;

import java.util.Date;

/**
 * Created by Faiter119 on 13.03.2016.
 * Consolidates all the other classes
 * LinkManager should be used where needed to connect the databases
 */
@Deprecated
public interface Factory {

    boolean editDatabase(String newDatabase,String newUsername, String newPassword);

    String[] getUsers();
    String[] getCustomers();
    String[] getFoods();
    String[] getOrders();
    String[] getPackages();
    String[] getIngredients();

    boolean User_createUser(String username, String password, int role);
    boolean User_editPassword(int user_id,String oldPass, String newPass);
    boolean User_editRole(int user_id,int newRole);
    boolean User_deleteUser(int user_id); //False if user does not exist or something
    boolean User_logonUser(String username, String password);

    boolean Customer_createCustomer(String name, String email, int tlf);
    boolean Customer_editName(int customer_id, String newName);
    boolean Customer_editEmail(int customer_id, String newEmail);
    boolean Customer_editTlf(int customer_id, int newTlf);
    boolean Customer_deleteCustomer(int customer_id);

    boolean Order_createOrder(int customer_id, int price, String adress, int zip, Date order_date, Date delivery_date);
    boolean Order_editPrice(int order_id, int newPrice);
    boolean Order_editAdress(int order_id, String newAdress);
    boolean Order_editZip(int order_id, int newZip);
    boolean Order_editDeliverDate(int order_id, Date newDeliveryDate);
    boolean Order_deleteOrder(int order_id);
    boolean Order_addFood(int order_id, int food_id); // Just links
    boolean Order_addPackage(int order_id, int package_id);

    boolean Subscription_createSubscription(int customer_id, int price, int food_id, int package_id);
    boolean Subscription_editPrice(int price);
    boolean Subscription_editFood(int food_id);
    boolean Subscription_editPackage(int package_id);
    boolean Subscription_addDate(Date newDate);
    boolean Subscription_deleteSubscription(int subscription_id);

    boolean Package_createPackage(String name, int price);
    boolean Package_editName(String newName);
    boolean Package_editPrice(int newPrice);
    boolean Package_deletePackage(int package_id);

    boolean Food_createFood(String name, int price);
    boolean Food_editPrice(int newPrice);
    boolean Food_deleteFood(int food_id);
    boolean Food_addIngredient(int ingredient_id);

    boolean Ingredient_createIngredient(String name,boolean nuts, boolean gluten, boolean lactose, String other);
    boolean Ingredient_editPurchase_Price(int newPrice);
    boolean Ingredient_editStock(int newStock);
    boolean Ingredient_editPurchasePrice(Date newDate);
    boolean Ingredient_editExpirationDate(Date newDate);
    boolean Ingredient_deleteIngredient(int ingredient_id);


    //String[][] getFinancialReport();
    //Methods for financials later
}
