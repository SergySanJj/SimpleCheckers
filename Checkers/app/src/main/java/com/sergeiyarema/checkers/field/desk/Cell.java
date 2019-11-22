package com.sergeiyarema.checkers.field.desk;

import android.graphics.Paint;


public class Cell {
    private int colorIdle;
    private int colorActive;
    private Paint paint;
    private CellState currentState = CellState.IDLE;

    public Cell() {
    }

    public Cell(int colorIdle, int colorActive) {
        this.colorIdle = colorIdle;
        this.colorActive = colorActive;

        this.paint = new Paint();
        this.paint.setColor(this.colorIdle);
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
}
