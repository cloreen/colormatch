package com.colormatch.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
 * TODO - Add scoring
 * TODO - Replace target color label text with color graphic
 * TODO - Remove unused code
 * DONE - Remove target color text upon timer expiration
 * DONE - Remove color button clickability upon time exhaustion
 * DONE - Make incorrect confirmation message display quicker
 * DONE - Lengthen timer duration
 * DONE - Set timer to stop on correct color button click
 * DONE - Make color button size independent of instruction text
 * DONE - Add a timer
 * DONE - Write a reset() method to reset colors and target color // Put main JPanel w/ functinality in separate class
 * DONE - Add incorrect selection to incorrect confirmation text
 * DONE - Modify colors to be more distinct (may require using custom in place of provided colors)
 * DONE - Move confirmation message from button to top JLabel
 * DONE - Set top label to reset after alerting the user of an incorrect choice
 * DONE - Assure target color is always among displayed colors
 * DONE - Ensure each button's color is unique in the grid
 * DONE - Update target color to reflect all possible colors
 * DONE - Add logic to apply correct grammar to instruction text e.g. "Click ('a' vs 'an') xxxx button!" - changed 'a' to 'the'
 * DONE - Put project up on GitHub (public)
 * DONE - Remove text from buttons
 * DONE - Add all custom colors to buttonColors array
 * DONE - Streamline CustomColor retrieval
 * DONE - Randomize JButton color assignment
 * DONE - Get RGB values for hundreds of colors
 **/

public class MainGameFrame extends JFrame {
    public static Container base = new Container();
    public static Container container1 = new Container();
    public static Container container2 = new Container();
    final Lock lock = new ReentrantLock();
    final Condition textHasChanged = lock.newCondition();
    public static JLabel instr;
    public static JLabel targetColor;
    public static String targetColorStr;
    public static String colorStr;
    public static String[] colorStrings;
    public static List<String> colors = new ArrayList<String>();
    public static int targetColorIndex;
    public static int targetColorInt;
    public static int colorInt;
    public static int tempInt;
    public static int secondsToWait;
    public static int buttonWait;
    public static int instrButtonWait;
    public static JButton[] jButtonArray = {};
    private static final float BTN_SIZE = 24f;
    private static final String BTN_TEXT = " ";
    public static CustomColor customColor;
    private int[] randomInts = { };


    public static Color buttonColors[] = { Color.RED, Color.GREEN, Color.GRAY, Color.ORANGE, Color.YELLOW,
            Color.PINK, Color.BLUE, Color.BLACK, Color.WHITE, CustomColor.LIGHT_BLUE, Color.DARK_GRAY,
            CustomColor.ORANGE, CustomColor.DARK_ORANGE, CustomColor.LIGHT_BROWN, CustomColor.BROWN,
            CustomColor.DARK_BROWN, Color.LIGHT_GRAY, CustomColor.LIGHT_GREEN, CustomColor.DARK_BLUE,
            CustomColor.DARK_RED, CustomColor.PURPLE, CustomColor.DARK_GREEN };


    public MainGameFrame() {
        super("Game Frame");
        setSize(312, 388);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GamePanel();

    }



    /*******************************************************************************************************************
     * GAME PANEL - Sets up GUI for game
     ******************************************************************************************************************/
    private void GamePanel() {
        final JPanel panel = new JPanel();
        final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        final ScheduledExecutorService timerExecutor = Executors.newScheduledThreadPool(1);

        // Set layout for container holding instruction components
        container1.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        // Set layout for container holding color buttons
        GridLayout container2Grid = new GridLayout(3, 2);
        container2Grid.setVgap(3);
        container2Grid.setHgap(3);
        container2.setLayout(container2Grid);

        container2.getMaximumSize();

        // Set layout for panel holding containers
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));



        // Setup Components
        //==============================================================================================================

        instr = new JLabel("Click a button!", JLabel.CENTER);   // Top instruction text

        final JLabel timerLabel = new JLabel("", JLabel.CENTER);    // Timer text
        final JButton restartButton = new JButton("Play Again");    // "Restart" button to play new game
        restartButton.setVisible(false);                            // Set Restart Button as invisible/non-functional
        restartButton.setEnabled(false);
        restartButton.setRolloverEnabled(false);
        restartButton.setFocusable(false);

        secondsToWait = 11;                                         // Set timer to countdown from 10 seconds. At zero,
        Runnable timerTask = new Runnable() {                       // display Restart Button and remove color button
            public void run() {                                     // clickability.
                secondsToWait--;
                if (secondsToWait != 0) {
                    timerLabel.setText("Time remaining: " + secondsToWait + " seconds.");
                } else {
                    instr.setText("Too late! You ran out of time!");
                    timerLabel.setText("Time remaining: " + secondsToWait + " seconds.");
                    for(int i=0; i < jButtonArray.length; i++) {
                        for(ActionListener aL : jButtonArray[i].getActionListeners()) {
                            jButtonArray[i].removeActionListener(aL);
                        }
                    }
                    targetColor.setVisible(false);
                    restartButton.setVisible(true);
                    restartButton.setEnabled(true);
                    restartButton.setRolloverEnabled(true);
                    restartButton.setFocusable(true);
                    restartButton.addActionListener(new ActionListener() {  // Add action listener to Restart Button so
                        public void actionPerformed(ActionEvent e) {        // game will properly restart when clicked.
                            container1.removeAll();
                            container2.removeAll();
                            GamePanel();
                        }
                    });
                    timerExecutor.shutdown();
                }
            }
        };
        timerExecutor.scheduleAtFixedRate(timerTask, 1, 1, TimeUnit.SECONDS);

        // Instantiate color buttons and assign to array
        JButton button1 = new JButton(BTN_TEXT);
        JButton button2 = new JButton(BTN_TEXT);
        JButton button3 = new JButton(BTN_TEXT);
        JButton button4 = new JButton(BTN_TEXT);
        JButton button5 = new JButton(BTN_TEXT);
        JButton button6 = new JButton(BTN_TEXT);
        jButtonArray = new JButton[]{button1, button2, button3, button4, button5, button6};

        // Create array of random numbers as a list from which to choose random color
        randomInts = new Random().ints(0, 22).distinct().limit(6).toArray();
        System.out.print("The numbers randomly generated are: ");
        for (int i = 0; i < randomInts.length; i++) {
            System.out.print(" " + randomInts[i] + " ");
        }

        // Instruction text for clicking color buttons
        targetColor = new JLabel("Click the " + getTargetColor(randomInts) + " button!", JLabel.CENTER);

        // Set color buttons
        for (int i = 0; i < jButtonArray.length; i++) {
            jButtonArray[i].setFont(jButtonArray[i].getFont().deriveFont(BTN_SIZE));
            jButtonArray[i].setBackground(buttonColors[randomInts[i]]);
            jButtonArray[i].setMnemonic(KeyEvent.VK_E);
            jButtonArray[i].setActionCommand("enable");
            tempInt = i;
            final int finalI = i;
            final int finalI1 = i;
            jButtonArray[i].addActionListener(new ActionListener() {        // Action Listener displays confirmation
                public void actionPerformed(ActionEvent e) {                // message if user clicks correct button
                    if (finalI == targetColorIndex) {                       // and restarts game a second later.
                        buttonWait = 3;                                     // If user clicks incorrect button, displays
                        Runnable task = new Runnable() {                    // appropriate text.
                            public void run() {
                                buttonWait--;
                                if (buttonWait == 2) {
                                    instr.setText("You were right!");
                                    for(int i=0; i < jButtonArray.length; i++) {
                                        for(ActionListener aL : jButtonArray[i].getActionListeners()) {
                                            jButtonArray[i].removeActionListener(aL);
                                        }
                                    }
                                    timerExecutor.shutdownNow();
                                } else if (buttonWait == 1) {
                                    container1.removeAll();
                                    container2.removeAll();
                                    GamePanel();
                                } else if (buttonWait == 0) {
                                    executor.shutdown();
                                }
                            }
                        };
                        executor.scheduleAtFixedRate(task, 1, 1, TimeUnit.SECONDS);
                    } else {
                        try {
                            changeInstructionText(finalI1);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }

                        System.out.println("The label text is now " + instr.getText());
                        System.out.println("Now leaving actionPerformed() method." + " Current time is: " + System.currentTimeMillis());
                    }
                }
            });
            container2.add(jButtonArray[i]);
        }



        getColorStrings(randomInts);    // Assign names to the random colors




        // Place components in containers and containers in panel
        //==============================================================================================================

        // Place instruction JLabel
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.ipady = 1;
        container1.add(instr, constraints);

        // Place timer JLabel
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.ipady = 2;
        container1.add(timerLabel, constraints);

        // Place restart JButton
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.insets = new Insets(5, 5, 5, 5);
        container1.add(restartButton, constraints);

        // Place target color instruction JLabel
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.ipady = 1;
        container1.add(targetColor, constraints);

        panel.add(container1);
        panel.add(container2);

        add(panel);
        setVisible(true);

    }

    // Change the instruction text according to user's button click
    private void changeInstructionText(final int index) throws InterruptedException {
        System.out.println("Now inside changeInstructionText() method.");
        final ScheduledExecutorService instrExecutor = Executors.newScheduledThreadPool(1);
        instrButtonWait = 2;
        Runnable instrTask = new Runnable() {

            public void run() {
                instrButtonWait--;
                if (instrButtonWait == 1) {
                    instr.setText("Wrong one!!" + " Try again!"/*" You chose " + colors.get(index) + "!"*/); //+ " Current time is: " + System.currentTimeMillis());
                    System.out.println("The label is now: " + instr.getText());
                } else if (instrButtonWait == 0) {
                    instrExecutor.shutdown();
                }
            }
        };
        instrExecutor.scheduleAtFixedRate(instrTask, 1, 1, TimeUnit.SECONDS);
        System.out.println("Now leaving changeInstructionText() method." + " Current time is: " + System.currentTimeMillis());
    }




    /*******************************************************************************************************************
    * MANAGE COLORS - Assign color names by button, create index from which to choose random target color.
    *******************************************************************************************************************/
     // Create Index for random target color
    private String getTargetColor(int[] randomInts) {
       targetColorIndex = ThreadLocalRandom.current().nextInt(0, 6);
       targetColorInt = randomInts[targetColorIndex];
        System.out.println("The target color index is: " + targetColorInt);
        switch(targetColorInt) {
            case 0:
                targetColorStr = "RED";
                break;
            case 1:
                targetColorStr = "GREEN";
                break;
            case 2:
                targetColorStr = "GRAY";
                break;
            case 3:
                targetColorStr = "ORANGE";
                break;
            case 4:
                targetColorStr = "YELLOW";
                break;
            case 5:
                targetColorStr = "PINK";
                break;
            case 6:
                targetColorStr = "BLUE";
                break;
            case 7:
                targetColorStr = "BLACK";
                break;
            case 8:
                targetColorStr = "WHITE";
                break;
            case 9:
                targetColorStr = "LIGHT BLUE";
                break;
            case 10:
                targetColorStr = "DARK GRAY";
                break;
            case 11:
                targetColorStr = "ORANGE";
                break;
            case 12:
                targetColorStr = "DARK ORANGE";
                break;
            case 13:
                targetColorStr = "LIGHT BROWN";
                break;
            case 14:
                targetColorStr = "BROWN";
                break;
            case 15:
                targetColorStr = "DARK BROWN";
                break;
            case 16:
                targetColorStr = "LIGHT GRAY";
                break;
            case 17:
                targetColorStr = "LIGHT GREEN";
                break;
            case 18:
                targetColorStr = "DARK BLUE";
                break;
            case 19:
                targetColorStr = "DARK RED";
                break;
            case 20:
                targetColorStr = "PURPLE";
                break;
            case 21:
                targetColorStr = "DARK GREEN";
                break;
        }
        return targetColorStr;
    }


    // Assign color name to each button
    private String getColorStrings(int[] randomInts) {
        for (int i : randomInts) {
            switch(i) {
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
                    colorStr = "ORANGE";
                    break;
                case 4:
                    colorStr = "YELLOW";
                    break;
                case 5:
                    colorStr = "PINK";
                    break;
                case 6:
                    colorStr = "BLUE";
                    break;
                case 7:
                    colorStr = "BLACK";
                    break;
                case 8:
                    colorStr = "WHITE";
                    break;
                case 9:
                    colorStr = "LIGHT BLUE";
                    break;
                case 10:
                    colorStr = "DARK GRAY";
                    break;
                case 11:
                    colorStr = "ORANGE";
                    break;
                case 12:
                    colorStr = "DARK ORANGE";
                    break;
                case 13:
                    colorStr = "LIGHT BROWN";
                    break;
                case 14:
                    colorStr = "BROWN";
                    break;
                case 15:
                    colorStr = "DARK BROWN";
                    break;
                case 16:
                    colorStr = "LIGHT GRAY";
                    break;
                case 17:
                    colorStr = "LIGHT GREEN";
                    break;
                case 18:
                    colorStr = "DARK BLUE";
                    break;
                case 19:
                    colorStr = "DARK RED";
                    break;
                case 20:
                    colorStr = "PURPLE";
                    break;
                case 21:
                    colorStr = "DARK GREEN";
                    break;
            }
            colors.add(colorStr);
        }
        return colorStr;
    }

    // END MANAGE COLORS -----------------------------------------------------------------------------------------------
    /*******************************************************************************************************************
     ******************************************************************************************************************/


    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus" +
                                    ".NimbusLookAndFeel");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // MAIN Method -----------------------------------------------------------------------------------------------------
    public static void main(String[] args) {

        MainGameFrame.setLookAndFeel();
        MainGameFrame frame = new MainGameFrame();


    }
}
