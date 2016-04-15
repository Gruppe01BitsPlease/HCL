package clientGUI;

import backend.SQL;
import backend.SettingsFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;

/**
 * Created by bahafeld on 03.04.2016.....
 * For HCL
 */
//TODO Edit filereference before deployement
public class SettingsGUI extends JFrame {
    private SettingsFile settings;
    private final JTextField host = new JTextField();
    private final JTextField database = new JTextField();
    private final JTextField user = new JTextField();
    private final JTextField password = new JPasswordField();
    private final JTextPane helpText = new JTextPane();
    public boolean isValid = false;


    public SettingsGUI(){

        try {
            settings = new SettingsFile();
        } catch (FileNotFoundException e) {
            JOptionPane.showConfirmDialog(null, "Settings file is corrupted or missing, and the default file can't be recovered");
            e.printStackTrace();
            System.exit(-1);
        }

        //window parameters

            setTitle("Setup");
            setLayout(new GridLayout(10, 1));
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            double x = (double) screen.width * 0.25;
            double y = (double) screen.height * 0.6;
            setSize((int) x, (int) y);
            setLocationRelativeTo(null);
            setResizable(false);
            //this.setModalityType(ModalityType.APPLICATION_MODAL);

            //Create label, textfield and center their text.


            JLabel jHost = new JLabel("Host: ", SwingConstants.LEFT);

            host.setToolTipText("The mySQL URL and port, e.g: mysql.server.com:1234");
            host.setHorizontalAlignment(JTextField.LEFT);

            JLabel jDatabase = new JLabel("Database: ", SwingConstants.LEFT);

            database.setToolTipText("Name of the database to use");
            database.setHorizontalAlignment(JTextField.LEFT);

            JLabel jUser = new JLabel("Username: ", SwingConstants.LEFT);

            user.setToolTipText("Username for server");
            user.setHorizontalAlignment(JTextField.LEFT);

            JLabel jPassword = new JLabel("Password: ", SwingConstants.LEFT);

            password.setToolTipText("Password for user");
            password.setHorizontalAlignment(JTextField.LEFT);

            //add label and textfield

        helpText.setText("Please enter your server settings. " +
                "If the connection is successful, settings will be saved." +
                " Hover over each field for more help! ");
        helpText.setEnabled(false);
        helpText.setAlignmentX(100);


        helpText.setBackground(this.getBackground());
        helpText.setFont(new Font("Courier New", Font.ITALIC, 12));
        helpText.setDisabledTextColor(Color.black);

        add(helpText);
            add(jHost);
            add(host);
            add(jDatabase);
            add(database);
            add(jUser);
            add(user);
            add(jPassword);
            add(password);

        getSettings();







        //buttons and their action
        //JButton exitButton = new JButton("Exit");
        JButton saveButton = new JButton("Save");

        saveButton.addActionListener((pressed) -> validateInput());

        //exitButton.addActionListener((pressed) -> dispose());


        //adding buttons to panel and panel to frame
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        //buttonPanel.add(exitButton);
        buttonPanel.add(saveButton);
            add(buttonPanel);

            this.setVisible(true);
        }


    private String addJDBCFormat(String url){
        return "jdbc:mysql://"+ url + "/";
    }

    private String remJDBCFormat(String urlJDBC){
        if(urlJDBC == null || urlJDBC.length() < 1) return "";
        String url = urlJDBC.substring(13);
        url =  url.substring(0, url.length()-1);
        return url;
    }



    private boolean validateInput() {
        String[] validateTable = {
                host.getText(),
                database.getText(),
                user.getText(),
                password.getText()
        };
        for (String s: validateTable) {
            if (s == null || s.trim().equals("")){
                helpText.setDisabledTextColor(Color.red);
                helpText.setText("Not all the fields are filled out correctly");
                return false;
            }
        }
        validateTable[0] = addJDBCFormat(validateTable[0]);
        SQL dbTest = new SQL(validateTable[0], validateTable[1], validateTable[2], validateTable[3]);
        if(!dbTest.isConnected()){
            helpText.setDisabledTextColor(Color.red);
            helpText.setText("Could not connect to SQL server, ensure the settings are correct and that you have a internet connection");
            return false;
        }
        helpText.setDisabledTextColor(new Color(0,102,0));
        helpText.setText("Settings saved successfully, please close this window in the upper right corner.");
        isValid = true;
        setSettings(addJDBCFormat(host.getText()), database.getText(), user.getText(), password.getText());
        return true;
    }


    private void setSettings(String host, String database, String user, String password){
        settings.setPropValue("host", host);
        settings.setPropValue("database", database);
        settings.setPropValue("user", user);
        settings.setPropValue("password", password);
        settings.setPropValue("firsttime", "0");
    }

    private void getSettings(){
        host.setText(remJDBCFormat(settings.getPropValue("host")));
        database.setText(settings.getPropValue("database"));
        user.setText(settings.getPropValue("user"));
        password.setText(settings.getPropValue("password"));



    }

    public static void main(String[] args){
        new SettingsGUI();
    }
}


