package clientGUI;

import backend.SQL;

/**
 * Created by Jens on 14-Apr-16.
 */
public class SubscriptionTab extends GenericList {
	private static String query = "SELECT * FROM HCL_subscription NATURAL JOIN HCL_order";
	private static String sqlTableName = "HCL_subscription";
	//Tab name, foreign PK, link table name, other table name, foreign identifier
	//{ "Foods", "food_id", "HCL_order_food", "HCL_food", "name" }
	private static String[][] linkTables = {{ "Dates", "date_id", "HCL_subscription_date", "HCL_subscription_date", "dato"}};
	public SubscriptionTab(SQL sql) {
		super(query, sqlTableName, linkTables, null, sql);
	}
}
