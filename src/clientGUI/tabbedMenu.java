package clientGUI;

import backend.Logon;
import backend.SQL;
import backend.UserManager;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Jens on 14.03.2016.
 */
public class tabbedMenu extends JFrame {
    UserManager user;
    public tabbedMenu (UserManager user, int rolle){
        this.user = user;
        setTitle("Bits Please HCL System 0.1");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );
        setSize(1600, 900);
        setLocationRelativeTo(null);
        setResizable(true);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Employees", new employeeTab());
        tabs.addTab("CEO functions", new CEOtab());
        add(tabs, BorderLayout.NORTH);
        this.setVisible(true);
    }

    //This constructor is probably just for testing,
    // gonna need an SQL object sent to the menu to connect to the database (probably easiest)
    public tabbedMenu (int rolle){
        setTitle("Bits Please HCL System 0.1");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );
        setSize(1600, 900);
        setLocationRelativeTo(null);
        setResizable(true);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Employees", new employeeTab());
        tabs.addTab("CEO functions", new CEOtab());
        add(tabs, BorderLayout.NORTH);
        this.setVisible(true);
    }

    //Tabs for the menu, to add one just add it to "tabs" above
    private class employeeTab extends JPanel {
        String[][] emp = {{ "Bob", "John", "Dave" }, { "0", "1", "2" }}; //TESTING
        public employeeTab() {
            setLayout(new GridLayout(1, 2));
 //           String[][] emp = sql.getStringTable("SELECT * FROM HCL_users");
            String[] emp2 = emp[1];
            JList list1 = new JList(emp2);
            JButton test = new JButton("Emplyee Testing");
            JButton test2 = new JButton("Testing more");
            JPanel tab1 = new list1();
            JPanel tab2 = new list2();
            add(tab1);
            add(tab2);
          //  add(test);
         //   add(test2);
        }
        //The [][] must be split into []'s, unfortunately
        private class list1 extends JPanel {
            public list1() {
                setLayout(new BorderLayout());
                String[] tab = emp[0];
                JList list = new JList(tab);
                JLabel listlabel = new JLabel("Employees");
                add(listlabel, BorderLayout.NORTH);
                add(list, BorderLayout.SOUTH);
            }
        }
        private class list2 extends JPanel {
            public list2() {
                setLayout(new BorderLayout());
                String[] tab = emp[1];
                JList list = new JList(tab);
                JLabel listlabel = new JLabel("ID");
                add(listlabel, BorderLayout.NORTH);
                add(list, BorderLayout.SOUTH);
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