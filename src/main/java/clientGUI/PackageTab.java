/*package clientGUI;

import backend.SQL;

import javax.swing.*;
import java.awt.*;

class PackageTab extends GenericList {
	private static String query = "SELECT * FROM HCL_package WHERE active = 1";
	//Tab name, foreign PK, link table name, other table name, foreign identifier
	private static String[][] linkTables = {{ "Orders", "order_id", "HCL_order_package", "HCL_order", "adress" },
	{ "Foods", "food_id", "HCL_package_food", "HCL_food", "name" }};
	private SQL sql;
	public PackageTab(SQL sql, int role) {
		super(query, "HCL_package", linkTables, null, sql, role);
		add(new GenericSearch(), BorderLayout.SOUTH);
		this.sql = sql;
	}
	public int generate(String[] args) {
		PackageManager mng = new PackageManager(sql);
		int price = Integer.parseInt(args[2]);
		return mng.generate(args[1], price);
	}
	public int delete(int nr) {
		PackageManager mng = new PackageManager(sql);
		int ret = mng.delete(nr);
		System.out.println("Delete code" + ret);
		return ret;
	}
}*/

