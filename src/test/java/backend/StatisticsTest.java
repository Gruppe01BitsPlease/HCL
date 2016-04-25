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
    SQL sql = new SQL();

    @Before
    public void setUp() throws Exception {
        manager = new Statistics();

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
        // Tester metoden og sjekker opp mot database - og får likt svar.
        int test = manager.getTotalSubscriptions();
        System.out.println(test);

    }

    @Test
    public void getTotalOrders() throws Exception {
        // Tester metoden og sjekker opp mot database - og får likt svar.
        int test2 = manager.getTotalOrders();
        System.out.println(test2);

    }

    @Test
    public void getOrdersPerDay() throws Exception {
        // Tester metoden og sjekker opp mot database - og får likt svar.

        double[] test3 = manager.getOrdersPerDay();
        for(int i = 0; i < test3.length; i++){
            System.out.println("day: " + (i + 1) + ", result: " + test3[i]);
        }
    }

    @Test
    public void getOrdersPerMonth() throws Exception {
        // Tester metoden og sjekker opp mot database - og får likt svar.
        double[] test4 = manager.getOrdersPerMonth();
        for(int i = 0; i < test4.length; i++){
            System.out.println("month: " + (i + 1) + ", result: " + test4[i]);
        }

    }

    @Test
    public void getOrdersAt() throws Exception {

        //Sjekker om metoden fanger opp gale parametere
        assertEquals(-1, manager.getOrdersAt(2016, 0));
        assertEquals(-1, manager.getOrdersAt(2016, 13));
        assertEquals(-1, manager.getOrdersAt(0, 05));
        // Tester metoden og sjekker opp mot database - og får likt svar.
        int test5 = manager.getOrdersAt(2016, 05);
        System.out.println(test5);

    }

    @Test
    public void getDeliveriesToday() throws Exception {
        // Tester metoden og sjekker opp mot database - og får likt svar.
        System.out.println(manager.getDeliveriesToday());

    }

    @Test
    public void getAvgOrdersPerMonthThisYear() throws Exception {
        // Tester metoden og sjekker opp mot database - og får likt svar.
        System.out.println(manager.getAvgOrdersPerMonthThisYear());

    }

    @Test
    public void getAllTimePopularIngredient() throws Exception {
        // Tester metoden og sjekker opp mot database - og får likt svar.
        System.out.println(manager.getAllTimePopularIngredient());

    }

    @Test
    public void getMonthlyPopularIngredient() throws Exception {
        // Tester metoden og sjekker opp mot database - og får likt svar.
        System.out.println(manager.getMonthlyPopularIngredient());

    }

    @Test
    public void getMonthlyPopularFood() throws Exception {
        // Tester metoden og sjekker opp mot database - og får likt svar.
        System.out.println(manager.getMonthlyPopularFood());

    }

    @Test
    public void getAllTimePopularFood() throws Exception {
        // Tester metoden og sjekker opp mot database - og får likt svar.
        System.out.println(manager.getAllTimePopularFood());

    }

    @Test
    public void getGrossIncome() throws Exception {
        // Tester metoden og sjekker opp mot database - og får likt svar.
        System.out.println(manager.getGrossIncome());

    }

    @Test
    public void getNumberOfCustomers() throws Exception {
        // Tester metoden og sjekker opp mot database - og får likt svar.
        System.out.println(manager.getNumberOfCustomers());

    }

    @Test
    public void getBiggestCustomer() throws Exception {
        // Tester metoden og sjekker opp mot database - og får likt svar.
        System.out.println(manager.getBiggestCustomer());

    }

    @Test
    public void getBiggestCustomerThisMonth() throws Exception {
        // Tester metoden og sjekker opp mot database - og får likt svar.
        System.out.println(manager.getBiggestCustomerThisMonth());


    }

}