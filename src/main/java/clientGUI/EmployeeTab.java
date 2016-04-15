package clientGUI;

import backend.SQL;
import backend.UserManager;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


class EmployeeTab extends GenericList {
	private static String query = "SELECT user_id, user_name, user_firstname, user_lastname, user_email, user_tlf, " +
			"user_adress, user_postnr FROM HCL_users  WHERE active = 1";
	private SQL sql;
	public EmployeeTab(SQL sql) {
		super(query, "HCL_users", null, null, sql);
		add(new GenericSearch(), BorderLayout.SOUTH);
	}
	/*public int generate(String[] args) {
		UserManager mng = new UserManager(sql);
		int role = Integer.parseInt(args[3]);
		return mng.generate(args[1], args[2], role);
	}*/
	public void edit(int id, boolean newEntry) {
		employeeEditWindow edit = new employeeEditWindow(id, newEntry);
	}
	class employeeEditWindow extends JFrame {
		public employeeEditWindow(int id, boolean newEmployee) {
			setSize((int) (x * 0.3), (int) (y * 0.3));
			setTitle("Employee");
			String[] selectedEmployee = sql.getRow("SELECT * FROM HCL_users WHERE user_id = " + id);
			System.out.println(Arrays.toString(selectedEmployee));
			JLabel username = new JLabel("User name:");
			JLabel nameRead = new JLabel(selectedEmployee[1]);
			JLabel role = new JLabel();
			//if (selectedEmployee)
		}
	}
}

