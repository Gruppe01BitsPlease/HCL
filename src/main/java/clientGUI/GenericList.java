package clientGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
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
	private String[][] linkTables;
	private JTable list;
	private JTable searchTab;
    private SQL sql;
    private int x;
    private int y;
	private boolean searchEnabled = false;
	private Action searchPress;
	public GenericList(String query, String SqlTableName, String[] dataTypes, String[][] linkTables, SQL sql) {
		try {
			this.sql = sql;
			this.table = sql.getStringTable(query, false);
			SqlColumnNames = sql.getColumnNames(query);
			fillTable();
		}
		catch (Exception e) {
			System.out.println("ERROR");
		}
		this.dataTypes = dataTypes;
		this.SqlTableName = SqlTableName;
		this.titles = ColumnNamer.getNamesFromArray(SqlColumnNames);
		this.query = query;
		this.linkTables = linkTables;
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
					if (!searchEnabled) {
						String[] selected = table[list.getSelectedRow()];
						int index = list.getSelectedRow();
						editWindow edit = new editWindow(selected, index, false);
					}
					else if (searchEnabled) {
						String[] selected = searchTable[list.getSelectedRow()];
						int index = list.getSelectedRow();
						editWindow edit = new editWindow(selected, index, false);
					}
				}
			}
		});
		list.setAutoCreateRowSorter(true);
        JScrollPane scroll = new JScrollPane(list);
		add(new northBar(), BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
		//removePK();
    }
	public void refresh() {
		try {
			table = sql.getStringTable(query, false);
			fillTable();
			SqlColumnNames = sql.getColumnNames(query);
			titles = ColumnNamer.getNamesFromArray(SqlColumnNames);
			System.out.println(Arrays.toString(SqlColumnNames));
			tabModel = new DefaultTableModel(table, titles);
			list.setModel(tabModel);
			System.out.println("REFRESH");
			if (searchEnabled) {
				ActionEvent act = new ActionEvent(this, 0, "");
				searchPress.actionPerformed(act);
			}
			//removePK();
		}
		catch (Exception e) {
			System.out.println("ERROR: " + e.toString());
		}
	}
	public void removePK() {
		fillTable();
		String[][] noPK = new String[table.length][];
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length - 1; j++) {
				noPK[i][j] = table[i][j + 1];
			}
		}
		String[] titlesNoPK = new String[SqlColumnNames.length - 1];
		for (int i = 0; i < SqlColumnNames.length - 1; i++) {
			titlesNoPK[i] = SqlColumnNames[i + 1];
		}
		titlesNoPK = ColumnNamer.getNamesFromArray(titlesNoPK);
		tabModel = new DefaultTableModel(noPK, titlesNoPK);
		list.setModel(tabModel);
	}
	public void fillTable() {
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < SqlColumnNames.length; j++) {
				if (table[i][j] == null) {
					table[i][j] = "";
				}
			}
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
					for (int i = 0; i < table.length; i++) {
						for (int j = 0; j < SqlColumnNames.length; j++) {
							newTable[i][j] = table[i][j];
						}
					}
					String[] selected = newTable[newTable.length - 1];
					int index = newTable.length - 1;
					table = newTable;
					fillTable();
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
			if (date != null && !(date.equals(""))) {
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
		private String[] selected;
		private ArrayList<JComponent> fields = new ArrayList<>();
		private ArrayList<String[]> addedLinks = new ArrayList<>();
		private boolean newEntry;
		private int index;
		private boolean linkTableChanged;
		public editWindow(String[] selected, int index, boolean newEntry) {
			this.newEntry = newEntry;
			this.selected = selected;
			this.index = index;
			if (newEntry) {
				setTitle("New Item");
			} else {
				setTitle("View Item");
			}
			setSize((int) (x * 0.5), (int) (y * 0.5));
			setLocationRelativeTo(null);
			setAlwaysOnTop(true);
			setResizable(false);
			JTabbedPane tabs = new JTabbedPane();
			tabs.addTab("Info", new editFields());
			if (linkTables != null) {
				for (int i = 0; i < linkTables.length; i++) {
					if (selected[0] != null && !(selected[0].equals(""))) {
						String link = "SELECT * FROM " + linkTables[i][2] + " NATURAL JOIN " + linkTables[i][3] +
								" WHERE " + SqlColumnNames[0] + " = " + selected[0];
						System.out.println(link);
						tabs.addTab(linkTables[i][0], new linkTab(link, i, linkTables[i][2]));
					}
					else {
						String link = "SELECT * FROM " + linkTables[i][2] + " NATURAL JOIN " + linkTables[i][3] +
								" WHERE " + SqlColumnNames[0] + " = -1";
						System.out.println(link);
						tabs.addTab(linkTables[i][0], new linkTab(link, i, linkTables[i][2]));
					}
				}
				add(tabs, BorderLayout.CENTER);
			}
			if (dataTypes.length != selected.length) {
				System.out.println("WARNING The data type array is not the correct size!");
			}
			add(tabs, BorderLayout.CENTER);
			add(new saveCancelButtons(), BorderLayout.SOUTH);
			setVisible(true);
		}
		String[] comboBoxChoices;
		class saveCancelButtons extends JPanel {
			public saveCancelButtons() {
				setLayout(new GridLayout(1, 2));
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
						String[] options = {"Yes", "No"};
						int sure = JOptionPane.showOptionDialog(editWindow.this, "Are you sure?", "Update", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
						if (sure == 0) {
							if (linkTableChanged) {
								//LinkManager linkMng = new LinkManager(sql);
								//linkMng.generate(linkTables[index][2], SqlColumnNames[0], linkTables[index][1], Integer.parseInt(selected[0]), Integer.parseInt(choiceID[input.getSelectedIndex()]), Integer.parseInt(amount.getText()));
							}
							String[] newValues = new String[selected.length];
							for (int i = 0; i < newValues.length; i++) {
								if (fields.get(i) instanceof JTextField) {
									JTextField field = (JTextField) fields.get(i);
									newValues[i] = field.getText();
								} else if (fields.get(i) instanceof JCheckBox) {
									JCheckBox chk = (JCheckBox) fields.get(i);
									if (chk.isSelected()) {
										newValues[i] = "true";
									} else if (!(chk.isSelected())) {
										newValues[i] = "false";
									}
								} else if (fields.get(i) instanceof datePane) {
									datePane dtp = (datePane) fields.get(i);
									newValues[i] = dtp.getDate();
								} else if (fields.get(i) instanceof JComboBox) {
									JComboBox cmb = (JComboBox) fields.get(i);
									String sel = (String) cmb.getSelectedItem();
									String[] chosen = sel.split(",");
									newValues[i] = chosen[0];
									System.out.println(newValues[i]);
								}
							}
							if (!newEntry) {
								for (int i = 1; i < newValues.length; i++) {
									if (newValues[i] != null && !(newValues[i].equals("")) && !(newValues[i].equals(selected[i]))) {
										if (dataTypes[i].equals("boolean")) {
											if (newValues[i].equals("true")) {
												boolean update = true;
												sql.update(SqlTableName, SqlColumnNames[i], SqlColumnNames[0], selected[0], update);
											} else if (newValues[i].equals("false")) {
												boolean update = false;
												sql.update(SqlTableName, SqlColumnNames[i], SqlColumnNames[0], selected[0], update);
											} else {
												System.out.println("ERROR NO BOOLEAN VALUE");
											}
										} else {
											sql.update(SqlTableName, SqlColumnNames[i], SqlColumnNames[0], selected[0], newValues[i]);
										}
									}
								}
							} else if (newEntry) {
								if (!(sql.rowExists(SqlTableName, SqlColumnNames[0], newValues[0]))) {
									int res = GenericList.this.generate(newValues);
									if (res == -2) {
										JOptionPane.showMessageDialog(editWindow.this, "Database Error!");
									} else if (res == -3) {
										JOptionPane.showMessageDialog(editWindow.this, "There is a problem with one of the parameters.");
									} else if (res == -4) {
										JOptionPane.showMessageDialog(editWindow.this, "There is no method for generating this object, it must be overridden in the tab class.");
									}
									//System.out.println(res);
								} else {
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
		class editFields extends JPanel {
			public editFields() {
				setLayout(new GridLayout(selected.length + 1, 2));
				for (int i = 0; i < dataTypes.length; i++) {
					if (dataTypes[i].equals("boolean")) {
						JLabel j = new JLabel(titles[i]);
						JCheckBox k = new JCheckBox();
						if (selected[i] != null && selected[i].equals("1")) {
							k.setSelected(true);
						}
						fields.add(k);
						add(j);
						add(k);
					} else if (dataTypes[i].equals("date")) {
						JLabel j = new JLabel(titles[i]);
						datePane k = new datePane(selected[i]);
						fields.add(k);
						add(j);
						add(k);
					} else if (dataTypes[i].contains("SELECT")) {
						JLabel j = new JLabel(titles[i]);
						String[][] choices = { sql.getColumn(dataTypes[i], 0), sql.getColumn(dataTypes[i], 1) };

						//String[][] choices = sql.getStringTable(dataTypes[i], false);
						String[] displayed = new String[choices[0].length];
						int selItem = 0;
						for (int l = 0; l < displayed.length; l++) {
							displayed[l] = choices[0][l] + ", " + choices[1][l];
							if (choices[0][l].equals(selected[i])) {
								selItem = l;
							}
						}
						//System.out.println(Arrays.toString(choices));
						JComboBox<String> k = new JComboBox<>(displayed);
						k.setSelectedIndex(selItem);
						fields.add(k);
						add(j);
						add(k);
					} else {
						JLabel j = new JLabel(titles[i]);
						JTextField k = new JTextField(selected[i]);
						fields.add(k);
						add(j);
						add(k);
					}
				}
				fields.get(0).setEnabled(false);
				setVisible(true);
			}
		}
		class linkTab extends JPanel {
			private int index;
			private DefaultTableModel tableModel;
			private String[][] data;
			private JTable list;
			private String link;
			private String[][] columns;
			private LinkManager linkMng = new LinkManager(sql);
			public linkTab(String link, int index, String tableName) {
				setLayout(new BorderLayout());
				this.index = index;
				this.link = link;
				data = sql.getStringTable(link, false);
				columns = ColumnNamer.getNamesWithOriginals(link, sql);
				tableModel = new DefaultTableModel(data, columns[1]);
				//System.out.println(Arrays.toString(data));
				list = new JTable(tableModel) {
					private static final long serialVersionUID = 1L;
					public boolean isCellEditable(int row, int column) {
						return false;
					}
				};
				JScrollPane scroll = new JScrollPane(list);
				lowerButtons lower = new lowerButtons();
				add(scroll, BorderLayout.CENTER);
				add(lower, BorderLayout.SOUTH);
				list.setAutoCreateRowSorter(true);
			}
			class lowerButtons extends JPanel {
				public lowerButtons() {
					setLayout(new GridLayout(1, 2));
					JButton neue = new JButton("New");
					neue.addActionListener(new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent e) {
							inputBox input = new inputBox();
						}
					});
					JButton delete = new JButton("Delete");
					delete.addActionListener(new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent e) {
							String[] options = {"Yes", "No"};
							int sure = JOptionPane.showOptionDialog(editWindow.this, "Are you sure?", "Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
							if (sure == 0) {
								int num = -1;
								String[] sel = data[list.getSelectedRow()];
								for (int i = 0; i < columns[0].length; i++) {
									if (columns[0][i].equals(linkTables[index][1])) {
										num = i;
									}
								}
								System.out.println(linkTables[index][2] + " " + SqlColumnNames[0] + " " + linkTables[index][1] + " " +
										Integer.parseInt(selected[0]) + " " + Integer.parseInt(sel[num]));
								int foo = linkMng.delete(linkTables[index][2], SqlColumnNames[0], linkTables[index][1],
										Integer.parseInt(selected[0]), Integer.parseInt(sel[num]));
								System.out.println(foo);
								data = sql.getStringTable(link, false);
								tableModel = new DefaultTableModel(data, columns[1]);
								list.setModel(tableModel);
							}
						}
					});
					add(neue);
					add(delete);
				}
			}
			class inputBox extends JFrame {
				public inputBox() {
					setSize((int) (x * 0.3), (int) (y * 0.2));
					setLayout(new GridLayout(3, 2));
					setLocationRelativeTo(null);
					setAlwaysOnTop(true);
					JLabel label = new JLabel("Select ID:");
					JLabel amountLabel = new JLabel("Amount");
					String choiceQuery = "SELECT " + linkTables[index][1] + ", " + linkTables[index][4] + " FROM " + linkTables[index][3];
					String[][] choices = sql.getStringTable(choiceQuery, false);
					String[] choice = new String[choices.length];
					for (int i = 0; i < choices.length; i++) {
						choice[i] = choices[i][0] + ", " + choices[i][1];
					}

					//System.out.println(Arrays.toString(choiceID));
					JComboBox<String> input = new JComboBox<>(choice);
					JTextField amount = new JTextField();
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
							String[] options = {"Yes", "No"};
							int sure = JOptionPane.showOptionDialog(editWindow.this, "Are you sure?", "Update", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
							if (sure == 0) {
								String IDquery = "SELECT " + linkTables[index][1] + " FROM " + linkTables[index][3];
								String[] choiceID = sql.getColumn(IDquery, 0);
								System.out.println(Arrays.toString(choiceID));
								linkTableChanged = true;
								String[] addedLink = sql.getRow("SELECT * FROM " + linkTables[index][3] + " WHERE " + linkTables[index][1] + " = " + choiceID[input.getSelectedIndex()]);
								System.out.println(Arrays.toString(addedLink));
								addedLinks.add(addedLink);
								String[][] newData = new String[data.length + 1][];
								for (int i = 0; i < data.length; i++) {
									newData[i] = data[i];
								}
								newData[data.length] = addedLink;
								data = newData;
							}
							else if (sure == 1) {
								dispose();
							}
							tableModel = new DefaultTableModel(data, columns[1]);
							list.setModel(tableModel);
						}
					});
					add(label);
					add(input);
					add(amountLabel);
					add(amount);
					add(save);
					add(cancel);
					setVisible(true);
				}
			}
		}
	}
    class GenericSearch extends JPanel {
        //This is a generic search tab with button, which will show results in a popup window
		private JTextField search;
        public GenericSearch() {
			buttonPanel panel = new buttonPanel();
			//refresh();
            setLayout(new BorderLayout());
            search = new JTextField();
            search.addActionListener(searchPress);
            add(search, BorderLayout.CENTER);
			add(panel, BorderLayout.EAST);
        }
		private class buttonPanel extends JPanel {
			Action toggleSearch;
			public buttonPanel() {
				setLayout(new GridLayout(1, 2));
				JButton searcher = new JButton("Search");
				JButton closeSearch = new JButton("Close search");
				searcher.setToolTipText("Search for any entry and display all matches in a separate window.");
				searchPress = new AbstractAction() {
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
						//searchWindow window = new searchWindow();
						tabModel = new DefaultTableModel(searchTable, titles);
						list.setModel(tabModel);
						toggleSearch.actionPerformed(new ActionEvent(this, 0, ""));
					}
				};
				toggleSearch = new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (searchEnabled) {
							searchEnabled = false;
							remove(closeSearch);
						}
						else if (!searchEnabled) {
							searchEnabled = true;
							add(closeSearch);
						}
					}
				};
				closeSearch.addActionListener(toggleSearch);
				searcher.addActionListener(searchPress);
				add(searcher);
			}
		}
    }
}