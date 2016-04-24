package clientGUI;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.util.Arrays;

/**
 * This extension of the JTable class handles our tables, since they all operate similarly
 * It sets them to not be editable, it makes them sortable, and it hides the ID numbers from the user
 */
public class JTableHCL extends JTable {

	private TableModel tabModel;

	public JTableHCL(TableModel model) {
		super(model);
		tabModel = this.getModel();
		TableColumnModel mod = this.getColumnModel();
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
	public int[] getSortColumn() {
		//second int is ascending or descending. hacky
		int sorted = this.getRowSorter().getSortKeys().get(0).getColumn();
		boolean asc = false;
		String[] values = { (String) this.getValueAt(0, sorted), (String) this.getValueAt(1, sorted) };
		String[] valuesSort = { (String) this.getValueAt(0, sorted), (String) this.getValueAt(1, sorted) };
		Arrays.sort(valuesSort);
		if (Arrays.equals(values, valuesSort)) {
			asc = true;
		}
		int[] ret = { sorted, asc ? 0 : 1 };
		return ret;
	}
	public void setSortColumn(int[] sort) {
		this.getRowSorter().toggleSortOrder(sort[0]);
		if (sort[1] == 1) {
			this.getRowSorter().toggleSortOrder(sort[0]);
		}
	}
}
