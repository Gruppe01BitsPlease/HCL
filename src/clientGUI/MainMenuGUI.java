package clientGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class MainMenuGUI extends JFrame {
	public MainMenuGUI (String title, int rolle){
		//window parameters
		setTitle(title);
		setLayout(new GridLayout(6, 1));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );
		setSize(300, 190);
		setLocationRelativeTo(null);
		setResizable(false);
		JButton Sales = new JButton("Sales");
		JButton Delivery = new JButton("Delivery");
		JButton Employees = new JButton("Employees");
		JButton CEO = new JButton("CEO");
		JButton Admin = new JButton("Admin");
		JButton Logout = new JButton("Log out");
		Logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent pressed) {
				dispose();
			}
		});
		Employees.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent pressed) {
				System.out.println("PLACEHOLDER");
				PersonaliaGUI personalia = new PersonaliaGUI("Vision 0.1");
				personalia.setVisible(true);
			}
		});
		add(Sales);
		add(Delivery);
		add(Employees);
		add(CEO);
		add(Admin);
		add(Logout);
				/*0: CEO, 1: Salesperson, 2: Chef,
	 *         3: Driver*/
		if (rolle == 3) {
			Sales.setEnabled(false);
			Admin.setEnabled(false);
			CEO.setEnabled(false);
			Delivery.setEnabled(true);
		}
		else if (rolle >= 1) {
			Sales.setEnabled(true);
			Admin.setEnabled(false);
			CEO.setEnabled(false);
			Delivery.setEnabled(true);
		}
		else if (rolle == 0) {
			Sales.setEnabled(true);
			Admin.setEnabled(true);
			CEO.setEnabled(true);
			Delivery.setEnabled(true);
		}
	}
}
