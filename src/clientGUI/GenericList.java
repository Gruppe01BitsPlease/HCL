package clientGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

import backend.*;
import com.sun.org.apache.xpath.internal.operations.Or;

class GenericList extends JPanel {
    //This is a generic list, shown in the middle of the tab where needed
    //Use the type int to choose which edit window appears whe double clicking
    private String query;
	private String SqlTableName;
    private String[][] table;
    private String[] titles;
	private String[] SqlColumnNames;
    private DefaultTableModel tabModel;
	private DefaultTableModel searchTableMod;
	private JTable list;
	private JTable searchTab;
    private SQL sql;
    private int x;
    private int y;
	public GenericList(String query, String[] titles, String SqlTableName) {
        try {
            this.sql = new SQL();
            this.table = sql.getStringTable(query, false);
			SqlColumnNames = sql.getStringTable(query, true)[0];
        }
        catch (Exception e) {
            System.out.println("ERROR");
        }
		this.SqlTableName = SqlTableName;
        this.titles = titles;
        this.query = query;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        x = (int) (screen.width * 0.75);
        y = (int) (screen.height * 0.75);
        setLayout(new BorderLayout());
        tabModel = new DefaultTableModel(table, titles);
        list = new JTable(tabModel) {
            private static final long serialVersionUID = 1L;

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        if (this instanceof OrderTab) {
            list.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        String[] selected = table[list.getSelectedRow()];
                        int index = list.getSelectedRow();
                        orderWindow edit = new orderWindow(selected, index);
                    }
                }
            });
        }
        else if (this instanceof EmployeeTab) {
            list.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        String[] selected = table[list.getSelectedRow()];
                        int index = list.getSelectedRow();
                        employeeWindow edit = new employeeWindow(selected, index);
                    }
                }
            });
        }
        JScrollPane scroll = new JScrollPane(list);
        add(scroll, BorderLayout.CENTER);
    }
    abstract class editWindow extends JFrame {
        //This class automatically adds text fields for the columns in the table.
		private String[] selected;
		private int index;
        public editWindow(String[] selected, int index) {
			this.selected = selected;
			this.index = index;
            setTitle("");
            setLayout(new GridLayout(selected.length + 1, 2));
            setSize((int) (x * 0.4), (int) (y * (titles.length + 2) * 0.05));
            setLocationRelativeTo(null);
            setAlwaysOnTop(true);
            setResizable(false);
            ArrayList<JTextField> fields = new ArrayList<>();
            for (int i = 0; i < titles.length; i++) {
                JLabel j = new JLabel(titles[i]);
                JTextField k = new JTextField(selected[i]);
                fields.add(k);
                add(j);
                add(k);
            }
			fields.get(0).setEnabled(false);
            setVisible(true);
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
					String[] newValues = new String[selected.length];
                    for (int i = 1; i < newValues.length; i++) {
                        newValues[i] = fields.get(i).getText();
                    }
					for (int i = 1; i < newValues.length; i++) {
						if (newValues[i] != null && !(newValues[i].equals(selected[i]))) {
							sql.update(SqlTableName, SqlColumnNames[i], SqlColumnNames[0], selected[0], newValues[i]);
						}
					}
					table[index] = newValues;
					tabModel.removeRow(index);
					tabModel.insertRow(index, newValues);
					if (searchTableMod != null) {
						int searchSelectedRow = searchTab.getSelectedRow();
						if (searchSelectedRow >= 0) {
							searchTableMod.removeRow(searchSelectedRow);
							searchTableMod.insertRow(searchSelectedRow, newValues);
						}
					}
					dispose();
				}
            });
            add(save);
            add(cancel);
        }
		public String[] getEdited() {
			return selected;
		}
		public int getIndex() {
			return index;
		}
    }
    private class orderWindow extends editWindow {
        public orderWindow(String[] selected, int index) {
            super(selected, index);
            setTitle("Edit order");
        }
    }
    private class employeeWindow extends editWindow {
        public employeeWindow(String[] selected, int index) {
            super(selected, index);
            setTitle("Edit employee");
        }
    }
    class GenericSearch extends JPanel {
        //This is a generic search tab with button, which will show results in a popup window
        private String[][] searchTable;
        public GenericSearch(String query, String[] titles) {
            table = sql.getStringTable(query, false);
            setLayout(new BorderLayout());
            JTextField search = new JTextField();
            JButton searcher = new JButton("Search");
            searcher.setToolTipText("Search for any entry and display all matches in a separate window.");
            Action searchPress = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ArrayList<Integer> rowAdded = new ArrayList<Integer>();
                    searchTable = new String[table.length][table[0].length];
                    for (int i = 0; i < table.length; i++) {
                        for (int j = 0; j < table[i].length; j++) {
                            if (table[i][j] == null) {
                                table[i][j] = "";
                            }
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

        private class searchWindow extends JFrame {
            public searchWindow() {
                setSize((int) (x * 0.4), (int) (y * 0.4));
                setTitle("Search results");
                setAlwaysOnTop(true);
				searchTableMod = new DefaultTableModel(searchTable, titles);
                searchTab = new JTable(searchTableMod) {
                    private static final long serialVersionUID = 1L;

                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                JScrollPane scroll = new JScrollPane(searchTab);
                searchTab.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            String[] selected = searchTable[searchTab.getSelectedRow()];
							int selectedIndex = searchTab.getSelectedRow();
                            for (int i = 0; i < table.length; i++) {
                                if (Arrays.equals(selected, table[i])) {
                                    if (GenericList.this instanceof EmployeeTab) {
                                        employeeWindow edit = new employeeWindow(table[i], i);
                                    }
                                    else if (GenericList.this instanceof OrderTab) {
                                        orderWindow edit = new orderWindow(table[i], i);
                                    }
                                }
                            }
                        }
                    }
                });
                add(scroll, BorderLayout.CENTER);
                setLocationRelativeTo(null);
                setVisible(true);
            }
        }
    }
}