package clientGUI;

import backend.SQL;

import javax.swing.*;
import java.awt.*;

/**
 * Created by bahafeld on 17.03.2016.
 */
class FoodTab extends GenericList {
	private static String query = "SELECT * FROM HCL_food";
	private static String[] titles = { "Food ID", "Name", "Price" };
	public FoodTab(SQL sql) {
		super(query, titles, "HCL_food", sql);
		add(new GenericSearch(query, titles), BorderLayout.SOUTH);
	}
}

