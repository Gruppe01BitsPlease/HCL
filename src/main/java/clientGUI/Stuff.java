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
		if (!(year.getText().equals("") && month.getText().equals("") & day.getText().equals(""))) {
			return year.getText() + "-" + month.getText() + "-" + day.getText();
		}
		else {
			return "";
		}
	}
	public void setDate(String date) {
		year = new JTextField(date.substring(0, 4));
		month = new JTextField(date.substring(5, 7));
		day = new JTextField(date.substring(8, 10));
	}
}
class editFields extends JPanel {
	private ArrayList<JComponent> fields = new ArrayList<>();
	public editFields(String query, SQL sql, boolean newEntry) {
		String[] selected = sql.getRow(query);
		String[] titles = ColumnNamer.getNames(query, sql);
		String[] dataTypes = DataTyper.getDataTypes(titles);
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
				JComboBox<String> k = new JComboBox<>(choices[1]);
				if (!newEntry) {
					k.setSelectedItem(choices[1][Stuff.findIndexOf(choices[1], selected[i])]);
					k.setEnabled(false);
				}
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
	public ArrayList<JComponent> getFields() {
		return fields;
	}
}
