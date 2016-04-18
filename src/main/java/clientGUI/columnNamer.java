package clientGUI;

import backend.SQL;

import java.util.Arrays;

/**
 * Created by Jens on 06-Apr-16.
 */
abstract class ColumnNamer {
	public static String[] getNames(String query, SQL sql) {
		return getNamesFromArray(sql.getColumnNames(query));
	}
	public static String[][] getNamesWithOriginals(String query, SQL sql) {
		String[] one = sql.getColumnNames(query);
		System.out.println(Arrays.toString(one));
		String[] two = getNames(query, sql);
		System.out.println(Arrays.toString(two));
		String[][] ret = { one, two };
		return ret;
	}
	public static String[] getNamesFromArray(String[] sqlNames){
		String[] ret = new String[sqlNames.length];
		for (int i = 0; i < sqlNames.length; i++) {
			if (sqlNames[i].equals("customer_id")) {
				ret[i] = "ID";
			}
			else if (sqlNames[i].equals("customer_name")) {
				ret[i] = "Customer name";
			}
			else if (sqlNames[i].equals("name")) {
				ret[i] = "Name";
			}
			else if (sqlNames[i].equals("epost")) {
				ret[i] = "E-mail";
			}
			else if (sqlNames[i].equals("tlf")) {
				ret[i] = "Telephone";
			}
			else if (sqlNames[i].equals("food_id")) {
				ret[i] = "ID";
			}
			else if (sqlNames[i].equals("price")) {
				ret[i] = "Price";
			}
			else if (sqlNames[i].equals("ingredient_id")) {
				ret[i] = "ID";
			}
			else if (sqlNames[i].equals("number")) {
				ret[i] = "Amount";
			}
			else if (sqlNames[i].equals("stock")) {
				ret[i] = "In stock";
			}
			else if (sqlNames[i].equals("purchase_price")) {
				ret[i] = "Purchase price";
			}
			else if (sqlNames[i].equals("nuts")) {
				ret[i] = "Contains nuts";
			}
			else if (sqlNames[i].equals("gluten")) {
				ret[i] = "Contains gluten";
			}
			else if (sqlNames[i].equals("lactose")) {
				ret[i] = "Contains lactose";
			}
			else if (sqlNames[i].equals("other")) {
				ret[i] = "Other info";
			}
			else if (sqlNames[i].equals("purchase_date")) {
				ret[i] = "Purchase date";
			}
			else if (sqlNames[i].equals("expiration_date")) {
				ret[i] = "Expiration date";
			}
			else if (sqlNames[i].equals("order_id")) {
				ret[i] = "ID";
			}
			else if (sqlNames[i].equals("adress")) {
				ret[i] = "Address";
			}
			else if (sqlNames[i].equals("order_date")) {
				ret[i] = "Order date";
			}
			else if (sqlNames[i].equals("delivery_date")) {
				ret[i] = "Delivery date";
			}
			else if (sqlNames[i].equals("package_id")) {
				ret[i] = "Package ID";
			}
			else if (sqlNames[i].equals("date_id")) {
				ret[i] = "ID";
			}
			else if (sqlNames[i].equals("dato")) {
				ret[i] = "Date";
			}
			else if (sqlNames[i].equals("user_id")) {
				ret[i] = "ID";
			}
			else if (sqlNames[i].equals("user_name")) {
				ret[i] = "User name";
			}
			else if (sqlNames[i].equals("user_firstname")) {
				ret[i] = "First name";
			}
			else if (sqlNames[i].equals("user_lastname")) {
				ret[i] = "Last name";
			}
			else if (sqlNames[i].equals("user_email")) {
				ret[i] = "E-mail";
			}
			else if (sqlNames[i].equals("user_tlf")) {
				ret[i] = "Telephone";
			}
			else if (sqlNames[i].equals("user_adress")) {
				ret[i] = "Address";
			}
			else if (sqlNames[i].equals("user_postnr")) {
				ret[i] = "Post code";
			}
			else if (sqlNames[i].equals("user_start")) {
				ret[i] = "Start date";
			}
			else if (sqlNames[i].equals("postnr")) {
				ret[i] = "Post code";
			}
			else if (sqlNames[i].equals("delivered")) {
				ret[i] = "Delivered";
			}
			else if (sqlNames[i].equals("active")) {
				ret[i] = "Active";
			}
			else {
				System.out.println(sqlNames[i]);
				ret[i] = "ERROR " + sqlNames[i];
			}
		}
		return ret;
	}
}
