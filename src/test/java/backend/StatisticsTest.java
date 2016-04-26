package backend;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by bahafeld on 21.04.2016...
 * For FoodPlease
 */
public class StatisticsTest {

    Statistics manager;
    SQL sql;

    @Before
    public void setUp() throws Exception {
        sql = new SQL();
        manager = new Statistics(sql);

    }

    @After
    public void tearDown() throws Exception {
        manager = null;
        sql.end();

    }

    @Test
    public void getIngredientsInAllOrders() throws Exception {
        //bruker metoden og sjekker om den gir ut alle ingredienser fra alle ordre
        // ved å skrive ut resultate og sjekke opp mot databasen - og får riktig utslag.
        ArrayList<LocalDate> test = manager.getDeliveryDates();
        for(int i = 0; i < test.size(); i++){
                System.out.println(test.get(i));
        }

    }

    @Test
    public void getTotalSubscriptions() throws Exception {
        // prints out and checks the results up againt the database - and get correct result.
        int test = manager.getTotalSubscriptions();
        System.out.println(test);

    }

    @Test
    public void getTotalOrders() throws Exception {
        // prints out and checks the results up againt the database - and get correct result.
        int test2 = manager.getTotalOrders();
        System.out.println(test2);

    }

    @Test
    public void getOrdersPerDay() throws Exception {
        // prints out and checks the results up againt the database - and get correct result.
        double[] test3 = manager.getOrdersPerDay();
        for(int i = 0; i < test3.length; i++){
            System.out.println("day: " + (i + 1) + ", result: " + test3[i]);
        }
    }

    @Test
    public void getOrdersPerMonth() throws Exception {
        // prints out and checks the results up againt the database - and get correct result.
        double[] test4 = manager.getOrdersPerMonth();
        for(int i = 0; i < test4.length; i++){
            System.out.println("month: " + (i + 1) + ", result: " + test4[i]);
        }

    }

    @Test
    public void getOrdersAt() throws Exception {

        //checking for wrong parameters.
        assertEquals(-1, manager.getOrdersAt(2016, 0));
        assertEquals(-1, manager.getOrdersAt(2016, 13));
        assertEquals(-1, manager.getOrdersAt(0, 05));
        // prints out and checks the results up againt the database - and get correct result.
        int test5 = manager.getOrdersAt(2016, 05);
        System.out.println(test5);

    }

    @Test
    public void getDeliveriesToday() throws Exception {
        // prints out and checks the results up againt the database - and get correct result.
        System.out.println(manager.getDeliveriesToday());

    }

    @Test
    public void getAvgOrdersPerMonthThisYear() throws Exception {
        // prints out and checks the results up againt the database - and get correct result.
        System.out.println(manager.getAvgOrdersPerMonthThisYear());

    }

    @Test
    public void getAllTimePopularIngredient() throws Exception {
        // prints out and checks the results up againt the database - and get correct result.
        System.out.println(manager.getAllTimePopularIngredient());

    }

    @Test
    public void getMonthlyPopularIngredient() throws Exception {
        // prints out and checks the results up againt the database - and get correct result.
        System.out.println(manager.getMonthlyPopularIngredient());

    }

    @Test
    public void getMonthlyPopularFood() throws Exception {
        // prints out and checks the results up againt the database - and get correct result.
        System.out.println(manager.getMonthlyPopularFood());

    }

    @Test
    public void getAllTimePopularFood() throws Exception {
        // prints out and checks the results up againt the database - and get correct result.
        System.out.println(manager.getAllTimePopularFood());

    }

    @Test
    public void getGrossIncome() throws Exception {
        // prints out and checks the results up againt the database - and get correct result.
        System.out.println(manager.getGrossIncome());

    }

    @Test
    public void getNumberOfCustomers() throws Exception {
        // prints out and checks the results up againt the database - and get correct result.
        System.out.println(manager.getNumberOfCustomers());

    }

    @Test
    public void getBiggestCustomer() throws Exception {
        // prints out and checks the results up againt the database - and get correct result.
        System.out.println(manager.getBiggestCustomer());

    }

    @Test
    public void getBiggestCustomerThisMonth() throws Exception {
        // prints out and checks the results up againt the database - and get correct result.
        System.out.println(manager.getBiggestCustomerThisMonth());


    }

}