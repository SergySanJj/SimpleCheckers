package com.sergeiyarema.checkers.field.checker;

import android.graphics.Color;
import android.graphics.Paint;

public class Checker {
    private CheckerState state = CheckerState.NORMAL;
    private CheckerColor color;
    private Paint paint;

    private static int errorColor = Color.rgb(0, 250, 0);

    public static final int whiteNormal = Color.rgb(200, 200, 200);
    public static final int blackNormal = Color.rgb(230, 20, 20);



    Checker(CheckerColor checkerColor) {
        color = checkerColor;
        paint = new Paint();
    }

    public CheckerColor getColor() {
        return color;
    }

    public CheckerState getState() {
        return state;
    }

    public void setState(CheckerState state) {
        this.state = state;
        updatePaint();
    }

    public Paint updatePaint() {
        switch (color) {
            case BLACK:
                paint.setColor(blackNormal);
                break;
            case WHITE:
                paint.setColor(whiteNormal);
                break;
            default:
                paint.setColor(errorColor);
        }

        return paint;
    }
}
