package com.sergeiyarema.checkers.field;

import android.graphics.Canvas;
import com.sergeiyarema.checkers.field.checker.Checkers;
import com.sergeiyarema.checkers.field.desk.Desk;

public class Field implements Drawable {
    private int fieldSize;
    private Desk desk;
    private Checkers checkers;

    public Field(int fieldSize) {
        this.fieldSize = fieldSize;
        desk = new Desk(fieldSize);
        checkers = new Checkers(fieldSize);
    }

    @Override
    public void draw(Canvas canvas) {
        desk.draw(canvas);
        checkers.draw(canvas);
    }
}
