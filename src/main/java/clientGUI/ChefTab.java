package clientGUI;

import backend.DeliveryManager;
import backend.SQL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

/**
 * Creates the JPanel that is used as a tab in tabbedMenu
 */
class ChefTab extends JPanel {
	private String query = "SELECT delivery_id, adress, delivery_date, completed, delivered, HCL_deliveries.active FROM HCL_deliveries JOIN HCL_order ON (HCL_deliveries.order_id = HCL_order.order_id) WHERE HCL_deliveries.active = 1 AND completed = 0 ORDER BY delivery_date ASC";
	private String[][] data;
	private String[] titles;
	private SQL sql;
	private JTableHCL table;
	private viewVindow botFinal;
	private DefaultTableModel tabModel;
	ChefTab(SQL sql, int role) {
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
		table.setRowSelectionInterval(0,0);
		viewVindow bottom;
		try {
			bottom = new viewVindow(Integer.parseInt((String) table.getValueAt(table.getSelectedRow(), 0)));
		}
		catch (Exception e) {
			bottom = new viewVindow(-1);
		}
		botFinal = bottom;
		centerPanel.add(botFinal);
		add(centerPanel, BorderLayout.CENTER);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (e.getClickCount() == 2) {
					botFinal.change(Integer.parseInt((String) table.getValueAt(table.getSelectedRow(), 0)));
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
		northBar() {
			setLayout(new GridLayout(1, 2));
			JButton deliver = new JButton("Finish");
			JButton refresh = new JButton("Refresh");
			refresh.addActionListener(e ->refresh());
			deliver.addActionListener(e ->  {
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
					table.setRowSelectionInterval(0,0);
					botFinal.change(Integer.parseInt((String) table.getValueAt(table.getSelectedRow(), 0)));

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
		viewVindow(int delivery_id) {
			setLayout(new BorderLayout());
			JTabbedPane tabs = new JTabbedPane();
			String foodQuery;
			String ingrQuery;
			if (delivery_id != -1) {
				foodQuery = "SELECT food_id, name FROM HCL_food NATURAL JOIN HCL_order_food NATURAL JOIN HCL_deliveries" +
						" WHERE delivery_id = " + delivery_id;
				ingrQuery = "SELECT DISTINCT food_id, name FROM HCL_order_food NATURAL JOIN HCL_food NATURAL JOIN " +
						"HCL_deliveries WHERE delivery_id = " + delivery_id;
			}
			else {
				foodQuery = "SELECT food_id, name FROM HCL_food NATURAL JOIN HCL_order_food NATURAL JOIN HCL_deliveries";
				ingrQuery = "SELECT DISTINCT food_id, name FROM HCL_order_food NATURAL JOIN HCL_food NATURAL JOIN HCL_deliveries";
			}
			System.out.println("Food query: " + foodQuery);
			tabs.addTab("Foods", new viewTab(foodQuery));
			System.out.println("Ingredients query: " + ingrQuery);
			String[] FoodIDs = sql.getColumn(ingrQuery, 0);
			String[] tabTitles = sql.getColumn(ingrQuery, 1);
			ingrTab = new ingredientTab(FoodIDs, tabTitles);
			tabs.addTab("Ingredients", ingrTab);
			add(tabs, BorderLayout.CENTER);
		}
		class viewTab extends JPanel {
			viewTab(String query) {
				setLayout(new BorderLayout());
				System.out.println("Chef view ingredient query:\t" + query);
				System.out.println(query);
				tabTitles = ColumnNamer.getNamesWithOriginals(query,sql);
				String[][] tabData = sql.getStringTable(query, false);
				tabModel = new DefaultTableModel(tabData, tabTitles[1]);
				tabTable = new JTableHCL(tabModel);
				JScrollPane scroll = new JScrollPane(tabTable);
				add(scroll, BorderLayout.CENTER);
				tabTable.removeIDs();
			}
		}
		class ingredientTab extends JPanel {
			ingredientTab(String[] IDs, String[] tabTitles) {
				setLayout(new BorderLayout());
				ingredientTabs = new JTabbedPane();
				for (int i = 0; i < IDs.length; i++) {
					ingredientTabs.addTab(tabTitles[i], new ingredientList(IDs[i]));
				}
				add(ingredientTabs, BorderLayout.CENTER);
			}
			void refresh(String[] IDs, String[] tabTitles) {
				ingredientTabs.removeAll();
				for (int i = 0; i < IDs.length; i++) {
					ingredientTabs.addTab(tabTitles[i], new ingredientList(IDs[i]));
				}
			}
		}
		class ingredientList extends JPanel {
			ingredientList(String food_id) {
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
		void change(int id) {
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
