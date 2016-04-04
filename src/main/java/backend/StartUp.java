package backend;

import clientGUI.SettingsGUI;
import clientGUI.LogOnGUI;

import javax.swing.*;
import java.awt.*;
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
    SettingsFile settings;
    SQL sql;

    public StartUp(){

       new LogOnGUI();


    }

    /**
     *
     * @return boolean
     */

    public void init(){
        try {
            this.settings = new SettingsFile();

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "Settingsfile could not be located nor recovered, please contact systemadmin.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        while(isFirstTime() || validateSettings()!=1 || !validateDBConnection()){
            if(!validateDBConnection()){
                JOptionPane.showMessageDialog(null,
                        "Unable to reach server, please check server settings",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            //todo make not shit as olav says.. trouble calling validateDBConnection LOTS OF CLEANUP

            new SettingsGUI();
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

    public void FirstTimeSetup(String host, String database, String user, String password){



    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new StartUp();
            }
        });
    }

}


