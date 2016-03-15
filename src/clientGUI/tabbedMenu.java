package clientGUI;

import backend.Logon;
import backend.SQL;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Jens on 14.03.2016.
 */
public class tabbedMenu extends JFrame {
    SQL user;
	double x;
	double y;
    public tabbedMenu (int rolle){
		user = new SQL(new Logon());
        setTitle("Bits Please HCL System 0.1");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );
		//Dynamic size based on screen resolution bitches
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		x = (double) screen.width * 0.75;
		y = (double) screen.height * 0.75;
        setSize((int) x, (int) y);
        setLocationRelativeTo(null);
        setResizable(true);
        JTabbedPane tabs = new JTabbedPane();
		JMenuBar bar = new JMenuBar();
		JMenu file = new JMenu("File");
		bar.add(file);
        tabs.addTab("Employees", new employeeTab());
        tabs.addTab("CEO functions", new CEOtab());
        add(tabs, BorderLayout.CENTER);
		add(bar, BorderLayout.NORTH);
        this.setVisible(true);
    }
	private class menubar{}
    //Tabs for the menu, to add one just add it to "tabs" above
    private class employeeTab extends JPanel {
        String[][] emp = {{ "Bob", "0" }, { "John", "1" }, { "Dave", "3" }}; //TESTING
		String[] clm = { "Employees", "ID" };
    //    String[][] emp = user.getStringTable("SELECT * FROM HCL_users");
        public employeeTab() {
            setLayout(new BorderLayout());
			//add(new top(), BorderLayout.NORTH);
            add(new center(), BorderLayout.NORTH);
			add(new bottom(), BorderLayout.SOUTH);
        }
        private class center extends JPanel {
            public center() {
                setLayout(new BorderLayout());
				JTable list = new JTable(emp, clm);
				JScrollPane scroll = new JScrollPane(list);
                add(scroll, BorderLayout.SOUTH);
            }
        }
		private class top extends JPanel {
			public top() {
				setLayout(new BorderLayout());
				JLabel label1 = new JLabel("Employee");
				JLabel label2 = new JLabel("ID");
				add(label1, BorderLayout.WEST);
				add(label2, BorderLayout.EAST);
			}
		}
		private class bottom extends JPanel {
			public bottom() {
				setLayout(new BorderLayout());
				JTextField search = new JTextField();
				JButton searcher = new JButton("Search");
				add(search, BorderLayout.CENTER);
				add(searcher, BorderLayout.EAST);
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

}
class test {
    public static void main(String[] args) throws Exception {
        tabbedMenu menu = new tabbedMenu(1);
    }
}