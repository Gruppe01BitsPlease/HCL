package clientGUI;

import backend.SQL;

import javax.swing.*;
import java.awt.*;

/**
 * Created by bahafeld on 17.03.2016.
 */
class OrderTab extends JPanel {

    public OrderTab() {
        String query = "SELECT * FROM HCL_order ORDER BY delivery_date ASC";
        String[] titles = { "Order", "Customer", "Price", "ZIP-code", "Date Ordered", "Delivery Date" };
        setLayout(new BorderLayout());
        add(new genericList(query, titles, 1), BorderLayout.CENTER);
    }
}

