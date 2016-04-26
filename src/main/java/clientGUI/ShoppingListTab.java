package clientGUI;

import backend.IngredientManager;
import backend.SQL;
import backend.Shoppinglist;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class ShoppingListTab extends JPanel {
	private SQL sql;
	private Shoppinglist shop;
	private String[][] shoppingList;
	private String[] titles = { "id", "Ingredient name", "Total in deliveries", "Total stock", "Stock balance" };
	private JTableHCL table;
	private DefaultTableModel tabModel;
	private JComboBox<Integer> dayBox;
	public ShoppingListTab(SQL sql) {
		this.sql = sql;
		setLayout(new BorderLayout());
		shop = new Shoppinglist(sql);
		shoppingList = shop.getShoppinglist(7);
		tabModel = new DefaultTableModel(shoppingList, titles);
		table = new JTableHCL(tabModel);
		TableColumn column = table.getColumnModel().getColumn(0);
		column.setMinWidth(0);
		column.setMaxWidth(0);
		column.setWidth(0);
		column.setPreferredWidth(0);
		table.getRowSorter().toggleSortOrder(1);
		JScrollPane scroller = new JScrollPane(table);
		Integer[] dayArray = new Integer[7];
		for (int i = 0; i < dayArray.length; i++) {
			dayArray[i] = i + 1;
		}
		dayBox = new JComboBox<>(dayArray);
            dayBox.setToolTipText("Interval in days of deliveries to show");
        dayBox.addItemListener(e -> {
			refresh();
		});
		dayBox.setSelectedIndex(6);
		JLabel boxLabel = new JLabel("Days to show:");
		JPanel boxPanel = new JPanel(new GridLayout(1, 2));
		boxPanel.add(boxLabel);
		boxPanel.add(dayBox);
		JPanel northBar = new JPanel(new GridLayout(1, 3));
		JButton addStock = new JButton("Add to stock");
            addStock.setToolTipText("Add stock to a single ingredient. You can select the amount of stock to be added.");
		JButton addAll = new JButton("Add all to stock");
            addAll.setToolTipText("Adds all items spesified in the shopping-list to the stock.");
		JButton refresh = new JButton("Refresh");
            refresh.setToolTipText("Refresh the list. Should not be necessary most of the time.");
        refresh.addActionListener(e -> {
			refresh();
		});
		addStock.addActionListener(e -> {
			if (table.getSelectedRow() != -1) {
				AddWindow window = new AddWindow(Integer.parseInt((String) table.getValueAt(table.getSelectedRow(), 0)));
			}
		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (e.getClickCount() == 2) {
					AddWindow window = new AddWindow(Integer.parseInt((String)table.getValueAt(table.getSelectedRow(), 0)));
				}
			}
		});
		addAll.addActionListener(e -> {
			int sure = JOptionPane.showConfirmDialog(ShoppingListTab.this, "Really add all items to stock?", "", JOptionPane.YES_NO_OPTION);
			if (sure == 0) {
				shop.addShoppinglist(shoppingList);
				refresh();
			}
		});
		northBar.add(addStock);
		northBar.add(addAll);
		northBar.add(refresh);
		add(northBar, BorderLayout.NORTH);
		add(scroller, BorderLayout.CENTER);
		add(boxPanel, BorderLayout.SOUTH);
	}
	private class AddWindow extends JFrame {
		public AddWindow(int ingredient_id) {
			setLocationRelativeTo(null);
			setSize(Stuff.getWindowSize(0.3, 0.15));
			setResizable(false);
			setAlwaysOnTop(true);
			JLabel amountLabel = new JLabel("Amount to add to stock:");
			JTextField amountField = new JTextField();
			JButton add = new JButton("Add");
                add.setToolTipText("Adds the inputted amount to the stock.");
			JButton addBalance = new JButton("Add balance");
                addBalance.setToolTipText("Adds the amount specified in the shopping list to the stock for this item.");
			JButton cancel = new JButton("Cancel");
			add.addActionListener(e -> {
				int amount = -1;
				try {
					amount = Integer.parseInt(amountField.getText());
				}
				catch (Exception f) {
					JOptionPane.showMessageDialog(AddWindow.this, "Please enter a valid number");
				}
				int sure = JOptionPane.showConfirmDialog(AddWindow.this, "Add " + amountField.getText() + " of this item to stock?", "", JOptionPane.YES_NO_OPTION);
				if (sure == 0 && amount > 0) {
					IngredientManager ingrMng = new IngredientManager(sql);
					ingrMng.addStock(ingredient_id, amount);
					dispose();
				}
				refresh();
			});
			addBalance.addActionListener(e ->  {
				int sure = JOptionPane.showConfirmDialog(AddWindow.this, "Add all of this item to stock?", "", JOptionPane.YES_NO_OPTION);
				if (sure == 0) {
					shop.add(shoppingList, ingredient_id);
					dispose();
				}
				refresh();
			});
			cancel.addActionListener(e -> {
				dispose();
				refresh();
			});
			setLayout(new GridLayout(2, 1));
			JPanel bottomPanel = new JPanel(new GridLayout(1, 3));
			JPanel topPanel = new JPanel(new GridLayout(1, 2));
			topPanel.add(amountLabel);
			topPanel.add(amountField);
			bottomPanel.add(add);
			bottomPanel.add(addBalance);
			bottomPanel.add(cancel);
			add(topPanel);
			add(bottomPanel);
			setVisible(true);
		}
	}
	private void refresh() {
		shoppingList = shop.getShoppinglist((Integer) dayBox.getSelectedItem());
		tabModel = new DefaultTableModel(shoppingList, titles);
		table.setModel(tabModel);
	}
}
