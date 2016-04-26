package clientGUI;

import backend.DeliveryManager;
import backend.IngredientManager;
import backend.SQL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * Creates the JPanel that is used as a tab in tabbedMenu
 */
class ChefTab extends JPanel {
	private String query = "SELECT delivery_id, adress, delivery_date FROM HCL_deliveries JOIN HCL_order ON (HCL_deliveries.order_id = HCL_order.order_id) WHERE HCL_order.active = 1 AND HCL_deliveries.active = 1 AND completed = 0 AND delivered=0";
	private String[][] data;
	private String[] titles;
	private SQL sql;
	private JTableHCL table;
	private ViewVindow botFinal;
	private DefaultTableModel tabModel;
	private JComboBox<String> dayBox;
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
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.add(scroller, BorderLayout.CENTER);
		northPanel.add(new SouthBar(), BorderLayout.SOUTH);
		table.setRowSelectionInterval(0,0);
		ViewVindow bottom;
		try {
			bottom = new ViewVindow(Integer.parseInt((String) table.getValueAt(table.getSelectedRow(), 0)));
		}
		catch (Exception e) {
			bottom = new ViewVindow(-1);
		}
		botFinal = bottom;
		centerPanel.add(northPanel);
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
		add(new NorthBar(), BorderLayout.NORTH);
		table.removeIDs();
		table.getRowSorter().toggleSortOrder(2);
	}
	private void refresh() {
		int[] sortColumn = table.getSortColumn();
		if (dayBox.getSelectedIndex() == 7) {
			data = sql.getStringTable(query, false);
			tabModel = new DefaultTableModel(data, titles);
			table.setModel(tabModel);
			table.removeIDs();
		}
		else {
			refresh(dayBox.getSelectedIndex() + 1);
		}
		if (sortColumn[0] != -1) {
			table.setSortColumn(sortColumn);
		}
	}
	private void refresh(int days) {
		LocalDate now = LocalDate.now();
		String date = now.plusDays(days).toString();
		String dayQuery = "SELECT delivery_id, adress, delivery_date, completed, delivered, HCL_deliveries.active FROM HCL_deliveries " +
				"JOIN HCL_order ON (HCL_deliveries.order_id = HCL_order.order_id) WHERE HCL_deliveries.active = 1 AND completed = 0 " +
				"AND delivery_date < '"+date+"' ORDER BY delivery_date ASC";
		data = sql.getStringTable(dayQuery, false);
		tabModel = new DefaultTableModel(data, titles);
		table.setModel(tabModel);
		table.removeIDs();
	}
	private class SouthBar extends JPanel {
		SouthBar() {
			setLayout(new GridLayout(1, 2));
			JLabel daysLabel = new JLabel("Days to show:");
			String[] dayChoices = new String[8];
			for (int i = 0; i < 7; i++){
				dayChoices[i] = Integer.toString(i + 1);
			}
			dayChoices[7] = "All";
			dayBox = new JComboBox<>(dayChoices);
                dayBox.setToolTipText("Interval in days of deliveries to show");
			dayBox.addItemListener(e -> {
				refresh();
			});
			dayBox.setSelectedIndex(7);
			add(daysLabel);
			add(dayBox);
		}
	}
	private class NorthBar extends JPanel {
		NorthBar() {
			setLayout(new GridLayout(1, 2));
			JButton deliver = new JButton("Finish");
                deliver.setToolTipText("Sets the order as completed and indicates that it is ready for delivery.");
			JButton refresh = new JButton("Refresh");
                refresh.setToolTipText("Refresh the list. Should not be necessary most of the time.");
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
					IngredientManager ingMng = new IngredientManager(sql);
                    for (int i = 0; i < selectedIDs.length; i++) {
						String selectIngr = "SELECT ingredient_id, number FROM HCL_food_ingredient WHERE food_id IN (" +
								"SELECT food_id FROM HCL_order_food WHERE order_id IN (" +
								"SELECT order_id FROM HCL_deliveries WHERE delivery_id = " + selectedIDs[i] + ")) AND active = 1";
						//Ingredients in delivery, and how many to be delivered
						String[][] ingredients = sql.getStringTable(selectIngr, false);
						for (int j = 0; j < ingredients.length; j++) {
							ingMng.removeStock(Integer.parseInt(ingredients[j][0]), Integer.parseInt(ingredients[j][1]));
						}
						System.out.println("Ingredients in delivery: \n"+selectIngr);
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
	private class ViewVindow extends JPanel {
		private DefaultTableModel tabModel;
		private String foodQuery;
		private String ingrQuery;
		private JTableHCL tabTable;
		private String[][] tabTitles;
		private JTabbedPane ingredientTabs;
		private IngredientTab ingrTab;
		private int delivery_id;
		ViewVindow(int delivery_id) {
			this.delivery_id = delivery_id;
			setLayout(new BorderLayout());
			JTabbedPane tabs = new JTabbedPane();
			if (delivery_id != -1) {
				foodQuery = "SELECT food_id, name FROM HCL_food NATURAL JOIN HCL_order_food NATURAL JOIN HCL_deliveries" +
						" WHERE delivery_id = " + delivery_id + " AND HCL_food.active = 1";
				ingrQuery = "SELECT DISTINCT food_id, name FROM HCL_order_food NATURAL JOIN HCL_food NATURAL JOIN " +
						"HCL_deliveries WHERE delivery_id = " + delivery_id + " AND active = 1";
			}
			else {
				foodQuery = "SELECT food_id, name FROM HCL_food NATURAL JOIN HCL_order_food NATURAL JOIN HCL_deliveries";
				ingrQuery = "SELECT DISTINCT food_id, name FROM HCL_order_food NATURAL JOIN HCL_food NATURAL JOIN HCL_deliveries";
			}
			System.out.println("Food query: " + foodQuery);
			tabs.addTab("Foods", new ViewTab(foodQuery));
			System.out.println("Ingredients query: " + ingrQuery);
			String[] FoodIDs = sql.getColumn(ingrQuery, 0);
			String[] tabTitles = sql.getColumn(ingrQuery, 1);
			ingrTab = new IngredientTab(FoodIDs, tabTitles);
			tabs.addTab("Ingredients", ingrTab);
			add(tabs, BorderLayout.CENTER);
		}
		class ViewTab extends JPanel {
			ViewTab(String query) {
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
		class IngredientTab extends JPanel {
			IngredientTab(String[] IDs, String[] tabTitles) {
				setLayout(new BorderLayout());
				ingredientTabs = new JTabbedPane();
				for (int i = 0; i < IDs.length; i++) {
					ingredientTabs.addTab(tabTitles[i], new IngredientList(IDs[i]));
				}
				add(ingredientTabs, BorderLayout.CENTER);
			}
			void refresh(String[] IDs, String[] tabTitles) {
				ingredientTabs.removeAll();
				for (int i = 0; i < IDs.length; i++) {
					ingredientTabs.addTab(tabTitles[i], new IngredientList(IDs[i]));
				}
			}
		}
		class IngredientList extends JPanel {
			IngredientList(String food_id) {
				setLayout(new BorderLayout());
				String query = "SELECT ingredient_id, name, number, stock, other, expiration_date FROM HCL_ingredient" +
						" NATURAL JOIN HCL_food_ingredient WHERE food_id = " + food_id + " AND HCL_ingredient.active = 1";
				String[][] foods = sql.getStringTable(query, false);
				String[][] titles = ColumnNamer.getNamesWithOriginals(query, sql);
				DefaultTableModel tabModel = new DefaultTableModel(foods, titles[1]);
				JTableHCL table = new JTableHCL(tabModel);
				table.removeIDs();
				JScrollPane scroll = new JScrollPane(table);
				add(scroll, BorderLayout.CENTER);
			}
		}
		void change(int id) {
			delivery_id = id;
			foodQuery = "SELECT food_id, name FROM HCL_food NATURAL JOIN HCL_order_food NATURAL JOIN HCL_deliveries" +
					" WHERE delivery_id = " + delivery_id + " AND HCL_food.active = 1";
			ingrQuery = "SELECT DISTINCT food_id, name FROM HCL_order_food NATURAL JOIN HCL_food NATURAL JOIN " +
					"HCL_deliveries WHERE delivery_id = " + delivery_id + " AND active = 1";
			System.out.println("Ingredients query: " + ingrQuery);
			String[] FoodIDs = sql.getColumn(ingrQuery, 0);
			String[] ingrTabTitles = sql.getColumn(ingrQuery, 1);
			System.out.println("Food ids: " + Arrays.toString(FoodIDs));
			System.out.println("Tab titles: " + Arrays.toString(ingrTabTitles));
			String[][] tabData = sql.getStringTable(foodQuery, false);
			tabModel = new DefaultTableModel(tabData, tabTitles[1]);
			ingrTab.refresh(FoodIDs, ingrTabTitles);
			tabTable.setModel(tabModel);
			tabTable.removeIDs();
		}
	}
}
