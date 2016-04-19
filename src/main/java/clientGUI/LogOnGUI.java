package clientGUI;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import backend.*;
import com.seaglasslookandfeel.*;

@SuppressWarnings("serial")
public class LogOnGUI extends JFrame{
	public LogOnGUI () {
		//window pa	parameters
		setTitle("Log in");
		Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/titleIcon.png"));
		setIconImage(image); // Put your own image instead of null
		setLayout(new GridLayout(5, 1));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		double x = (double) screen.width * 0.25;
		double y = (double) screen.height * 0.3;
		setSize((int) x, (int) y);
		setLocationRelativeTo(null);
		setResizable(false);

		//Create label, textfield and center their text.
		JLabel jUser = new JLabel("Username: ", SwingConstants.LEFT);
		final JTextField user = new JTextField();
		user.setHorizontalAlignment(JTextField.LEFT);
		JLabel jPassword = new JLabel("Password: ", SwingConstants.LEFT);
		final JTextField password = new JPasswordField();
		password.setHorizontalAlignment(JTextField.LEFT);

		//add label and textfield
		add(jUser);
		add(user);
		add(jPassword);
		add(password);

		//buttons and their action for buttonpanel
		JButton LogOn = new JButton("LogOn");
		JButton exit = new JButton("Exit");
		exit.addActionListener((pressed) -> {
			dispose();
		});
		Action enterpass = new AbstractAction() {
			public void actionPerformed(ActionEvent pressed) {
				//ENTERING ADMIN & ADMIN WILL GET ACCESS FOR TESTING!!!!! TODO: Remove that before release
				String navn = user.getText();
				String pass = password.getText();
				int i;
				try {
					UserManager u = new UserManager(new SQL());
					i = u.logon(navn, pass);
					if (i >= 0) {
						tabbedMenu main = new tabbedMenu(i, navn);
						dispose();
					} else if (i == -1) {
						JOptionPane.showMessageDialog(null, "The user name or password is incorrect.");
					} else if (i == -2) {
						JOptionPane.showMessageDialog(null, "Could not connect to the database.");
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Something went wrong!");
					e.printStackTrace();
				}
			}
		};
		LogOn.addActionListener(enterpass);
		password.addActionListener(enterpass);

		//adding buttons to panel and panel to frame
		JPanel buttonrow = new JPanel();
		buttonrow.setLayout(new FlowLayout());
		buttonrow.add(LogOn);
		buttonrow.add(exit);
		add(buttonrow);
		this.setVisible(true);
	}

	public static void main(String[] args) throws Exception {
		//	User u = new User();
		//	u.generateUser("jens", "1234", 0);
		LogOnGUI test = new LogOnGUI();
	}
}

