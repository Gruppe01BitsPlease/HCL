package clientGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class MainMenuGUI extends JFrame {
	public MainMenuGUI (String title){
		//window parameters
		setTitle(title);
		setLayout(new GridLayout(5, 1));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );
		setSize(300, 190);
		setLocationRelativeTo(null);
		setResizable(false);
		
		JButton Sales = new JButton("Sales");
		add(Sales);
		Sales.setEnabled(false);
		JButton Delivery = new JButton("Delivery");
		add(Delivery);
		JButton Employees = new JButton("Employees");
		Employees.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent pressed) {
				System.out.println("PLACEHOLDER");
				PersonaliaGUI personalia = new PersonaliaGUI("Vision 0.1");
				personalia.setVisible(true);
			}
		});
		add(Employees);
		JButton CEO = new JButton("CEO");
		add(CEO);
		CEO.setEnabled(false);
		JButton Admin = new JButton("Admin");
		add(Admin);
		Admin.setEnabled(false);
		
		
		
	}
}
