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

@SuppressWarnings("serial")
public class LogOnPanel{

    private JPanel logOnPanel;

    public LogOnPanel () {
        buildPanel();




    }
    public void showGUI(Container parent){
        parent.add(logOnPanel);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        double x = (double) screen.width * 0.25;
        double y = (double) screen.height * 0.3;
        Dimension dynamic =  new Dimension((int)x, (int)y);
        logOnPanel.setPreferredSize(dynamic);
        parent.setPreferredSize(dynamic);
        parent.setVisible(true);
    }



    public void buildPanel(){
        //window parameters
        logOnPanel = new JPanel();
        logOnPanel.setLayout(new GridLayout(5, 1));
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        double x = (double) screen.width * 0.25;
        double y = (double) screen.height * 0.3;
        Dimension dynamic =  new Dimension((int)x, (int)y);
        logOnPanel.setPreferredSize(dynamic);

        //Create label, textfield and center their text.
        JLabel jUser = new JLabel("Username: ", SwingConstants.LEFT);
        final JTextField user = new JTextField();
        user.setHorizontalAlignment(JTextField.LEFT);
        JLabel jPassword = new JLabel("Password: ", SwingConstants.LEFT);
        final JTextField password = new JPasswordField();
        password.setHorizontalAlignment(JTextField.LEFT);

        //add label and textfield
        logOnPanel.add(jUser);
        logOnPanel.add(user);
        logOnPanel.add(jPassword);
        logOnPanel.add(password);

        //buttons and their action for buttonpanel
        JButton LogOn = new JButton("LogOn");
        JButton exit = new JButton("Exit");
        exit.addActionListener((pressed) -> System.exit(0));
        LogOn.addActionListener(enterpass -> System.out.print(true));


        //adding buttons to panel and panel to frame
        JPanel buttonrow = new JPanel();
        buttonrow.setLayout(new FlowLayout());
        buttonrow.add(LogOn);
        buttonrow.add(exit);

        logOnPanel.add(buttonrow);
    }


    public JPanel getPanel(){
        return logOnPanel;
    }

    public static void main(String[] args) throws Exception {
        JFrame mainWindow = new JFrame();
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        LogOnPanel test = new LogOnPanel();
        test.showGUI(mainWindow);

    }
}


