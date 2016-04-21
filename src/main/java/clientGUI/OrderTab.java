package clientGUI;

import backend.DeliveryManager;
import backend.OrderManager;
import backend.SQL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Creates the JPanel that is used as a tab in tabbedMenu
 */
class OrderTab extends GenericList {
    private static String query = "SELECT order_id, customer_name, price, adress, postnr" +
            " , order_date, active FROM HCL_order NATURAL JOIN HCL_customer  WHERE HCL_order.active = 1 " +
            "ORDER BY customer_name ASC";
    private static String[][] foreignKeys = {{ "SELECT DISTINCT customer_id, customer_name FROM HCL_customer WHERE active=1;", "1" }};
    //Tab name, foreign PK, link table name, other table name, foreign identifier
    private SQL sql;
    public OrderTab(SQL sql, int role) {
        super(query, "HCL_order", null, foreignKeys, sql, role);
        add(new GenericSearch(), BorderLayout.SOUTH);
        this.sql = sql;
    }
    public int delete(int nr) {
        OrderManager mng = new OrderManager(sql);
        int ret = mng.delete(nr);
        System.out.println("Delete code" + ret);
        return ret;
    }
    public int generate(String[] args) {
        OrderManager mng = new OrderManager(sql);
        int customerid = Integer.parseInt(args[1]);
        int price = 0;
        int postnr = 0;
        try {
            price = Integer.parseInt(args[2]);
            postnr = Integer.parseInt(args[4]);
        }
        catch (Exception e) {
        }
        System.out.println("Generate arguments: " + Arrays.toString(args));
        System.out.println(customerid + " - " + price + " - " + args[3] + " - " + postnr + " - " + args[6]);
        return mng.generate(customerid, price, args[3], postnr, args[6]);
    }
    public void edit(int id, boolean newItem) {
        editWindow edit = new editWindow(id, newItem);
    }

    class editWindow extends JFrame {
        //Addeddates has dates as strings, YYYYMMDD
        private ArrayList<String> addedDates = new ArrayList<>();
        //deletedDates has ID's
        private ArrayList<String> deletedDates = new ArrayList<>();
        private linkTab foodTab;
        private DefaultTableModel subModel;
        private String getDateQuery;
        private JTableHCL subTable;
        private String[][] dateArray;
        private String[] subTitles;
        private int order_id;
        private boolean newOrder;
        private editFields editFields;
        private String[] selected;
        private String[][] titles;

        public editWindow(int order_id, boolean newOrder) {
            this.order_id = order_id;
            this.newOrder = newOrder;
            setSize(Stuff.getWindowSize(0.5,0.5));
            setTitle("Order");
            setLayout(new BorderLayout());
            JTabbedPane tabs = new JTabbedPane();
            String selectedQuery = "SELECT * FROM HCL_order WHERE order_id = " + order_id;
            titles = ColumnNamer.getNamesWithOriginals(selectedQuery, sql);
            selected = new String[titles[0].length];
            if (!newOrder) {
                selected = sql.getRow(selectedQuery);
            }
            editFields = new editFields(titles[1], selected, newOrder, foreignKeys[0], sql);
            //tabs.addTab("Info", new infoTab());
            tabs.addTab("Edit", editFields);
            tabs.addTab("Dates", new dateTab());
            String[] link = { "Foods", "food_id", "HCL_order_food", "HCL_food", "name" };
            foodTab = new linkTab(link, "order_id", order_id, sql, newOrder);
            tabs.addTab("Foods", foodTab);
            add(tabs, BorderLayout.CENTER);
            add(new lowerButtons(), BorderLayout.SOUTH);
            setLocationRelativeTo(null);
            setVisible(true);
        }
        class dateTab extends JPanel {
            public dateTab() {
                getDateQuery = "SELECT * FROM HCL_deliveries WHERE order_id = " + order_id + " AND active = 1 ORDER BY delivery_date ASC";
                dateArray = new String[0][];
                if (!newOrder) {
                    dateArray = sql.getStringTable(getDateQuery, false);
                    subTitles = ColumnNamer.getNames(getDateQuery, sql);
                }
                else {
                    subTitles = ColumnNamer.getNames("SELECT * FROM HCL_deliveries", sql);
                }
                subModel = new DefaultTableModel(dateArray, subTitles);
                subTable = new JTableHCL(subModel);
                JScrollPane subScroll = new JScrollPane(subTable);
                subTable.removeIDs();
                setLayout(new BorderLayout());
                add(subScroll, BorderLayout.CENTER);
                add(new buttons(), BorderLayout.SOUTH);
            }
            class buttons extends JPanel {
                public buttons() {
                    setLayout(new GridLayout(1, 2));
                    JButton neue = new JButton("New...");
                    JButton del = new JButton("Delete");
                    neue.addActionListener(e-> {
                            editBox edit = new editBox();
                    });
                    del.addActionListener(e-> {
                            int[] sel = subTable.getSelectedRows();
                            for (int i = 0; i < sel.length; i++) {
                                for (int j = 0; j < subModel.getColumnCount(); j++) {
                                    if (subModel.getValueAt(sel[i], j) != null) {
                                        String value = Stuff.setGrey() + subModel.getValueAt(sel[i], j) + Stuff.endGrey();
                                        subModel.setValueAt(value, sel[i], j);
                                    }
                                }
                                deletedDates.add((String) subModel.getValueAt(sel[i], 0));
                            }
                            subTable.setModel(subModel);
                    });
                    add(neue);
                    add(del);
                }
            }
        }
        class lowerButtons extends JPanel {
            public lowerButtons() {
                JButton save = new JButton("Save");
                JButton cancel = new JButton("Cancel");
                setLayout(new GridLayout(1, 2));
                save.addActionListener( e-> {
                    String[] newValues = editFields.getNewValues();
                    if (newOrder) {
                        order_id = generate(newValues);
                    }
                    System.out.println("New values: " + Arrays.toString(newValues));
                    System.out.println("Selected original: " + Arrays.toString(selected));
                    if (!newOrder) {
                        for (int i = 0; i < newValues.length; i++) {
                            if (!(newValues[i].equals(selected[i]))) {
                                sql.update("HCL_order", titles[0][i], "order_id", Integer.toString(order_id), newValues[i]);
                            }
                        }
                    }
                    DeliveryManager mng = new DeliveryManager(sql);
                    int removeResult = 0;
                    int addResult = 0;
                    for (int i = 0; i < dateArray.length; i++) {
                        System.out.println("Date: " + dateArray[i][2]);
                        String value = (String)subModel.getValueAt(i,2);
                        if (value.contains(Stuff.setBold())) {
                            addResult = mng.addDate(order_id, Stuff.removeHTML(dateArray[i][2]));
                        }
                        if (value.contains(Stuff.setGrey())) {
                            //System.out.println("Removing delivery id: "+Integer.parseInt(dateArray[i][0]));
                            removeResult = mng.removeDate(Integer.parseInt(dateArray[i][0]));
                        }
                    }
                    /*if (deletedDates.size() > 0 && removeResult != 1 || addedDates.size() > 0 && addResult != 1) {
                        JOptionPane.showMessageDialog(null, "There was a problem with updating the dates");
                        System.out.println("Remove result: " + removeResult + "\nAdd result: " + addResult);
                    }*/
                    //else {
                        dispose();
                    //}
                    foodTab.generate();
                    refresh();
                });
                cancel.addActionListener(e-> dispose());

                add(save);
                add(cancel);
            }
        }
        class editBox extends JFrame {
            private JComboBox<String> intervalBox;
            private JComboBox<String> numberBox;
            private datePane pane;
            public editBox() {
                setLayout(new GridLayout(4, 2));
                pane = new datePane(null);
                JLabel dateLabel = new JLabel("Date");
                JLabel intervalLabel = new JLabel("Interval");
                JLabel numberLabel = new JLabel("Number of dates");
                pane.setDate(LocalDate.now().toString());
                String[] boxChoices = { "Single", "Weekly", "Monthly" };
                intervalBox = new JComboBox<>(boxChoices);
                String[] numberBoxChoices = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
                numberBox = new JComboBox<>(numberBoxChoices);
                add(dateLabel);
                add(pane);
                add(intervalLabel);
                add(intervalBox);
                add(numberLabel);
                add(numberBox);
                numberBox.setEnabled(false);
                intervalBox.addItemListener(e->{
                        if (!(intervalBox.getSelectedItem().equals("Single"))) {
                            numberBox.setEnabled(true);
                            dateLabel.setText("First date");
                            System.out.println("List selection!");
                        }
                        else {
                            dateLabel.setText("Date");
                            numberBox.setEnabled(false);
                        }
                });

                JButton save = new JButton("Save");
                JButton cancel = new JButton("Cancel");
                save.addActionListener(e->{
                        String[] newDates = new String[1];
                        if (!(intervalBox.getSelectedItem().equals("Single"))) {
                            newDates = new String[numberBox.getSelectedIndex() + 1];
                            for (int i = 0; i < newDates.length; i++) {
                                if (intervalBox.getSelectedItem().equals(("Weekly"))) {
                                    pane.addDays(7);
                                    newDates[i] = pane.getDate();
                                    System.out.println("Add date: " + pane.getDate());
                                }
                                else if (intervalBox.getSelectedItem().equals("Monthly")) {
                                    pane.addMonths(1);
                                    newDates[i] = pane.getDate();
                                    System.out.println("Add date: " + pane.getDate());
                                }
                            }
                        }
                        else {
                            newDates[0] = pane.getDate();
                        }
                        String[][] newArray = new String[dateArray.length + newDates.length][];
                        for (int i = 0; i < dateArray.length; i++) {
                            newArray[i] = dateArray[i];
                        }
                        for (int j = 0; j < newDates.length; j++) {
                            if (newDates[j] != null && !(newDates[j].equals(""))) {
                                addedDates.add(newDates[j]);
                                String newDate = Stuff.setBold() + newDates[j] + Stuff.endBold();
                                newArray[dateArray.length + j] = new String[subTitles.length];
                                //System.out.println(Arrays.toString(dateArray[0]));
                                newArray[dateArray.length + j][2] = newDate;
                            } else {
                                JOptionPane.showMessageDialog(null, "Enter a valid date");
                            }
                            dispose();
                        }
                        dateArray = newArray;
                        subModel = new DefaultTableModel(dateArray, subTitles);
                        subTable.setModel(subModel);
                        subTable.removeIDs();
                });
                cancel.addActionListener(e-> dispose());
                add(save);
                add(cancel);
                setSize(Stuff.getWindowSize(0.3, 0.3));
                setLocationRelativeTo(null);
                setVisible(true);
            }
        }
    }
}

