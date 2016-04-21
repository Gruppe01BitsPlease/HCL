package clientGUI;

import backend.CustomerManager;
import backend.FoodManager;
import backend.SQL;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * Creates the JPanel that is used as a tab in tabbedMenu
 */
class CustomerTab extends GenericList {
	private static String query = "SELECT * FROM HCL_customer WHERE active = 1";
	private SQL sql;
	public CustomerTab(SQL sql, int role) {
		super(query, "HCL_customer", null, null, sql, role);
		add(new GenericSearch(), BorderLayout.SOUTH);
		this.sql = sql;
	}
	public int generate(String[] args) {
		CustomerManager mng = new CustomerManager(sql);
		int tlf = 0;
		try {
			tlf = Integer.parseInt(args[3]);
		}
		catch (Exception e) {}
		return mng.generate(args[1], args[2], tlf);
	}
	public int delete(int nr) {
		CustomerManager mng = new CustomerManager(sql);
		int ret = mng.delete(nr);
		System.out.println("Delete code" + ret);
		return ret;
	}
}

