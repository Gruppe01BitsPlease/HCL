package clientGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import backend.*;

class genericList extends JPanel {
    //This is a generic list, shown in the middle of the tab where needed
    //Use the type int to choose which edit window appears whe double clicking
    String[][] table;
    String[] titles;
    JTable list;
    int type;
    SQL sql;
    int x;
    int y;

    public genericList(String[][] table, String[] titles, int type) {
        try {
            this.sql = new SQL(new Logon(new File()));
        }
        catch (Exception e) {}
        this.table = table;
        this.titles = titles;
        this.type = type;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        x = (int) (screen.width * 0.75);
        y = (int) (screen.height * 0.75);
        setLayout(new BorderLayout());
        list = new JTable(table, titles) {
            private static final long serialVersionUID = 1L;

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String[] selected = table[list.getSelectedRow()];
                    int index = list.getSelectedRow();
                    if (type == 1) {
                        employeeWindow edit = new employeeWindow(selected, index);
                    } else if (type == 2) {
                        orderWindow edit = new orderWindow(selected, index);
                    }
                }
            }
        });
        JScrollPane scroll = new JScrollPane(list);
        add(scroll, BorderLayout.CENTER);
    }

    public genericList(String query, String[] titles, int type) {
        try {
            this.sql = new SQL(new Logon(new File()));
        }
        catch (Exception e) {
            System.out.println("SQL problem");
        }
        try {
            this.table = sql.getStringTable(query, false);
        }
        catch (Exception e) {
            System.out.println("SQL table problem");
        }
        this.titles = titles;
        this.type = type;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        x = (int) (screen.width * 0.75);
        y = (int) (screen.height * 0.75);
        setLayout(new BorderLayout());
        list = new JTable(table, titles) {
            private static final long serialVersionUID = 1L;

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String[] selected = table[list.getSelectedRow()];
                    int index = list.getSelectedRow();
                    if (type == 1) {
                        employeeWindow edit = new employeeWindow(selected, index);
                    } else if (type == 2) {
                        orderWindow edit = new orderWindow(selected, index);
                    }
                }
            }
        });
        JScrollPane scroll = new JScrollPane(list);
        add(scroll, BorderLayout.CENTER);
        add(new genericSearch(), BorderLayout.SOUTH);
    }

    private class orderWindow extends JFrame {
        //TODO add fields for all data
        public orderWindow(String[] selected, int index) {
            setTitle("Edit order");
            setLayout(new GridLayout(3, 2));
            setSize((int) (x * 0.4), (int) (y * 0.2));
            setLocationRelativeTo(null);
            setAlwaysOnTop(true);
            setResizable(false);
            JLabel customer = new JLabel("Customer");
            JLabel address = new JLabel("Address");
            JTextField customerField = new JTextField(selected[0]);
            JTextField addressField = new JTextField(selected[1]);
            JButton save = new JButton("Save");
            JButton cancel = new JButton("Cancel");
            cancel.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            save.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selected[0] = customerField.getText();
                    selected[1] = addressField.getText();
                    table[index] = selected;
                    list.repaint();
                    dispose();
                }
            });
            add(customer);
            add(customerField);
            add(address);
            add(addressField);
            add(save);
            add(cancel);
            setVisible(true);
        }
    }

    private class employeeWindow extends JFrame {
        //TODO add fields for all data
        public employeeWindow(String[] selected, int index) {
            setTitle("Edit employee nr: " + (selected[0]));
            setLayout(new GridLayout(4, 2));
            setSize((int) (x * 0.4), (int) (y * 0.2));
            setLocationRelativeTo(null);
            setAlwaysOnTop(true);
            setResizable(false);
            JLabel name = new JLabel("Name");
            JLabel email = new JLabel("E-mail");
            JLabel address = new JLabel("Address");
            JTextField nameField = new JTextField(selected[0]);
            JTextField mailField = new JTextField(selected[1]);
            JTextField addressField = new JTextField(selected[2]);
            JButton save = new JButton("Save");
            JButton cancel = new JButton("Cancel");
            cancel.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            save.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String nameText = nameField.getText();
                    String mailText = mailField.getText();
                    String addressText = addressField.getText();
                    boolean updated = false;
                    if (!(nameText.equals(selected[0]))) {
                        if (sql.update("HCL_users", "user_name", "user_ID", selected[3], nameText)) {
                            selected[0] = nameText;
                            updated = true;
                        }
                    }
                    if (!(mailText.equals(selected[1]))) {
                        if (sql.update("HCL_users", "user_email", "user_ID", selected[3], mailText)) {
                            selected[1] = mailText;
                            updated = true;
                        }
                    }
                    if (!(addressText.equals(selected[2]))) {
                        if (sql.update("HCL_users", "user_adress", "user_ID", selected[3], addressText)) {
                            selected[2] = addressText;
                            updated = true;
                        }
                    }
                    //"SELECT user_name, user_email, user_adress, user_ID FROM HCL_users"
                    if (updated) {
                        table[index] = selected;
                        list.repaint();
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Update failed!");
                    }
                }
            });
            add(name);
            add(nameField);
            add(email);
            add(mailField);
            add(address);
            add(addressField);
            add(save);
            add(cancel);
            setVisible(true);
        }
    }

    private class genericSearch extends JPanel {
        //This is a generic search tab with button, which will show results in a popup window
        String[][] searchTable;

        public genericSearch() {
            setLayout(new BorderLayout());
            JTextField search = new JTextField();
            JButton searcher = new JButton("Search");
            searcher.setToolTipText("Search for any entry and display all matches in seperate window.");
            Action searchPress = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ArrayList<Integer> rowAdded = new ArrayList<Integer>();
                    searchTable = new String[table.length][table[0].length];
                    for (int i = 0; i < table.length; i++) {
                        for (int j = 0; j < table[i].length; j++) {
                            if (!(rowAdded.contains(i)) && table[i][j].toLowerCase().contains(search.getText().toLowerCase())) {
                                int k = 0;
                                boolean added = false;
                                for (int l = 0; l < searchTable.length; l++) {
                                    while (!added && k < searchTable[0].length) {
                                        if (searchTable[k][0] == null || searchTable[k][0].isEmpty()) {
                                            searchTable[k] = table[i];
                                            added = true;
                                            rowAdded.add(i);
                                        } else {
                                            k++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    try {
                        searchWindow window = new searchWindow();
                    }
                    catch (Exception l) {}
                }
            };
            search.addActionListener(searchPress);
            searcher.addActionListener(searchPress);
            add(search, BorderLayout.CENTER);
            add(searcher, BorderLayout.EAST);
        }

        private class searchWindow extends JFrame {
            public searchWindow() throws Exception {
                setSize((int) (x * 0.4), (int) (y * 0.4));
                setTitle("Search results");
                setAlwaysOnTop(true);
                genericList searchTab = new genericList(searchTable, titles, type);
                add(searchTab, BorderLayout.CENTER);
                setLocationRelativeTo(null);
                setVisible(true);
            }
        }
    }
}