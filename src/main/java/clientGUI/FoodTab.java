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
	private static String[][] linkTables = {{ "Ingredients", "SELECT ingredient_id FROM HCL_food_ingredient WHERE food_id = " },
			{ "Orders", "SELECT order_id FROM HCL_order_food WHERE food_id = " },
			{ "Packages", "SELECT package_id FROM HCL_package_food WHERE food_id = " }};
	public FoodTab(SQL sql) {
		super(query, titles, "HCL_food", dataTypes, linkTables, sql);
		add(new GenericSearch(query, titles), BorderLayout.SOUTH);
		this.sql = sql;
	}
	public int generate(String[] args) {
		FoodManager mng = new FoodManager(sql);
		int price = Integer.parseInt(args[2]);
		return mng.generate(args[1], price);
	}
}

