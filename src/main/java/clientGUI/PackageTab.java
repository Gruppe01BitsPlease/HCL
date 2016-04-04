package clientGUI;

import backend.PackageManager;
import backend.SQL;

import javax.swing.*;
import java.awt.*;

class PackageTab extends GenericList {
	private static String query = "SELECT * FROM HCL_package";
	private static String[] titles = { "Package ID", "Name", "Price" };
	private static String[] dataTypes = { "int", "string", "int" };
	private SQL sql;
	public PackageTab(SQL sql) {
		super(query, titles, "HCL_package", dataTypes, sql);
		add(new GenericSearch(query, titles), BorderLayout.SOUTH);
		this.sql = sql;
	}
	public int generate(String[] args) {
		PackageManager mng = new PackageManager(sql);
		int price = Integer.parseInt(args[2]);
		return mng.generate(args[1], price);
	}
}

