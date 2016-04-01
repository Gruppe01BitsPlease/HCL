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
	private String[] dataTypes;
    private DefaultTableModel tabModel;
	private DefaultTableModel searchTableMod;
	private JTable list;
	private JTable searchTab;
    private SQL sql;
    private int x;
    private int y;
	public GenericList(String query, String[] titles, String SqlTableName, String[] dataTypes, SQL sql) {
        try {
            this.sql = sql;
            this.table = sql.getStringTable(query, false);
			SqlColumnNames = sql.getColumnNames(query);
			System.out.println(Arrays.toString(SqlColumnNames));
        }
        catch (Exception e) {
            System.out.println("ERROR");
        }
		this.dataTypes = dataTypes;
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
	public int generate(String[] arguments) {
		return -4;
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
	class datePane extends JPanel {
		JTextField year;
		JTextField month;
		JTextField day;
		public datePane(String date) {
			//2014-01-01
			setLayout(new GridLayout(1, 3));
			if (date != null) {
				year = new JTextField(date.substring(0, 4));
				month = new JTextField(date.substring(5, 7));
				day = new JTextField(date.substring(8, 10));
			}
			else {
				year = new JTextField("");
				month = new JTextField("");
				day = new JTextField("");
			}
			add(year);
			add(month);
			add(day);
		}
		public datePane() {
			setLayout(new GridLayout(1, 3));
			year = new JTextField("");
			month = new JTextField("");
			day = new JTextField("");
			add(year);
			add(month);
			add(day);
		}
		public String getDate() {
			return year.getText() + "-" + month.getText() + "-" + day.getText();
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
				setTitle("Edit Item");
			}
			setLayout(new GridLayout(selected.length + 1, 2));
            setSize((int) (x * 0.4), (int) (y * (titles.length + 2) * 0.03));
            setLocationRelativeTo(null);
            setAlwaysOnTop(true);
            setResizable(false);
            ArrayList<JComponent> fields = new ArrayList<>();
			if (dataTypes.length != selected.length) {
				System.out.println("The data type array is not the correct size!");
			}
            for (int i = 0; i < dataTypes.length; i++) {
				if (dataTypes[i].equals("boolean")) {
					JLabel j = new JLabel(titles[i]);
					JCheckBox k = new JCheckBox();
					if (selected[i].equals("1")) {
						k.setSelected(true);
					}
					fields.add(k);
					add(j);
					add(k);
				}
				else if (dataTypes[i].equals("date")) {
					JLabel j = new JLabel(titles[i]);
					datePane k = new datePane(selected[i]);
					fields.add(k);
					add(j);
					add(k);
				}
				else {
					JLabel j = new JLabel(titles[i]);
					JTextField k = new JTextField(selected[i]);
					fields.add(k);
					add(j);
					add(k);
				}
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
					String[] options = { "Yes", "No" };
					int sure = JOptionPane.showOptionDialog(editWindow.this, "Are you sure?", "Update", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
					if (sure == 0) {
						String[] newValues = new String[selected.length];
						for (int i = 0; i < newValues.length; i++) {
							if (fields.get(i) instanceof JTextField) {
								JTextField field = (JTextField) fields.get(i);
								newValues[i] = field.getText();
							}
							else if (fields.get(i) instanceof JCheckBox) {
								JCheckBox chk = (JCheckBox) fields.get(i);
								if (chk.isSelected()) {
									newValues[i] = "true";
								}
								else if (!(chk.isSelected())) {
									newValues[i] = "false";
								}
							}
							else if (fields.get(i) instanceof datePane) {
								datePane dtp = (datePane) fields.get(i);
								newValues[i] = dtp.getDate();
							}
						}
						if (!newEntry) {
							for (int i = 1; i < newValues.length; i++) {
								if (newValues[i] != null && !(newValues[i].equals("")) && !(newValues[i].equals(selected[i]))) {
									if (dataTypes[i].equals("boolean")) {
										if (newValues[i].equals("true")) {
											boolean update = true;
											sql.update(SqlTableName, SqlColumnNames[i], SqlColumnNames[0], selected[0], update);
										}
										else if (newValues[i].equals("false")) {
											boolean update = false;
											sql.update(SqlTableName, SqlColumnNames[i], SqlColumnNames[0], selected[0], update);
										}
										else {
											System.out.println("ERROR NO BOOLEAN VALUE");
										}
									}
									else {
										sql.update(SqlTableName, SqlColumnNames[i], SqlColumnNames[0], selected[0], newValues[i]);
									}
								}
							}
						}
						else if (newEntry) {
							if (!(sql.rowExists(SqlTableName, SqlColumnNames[0], newValues[0]))) {
								int res = GenericList.this.generate(newValues);
								if (res == -2) {
									JOptionPane.showMessageDialog(editWindow.this, "Database Error!");
								}
								else if (res == -3) {
									JOptionPane.showMessageDialog(editWindow.this, "There is a problem with one of the parameters.");
								}
								else if (res == -4) {
									JOptionPane.showMessageDialog(editWindow.this, "There is no method for generating this object, it must be overridden in the tab class.");
								}
								System.out.println(res);
							}
							else {
								JOptionPane.showMessageDialog(editWindow.this, "Entry already exists! Choose a different ID number.");
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