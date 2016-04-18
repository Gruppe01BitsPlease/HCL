package clientGUI;

import backend.*;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import backend.SQL;
import backend.SettingsFile;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.sql.SQLException;

/**
 * Created by bahafeld on 15.04.2016...
 * For HCL
 */

//TODO Edit filereference before deployement
    public class DatabaseResetGUI extends JDialog{
    private JCheckBox checkTables;
    private JCheckBox checkViews;
    private JCheckBox checkData;
    private JCheckBox checkOverWriteTables;
    private DatabaseReset reset;
    private JTextPane helpText;


    public DatabaseResetGUI(){
        reset = new DatabaseReset();
        setVisible(true);

            //window parameters
            setLayout(new GridLayout(6, 1));
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            double x = (double) screen.width * 0.25;
            double y = (double) screen.height * 0.6;
            setSize((int) x, (int) y);

            //Create label, textfield and center their text.
            helpText = new JTextPane();
            helpText.setText("WARNING: THESE SETTINGS MAY GIVE DATALOSS IF NOT USED CORRECTLY \n" +
                    "If unsure, leave the default settings");
            helpText.setEnabled(false);
            helpText.setAlignmentX(100);
            helpText.setDisabledTextColor(Color.red);
            helpText.setBackground(this.getBackground());
            helpText.setFont(new Font("Courier New", Font.ITALIC, 15));
            add(helpText);

            //Checkboxes
            checkTables = new JCheckBox("Create tables");
            checkTables.setSelected(true);
            checkOverWriteTables = new JCheckBox("Overwrite tables");
            checkOverWriteTables.setSelected(false);
            checkViews = new JCheckBox("Create views");
            checkViews.setSelected(true);
            checkData = new JCheckBox("Create testdata");
            add(checkTables);
            add(checkViews);
            add(checkData);
            add(checkOverWriteTables);

            JButton buttonExecute = new JButton("Execute");
            buttonExecute.addActionListener((pressed) -> executeCheckedScripts());
            add(buttonExecute);

            //exitButton.addActionListener((pressed) -> dispose());



        }

    private void executeCheckedScripts(){
        SQL sql = new SQL();
        try {
            sql.connection.setAutoCommit(false);
            if(checkTables.isSelected()) {
                String path;
                if(checkOverWriteTables.isSelected()){
                    path = "sqlscripts/tables.sql";
                    reset.resetDatabaseWithScript(path);
                }else{
                    path = "sqlscripts/tables_nooverwrite.sql";
                    reset.resetDatabaseWithScript(path);
                }
            }
            if(checkViews.isSelected()) {
                String path = "sqlscripts/views.sql";
                reset.resetDatabaseWithScript(path);
            }
            if(checkData.isSelected()) {
                String path = "sqlscripts/randomdata.sql";
                reset.resetDatabaseWithScript(path);
            }
            sql.connection.commit();
            helpText.setDisabledTextColor(new Color(0,102,0));
            helpText.setText("Database operation applied successfully, please close this window in the upper right corner.");

        } catch (SQLException e) {
            try {
                JOptionPane.showMessageDialog(null, e.toString());
                sql.connection.rollback();
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(null, "Could not rollback, fatal error. System shutdown!");
                e.printStackTrace();
                System.exit(-2);
            }
        }finally {
            try {
                sql.connection.setAutoCommit(true);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Could not set AutoCommit back, do this manually");
            }
        }
    }

    public static void main(String[] args){
            DatabaseResetGUI frame = new DatabaseResetGUI();
            frame.setVisible(true);
        }
}
