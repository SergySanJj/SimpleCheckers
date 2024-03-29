package com.sergeiyarema.checkers.field.desk;

import android.graphics.Color;
import android.graphics.Paint;


public class Cell {
    private CellColor cellColor;
    private int colorIdle;

    private Paint paint;
    private CellState currentState = CellState.IDLE;

    public static final int black = Color.rgb(50, 50, 50);
    public static final int white = Color.WHITE;
    public static final int colorActive = Color.rgb(10, 250, 20);

    public Cell(CellColor color) {
        cellColor = color;
        this.paint = new Paint();

        if (color == CellColor.BLACK) {
            setColorIdle(black);
        } else if (color == CellColor.WHITE) {
            setColorIdle(white);
        }
    }

    public int getColorIdle() {
        return colorIdle;
    }

    public void setColorIdle(int colorIdle) {
        this.colorIdle = colorIdle;
        this.paint.setColor(colorIdle);
    }

    public Paint getPaint() {
        return paint;
    }

    public CellState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(CellState currentState) {
        this.currentState = currentState;
        if (currentState == CellState.IDLE) {
            paint.setColor(colorIdle);
        } else if (currentState == CellState.ACTIVE) {
            paint.setColor(colorActive);
        }
    }

    public CellColor getCellColor() {
        return cellColor;
    }

    public void setCellColor(CellColor cellColor) {
        this.cellColor = cellColor;
    }
}
