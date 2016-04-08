package clientGUI;

import backend.SQL;
import backend.UserManager;

import javax.swing.*;
import java.awt.*;


class EmployeeTab extends GenericList {
	private static String query = "SELECT user_id, user_name, user_firstname, user_lastname, user_email, user_tlf, user_adress, user_postnr FROM HCL_users";
	private static String[] dataTypes = { "int", "string", "string", "string", "string", "int", "string", "int" };
	private SQL sql;
	public EmployeeTab(SQL sql) {
		super(query, "HCL_users", dataTypes, null, sql);
		add(new GenericSearch(), BorderLayout.SOUTH);
	}
	/*public int generate(String[] args) {
		UserManager mng = new UserManager(sql);
		int role = Integer.parseInt(args[3]);
		return mng.generate(args[1], args[2], role);
	}*/
}

