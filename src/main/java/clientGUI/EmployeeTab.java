package clientGUI;

import backend.SQL;
import backend.UserManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;


class EmployeeTab extends GenericList {
	private static String query = "SELECT user_id, user_name, user_firstname, user_lastname, user_email, user_tlf, " +
			"user_adress, user_postnr FROM HCL_users  WHERE active = 1";
	private SQL sql;
	private int role;
	public EmployeeTab(SQL sql, int role) {
		super(query, "HCL_users", null, null, sql, role);
		this.sql = sql;
		this.role = role;
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
			if (role != 0) {
				dispose();
				JOptionPane.showMessageDialog(null, "You do not have permission to edit employees.");
			}
			setSize((int) (x * 0.3), (int) (y * 0.3));
			String select = "SELECT user_id, user_name, user_firstname, user_lastname, user_email, user_tlf, " +
					"user_adress, user_postnr, user_start FROM HCL_users";
			System.out.println("Select query: " + select);
			String[][] titles = ColumnNamer.getNamesWithOriginals(select, sql);
			String[] selectedEmployee = new String[titles[0].length];
			setTitle("Employee");
			if (newEmployee) {
				setTitle("New employee");
			}
			if (!newEmployee) {
				selectedEmployee = sql.getRow(select + " WHERE user_id = " + id);
			}
			System.out.println(Arrays.toString(selectedEmployee));
			String[] dataTypes = DataTyper.getDataTypesSQL(titles[0]);
			setLayout(new GridLayout(dataTypes.length, 2));
			ArrayList<JComponent> fields = new ArrayList<>();
			for (int i = 0; i < dataTypes.length; i++) {
				if (dataTypes[i].equals("curdate")) {
					JLabel j = new JLabel(titles[1][i]);
					datePane k = new datePane(selectedEmployee[i]);
					if (newEmployee) {
						LocalDate now = LocalDate.now();
						String date = now.toString();
						k = new datePane(date);
					}
					fields.add(k);
					add(j);
					add(k);
				}
				else if (dataTypes[i].equals("id")) {
					JTextField k = new JTextField(selectedEmployee[i]);
					fields.add(k);
				} else {
					JLabel j = new JLabel(titles[1][i]);
					JTextField k = new JTextField(selectedEmployee[i]);
					fields.add(k);
					add(j);
					add(k);
				}
			}
			setLocationRelativeTo(null);
			setVisible(true);
			//if (selectedEmployee)
		}
	}
}

