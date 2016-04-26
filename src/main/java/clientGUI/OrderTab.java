package clientGUI;

import backend.DeliveryManager;
import backend.OrderManager;
import backend.SQL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static java.time.temporal.ChronoUnit.DAYS;


class OrderTab extends GenericList {
    private static String query = "SELECT order_id, customer_name, price, adress, postnr" +
            " , order_date, active FROM HCL_order NATURAL JOIN HCL_customer  WHERE HCL_order.active = 1 " +
            "ORDER BY customer_name ASC";
    //private static String query = "";
    private static String[][] foreignKeys = {{ "SELECT DISTINCT customer_id, customer_name FROM HCL_customer WHERE active=1;", "1" }};
    //Tab name, foreign PK, link table name, other table name, foreign identifier
    private SQL sql;
    public OrderTab(SQL sql, int role) {
        super(query, "HCL_order", null, foreignKeys, sql, role, 1);
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
        EditWindow edit = new EditWindow(id, newItem);
    }

    class EditWindow extends JFrame {
        //Addeddates has dates as strings, YYYYMMDD
        //deletedDates has ID's
        private LinkTab foodTab;
        private DefaultTableModel subModel;
        private String getDateQuery;
        private JTableHCL subTable;
        private String[][] dateArray;
        private String[] subTitles;
        private int order_id;
        private boolean newOrder;
        private EditFields editFields;
        private String[] selected;
        private String[][] titles;
        private EditBox editDatesWindow;

        public EditWindow(int order_id, boolean newOrder) {
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
            editFields = new EditFields(titles[1], selected, newOrder, foreignKeys[0], sql);
            //tabs.addTab("Info", new infoTab());
            tabs.addTab("Edit", editFields);
            tabs.addTab("Dates", new DateTab());
            String[] link = { "Foods", "food_id", "HCL_order_food", "HCL_food", "name" };
            foodTab = new LinkTab(link, "order_id", order_id, sql, newOrder);
            tabs.addTab("Foods", foodTab);
            add(tabs, BorderLayout.CENTER);
            add(new LowerButtons(), BorderLayout.SOUTH);
            setLocationRelativeTo(null);
            setVisible(true);
        }
        class DateTab extends JPanel {
            public DateTab() {
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
                add(new Buttons(), BorderLayout.SOUTH);
            }
            class Buttons extends JPanel {
                public Buttons() {
                    setLayout(new GridLayout(1, 2));
                    JButton neue = new JButton("New...");
                    JButton del = new JButton("Delete");
                    neue.addActionListener(e-> {
                        editDatesWindow = new EditBox();
                    });
                    del.addActionListener(e-> {
                        int[] sel = subTable.getSelectedRows();
                        for (int i = 0; i < sel.length; i++) {
                            for (int j = 0; j < subModel.getColumnCount(); j++) {
                                if (subModel.getValueAt(sel[i], j) != null) {
                                    String value = Stuff.grey(Stuff.removeHTML((String)subModel.getValueAt(sel[i], j)));
                                    System.out.println("Setting grey!: "+value);
                                    subModel.setValueAt(value, sel[i], j);
                                    System.out.println(subModel.getValueAt(sel[i], j));
                                }
                            }
                        }
                        subTable.setModel(subModel);
                    });
                    add(neue);
                    add(del);
                }
            }
        }
        class LowerButtons extends JPanel {
            public LowerButtons() {

                //DeliveryManager manager = new DeliveryManager(new SQL());

                JButton save = new JButton("Save");
                JButton cancel = new JButton("Cancel");
                setLayout(new GridLayout(1, 2));

                save.addActionListener( e-> {
                    int sure = JOptionPane.showConfirmDialog(EditWindow.this, "Are you sure?", "", JOptionPane.YES_NO_OPTION);
                    if (sure == 0) {
                        String[] newValues = editFields.getNewValues();
                        boolean success = true;
                        if (newOrder) {
                            boolean valid = true;
                            DataTyper.DataType[] dataTypes = DataTyper.getDataTypesSQL(titles[0]);
                            for (int i = 0; i < dataTypes.length; i++) {
                                if (dataTypes[i] == DataTyper.DataType.INT) {
                                    try {
                                        Integer.parseInt(newValues[i]);
                                    }
                                    catch (Exception k) {
                                        JOptionPane.showMessageDialog(EditWindow.this, "Please enter a valid number in field: " + titles[1][i]);
                                        valid = false;
                                        success = false;
                                    }
                                }
                            }
                            if (valid) {
                                order_id = generate(newValues);
                            }
                        }
                        else if (!newOrder) {
                            boolean valid = true;
                            DataTyper.DataType[] dataTypes = DataTyper.getDataTypesSQL(titles[0]);
                            for (int i = 0; i < dataTypes.length; i++) {
                                if (dataTypes[i] == DataTyper.DataType.INT && !(newValues[i].equals("") || newValues[i] == null)) {
                                    try {
                                        Integer.parseInt(newValues[i]);
                                    }
                                    catch (Exception k) {
                                        JOptionPane.showMessageDialog(EditWindow.this, "Please enter a valid number in field: " + titles[1][i]);
                                        valid = false;
                                        success = false;
                                    }
                                }
                            }
                            if (valid) {
                                for (int i = 0; i < newValues.length; i++) {
                                    if (!(newValues[i].equals(selected[i])) && !(newValues[i].equals("") || newValues[i] == null)) {
                                        sql.update("HCL_order", titles[0][i], "order_id", Integer.toString(order_id), newValues[i]);
                                    }
                                }
                            }
                        }
                        if (success) {
                            DeliveryManager mng = new DeliveryManager(sql);
                            int removeResult = 0;
                            int addResult = 0;
                            for (int i = 0; i < dateArray.length; i++) {
                                System.out.println("Date: " + dateArray[i][2]);
                                String value = (String) subModel.getValueAt(i, 2);
                                if (value.contains(Stuff.setBold())) {
                                    addResult = mng.addDate(order_id, Stuff.removeHTML(dateArray[i][2]));
                                }
                                if (value.contains(Stuff.setGrey())) {
                                    //System.out.println("Removing delivery id: "+Integer.parseInt(dateArray[i][0]));
                                    if (dateArray[i][0] != null) {
                                        removeResult = mng.removeDate(Integer.parseInt(Stuff.removeHTML(dateArray[i][0])));
                                    }
                                }
                            }
                            dispose();
                            foodTab.generate();
                            refresh();
                        }
                    }
                });
                cancel.addActionListener(e->{
                    dispose();
                });

                add(save);
                add(cancel);
            }
        }
        class EditBox extends JFrame {

            private DatePane startDatePane;
            private DatePane endDatePane;
            private ArrayList<JCheckBox> dayBoxes;
            private ArrayList<JLabel> dayLabels;
            private JComboBox<String> intervalDropdown;

            public EditBox() {

                setLayout(new GridLayout(5, 1));
                setResizable(false);

                JPanel startDatePanel = new JPanel(new GridLayout(1,2));
                JPanel endDatePanel = new JPanel(new GridLayout(1,2));
                JPanel weekIntervalPanel = new JPanel(new GridLayout(1,2));
                JPanel daysPanel = new JPanel(new GridLayout(2,7));
                JPanel saveCancelPanel = new JPanel();

                // StartDatePanel
                startDatePanel.add(new JLabel("Start Date"));

                startDatePane = new DatePane(null);
                startDatePane.setDate(LocalDate.now().toString());
                startDatePanel.add(startDatePane);
                // StartDatePanel


                // EndDatePanel
                endDatePanel.add(new JLabel("End Date"));

                endDatePane = new DatePane(null);
                endDatePane.setDate(LocalDate.now().plusMonths(1).toString());
                endDatePane.setEnabled(false);
                endDatePanel.add(endDatePane);
                // EndDatePanel


                // WeekIntervalPanel
                weekIntervalPanel.add(new JLabel("Week Interval"));

                String[] boxChoices = {"One Delivery", "Every Week", "Every 2nd Week", "Every 3rd Week","Every 4th Week"};
                intervalDropdown = new JComboBox<>(boxChoices);

                weekIntervalPanel.add(intervalDropdown);
                // WeekIntervalPanel

                // DaysPanel
                dayBoxes = new ArrayList<>();
                dayLabels = new ArrayList<>();

                for(int i = 1; i< DayOfWeek.values().length+1; i++){
                    JLabel thisLabel = new JLabel(DayOfWeek.of(i).toString().substring(0,1) + DayOfWeek.of(i).toString().substring(1,3).toLowerCase());
                    thisLabel.setEnabled(false);
                    dayLabels.add(thisLabel);
                    daysPanel.add(thisLabel);
                }

                for(int i=1; i < DayOfWeek.values().length+1; i++){
                    JCheckBox box = new JCheckBox();
                    box.setEnabled(false);
                    dayBoxes.add(box);
                    daysPanel.add(box);
                }
                //daysPanel.setEnabled(false);
                // DaysPanel


                // SaveCancelButton
                JButton saveButton = new JButton("Save");
                JButton cancelButton = new JButton("Cancel");

                saveCancelPanel.add(saveButton);
                saveCancelPanel.add(cancelButton);
                // SaveCancelButton

                add(startDatePanel);
                add(endDatePanel);
                add(weekIntervalPanel);
                add(daysPanel);
                add(saveCancelPanel);

                cancelButton.addActionListener(e->{
                    dispose();
                });
                intervalDropdown.addItemListener(e->{

                    boolean oneOrder = intervalDropdown.getSelectedIndex() != 0;

                    for(JLabel label : dayLabels){
                        label.setEnabled(oneOrder);
                    }

                    for(JCheckBox box : dayBoxes){
                        box.setEnabled(oneOrder);
                    }

                    endDatePane.setEnabled(oneOrder);

                });
                saveButton.addActionListener(e -> {
                    DeliveryManager manager = new DeliveryManager(sql);

                    String[][] dates;

                        if(intervalDropdown.getSelectedItem().equals(boxChoices[0])) {
                            dates = new String[1][];
                            dates[0]= new String[6];

                            dates[0][1] = Stuff.bold("0");
                            dates[0][2] = Stuff.bold(editDatesWindow.getStartDate().toString());
                            dates[0][3] = Stuff.bold("0");
                            dates[0][4] = Stuff.bold("0");
                            dates[0][5] = Stuff.bold("0");
                        }
                        else {
                            ArrayList<DayOfWeek> days = new ArrayList<>();

                            for(int i=0; i<dayBoxes.size(); i++){
                                if(dayBoxes.get(i).isSelected()){
                                    days.add(DayOfWeek.of(i+1));
                                }
                            }
                            DayOfWeek[] dayArray = new DayOfWeek[days.size()];
                            for(int i=0; i<days.size(); i++){
                                dayArray[i] = days.get(i);
                            }
                            LocalDate startDate = editDatesWindow.getStartDate();
                            //for (int i = 0; i < dayArray.length; i++) {

                            //}
                            //String[] boxChoices = {"One Delivery", "Every Week", "Every 2nd Week", "Every 3rd Week","Every 4th Week"};
                            ArrayList<String> datesToAdd = new ArrayList<>();
                            boolean complete = false;
                            System.out.println("Day array:" + Arrays.toString(dayArray));
                            long dayNumber = DAYS.between(startDate, editDatesWindow.getEndDate());
                            for (long i = 0; i < dayNumber; i++) {
                                for (int j = 0; j < dayArray.length; j++) {
                                    boolean success = false;
                                    while (!success) {
                                        if (startDate.getDayOfWeek() != dayArray[j]) {
                                            startDate = startDate.plusDays(1);
                                            System.out.println(startDate.getDayOfWeek());
                                        } else {
                                            success = true;
                                        }
                                    }
                                    LocalDate addDate = startDate;
                                    if (addDate.isBefore(editDatesWindow.getEndDate())) {
                                        datesToAdd.add(addDate.toString());
                                    }
                                    while (!complete) {
                                        if (intervalDropdown.getSelectedIndex() == 1) {
                                            addDate = addDate.plusDays(7);
                                            System.out.println("Seven days");
                                        } else if (intervalDropdown.getSelectedIndex() == 2) {
                                            addDate = addDate.plusDays(14);
                                            System.out.println("14 days");
                                        } else if (intervalDropdown.getSelectedIndex() == 3) {
                                            addDate = addDate.plusDays(21);
                                            System.out.println("21 days");
                                        } else if (intervalDropdown.getSelectedIndex() == 4) {
                                            addDate = addDate.plusDays(28);
                                            System.out.println("28 days");
                                        }
                                        if (addDate.isAfter(editDatesWindow.getEndDate())) {
                                            complete = true;
                                        }
                                    }

                                }
                            }
                            dates = new String[datesToAdd.size()][6];
                            for(int i=0; i<datesToAdd.size(); i++){
                                dates[i][0]=Stuff.bold("0");
                                dates[i][1] = Stuff.bold("0");
                                dates[i][2] = Stuff.bold(datesToAdd.get(i));
                                dates[i][3] = Stuff.bold("0");
                                dates[i][4] = Stuff.bold("0");
                                dates[i][5] = Stuff.bold("0");
                            }
                        }
                    String[][] temp = new String[dateArray.length+dates.length][];

                    for(int i=0; i<dateArray.length; i++){
                        temp[i] = dateArray[i];
                    }
                    int counter = 0;
                    for(int j = dateArray.length; j < temp.length; j++){
                        temp[j] = dates[counter];
                        counter++;
                    }
                    dateArray = temp;
                    subModel = new DefaultTableModel(dateArray, subTitles); // -||-
                    subTable.setModel(subModel); // -||-
                    subTable.removeIDs();
                    dispose();
                });

                setSize(Stuff.getWindowSize(0.4, 0.4));
                setLocationRelativeTo(null);
                setVisible(true);
            }

            public LocalDate getStartDate(){
                return LocalDate.parse(startDatePane.getDate());
            }
            public LocalDate getEndDate(){
                return LocalDate.parse(endDatePane.getDate());
            }
            public int getInterval(){
                return intervalDropdown.getSelectedIndex();
            }
            public boolean isSingleDelivery(){
                return intervalDropdown.getSelectedIndex() == 0;
            }
            public DayOfWeek[] getDays(){
                ArrayList<DayOfWeek> days = new ArrayList<>();

                for(int i=0; i<dayBoxes.size(); i++){
                    if(dayBoxes.get(i).isSelected()){
                        days.add(DayOfWeek.of(i+1));
                    }
                }
                DayOfWeek[] dayArray = new DayOfWeek[days.size()]; for(int i=0; i<days.size(); i++){dayArray[i] = days.get(i);}
                return dayArray;
            }
        }
    }
}

