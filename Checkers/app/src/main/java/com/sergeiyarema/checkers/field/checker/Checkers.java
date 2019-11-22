package com.sergeiyarema.checkers.field.checker;

import android.graphics.Canvas;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import com.sergeiyarema.checkers.field.Coords;
import com.sergeiyarema.checkers.field.Drawable;

public class Checkers implements Drawable {
    private int cellSize;
    private int checkerSize;
    private int fieldSize;
    private Checker[][] checkers;
    private Paint queenPaint;

    public Checkers(int fieldSize) {
        this.fieldSize = fieldSize;
        initCheckers();
        queenPaint = new Paint();
        queenPaint.setColor(Color.rgb(0, 0, 0));
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
        canvas.drawCircle(cx, cy, checkerSize, checker.updatePaint());
        if (checker.getState() == CheckerState.QUEEN) {
            canvas.drawCircle(cx, cy, (int) (checkerSize * 0.5), queenPaint);
        }
    }

    public Checker getChecker(float x, float y) {
        Coords coords = Coords.toCoords(x, y, fieldSize, cellSize);
        if (coords == null)
            return null;
        return checkers[coords.y][coords.x];
    }

    public Checker getChecker(int x, int y) {
        return checkers[y][x];
    }

    public Checker getChecker(Coords coords) {
        return checkers[coords.y][coords.x];
    }

    public Coords find(Checker checker) {
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                if (checkers[i][j] == null)
                    continue;
                if (checkers[i][j].equals(checker))
                    return new Coords(j, i);
            }
        }
        return null;
    }

    public void remove(int x, int y) {
        checkers[y][x] = null;
    }

    public boolean move(Checker checker, int x, int y) {
        boolean haveBeaten = false;
        Coords found = find(checker);
        if (found != null)
            haveBeaten = beat(found.x, found.y, x, y);
        checkers[y][x] = checker;
        if (found != null) {
            remove(found.x, found.y);
        }
        return haveBeaten;
    }

    public boolean beat(int xStart, int yStart,
                        int xEnd, int yEnd) {
        boolean haveBeaten = false;
        int xDir = xEnd - xStart;
        xDir = xDir / Math.abs(xDir);
        int yDir = yEnd - yStart;
        yDir = yDir / Math.abs(yDir);
//        Log.println(Log.DEBUG, "beat", xStart + " " + yStart, )
        int xCurr = xStart;
        int yCurr = yStart;
        while (xEnd - xCurr != 0) {
            xCurr += xDir;
            yCurr += yDir;
            if (checkers[yCurr][xCurr] != null)
                haveBeaten = true;
            checkers[yCurr][xCurr] = null;
        }
        return haveBeaten;
    }

    public int getCheckerSize() {
        return checkerSize;
    }

    public void updateQueens() {
        // BLACKS
        for (Checker checker : checkers[0]) {
            if (checker == null)
                continue;
            if (checker.getColor() == CheckerColor.BLACK) {
                checker.setState(CheckerState.QUEEN);
                Log.println(Log.DEBUG, "QUEEN", "QUEEN");
            }
        }

        // WHITES
        for (Checker checker : checkers[fieldSize - 1]) {
            if (checker == null)
                continue;
            if (checker.getColor() == CheckerColor.WHITE) {
                checker.setState(CheckerState.QUEEN);
                Log.println(Log.DEBUG, "QUEEN", "QUEEN");

            }
        }
    }
}
