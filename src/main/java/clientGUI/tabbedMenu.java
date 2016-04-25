package clientGUI;
import backend.SQL;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;

/**
 * Main window
 */
public class tabbedMenu extends JFrame {
	private SQL sql;
	//X and Y is the size of the main menu window, other windows should be scaled according to this value
	private int x;
	private int y;
	private static JTabbedPane tabs;
	private int rolle;

	public tabbedMenu(int rolle, String username) throws Exception {
		boolean searchAdded = false;
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
		setMinimumSize(Stuff.getWindowSize(1, 1));
		setLocationRelativeTo(null);
		setResizable(true);
		tabs = new JTabbedPane();
		addTabs();
		add(tabs, BorderLayout.CENTER);
		add(new menubar(rolle), BorderLayout.NORTH);
		this.setVisible(true);
	}
	// 1 salg, 2 chef, 3 driver, 0 ceo

	private void addTabs() {
		if (rolle == 0) {
			addTab(new CeoTab(rolle, sql));
		}
		if (rolle <= 1) {
			addTab(new CustomerTab(sql, rolle));
            addTab(new OrderTab(sql, rolle));
		}
		if (rolle <= 2) {
			addTab(new FoodTab(sql, rolle));
			addTab(new IngredientTab(sql, rolle));
			addTab(new ShoppingListTab(sql));
		}
		if (rolle == 2) {
			addTab(new ChefTab(sql, rolle));
		}
		if (rolle == 3) {
			addTab(new DriverTab(sql, rolle));
		}
	}

	private String ceoName = "CEO functions";
	private String custName = "Customers";
	private String foodName = "Foods";
	private String ingrName = "Ingredients";
	private String ordrName = "Orders";
	private String chefName = "Chef view";
	private String drivName = "Driver view";
	private String shopName = "Shopping list";

	private void addTab(JPanel tab) {

		if (tab instanceof CeoTab) {
			if (tabs.indexOfTab(ceoName) == -1) {
				tabs.addTab(ceoName, tab);
			}
		} else if (tab instanceof CustomerTab) {
			if (tabs.indexOfTab(custName) == -1) {
				tabs.addTab(custName, tab);
			}
		}
		else if (tab instanceof FoodTab) {
			if (tabs.indexOfTab(foodName) == -1) {
				tabs.addTab(foodName, tab);
			}
		} else if (tab instanceof IngredientTab) {
			if (tabs.indexOfTab(ingrName) == -1) {
				tabs.addTab(ingrName, tab);
			}
		} else if (tab instanceof OrderTab) {
			if (tabs.indexOfTab(ordrName) == -1) {
				tabs.addTab(ordrName, tab);
			}
		}
		else if (tab instanceof ChefTab) {
			if (tabs.indexOfTab(chefName) == -1) {
				tabs.addTab(chefName, tab);
			}
		}
		else if (tab instanceof DriverTab) {
			if (tabs.indexOfTab(drivName) == -1) {
				tabs.addTab(drivName, tab);
			}
		}
		else if (tab instanceof ShoppingListTab) {
			if (tabs.indexOfTab(shopName) == -1) {
				tabs.addTab(shopName, tab);
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
				public void menuDeselected(MenuEvent e) {
				}

				@Override
				public void menuCanceled(MenuEvent e) {
				}
			});
			refresh.addActionListener(refreshpress);
			DBsettings.addActionListener(settingspress);
			logout.addActionListener(logoutpress);
			about.addActionListener(aboutpress);
			editUser.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						JPanel tab = (JPanel)tabs.getTabComponentAt(tabs.indexOfTab(ceoName));
						userEditMenu edit = new userEditMenu(sql, rolle, (JPanel) tabs.getTabComponentAt(tabs.indexOfTab(ceoName)));
					}
					catch (Exception k) {
						userEditMenu edit = new userEditMenu(sql, rolle, null);
					}
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
			if (rolle == 3 || rolle == 0) {
				if (tabs.indexOfTab(drivName) == -1) {
					JMenuItem driv = new JMenuItem(drivName);
					driv.addActionListener(new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent e) {
							addTab(new DriverTab(sql, rolle));
						}
					});
					newTab.add(driv);
				} else {
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
				} else {
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
				} else {
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
				} else {
					JMenuItem chf = new JMenuItem(chefName);
					chf.setEnabled(false);
					newTab.add(chf);
				}
				if (tabs.indexOfTab(shopName) == -1) {
					JMenuItem shp = new JMenuItem(shopName);
					shp.addActionListener(e -> {
						addTab(new ShoppingListTab(sql));
					});
					newTab.add(shp);
				}
				else {
					JMenuItem shp = new JMenuItem(shopName);
					shp.setEnabled(false);
					newTab.add(shp);
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
				} else {
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
				} else {
					JMenuItem cust = new JMenuItem(custName);
					cust.setEnabled(false);
					newTab.add(cust);
				}
			}
			if (rolle == 0) {
				if (tabs.indexOfTab(ceoName) == -1) {
					JMenuItem ceo = new JMenuItem(ceoName);
					ceo.addActionListener(new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent e) {
							addTab(new CeoTab(rolle, sql));
						}
					});
					newTab.add(ceo);
				} else {
					JMenuItem ceo = new JMenuItem(ceoName);
					ceo.setEnabled(false);
					newTab.add(ceo);
				}
			}
		}
		private class settingsMenu extends JFrame {
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
}

