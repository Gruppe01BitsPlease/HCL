package clientGUI;

import backend.*;

import javax.swing.*;
import java.awt.*;

import backend.SQL;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;

/**
 * Created by bahafeld on 15.04.2016...
 * For HCL
 */

//TODO Edit filereference before deployement
    public class DatabaseResetGUI extends JDialog{
    private JCheckBox checkTables;
    private JCheckBox checkRandomData;
    private JCheckBox checkOverWriteTables;
    private SQLScriptReader reset;
    private JTextPane helpText;


    public DatabaseResetGUI(){
        reset = new SQLScriptReader();
        setVisible(true);

            //window parameters
            setTitle("Database Setup");
            setLayout(new GridLayout(5, 1));
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            double x = (double) screen.width * 0.25;
            double y = (double) screen.height * 0.6;
            setSize((int) x, (int) y);
            setResizable(false);
            setModalityType(ModalityType.APPLICATION_MODAL);
            setAlwaysOnTop(true);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            //Create label, textfield and center their text.
            helpText = new JTextPane();
            helpText.setText("WARNING: THESE SETTINGS MAY GIVE DATALOSS IF NOT USED CORRECTLY");
            helpText.setEnabled(false);
            helpText.setAlignmentX(100);
            helpText.setDisabledTextColor(Color.red);
            helpText.setBackground(this.getBackground());
            helpText.setFont(new Font("Courier New", Font.ITALIC, 15));
            add(helpText);

            //Checkboxes
            checkTables = new JCheckBox("Create/fix DB structure (data safe)");
            checkTables.setToolTipText("Creates missing tables/views required by this software");
            checkTables.setSelected(true);

            checkOverWriteTables = new JCheckBox("Drop all data and create DB structure");
            checkOverWriteTables.setToolTipText("Drops any existing tables/views, then creates DB structure");
            checkOverWriteTables.setSelected(false);

            checkRandomData = new JCheckBox("Fill DB with random data");
            checkRandomData.setToolTipText("Adds random entries, for testing purposes");
            checkRandomData.setSelected(false);

            add(checkTables);
            add(checkOverWriteTables);
            add(checkRandomData);

            //buttonpanel
            JButton executeButton = new JButton("Execute");
            executeButton.addActionListener((pressed) -> executeCheckedScripts());
            executeButton.setToolTipText("Execute selected SQL Scripts");
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener((pressed) -> dispose());
            closeButton.setToolTipText("Close this window");

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());
            buttonPanel.add(closeButton);
            buttonPanel.add(executeButton);

            add(buttonPanel);
        }

    private void executeCheckedScripts(){
        SQL sql = new SQL();
        try {
            sql.connection.setAutoCommit(false);
            if (checkTables.isSelected()) {
                String path;
                if (checkOverWriteTables.isSelected()) {
                    path = "sqlscripts/tables.sql";
                    reset.resetDatabaseWithScript(path);
                } else {
                    path = "sqlscripts/tables_nooverwrite.sql";
                    reset.resetDatabaseWithScript(path);
                }
                path = "sqlscripts/views.sql";
                reset.resetDatabaseWithScript(path);
            }
            if (checkRandomData.isSelected()) {
                String path = "sqlscripts/randomdata.sql";
                reset.resetDatabaseWithScript(path);
            }
            sql.connection.commit();
            helpText.setDisabledTextColor(new Color(0, 102, 0));
            helpText.setText("Database operation successful.");
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (SQLException e) {
            try {
                sql.connection.rollback();
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(null, "Could not rollback, fatal error. System shutdown!");
                System.exit(-2);
            }
        }finally {
            try {
                sql.connection.setAutoCommit(true);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Could not set AutoCommit back, do this manually!");
            }
        }
    }

    public static void main(String[] args){
            DatabaseResetGUI frame = new DatabaseResetGUI();
            frame.setVisible(true);
        }
}
