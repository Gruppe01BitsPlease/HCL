package clientGUI;
import backend.SQL;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

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
        setTitle("Bits Please HCL System 0.5 - " + username);
		Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/titleIcon.png"));
		setIconImage(image);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Dynamic size based on screen resolution bitches
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		x = (int) (screen.width * 0.75);
		y = (int) (screen.height * 0.75);
		setMinimumSize(new Dimension(x, y));
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
			addTab(new EmployeeTab(sql));
			addTab(new CeoTab());
		}
		if (rolle == 1 || rolle == 0) {
			addTab(new OrderTab(sql));
			addTab(new CustomerTab(sql));
		}
		if (rolle == 2 || rolle == 0) {
			addTab(new FoodTab(sql));
			addTab(new IngredientTab(sql));
		}
		if (rolle == 3 || rolle == 0) {
			addTab(new PackageTab(sql));
		}
	}
	private String ceoName = "CEO functions";
	private String custName = "Customers";
	private String empName = "Employees";
	private String foodName = "Foods";
	private String ingrName = "Ingredients";
	private String ordrName = "Orders";
	private String packName = "Packages";

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
		else if (tab instanceof EmployeeTab) {
			if (tabs.indexOfTab(empName) == -1) {
				tabs.addTab(empName, tab);
			}
		}
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
		else if (tab instanceof PackageTab) {
			if (tabs.indexOfTab(packName) == -1) {
				tabs.addTab(packName, tab);
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
			Icon icon = UIManager.getIcon("InternalFrame.closeIcon");
			JButton close = new JButton(icon);
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
			if (rolle != 0) {
                settings.setEnabled(false);
            }
			JMenuItem DBsettings = new JMenuItem("Database Settings...");
			JMenuItem logout = new JMenuItem("Log out...");
			JMenuItem about = new JMenuItem("About...");
			JMenuItem refresh = new JMenuItem("Refresh all");
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
					JOptionPane.showMessageDialog(null, "Healthy Catering Limited © 2016 Bits Please");
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
            settings.add(DBsettings);
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
				if (tabs.indexOfTab(packName) == -1) {
					JMenuItem pack = new JMenuItem(packName);
					pack.addActionListener(new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent e) {
							addTab(new PackageTab(sql));
						}
					});
					newTab.add(pack);
				}
				else {
					JMenuItem pack = new JMenuItem(packName);
					pack.setEnabled(false);
					newTab.add(pack);
				}
			}
			if (rolle == 2 || rolle == 0) {
				if (tabs.indexOfTab(foodName) == -1) {
					JMenuItem food = new JMenuItem(foodName);
					food.addActionListener(new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent e) {
							addTab(new FoodTab(sql));
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
							addTab(new IngredientTab(sql));
						}
					});
					newTab.add(ingr);
				}
				else {
					JMenuItem ingr = new JMenuItem(ingrName);
					ingr.setEnabled(false);
					newTab.add(ingr);
				}
			}
			if (rolle == 1 || rolle == 0) {
				if (tabs.indexOfTab(ordrName) == -1) {
					JMenuItem order = new JMenuItem(ordrName);
					order.addActionListener(new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent e) {
							addTab(new OrderTab(sql));
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
							addTab(new CustomerTab(sql));
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
			if (rolle == 0) {
				if (tabs.indexOfTab(empName) == -1) {
					JMenuItem emp = new JMenuItem(empName);
					emp.addActionListener(new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent e) {
							addTab(new EmployeeTab(sql));
						}
					});
					newTab.add(emp);
				}
				else {
					JMenuItem emp = new JMenuItem(empName);
					emp.setEnabled(false);
					newTab.add(emp);
				}
				if (tabs.indexOfTab(ceoName) == -1) {
					JMenuItem ceo = new JMenuItem(ceoName);
					ceo.addActionListener(new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent e) {
							addTab(new CeoTab());
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
}
