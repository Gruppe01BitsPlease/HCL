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

/**
 * Creates the an extended JPanel that most other tabs use
 */
class GenericList extends JPanel {
    //This class is the superclass for most of our tabs
    private String query;
	private String SqlTableName;
    private String[][] table;
	private String[][] searchTable;
	private String[] titles;
	private String[] SqlColumnNames;
	private DataTyper.DataType[] dataTypes;
    private DefaultTableModel tabModel;
	private DefaultTableModel searchTableMod;
	private String[][] linkTables;
	private String[][] FKs;
	private JTableHCL list;
	private JTableHCL searchList;
	private JScrollPane scroll;
	private CardTable cards;
	private CardLayout cardLayout;
	private String scrollName = "List";
	private String searchName = "Search";
    private SQL sql;
	private int role;
	private boolean searchEnabled = false;
	private Action searchPress;
	GenericList(String query, String SqlTableName, String[][] linkTables, String[][] FKs, SQL sql, int role, int defaultSortColumn) {
		System.out.println(SqlTableName + "query: " + query);
		this.role = role;
		this.FKs = FKs;
		this.sql = sql;
		if (sql == null) {
			System.out.println("SQL is null!");
		}
		this.table = sql.getStringTable(query, false);
		SqlColumnNames = sql.getColumnNames(query);
		if (SqlColumnNames == null) {
			SqlColumnNames = new String[1];
		}
		fillTable();
		dataTypes = DataTyper.getDataTypesSQL(SqlColumnNames);
		if (dataTypes == null) {
			dataTypes = new DataTyper.DataType[1];
		}
		if (FKs != null) {
			for (int i = 0; i < FKs.length; i++) {
				dataTypes[Integer.parseInt(FKs[i][1])] = DataTyper.DataType.FOREIGN;
			}
		}
		this.SqlTableName = SqlTableName;
		this.titles = ColumnNamer.getNamesFromArray(SqlColumnNames);
		this.query = query;
		this.linkTables = linkTables;

		setLayout(new BorderLayout());
		tabModel = new DefaultTableModel(table, titles);
        list = new JTableHCL(tabModel);
		list.getRowSorter().toggleSortOrder(defaultSortColumn);
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int ID = Integer.parseInt(table[Stuff.findIndexOf(table, (String) list.getValueAt(list.getSelectedRow(), 0), 0)][0]);
					System.out.println("ID: " + ID);
					edit(ID, false);
				}
			}
		});
        scroll = new JScrollPane(list);
		cards = new CardTable();
		add(new NorthBar(), BorderLayout.NORTH);
		add(cards, BorderLayout.CENTER);
		cardLayout.show(cards, scrollName);
		list.removeIDs();
    }
	int getSelectedID() {
		if (scroll.isVisible()) {
			return Integer.parseInt((String) list.getValueAt(list.getSelectedRow(), 0));
		}
		else {
			return Integer.parseInt((String) searchList.getValueAt(searchList.getSelectedRow(), 0));
		}
	}
	class CardTable extends JPanel {
		public CardTable() {
			cardLayout = new CardLayout();
			setLayout(cardLayout);
			add(scroll, scrollName);
		}
	}
	public void refresh() {
		int[] sortColumn = list.getSortColumn();
		try {
			table = sql.getStringTable(query, false);
			fillTable();
			SqlColumnNames = sql.getColumnNames(query);
			titles = ColumnNamer.getNamesFromArray(SqlColumnNames);
			dataTypes = DataTyper.getDataTypesSQL(SqlColumnNames);
			if (FKs != null) {
				for (int i = 0; i < FKs.length; i++) {
					dataTypes[Integer.parseInt(FKs[i][1])] = DataTyper.DataType.FOREIGN;
				}
			}
			System.out.println(Arrays.toString(SqlColumnNames));
			tabModel = new DefaultTableModel(table, titles);
			list.setModel(tabModel);
			System.out.println("REFRESH");
			ActionEvent act = new ActionEvent(this, 0, "");
			searchPress.actionPerformed(act);
			list.removeIDs();
			if (sortColumn[0] != -1) {
				list.setSortColumn(sortColumn);
			}
		}
		catch (Exception e) {
			System.out.println("ERROR: " + e.toString());
		}
	}
	private void fillTable() {
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < SqlColumnNames.length; j++) {
				if (table[i][j] == null) {
					table[i][j] = "";
				}
			}
		}
	}
	int generate(String[] arguments) {
		return -4;
	}
	int delete(int nr) {
		return -4;
	}
	void edit(int ID, boolean newItem) {
        EditWindow edit = new EditWindow(ID, newItem);
    }
	private class NorthBar extends JPanel {
		public NorthBar() {
			setLayout(new GridLayout(1, 0));
			JButton refresh = new JButton("Refresh");
			JButton newThing = new JButton("New...");
			JButton delete = new JButton("Delete");
			delete.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (list.getSelectedRow() != -1) {
						String[] options = {"Yes", "No"};
						int sure = JOptionPane.showOptionDialog(GenericList.this, "Are you sure?", "Update", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
						if (sure == 0) {
							int[] s = list.getSelectedRows();
							ArrayList<String> delIDs = new ArrayList<>();
							for (int i = 0; i < s.length; i++) {
								String ID = (String) list.getValueAt(s[i], 0);
								delIDs.add(ID);
								System.out.println("ID to be deleted: " + ID);
							}
							for (String i : delIDs) {
								//int args = Integer.parseInt(table[list.getSelectedRow()][0]);
								GenericList.this.delete(Integer.parseInt(i));
								refresh();
							}
						}
					}
				}
			});
			refresh.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					refresh();
				}
			});
			newThing.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					edit(-1, true);
				}
			});
			add(newThing);
			add(delete);
			add(refresh);
		}
	}
	class EditWindow extends JFrame {
		private String[] selected;
		private ArrayList<JComponent> fields = new ArrayList<>();
		//int[] inputTable = { linkIndex, ID of other item, amount };
		private ArrayList<LinkTab> linkTabs = new ArrayList<>();
		private boolean newEntry;
		private int index;
		private EditFields editFields;
		public EditWindow(int ID, boolean newEntry) {
			this.newEntry = newEntry;
			if (!newEntry) {
				index = Stuff.findIndexOf(table, Integer.toString(ID), 0);
				selected = table[index];
			}
			if (newEntry) {
				setTitle("New Item");
				selected = new String[titles.length];
			} else {
				setTitle("View Item");
			}
			setSize(Stuff.getWindowSize(0.5,0.5));
			setLocationRelativeTo(null);
			setAlwaysOnTop(true);
			setResizable(false);
			editFields = new EditFields(titles, selected, newEntry, null, sql);
			JTabbedPane tabs = new JTabbedPane();
			tabs.addTab("Info", editFields);
			if (linkTables != null) {
				for (int i = 0; i < linkTables.length; i++) {
					LinkTab k = new LinkTab(linkTables[i], SqlColumnNames[0], ID, sql, newEntry);
					tabs.addTab(linkTables[i][0], k);
					linkTabs.add(k);
				}
				add(tabs, BorderLayout.CENTER);
			}
			add(tabs, BorderLayout.CENTER);
			add(new SaveCancelButtons(), BorderLayout.SOUTH);
			setVisible(true);
		}
		class SaveCancelButtons extends JPanel {
			public SaveCancelButtons() {
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
						int sure = JOptionPane.showOptionDialog(EditWindow.this, "Are you sure?", "Update", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
						if (sure == 0) {
							boolean success = true;
							String[] newValues = editFields.getNewValues();
							if (!newEntry) {
								for (int i = 1; i < newValues.length; i++) {
									boolean valid = true;
									//checks if number is valid
									if (dataTypes[i] == DataTyper.DataType.INT) {
										try {
											int value = Integer.parseInt(newValues[i]);
										}
										catch (NumberFormatException k) {
											JOptionPane.showMessageDialog(EditWindow.this, "Please enter a valid number in field: " + titles[i]);
											valid = false;
											success = false;
										}
									}
									if (valid && (newValues[i] != null && !(newValues[i].equals("")) && !(newValues[i].equals(selected[i])))) {
										if (dataTypes[i] == DataTyper.DataType.BOOLEAN) {
											System.out.println("Boolean value: " + newValues[i]);
											if (newValues[i].equals("true")) {
												sql.update(SqlTableName, SqlColumnNames[i], SqlColumnNames[0], selected[0], true);
											} else if (newValues[i].equals("false")) {
												sql.update(SqlTableName, SqlColumnNames[i], SqlColumnNames[0], selected[0], false);
											} else {
												System.out.println("ERROR NO BOOLEAN VALUE");
												success = false;
											}
										}
										else {
											sql.update(SqlTableName, SqlColumnNames[i], SqlColumnNames[0], selected[0], newValues[i]);
										}
									}
								}
							} else if (newEntry) {
								boolean valid = true;
								for (int i = 0; i < dataTypes.length; i++) {
									if (dataTypes[i] == DataTyper.DataType.INT) {
										try {
											Integer.parseInt(newValues[i]);
										}
										catch (Exception k) {
											valid = false;
											success = false;
											JOptionPane.showMessageDialog(EditWindow.this, "Please enter a valid number in field: " + titles[i]);
										}
									}
								}
								if (valid && !(sql.rowExists(SqlTableName, SqlColumnNames[0], newValues[0]))) {
									int res = GenericList.this.generate(newValues);
									if (res == -2) {
										JOptionPane.showMessageDialog(EditWindow.this, "Database Error!");
										success = false;
									} else if (res == -3) {
										JOptionPane.showMessageDialog(EditWindow.this, "One of the parameters appears to be invalid.");
										success = false;
									} else if (res == -4) {
										JOptionPane.showMessageDialog(EditWindow.this, "There is no method for generating this object, it must be overridden in the tab class.");
										success = false;
									}
									//System.out.println(res);
								}
							}
							for (LinkTab tab : linkTabs) {
								tab.generate();
							}
							//table[index] = newValues;
							refresh();
							if (searchTableMod != null) {
								int searchSelectedRow = searchList.getSelectedRow();
								if (searchSelectedRow >= 0 && searchSelectedRow < searchTable.length) {
									searchTable[searchSelectedRow] = newValues;
									searchTableMod = new DefaultTableModel(searchTable, titles);
									searchList.setModel(searchTableMod);
								}
							}
							if (success) {
								dispose();
							}
						}
					}
				});
				add(save);
				add(cancel);
			}
		}
	}
    class GenericSearch extends JPanel {
		private JTextField search;
        public GenericSearch() {
			ButtonPanel panel = new ButtonPanel();
			//refresh();
            setLayout(new BorderLayout());
            search = new JTextField();
            search.addActionListener(searchPress);
            add(search, BorderLayout.CENTER);
			add(panel, BorderLayout.EAST);
        }
		private class ButtonPanel extends JPanel {
			public ButtonPanel() {
				setLayout(new GridLayout(1, 2));
				JButton searcher = new JButton("Search");
				searchPress = new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (search.getText().equals("")) {
							cardLayout.show(cards, scrollName);
						}
						else {
							ArrayList<Integer> rowAdded = new ArrayList<>();
							searchTable = new String[table.length][table[0].length];
							ArrayList<String[]> searchArray = new ArrayList<>();
							for (int i = 0; i < table.length; i++) {
								for (int j = 1; j < table[i].length; j++) {
									if (!(rowAdded.contains(i)) && table[i][j].toLowerCase().contains(search.getText().toLowerCase())) {
										int k = 0;
										boolean added = false;
										for (int l = 0; l < searchTable.length; l++) {
											while (!added && k < searchTable[0].length) {
												if (searchTable[k][0] == null || searchTable[k][0].isEmpty()) {
													searchTable[k] = table[i];
													searchArray.add(table[i]);
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
							if (searchArray.size() > 0) {
								String[][] newArr = new String[searchArray.size()][];
								for (int i = 0; i < newArr.length; i++) {
									newArr[i] = searchArray.get(i);
								}
								searchTable = newArr;
								cardLayout.show(cards, searchName);
								DefaultTableModel searchTM = new DefaultTableModel(searchTable, titles);
								System.out.println("Titles:\t" + Arrays.toString(titles));
								System.out.println("Search array:\t" + Arrays.toString(searchArray.get(0)));
								searchList = new JTableHCL(searchTM);
								searchList.removeIDs();
								searchList.addMouseListener(new MouseAdapter() {
									@Override
									public void mouseClicked(MouseEvent e) {
										if (e.getClickCount() == 2) {
											edit(Integer.parseInt(searchTable[Stuff.findIndexOf(searchTable, (String) searchList.getValueAt(searchList.getSelectedRow(), 0), 0)][0]), false);
										}
									}
								});
								JScrollPane searchScroll = new JScrollPane(searchList);
								cards.add(searchScroll, searchName);
								cardLayout.show(cards, searchName);
							}
						}
					}
				};
				searcher.addActionListener(searchPress);
				add(searcher);
			}
		}
    }
}