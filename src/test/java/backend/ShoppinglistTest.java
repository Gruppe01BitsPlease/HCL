package backend;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by bahafeld on 21.04.2016...
 * For FoodPlease
 */
public class ShoppinglistTest {


    SQL sql = new SQL();
    DeliveryManager dManager;
    OrderManager oManager;
    CustomerManager cManager;
    Shoppinglist manager;
    IngredientManager iManager;
    FoodManager fManager;


    @Before
    public void setUp() throws Exception {
        manager = new Shoppinglist(sql);
        oManager = new OrderManager(sql);
        cManager = new CustomerManager(sql);
        iManager = new IngredientManager(sql);
        fManager = new FoodManager(sql);

    }

    @After
    public void tearDown() throws Exception {
        oManager = null;
        cManager = null;
        manager = null;
        iManager = null;
        fManager = null;
        sql.end();

    }

    @Test
    public void getShoppinglist() throws Exception {

    }

    @Test
    public void addShoppinglist() throws Exception {

        int forLiteID = iManager.generate("forLite", 1, 56, false, false, true, "kul mat", "2016-04-04", "2017-05-06");
        String forLite = "" + forLiteID;
        int forLite2ID = iManager.generate("forLiten", 1, 56, false, false, true, "kul mat", "2016-04-04", "2017-05-06");
        String forLiten = "" + forLite2ID;

        String[][] testArray = {forLite, forLiten}{}{}{}{}{}{}{}{}{}{]




        int forLiteID = iManager.generate("forLite", 1, 56, false, false, true, "kul mat", "2016-04-04", "2017-05-06");
        int custID = cManager.generate("Prøvekanin", "kanin@hotmail.com", 75584788);
        int foodID = fManager.generate("test", 123);
        fManager.addIngredient(foodID, forLiteID, 20);
        int ordreID = oManager.generate(custID, 123, "Trondheim", 7030, "2017-04-30");
        oManager.addFood(ordreID, foodID, 1);




        //skriver ut informasjon om dagens leveringer sortert etter postnummer

        String[][] liste = manager.getShoppinglist(1);
        manager.addShoppinglist(liste);
        for(int j = 0; j <liste.length; j++){
            for(int i = 0; i < liste[j].length; i++){
                System.out.println(liste[j][i]);
            }
        }
        //Skrev ut riktig, alle Robertos leveringer ble med.

        sql.deleteForGood("HCL_order_food", "food_id", "order_id", foodID, ordreID);
        sql.deleteForGood("HCL_food_ingredient", "food_id", "ingredient_id",foodID, forLiteID);
        sql.deleteForGood("HCL_order", "order_id", ordreID);
        sql.deleteForGood("HCL_food", "food_id", foodID);
        sql.deleteForGood("HCL_ingredient", "ingredient_id", forLiteID);
        sql.deleteForGood("HCL_customer", "customer_id", custID);

    }

    @Test
    public void add() throws Exception {

    }

}