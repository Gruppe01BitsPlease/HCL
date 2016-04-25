package clientGUI;

import backend.IngredientManager;
import backend.SQL;

import java.awt.*;
import java.util.Arrays;

/**
 * Creates the JPanel that is used as a tab in tabbedMenu
 */
class IngredientTab extends GenericList {
	private static String query = "SELECT * FROM HCL_ingredient WHERE active = 1";
	private SQL sql;
	//Tab name, foreign PK, link table name, other table name, foreign identifier
	private static String[][] linkTables = {{ "Foods", "food_id", "HCL_food_ingredient", "HCL_food", "name" }};
	public IngredientTab(SQL sql, int role) {
		super(query, "HCL_ingredient", linkTables, null, sql, role, 1);
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
		}
		catch (Exception e) {
			System.out.println("generate() error: " + e.getMessage());
			stock = 0;
			pprice = 0;
		}
		try {
			nuts = Boolean.valueOf(args[4]);
			gluten = Boolean.valueOf(args[5]);
			lactose = Boolean.valueOf(args[6]);
		}
		catch (Exception k) {
			System.out.println("generate() error: " + k.getMessage());
			nuts = false;
			gluten = false;
			lactose = false;
		}
		return mng.generate(args[1], stock, pprice, nuts, gluten, lactose, args[7], args[8], args[9]);
	}
	public int delete(int nr) {
		IngredientManager mng = new IngredientManager(sql);
		int ret = mng.delete(nr);
		System.out.println("Delete code" + ret);
		return ret;
	}
}

