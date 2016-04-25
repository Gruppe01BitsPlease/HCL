package backend;

import clientGUI.DatabaseSetupGUI;
import clientGUI.SettingsGUI;
import clientGUI.LogOnGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;

/**
 * Class for the startup logic. Checks if settings file is valid and connection to db is successful.
 */
public class StartUp
{
    private SettingsFile settings;
    private SettingsGUI window;
    SQL sql = new SQL();


    private StartUp(){ init();}

    /**
     * Startup Logic, checks everything is setup correctly, then calls the LoginGUI
     */
    private void init() {
        if (settings == null) {
            try {
                this.settings = new SettingsFile();
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null,
                        "Settingsfile could not be located nor recovered, please contact your system administrator.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            }
        }

        if (validateSettings() != 1 || !validateDBConnection(sql)) {
            if(isFirstTime()){
                JOptionPane.showMessageDialog(null,
                        "This looks like the first time you are running this application, please configure your database settings",
                        "Welcome",
                        JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null,
                        "Unable to reach server, please check server settings and your internet connection",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            window = new SettingsGUI();
            window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            window.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent windowEvent){
                    if(window.isValid) {
                        int reply = JOptionPane.showConfirmDialog(null,
                                "Do you want to run the database setup?",
                                "Please make a selection",
                                JOptionPane.YES_NO_OPTION);
                        if (reply == JOptionPane.NO_OPTION) {
                            window.dispose();
                            new LogOnGUI(sql);
                        }
                        if (reply == JOptionPane.YES_OPTION) {
                            window.dispose();
                            new LogOnGUI(sql);
                            DatabaseSetupGUI DBsetting = new DatabaseSetupGUI(sql);
                            DBsetting.setLocationRelativeTo(null);
                            DBsetting.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                        }
                    }else{
                        int reply = JOptionPane.showConfirmDialog(null,
                                    "Settings are not valid, program will exit!",
                                    "Settings not saved!",
                                    JOptionPane.OK_CANCEL_OPTION);
                        if(reply == JOptionPane.OK_OPTION){
                            System.exit(0);
                        }
                    }
                }
            });
        }else{
            new LogOnGUI(sql);
        }
    }



    /**
     * Returns true if this is the first startup of the program
     * @return boolean
     */
    private boolean isFirstTime(){
        String firsttime = settings.getPropValue("firsttime");
        return firsttime == null || firsttime.trim().equals("1") || firsttime.trim().equals("");
    }

    /**
     * Returns true the database connection is successful
     * @return boolean
     */
    private boolean validateDBConnection(SQL sql) {
        System.out.println(sql.isConnected());
        return sql.isConnected(); // True if the database, username, password, and JDBC drivers are all correct, and the servers are online
    }

    /**
     *  Checks the settingsfile for errors
     *  -1 if settings file is not found and default can't be recovered. <br>
     *   0 if any setting in the validateTable is null or empty. <br>
     *   1 everything seems fine. <br>
     *       @return int -1, 0 or 1
     */
    private int validateSettings() {
        //add settings to be validated here
        String[] validateTable = {
                settings.getPropValue("host"),
                settings.getPropValue("database"),
                settings.getPropValue("user"),
                settings.getPropValue("password")
        };
        for (String s: validateTable) {
            if (s == null || s.trim().equals("")){
                return 0;
            }
        }
        return 1;

    }

    /**
     * Tries to set our "seeglass" look and feel for windows. Sets default if it fails.
     * Skips this on mac
     * Then launches the startupclass
     */
    public static void main(String[] args) {
        if(!System.getProperty("os.name").equalsIgnoreCase("mac os x")){
            try {
                UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
            } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            }
        }
        EventQueue.invokeLater(StartUp::new);
    }
}


