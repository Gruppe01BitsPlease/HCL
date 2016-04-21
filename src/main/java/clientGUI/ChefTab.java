package clientGUI;

import backend.DeliveryManager;
import backend.SQL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

/**
 * Created by Jens on 18-Apr-16.
 */
public class ChefTab extends JPanel {
	//private String query = "SELECT date_id, adress, delivery_date FROM HCL_order WHERE active = 1 AND delivered = 0 ORDER BY delivery_date ASC";
	private String query = "SELECT delivery_id, adress, delivery_date FROM HCL_deliveries NATURAL JOIN HCL_order WHERE active = 1 AND completed = 0 ORDER BY delivery_date ASC";
	private String[][] data;
	private String[] titles;
	private SQL sql;
	private JTableHCL table;
	private DefaultTableModel tabModel;
	public ChefTab(SQL sql, int role) {
		this.sql = sql;
		setLayout(new BorderLayout());
		System.out.println("Chef tab query: " + query);
		data = sql.getStringTable(query, false);
		titles = ColumnNamer.getNames(query, sql);
		tabModel = new DefaultTableModel(data, titles);
		table = new JTableHCL(tabModel);
		JPanel centerPanel = new JPanel(new GridLayout(2, 1));
		JScrollPane scroller = new JScrollPane(table);
		centerPanel.add(scroller);
		//JTabbedPane bottomTabs = new JTabbedPane();
		viewVindow bottom = new viewVindow(Integer.parseInt((String)table.getValueAt(0, 0)));
		centerPanel.add(bottom);
		add(centerPanel, BorderLayout.CENTER);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (e.getClickCount() == 2) {
					bottom.change(Integer.parseInt((String) table.getValueAt(table.getSelectedRow(), 0)));
				}
			}
		});
		add(new northBar(), BorderLayout.NORTH);
		table.removeIDs();
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
			JButton deliver = new JButton("Finish");
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
					int choice = JOptionPane.showConfirmDialog(null, "Complete deliveries?", "Order", JOptionPane.YES_NO_OPTION);
					if (choice == 0) {
						int[] selectedIndexes = table.getSelectedRows();
						int[] selectedIDs = new int[selectedIndexes.length];
						for (int i = 0; i < selectedIndexes.length; i++) {
							selectedIDs[i] = Integer.parseInt((String) table.getValueAt(selectedIndexes[i], 0));
						}
						DeliveryManager mng = new DeliveryManager(sql);
						for (int i = 0; i < selectedIDs.length; i++) {
							System.out.println("Deliver ID: " + selectedIDs[i]);
							int rs = mng.complete(selectedIDs[i]);
							System.out.println("Deliver result: " + rs);
						}
						refresh();
					}
				}
			});
			add(deliver);
			add(refresh);
		}
	}
	private class viewVindow extends JPanel {
		private DefaultTableModel tabModel;
		private JTableHCL tabTable;
		private String[][] tabTitles;
		private JTabbedPane ingredientTabs;
		private ingredientTab ingrTab;
		public viewVindow(int delivery_id) {
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
			ingrTab = new ingredientTab(FoodIDs, tabTitles);
			tabs.addTab("Ingredients", ingrTab);
			add(tabs, BorderLayout.CENTER);
			JButton finish = new JButton("Complete delivery");
			finish.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int choice = JOptionPane.showConfirmDialog(null, "Complete delivery?", "Order", JOptionPane.YES_NO_OPTION);
					if (choice == 0) {
						DeliveryManager mng = new DeliveryManager(sql);
						mng.complete(delivery_id);
						refresh();
					}
				}
			});
			add(finish, BorderLayout.SOUTH);
		}
		class viewTab extends JPanel {
			public viewTab(String query) {
				setLayout(new BorderLayout());
				String tabQuery = query;
				System.out.println("Chef view ingredient query:\t" + tabQuery);
				System.out.println(tabQuery);
				tabTitles = ColumnNamer.getNamesWithOriginals(tabQuery,sql);
				String[][] tabData = sql.getStringTable(tabQuery, false);
				tabModel = new DefaultTableModel(tabData, tabTitles[1]);
				tabTable = new JTableHCL(tabModel);
				JScrollPane scroll = new JScrollPane(tabTable);
				add(scroll, BorderLayout.CENTER);
				tabTable.removeIDs();
			}
		}
		class ingredientTab extends JPanel {
			public ingredientTab(String[] IDs, String[] tabTitles) {
				setLayout(new BorderLayout());
				ingredientTabs = new JTabbedPane();
				for (int i = 0; i < IDs.length; i++) {
					ingredientTabs.addTab(tabTitles[i], new ingredientList(IDs[i]));
				}
				add(ingredientTabs, BorderLayout.CENTER);
			}
			public void refresh(String[] IDs, String[] tabTitles) {
				ingredientTabs.removeAll();
				for (int i = 0; i < IDs.length; i++) {
					ingredientTabs.addTab(tabTitles[i], new ingredientList(IDs[i]));
				}
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
		public void change(int id) {
			String select = "SELECT food_id, name, number FROM HCL_food NATURAL JOIN HCL_order_food NATURAL JOIN HCL_deliveries" +
					" WHERE delivery_id = " + id;
			String ingrQuery = "SELECT DISTINCT food_id, name FROM HCL_order_food NATURAL JOIN HCL_food NATURAL JOIN " +
					"HCL_deliveries WHERE delivery_id = " + id;
			System.out.println("Ingredients query: " + ingrQuery);
			String[] FoodIDs = sql.getColumn(ingrQuery, 0);
			String[] ingrTabTitles = sql.getColumn(ingrQuery, 1);
			System.out.println("Food ids: " + Arrays.toString(FoodIDs));
			System.out.println("Tab titles: " + Arrays.toString(ingrTabTitles));
			String[][] tabData = sql.getStringTable(select, false);
			tabModel = new DefaultTableModel(tabData, tabTitles[1]);
			ingrTab.refresh(FoodIDs, ingrTabTitles);
			tabTable.setModel(tabModel);
			tabTable.removeIDs();
		}
	}
}
