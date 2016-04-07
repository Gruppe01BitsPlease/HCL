package clientGUI;

import backend.FoodManager;
import backend.SQL;

import javax.swing.*;
import java.awt.*;

class FoodTab extends GenericList {
	private static String query = "SELECT * FROM HCL_food";
	private static String[] dataTypes = { "int", "string", "int" };
	private static SQL sql;
	private static String[][] linkTables = {{ "Ingredients", "ingredient_ID", "HCL_food_ingredient", "HCL_ingredient", "name" },
			{ "Orders", "order_id", "HCL_order_food", "HCL_order", "adress" },
			{ "Packages", "package_id", "HCL_package_food", "HCL_package", "name" }};
	public FoodTab(SQL sql) {
		super(query, "HCL_food", dataTypes, linkTables, sql);
		add(new GenericSearch(), BorderLayout.SOUTH);
		this.sql = sql;
	}
	public int generate(String[] args) {
		FoodManager mng = new FoodManager(sql);
		int price = Integer.parseInt(args[2]);
		return mng.generate(args[1], price);
	}
}

