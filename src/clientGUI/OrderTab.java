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
    private static String[] titles = { "Order", "Customer", "Price", "ZIP-code", "Date Ordered", "Delivery Date" };
    private static String[][] table = sql.getStringTable(query, false);
    public OrderTab() {
        super(table, titles);
        add(new GenericSearch(query, titles), BorderLayout.SOUTH);
    }
}

