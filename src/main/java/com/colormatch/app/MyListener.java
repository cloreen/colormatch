package com.colormatch.app;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ben on 6/29/2016.
 */
public class MyListener implements PropertyChangeListener {

    private JLabel label;

    public MyListener(JLabel label) {
        this.label = label;
    }

    public void propertyChange(PropertyChangeEvent pce) {

        if (label.getText().contains("Wrong one!!")) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            label.setText("Try again!");
        } else {
            // do nothing.
/*            try {
                changeInstrucionText();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            System.out.println("The label text is now " + instr.getText());

            String labelText = instr.getText();
            if(labelText.contains("Wrong"))
                instr = new JLabel("Click a button!");*/

/*
                        Timer timer = new Timer(0, this);
                        timer.setInitialDelay(3000);
*/
//                        resetInstructionText();
//                        instr.setText("Wrong one!!");
/*                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }*/
//                        instr.setText("Click a button!");
            System.out.println("Now leaving actionPerformed() method." + " Current time is: " + System.currentTimeMillis());
        }
    }
}
