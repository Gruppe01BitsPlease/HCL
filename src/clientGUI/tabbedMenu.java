package clientGUI;

import backend.Logon;
import backend.SQL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;

import backend.*;

/**
 * Created by Jens on 14.03.2016.
 */
public class tabbedMenu extends JFrame {
    SQL sql;
    //X and Y is the size of the main menu window, other windows should be scaled according to this value
	int x;
	int y;
    public tabbedMenu (int rolle, String username) throws Exception {
		sql = new SQL(new Logon(new File()));
        setTitle("Bits Please HCL System 0.1 - " + username);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Dynamic size based on screen resolution bitches
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		x = (int) (screen.width * 0.75);
		y = (int) (screen.height * 0.75);
        setSize(x, y);
        setLocationRelativeTo(null);
        setResizable(false);
        JTabbedPane tabs = new JTabbedPane();
		menubar bar = new menubar();
        tabs.addTab("Employees", new employeeTab());
        tabs.addTab("CEO functions", new CEOtab());
		tabs.addTab("Orders", new orderTab());
        add(tabs, BorderLayout.CENTER);
		add(bar, BorderLayout.NORTH);
        this.setVisible(true);
    }
    private class genericSearch extends JPanel {
        //This is a generic search tab with button, which will show results in a popup window
        String[][] searchTable;
        String[] titles;
        public genericSearch(String[][] table, String[] titles) {
            this.titles = titles;
            setLayout(new BorderLayout());
            JTextField search = new JTextField();
            JButton searcher = new JButton("Search");
            Action searchPress = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ArrayList<Integer> rowAdded = new ArrayList<Integer>();
                    searchTable = new String[table.length][table[0].length];
                    for (int i = 0; i < table.length; i++) {
                        for (int j = 0; j < table[i].length; j++) {
                            if (!(rowAdded.contains(i)) && table[i][j].toLowerCase().contains(search.getText().toLowerCase())) {
                                int k = 0;
                                boolean added = false;
                                for (int l = 0; l < searchTable.length; l++) {
                                    while (!added && k < searchTable[0].length) {
                                        if (searchTable[k][0] == null || searchTable[k][0].isEmpty()) {
                                            searchTable[k] = table[i];
                                            added = true;
                                            rowAdded.add(i);
                                            System.out.println(rowAdded.contains(i));
                                        } else {
                                            k++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    searchWindow window = new searchWindow();
                }
            };
            search.addActionListener(searchPress);
            searcher.addActionListener(searchPress);
            add(search, BorderLayout.CENTER);
            add(searcher, BorderLayout.EAST);
        }
        private class searchWindow extends JFrame {
            public searchWindow() {
                setSize((int) (x * 0.4), (int) (y * 0.4));
                setTitle("Search results");
                JTable searchTab = new JTable(searchTable, titles);
                add(searchTab, BorderLayout.CENTER);
                setLocationRelativeTo(null);
                setVisible(true);
            }
        }
    }
	private class menubar extends JMenuBar {
		public menubar() {
			JMenu file = new JMenu("File");
            JMenu settings = new JMenu("Settings");
			JMenuItem DBsettings = new JMenuItem("Database Settings...");
			JMenuItem logout = new JMenuItem("Log out...");
			JMenuItem about = new JMenuItem("About...");
			Action settingspress = new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					settingsMenu meny = new settingsMenu();
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
			DBsettings.addActionListener(settingspress);
			logout.addActionListener(logoutpress);
			about.addActionListener(aboutpress);
			settings.add(DBsettings);
            file.add(logout);
            file.add(about);
			add(file);
            add(settings);
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
    //Tabs for the menu, to add one just add it to "tabs" above
    private class employeeTab extends JPanel {
        String[][] table = {{ "Bob", "0" }, { "John", "1" }, { "Dave", "3" }}; //TESTING
//		String[][] table = sql.getStringTable("SELECT user_name, user_epost, user_adresse FROM HCL_users");
		String[] titles = { "Employees", "E-mail" };
        public employeeTab() {
            setLayout(new BorderLayout());
            center center = new center();
            Dimension dim = new Dimension((int) (x * 0.5), (int) (y * 0.5));
            center.setMinimumSize(dim);
            add(center, BorderLayout.CENTER);
			add(new genericSearch(table, titles), BorderLayout.SOUTH);
        }
        private class center extends JPanel {
            public center() {
                setLayout(new BorderLayout());
				JTable list = new JTable(table, titles);
				JScrollPane scroll = new JScrollPane(list);
                add(scroll, BorderLayout.CENTER);
            }
        }
    }
    private class CEOtab extends JPanel {
        public CEOtab() {
            setLayout(new GridLayout(2, 2));
            JButton test = new JButton("CEO Testing");
            JButton test2 = new JButton("Testing more");
            add(test);
            add(test2);
        }
    }
	private class orderTab extends JPanel {
		String[][] table = { { "McDonalds" , "McStreet 15"}, { "HiST", "Kjellern"}};
		String[] titles = { "Customer", "Adress"};
		public orderTab() {
			setLayout(new BorderLayout());
			add(new center(), BorderLayout.CENTER);
			add(new genericSearch(table, titles), BorderLayout.SOUTH);
		}
		private class center extends JPanel {
			public center() {
				setLayout(new BorderLayout());
				JTable list = new JTable(table, titles);
				JScrollPane scroll = new JScrollPane(list);
				add(scroll, BorderLayout.CENTER);
			}
		}
	}

}
class test {
    public static void main(String[] args) throws Exception {
        tabbedMenu menu = new tabbedMenu(1, "test");
    }
}