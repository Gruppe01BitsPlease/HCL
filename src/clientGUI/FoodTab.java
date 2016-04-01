package clientGUI;

import backend.FoodManager;
import backend.SQL;

import javax.swing.*;
import java.awt.*;

class FoodTab extends GenericList {
	private static String query = "SELECT * FROM HCL_food";
	private static String[] titles = { "Food ID", "Name", "Price" };
	private static String[] dataTypes = { "int", "string", "int" };
	private static SQL sql;
	public FoodTab(SQL sql) {
		super(query, titles, "HCL_food", dataTypes, sql);
		add(new GenericSearch(query, titles), BorderLayout.SOUTH);
		this.sql = sql;
	}
	public int generate(String[] args) {
		FoodManager mng = new FoodManager(sql);
		int price = Integer.parseInt(args[2]);
		return mng.generate(args[1], price);
	}
}

