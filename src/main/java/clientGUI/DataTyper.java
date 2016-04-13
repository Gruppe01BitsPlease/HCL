package clientGUI;

/**
 * Created by Jens on 12.04.2016.
 */
abstract class DataTyper {
	//This class takes arrays with column names and returns the data types for them.
	//Data types are mostly used for edit windows

	public static String getDataType(String UIName) {
		String[] data = { UIName };
		String[] ret = getDataTypes(data);
		return ret[0];
	}
	public static String getDataTypeSQL(String sqlName) {
		String[] data = { sqlName };
		String[] ret = getDataTypesSQL(data);
		return ret[0];
	}
	public static String[] getDataTypesSQL(String[] sqlNames) {
		return getDataTypes(ColumnNamer.getNamesFromArray(sqlNames));
	}
	public static String[] getDataTypes(String[] UINames) {
		String[] ret = new String[UINames.length];
		for (int i = 0; i < UINames.length; i++) {
			if (UINames[i].equals("Customer")) {
				ret[i] = "id";
			}
			else if (UINames[i].equals("Customer name")) {
				ret[i] = "string";
			}
			else if (UINames[i].equals("Name")) {
				ret[i] = "string";
			}
			else if (UINames[i].equals("E-mail")) {
				ret[i] = "string";
			}
			else if (UINames[i].equals("Telephone")) {
				ret[i] = "int";
			}
			else if (UINames[i].equals("Food ID")) {
				ret[i] = "id";
			}
			else if (UINames[i].equals("Price")) {
				ret[i] = "int";
			}
			else if (UINames[i].equals("Ingredient ID")) {
				ret[i] = "id";
			}
			else if (UINames[i].equals("Amount")) {
				ret[i] = "int";
			}
			else if (UINames[i].equals("In stock")) {
				ret[i] = "int";
			}
			else if (UINames[i].equals("Purchase price")) {
				ret[i] = "int";
			}
			else if (UINames[i].equals("Contains nuts")) {
				ret[i] = "boolean";
			}
			else if (UINames[i].equals("Contains gluten")) {
				ret[i] = "boolean";
			}
			else if (UINames[i].equals("Contains lactose")) {
				ret[i] = "boolean";
			}
			else if (UINames[i].equals("Other info")) {
				ret[i] = "string";
			}
			else if (UINames[i].equals("Purchase date")) {
				ret[i] = "curdate";
			}
			else if (UINames[i].equals("Expiration date")) {
				ret[i] = "date";
			}
			else if (UINames[i].equals("Order ID")) {
				ret[i] = "id";
			}
			else if (UINames[i].equals("Address")) {
				ret[i] = "string";
			}
			else if (UINames[i].equals("Order date")) {
				ret[i] = "curdate";
			}
			else if (UINames[i].equals("Delivery date")) {
				ret[i] = "date";
			}
			else if (UINames[i].equals("Package ID")) {
				ret[i] = "id";
			}
			else if (UINames[i].equals("Date ID")) {
				ret[i] = "id";
			}
			else if (UINames[i].equals("Date")) {
				ret[i] = "date";
			}
			else if (UINames[i].equals("User ID")) {
				ret[i] = "id";
			}
			else if (UINames[i].equals("User name")) {
				ret[i] = "string";
			}
			else if (UINames[i].equals("First name")) {
				ret[i] = "string";
			}
			else if (UINames[i].equals("Last name")) {
				ret[i] = "string";
			}
			else if (UINames[i].equals("E-mail")) {
				ret[i] = "string";
			}
			else if (UINames[i].equals("Telephone")) {
				ret[i] = "int";
			}
			else if (UINames[i].equals("Address")) {
				ret[i] = "string";
			}
			else if (UINames[i].equals("Post code")) {
				ret[i] = "int";
			}
			else if (UINames[i].equals("Start date")) {
				ret[i] = "date";
			}
			else if (UINames[i].equals("Post code")) {
				ret[i] = "int";
			}
			else if (UINames[i].equals("Delivered")) {
				ret[i] = "boolean";
			}
			else if (UINames[i].equals("Active")) {
				ret[i] = "id";
			}
			else {
				System.out.println("ERROR no data type found: " + UINames[i]);
			}
		}
		return ret;
	}
}
