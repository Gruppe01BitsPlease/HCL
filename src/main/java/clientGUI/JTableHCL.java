package clientGUI;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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
				column.setMinWidth(0);
				column.setMaxWidth(0);
				column.setWidth(0);
				column.setPreferredWidth(0);
			}
			else if (type == null){
				System.out.println("Error in column hider: " + type);
			}
		}
	}
	public int[] getSortColumn() {
		//second int is ascending or descending. hacky
		int ret[] = { -1 };
		try {
			int sorted = this.getRowSorter().getSortKeys().get(0).getColumn();
			SortOrder sortOrder = this.getRowSorter().getSortKeys().get(0).getSortOrder();
			int asc = 0;
			if (sortOrder == SortOrder.ASCENDING) {
				asc = 1;
			}
			ret = new int[]{ sorted, asc };
			return ret;
		}
		catch (Exception e) {
			return ret;
		}
	}
	public void setSortColumn(int[] sort) {
		this.getRowSorter().toggleSortOrder(sort[0]);
		if (sort[1] == 0) {
			System.out.println("Descending");
			this.getRowSorter().toggleSortOrder(sort[0]);
		}
	}
}
