package clientGUI;
import backend.SQL;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by Jens on 14.03.2016.
 */
public class tabbedMenu extends JFrame {
    private SQL sql;
    //X and Y is the size of the main menu window, other windows should be scaled according to this value
	private int x;
	private int y;
	private OrderTab order;
	private EmployeeTab emp;
	private CeoTab CEO;
	private CustomerTab cust;
	private FoodTab food;
	private IngredientTab ingr;
	private PackageTab pack;
	public tabbedMenu (int rolle, String username) throws Exception {
		sql = new SQL();
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
        JTabbedPane tabs = new JTabbedPane();
		menubar bar = new menubar(rolle);
		order = new OrderTab(sql);
		emp = new EmployeeTab(sql);
		CEO = new CeoTab();
		cust = new CustomerTab(sql);
		food = new FoodTab(sql);
		ingr = new IngredientTab(sql);
		pack = new PackageTab(sql);




        add(tabs, BorderLayout.CENTER);
		add(bar, BorderLayout.NORTH);
       // 1 salg, 2 chef, 3 driver, 0 ceo
            if(rolle == 0){
                tabs.addTab("Employees", emp);
                tabs.addTab("CEO functions", CEO);
            }
            if(rolle == 1 || rolle == 0){
                tabs.addTab("Orders", order);
                tabs.addTab("Customers", cust);


            }
            if(rolle == 2 || rolle == 0){
                tabs.addTab("Food", food);
                tabs.addTab("Ingredients", ingr);

            }
            if(rolle == 3 || rolle == 0){
                tabs.addTab("Packages", pack);

            }

        this.setVisible(true);
    }

	private class menubar extends JMenuBar {
		public menubar(int rolle) {
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
					order.refresh();
					emp.refresh();
					cust.refresh();
					food.refresh();
					ingr.refresh();
					pack.refresh();
				}
			};
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
        tabbedMenu menu2 = new tabbedMenu(1, "Sales");
	}
}
