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
	private static String[][] linkTables = {{ "Ingredients", "ingredient_ID", "HCL_food_ingredient" },
			{ "Orders", "order_id", "HCL_order_food" },
			{ "Packages", "package_id", "HCL_package_food" }};
	public FoodTab(SQL sql) {
		super(query, titles, "HCL_food", dataTypes, linkTables, sql);
		add(new GenericSearch(), BorderLayout.SOUTH);
		this.sql = sql;
	}
	public int generate(String[] args) {
		FoodManager mng = new FoodManager(sql);
		int price = Integer.parseInt(args[2]);
		return mng.generate(args[1], price);
	}
}

