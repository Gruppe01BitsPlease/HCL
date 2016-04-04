package clientGUI;

import backend.CustomerManager;
import backend.IngredientManager;
import backend.SQL;

import javax.swing.*;
import java.awt.*;

class IngredientTab extends GenericList {
	private static String query = "SELECT * FROM HCL_ingredient";
	private static String[] titles = { "Ingredient ID", "Name", "In stock", "Purchase price", "Contains nuts", "Contains gluten", "Contains lactose",
	"Other info", "Purchase date", "Expiration date" };
	private SQL sql;
	private static String[] dataTypes = { "int", "string", "int", "int", "boolean", "boolean", "boolean", "string", "date", "date" };
	public IngredientTab(SQL sql) {
		super(query, titles, "HCL_ingredient", dataTypes, sql);
		add(new GenericSearch(query, titles), BorderLayout.SOUTH);
		this.sql = sql;
	}
	public int generate(String[] args) {
		IngredientManager mng = new IngredientManager(sql);
		int stock = Integer.parseInt(args[2]);
		int pprice = Integer.parseInt(args[3]);
		boolean nuts = Boolean.getBoolean(args[4]);
		boolean gluten = Boolean.getBoolean(args[5]);
		boolean lactose = Boolean.getBoolean(args[6]);
		int res = mng.generate(args[1], stock, pprice, nuts, gluten, lactose, args[7], args[8], args[9]);
		return res;
	}
}
