package clientGUI;

import backend.Logon;
import backend.SQL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

import backend.*;

/**
 * Created by Jens on 14.03.2016.
 */
public class tabbedMenu extends JFrame {
    SQL sql;
    //X and Y is the size of the main menu window, other windows should be scaled according to this value
    //
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
        tabs.addTab("Employees", new EmployeeTab(sql));
        tabs.addTab("CEO functions", new CeoTab());
		tabs.addTab("Orders", new OrderTab(sql));
        add(tabs, BorderLayout.CENTER);
		add(bar, BorderLayout.NORTH);
        this.setVisible(true);
    }

    //Contructor for searchwindow & edit window
    public tabbedMenu() {
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
}
class test {
    public static void main(String[] args) throws Exception {
        tabbedMenu menu = new tabbedMenu(1, "test");
    }
}