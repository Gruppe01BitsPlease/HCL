package clientGUI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Jens on 14.03.2016.
 */
public class tabbedMenu extends JFrame {
    public tabbedMenu (int rolle){
        setTitle("Main menu");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );
        setSize(1600, 900);
        setLocationRelativeTo(null);
        setResizable(true);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Employees", new employeeTab());
        tabs.addTab("CEO functions", new CEOtab());
        add(tabs, BorderLayout.NORTH);
    }
    private class employeeTab extends JPanel {
        public employeeTab() {
            setLayout(new GridLayout(2, 2));
            JButton test = new JButton("Emplyee Testing");
            JButton test2 = new JButton("Testing more");
            add(test);
            add(test2);
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
    public static void main(String[] args) {
        tabbedMenu menu = new tabbedMenu(1);
        menu.setVisible(true);
    }
}
