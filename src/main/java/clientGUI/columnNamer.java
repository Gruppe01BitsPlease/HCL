package clientGUI;

/**
 * Created by Jens on 06-Apr-16.
 */
abstract class ColumnNamer {
	public static String[] getNames(String sqlName) {
		if (sqlName.equals("HCL_food_ingredient")) {
			String[] ret = { "Food ID", "Ingredient ID", "Amount" };
			return ret;
		}
		if (sqlName.equals("HCL_order_food")) {
			String[] ret = { "Order ID", "Food ID" , "Amount" };
			return ret;
		}
		if (sqlName.equals("HCL_order_package")) {
			String[] ret = { "Order ID", "Package ID" };
			return ret;
		}
		if (sqlName.equals("HCL_package_food")) {
			String[] ret = { "Package ID", "Food ID", "Amount" };
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
	}
}
