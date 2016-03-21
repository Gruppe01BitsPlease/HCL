package clientGUI;

import backend.SQL;

import javax.swing.*;
import java.awt.*;

class PackageTab extends GenericList {
	private static String query = "SELECT * FROM HCL_package";
	private static String[] titles = { "Package ID", "Name", "Price" };
	public PackageTab(SQL sql) {
		super(query, titles, "HCL_package", sql);
		add(new GenericSearch(query, titles), BorderLayout.SOUTH);
	}
}

