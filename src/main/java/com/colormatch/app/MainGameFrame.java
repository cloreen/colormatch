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
 * TODO - Make color button size independent of instruction text
 * TODO - Set timer to stop on color button click
 * TODO - Remove color button clickability upon time exhaustion
 * TODO - Add scoring
 * TODO - Lengthen timer duration
 * TODO - Replace target color label text with color graphic
 * TODO - Make incorrect confirmation message display quicker
 * TODO - Remove unused code
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

public class MainGameFrame extends JFrame implements ActionListener {
    public static Container base = new Container();
    public static Container container1 = new Container();
    public static Container container2 = new Container();
    final Lock lock = new ReentrantLock();
    final Condition textHasChanged = lock.newCondition();
    public static JLabel instr;
    public static String targetColorStr;
    public static String colorStr;
    public static String[] colorStrings;
    public static List<String> colors = new ArrayList<String>();
    public static int targetColorIndex;
    public static int targetColorInt;
    public static int colorInt;
    public static int tempInt;
    public static int secondsToWait;
    public static JButton[] jButtonArray = {};
//    public static CustomColor customColor = new CustomColor();
    public static CustomColor customColor;

    public static Color buttonColors[] = { Color.RED, Color.GREEN, Color.GRAY, Color.ORANGE, Color.YELLOW,
            Color.PINK, Color.BLUE, Color.BLACK, Color.WHITE, CustomColor.LIGHT_BLUE, Color.DARK_GRAY,
            CustomColor.ORANGE, CustomColor.DARK_ORANGE, CustomColor.LIGHT_BROWN, CustomColor.BROWN,
            CustomColor.DARK_BROWN, Color.LIGHT_GRAY, CustomColor.LIGHT_GREEN, CustomColor.DARK_BLUE,
            CustomColor.DARK_RED, CustomColor.PURPLE, CustomColor.DARK_GREEN };


    public MainGameFrame() {
        super("Game Frame");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GamePanel();

    }

    private void GamePanel() {
        final JPanel panel = new JPanel();

        Dimension container2Dim = new Dimension(150, 100);
        container2.setPreferredSize(container2Dim);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();



        String colorStr = "";
        instr = new JLabel("Click a button!", JLabel.CENTER);
        MyListener labelListener = new MyListener(instr);
        instr.addPropertyChangeListener(labelListener);

        final JLabel timerLabel = new JLabel("", JLabel.CENTER);
        final JButton restartButton = new JButton("Play Again");
        restartButton.setVisible(false);
        restartButton.setEnabled(false);
        restartButton.setRolloverEnabled(false);
        restartButton.setFocusable(false);
        final ScheduledExecutorService timerExecutor = Executors.newScheduledThreadPool(1);
        secondsToWait = 4;
        Runnable task = new Runnable() {
            //            @Override
            public void run() {
                secondsToWait--;
                if (secondsToWait != 0) {
                    timerLabel.setText("Time remaining: " + secondsToWait + " seconds."); //+ " Current time is: " + System.currentTimeMillis());
                } else {
                    instr.setText("Too late! You ran out of time!");
                    timerLabel.setText("Time remaining: " + secondsToWait + " seconds.");
//                    panel.remove(timerLabel);
                    restartButton.setVisible(true);
                    restartButton.setEnabled(true);
                    restartButton.setRolloverEnabled(true);
                    restartButton.setFocusable(true);
                    restartButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            container2.removeAll();
                            panel.removeAll();
                            GamePanel();
                        }
                    });
                    timerExecutor.shutdown();
                }
            }
        };
        timerExecutor.scheduleAtFixedRate(task, 1, 1, TimeUnit.SECONDS);

        GridLayout grid = new GridLayout(3, 2);
        container2.setLayout(grid);

        Dimension bDimension = new Dimension(100, 50);
        List<JButton> jButtonList = new ArrayList<JButton>();
        JButton button1 = new JButton();
        JButton button2 = new JButton();
        JButton button3 = new JButton();
        JButton button4 = new JButton();
        JButton button5 = new JButton();
        JButton button6 = new JButton();

        jButtonArray = new JButton[]{button1, button2, button3, button4, button5, button6};
        int[] randomInts = new Random().ints(0, 22).distinct().limit(6).toArray();
        System.out.print("The numbers randomly generated are: ");
        for (int i = 0; i < randomInts.length; i++) {
            System.out.print(" " + randomInts[i] + " ");
        }

        JLabel targetColor = new JLabel("Click the " + getTargetColor(randomInts) + " button!", JLabel.CENTER);


        for (int i = 0; i < jButtonArray.length; i++) {
            jButtonArray[i].setSize(bDimension);
            jButtonArray[i].setBackground(buttonColors[randomInts[i]]);
            jButtonArray[i].setMnemonic(KeyEvent.VK_E);
            jButtonArray[i].setActionCommand("enable");
            tempInt = i;
//            MyListener listener = new MyListener(jButtonArray[i], instr, tempInt, colorIndex);
            final int finalI = i;
            final int finalI1 = i;
            jButtonArray[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
//                    jButtonArray[finalI].setBackground(Color.RED);

                    if (finalI == targetColorIndex) {

                        final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
                        secondsToWait = 4;
                        Runnable task = new Runnable() {
                            //            @Override
                            public void run() {
                                secondsToWait--;
                                if (secondsToWait == 3) {
                                    instr.setText("Click a button!"); //+ " Current time is: " + System.currentTimeMillis());
                                } else if (secondsToWait == 2) {
                                    instr.setText("You were right!");
                                } else if (secondsToWait == 1) {
                                    container2.removeAll();
                                    GamePanel();
                                } else if (secondsToWait == 0) {
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

        getColorStrings(randomInts);


        // Place instruction JLabel
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.ipady = 1;
        panel.add(instr, constraints);

        // Place timer JLabel
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.ipady = 5;
        panel.add(timerLabel, constraints);

        // Place restart JButton
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 2;
//        constraints.ipady = 1;
        constraints.insets = new Insets(5, 5, 5, 5);
        panel.add(restartButton, constraints);

        // Place target color instruction JLabel
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.ipady = 1;
        panel.add(targetColor, constraints);

        // Place color buttons
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.ipady = 1;
        constraints.anchor = GridBagConstraints.PAGE_END;
        constraints.insets = new Insets(10, 0, 0, 0);
        constraints.gridwidth = 2;
        panel.add(container2, constraints);

        add(panel);
        setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {

            if (tempInt == targetColorIndex) {
                instr.setText("You were right!");
            } else {
                System.out.println("The label text is now " + instr.getText());

                String labelText = instr.getText();
                if(labelText.contains("Wrong"))
                    instr = new JLabel("Click a button!");

                System.out.println("Now leaving actionPerformed() method." + " Current time is: " + System.currentTimeMillis());
            }
    }

    private void changeInstructionText(final int index) throws InterruptedException {
        System.out.println("Now inside changeInstructionText() method.");
        final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        secondsToWait = 3;
        Runnable task = new Runnable() {

            public void run() {
                secondsToWait--;
                if (secondsToWait == 2) {
                    instr.setText("Wrong one!!" + " You chose " + colors.get(index) + "!"); //+ " Current time is: " + System.currentTimeMillis());
                    System.out.println("The label is now: " + instr.getText());
                } else if (secondsToWait == 1) {
                    instr.setText("Wrong one!!" + " You chose " + colors.get(index) + "!"); /*+ " Current time is: " + System.currentTimeMillis());*/
                    System.out.println("The label is now: " + instr.getText());                    // do nothing
                } else if (secondsToWait == 0) {
                    executor.shutdown();
                }
            }
        };
        executor.scheduleAtFixedRate(task, 1, 1, TimeUnit.SECONDS);

        System.out.println("Now leaving changeInstructionText() method." + " Current time is: " + System.currentTimeMillis());
    }

    public static void resetInstructionText() {
        System.out.println("Now inside resetInstructionText() method." + " Current time is: " + System.currentTimeMillis());
    }

/*
    private void addToRandomIntList(JButton[] jButtonArray, ArrayList<Integer> randIntList) {
        for (int i = 0; i < jButtonArray.length; i++) {
            int randInt = ThreadLocalRandom.current().nextInt(0, 25 + 1);
            if (randIntList.size() < 1) {
                randIntList.add(randInt);
            } else {
                for (int item : randIntList) {
                    if (item != randInt) {
                        randIntList.add(item);
                    } else {
                        int newInt = checkForDuplicate(item);
                        randIntList.add(item);
                        } while (item != randInt);
                    }
                }
            }
        }
*/

/*
    private int checkForDuplicate(int item) {
//        Boolean isUnique = false;
        int randInt = ThreadLocalRandom.current().nextInt(0, 25 + 1);
        if (item != randInt) {
            // do nothing
        } else {
            checkForDuplicate(item);
        }
        return randInt;
    }
*/


/*
    private void addRandomInt(List<Integer> randIntList, JButton[] jButtonArray) {
        int newInt = ThreadLocalRandom.current().nextInt(0, jButtonArray.length + 1);
        if (randIntList.size() < 1) {
            randIntList.add(newInt);
        } else {
//            checkForDuplicateRandomInt(newInt, (ArrayList<Integer>) randIntList, jButtonArray);
        }
    }
*/

/*
    private void checkForDuplicateRandomInt(int newInt, ArrayList<Integer> randIntList, JButton[] jButtonArray) {
        List<Integer> duplicateList = new ArrayList<Integer>();
        */
/*for (int index : randIntList) {
            duplicateList.add(randIntList.get(index));
            System.out.println("Index " + index + " in duplicateList is: " + duplicateList.get(index));
        }*//*

        for (int item : randIntList) {
            if (item != newInt) {
                duplicateList.add(newInt);
            } else {
                newInt = ThreadLocalRandom.current().nextInt(0, jButtonArray.length + 1);
                checkForDuplicateRandomInt(newInt, randIntList, jButtonArray);
            }
        }
    }
*/

/*
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
*/

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

/*
    private void getButtonColorStrings(int[] randomInts) {
    }
*/

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
        if (instr.getText() == "Wrong one!!")
            resetInstructionText();
    }

}
