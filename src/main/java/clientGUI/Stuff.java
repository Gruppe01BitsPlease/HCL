package clientGUI;

import backend.LinkManager;
import backend.SQL;
import backend.UserManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static java.time.temporal.ChronoField.YEAR;
/**
 * Various useful methods
 */
class Stuff {

    private Stuff(){} // Can't be instansized
	//Used to size a window relative to the main window, which is sized relative to the screen
	static Dimension getWindowSize(double factorX, double factorY) {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) (screen.width * 0.75);
		int y = (int) (screen.height * 0.75);
		return new Dimension((int) (x * factorX), (int) (y * factorY));
	}
	//Finds the index in the list from a search key
	static int findIndexOf(String[][] searchArrays, String search, int column) {
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
	static int findIndexOf(String[] searchArray, String search) {
		int ret = -1;
		for (int i = 0; i < searchArray.length; i++) {
			if (searchArray[i].equals(search)) {
				ret = i;
			}
		}
		return ret;
	}
	static String bold(String text) {
		return setBold() + text + endBold();
	}
	static String grey(String text){return setGrey()+text+endGrey();}
	static String setBold() {return "<html><b>";	}
	static String endBold() {
		return "</b></html>";
	}
	static String setGrey() {
		return "<html><p style=\"color:#808080\">";
	}
	static String endGrey() {
		return "</p></html>";
	}
	static String setBlue() {
		return "<html><p style=\"color:#0000FF\">";
	}
	static String endBlue() {
		return "</p></html>";
	}
	static String removeHTML(String input) {
		String ret = input;
		ret = ret.replaceAll(setBold(), "");
		ret = ret.replaceAll(endBold(), "");
		ret = ret.replaceAll(setGrey(), "");
		ret = ret.replaceAll(endGrey(), "");
		ret = ret.replaceAll(setBlue(), "");
		ret = ret.replaceAll(endBlue(), "");
		return ret;
	}
    public static boolean isBold(String string){
        return string.contains(setBold()) && string.contains(endBold());
    }
    public static boolean isGrey(String string){
        return string.contains(setGrey()) && string.contains(endGrey());
    }
    /**
     * Checks if the inputted is in the array, and if it is; checks if it is greyed out.
     */
    static boolean isGrayedInArray(String[][] array, String string){

        for(String[] row : array){
            for(String element : row){
                if (element != null && element.equals(grey(string))) return true;
            }
        }
        return false;
    }
    public static boolean isBoldInArray(String[][] array, String string){
        // System.out.println(Arrays.deepToString(array)+" - "+string);
        if(array == null || string == null) return false;
        for(String[] row : array){
            for(String element : row){
                if (element != null && element.contains(string) && element.contains(bold(string))) return true;
            }
        }
        return false;
    }
    static boolean arrayContains(String[] array, String string){
        if(array == null || string == null) return false;

        for(String element : array){
            if(element != null && element.equals(string)) return true;
        }
        return false;
    }
    public static boolean twoDArrayContains(String[][] array, String string){
        if(array == null || string == null) return false;
        for(String[] row : array){
            if(arrayContains(row,string)) return true;
        }
        return false;
    }
}
/**
 * This is our date entry panel, used everywhere you enter a date
 */
class DatePane extends JPanel {
	private JComboBox<String> yearBox;
	private JComboBox<String> monthBox;
	private JComboBox<Integer> dayBox;
	private LocalDate date;
	public DatePane(String dateInput) {
		//2014-01-31
		if (dateInput != null) {
			date = LocalDate.parse(dateInput);
		}
		else {
			date = LocalDate.now();
		}
		String[] years = new String[5];
		LocalDate now = LocalDate.now();
		int year = now.getYear();
		for (int i = 0; i < years.length; i++) {
			years[i] = Integer.toString(year + i);
		}
		yearBox = new JComboBox<>(years);
		Integer[][] daysOfMonths = new Integer[12][];
		for (int i = 0; i < daysOfMonths.length; i++) {
			int yearSel = Integer.parseInt((String)(yearBox.getSelectedItem()));
			LocalDate date = LocalDate.of(yearSel, i+1, 1);
			daysOfMonths[i] = new Integer[date.getMonth().maxLength()];
			for (int j = 0; j < daysOfMonths[i].length; j++) {
				daysOfMonths[i][j] = j + 1;
			}
		}
		String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec" };
		monthBox = new JComboBox<>(months);
		dayBox = new JComboBox<>(daysOfMonths[monthBox.getSelectedIndex() + 1]);
		ItemListener yearListener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				for (int i = 0; i < daysOfMonths.length; i++) {
					daysOfMonths[i] = new Integer[LocalDate.of(Integer.parseInt((String) yearBox.getSelectedItem()), i + 1, 1).getMonth().maxLength()];
					for (int j = 0; j < daysOfMonths[i].length; j++) {
						daysOfMonths[i][j] = j + 1;
					}
				}
			}
		};
		yearBox.addItemListener(yearListener);
		ItemListener monthListener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				JComboBox<Integer> newBox = new JComboBox<>(daysOfMonths[monthBox.getSelectedIndex()]);
				dayBox.setModel(newBox.getModel());
			}
		};
		monthBox.addItemListener(monthListener);
		setLayout(new GridLayout(1, 3));
		if (date != null) {
			String selyear = Integer.toString(date.get(YEAR));
			if (Integer.parseInt(selyear) < year) {
				yearBox.addItem(selyear);
			}
			yearBox.setSelectedItem(selyear);
			yearListener.itemStateChanged(new ItemEvent(yearBox, 0, null, ItemEvent.SELECTED));
			//System.out.println(year.getText());
			int selmonth = date.getMonthValue();
			monthBox.setSelectedIndex(selmonth - 1);
			monthListener.itemStateChanged(new ItemEvent(monthBox, 0, null, ItemEvent.SELECTED));
			//System.out.println(month.getText());
			int selday = date.getDayOfMonth();
			dayBox.setSelectedIndex(selday - 1);
			//System.out.println(day.getText());
			System.out.println(selyear + " " + selmonth);
		}
		add(yearBox);
		add(monthBox);
		add(dayBox);
	}
	public String getDate() {
		String year = ((String)(yearBox.getSelectedItem()));
		String month = Integer.toString(monthBox.getSelectedIndex() + 1);
		if (month.length() < 2) {
			month = "0" + month;
		}
		String day = Integer.toString((Integer)dayBox.getSelectedItem());
		if (day.length() < 2) {
			day = "0" + day;
		}
		return year + "-" + month + "-" + day;
	}
	public String getYear() {
		return (String) yearBox.getSelectedItem();
	}
	public String getMonth() {
		return Integer.toString(monthBox.getSelectedIndex() + 1);
	}
	public String getDay() {
		return Integer.toString((Integer)dayBox.getSelectedItem());
	}
	public void addDays(int days) {
		date = date.plusDays(days);
		yearBox.setSelectedItem(date.getYear());
		monthBox.setSelectedIndex(date.getMonthValue() - 1);
		dayBox.setSelectedIndex(date.getDayOfMonth() - 1);
	}
	public void addMonths(int months) {
		date = date.plusMonths(months);
		yearBox.setSelectedItem(date.getYear());
		monthBox.setSelectedIndex(date.getMonthValue() - 1);
		dayBox.setSelectedIndex(date.getDayOfMonth() - 1);	}
	public void setDate(String dateIn) {
		date = LocalDate.parse(dateIn);
		yearBox.setSelectedItem(date.getYear());
		monthBox.setSelectedIndex(date.getMonthValue() - 1);
		dayBox.setSelectedIndex(date.getDayOfMonth() - 1);
	}
	public void setEnabled(boolean enable) {
		yearBox.setEnabled(enable);
		monthBox.setEnabled(enable);
		dayBox.setEnabled(enable);
	}
}
/**
 * This class generates edit fields for edit windows
 */
class EditFields extends JPanel {
	private ArrayList<JComponent> fields = new ArrayList<>();
	private String[][] comboBoxChoices;
	private String[] selected;
	public EditFields(String[] titles, String[] selected, boolean newEntry, String[] FKs, SQL sql) {
		this.selected = selected;
		DataTyper.DataType[] dataTypes = DataTyper.getDataTypes(titles);
		String foreignSelect = "";
		if (FKs != null && FKs.length > 0) {
			dataTypes[Integer.parseInt(FKs[1])] = DataTyper.DataType.FOREIGN;
			foreignSelect = FKs[0];
		}
		setLayout(new GridLayout(10, 2));
		for (int i = 0; i < dataTypes.length; i++) {
			if (dataTypes[i] == DataTyper.DataType.BOOLEAN) {
				JLabel j = new JLabel(titles[i]);
				JCheckBox k = new JCheckBox();
				if (selected[i] != null && selected[i].equals("1")) {
					k.setSelected(true);
				}
				fields.add(k);
				add(j);
				add(k);
			} else if (dataTypes[i] == DataTyper.DataType.DATE) {
				JLabel j = new JLabel(titles[i]);
				DatePane k = new DatePane(selected[i]);
				fields.add(k);
				add(j);
				add(k);
			} else if (dataTypes[i] == DataTyper.DataType.CURDATE) {
				JLabel j = new JLabel(titles[i]);
				DatePane k = new DatePane(selected[i]);
				if (newEntry) {
					LocalDate now = LocalDate.now();
					String date = now.toString();
					k = new DatePane(date);
				}
				fields.add(k);
				add(j);
				add(k);
			} else if (dataTypes[i] == DataTyper.DataType.FOREIGN) {
				JLabel j = new JLabel("Customer");
				comboBoxChoices = new String[][]{sql.getColumn(foreignSelect, 0), sql.getColumn(foreignSelect, 1)};
				JComboBox<String> k = new JComboBox<>(comboBoxChoices[1]);
				if (!newEntry) {
					k.setSelectedItem(comboBoxChoices[1][Stuff.findIndexOf(comboBoxChoices[0], selected[i])]);
					k.setEnabled(false);
				}
				fields.add(k);
				add(j);
				add(k);
			}
			else if (dataTypes[i] == DataTyper.DataType.ID || dataTypes[i] == DataTyper.DataType.ACTIVE) {
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
	public String[] getNewValues() {
		//returns the new values as an array, should be in same order as columns in jtable (including hidden ones!)
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
			} else if (fields.get(i) instanceof DatePane) {
				DatePane dtp = (DatePane) fields.get(i);
				newValues[i] = dtp.getDate();
			} else if (fields.get(i) instanceof JComboBox) {
				JComboBox cmb = (JComboBox) fields.get(i);
				String selID = comboBoxChoices[0][cmb.getSelectedIndex()];
				newValues[i] = selID;
				System.out.println(newValues[i]);
			}
		}
		return newValues;
	}
}
/**
 * This class creates tabs where relations are linked by link tables
 */
class LinkTab extends JPanel {
	private ArrayList<int[]> removeLinks = new ArrayList<>();
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
	public LinkTab(String[] link, String primaryColumn, int selectedID, SQL sql, boolean newEntry) {

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

					InputBox edit = new InputBox(selected, false);
				}
			}
		});
		JScrollPane scroll = new JScrollPane(linkTable);
		LowerButtons lower = new LowerButtons();
		add(scroll, BorderLayout.CENTER);
		add(lower, BorderLayout.SOUTH);
		//Finds column index for primary key
		PKColumnIndex = 0;
		linkTable.removeIDs();
	}
	class LowerButtons extends JPanel {
		public LowerButtons() {
			setLayout(new GridLayout(1, 2));
			JButton neue = new JButton("New...");
			neue.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					InputBox input = new InputBox(null, true);
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
								linkTableData[sel[i]][j] = Stuff.setGrey() + Stuff.removeHTML(linkTableData[sel[i]][j]) + Stuff.endGrey();
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
	class InputBox extends JFrame {
		public InputBox(String[] selectedLink, boolean newLink) {
			setSize(Stuff.getWindowSize(0.3, 0.2));
			setResizable(false);
			setLayout(new GridLayout(3, 2));
			setLocationRelativeTo(null);
			setAlwaysOnTop(true);
			JLabel label = new JLabel("Item");
			JLabel amountLabel = new JLabel("Amount");
			JComboBox<String> inpTemp = new JComboBox<>();
			String choiceQuery = "SELECT DISTINCT " + link[1] + ", " + link[4] + " FROM " + link[3] +
					" WHERE " + link[1] + " NOT IN (SELECT " + link[1] + " FROM " + link[2]
					+ " WHERE " + primaryColumn + " = " + selectedID + " AND active = 1) AND " + link[3] + ".active = 1";
			System.out.println("Choice query: \n\t" + choiceQuery);
			String[] choice = sql.getColumn(choiceQuery, 1);
			JTextField amount = new JTextField();
			inpTemp = new JComboBox<>(choice);
			String[] choiceIDtemp = sql.getColumn(choiceQuery, 0);
			if (!newLink) {
				int nameColumn = -1;
				int idColumn = -1;
				for (int i = 0; i < titles[0].length; i++) {
					if (titles[0][i].contains(link[4])) {
						nameColumn = i;
					}
					if (titles[0][i].contains(link[1])) {
						idColumn = i;
					}
				}
				String[] choicesExist = { Stuff.removeHTML(linkTableData[linkTable.getSelectedRow()][nameColumn]) };
				String[] IDExist = { Stuff.removeHTML(linkTableData[linkTable.getSelectedRow()][idColumn]) };
				System.out.println("Choices for linkTable: \n\t"  + Arrays.toString(choicesExist));
				inpTemp = new JComboBox<>(choicesExist);
				choiceIDtemp = IDExist;
				inpTemp.setEnabled(false);
				amount.setText(Stuff.removeHTML(selectedLink[linkTableModel.findColumn("Amount")]));
			}
			final String[] choiceID = choiceIDtemp;
			System.out.println("Choice IDs: " + Arrays.toString(choiceID));
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
					//Link: "Orders", "order_id", "HCL_order_food", "HCL_order", "adress"
					String[] addedLink = new String[linkTableModel.getColumnCount()];
					addedLink[linkTableModel.findColumn("Amount")] = Stuff.bold(amount.getText());
					addedLink[linkTableModel.findColumn(ColumnNamer.getName(link[1]))] = Stuff.bold(choiceID[input.getSelectedIndex()]);
					addedLink[linkTableModel.findColumn(ColumnNamer.getName(link[4]))] = Stuff.bold((String)input.getSelectedItem());
					int[] inputTable = {linkIndex, Integer.parseInt(choiceID[input.getSelectedIndex()]), Integer.parseInt(amount.getText())};
					if (newLink) {
						createLinks.add(inputTable);
					}
					else {
						changeLinks.add(inputTable);
						linkTableData[linkTable.getSelectedRow()] = addedLink;
					}
					if (newLink) {
						//Hackin all over the world
						if (linkTableData.length == 0) {
							linkTableData = sql.getStringTable(LinkTab.this.linkQuery, false);
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
	public void generate() {
		if (newEntry) {
			selectedID = sql.getLastID();
		}
		for (int[] rem : removeLinks) {
			System.out.println("Remove links: "+Arrays.toString(rem));
		}
		for (int[] create : createLinks) {
			System.out.println("Create links: "+Arrays.toString(create));
		}
		for (int[] change : changeLinks) {
			System.out.println("Change links: " + Arrays.toString(change));
		}
		for (int i = 0; i < linkTableData.length; i++) {
			//link: "Foods", "food_id", "HCL_food_ingredient", "HCL_food", "name"
			int numberColumn = -1;
			int IDcolumn = -1;
			LinkManager mng = new LinkManager(sql);
			for (int j = 0; j < titles[0].length; j++) {
				if (titles[0][j].contains("number")) {
					numberColumn = j;
				}
				if (titles[0][j].contains(link[1])) {
					IDcolumn = j;
				}
			}
			if (linkTableData[i][1].contains(Stuff.setBold())) {
				int genID = Integer.parseInt(Stuff.removeHTML(linkTableData[i][IDcolumn]));
				int amount = Integer.parseInt(Stuff.removeHTML(linkTableData[i][numberColumn]));
				System.out.println("Generate: " + link[2]+"-"+primaryColumn+"-"+link[1]+"-"+selectedID+"-"+genID+"-"+amount);
				mng.generate(link[2], primaryColumn, link[1], selectedID, genID, amount);
			}
			if (linkTableData[i][1].contains(Stuff.setGrey())) {
				int delID = Integer.parseInt(Stuff.removeHTML(linkTableData[i][IDcolumn]));
				mng.delete(link[2], primaryColumn, link[1], selectedID, delID);
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
/**
 * The menu for editing employees in the database
 */
class UserEditMenu extends JFrame {
	private SQL sql;
	private String userName;
	private JPasswordField passField;
	private JButton passButton;
	private JPanel tab;
	public UserEditMenu(String id, SQL sql, int rolle, JPanel tab) {
		this.tab = tab;
		this.sql = sql;
		setAlwaysOnTop(true);
		String select = "SELECT user_name FROM HCL_user WHERE user_id = " + id;
		userName = sql.getRow(select)[0];
		passButton = new JButton("New password");
		passButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int choice = JOptionPane.showConfirmDialog(UserEditMenu.this, "Generate new password for this user?", "New password", JOptionPane.YES_NO_OPTION);
				if (choice == 0) {
					UserManager mng = new UserManager(sql);
					String newPass = mng.generateRandomPassword(userName);
					JOptionPane.showMessageDialog(UserEditMenu.this, "The new password is: \"" + newPass + "\"");
				}
			}
		});
		EditMenu menu = new EditMenu(rolle);
	}
	public UserEditMenu(SQL sql, int rolle, JPanel tab) {
		this.tab = tab;
		this.sql = sql;
		setSize(Stuff.getWindowSize(0.3,0.2));
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		JLabel loginAgain = new JLabel("Please log in again");
		JPanel namePassPanel = new JPanel();
		namePassPanel.setLayout(new GridLayout(2,2));
		JLabel userNameLabel = new JLabel("User name:");
		JLabel passLabel = new JLabel("Password:");
		JTextField userNameField = new JTextField();
		passField = new JPasswordField();
		namePassPanel.add(userNameLabel);
		namePassPanel.add(userNameField);
		namePassPanel.add(passLabel);
		namePassPanel.add(passField);
		JPanel okCancel = new JPanel(new GridLayout(1, 2));
		JButton okButton = new JButton("OK");
		JButton cancelButton = new JButton("Cancel");
		Action okAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UserManager mng = new UserManager(sql);
				int res = mng.logon(userNameField.getText(), new String(passField.getPassword()));
				if (res >= 0) {
					userName = userNameField.getText();
					EditMenu menu = new EditMenu(res);
					dispose();
				}
				else {
					JOptionPane.showMessageDialog(null, "Wrong user name or password");
				}
			}
		};
		passField.addActionListener(okAction);
		okButton.addActionListener(okAction);
		cancelButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		okCancel.add(okButton);
		okCancel.add(cancelButton);
		add(loginAgain, BorderLayout.NORTH);
		add(namePassPanel, BorderLayout.CENTER);
		add(okCancel, BorderLayout.SOUTH);
		setVisible(true);
		passButton = new JButton("Change password...");
		passButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PassBox box = new PassBox();
			}
		});
	}
	private class EditMenu extends JFrame {
		public EditMenu(int rolle) {
			setResizable(false);
			setSize(Stuff.getWindowSize(0.5,0.5));
			setLocationRelativeTo(null);
			String selectQuery = "SELECT user_name, user_firstname, user_lastname, user_email, user_tlf, " +
					"user_adress, user_postnr, user_start FROM HCL_user WHERE user_name = '" + userName + "'";
			System.out.println("User edit select query = " + selectQuery);
			setLayout(new BorderLayout());
			String[] selectedUser = sql.getRow(selectQuery);
			String[][] titles = ColumnNamer.getNamesWithOriginals(selectQuery, sql);
			EditFields fields = new EditFields(titles[1], selectedUser, false, null, sql);
			fields.getFields().get(0).setEnabled(false);
			if (rolle != 0) {
				fields.getFields().get(7).setEnabled(false);
			}
			add(fields, BorderLayout.CENTER);
			JPanel saveCancel = new JPanel(new GridLayout(1, 2));
			JButton save = new JButton("Save");
			JButton cancel = new JButton("Cancel");
			JButton roleButton = new JButton("Change access level...");
			roleButton.addActionListener(e -> {
				LoginWindow win = new LoginWindow(userName);
			});
			save.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int sure = JOptionPane.showConfirmDialog(EditMenu.this, "Are you sure?", "Edit user", JOptionPane.YES_NO_OPTION);
					if (sure == 0) {
						String[] newValues = fields.getNewValues();
						DataTyper.DataType[] dataTypes = DataTyper.getDataTypes(titles[1]);
						boolean success = true;
						for (int i = 0; i < newValues.length; i++) {
							if (!(newValues[i] == null || newValues[i].equals(""))) {
								boolean valid = true;
								if (dataTypes[i] == DataTyper.DataType.INT) {
									try {
										Integer.parseInt(newValues[i]);
									} catch (Exception k) {
										valid = false;
									}
								}
								if (valid && (selectedUser[i] == null || !(selectedUser[i].equals(newValues[i])))) {
									sql.update("HCL_user", titles[0][i], "user_name", userName, newValues[i]);
								} else if (!valid) {
									JOptionPane.showMessageDialog(EditMenu.this, "Please enter a valid number in field: " + titles[1][i]);
									success = false;
								}
							}
						}
						if (success ) {
							dispose();
						}
						if (tab != null) {
							if (tab instanceof GenericList) {
								((GenericList) tab).refresh();
							}
							else if (tab instanceof CeoTab) {
								((CeoTab) tab).refresh();
							}
						}
					}
				}
			});
			cancel.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			saveCancel.add(save);
			saveCancel.add(cancel);
			saveCancel.add(passButton);
			if (rolle == 0) {
				saveCancel.add(roleButton);
			}
			add(saveCancel, BorderLayout.SOUTH);
			setVisible(true);
		}
	}
	private class LoginWindow extends JFrame {
		public LoginWindow(String user_name) {
			setResizable(false);
			setSize(Stuff.getWindowSize(0.3,0.2));
			setTitle("Administrator login");
			setLayout(new GridLayout(3, 2));
			setLocationRelativeTo(null);
			JLabel login = new JLabel("User name");
			JTextField userNameField = new JTextField();
			JLabel pass = new JLabel("Password");
			JPasswordField passField = new JPasswordField();
			JButton ok = new JButton("Log in");
			JButton canc = new JButton("Cancel");
			ActionListener action = new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					UserManager mng = new UserManager(sql);
					int role = mng.logon(userNameField.getText(), new String(passField.getPassword()));
					if (role > 0) {
						JOptionPane.showMessageDialog(LoginWindow.this, "You do not have the required access level.");
					} else if (role == -1) {
						JOptionPane.showMessageDialog(LoginWindow.this, "Incorrect user name or password.");
					} else if (role == -2) {
						JOptionPane.showMessageDialog(LoginWindow.this, "There was a problem with the connection.");
					} else if (role == 0) {
						RoleChangeBox box = new RoleChangeBox(user_name);
						dispose();
					}
				}
			};
			ok.addActionListener(action);
			canc.addActionListener(e -> dispose());
			passField.addActionListener(action);
			add(login);
			add(userNameField);
			add(pass);
			add(passField);
			add(ok);
			add(canc);
			setVisible(true);
		}
	}
	private class RoleChangeBox extends JFrame {
		public RoleChangeBox(String user_name) {
			setResizable(false);
			setSize(Stuff.getWindowSize(0.3,0.15));
			setLayout(new GridLayout(2, 2));
			setTitle("Change user role");
			setLocationRelativeTo(null);
			String selectQuery = "SELECT user_name, user_role FROM HCL_user WHERE user_name = '" + user_name + "'";
			System.out.println(selectQuery);
			String[] selected = sql.getRow(selectQuery);
			JLabel user = new JLabel("User: "+selected[0]);
			// 1 salg, 2 chef, 3 driver, 0 ceo
			String[] roleChoices = { "CEO", "Sales", "Chef", "Driver" };
			System.out.println(Arrays.toString(selected));
			JComboBox<String> roleChoice = new JComboBox<>(roleChoices);
			roleChoice.setSelectedIndex(Integer.parseInt(selected[1]));
			JButton save = new JButton("Save");
			JButton cancel = new JButton("Cancel");
			save.addActionListener(e -> {
				int sure = JOptionPane.showConfirmDialog(RoleChangeBox.this, "Are you sure", "Confirm", JOptionPane.YES_NO_OPTION);
				if (sure == 0) {
					sql.update("HCL_user", "user_role", "User_name", user_name, roleChoice.getSelectedIndex());
					dispose();
				}
			});
			cancel.addActionListener(e -> dispose());
			add(user);
			add(roleChoice);
			add(save);
			add(cancel);
			setVisible(true);
		}
	}
	private class PassBox extends JFrame {
		public PassBox() {
			setResizable(false);
			setLocationRelativeTo(null);
			setAlwaysOnTop(true);
			setSize(Stuff.getWindowSize(0.3,0.2));
			setLayout(new GridLayout(3, 2));
			JLabel newPassLabel = new JLabel("Enter new password:");
			JPasswordField newPassField = new JPasswordField();
			JLabel reEnterPass = new JLabel("Reenter password:");
			JPasswordField newPassField2 = new JPasswordField();
			JButton save = new JButton("Save");
			JButton cancel = new JButton("Cancel");
			AbstractAction saveAction = new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int sure = JOptionPane.showConfirmDialog(PassBox.this, "Are you sure?", "Edit user", JOptionPane.YES_NO_OPTION);
					if (sure == 0) {
						if (!(Arrays.equals(newPassField.getPassword(), newPassField2.getPassword()))) {
							JOptionPane.showMessageDialog(PassBox.this, "Passwords do not match");
						} else {
							UserManager mng = new UserManager(sql);
							String oldPass = new String(passField.getPassword());
							String newPass = new String(newPassField.getPassword());
							mng.changePassword(userName, oldPass, newPass);
							dispose();
						}
					}
				}
			};
			save.addActionListener(saveAction);
			newPassField2.addActionListener(saveAction);
			cancel.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			add(newPassLabel);
			add(newPassField);
			add(reEnterPass);
			add(newPassField2);
			add(save);
			add(cancel);
			setVisible(true);
		}
	}
}
