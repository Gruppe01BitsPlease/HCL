package clientGUI;

import backend.OrderManager;
import backend.SQL;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


class OrderTab extends GenericList {
    private static String query = "SELECT * FROM HCL_order ORDER BY delivery_date ASC";
    private static String[] titles = { "Order", "Customer", "Price", "Adress", "ZIP-code", "Date Ordered", "Delivery Date" };
    private static String[] dataTypes = { "int", "SELECT customer_id FROM HCL_customer", "int", "int", "string", "date", "date" };
    private SQL sql;
    public OrderTab(SQL sql) {
        super(query, titles, "HCL_order", dataTypes, null, sql);
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

