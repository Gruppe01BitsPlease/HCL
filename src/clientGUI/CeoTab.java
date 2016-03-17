package clientGUI;

import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;

/**
 * Created by bahafeld on 17.03.2016.
 */
class CeoTab extends JPanel {
    public CeoTab() {
        JButton button;
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        boolean shouldFill = false;
        boolean shouldWeightX = true;
        if (shouldFill) {
            //natural height, maximum width
            c.fill = GridBagConstraints.HORIZONTAL;
        }

        button = new JButton("Button 1");
        if (shouldWeightX) {
            c.weightx = 1;
        }
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        add(button, c);

        button = new JButton("Button 2");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 10;
        c.gridx = 	GridBagConstraints.RELATIVE;
        c.gridy = GridBagConstraints.RELATIVE;
        add(button, c);

        button = new JButton("Button 3");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 2;
        add(button, c);

        button = new JButton("Long-Named Button 4");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 40;      //make this component tall
        c.weightx = 0.0;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 3;
        add(button, c);

        button = new JButton("5");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(10,0,0,0);  //top padding
        c.gridx = 0;       //aligned with button 2
        c.gridwidth = 2;   //2 columns wide
        c.gridy = 5;       //third row
        add(button, c);
    }
}

