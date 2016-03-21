package clientGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import backend.*;
import com.sun.org.apache.xpath.internal.operations.Or;

class GenericList extends JPanel {
    //This is a generic list, shown in the middle of the tab where needed
    //Use the type int to choose which edit window appears whe double clicking
    private String[][] table;
    private String[] titles;
    private JTable list;
    private SQL sql;
    private int x;
    private int y;

    public GenericList(String[][] table, String[] titles) {
        try {
            this.sql = new SQL(new Logon(new File()));
        }
        catch (Exception e) {}
        this.table = table;
        this.titles = titles;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        x = (int) (screen.width * 0.75);
        y = (int) (screen.height * 0.75);
        setLayout(new BorderLayout());
        list = new JTable(table, titles) {
            private static final long serialVersionUID = 1L;

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        if (this instanceof OrderTab) {
            list.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        String[] selected = table[list.getSelectedRow()];
                        int index = list.getSelectedRow();
                        orderWindow edit = new orderWindow(selected, index);
                    }
                }
            });
        }
        else if (this instanceof EmployeeTab) {
            list.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        String[] selected = table[list.getSelectedRow()];
                        int index = list.getSelectedRow();
                        employeeWindow edit = new employeeWindow(selected, index);
                    }
                }
            });
        }
        JScrollPane scroll = new JScrollPane(list);
        add(scroll, BorderLayout.CENTER);
    }
    abstract class editWindow extends JFrame {
        //This class automatically adds text fields for the columns in the table.
        public editWindow(String[] selected, int index) {
            setTitle("Edit order");
            setLayout(new GridLayout(selected.length + 1, 2));
            setSize((int) (x * 0.4), (int) (y * (titles.length + 2) * 0.05));
            setLocationRelativeTo(null);
            setAlwaysOnTop(true);
            setResizable(false);
            ArrayList<JTextField> fields = new ArrayList<>();
            for (int i = 0; i < titles.length; i++) {
                JLabel j = new JLabel(titles[i]);
                JTextField k = new JTextField(selected[i]);
                fields.add(k);
                add(j);
                add(k);
            }
            setVisible(true);
            JButton save = new JButton("Save");
            JButton cancel = new JButton("Cancel");
            cancel.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            save.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (int i = 0; i < selected.length; i++) {
                        selected[i] = fields.get(i).getText();
                    }
                    table[index] = selected;
                    list.repaint();
                    dispose();
                }
            });
            add(save);
            add(cancel);
        }
    }
    private class orderWindow extends editWindow {
        public orderWindow(String[] selected, int index) {
            super(selected, index);
            setTitle("Edit order");
        }
    }

    private class employeeWindow extends editWindow {
        public employeeWindow(String[] selected, int index) {
            super(selected, index);
            setTitle("Edit employee");
        }
    }
}