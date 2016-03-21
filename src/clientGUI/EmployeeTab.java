package clientGUI;

import backend.SQL;

import javax.swing.*;
import java.awt.*;


class EmployeeTab extends GenericList {
	private static String query = "SELECT user_id, user_name, user_firstname, user_lastname, user_email, user_adress, user_postnr FROM HCL_users";
	private static String[] titles = { "Employee ID", "Username", "First Name", "Last Name", "E-mail", "Address", "ZIP-code" };
	public EmployeeTab(SQL sql) {
		super(query, titles, "HCL_users", sql);
		add(new GenericSearch(query, titles), BorderLayout.SOUTH);
	}
}

