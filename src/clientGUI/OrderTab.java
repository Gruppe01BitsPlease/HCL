package clientGUI;

import backend.SQL;

import javax.swing.*;
import java.awt.*;


class OrderTab extends GenericList {
    private static String query = "SELECT * FROM HCL_order ORDER BY delivery_date ASC";
    private static String[] titles = { "Order", "Customer", "Price", "ZIP-code", "Date Ordered", "Delivery Date", "*" };
    private static String[] dataTypes = { "int", "string", "int", "int", "string", "string", "string" };
    public OrderTab(SQL sql) {
        super(query, titles, "HCL_order", dataTypes, sql);
        add(new GenericSearch(query, titles), BorderLayout.SOUTH);
    }
}

