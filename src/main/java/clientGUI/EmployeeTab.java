package clientGUI;

import backend.SQL;
import backend.UserManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;

/**
 * Creates the JPanel that is used as a tab in tabbedMenu
 */
class EmployeeTab extends GenericList {
	private static String query = "SELECT user_id, user_name, user_firstname, user_lastname, user_email, user_tlf, " +
			"user_adress, user_postnr, user_start FROM HCL_user WHERE active = 1";
	private SQL sql;
	private int role;
	public EmployeeTab(SQL sql, int role) {
		super(query, "HCL_user", null, null, sql, role, 1);
		this.sql = sql;
		this.role = role;
		add(new GenericSearch(), BorderLayout.SOUTH);
	}
	public int generate(String[] args) {
		int role = 3;
		UserManager mng = new UserManager(sql);
		try {
			role = Integer.parseInt(args[3]);
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Role is invalid!");
		}
		return mng.generate(args[1], args[2], role);
	}
	public void edit(int id, boolean newEntry) {
		if (newEntry && role == 0) {
			EditBox box = new EditBox();
		}
		else if (newEntry && role < 0) {
			JOptionPane.showMessageDialog(this, "You do not have the requred access level to create new users");
		}
		else if (!newEntry && role == 0) {
			UserEditMenu menu = new UserEditMenu(Integer.toString(getSelectedID()), sql, role, this);
		}
	}
	public int delete(int id) {
		int ret = -1;
		if (role == 0) {
			UserManager mng = new UserManager(sql);
			String userName = sql.getColumn("SELECT user_name FROM HCL_user WHERE user_id = " + id, 0)[0];
			ret = mng.delete(userName);
		}
		else {
			JOptionPane.showMessageDialog(EmployeeTab.this, "You do not have permission to delete users");
		}
		return ret;
	}
	private class EditBox extends JFrame {
		public EditBox() {
			setSize(Stuff.getWindowSize(0.3,0.25));
			setLocationRelativeTo(null);
			setTitle("New user");
			setLayout(new GridLayout(3, 2));
			// 1 salg, 2 chef, 3 driver, 0 ceo
			String[] roles = { "CEO", "Sales", "Chef", "Driver" };
			JLabel userNameLabel = new JLabel("User name:");
			JTextField userNameField = new JTextField();
			JLabel roleLabel = new JLabel("Access level:");
			JComboBox<String> roleBox = new JComboBox<>(roles);
			JButton save = new JButton("Save");
			JButton cancel = new JButton("Cancel");
			save.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int sure = JOptionPane.showConfirmDialog(EditBox.this, "Are you sure?", "Create user", JOptionPane.YES_NO_OPTION);
					if (sure == 0) {
						UserManager mng = new UserManager(sql);
						String pass = mng.generate(userNameField.getText(), roleBox.getSelectedIndex());
						String start = LocalDate.now().toString();
						sql.update("HCL_user", "user_start", "user_name", userNameField.getText(), start);
						JOptionPane.showMessageDialog(EditBox.this, "The password is: \"" + pass + "\". Write it down!");
					}
					dispose();
					refresh();
				}
			});
			cancel.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			add(userNameLabel);
			add(userNameField);
			add(roleLabel);
			add(roleBox);
			add(save);
			add(cancel);
			setVisible(true);
		}
	}
}

