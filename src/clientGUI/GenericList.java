package clientGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

import backend.*;

class GenericList extends JPanel {
    //This is a generic list, shown in the middle of the tab where needed
    private String query;
	private String SqlTableName;
    private String[][] table;
	private String[][] searchTable;
	private String[] titles;
	private String[] SqlColumnNames;
    private DefaultTableModel tabModel;
	private DefaultTableModel searchTableMod;
	private JTable list;
	private JTable searchTab;
    private SQL sql;
    private int x;
    private int y;
	public GenericList(String query, String[] titles, String SqlTableName, SQL sql) {
        try {
            this.sql = sql;
            this.table = sql.getStringTable(query, false);
			SqlColumnNames = sql.getColumnNames(query);
			System.out.println(Arrays.toString(SqlColumnNames));
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
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					String[] selected = table[list.getSelectedRow()];
					int index = list.getSelectedRow();
					editWindow edit = new editWindow(selected, index, false);
				}
			}
		});
        JScrollPane scroll = new JScrollPane(list);
		add(new northBar(), BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }
	public void refresh() {
		try {
			table = sql.getStringTable(query, false);
			SqlColumnNames = sql.getColumnNames(query);
			System.out.println(Arrays.toString(SqlColumnNames));
			tabModel = new DefaultTableModel(table, titles);
			list.setModel(tabModel);
			System.out.println("REFRESH");
		}
		catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}
	}
	private class northBar extends JPanel {
		public northBar() {
			setLayout(new GridLayout(1, 5));
			JButton refresh = new JButton("Refresh");
			JButton newThing = new JButton("New...");
			refresh.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					refresh();
				}
			});
			newThing.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String[][] newTable = new String[table.length + 1][SqlColumnNames.length];
					String[] selected = newTable[newTable.length - 1];
					int index = newTable.length - 1;
					table = newTable;
					editWindow edit = new editWindow(selected, index, true);
				}
			});
			add(newThing);
			add(refresh);
		}
	}
	class editWindow extends JFrame {
        //This class automatically adds text fields for the columns in the table.
		private String[] selected;
		private int index;
        public editWindow(String[] selected, int index, boolean newEntry) {
			this.selected = selected;
			this.index = index;
			if (newEntry) {
				setTitle("New Item");
			}
			else {
				setTitle("Edit item");
			}
			setLayout(new GridLayout(selected.length + 1, 2));
            setSize((int) (x * 0.4), (int) (y * (titles.length + 2) * 0.03));
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
			if (!newEntry) {
				fields.get(0).setEnabled(false);
			}
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
					String[] options = { "Yes", "No" };
					int sure = JOptionPane.showOptionDialog(editWindow.this, "Are you sure?", "Update", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
					if (sure == 0) {
						String[] newValues = new String[selected.length];
						for (int i = 0; i < newValues.length; i++) {
							newValues[i] = fields.get(i).getText();
						}
						if (!newEntry) {
							for (int i = 1; i < newValues.length; i++) {
								if (newValues[i] != null && !(newValues[i].equals("")) && !(newValues[i].equals(selected[i]))) {
									sql.update(SqlTableName, SqlColumnNames[i], SqlColumnNames[0], selected[0], newValues[i]);
								}
							}
						}
						else if (newEntry) {
							if (!(sql.rowExists(SqlTableName, SqlColumnNames[0], newValues[0]))) {
								for (int i = 0; i < newValues.length; i++) {
									if (newValues[i] != null && !(newValues[i].equals(""))) {
										sql.insert("INSERT INTO " + SqlTableName + "(" + SqlColumnNames[i] + ") VALUE " + newValues[i]);
										System.out.println("INSERT INTO " + SqlTableName + "(" + SqlColumnNames[i] + ") VALUES " + newValues[i]);
									}
								}
							}
							else {
								JOptionPane.showMessageDialog(null, "Entry already exists! Choose a different ID number.");
							}
						}
						table[index] = newValues;
						refresh();
						if (searchTableMod != null) {
							int searchSelectedRow = searchTab.getSelectedRow();
							if (searchSelectedRow >= 0 && searchSelectedRow < searchTable.length) {
								searchTable[searchSelectedRow] = newValues;
								searchTableMod = new DefaultTableModel(searchTable, titles);
								searchTab.setModel(searchTableMod);
							}
						}
						dispose();
					}
				}
			});
            add(save);
            add(cancel);
        }
    }

    class GenericSearch extends JPanel {
        //This is a generic search tab with button, which will show results in a popup window
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
                                        editWindow edit = new editWindow(table[i], i, false);
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