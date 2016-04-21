package clientGUI;

import backend.DeliveryManager;
import backend.SQL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Creates the JPanel that is used as a tab in tabbedMenu
 */
public class DriverTab extends JPanel {
	//private String query = "SELECT date_id, adress, delivery_date FROM HCL_order WHERE active = 1 AND delivered = 0 ORDER BY delivery_date ASC";
	private String query = "SELECT delivery_id, adress, delivery_date, completed FROM HCL_deliveries NATURAL JOIN HCL_order WHERE active = 1 AND delivered = 0 ORDER BY delivery_date ASC";
	private String[][] data;
	private String[] titles;
	private SQL sql;
	private JTableHCL table;
	private DefaultTableModel tabModel;
	public DriverTab(SQL sql, int role) {
		this.sql = sql;
		setLayout(new BorderLayout());
		System.out.println("Chef tab query: " + query);
		data = sql.getStringTable(query, false);
		titles = ColumnNamer.getNames(query, sql);
		tabModel = new DefaultTableModel(data, titles);
		table = new JTableHCL(tabModel);
		JScrollPane scroller = new JScrollPane(table);
		add(scroller, BorderLayout.CENTER);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (e.getClickCount() == 2) {
					int choice = JOptionPane.showConfirmDialog(null, "Deliver this order?", "Deliver", JOptionPane.YES_NO_OPTION);
					if (choice == 0) {
						int selID = Integer.parseInt((String) tabModel.getValueAt(table.getSelectedRow(), 0));
						DeliveryManager mng = new DeliveryManager(sql);
						mng.deliver(selID);
						refresh();
					}
				}
			}
		});
		add(new northBar(), BorderLayout.NORTH);
		table.removeIDs();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	private void refresh() {
		data = sql.getStringTable(query, false);
		tabModel = new DefaultTableModel(data, titles);
		table.setModel(tabModel);
		table.removeIDs();
	}
	private class northBar extends JPanel {
		public northBar() {
			setLayout(new GridLayout(1, 2));
			JButton deliver = new JButton("Deliver");
			JButton refresh = new JButton("Refresh");
			refresh.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					refresh();
				}
			});
			deliver.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (table.getSelectedRow() != -1) {
						int choice = JOptionPane.showConfirmDialog(null, "Deliver this order?", "Order", JOptionPane.YES_NO_OPTION);
						if (choice == 0) {
							int selID = Integer.parseInt((String) tabModel.getValueAt(table.getSelectedRow(), 0));
							DeliveryManager mng = new DeliveryManager(sql);
							mng.deliver(selID);
							refresh();
						}
					}
				}
			});
			add(deliver);
			add(refresh);
		}
	}
	/*private class viewVindow extends JFrame {
		private int date_id;
		public viewVindow(int delivery_id) {
			this.date_id = delivery_id;
			Dimension d = new Dimension((int) (GenericList.x * 0.4), (int) (GenericList.y * 0.4));
			setMinimumSize(d);
			setLayout(new BorderLayout());
			JTabbedPane tabs = new JTabbedPane();
			String foodQuery = "SELECT food_id, name FROM HCL_food NATURAL JOIN HCL_order_food NATURAL JOIN HCL_deliveries" +
					" WHERE delivery_id = " + delivery_id;
			System.out.println("Food query: " + foodQuery);
			tabs.addTab("Foods", new viewTab(foodQuery));
			String ingrQuery = "SELECT DISTINCT food_id, name FROM HCL_order_food NATURAL JOIN HCL_food NATURAL JOIN " +
					"HCL_deliveries WHERE delivery_id = " + delivery_id;
			System.out.println("Ingredients query: " + ingrQuery);
			String[] FoodIDs = sql.getColumn(ingrQuery, 0);
			String[] tabTitles = sql.getColumn(ingrQuery, 1);
			tabs.addTab("Ingredients", new ingredientTab(FoodIDs, tabTitles));
			add(tabs, BorderLayout.CENTER);
			JButton finish = new JButton("Complete delivery");
			finish.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int choice = JOptionPane.showConfirmDialog(null, "Complete delivery?", "Order", JOptionPane.YES_NO_OPTION);
					if (choice == 0) {
						DeliveryManager mng = new DeliveryManager(sql);
						mng.deliver(delivery_id);
						dispose();
						refresh();
					}
				}
			});
			add(finish, BorderLayout.SOUTH);
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
		class ingredientTab extends JPanel {
			public ingredientTab(String[] IDs, String[] tabTitles) {
				setLayout(new BorderLayout());
				JTabbedPane tabs = new JTabbedPane();
				for (int i = 0; i < IDs.length; i++) {
					tabs.addTab(tabTitles[i], new ingredientList(IDs[i]));
				}
				add(tabs, BorderLayout.CENTER);
			}
		}
		class ingredientList extends JPanel {
			public ingredientList(String food_id) {
				setLayout(new BorderLayout());
				String query = "SELECT name, number, stock, other, expiration_date FROM HCL_ingredient" +
						" NATURAL JOIN HCL_food_ingredient WHERE food_id = " + food_id;
				String[][] foods = sql.getStringTable(query, false);
				String[][] titles = ColumnNamer.getNamesWithOriginals(query, sql);
				DefaultTableModel tabModel = new DefaultTableModel(foods, titles[1]);
				JTableHCL table = new JTableHCL(tabModel);
				JScrollPane scroll = new JScrollPane(table);
				add(scroll, BorderLayout.CENTER);
			}
		}
	}*/
}
