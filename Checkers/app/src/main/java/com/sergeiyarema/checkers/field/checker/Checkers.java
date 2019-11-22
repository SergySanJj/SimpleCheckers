package com.sergeiyarema.checkers.field.checker;

import android.graphics.Canvas;
import com.sergeiyarema.checkers.field.Drawable;

public class Checkers implements Drawable {
    private int cellSize;
    private int checkerSize;
    private int fieldSize;
    private Checker[][] checkers;

    public Checkers(int fieldSize) {
        this.fieldSize = fieldSize;
        initCheckers();
    }

    private void initCheckers() {
        checkers = new Checker[fieldSize][fieldSize];

        initWhites();
        initBlacks();
    }

    private void initWhites() {
        for (int row = 0; row < 3; row++) {
            initRow(row, CheckerColor.WHITE);
        }
    }

    private void initBlacks() {
        for (int row = fieldSize - 1; row > fieldSize - 3 - 1; row--) {
            initRow(row, CheckerColor.BLACK);
        }
    }

    private void initRow(int row, CheckerColor color) {
        for (int i = 0; i < fieldSize; i++) {
            if ((row + i) % 2 == 1)
                checkers[row][i] = new Checker(color);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        cellSize = canvas.getWidth() / fieldSize;
        checkerSize = (int) (cellSize * 0.2);
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                if ((j + i) % 2 == 1 &&
                        checkers[i][j] != null) {
                    drawChecker(i, j, checkers[i][j], canvas);
                }
            }
        }
    }

    private void drawChecker(int row, int column, Checker checker, Canvas canvas) {
        int cx = column * cellSize + cellSize / 2;
        int cy = row * cellSize + cellSize / 2;
        canvas.drawCircle(cx, cy, checkerSize, checker.getPaint());
    }


}
