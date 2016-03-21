package clientGUI;

import backend.SQL;

import javax.swing.*;
import java.awt.*;

/**
 * Created by bahafeld on 17.03.2016.
 */
class OrderTab extends GenericList {
    private static SQL sql = new SQL();
    private static String query = "SELECT * FROM HCL_order ORDER BY delivery_date ASC";
    private static String[] titles = { "Order", "Customer", "Price", "ZIP-code", "Date Ordered", "Delivery Date", "*" };
    public OrderTab() {
        super(query, titles, "HCL_order");
        add(new GenericSearch(query, titles), BorderLayout.SOUTH);
    }
}

