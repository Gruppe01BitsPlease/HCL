package clientGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * Created by Jens on 10.04.2016.
 */
public class JTableHCL extends JTable {
	private TableModel tabModel;
	private TableColumnModel mod;
	public JTableHCL(TableModel model) {
		super(model);
		tabModel = this.getModel();
		mod = this.getColumnModel();
		removeIDs();
		this.setAutoCreateRowSorter(true);
	}
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	public void removeIDs() {
		for (int i = tabModel.getColumnCount() - 1; i >= 0; i--) {
			if (tabModel.getColumnName(i).contains("ID")) {
				mod.removeColumn(mod.getColumn(i));
			}
		}
	}
}
