package clientGUI.NewGUI;

/**
 * Created by bahafeld on 04.04.2016...
 * For HCL
 */

import backend.SQL;
import backend.UserManager;
import clientGUI.tabbedMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;




import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import backend.*;
import com.seaglasslookandfeel.*;

@SuppressWarnings("serial")
public class LogOnPanel{

    public JPanel logOn;
    public LogOnPanel () {

        this.logOn = new JPanel();
        //window parameters
        logOn.setLayout(new GridLayout(5, 1));
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        double x = (double) screen.width * 0.25;
        double y = (double) screen.height * 0.3;
        logOn.setSize((int) x, (int) y);

        //Create label, textfield and center their text.
        JLabel jUser = new JLabel("Username: ", SwingConstants.LEFT);
        final JTextField user = new JTextField();
        user.setHorizontalAlignment(JTextField.LEFT);
        JLabel jPassword = new JLabel("Password: ", SwingConstants.LEFT);
        final JTextField password = new JPasswordField();
        password.setHorizontalAlignment(JTextField.LEFT);

        //add label and textfield
        logOn.add(jUser);
        logOn.add(user);
        logOn.add(jPassword);
        logOn.add(password);

        //buttons and their action for buttonpanel
        JButton LogOn = new JButton("LogOn");
        JButton exit = new JButton("Exit");
        exit.addActionListener((pressed) -> System.exit(0));

        Action enterpass = new AbstractAction() {
            public void actionPerformed(ActionEvent pressed) {
                //ENTERING ADMIN & ADMIN WILL GET ACCESS FOR TESTING!!!!! TODO: Remove that before release
                String navn = user.getText();
                String pass = password.getText();
                int i;
                try {
                    UserManager u = new UserManager(new SQL());
                    if (navn.equals("admin") && pass.equals("admin")) {
                        i = 0;
                    } else {
                        i = u.logon(navn, pass);
                    }
                    if (i >= 0) {
                        tabbedMenu main = new tabbedMenu(i, navn);
                    } else if (i == -1) {
                        JOptionPane.showMessageDialog(null, "The user name or password is incorrect.");
                    } else if (i == -2) {
                        JOptionPane.showMessageDialog(null, "Could not connect to the database.");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Could not locate the settings file.");
                }
            }
        };
        LogOn.addActionListener(enterpass);


        //adding buttons to panel and panel to frame
        JPanel buttonrow = new JPanel();
        buttonrow.setLayout(new FlowLayout());
        buttonrow.add(LogOn);
        buttonrow.add(exit);
        logOn.add(buttonrow);
    }

    public JPanel getPanel(){
        return logOn;
    }

    public static void main(String[] args) throws Exception {
        //	User u = new User();
        //	u.generateUser("jens", "1234", 0);

    }
}


