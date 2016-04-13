package clientGUI;

import backend.OrderManager;
import backend.SQL;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


class OrderTab extends GenericList {
    private static String query = "SELECT * FROM HCL_order ORDER BY delivery_date DESC";
    private static String[][] foreignKeys = {{ "SELECT customer_id, name FROM HCL_customer NATURAL JOIN HCL_order", "1" }};
    //Tab name, foreign PK, link table name, other table name, foreign identifier
    private static String[][] linkTables = {{ "Foods", "food_id", "HCL_order_food", "HCL_food", "name" },
            { "Packages", "package_id", "HCL_order_package", "HCL_package", "name" }};
    private SQL sql;
    public OrderTab(SQL sql) {
        super(query, "HCL_order", linkTables, foreignKeys, sql);
        add(new GenericSearch(), BorderLayout.SOUTH);
        this.sql = sql;
    }
    public int generate(String[] args) {
        System.out.println(Arrays.toString(args));
        OrderManager mng = new OrderManager(sql);
        int customerid = Integer.parseInt(args[1]);
        int price = Integer.parseInt(args[2]);
        int postnr = Integer.parseInt(args[4]);
        return mng.generate(customerid, price, args[3], postnr, args[5], args[6]);
    }
}

