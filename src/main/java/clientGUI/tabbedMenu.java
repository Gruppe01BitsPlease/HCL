package clientGUI;
import backend.SQL;
import backend.UserManager;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

/**
 * Created by Jens on 14.03.2016.
 */
public class tabbedMenu extends JFrame {
    private SQL sql;
    //X and Y is the size of the main menu window, other windows should be scaled according to this value
	private int x;
	private int y;
	private static JTabbedPane tabs;
	private static boolean searchAdded;
	private int rolle;
	public tabbedMenu (int rolle, String username) throws Exception {
		searchAdded = false;
		sql = new SQL();
		this.rolle = rolle;
        setTitle("Bits Please HCL System 0.5 - User: " + username);
		Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/titleIcon.png"));
		setIconImage(image);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {}
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("Closing connection");
				sql.end();
				System.exit(0);
			}
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowActivated(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}
		});
		setMinimumSize(Stuff.getWindowSize(1,1));
        setLocationRelativeTo(null);
        setResizable(true);
        tabs = new JTabbedPane();
		addTabs();
		add(tabs, BorderLayout.CENTER);
		add(new menubar(rolle), BorderLayout.NORTH);
       // 1 salg, 2 chef, 3 driver, 0 ceo
        this.setVisible(true);
    }
	private void addTabs() {
		if (rolle == 0) {
			//addTab(new StatisticsTab());
			// addTab(new EmployeeTab(sql, rolle));
			addTab(new CeoTab(rolle, sql));
		}
		if (rolle == 1 || rolle == 0) {
			addTab(new OrderTab(sql, rolle));
			addTab(new CustomerTab(sql, rolle));
			//addTab(new SubscriptionTab(sql, rolle));
		}
		if (rolle == 2 || rolle == 0) {
			addTab(new FoodTab(sql, rolle));
			addTab(new IngredientTab(sql, rolle));
			addTab(new ChefTab(sql, rolle));
		}
		if (rolle == 3 || rolle == 0) {
			addTab(new DriverTab(sql, rolle));
		}
	}
	private String ceoName = "CEO functions";
	private String custName = "Customers";
	//private String empName = "Employees";
	private String foodName = "Foods";
	private String ingrName = "Ingredients";
	private String ordrName = "Orders";
	private String packName = "Packages";
	private String subscrName = "Subscriptions";
	private String chefName = "Chef view";
	private String statName = "Statistics";
	private String drivName = "Driver view";

	private void addTab(JPanel tab) {

		if (tab instanceof CeoTab) {
			if (tabs.indexOfTab(ceoName) == -1) {
				tabs.addTab(ceoName, tab);
			}
		}
		else if (tab instanceof CustomerTab) {
			if (tabs.indexOfTab(custName) == -1) {
				tabs.addTab(custName, tab);
			}
		}
		/*else if (tab instanceof EmployeeTab) {
			if (tabs.indexOfTab(empName) == -1) {
				tabs.addTab(empName, tab);
			}
		}*/
		else if (tab instanceof FoodTab) {
			if (tabs.indexOfTab(foodName) == -1) {
				tabs.addTab(foodName, tab);
			}
		}
		else if (tab instanceof IngredientTab) {
			if (tabs.indexOfTab(ingrName) == -1) {
				tabs.addTab(ingrName, tab);
			}
		}
		else if (tab instanceof OrderTab) {
			if (tabs.indexOfTab(ordrName) == -1) {
				tabs.addTab(ordrName, tab);
			}
		}
		/*else if (tab instanceof PackageTab) {
			if (tabs.indexOfTab(packName) == -1) {
				tabs.addTab(packName, tab);
			}
		}*/
		/*else if (tab instanceof SubscriptionTab) {
			if (tabs.indexOfTab(subscrName) == -1) {
				tabs.addTab(subscrName, tab);
			}
		}*/
		else if (tab instanceof ChefTab) {
			if (tabs.indexOfTab(chefName) == -1) {
				tabs.addTab(chefName, tab);
			}
		}
		/*else if (tab instanceof StatisticsTab) {
			if (tabs.indexOfTab(statName) == -1) {
				tabs.addTab(statName, tab);
			}
		}*/
		else if (tab instanceof DriverTab) {
			if (tabs.indexOfTab(drivName) == -1) {
				tabs.addTab(drivName, tab);
			}
		}
		addCloseButtons();
	}
	private void addCloseButtons() {
		for (int i = 0; i < tabs.getTabCount(); i++) {
			tabs.setTabComponentAt(i, new tabCloser(tabs.getTitleAt(i)));
		}
	}
	private class tabCloser extends JPanel {
		public tabCloser(String title) {
			BorderLayout layout = new BorderLayout(5, 0);
			setLayout(layout);
			setOpaque(false);
			JLabel tit = new JLabel(title);
			ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/delete.png")));
			JButton close = new JButton(icon);

			close.setContentAreaFilled(false);
			close.setBorderPainted(false);
			Dimension size = new Dimension(icon.getIconWidth(), icon.getIconHeight());
			close.setPreferredSize(size);
			close.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (tabs.getTabCount() > 1) {
						tabs.removeTabAt(tabs.indexOfTab(title));
					}
				}
			});
			add(tit);
			add(close, BorderLayout.EAST);
			setVisible(true);
		}
	}
	private class menubar extends JMenuBar {
		private JMenu newTab;
		public menubar(int rolle) {
			newTab = new JMenu("View");
			JMenu file = new JMenu("File");
            JMenu settings = new JMenu("Settings");
			JMenuItem DBsettings = new JMenuItem("Database Settings...");
			if (rolle != 0) {
				DBsettings.setEnabled(false);
			}
			JMenuItem logout = new JMenuItem("Log out...");
			JMenuItem about = new JMenuItem("About...");
			JMenuItem refresh = new JMenuItem("Refresh all");
			JMenuItem editUser = new JMenuItem("Edit user...");
			Action settingspress = new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new SettingsGUI();
				}
			};
			Action logoutpress = new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					LogOnGUI logon = new LogOnGUI();
					dispose();
				}
			};
			Action aboutpress = new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(null, "FoodPlease © 2016 Bits Please");
				}
			};
			Action refreshpress = new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					for (int i = 0; i < tabs.getTabCount(); i++) {
						if (tabs.getComponentAt(i) instanceof GenericList) {
							((GenericList) tabs.getComponentAt(i)).refresh();
						}
					}
				}
			};
			newTab.addMenuListener(new MenuListener() {
				@Override
				public void menuSelected(MenuEvent e) {
					addTabButtons();
				}
				@Override
				public void menuDeselected(MenuEvent e) {}
				@Override
				public void menuCanceled(MenuEvent e) {}
			});
			refresh.addActionListener(refreshpress);
			DBsettings.addActionListener(settingspress);
			logout.addActionListener(logoutpress);
			about.addActionListener(aboutpress);
			editUser.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					userEditMenu edit = new userEditMenu();
				}
			});
            settings.add(DBsettings);
			settings.add(editUser);
			file.add(refresh);
            file.add(logout);
            file.add(about);
			add(file);
            add(settings);
			add(newTab);
		}
		private void addTabButtons() {
			newTab.removeAll();
			JMenuItem orig = new JMenuItem("Default");
			orig.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					addTabs();
				}
			});
			newTab.add(orig);
			if(rolle == 3 || rolle == 0) {
				if (tabs.indexOfTab(drivName) == -1) {
					JMenuItem driv = new JMenuItem(drivName);
					driv.addActionListener(new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent e) {
							addTab(new DriverTab(sql, rolle));
						}
					});
					newTab.add(driv);
				}
				else {
					JMenuItem driv = new JMenuItem(drivName);
					driv.setEnabled(false);
					newTab.add(driv);
				}
			}
			if (rolle == 2 || rolle == 0) {
				if (tabs.indexOfTab(foodName) == -1) {
					JMenuItem food = new JMenuItem(foodName);
					food.addActionListener(new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent e) {
							addTab(new FoodTab(sql, rolle));
						}
					});
					newTab.add(food);
				}
				else {
					JMenuItem food = new JMenuItem(foodName);
					food.setEnabled(false);
					newTab.add(food);
				}
				if (tabs.indexOfTab(ingrName) == -1) {
					JMenuItem ingr = new JMenuItem(ingrName);
					ingr.addActionListener(new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent e) {
							addTab(new IngredientTab(sql, rolle));
						}
					});
					newTab.add(ingr);
				}
				else {
					JMenuItem ingr = new JMenuItem(ingrName);
					ingr.setEnabled(false);
					newTab.add(ingr);
				}
				if (tabs.indexOfTab(chefName) == -1) {
					JMenuItem chf = new JMenuItem(chefName);
					chf.addActionListener(new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent e) {
							addTab(new ChefTab(sql, rolle));
						}
					});
					newTab.add(chf);
				}
				else {
					JMenuItem chf = new JMenuItem(chefName);
					chf.setEnabled(false);
					newTab.add(chf);
				}
			}
			if (rolle == 1 || rolle == 0) {
				if (tabs.indexOfTab(ordrName) == -1) {
					JMenuItem order = new JMenuItem(ordrName);
					order.addActionListener(new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent e) {
							addTab(new OrderTab(sql, rolle));
						}
					});
					newTab.add(order);
				}
				else {
					JMenuItem order = new JMenuItem(ordrName);
					order.setEnabled(false);
					newTab.add(order);
				}
				if (tabs.indexOfTab(custName) == -1) {
					JMenuItem cust = new JMenuItem(custName);
					cust.addActionListener(new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent e) {
							addTab(new CustomerTab(sql, rolle));
						}
					});
					newTab.add(cust);
				}
				else {
					JMenuItem cust = new JMenuItem(custName);
					cust.setEnabled(false);
					newTab.add(cust);
				}
			}
			if (rolle == 0) {/*
				if (tabs.indexOfTab(empName) == -1) {
					JMenuItem emp = new JMenuItem(empName);
					emp.addActionListener(new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent e) {
							addTab(new EmployeeTab(sql, rolle));
						}
					});
					newTab.add(emp);
				}
				else {
					JMenuItem emp = new JMenuItem(empName);
					emp.setEnabled(false);
					newTab.add(emp);
				}*/
				if (tabs.indexOfTab(ceoName) == -1) {
					JMenuItem ceo = new JMenuItem(ceoName);
					ceo.addActionListener(new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent e) {
							addTab(new CeoTab(rolle, sql));
						}
					});
					newTab.add(ceo);
				}
				else {
					JMenuItem ceo = new JMenuItem(ceoName);
					ceo.setEnabled(false);
					newTab.add(ceo);
				}
			}
		}
		private class settingsMenu extends JFrame{
			public settingsMenu() {
				setTitle("Database settings");
				setLayout(new GridLayout(6, 1));
				setSize((int) (x * 0.3), (int) (y * 0.3));
				setLocationRelativeTo(null);
				JLabel dbNameLabel = new JLabel("Database name:");
				JTextField DBname = new JTextField();
				JLabel userLabel = new JLabel("User Name:");
				JTextField user = new JTextField();
				JLabel passLabel = new JLabel("Password:");
				JTextField pass = new JTextField();
				add(dbNameLabel);
				add(DBname);
				add(userLabel);
				add(user);
				add(passLabel);
				add(pass);
				setVisible(true);
			}
		}

	}
	public static void main(String[] args) throws Exception {
		tabbedMenu menu = new tabbedMenu(0, "CEO");
        //tabbedMenu menu2 = new tabbedMenu(1, "Sales");
	}
	private class userEditMenu extends JFrame {
		private String userName;
		private JPasswordField passField;
		public userEditMenu() {
			setSize(Stuff.getWindowSize(0.3,0.2));
			setLocationRelativeTo(null);
			setLayout(new BorderLayout());
			JLabel loginAgain = new JLabel("Please log in again");
			JPanel namePassPanel = new JPanel();
			namePassPanel.setLayout(new GridLayout(2,2));
			JLabel userNameLabel = new JLabel("User name:");
			JLabel passLabel = new JLabel("Password:");
			JTextField userNameField = new JTextField();
			passField = new JPasswordField();
			namePassPanel.add(userNameLabel);
			namePassPanel.add(userNameField);
			namePassPanel.add(passLabel);
			namePassPanel.add(passField);
			JPanel okCancel = new JPanel(new GridLayout(1, 2));
			JButton okButton = new JButton("OK");
			JButton cancelButton = new JButton("Cancel");
			Action okAction = new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					UserManager mng = new UserManager(sql);
					int res = mng.logon(userNameField.getText(), new String(passField.getPassword()));
					if (res >= 0) {
						userName = userNameField.getText();
						editMenu menu = new editMenu();
						dispose();
					}
					else {
						JOptionPane.showMessageDialog(null, "Wrong user name or password");
					}
				}
			};
			passField.addActionListener(okAction);
			okButton.addActionListener(okAction);
			cancelButton.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			okCancel.add(okButton);
			okCancel.add(cancelButton);
			add(loginAgain, BorderLayout.NORTH);
			add(namePassPanel, BorderLayout.CENTER);
			add(okCancel, BorderLayout.SOUTH);
			setVisible(true);
		}
		private class editMenu extends JFrame {
			public editMenu() {
				setSize(Stuff.getWindowSize(0.5,0.5));
				setLocationRelativeTo(null);
				String selectQuery = "SELECT user_name, user_firstname, user_lastname, user_email, user_tlf, " +
						"user_adress, user_postnr, user_start FROM HCL_user WHERE user_name = '" + userName + "'";
				System.out.println("User edit select query = " + selectQuery);
				setLayout(new BorderLayout());
				String[] selectedUser = sql.getRow(selectQuery);
				String[][] titles = ColumnNamer.getNamesWithOriginals(selectQuery, sql);
				editFields fields = new editFields(titles[1], selectedUser, false, null, sql);
				fields.getFields().get(0).setEnabled(false);
				if (rolle != 0) {
					fields.getFields().get(7).setEnabled(false);
				}
				add(fields, BorderLayout.CENTER);
				JPanel saveCancel = new JPanel(new GridLayout(1, 2));
				JButton save = new JButton("Save");
				JButton cancel = new JButton("Cancel");
				JButton changePass = new JButton("Change password...");
				save.addActionListener(new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent e) {
						int sure = JOptionPane.showConfirmDialog(editMenu.this, "Are you sure?", "Edit user", JOptionPane.YES_NO_OPTION);
						if (sure == 0) {
							String[] newValues = fields.getNewValues();
							for (int i = 0; i < newValues.length; i++) {
								if (selectedUser[i] == null || !(selectedUser[i].equals(newValues[i]))) {
									sql.update("HCL_user", titles[0][i], "user_name", userName, newValues[i]);
								}
							}
							dispose();
							for (int i = 0; i < tabs.getTabCount(); i++) {
								if (tabs.getComponentAt(i) instanceof CeoTab) {
									((GenericList) tabs.getComponentAt(i)).refresh();
								}
							}
						}
					}
				});
				cancel.addActionListener(new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				changePass.addActionListener(new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent e) {
						passBox box = new passBox();
					}
				});
				saveCancel.add(save);
				saveCancel.add(cancel);
				saveCancel.add(changePass);
				add(saveCancel, BorderLayout.SOUTH);
				setVisible(true);
			}
		}
		private class passBox extends JFrame {
			public passBox() {
				setLocationRelativeTo(null);
				setAlwaysOnTop(true);
				setSize(Stuff.getWindowSize(0.3,0.2));
				setLayout(new GridLayout(3, 2));
				JLabel newPassLabel = new JLabel("Enter new password:");
				JPasswordField newPassField = new JPasswordField();
				JLabel reEnterPass = new JLabel("Reenter password:");
				JPasswordField newPassField2 = new JPasswordField();
				JButton save = new JButton("Save");
				JButton cancel = new JButton("Cancel");
				save.addActionListener(new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent e) {
						int sure = JOptionPane.showConfirmDialog(passBox.this, "Are you sure?", "Edit user", JOptionPane.YES_NO_OPTION);
						if (sure == 0) {
							if (!(Arrays.equals(newPassField.getPassword(), newPassField2.getPassword()))) {
								JOptionPane.showMessageDialog(passBox.this, "Passwords do not match");
							} else {
								UserManager mng = new UserManager(sql);
								String oldPass = new String(passField.getPassword());
								String newPass = new String(newPassField.getPassword());
								mng.changePassword(userName, oldPass, newPass);
								dispose();
							}
						}
					}
				});
				cancel.addActionListener(new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				add(newPassLabel);
				add(newPassField);
				add(reEnterPass);
				add(newPassField2);
				add(save);
				add(cancel);
				setVisible(true);
			}
		}
	}
}
