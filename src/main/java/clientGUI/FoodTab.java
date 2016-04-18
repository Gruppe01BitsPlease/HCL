package clientGUI;

import backend.FoodManager;
import backend.SQL;

import javax.swing.*;
import java.awt.*;

class FoodTab extends GenericList {
	private static String query = "SELECT * FROM HCL_food WHERE active = 1";
	private static SQL sql;
	//Tab name, foreign PK, link table name, other table name, foreign identifier
	private static String[][] linkTables = {{ "Ingredients", "ingredient_id", "HCL_food_ingredient", "HCL_ingredient", "name" },
			{ "Orders", "order_id", "HCL_order_food", "HCL_order", "adress" },
			{ "Packages", "package_id", "HCL_package_food", "HCL_package", "name" }};
	public FoodTab(SQL sql) {
		super(query, "HCL_food", linkTables, null, sql);
		add(new GenericSearch(), BorderLayout.SOUTH);
		FoodTab.sql = sql;
	}
	public int generate(String[] args) {
		FoodManager mng = new FoodManager(sql);
		int price = Integer.parseInt(args[2]);
		return mng.generate(args[1], price);
	}
	public int delete(int nr) {
		FoodManager mng = new FoodManager(sql);
		int ret = mng.delete(nr);
		System.out.println("Delete code" + ret);
		return ret;
	}
}

