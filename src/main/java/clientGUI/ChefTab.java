package clientGUI;

import backend.SQL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Jens on 18-Apr-16.
 */
public class ChefTab extends JPanel {
	private String query = "SELECT order_id, adress, delivery_date, name FROM HCL_order NATURAL JOIN HCL_food_ingredient " +
			"NATURAL JOIN HCL_food WHERE active = 1 AND delivered = 0 ORDER BY delivery_date ASC";
	private String[][] data;
	private String[] titles;
	private SQL sql;
	public ChefTab(SQL sql, int role) {
		this.sql = sql;
		setLayout(new BorderLayout());
		data = sql.getStringTable(query, false);
		titles = ColumnNamer.getNames(query, sql);
		DefaultTableModel tabModel = new DefaultTableModel(data, titles);
		JTableHCL table = new JTableHCL(tabModel);
		JScrollPane scroller = new JScrollPane(table);
		add(scroller, BorderLayout.CENTER);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (e.getClickCount() == 2) {
					int selID = Integer.parseInt((String) tabModel.getValueAt(table.getSelectedRow(), 0));
					viewVindow view = new viewVindow(selID);
				}
			}
		});
		table.removeIDs();
	}
	private class viewVindow extends JFrame {
		private int order_id;
		public viewVindow(int order_id) {
			this.order_id = order_id;
			Dimension d = new Dimension((int) (GenericList.x * 0.4), (int) (GenericList.y * 0.4));
			setMinimumSize(d);
			setLayout(new BorderLayout());
			JTabbedPane tabs = new JTabbedPane();
			String foodQuery = "SELECT food_id, name FROM HCL_food NATURAL JOIN HCL_order_food WHERE order_id = " + order_id;
			tabs.addTab("Foods", new viewTab(foodQuery));
			String ingrQuery = "SELECT ingredient_id, name, number FROM HCL_ingredient NATURAL JOIN HCL_food_ingredient " +
					"WHERE food_id IN (SELECT food_id FROM HCL_order_food WHERE order_id = " + order_id + ")";
			tabs.addTab("Ingredients", new viewTab(ingrQuery));
			add(tabs, BorderLayout.CENTER);
			JButton finish = new JButton("Complete order");
			finish.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int choice = JOptionPane.showConfirmDialog(null, "Complete order?", "Order", JOptionPane.YES_NO_OPTION);
					if (choice == 0) {

					}
				}
			});
			setLocationRelativeTo(null);
			setVisible(true);
		}
		class viewTab extends JPanel {
			public viewTab(String query) {
				setLayout(new BorderLayout());
				String tabQuery = query;
				System.out.println("Chef view ingredient query:\t" + tabQuery);
				System.out.println(tabQuery);
				String[][] tabTitles = ColumnNamer.getNamesWithOriginals(tabQuery,sql);
				String[][] tabData = sql.getStringTable(tabQuery, false);
				DefaultTableModel tabModel = new DefaultTableModel(tabData, tabTitles[1]);
				JTableHCL tabTable = new JTableHCL(tabModel);
				JScrollPane scroll = new JScrollPane(tabTable);
				add(scroll, BorderLayout.CENTER);
				tabTable.removeIDs();
			}
		}
	}
}
