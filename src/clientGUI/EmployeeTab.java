package clientGUI;

import backend.SQL;

import javax.swing.*;
import java.awt.*;

/**
 * Created by bahafeld on 17.03.2016.
 */
class EmployeeTab extends JPanel {
    public EmployeeTab() {
        //String[][] table = sql.getStringTable("SELECT user_name, user_email, user_adress FROM HCL_users", false);
        String[] titles = { "Employees", "E-mail", "Address" };
        String query = "SELECT user_name, user_email, user_adress FROM HCL_users";
        setLayout(new BorderLayout());
        add(new genericList(query, titles, 1), BorderLayout.CENTER);
    }
}

