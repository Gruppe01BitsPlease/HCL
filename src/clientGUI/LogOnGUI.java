package clientGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import backend.*;

@SuppressWarnings("serial")
class LogOnGUI extends JFrame{
	protected LogOnGUI (String title){
		//window parameters
		setTitle(title);
		setLayout(new GridLayout(5, 1));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 190);
		setLocationRelativeTo(null);
		setResizable(false);
		
		//Create label, textfield and center their text.
		JLabel jUser = new JLabel("Username: ", SwingConstants.LEFT);
		final JTextField user = new JTextField();
		user.setHorizontalAlignment(JTextField.LEFT);
		JLabel jPassword = new JLabel("Password: ", SwingConstants.LEFT);
		final JTextField password = new JPasswordField();
		Action enterpass = new AbstractAction() {
			public void actionPerformed(ActionEvent pressed) {
				String navn = user.getText();
				String pass = password.getText();
				User u = new User("jdbc:mysql://mysql.stud.iie.ntnu.no:3306/", "olavhus", "CmrXjoQn");
				int i = u.logon(navn, pass);
				if (i >= 0) {
					System.out.println("LOGGEDON");
					MainMenuGUI main = new MainMenuGUI("Vision 0.1", i);
					main.setVisible(true);
				}
				else {
					JOptionPane.showMessageDialog(null, "Login feilet!");
				}
			}
		};
		password.addActionListener(enterpass);
		password.setHorizontalAlignment(JTextField.LEFT);
		
		//add label and textfield
		add(jUser);
		add(user);
		add(jPassword);
		add(password);
		
		//buttons and their action for buttonpanel
		JButton LogOn = new JButton("LogOn");
		LogOn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent pressed) {
				String navn = user.getText();
				String pass = password.getText();
				User u = new User();
				int i = u.logon(navn, pass);
				if (i >= 0) {
					System.out.println("LOGGEDON");
					MainMenuGUI main = new MainMenuGUI("Vision 0.1", i);
					main.setVisible(true);
				}
				else {
					JOptionPane.showMessageDialog(null, "Login feilet!");
				}
			}
		});
		JButton Help = new JButton("About");
		Help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent pressed) {
			JOptionPane.showMessageDialog(null, "Vision 0.1" + "\n" +  "by Team Bits");
			}
		});
		
		//adding buttons to panel and panel to frame
		JPanel buttonrow = new JPanel();
		buttonrow.setLayout(new FlowLayout());
		buttonrow.add(LogOn);
		buttonrow.add(Help);
		add(buttonrow);
	}
}

class TestLogOn{
	public static void main(String[] args){
	//	User u = new User();
	//	u.generateUser("jens", "1234", 0);
		LogOnGUI test = new LogOnGUI("Vision 0.1");
		test.setVisible(true);
	}
}