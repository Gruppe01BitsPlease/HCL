package clientGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import backend.*;

class GenericList extends JPanel {
    //This is a generic linkTable, shown in the middle of the tab where needed
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
	private String[][] FKs;
	private JTableHCL list;
	private JTableHCL searchTab;
    private SQL sql;
    private int x;
    private int y;
	private boolean searchEnabled = false;
	private Action searchPress;
	public GenericList(String query, String SqlTableName, String[][] linkTables, String[][] FKs, SQL sql) {
		this.FKs = FKs;
		try {
			this.sql = sql;
			this.table = sql.getStringTable(query, false);
			SqlColumnNames = sql.getColumnNames(query);
			fillTable();
		}
		catch (Exception e) {
			System.out.println("ERROR");
		}

		dataTypes = DataTyper.getDataTypesSQL(SqlColumnNames);
		if (FKs != null) {
			for (int i = 0; i < FKs.length; i++) {
				dataTypes[Integer.parseInt(FKs[i][1])] = FKs[i][0];
			}
		}
		this.SqlTableName = SqlTableName;
		this.titles = ColumnNamer.getNamesFromArray(SqlColumnNames);
		this.query = query;
		this.linkTables = linkTables;
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		x = (int) (screen.width * 0.75);
		y = (int) (screen.height * 0.75);
		setLayout(new BorderLayout());
		tabModel = new DefaultTableModel(table, titles);
        list = new JTableHCL(tabModel);
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
			dataTypes = DataTyper.getDataTypesSQL(SqlColumnNames);
			if (FKs != null) {
				for (int i = 0; i < FKs.length; i++) {
					dataTypes[Integer.parseInt(FKs[i][1])] = FKs[i][0];
				}
			}
			System.out.println(Arrays.toString(SqlColumnNames));
			tabModel = new DefaultTableModel(table, titles);
			list.setModel(tabModel);
			System.out.println("REFRESH");
			if (searchEnabled) {
				ActionEvent act = new ActionEvent(this, 0, "");
				searchPress.actionPerformed(act);
			}
			list.removeIDs();
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
	public int generate(String[] arguments) {
		return -4;
	}
	public int delete(int nr) {
		return -4;
	}
	private class northBar extends JPanel {
		public northBar() {
			setLayout(new GridLayout(1, 5));
			JButton refresh = new JButton("Refresh");
			JButton newThing = new JButton("New...");
			JButton delete = new JButton("Delete");
			delete.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String[] options = {"Yes", "No"};
					int sure = JOptionPane.showOptionDialog(GenericList.this, "Are you sure?", "Update", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
					if (sure == 0) {
						int args = Integer.parseInt(table[list.getSelectedRow()][0]);
						GenericList.this.delete(args);
						refresh();
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
			add(delete);
			add(refresh);
		}
	}
	private class datePane extends JPanel {
		JTextField year;
		JTextField month;
		JTextField day;
		public datePane(String date) {
			//2014-01-01
			setLayout(new GridLayout(1, 3));
			if (date != null && !(date.equals(""))) {
				if (date.length() == 10) {
					year = new JTextField(date.substring(0, 4));
					//System.out.println(year.getText());
					month = new JTextField(date.substring(5, 7));
					//System.out.println(month.getText());
					day = new JTextField(date.substring(8, 10));
					//System.out.println(day.getText());
				}
				else {
					System.out.println("Date format is wrong! GenericList.datePane");
				}
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
		public void setDate(String date) {
			year = new JTextField(date.substring(0, 4));
			month = new JTextField(date.substring(5, 7));
			day = new JTextField(date.substring(8, 10));
		}
	}
	class editWindow extends JFrame {
		private String setBold = "<html><b>";
		private String endBold = "</b></html>";
		private String setGrey = "<html><p style=\"color:#808080\">";
		private String endGrey = "</p></html>";
		private String[] selected;
		private ArrayList<JComponent> fields = new ArrayList<>();
		private ArrayList<String[]> addedLinks = new ArrayList<>();
		private ArrayList<int[]> changeLinks = new ArrayList<>();
		private ArrayList<int[]> createLinks = new ArrayList<>();
		private ArrayList<int[]> removeLinks = new ArrayList<>();
		private ArrayList<JTableHCL> linkJTables = new ArrayList<>();
		private String[][] linkTableData;
		private boolean newEntry;
		private int index;
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
						if (linkTables[i][2].equals(linkTables[i][3])) {
							link = "SELECT * FROM " + linkTables[i][2] +
									" WHERE " + SqlColumnNames[0] + " = " + selected[0];
						}
						System.out.println(link);
						tabs.addTab(linkTables[i][0], new linkTab(link, i));
					}
					else {
						String link = "SELECT * FROM " + linkTables[i][2] + " NATURAL JOIN " + linkTables[i][3] +
								" WHERE " + SqlColumnNames[0] + " = -1";
						System.out.println(link);
						tabs.addTab(linkTables[i][0], new linkTab(link, i));
					}
				}
				add(tabs, BorderLayout.CENTER);
			}
			if (dataTypes.length != selected.length) {
				System.out.println("WARNING The linkTableData type array is not the correct size!");
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
							/*for (JTableHCL i : linkJTables) {
								LinkManager linkMng = new LinkManager(sql);
								for (int j = 0; j < i.getRowCount(); j++) {
									String one = (String) i.getValueAt(j, 0);
									if (one.contains(setBold)) {
										linkMng.generate()
									}
								}
							}*/
							if (removeLinks.size() > 0) {
								LinkManager linkMng = new LinkManager(sql);
								for (int[] i : removeLinks) {
									linkMng.delete(linkTables[i[0]][2], SqlColumnNames[0], linkTables[i[0]][1], Integer.parseInt(selected[0]), i[1]);
								}
							}
							int pk = sql.getLastID();
							System.out.println("New primary key: " + pk);
							if (createLinks.size() > 0) {
								//Saves to link tables if links have been created
								LinkManager linkMng = new LinkManager(sql);
								System.out.println(Arrays.toString(createLinks.get(0)));
								/*for (int i = 0; i < linkTableData.length; i++) {
									if (linkTableData[i][0].contains("<html><p style=\"color:#808080\">")) {

									}
								}*/
								if (newEntry) {
									/*for (int i = 0; i < linkTableData.length; i++) {

									}*/
									System.out.println("NEW LINK NEW ENTRY!");
									for (int[] i : createLinks) {
										linkMng.generate(linkTables[i[0]][2], SqlColumnNames[0], linkTables[i[0]][1], pk, i[1], i[2]);
									}
								}
								else {
									for (int[] i : createLinks) {
										linkMng.generate(linkTables[i[0]][2], SqlColumnNames[0], linkTables[i[0]][1], Integer.parseInt(selected[0]), i[1], i[2]);
									}
								}
							}
							if (changeLinks.size() > 0) {
								//Changes link tables if needed
								LinkManager linkMng = new LinkManager(sql);
								System.out.println("Changelink: " + Arrays.toString(changeLinks.get(0)));
								//int[] inputTable = {linkIndex, Integer.parseInt(choiceID[input.getSelectedIndex()]), Integer.parseInt(amount.getText())};
								for (int[] i : changeLinks) {
									linkMng.editNumber(linkTables[i[0]][2], SqlColumnNames[0], linkTables[i[0]][1], Integer.parseInt(selected[0]), i[1], i[2]);
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
				int length = selected.length + 1;
				for (int i = 0; i < titles.length; i++) {
					if (titles[i].equals("x")) {
						length--;
					}
				}
				setLayout(new GridLayout(length, 2));
				//setSize((int) (x * 0.5), (int) (length * 0.01));
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
					} else if (dataTypes[i].equals("curdate")) {
						JLabel j = new JLabel(titles[i]);
						datePane k = new datePane(selected[i]);
						if (newEntry) {
							LocalDate now = LocalDate.now();
							String date = now.toString();
							k = new datePane(date);
						}
						fields.add(k);
						add(j);
						add(k);
					} else if (dataTypes[i].contains("SELECT")) {
						JLabel j = new JLabel(titles[i]);
						String[][] choices = {sql.getColumn(dataTypes[i], 0), sql.getColumn(dataTypes[i], 1)};
						//String[][] choices = sql.getStringTable(dataTypes[i], false);
						String[] displayed = choices[1];
						int selItem = 0;
						for (int l = 0; l < displayed.length; l++) {
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
					}
					else if (dataTypes[i].equals("id")) {
						JTextField k = new JTextField(selected[i]);
						fields.add(k);
					} else {
						JLabel j = new JLabel(titles[i]);
						JTextField k = new JTextField(selected[i]);
						fields.add(k);
						add(j);
						add(k);
					}
				}
				//fields.get(0).setEnabled(false);
				setVisible(true);
			}
		}
		class linkTab extends JPanel {
			private DefaultTableModel linkTableModel;
			private JTableHCL linkTable;
			private String[][] columns;
			private int linkIndex;
			private int PKColumnIndex;
			private String link;
			public linkTab(String link, int linkIndex) {
				this.link = link;
				setLayout(new BorderLayout());
				this.linkIndex = linkIndex;
				linkTableData = sql.getStringTable(link, false);
				columns = ColumnNamer.getNamesWithOriginals(link, sql);
				linkTableModel = new DefaultTableModel(linkTableData, columns[1]);
				linkTable = new JTableHCL(linkTableModel);
				linkJTables.add(linkTable);
				linkTable.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						super.mouseClicked(e);
						if (e.getClickCount() == 2) {
							//Hackin all over the world
							if (linkTableData.length == 0) {
								linkTableData = sql.getStringTable(link, false);
								System.out.println("Getting new data");
							}
							System.out.println("Array: ");
							for (int i = 0; i < linkTableData.length; i++) {
								System.out.println("\t" + Arrays.toString(linkTableData[i]));
							}
							String[] selected = linkTableData[linkTable.getSelectedRow()];
							System.out.println("Selected line: " + Arrays.toString(selected));
							if (!(GenericList.this instanceof SubscriptionTab)) {
								inputBox edit = new inputBox(selected, false);
							}
						}
					}
				});
				JScrollPane scroll = new JScrollPane(linkTable);
				lowerButtons lower = new lowerButtons();
				add(scroll, BorderLayout.CENTER);
				add(lower, BorderLayout.SOUTH);
				//Finds column index for primary key
				for (int i = 0; i < columns[0].length; i++) {
					if (columns[0][i].equals(linkTables[linkIndex][1])) {
						PKColumnIndex = i;
					}
				}
			}

			class lowerButtons extends JPanel {
				public lowerButtons() {
					setLayout(new GridLayout(1, 2));
					JButton neue = new JButton("New...");
					neue.addActionListener(new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if (GenericList.this instanceof SubscriptionTab) {
								subscriptionInputBox box = new subscriptionInputBox();
							}
							else {
								inputBox input = new inputBox(null, true);
							}
						}
					});
					JButton delete = new JButton("Delete");
					delete.addActionListener(new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent e) {
							//Hackin all over the world
							if (linkTableData.length == 0) {
								linkTableData = sql.getStringTable(link, false);
								System.out.println("Getting new data");
							}
							int[] sel = linkTable.getSelectedRows();
							for (int i = 0; i < sel.length; i++) {
								//Do nothing if already deleted
								if (!linkTableData[sel[i]][0].contains(setGrey)) {
									//Greys out deleted links and adds them to linkTable for deletion
									//They are not actually deleted until the user hits "save"
									if (!(linkTableData[sel[i]][0].contains(setBold))) {
										//If it contains html it was added in this session - hackzor
										int[] link = {linkIndex, Integer.parseInt(linkTableData[sel[i]][PKColumnIndex])};
										removeLinks.add(link);
									}
									for (int j = 0; j < linkTableData[sel[i]].length; j++) {
										linkTableData[sel[i]][j] = setGrey + linkTableData[sel[i]][j] + endGrey;
									}
								}
								linkTableModel = new DefaultTableModel(linkTableData, columns[1]);
								linkTable.setModel(linkTableModel);
								linkTable.removeIDs();
							}
						}
					});
					add(neue);
					add(delete);
				}
			}
			class inputBox extends JFrame {
				public inputBox(String[] selectedLink, boolean newLink) {
					setSize((int) (x * 0.3), (int) (y * 0.2));
					setLayout(new GridLayout(3, 2));
					setLocationRelativeTo(null);
					setAlwaysOnTop(true);
					JLabel label = new JLabel("Item");
					JLabel amountLabel = new JLabel("Amount");
					String[] foo2 = new String[0];
					JComboBox<String> inpTemp = new JComboBox<>();
					//Gets choices and IDs for dropdown linkTable
					//linkTables = "Ingredients", "ingredient_id", "HCL_food_ingredient", "HCL_ingredient", "name"
					String choiceQuery = "SELECT DISTINCT " + linkTables[linkIndex][1] + ", " + linkTables[linkIndex][4] +
								" FROM " + linkTables[linkIndex][3] + " NATURAL JOIN " + linkTables[linkIndex][2] +
								" WHERE " + linkTables[linkIndex][1] + " NOT IN (SELECT " + linkTables[linkIndex][1] +
								" FROM " + linkTables[linkIndex][2] + " WHERE " + SqlColumnNames[0] +
								" = " + selected[0] + ")";
					System.out.println("Choice query: \n\t" + choiceQuery);
					String[] choice = sql.getColumn(choiceQuery, 1);
					JTextField amount = new JTextField();
					inpTemp = new JComboBox<>(choice);
					String[] choiceIDtemp = sql.getColumn(choiceQuery, 0);
					if (!newLink) {
						String existingChoices = "SELECT DISTINCT " + linkTables[linkIndex][1] + ", " + linkTables[linkIndex][4] +
								" FROM " + linkTables[linkIndex][3] + " NATURAL JOIN " + linkTables[linkIndex][2] +
								" WHERE " + linkTables[linkIndex][1] + " = " + selectedLink[PKColumnIndex] + " AND " + SqlColumnNames[0]
								+ " = " + selected[0];
						/*if (linkTables[linkIndex][3].equals(linkTables[linkIndex][2])) {
							existingChoices = "SELECT DISTINCT " + linkTables[linkIndex][1] + ", " + linkTables[linkIndex][4] +
									" FROM " + linkTables[linkIndex][3] + " WHERE " + linkTables[linkIndex][1] +
									" = " + selectedLink[PKColumnIndex] + " AND " + SqlColumnNames[0]
									+ " = " + selected[0];
						}*/
						String[] choicesExist = sql.getColumn(existingChoices, 1);
						String[] IDExist = sql.getColumn(existingChoices, 0);
						System.out.println("Query for choices: \n\t" + existingChoices);
						System.out.println("Choices for linkTable: \n\t"  + Arrays.toString(choicesExist));
						inpTemp = new JComboBox<>(choicesExist);
						choiceIDtemp = IDExist;
						inpTemp.setEnabled(false);
						amount.setText(selectedLink[linkTableModel.findColumn("Amount")]);
					}
					final String[] choiceID = choiceIDtemp;
					final JComboBox<String> input = inpTemp;
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
							String[] addedLink = new String[linkTableModel.getColumnCount()];
							addedLink[linkTableModel.findColumn("Amount")] = setBold + amount.getText() + endBold;
							String linkQuery = "SELECT * FROM " + linkTables[linkIndex][3] + " WHERE " + linkTables[linkIndex][1] + " = " + choiceID[input.getSelectedIndex()];
							System.out.println(linkQuery);
							String[] linker = sql.getRow(linkQuery);
							String[] clm = ColumnNamer.getNames(linkQuery, sql);
							for (int i = 0; i < linkTableModel.getColumnCount(); i++) {
								for (int j = 0; j < clm.length; j++) {
									if (linkTableModel.getColumnName(i).equals(clm[j])) {
										addedLink[i] = setBold + linker[j] + endBold;
									}
								}
							}
							addedLinks.add(addedLink);
							int[] inputTable = {linkIndex, Integer.parseInt(choiceID[input.getSelectedIndex()]), Integer.parseInt(amount.getText())};
							if (newLink) {
								createLinks.add(inputTable);
							}
							else if (!newLink) {
								changeLinks.add(inputTable);
							}
							if (!newLink) {
								linkTableData[linkTable.getSelectedRow()] = addedLink;
							}
							else {
								//Hackin all over the world
								if (linkTableData.length == 0) {
									linkTableData = sql.getStringTable(link, false);
									System.out.println("Getting new data");
								}
								String[][] newData = new String[linkTableData.length + 1][];
								for (int i = 0; i < linkTableData.length; i++) {
									newData[i] = linkTableData[i];
								}
								System.out.println("Creating new array with length: " + newData.length);
								newData[linkTableData.length] = addedLink;
								linkTableData = newData;
							}
							linkTableModel = new DefaultTableModel(linkTableData, columns[1]);
							linkTable.setModel(linkTableModel);
							linkTable.removeIDs();
							dispose();
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
			class subscriptionInputBox extends JFrame {
				private datePane pane;
				public subscriptionInputBox() {
					setTitle("Add date");
					setLayout(new GridLayout(2, 1));
					pane = new datePane();
					buttons button = new buttons();
					add(pane);
					add(button);
					setSize((int)(x * 0.3), (int)(y * 0.15));
					setLocationRelativeTo(null);
					setAlwaysOnTop(true);
					setVisible(true);
				}
				private class buttons extends JPanel {
					public buttons() {
						setLayout(new GridLayout(1, 2));
						JButton save = new JButton("Save");
						JButton cancel = new JButton("Cancel");
						save.addActionListener(new AbstractAction() {
							@Override
							public void actionPerformed(ActionEvent e) {
								String[][] foo = new String[linkTableData.length + 1][];
								String[] fii = new String[linkTableData[0].length];
								fii[linkTableModel.findColumn("Date")] = setBold + pane.getDate() + endBold;
								foo[linkTableData.length] = fii;
								for (int i = 0; i < linkTableData.length; i++) {
									foo[i] = linkTableData[i];
								}
								linkTableData = foo;
								linkTableModel = new DefaultTableModel(foo, columns[1]);
								linkTable.setModel(linkTableModel);
								linkTable.removeIDs();
								dispose();
							}
						});
						cancel.addActionListener(new AbstractAction() {
							@Override
							public void actionPerformed(ActionEvent e) {
								dispose();
							}
						});
						add(save);
						add(cancel);
					}
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
						DefaultTableModel searchTM = new DefaultTableModel(searchTable, titles);
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