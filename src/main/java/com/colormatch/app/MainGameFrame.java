package com.colormatch.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * COLOR MATCH is a game where the user is given a color to to click,
 * along with a grid of colored buttons to choose from. The user is
 * expected to click the button with the color corresponding to the
 * color the application asks them to click. The user is given feedback
 * on whether their choice was right or wrong.
 *
 * Created by Ben on 6/22/2016.
 *
 *
 * Future improvements:
 *  -   Include a running score total, along with highest possible score
 *  -   Include a timer. If the user does not choose a button in time, the
 *      grid resets and a new target color is given to the user
 *  -   Add sound effects
 * */

/***
 * TODO - Put project up on GitHub (public)
 * TODO - Streamline CustomColor retrieval
 * TODO - Remove text from buttons
 * TODO - Add all custom colors to buttonColors array
 * TODO - Randomize JButton color assignment
 * TODO - Assure target color is always among displayed colors
 * TODO - Write a reset() method to reset colors and target color
 * TODO - Get RGB values for hundreds of colors
 */

public class MainGameFrame extends JFrame {
    public static Container base = new Container();
    public static Container container1 = new Container();
    public static Container container2 = new Container();
    public static int colorIndex;
    public static CustomColor customColor = new CustomColor();

    public static Color buttonColors[] = {Color.RED, Color.GREEN, Color.GRAY, customColor.getCustomColor(CustomColor.CustColor.LIGHT_BLUE), Color.ORANGE, Color.YELLOW};

    public MainGameFrame() {
        super("Game Frame");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        GridLayout panelGrid = new GridLayout(3, 1);
        panel.setLayout(panelGrid);

        String colorStr = "";
        JLabel instr = new JLabel("Click a button!", JLabel.CENTER);

        JLabel targetColor = new JLabel("Click a " + getTargetColor() + " button!", JLabel.CENTER);

/*
        instr.setSize(100, 50);
        container1.add(instr);
*/

        GridLayout grid = new GridLayout(3, 2);
        container2.setLayout(grid);

        Dimension bDimension = new Dimension(50, 50);
        List<JButton> jButtonList = new ArrayList<JButton>();
        JButton button1 = new JButton("1");
        JButton button2 = new JButton("2");
        JButton button3 = new JButton("3");
        JButton button4 = new JButton("4");
        JButton button5 = new JButton("5");
        JButton button6 = new JButton("6");

        final JButton[] jButtonArray = { button1, button2, button3, button4, button5, button6 };
        for (int i = 0; i < jButtonArray.length; i++) {
            jButtonArray[i].setSize(bDimension);
            jButtonArray[i].setBackground(buttonColors[i]);
            jButtonArray[i].setMnemonic(KeyEvent.VK_E);
            jButtonArray[i].setActionCommand("enable");
            final int finalI1 = i;
            jButtonArray[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
//                    jButtonArray[finalI].setBackground(Color.RED);
                    if (finalI1 == colorIndex) {
                        jButtonArray[finalI1].setText("You were right!");
                    } else {
                        jButtonArray[finalI1].setText("Wrong one!!");
                    }
                }
            });
            container2.add(jButtonArray[i]);
        }

/*        container2.add(new Button("1"));
        container2.add(new Button("2"));
        container2.add(new Button("3"));
        container2.add(new Button("4"));
        container2.add(new Button("5"));
        container2.add(new Button("6"));*/
//        panel.add(container1);
        panel.add(instr);
        panel.add(targetColor);
        panel.add(container2);
        add(panel);
        setVisible(true);
    }

    public void initGrid() {
        GridLayout grid = new GridLayout(3, 2);
        add(new Button("1"));
        add(new Button("2"));
        add(new Button("3"));
        add(new Button("4"));
        add(new Button("5"));
        add(new Button("6"));
        container2.setLayout(grid);
    }

    private String getTargetColor() {
       colorIndex = ThreadLocalRandom.current().nextInt(0, 5 + 1);
       String colorStr = "";
        switch(colorIndex) {
            case 0:
                colorStr = "RED";
                break;
            case 1:
                colorStr = "GREEN";
                break;
            case 2:
                colorStr = "GRAY";
                break;
            case 3:
                colorStr = "BLUE";
                break;
            case 4:
                colorStr = "ORANGE";
                break;
            case 5:
                colorStr = "YELLOW";
                break;
        }
        return colorStr;
    }

    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus" +
                                    ".NimbusLookAndFeel");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        MainGameFrame.setLookAndFeel();
        MainGameFrame frame = new MainGameFrame();
        //frame.show();
    }

}
