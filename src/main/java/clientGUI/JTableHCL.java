package clientGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * Created by Jens on 10.04.2016.
 */
public class JTableHCL extends JTable {
	//This extension of the JTable class handles our tables, since they all operate similarly
	//It sets them to not be editable, it makes them sortable, and it hides the ID numbers from the user
	private TableModel tabModel;
	private TableColumnModel mod;
	public JTableHCL(TableModel model) {
		super(model);
		tabModel = this.getModel();
		mod = this.getColumnModel();
		this.setAutoCreateRowSorter(true);
	}
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	public void removeIDs() {
		for (int i = tabModel.getColumnCount() - 1; i >= 0; i--) {
			String type = DataTyper.getDataType(tabModel.getColumnName(i));
			if (type != null && type.equals("id") || type.equals("active")) {
				TableColumn column = getColumnModel().getColumn(i);
				column.setMinWidth(50);
				column.setMaxWidth(50);
				column.setWidth(50);
				column.setPreferredWidth(50);
			}
			else if (type == null){
				System.out.println("Error in column hider: " + type);
			}
		}
	}
	public int getSortColumn() {
		return this.getRowSorter().getSortKeys().get(0).getColumn();
	}
	public void setSortColumn(int column) {
		this.getRowSorter().toggleSortOrder(column);
	}
}
