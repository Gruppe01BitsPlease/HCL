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
        String[] titles = { "Employee ID", "Username", "First Name", "Last Name", "E-mail", "Address", "ZIP-code" };
        String query = "SELECT user_id, user_name, user_firstname, user_lastname, user_email, user_adress, user_postnr FROM HCL_users";
        setLayout(new BorderLayout());
        add(new genericList(query, titles, 1), BorderLayout.CENTER);
    }
}

