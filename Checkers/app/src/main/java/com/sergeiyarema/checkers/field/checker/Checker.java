package com.sergeiyarema.checkers.field.checker;

import android.graphics.Color;
import android.graphics.Paint;

public class Checker {
    private CheckerState state = CheckerState.NORMAL;
    private CheckerColor color;
    private Paint paint;

    private static int errorColor = Color.rgb(0, 250, 0);

    private static int whiteNormal = Color.rgb(200, 200, 200);
    private static int whiteQueen = Color.rgb(200, 250, 200);

    private static int blackNormal = Color.rgb(230, 20, 20);
    private static int blackQueen = Color.rgb(230, 50, 50);


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
    }

    public Paint getPaint() {
        if (state == CheckerState.NORMAL) {
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
        } else if (state == CheckerState.QUEEN) {
            switch (color) {
                case BLACK:
                    paint.setColor(blackQueen);
                    break;
                case WHITE:
                    paint.setColor(whiteQueen);
                    break;
                default:
                    paint.setColor(errorColor);
            }
        }
        return paint;
    }
}
