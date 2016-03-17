package clientGUI;

import backend.SQL;

import javax.swing.*;
import java.awt.*;

/**
 * Created by bahafeld on 17.03.2016.
 */
class OrderTab extends JPanel {

    public OrderTab(SQL sql) {
        String[][] table = sql.getStringTable("SELECT * FROM HCL_order ORDER BY delivery_date ASC", false);
        String[] titles = { "Order", "Customer", "Price", "ZIP-code", "Date Ordered", "Delivery Date" };
        setLayout(new BorderLayout());
        add(new genericList(table, titles, 1, true), BorderLayout.CENTER);
    }
}

