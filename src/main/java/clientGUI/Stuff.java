package clientGUI;

import backend.SQL;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by Jens on 15-Apr-16.
 */
abstract class Stuff {
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
	/*public void update() {
		String[] newValues = getNewValues();
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
					JOptionPane.showMessageDialog(GenericList.editWindow.this, "Database Error!");
				} else if (res == -3) {
					JOptionPane.showMessageDialog(GenericList.editWindow.this, "There is a problem with one of the parameters.");
				} else if (res == -4) {
					JOptionPane.showMessageDialog(GenericList.editWindow.this, "There is no method for generating this object, it must be overridden in the tab class.");
				}
				//System.out.println(res);
			} else {
				JOptionPane.showMessageDialog(GenericList.editWindow.this, "Entry already exists! Choose a different ID number.");
			}
		}
	}*/
}
