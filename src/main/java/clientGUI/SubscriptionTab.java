package clientGUI;

import backend.SQL;
import backend.SubscriptionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

/**
 * Created by Jens on 14-Apr-16.
 */
public class SubscriptionTab extends GenericList {
	private static SQL sql;
	private static String query = "SELECT order_id, customer_name, adress, postnr FROM HCL_subscription NATURAL JOIN HCL_order NATURAL JOIN HCL_customer";
	private static String sqlTableName = "HCL_subscription";
	//Tab name, foreign PK, link table name, other table name, foreign identifier
	//{ "Foods", "food_id", "HCL_order_food", "HCL_food", "name" }
	private static String[][] linkTables = {{ "Dates", "date_id", "HCL_subscription_date", "HCL_subscription_date", "dato"}};
	public SubscriptionTab(SQL sql) {
		super(query, sqlTableName, linkTables, null, sql);
		this.sql = sql;
	}
	public int delete(int nr) {
		SubscriptionManager mng = new SubscriptionManager(sql);
		int ret = mng.delete(nr);
		System.out.println("Delete code" + ret);
		return ret;
	}
	public void edit(int id, boolean newItem) {
		editWindow edit = new editWindow(id, newItem);
	}
	class editWindow extends JFrame {
		DefaultTableModel subModel;
		JTableHCL subTable;
		String[][] dateArray;
		String[] subTitles;
		public editWindow(int id, boolean newSubscription) {
			setSize((int) (x * 0.3), (int) (y * 0.3));
			setTitle("Subscription");
			setLayout(new BorderLayout());
			String getDateQuery = "SELECT * FROM HCL_subscription_date WHERE order_id = " + id;
			dateArray = new String[0][];
			if (!newSubscription) {
				dateArray = sql.getStringTable(getDateQuery, false);
				subTitles = ColumnNamer.getNames(getDateQuery, sql);
			}
			else {
				subTitles = ColumnNamer.getNames("SELECT * FROM HCL_subscription", sql);
			}
			subModel = new DefaultTableModel(dateArray, subTitles);
			subTable = new JTableHCL(subModel);
			JScrollPane subScroll = new JScrollPane(subTable);
			add(subScroll, BorderLayout.CENTER);
			add(new lowerButtons(), BorderLayout.SOUTH);
			setLocationRelativeTo(null);
			subTable.removeIDs();
			setVisible(true);
		}
		class lowerButtons extends JPanel {
			public lowerButtons() {
				JButton neue = new JButton("New...");
				JButton del = new JButton("Delete");
				setLayout(new GridLayout(1, 2));
				neue.addActionListener(new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent e) {
						editBox edit = new editBox();
					}
				});
				add(neue);
				add(del);
			}
		}
		class editBox extends JFrame {
			private datePane pane;
			public editBox() {
				setLayout(new GridLayout(2, 1));
				pane = new datePane();
				saveCancel buts = new saveCancel();
				add(pane);
				add(buts);
				setSize((int) (x * 0.2), (int) (y * 0.2));
				setLocationRelativeTo(null);
				setVisible(true);
			}
			class saveCancel extends JPanel {
				public saveCancel() {
					JButton save = new JButton("Save");
					JButton cancel = new JButton("Cancel");
					setLayout(new GridLayout(1, 2));
					save.addActionListener(new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if (!(pane.getDate().equals(""))) {
								String newDate = Stuff.setBold() + pane.getDate() + Stuff.endBold();
								String[][] newArray = new String[dateArray.length + 1][];
								for (int i = 0; i < dateArray.length; i++) {
									newArray[i] = dateArray[i];
								}
								newArray[dateArray.length] = new String[subTitles.length];
								System.out.println(Arrays.toString(dateArray[0]));
								newArray[dateArray.length][2] = newDate;
								dateArray = newArray;
								subModel = new DefaultTableModel(dateArray, subTitles);
								subTable.setModel(subModel);
								subTable.removeIDs();
							}
							else {
								JOptionPane.showMessageDialog(null, "Enter a valid date");
							}
							dispose();
						}
					});
					cancel.addActionListener(new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent e) {
							dispose();
						}
					});
					add(save);
					add(cancel);
				}
			}
		}
	}
}
