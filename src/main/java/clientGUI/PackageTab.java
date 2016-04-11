package clientGUI;

import backend.PackageManager;
import backend.SQL;

import javax.swing.*;
import java.awt.*;

class PackageTab extends GenericList {
	private static String query = "SELECT * FROM HCL_package";
	private static String[] dataTypes = { "primary", "string", "int" };
	private SQL sql;
	public PackageTab(SQL sql) {
		super(query, "HCL_package", dataTypes, null, sql);
		add(new GenericSearch(), BorderLayout.SOUTH);
		this.sql = sql;
	}
	public int generate(String[] args) {
		PackageManager mng = new PackageManager(sql);
		int price = Integer.parseInt(args[2]);
		return mng.generate(args[1], price);
	}
}

