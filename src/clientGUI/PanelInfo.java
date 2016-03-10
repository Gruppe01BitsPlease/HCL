package clientGUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class PanelInfo extends JPanel {
	public PanelInfo() {
		setLayout(new GridLayout(0,2));
        JLabel lastName=new JLabel("Last Name");
        add(lastName);
        JTextField lastNameE=new JTextField("Martian",20);
        add(lastNameE);
        JLabel firstName=new JLabel("First Name");
        add(firstName);
        JTextField firstNameE=new JTextField("Marvin",20);
        add(firstNameE);
        JLabel phone=new JLabel("Phone");
        add(phone);
        JTextField phoneE=new JTextField("805-123-4567",20);
        add(phoneE);
        JLabel email=new JLabel("Email");
        add(email);
        JTextField emailE=new JTextField("marvin@wb.com",20);
        add(emailE);
        JLabel address1=new JLabel("Address1");
        add(address1);
        JTextField address1E=new JTextField("1001001010110 Martian Way ",30);
        add(address1E);
        JLabel address2=new JLabel("Addres2");
        add(address2);
        JTextField address2E=new JTextField("Suite 101101011 ",30);
        add(address2E);
        JLabel city=new JLabel("City");
        add(city);
        JTextField cityE=new JTextField("Ventura ",20);
        add(cityE);
        JLabel state=new JLabel("State");
        add(state);
        JTextField stateE=new JTextField("CA ",20);
        add(stateE);
        JLabel county=new JLabel("County");
        add(county);
        JTextField countyE=new JTextField("USA",20);
        add(countyE);
        JLabel postal=new JLabel("Postal Code");
        add(postal);
        JTextField postalE=new JTextField("93001 ",20);
        add(postalE);
	
	}
	
	//TODO SETMETODER, GETMETODER
}
