package com.sergeiyarema.checkers.field.checker;

import android.graphics.Canvas;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import com.sergeiyarema.checkers.field.Coords;
import com.sergeiyarema.checkers.field.Drawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Checkers implements Drawable {
    private int cellSize;
    private int checkerSize;
    private int fieldSize;
    private Checker[][] checkersTable;
    private Paint queenPaint;

    public Checkers(int fieldSize) {
        this.fieldSize = fieldSize;
        initCheckers();
        queenPaint = new Paint();
        queenPaint.setColor(Color.rgb(0, 0, 0));
    }

    private void initCheckers() {
        checkersTable = new Checker[fieldSize][fieldSize];

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
                checkersTable[row][i] = new Checker(color);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        cellSize = canvas.getWidth() / fieldSize;
        checkerSize = (int) (cellSize * 0.2);
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                if ((j + i) % 2 == 1 &&
                        checkersTable[i][j] != null) {
                    drawChecker(i, j, checkersTable[i][j], canvas);
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
        return checkersTable[coords.y][coords.x];
    }

    public Checker getChecker(int x, int y) {
        return checkersTable[y][x];
    }

    public Checker getChecker(Coords coords) {
        return checkersTable[coords.y][coords.x];
    }

    public Coords find(Checker checker) {
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                if (checkersTable[i][j] == null)
                    continue;
                if (checkersTable[i][j].equals(checker))
                    return new Coords(j, i);
            }
        }
        return null;
    }

    public void remove(int x, int y) {
        checkersTable[y][x] = null;
    }

    public boolean move(Checker checker, int x, int y) {
        boolean haveBeaten = false;
        Coords found = find(checker);
        if (found != null)
            haveBeaten = beat(found.x, found.y, x, y);
        checkersTable[y][x] = checker;
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
            if (checkersTable[yCurr][xCurr] != null)
                haveBeaten = true;
            checkersTable[yCurr][xCurr] = null;
        }
        return haveBeaten;
    }

    public int getCheckerSize() {
        return checkerSize;
    }

    public void updateQueens() {
        // BLACKS
        for (Checker checker : checkersTable[0]) {
            if (checker == null)
                continue;
            if (checker.getColor() == CheckerColor.BLACK) {
                checker.setState(CheckerState.QUEEN);
                Log.println(Log.DEBUG, "QUEEN", "QUEEN");
            }
        }

        // WHITES
        for (Checker checker : checkersTable[fieldSize - 1]) {
            if (checker == null)
                continue;
            if (checker.getColor() == CheckerColor.WHITE) {
                checker.setState(CheckerState.QUEEN);
                Log.println(Log.DEBUG, "QUEEN", "QUEEN");

            }
        }
    }

    public List<Coords> buildAvailable(Checker checker) {
        Coords coords = find(checker);
        int x = coords.x;
        int y = coords.y;
        List<Coords> res = new ArrayList<>();
        if (checker.getState() == CheckerState.NORMAL) {
            if (checker.getColor() == CheckerColor.BLACK) {
                tryMove(x, y, -1, -1, checker, res);
                tryMove(x, y, 1, -1, checker, res);

                tryBeat(x, y, -1, 1, checker, res);
                tryBeat(x, y, 1, 1, checker, res);
            } else {
                tryBeat(x, y, -1, -1, checker, res);
                tryBeat(x, y, 1, -1, checker, res);

                tryMove(x, y, -1, 1, checker, res);
                tryMove(x, y, 1, 1, checker, res);
            }

        } else if (checker.getState() == CheckerState.QUEEN) {
            moveInVector(x, y, 1, 1, checker, res);
            moveInVector(x, y, 1, -1, checker, res);
            moveInVector(x, y, -1, 1, checker, res);
            moveInVector(x, y, -1, -1, checker, res);
        }

        return res;
    }

    private void moveInVector(int x, int y, int vx, int vy, Checker current, List<Coords> res) {
        while (border(x + vx) && border(y + vy)) {
            x = x + vx;
            y = y + vy;
            Checker other = getChecker(x, y);
            if (other != null) {
                if (other.getColor() == current.getColor())
                    return;
                if (border(x + vx) && border(y + vy)) {
                    other = getChecker(x + vx, y + vy);
                    if (other == null) {
                        res.add(new Coords(x + vx, y + vy));
                    }
                    return;
                }
            } else {
                res.add(new Coords(x, y));
            }
        }
    }

    private void beatInVector(int x, int y, int vx, int vy, Checker current, List<Coords> res) {
        while (border(x + vx) && border(y + vy)) {
            x = x + vx;
            y = y + vy;
            Checker other = getChecker(x, y);
            if (other != null) {
                if (other.getColor() == current.getColor())
                    return;
                if (border(x + vx) && border(y + vy)) {
                    other = getChecker(x + vx, y + vy);
                    if (other == null) {
                        res.add(new Coords(x + vx, y + vy));
                    }
                    return;
                }
            }
        }
    }

    private void tryMove(int x, int y, int dx, int dy, Checker current, List<Coords> res) {
        if (border(x, dx) && border(y, dy)) {
            Checker checkerOther = getChecker(x + dx, y + dy);
            if (checkerOther == null) {
                res.add(new Coords(x + dx, y + dy));
            } else if (checkerOther.getColor() != current.getColor()
                    && border(x, 2 * dx)
                    && border(y, 2 * dy)) {
                checkerOther = getChecker(x + 2 * dx, y + 2 * dy);
                if (checkerOther == null) {
                    res.add(new Coords(x + 2 * dx, y + 2 * dy));
                }
            }
        }
    }

    public Map<Checker, List<Coords>> getAvailableListByColor(CheckerColor color) {
        Map<Checker, List<Coords>> res = new HashMap<>();
        for (Checker[] row : checkersTable) {
            for (Checker checker : row) {
                if (checker == null)
                    continue;
                if (checker.getColor() == color) {
                    List<Coords> availableForThisChecker = buildAvailable(checker);
                    res.put(checker, availableForThisChecker);
                }
            }
        }
        return res;
    }

    private void tryBeat(int x, int y, int dx, int dy, Checker current, List<Coords> res) {
        if (border(x, dx) && border(y, dy)) {
            Checker checkerOther = getChecker(x + dx, y + dy);
            if (checkerOther != null && checkerOther.getColor() != current.getColor()
                    && border(x, 2 * dx)
                    && border(y, 2 * dy)) {
                checkerOther = getChecker(x + 2 * dx, y + 2 * dy);
                if (checkerOther == null) {
                    res.add(new Coords(x + 2 * dx, y + 2 * dy));
                }
            }
        }
    }

    private boolean border(int x, int dx) {
        return border(x + dx);
    }

    private boolean border(int x) {
        return (x >= 0) && x < fieldSize;
    }

    public List<Coords> canBeat(Checker checker) {
        List<Coords> res = new ArrayList<>();
        Coords coords = find(checker);
        int x = coords.x;
        int y = coords.y;
        if (checker.getState() == CheckerState.NORMAL) {
            if (checker.getColor() == CheckerColor.BLACK) {
                tryBeat(x, y, -1, 1, checker, res);
                tryBeat(x, y, 1, 1, checker, res);
            } else {
                tryBeat(x, y, -1, -1, checker, res);
                tryBeat(x, y, 1, -1, checker, res);
            }
        } else {
            beatInVector(x, y, 1, 1, checker, res);
            beatInVector(x, y, 1, -1, checker, res);
            beatInVector(x, y, -1, 1, checker, res);
            beatInVector(x, y, -1, -1, checker, res);
        }
        return res;
    }
}
