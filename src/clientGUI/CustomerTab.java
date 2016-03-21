package clientGUI;

import backend.SQL;

import javax.swing.*;
import java.awt.*;

class CustomerTab extends GenericList {
	private static String query = "SELECT * FROM HCL_customer";
	private static String[] titles = { "Customer ID", "Name", "E-mail", "Phone" };
	public CustomerTab(SQL sql) {
		super(query, titles, "HCL_customer", sql);
		add(new GenericSearch(query, titles), BorderLayout.SOUTH);
	}
}

