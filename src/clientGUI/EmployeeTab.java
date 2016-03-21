package clientGUI;

import backend.SQL;

import javax.swing.*;
import java.awt.*;

/**
 * Created by bahafeld on 17.03.2016.
 */
class EmployeeTab extends GenericList {
	private static SQL sql = new SQL();
	private static String query = "SELECT user_id, user_name, user_firstname, user_lastname, user_email, user_adress, user_postnr FROM HCL_users";
	private static String[] titles = { "Employee ID", "Username", "First Name", "Last Name", "E-mail", "Address", "ZIP-code" };
	public EmployeeTab() {
		super(query, titles);
		add(new GenericSearch(query, titles), BorderLayout.SOUTH);
	}
}

