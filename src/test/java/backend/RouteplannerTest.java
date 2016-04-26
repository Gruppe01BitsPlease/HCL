package backend;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by bahafeld on 21.04.2016...
 * For FoodPlease
 */
public class RouteplannerTest {

    SQL sql = new SQL();
    DeliveryManager dManager;
    OrderManager oManager;
    CustomerManager cManager;
    Routeplanner manager;



    @Before
    public void setUp() throws Exception {
        manager = new Routeplanner(sql);
        dManager = new DeliveryManager(sql);
        oManager = new OrderManager(sql);
        cManager = new CustomerManager(sql);

    }

    @After
    public void tearDown() throws Exception {
        dManager = null;
        oManager = null;
        cManager = null;
        manager = null;
        sql.end();

    }

    @Test
    public void getRoute() throws Exception {
        //NB - orderdate must be todays date if test shall function properly.

        //making testobjects
        int robertoID = cManager.generate("Roberto", "rRoberto@hotmail.com", 75584788);
        int bestilling1ID = oManager.generate(robertoID, 123, "Trondheim", 7003, "2016-02-02");
        int bestilling2ID = oManager.generate(robertoID, 123, "Trondheim", 7030, "2016-02-02");
        int bestilling3ID = oManager.generate(robertoID, 123, "Trondheim", 7010, "2016-02-02");
        int bestilling4ID = oManager.generate(robertoID, 123, "Trondheim", 7023, "2016-02-02");
        int levering1ID = dManager.addDate(bestilling1ID, "2016-04-25");
        int levering2ID = dManager.addDate(bestilling2ID, "2016-04-25");
        int levering3ID = dManager.addDate(bestilling3ID, "2016-04-25");
        int levering4ID = dManager.addDate(bestilling4ID, "2016-04-25   '");

        //prnts out information about the deliveriessorted on postalcodes
        String[][] rute = manager.getRoute();

        sql.print2dArray(rute);
        //printed correctly, all of Robertos deliveries were included..



        sql.deleteForGood("HCL_deliveries", "delivery_id", levering1ID);
        sql.deleteForGood("HCL_deliveries", "delivery_id", levering2ID);
        sql.deleteForGood("HCL_deliveries", "delivery_id", levering3ID);
        sql.deleteForGood("HCL_deliveries", "delivery_id", levering4ID);
        sql.deleteForGood("HCL_order", "order_id", bestilling1ID);
        sql.deleteForGood("HCL_order", "order_id", bestilling2ID);
        sql.deleteForGood("HCL_order", "order_id", bestilling3ID);
        sql.deleteForGood("HCL_order", "order_id", bestilling4ID);
        sql.deleteForGood("HCL_customer", "customer_id", robertoID);


    }

}