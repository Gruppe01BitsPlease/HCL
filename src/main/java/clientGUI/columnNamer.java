package clientGUI;

import backend.SQL;

/**
 * Created by Jens on 06-Apr-16.
 */
abstract class ColumnNamer {
	/*public static String[] getNames(String sqlName) {
		if (sqlName.equals("HCL_food_ingredient")) {
			String[] ret = { "Food ID", "Ingredient ID", "Ingredient name", "Amount" };
			return ret;
		}
		if (sqlName.equals("HCL_order_food")) {
			String[] ret = { "Order ID", "Food ID" , "Address", "Amount" };
			return ret;
		}
		if (sqlName.equals("HCL_order_package")) {
			String[] ret = { "Order ID", "Package ID", "Package name" };
			return ret;
		}
		if (sqlName.equals("HCL_package_food")) {
			String[] ret = { "Package ID", "Food ID", "Amount", "Name" };
			return ret;
		}
		if (sqlName.equals("HCL_subscription_date")) {
			String[] ret = { "Date ID", "Order ID", "Date" };
			return ret;
		}
		if (sqlName.equals("HCL_customer")) {
			String[] ret = { "Customer ID", "Name", "E-mail", "Telephone" };
			return ret;
		}
		if (sqlName.equals("HCL_food")) {
			String[] ret = { "Food ID", "Name", "Price" };
			return ret;
		}
		if (sqlName.equals("HCL_ingredient")) {
			String[] ret = { "Ingredient ID", "Name", "In stock", "Purchase price", "Contains nuts", "Contains gluten", "Contains lactose", "Other", "Purchase date", "Expiration date" };
			return ret;
		}
		if (sqlName.equals("HCL_order")) {
			String[] ret = { "Order ID", "Customer ID", "Price", "Address", "Post code", "Order date", "Delivery date" };
			return ret;
		}
		if (sqlName.equals("HCL_package")) {
			String[] ret = { "Package ID", "Name", "Price" };
			return ret;
		}
		if (sqlName.equals("HCL_subscription")) {
			String[] ret = { "Order ID" };
			return ret;
		}
		if (sqlName.equals("HCL_users")) {
			String[] ret = { "User ID", "User name", "First name", "Last name", "E-mail", "Telephone", "Address", "Post code"};
			return ret;
		}
		String[] ret = { "ERROR!" };
		return ret;
	}*/
	public static String[] getNames(String query, SQL sql) {
		return getNamesFromArray(sql.getColumnNames(query));
	}
	public static String[][] getNamesWithOriginals(String query, SQL sql) {
		String[] one = sql.getColumnNames(query);
		String[] two = getNames(query, sql);
		String[][] ret = { one, two };
		return ret;
	}
	public static String[] getNamesFromArray(String[] sqlNames){
		String[] ret = new String[sqlNames.length];
		for (int i = 0; i < sqlNames.length; i++) {
			if (sqlNames[i].equals("customer_id")) {
				ret[i] = "Customer ID";
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
				ret[i] = "Food ID";
			}
			else if (sqlNames[i].equals("price")) {
				ret[i] = "Price";
			}
			else if (sqlNames[i].equals("ingredient_id")) {
				ret[i] = "Ingredient ID";
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
				ret[i] = "Order ID";
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
				ret[i] = "Date ID";
			}
			else if (sqlNames[i].equals("dato")) {
				ret[i] = "Date";
			}
			else if (sqlNames[i].equals("user_id")) {
				ret[i] = "User ID";
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
			else {
				System.out.println(sqlNames[i]);
				ret[i] = "ERROR " + sqlNames[i];
			}
		}
		return ret;
	}
}
