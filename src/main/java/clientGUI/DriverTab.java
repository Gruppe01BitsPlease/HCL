package clientGUI;

import backend.DeliveryManager;
import backend.SQL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

/**
 * Creates the JPanel that is used as a tab in TabbedMenu
 */
class DriverTab extends JPanel {
	private String query = "SELECT delivery_id, adress, customer_name, tlf, delivery_date, postnr, completed FROM HCL_deliveries NATURAL JOIN HCL_order JOIN  HCL_customer ON (HCL_order.customer_id = HCL_customer.customer_id) WHERE HCL_deliveries.active = 1 AND delivered = 0";
	//private String query = "";
	private String[][] data;
	private String[] titles;
	private SQL sql;
	private JTableHCL table;
	private DefaultTableModel tabModel;
	private JComboBox<String> dayBox;
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
						int selID = Integer.parseInt((String) table.getValueAt(table.getSelectedRow(), 0));
						System.out.println("Selected delivery: " + selID);
						DeliveryManager mng = new DeliveryManager(sql);
						mng.deliver(selID);
						refresh();
					}
				}
			}
		});
		add(new NorthBar(), BorderLayout.NORTH);
		add(new SouthBar(), BorderLayout.SOUTH);
		table.getRowSorter().toggleSortOrder(2);
		table.removeIDs();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
		String dayQuery = "SELECT delivery_id, adress, delivery_date, postnr, completed FROM HCL_deliveries NATURAL JOIN HCL_order " +
				"WHERE active = 1 AND delivered = 0 AND delivery_date < '"+date+"' ORDER BY delivery_date ASC";
		System.out.println("Day query: " + dayQuery);
		data = sql.getStringTable(dayQuery, false);
		tabModel = new DefaultTableModel(data, titles);
		table.setModel(tabModel);
		table.removeIDs();
	}
	private class SouthBar extends JPanel {
		SouthBar() {
			setLayout(new GridLayout(1, 4));
			JLabel daysLabel = new JLabel("Days to show:");
			String[] dayChoices = new String[8];
			for (int i = 0; i < 7; i++){
				dayChoices[i] = Integer.toString(i + 1);
			}
			dayChoices[7] = "All";
			dayBox = new JComboBox<>(dayChoices);
                dayBox.setToolTipText("Interval in days of deliveries to show");
            dayBox.addItemListener(e -> {
				if (dayBox.getSelectedIndex() < 7) {
					refresh(dayBox.getSelectedIndex() + 1);
				}
				else {
					refresh();
				}
			});
			dayBox.setSelectedIndex(7);
			JPanel daysPanel = new JPanel(new GridLayout(1, 2));
			daysPanel.add(daysLabel);
			daysPanel.add(dayBox);
			add(Box.createHorizontalBox());
			add(Box.createHorizontalBox());
			add(Box.createHorizontalBox());
			add(daysPanel);
		}
	}
	private class NorthBar extends JPanel {
		NorthBar() {
			setLayout(new GridLayout(1, 2));
			JButton deliver = new JButton("Deliver");
                deliver.setToolTipText("Set the delivery as delivered and remove it from the list. " +
                        "Indicates that the delivery has been paid for.");
			JButton refresh = new JButton("Refresh");
                refresh.setToolTipText("Refresh the list. Should not be necessary most of the time.");
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
