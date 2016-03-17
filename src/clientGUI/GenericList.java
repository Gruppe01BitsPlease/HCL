package clientGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

class genericList extends JPanel {
    //This is a generic list, shown in the middle of the tab where needed
    //Use the type int to choose which edit window appears whe double clicking
    String[][] table;
    String[] titles;
    JTable list;
    int type;
     genericList(String[][] table, String[] titles, int type, boolean searchable) {
        this.table = table;
        this.titles = titles;
        this.type = type;
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
                    }
                    else if (type == 2) {
                        orderWindow edit = new orderWindow(selected, index);
                    }
                }
            }
        });
        JScrollPane scroll = new JScrollPane(list);
        add(scroll, BorderLayout.CENTER);
        if (searchable) {
            add(new genericSearch(), BorderLayout.SOUTH);
        }
    }
    private class orderWindow extends tabbedMenu {
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
    private class employeeWindow extends tabbedMenu {
        public employeeWindow(String[] selected, int index) {
            setTitle("Edit employee");
            setLayout(new GridLayout(4, 2));
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            x = (int) (screen.width * 0.75);
            y = (int) (screen.height * 0.75);
            setSize((int) (x * 0.4), (int) (y * 0.2));
            setLocationRelativeTo(null);
            setAlwaysOnTop(true);
            setResizable(false);
            JLabel name = new JLabel("Name");
            JLabel email = new JLabel("E-mail");
            JLabel id = new JLabel ("Employee ID");
            JTextField nameField = new JTextField(selected[0]);
            JTextField mailField = new JTextField(selected[1]);
            JTextField idField = new JTextField(selected[2]);
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
                    selected[0] = nameField.getText();
                    selected[1] = mailField.getText();
                    selected[2] = idField.getText();
                    table[index] = selected;
                    list.repaint();
                    dispose();
                }
            });
            add(name);
            add(nameField);
            add(email);
            add(mailField);
            add(id);
            add(idField);
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
                    searchWindow window = new searchWindow();
                }
            };
            search.addActionListener(searchPress);
            searcher.addActionListener(searchPress);
            add(search, BorderLayout.CENTER);
            add(searcher, BorderLayout.EAST);
        }
        private class searchWindow extends tabbedMenu {
            public searchWindow() {
                Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                x = (int) (screen.width * 0.75);
                y = (int) (screen.height * 0.75);
                setSize((int) (x * 0.4), (int) (y * 0.4));
                setTitle("Search results");
                setAlwaysOnTop(true);
                genericList searchTab = new genericList(searchTable, titles, type, false);
                add(searchTab, BorderLayout.CENTER);
                setLocationRelativeTo(null);
                setVisible(true);
            }
        }
    }
}
