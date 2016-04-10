package clientGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * Created by Jens on 10.04.2016.
 */
public class JTableHCL extends JTable {
	public JTableHCL(TableModel model) {
		super(model);
		TableModel tabModel = this.getModel();
		TableColumnModel mod = this.getColumnModel();
		for (int i = tabModel.getColumnCount() - 1; i >= 0; i--) {
			if (tabModel.getColumnName(i).contains("ID")) {
				mod.removeColumn(mod.getColumn(i));
			}
		}
		this.setAutoCreateRowSorter(true);
	}
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}
