package backend;

import clientGUI.SettingsGUI;
import clientGUI.LogOnGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;


/**
 * Created by bahafeld on 02.04.2016...
 */

//initialize and configure Settings
    // main
    // rebuild database?
    // check settingfile for error
    // check dbconnection

public class StartUp {
    LogOnGUI logon;
    SettingsFile settings;
    SettingsGUI window;
    SQL sql;

    public StartUp(){
    init();
    }

    /**
     *
     * @return boolean
     */

    public void init() {
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

        if (validateSettings() != 1 || validateDBConnection()) {
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
                    if(window.isValid){
                        window.dispose();
                        new LogOnGUI();
                    }else{
                        int reply = JOptionPane.showConfirmDialog(null,
                                    "Settings are still not verified, program will exit!",
                                    "Settings not saved!",
                                    JOptionPane.OK_CANCEL_OPTION);
                        if(reply == JOptionPane.OK_OPTION){
                            System.exit(0);
                        }
                    }
                }
            });
        }
    }




    public boolean isFirstTime(){
        String firsttime = settings.getPropValue("firsttime");
        return firsttime == null || firsttime.trim().equals("1") || firsttime.trim().equals("");
    }

    /**
     * Returns true if the stars align
     */
    public boolean validateDBConnection() {
        sql = new SQL();
        System.out.println(sql.isConnected());
        return sql.isConnected(); // True if the database, username, password, and JDBC drivers are all correct, and the servers are online
    }

    /**
     *
     *  -1 if settings file is not found and default can't be recovered. <br>
     *   0 if any setting in the validateTable is null or empty. <br>
     *   1 everything seems fine. <br>
     *       @return int -1, 0 or 1
     */
    public int validateSettings() {
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

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new StartUp();
            }
        });
    }

}


