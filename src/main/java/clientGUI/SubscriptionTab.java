package clientGUI;

import backend.SQL;
import backend.SubscriptionManager;

/**
 * Created by Jens on 14-Apr-16.
 */
public class SubscriptionTab extends GenericList {
	private static SQL sql;
	private static String query = "SELECT order_id, customer_name, adress, postnr FROM HCL_subscription NATURAL JOIN HCL_order NATURAL JOIN HCL_customer";
	private static String sqlTableName = "HCL_subscription";
	//Tab name, foreign PK, link table name, other table name, foreign identifier
	//{ "Foods", "food_id", "HCL_order_food", "HCL_food", "name" }
	private static String[][] linkTables = {{ "Dates", "date_id", "HCL_subscription_date", "HCL_subscription_date", "dato"}};
	public SubscriptionTab(SQL sql) {
		super(query, sqlTableName, linkTables, null, sql);
		this.sql = sql;
	}
	public int delete(int nr) {
		SubscriptionManager mng = new SubscriptionManager(sql);
		int ret = mng.delete(nr);
		System.out.println("Delete code" + ret);
		return ret;
	}
}
