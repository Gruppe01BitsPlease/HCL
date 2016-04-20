package clientGUI;

import backend.LinkManager;
import backend.SQL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Jens on 15-Apr-16.
 */
abstract class Stuff {
	public static Dimension getEditBoxSize() {
		Dimension dim = new Dimension((int) (GenericList.x * 0.3), (int) (GenericList.y * 0.3));
		return dim;
	}
	//Finds the index in the list from a search key
	public static int findIndexOf(String[][] searchArrays, String search, int column) {
		int ret = -1;
		for (int i = 0; i < searchArrays.length; i++) {
			if (searchArrays[i][column].equals(search)) {
				ret = i;
			}
		}
		if (ret == -1) {
			System.out.println("Not found");
		}
		else {
			System.out.println("Selected row index: " + ret);
		}
		return ret;
	}
	public static int findIndexOf(String[] searchArray, String search) {
		int ret = -1;
		for (int i = 0; i < searchArray.length; i++) {
			if (searchArray[i].equals(search)) {
				ret = i;
			}
		}
		return ret;
	}
	public static String setBold() {
		return "<html><b>";
	}
	public static String endBold() {
		return "</b></html>";
	}
	public static String setGrey() {
		return "<html><p style=\"color:#808080\">";
	}
	public static String endGrey() {
		return "</p></html>";
	}
}
class datePane extends JPanel {
	JComboBox<String> yearBox;
	JComboBox<String> monthBox;
	JTextField dayField;
	public datePane(String date) {
		//2014-01-31
		String[] years = new String[5];
		LocalDate now = LocalDate.now();
		int year = now.getYear();
		for (int i = 0; i < years.length; i++) {
			years[i] = Integer.toString(year + i);
		}
		yearBox = new JComboBox<>(years);
		String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec" };
		monthBox = new JComboBox<>(months);
		setLayout(new GridLayout(1, 3));
		if (date != null && !(date.equals(""))) {
			if (date.length() == 10) {
				String selyear = date.substring(0, 4);
				if (Integer.parseInt(selyear) < year) {
					yearBox.addItem(selyear);
				}
				yearBox.setSelectedItem(selyear);
				//System.out.println(year.getText());
				int selmonth = Integer.parseInt(date.substring(5, 7));
				monthBox.setSelectedIndex(selmonth - 1);
				//System.out.println(month.getText());
				dayField = new JTextField(date.substring(8, 10));
				//System.out.println(day.getText());
			}
			else {
				System.out.println("Date format is wrong! GenericList.datePane");
			}
		}
		else {
			dayField = new JTextField("");
		}
		add(yearBox);
		add(monthBox);
		add(dayField);
	}
	public datePane() {
		//2014-01-31
		String[] years = new String[5];
		LocalDate now = LocalDate.now();
		int year = now.getYear();
		for (int i = 0; i < years.length; i++) {
			years[i] = Integer.toString(year + i);
		}
		yearBox = new JComboBox<>(years);
		String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec" };
		monthBox = new JComboBox<>(months);
		setLayout(new GridLayout(1, 3));
		dayField = new JTextField("");
		add(yearBox);
		add(monthBox);
		add(dayField);
	}
	public String getDate() {
		String year = (String) yearBox.getSelectedItem();
		String month = Integer.toString(monthBox.getSelectedIndex() + 1);
		String day = dayField.getText();
		if (month.length() < 2) {
			String foo = "0";
			foo += month;
			month = foo;
		}
		return year + "-" + month + "-" + day;
	}
	public void setDate(String date) {
		String year = (date.substring(0, 4));
		String month = (date.substring(5, 7));
		String day = (date.substring(8, 10));
		yearBox.setSelectedItem(year);
		monthBox.setSelectedIndex(Integer.parseInt(month) - 1);
		dayField.setText(day);
	}
	public void setEnabled(boolean enable) {
		yearBox.setEnabled(enable);
		monthBox.setEnabled(enable);
		dayField.setEnabled(enable);
	}
}
class editFields extends JPanel {
	private ArrayList<JComponent> fields = new ArrayList<>();
	private String[][] comboBoxChoices;
	private String[] selected;
	private String[] dataTypes;
	private boolean newEntry;
	private SQL sql;
	public editFields(String[] titles, String[] selected, boolean newEntry, String[] FKs, SQL sql) {
		this.selected = selected;
		this.newEntry = newEntry;
		this.sql = sql;
		dataTypes = DataTyper.getDataTypes(titles);
		if (FKs != null && FKs.length > 0) {
			dataTypes[Integer.parseInt(FKs[1])] = FKs[0];
		}
		int length = selected.length + 1;
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
				comboBoxChoices = choices;
				JComboBox<String> k = new JComboBox<>(comboBoxChoices[1]);
				if (!newEntry) {
					k.setSelectedItem(comboBoxChoices[1][Stuff.findIndexOf(comboBoxChoices[0], selected[i])]);
					k.setEnabled(false);
				}
				fields.add(k);
				add(j);
				add(k);
			}
			else if (dataTypes[i].equals("id") || dataTypes[i].equals("active")) {
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
	public ArrayList<JComponent> getFields() {
		return fields;
	}
	public String[][] getComboBoxChoices() {
		return comboBoxChoices;
	}
	public String[] getNewValues() {
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
				String selID = comboBoxChoices[0][cmb.getSelectedIndex()];
										/*String sel = (String) cmb.getSelectedItem();
										String[] chosen = sel.split(",");*/
				newValues[i] = selID;
				System.out.println(newValues[i]);
			}
		}
		return newValues;
	}
}
class linkTab extends JPanel {
	private ArrayList<int[]> removeLinks = new ArrayList<>();
	private ArrayList<String[]> addedLinks = new ArrayList<>();
	private ArrayList<int[]> createLinks = new ArrayList<>();
	private ArrayList<int[]> changeLinks = new ArrayList<>();
	private String[][] linkTableData;
	private DefaultTableModel linkTableModel;
	private JTableHCL linkTable;
	private String[][] titles;
	private int linkIndex;
	private int PKColumnIndex;
	private String linkQuery;
	private SQL sql;
	private String[] link;
	private int selectedID;
	private String primaryColumn;
	private boolean newEntry;
	public linkTab(String[] link, String primaryColumn, int selectedID, SQL sql, boolean newEntry) {
		this.newEntry = newEntry;
		this.primaryColumn = primaryColumn;
		this.sql = sql;
		this.link = link;
		this.selectedID = selectedID;
		//Link = "Ingredients", "ingredient_id", "HCL_food_ingredient", "HCL_ingredient", "name"
		//SELECT HCL_ingredient.ingredient_id, name, number FROM HCL_ingredient JOIN HCL_food_ingredient ON
		// (HCL_ingredient.ingredient_id = HCL_food_ingredient.ingredient_id) WHERE food_id =200;
		this.linkQuery = "SELECT " + link[3] + "." + link[1] +  ", " + link[4] + ", number FROM " + link[2] + " " +
				"JOIN " + link[3] + " ON (" + link[3] + "." + link[1] + " = " + link[2] + "." + link[1] + ") WHERE " +
				primaryColumn + " = " + selectedID + " AND " + link[2] + ".active = 1";
		System.out.println("Link query: " + linkQuery);
		titles = ColumnNamer.getNamesWithOriginals(linkQuery, sql);
		setLayout(new BorderLayout());
		this.linkIndex = linkIndex;
		linkTableData = sql.getStringTable(linkQuery, false);
		//System.out.println("Link table data: " + Arrays.toString(linkTableData[0]));
		linkTableModel = new DefaultTableModel(linkTableData, titles[1]);
		linkTable = new JTableHCL(linkTableModel);
		linkTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (e.getClickCount() == 2) {
					//Hackin all over the world
					if (linkTableData.length == 0) {
						linkTableData = sql.getStringTable(linkQuery, false);
						System.out.println("Getting new data");
					}
					System.out.println("Array: ");
					for (int i = 0; i < linkTableData.length; i++) {
						System.out.println("\t" + Arrays.toString(linkTableData[i]));
					}
					String[] selected = linkTableData[linkTable.getSelectedRow()];
					System.out.println("Selected line: " + Arrays.toString(selected));
					inputBox edit = new inputBox(selected, false);
				}
			}
		});
		JScrollPane scroll = new JScrollPane(linkTable);
		lowerButtons lower = new lowerButtons();
		add(scroll, BorderLayout.CENTER);
		add(lower, BorderLayout.SOUTH);
		//Finds column index for primary key
		PKColumnIndex = 0;
		linkTable.removeIDs();
	}
	class lowerButtons extends JPanel {
		public lowerButtons() {
			setLayout(new GridLayout(1, 2));
			JButton neue = new JButton("New...");
			neue.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					inputBox input = new inputBox(null, true);
				}
			});
			JButton delete = new JButton("Delete");
			delete.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//Hackin all over the world
					if (linkTableData.length == 0) {
						linkTableData = sql.getStringTable(linkQuery, false);
						System.out.println("Getting new data");
					}
					int[] sel = linkTable.getSelectedRows();
					for (int i = 0; i < sel.length; i++) {
						//Do nothing if already deleted
						if (!linkTableData[sel[i]][0].contains(Stuff.setGrey())) {
							//Greys out deleted links and adds them to linkTable for deletion
							//They are not actually deleted until the user hits "save"
							if (!(linkTableData[sel[i]][0].contains(Stuff.setBold()))) {
								//If it contains html it was added in this session - hackzor
								int[] link = {linkIndex, Integer.parseInt(linkTableData[sel[i]][PKColumnIndex])};
								removeLinks.add(link);
							}
							for (int j = 0; j < linkTableData[sel[i]].length; j++) {
								linkTableData[sel[i]][j] = Stuff.setGrey() + linkTableData[sel[i]][j] + Stuff.endGrey();
							}
						}
						linkTableModel = new DefaultTableModel(linkTableData, titles[1]);
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
			setSize((int) (GenericList.x * 0.3), (int) (GenericList.y * 0.2));
			setLayout(new GridLayout(3, 2));
			setLocationRelativeTo(null);
			setAlwaysOnTop(true);
			JLabel label = new JLabel("Item");
			JLabel amountLabel = new JLabel("Amount");
			String[] foo2 = new String[0];
			JComboBox<String> inpTemp = new JComboBox<>();
			//Gets choices and IDs for dropdown linkTable
			//linkTables = "Ingredients", "ingredient_id", "HCL_food_ingredient", "HCL_ingredient", "name"

			//SELECT DISTINCT ingredient_id, name FROM HCL_ingredient WHERE ingredient_id NOT IN
			// (SELECT ingredient_id FROM HCL_food_ingredient WHERE food_id = 200 AND active = 1)
			// AND HCL_ingredient.active = 1

			String choiceQuery = "SELECT DISTINCT " + link[1] + ", " + link[4] + " FROM " + link[3] +
					" WHERE " + link[1] + " NOT IN (SELECT " + link[1] + " FROM " + link[2]
					+ " WHERE " + primaryColumn + " = " + selectedID + " AND active = 1) AND " + link[3] + ".active = 1";
			System.out.println("Choice query: \n\t" + choiceQuery);
			String[] choice = sql.getColumn(choiceQuery, 1);
			JTextField amount = new JTextField();
			inpTemp = new JComboBox<>(choice);
			String[] choiceIDtemp = sql.getColumn(choiceQuery, 0);
			if (!newLink) {
				String existingChoices = "SELECT DISTINCT " + link[1] + ", " + link[4] +
						" FROM " + link[3] + " NATURAL JOIN " + link[2] +
						" WHERE " + link[1] + " = " + selectedLink[0] + " AND " + primaryColumn
						+ " = " + selectedID;
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
					addedLink[linkTableModel.findColumn("Amount")] = Stuff.setBold() + amount.getText() + Stuff.endBold();
					String linkQuery = "SELECT * FROM " + link[3] + " WHERE " + link[1] + " = " + choiceID[input.getSelectedIndex()];
					System.out.println(linkQuery);
					String[] linker = sql.getRow(linkQuery);
					String[] clm = ColumnNamer.getNames(linkQuery, sql);
					for (int i = 0; i < linkTableModel.getColumnCount(); i++) {
						for (int j = 0; j < clm.length; j++) {
							if (linkTableModel.getColumnName(i).equals(clm[j])) {
								addedLink[i] = Stuff.setBold() + linker[j] + Stuff.endBold();
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
							linkTableData = sql.getStringTable(linkTab.this.linkQuery, false);
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
					linkTableModel = new DefaultTableModel(linkTableData, titles[1]);
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
	public ArrayList<int[]> getRemoveLinks() {
		return removeLinks;
	}
	public ArrayList<int[]> getCreateLinks() {
		return createLinks;
	}
	public ArrayList<int[]> getChangeLinks() {
		return changeLinks;
	}
	public void generate() {
		if (removeLinks.size() > 0) {
			LinkManager linkMng = new LinkManager(sql);
			for (int[] i : removeLinks) {
				int a = linkMng.delete(link[2], primaryColumn, link[1], selectedID, i[1]);
				System.out.println("Remove link: " + link[2] + " - " +  primaryColumn + " - " + link[1] + " - " + selectedID + " - " + i[1]);
				System.out.println("Delete result :" + a);
			}
		}
		int pk = sql.getLastID();
		System.out.println("New primary key: " + pk);
		if (createLinks.size() > 0) {
			//Saves to link tables if links have been created
			LinkManager linkMng = new LinkManager(sql);
			System.out.println("Add link: " + Arrays.toString(createLinks.get(0)));
			if (newEntry) {
				System.out.println("NEW LINK NEW ENTRY!");
				for (int[] i : createLinks) {
					linkMng.generate(link[2], primaryColumn, link[1], pk, i[1], i[2]);
				}
			} else {
				for (int[] i : createLinks) {
					linkMng.generate(link[2], primaryColumn, link[1], selectedID, i[1], i[2]);
				}
			}
		}
		if (changeLinks.size() > 0) {
			//Changes link tables if needed
			LinkManager linkMng = new LinkManager(sql);
			System.out.println("Changelink: " + Arrays.toString(changeLinks.get(0)));
			//int[] inputTable = {linkIndex, Integer.parseInt(choiceID[input.getSelectedIndex()]), Integer.parseInt(amount.getText())};
			for (int[] i : changeLinks) {
				linkMng.editNumber(link[2], primaryColumn, link[1], selectedID, i[1], i[2]);
			}
		}
	}
}
