package com.sergeiyarema.checkers;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import androidx.annotation.RequiresApi;

public class Cell {
    private int color;
    private Paint paint;
    private int size;

    public Cell(int size, int color) {
        this.size = size;
        this.color = color;
        this.paint = new Paint();
        this.paint.setColor(color);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        this.paint.setColor(color);
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }
}
