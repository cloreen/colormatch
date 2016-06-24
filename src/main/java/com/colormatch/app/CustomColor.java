package com.colormatch.app;

import java.awt.*;

/**
 * Created by Ben on 6/24/2016.
 */
public class CustomColor {
    public static Color newColor = null;

    public CustomColor() {

    }

    public CustomColor(CustColor custColor) {
        getCustomColor(custColor);
    }


    public enum CustColor {LIGHT_BLUE, ORCHID, PURPLE, SALMON, LIGHT_BROWN, PEACH, LIGHT_GRAY, WHITE, LIGHT_GREEN, STEEL_BLUE};

    public Color getCustomColor(CustColor custColor) {

        switch(custColor) {
            case LIGHT_BLUE:
                newColor = new Color(170, 224, 242, 1);
                break;
            case ORCHID:
                newColor = new Color(238, 112, 250, 1);
                break;
            case PURPLE:
                newColor = new Color(168, 0, 252, 1);
                break;
            case SALMON:
                newColor = new Color(252, 174, 192, 1);
                break;
            case LIGHT_BROWN:
                newColor = new Color(135, 64, 71, 1);
                break;
            case PEACH:
                newColor = new Color(255, 194, 156, 1);
                break;
            case LIGHT_GRAY:
                newColor = new Color(214, 212, 210, 1);
                break;
            case LIGHT_GREEN:
                newColor = new Color(148, 255, 160, 1);
                break;
            case STEEL_BLUE:
                newColor = new Color(138, 141, 194, 1);
                break;
        }
        return newColor;
    }

}



/*
    public CustomColor() {

    }

    public enum CustColor {LIGHT_BLUE, ORCHID, PURPLE, SALMON, LIGHT_BROWN, PEACH, LIGHT_GRAY, WHITE, LIGHT_GREEN, STEEL_BLUE};

        public static Color assignCustColor() {
            LIGHT_BLUE = new Color();

        }
    }

    public static Color getCustColor(String colorStr) {
        Color colorOut;
        switch(colorStr) {
            case("LIGHT_BLUE"):
                colorOut = CustColor.LIGHT_BLUE(new Color(170, 224, 242, 1));

        }
    }



}
*/
