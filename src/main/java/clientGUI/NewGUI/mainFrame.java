package clientGUI.NewGUI;

import clientGUI.LogOnGUI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by bahafeld on 04.04.2016...
 * For HCL
 */

public class mainFrame {
    JFrame editet;

     JFrame buildFrame(){
        JFrame frame = new JFrame("mainFrame");
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/titleIcon.png"));
        frame.setIconImage(image); // Put your own image instead of null
        try {
            UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e1) {
                e1.printStackTrace();
            }
        }
         return frame;
    }


    public static void main (String[] args){
        mainFrame toolkit = new mainFrame();
        JFrame parent = toolkit.buildFrame();


        LogOnPanel test = new LogOnPanel();
        parent.add(test.getPanel());


        parent.pack();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        double x = (double) screen.width * 0.25;
        double y = (double) screen.height * 0.3;
        parent.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        parent.setSize((int) x, (int) y);
        parent.setLocationRelativeTo(null);
        parent.setVisible(true);
    }
}
