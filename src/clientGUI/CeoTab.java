package clientGUI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by bahafeld on 17.03.2016.
 */
class CeoTab extends JPanel {
    public CeoTab() {
        setLayout(new GridLayout(2, 2));
        JButton test = new JButton("CEO Testing");
        JButton test2 = new JButton("Testing more");
        add(test);
        add(test2);
    }
}

