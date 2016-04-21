package clientGUI;

import backend.CustomerManager;
import backend.IngredientManager;
import backend.SQL;

import javax.swing.*;
import java.awt.*;

/**
 * Creates the JPanel that is used as a tab in tabbedMenu
 */
class IngredientTab extends GenericList {
	private static String query = "SELECT * FROM HCL_ingredient WHERE active = 1";
	private SQL sql;
	//Tab name, foreign PK, link table name, other table name, foreign identifier
	private static String[][] linkTables = {{ "Foods", "food_id", "HCL_food_ingredient", "HCL_food", "name" }};
	public IngredientTab(SQL sql, int role) {
		super(query, "HCL_ingredient", linkTables, null, sql, role);
		add(new GenericSearch(), BorderLayout.SOUTH);
		this.sql = sql;
	}
	public int generate(String[] args) {
		IngredientManager mng = new IngredientManager(sql);
		int stock = 0;
		int pprice = 0;
		boolean nuts = false;
		boolean gluten = false;
		boolean lactose = false;
		try {
			stock = Integer.parseInt(args[2]);
			pprice = Integer.parseInt(args[3]);
			nuts = Boolean.getBoolean(args[4]);
			gluten = Boolean.getBoolean(args[5]);
			lactose = Boolean.getBoolean(args[6]);
		}
		catch (Exception e) {
			System.out.println("generate() error: " + e.getMessage());
		}
		int res = mng.generate(args[1], stock, pprice, nuts, gluten, lactose, args[7], args[8], args[9]);
		return res;
	}
	public int delete(int nr) {
		IngredientManager mng = new IngredientManager(sql);
		int ret = mng.delete(nr);
		System.out.println("Delete code" + ret);
		return ret;
	}
}

