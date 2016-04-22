package clientGUI;

import backend.DeliveryManager;
import backend.LinkManager;
import backend.OrderManager;
import backend.SQL;
import com.sun.scenario.effect.impl.sw.java.JSWBlend_COLOR_BURNPeer;
import org.jfree.data.time.Day;
import sun.awt.image.ImageWatched;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;


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
        private editBox editDatesWindow;

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
                        editDatesWindow = new editBox();
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

                DeliveryManager manager = new DeliveryManager(new SQL());

                JButton save = new JButton("Save");
                JButton cancel = new JButton("Cancel");
                setLayout(new GridLayout(1, 2));

                save.addActionListener( e-> {

                    ArrayList<String> datesArrayAsArrayList= new ArrayList<>();

                    for(String[] date : dateArray){
                    }

                    if(editDatesWindow != null) {
                        if (editDatesWindow.isSingleDelivery()) {
                          //  if()
                            manager.addDate(order_id, editDatesWindow.getStartDate().toString());
                        }
                        else {
                            manager.addDates(order_id, editDatesWindow.getStartDate(), editDatesWindow.getEndDate(), editDatesWindow.getInterval(), editDatesWindow.getDays());
                        }
                    }
                   /*
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
                    *//*if (deletedDates.size() > 0 && removeResult != 1 || addedDates.size() > 0 && addResult != 1) {
                        JOptionPane.showMessageDialog(null, "There was a problem with updating the dates");
                        System.out.println("Remove result: " + removeResult + "\nAdd result: " + addResult);
                    }*//*
                    //else {*/
                    dispose();
                    //}
                    foodTab.generate();
                    refresh();
                });
                cancel.addActionListener(e->{
                    dispose();
                });

                add(save);
                add(cancel);
            }
        }
        class editBox extends JFrame {

            private datePane startDatePane;
            private datePane endDatePane;
            private ArrayList<JCheckBox> dayBoxes;
            private ArrayList<JLabel> dayLabels;
            private JComboBox<String> intervalDropdown;

            public editBox() {

                setLayout(new GridLayout(5, 1));
                setResizable(false);

                JPanel startDatePanel = new JPanel(new GridLayout(1,2));
                JPanel endDatePanel = new JPanel(new GridLayout(1,2));
                JPanel weekIntervalPanel = new JPanel(new GridLayout(1,2));
                JPanel daysPanel = new JPanel(new GridLayout(2,7));
                JPanel saveCancelPanel = new JPanel();

                // StartDatePanel
                startDatePanel.add(new JLabel("Start Date"));

                startDatePane = new datePane(null);
                startDatePane.setDate(LocalDate.now().toString());
                startDatePanel.add(startDatePane);
                // StartDatePanel


                // EndDatePanel
                endDatePanel.add(new JLabel("End Date"));

                endDatePane = new datePane(null);
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
                    DeliveryManager manager = new DeliveryManager(new SQL());

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
                            DayOfWeek[] dayArray = new DayOfWeek[days.size()]; for(int i=0; i<days.size(); i++){dayArray[i] = days.get(i);}

                            String[] daysToAdd = manager.getDatesToBeAdded(order_id,editDatesWindow.getStartDate(),
                                    editDatesWindow.getEndDate(),intervalDropdown.getSelectedIndex(),
                                    dayArray);

                            System.out.println(Arrays.toString(daysToAdd));
                            dates = new String[daysToAdd.length][6];

                            for(int i=0; i<daysToAdd.length; i++){
                                dates[i][0]=Stuff.bold("0"); dates[i][1] = Stuff.bold("0"); dates[i][2] = Stuff.bold(daysToAdd[i]); dates[i][3] = Stuff.bold("0"); dates[i][4] = Stuff.bold("0"); dates[i][5] = Stuff.bold("0");

                            }
                        }
                    String[][] temp = new String[dateArray.length+dates.length][];

                    for(int i=0; i<dateArray.length; i++){
                        temp[i] = dateArray[i];
                    }
                    int counter = 0;
                    for(int j=dateArray.length; j<temp.length; j++){
                        temp[j] = dates[counter];
                        counter++;
                    }
                    dateArray = temp; // I think these all these global variables should have been methods instead ftr - Olav
                    subModel = new DefaultTableModel(dateArray, subTitles); // -||-
                    subTable.setModel(subModel); // -||-
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

