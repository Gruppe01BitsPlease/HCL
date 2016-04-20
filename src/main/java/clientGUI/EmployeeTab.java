package clientGUI;

import backend.SQL;
import backend.UserManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;


class EmployeeTab extends GenericList {
	private static String query = "SELECT user_id, user_name, user_firstname, user_lastname, user_email, user_tlf, " +
			"user_adress, user_postnr, user_start FROM HCL_user WHERE active = 1";
	private SQL sql;
	private int role;
	public EmployeeTab(SQL sql, int role) {
		super(query, "HCL_user", null, null, sql, role);
		this.sql = sql;
		this.role = role;
		add(new GenericSearch(), BorderLayout.SOUTH);
	}
	/*public int generate(String[] args) {
		UserManager mng = new UserManager(sql);
		int role = Integer.parseInt(args[3]);
		return mng.generate(args[1], args[2], role);
	}*/
	public void edit(int id, boolean newEntry) {

	}
}

