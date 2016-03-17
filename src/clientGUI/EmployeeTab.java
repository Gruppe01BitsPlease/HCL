package clientGUI;

import backend.SQL;

import javax.swing.*;
import java.awt.*;

/**
 * Created by bahafeld on 17.03.2016.
 */
class EmployeeTab extends JPanel {

    public EmployeeTab(SQL sql) {
        String[][] table = sql.getStringTable("SELECT user_name, user_email, user_adress FROM HCL_users", false);
        String[] titles = { "Employees", "E-mail", "Address" };
        setLayout(new BorderLayout());
        add(new genericList(table, titles, 1, true), BorderLayout.CENTER);
    }
}

