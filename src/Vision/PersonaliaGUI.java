package Vision;


import javax.swing.*;
import java.awt.*;

public class PersonaliaGUI extends JFrame {


	public PersonaliaGUI(String tittel) {
        setTitle (tittel);
 		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 		setLayout(new BorderLayout(10, 10));
 		
 		PanelList west = new PanelList();
 		add(west, BorderLayout.WEST);
 		
 		PanelInfo center = new PanelInfo();
		add(center, BorderLayout.CENTER);
	 	
		PanelButtons sør = new PanelButtons();
 		add(sør, BorderLayout.SOUTH);
 		
 		pack();
	}
	
	
	


public class PanelList extends JPanel {
	private DefaultListModel<String> listeinnhold = new DefaultListModel<String>();
	private JList<String> navnliste = new JList<String>(listeinnhold);
	private String[] testliste = {"Ole Olsen", "Kari Traa", "Steinar Ege"};

	public PanelList() {
		for (int i = 0; i < testliste.length; i++) {
			listeinnhold.addElement(testliste[i]);
		}
		setLayout(new GridLayout(1,1));
		navnliste.setSelectionMode(0);
		navnliste.setBorder(BorderFactory.createMatteBorder(6, 6, 6, 6, Color.WHITE));
		JScrollPane rullenavnliste = new JScrollPane(navnliste);
		
		add(rullenavnliste);
	}
	
	//TODO SETMETODER, GETMETODER
}
public class PanelInfo extends JPanel {
	public PanelInfo() {
		setLayout(new GridLayout(0,2));
        JLabel lastName=new JLabel("Last Name  ", SwingConstants.RIGHT);
        add(lastName);
        JTextField lastNameE=new JTextField("Olsen" ,12);
        add(lastNameE);
        JLabel firstName=new JLabel("First Name  ", SwingConstants.RIGHT);
        add(firstName);
        JTextField firstNameE=new JTextField("Ole", 12);
        add(firstNameE);
        JLabel phone=new JLabel("Phone  ", SwingConstants.RIGHT);
        add(phone);
        JTextField phoneE=new JTextField("41411476", 12);
        add(phoneE);
        JLabel email=new JLabel("Email  ", SwingConstants.RIGHT);
        add(email);
        JTextField emailE=new JTextField("oleolsen@vision.com", 12);
        add(emailE);
        JLabel address1=new JLabel("Address1  ", SwingConstants.RIGHT);
        add(address1);
        JTextField address1E=new JTextField("Karl Johan 3", 12);
        add(address1E);
        JLabel address2=new JLabel("Addres2  ", SwingConstants.RIGHT);
        add(address2);
        JTextField address2E=new JTextField("Apartment 3B", 12);
        add(address2E);
        JLabel city=new JLabel("City  ", SwingConstants.RIGHT);
        add(city);
        JTextField cityE=new JTextField("Oslo ", 12);
        add(cityE);
        JLabel state=new JLabel("State  ", SwingConstants.RIGHT);
        add(state);
        JTextField stateE=new JTextField("Oslo ", 12);
        add(stateE);
        JLabel county=new JLabel("County  ", SwingConstants.RIGHT);
        add(county);
        JTextField countyE=new JTextField("Norway", 12);
        add(countyE);
        JLabel postal=new JLabel("Postal Code  ", SwingConstants.RIGHT);
        add(postal);
        JTextField postalE=new JTextField("0010 ", 12);
        add(postalE);
	
	}
	
	//TODO SETMETODER, GETMETODER
}
public class PanelButtons extends JPanel {
	public PanelButtons() {
		setLayout(new FlowLayout());
		JButton b1=new JButton("New");
		add(b1);
        JButton b2=new JButton("Delete");
        add(b2);
        JButton b3=new JButton("Edit");
        add(b3);
        JButton b4=new JButton("Save");
        add(b4);
        JButton b5=new JButton("Cancel");
        add(b5);
	}
}
}