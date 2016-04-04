package clientGUI;

import backend.CustomerManager;
import backend.FoodManager;
import backend.SQL;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

class CustomerTab extends GenericList {
	private static String query = "SELECT * FROM HCL_customer";
	private static String[] titles = { "Customer ID", "Name", "E-mail", "Phone" };
	private static String[] dataTypes = { "int", "string", "string", "int" };
	private SQL sql;
	public CustomerTab(SQL sql) {
		super(query, titles, "HCL_customer", dataTypes, sql);
		add(new GenericSearch(query, titles), BorderLayout.SOUTH);
		this.sql = sql;
	}
	public int generate(String[] args) {
		CustomerManager mng = new CustomerManager(sql);
		int tlf = Integer.parseInt(args[3]);
		return mng.generate(args[1], args[2], tlf);
	}
}

