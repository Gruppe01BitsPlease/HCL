package clientGUI;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * This extension of the JTable class handles our tables, since they all operate similarly
 * It sets them to not be editable, it makes them sortable, and it hides the ID numbers from the user
 */
class JTableHCL extends JTable {
	public JTableHCL(TableModel model) {
		super(model);
		this.setAutoCreateRowSorter(true);
	}
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	public void removeIDs() {
		TableModel tabModel = this.getModel();
		for (int i = tabModel.getColumnCount() - 1; i >= 0; i--) {
			String type = DataTyper.getDataType(tabModel.getColumnName(i));
			if (type.equals("id") || type.equals("active")) {
				TableColumn column = getColumnModel().getColumn(i);
				column.setMinWidth(50);
				column.setMaxWidth(50);
				column.setWidth(50);
				column.setPreferredWidth(50);
			}
			else if (type.equals("boolean")) {
				for (int j = 0; j < tabModel.getRowCount(); j++) {
					if (tabModel.getValueAt(j, i).equals("1")) {
						tabModel.setValueAt("\u2713", j, i);
					}
					else if (tabModel.getValueAt(j, i).equals("0")) {
						tabModel.setValueAt("-", j, i);
					}
				}
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
