package clientGUI;

/**
 * 	This class takes arrays with column names and returns the data types for them.
 * 	Data types are mostly used for edit windows
 */
abstract class DataTyper {
	public static DataType getDataType(String UIName) {
		String[] data = { UIName };
		DataType[] ret = getDataTypes(data);
		return ret[0];
	}
	public static DataType[] getDataTypesSQL(String[] sqlNames) {
		return getDataTypes(ColumnNamer.getNamesFromArray(sqlNames));
	}
	public enum DataType {
		ID, STRING, INT, BOOLEAN, CURDATE, DATE, ACTIVE, FOREIGN
	}
	public static DataType[] getDataTypes(String[] UINames) {
		DataType[] ret = new DataType[UINames.length];
		for (int i = 0; i < UINames.length; i++) {
			if (UINames[i].equals("Customer")) {
				ret[i] = DataType.ID;
			}
			else if (UINames[i].equals("Customer name")) {
				ret[i] = DataType.STRING;
			}
			else if (UINames[i].equals("Name")) {
				ret[i] = DataType.STRING;
			}
			else if (UINames[i].equals("E-mail")) {
				ret[i] = DataType.STRING;
			}
			else if (UINames[i].equals("Telephone")) {
				ret[i] = DataType.INT;
			}
			else if (UINames[i].equals("Food ID")) {
				ret[i] = DataType.ID;
			}
			else if (UINames[i].equals("Price")) {
				ret[i] = DataType.INT;
			}
			else if (UINames[i].equals("Ingredient ID")) {
				ret[i] = DataType.ID;
			}
			else if (UINames[i].equals("Amount")) {
				ret[i] = DataType.INT;
			}
			else if (UINames[i].equals("In stock")) {
				ret[i] = DataType.INT;
			}
			else if (UINames[i].equals("Purchase price")) {
				ret[i] = DataType.INT;
			}
			else if (UINames[i].equals("Contains nuts")) {
				ret[i] = DataType.BOOLEAN;
			}
			else if (UINames[i].equals("Contains gluten")) {
				ret[i] = DataType.BOOLEAN;
			}
			else if (UINames[i].equals("Contains lactose")) {
				ret[i] = DataType.BOOLEAN;
			}
			else if (UINames[i].equals("Other info")) {
				ret[i] = DataType.STRING;
			}
			else if (UINames[i].equals("Purchase date")) {
				ret[i] = DataType.CURDATE;
			}
			else if (UINames[i].equals("Expiration date")) {
				ret[i] = DataType.DATE;
			}
			else if (UINames[i].equals("Order ID")) {
				ret[i] = DataType.ID;
			}
			else if (UINames[i].equals("Address")) {
				ret[i] = DataType.STRING;
			}
			else if (UINames[i].equals("Order date")) {
				ret[i] = DataType.CURDATE;
			}
			else if (UINames[i].equals("Delivery date")) {
				ret[i] = DataType.CURDATE;
			}
			else if (UINames[i].equals("Package ID")) {
				ret[i] = DataType.ID;
			}
			else if (UINames[i].equals("Date ID")) {
				ret[i] = DataType.ID;
			}
			else if (UINames[i].equals("Date")) {
				ret[i] = DataType.DATE;
			}
			else if (UINames[i].equals("User ID")) {
				ret[i] = DataType.ID;
			}
			else if (UINames[i].equals("User name")) {
				ret[i] = DataType.STRING;
			}
			else if (UINames[i].equals("First name")) {
				ret[i] = DataType.STRING;
			}
			else if (UINames[i].equals("Last name")) {
				ret[i] = DataType.STRING;
			}
			else if (UINames[i].equals("E-mail")) {
				ret[i] = DataType.STRING;
			}
			else if (UINames[i].equals("Telephone")) {
				ret[i] = DataType.INT;
			}
			else if (UINames[i].equals("Address")) {
				ret[i] = DataType.STRING;
			}
			else if (UINames[i].equals("Post code")) {
				ret[i] = DataType.INT;
			}
			else if (UINames[i].equals("Start date")) {
				ret[i] = DataType.CURDATE;
			}
			else if (UINames[i].equals("Post code")) {
				ret[i] = DataType.INT;
			}
			else if (UINames[i].equals("Delivered")) {
				ret[i] = DataType.BOOLEAN;
			}
			else if (UINames[i].equals("Active")) {
				ret[i] = DataType.ACTIVE;
			}
			else if (UINames[i].equals("ID")) {
				ret[i] = DataType.ID;
			}
			else if (UINames[i].equals("Completed")) {
				ret[i] = DataType.BOOLEAN;
			}
			else {
				System.out.println("ERROR no data type found: " + UINames[i]);
			}
		}
		return ret;
	}
}
