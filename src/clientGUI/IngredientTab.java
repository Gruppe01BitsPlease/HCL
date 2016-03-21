package clientGUI;

import backend.SQL;

import javax.swing.*;
import java.awt.*;

class IngredientTab extends GenericList {
	private static String query = "SELECT * FROM HCL_ingredient";
	private static String[] titles = { "Ingredient ID", "Name", "In stock", "Purchase price", "Contains nuts", "Contains gluten", "Contains lactose",
	"Other info", "Purchase date", "Expiration date" };
	public IngredientTab(SQL sql) {
		super(query, titles, "HCL_ingredient", sql);
		add(new GenericSearch(query, titles), BorderLayout.SOUTH);
	}
}

