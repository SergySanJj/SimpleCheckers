package com.sergeiyarema.checkers.field;

import android.graphics.Canvas;
import android.os.Build;
import androidx.annotation.RequiresApi;
import com.sergeiyarema.checkers.field.checker.Checker;
import com.sergeiyarema.checkers.field.checker.CheckerColor;
import com.sergeiyarema.checkers.field.checker.CheckerState;
import com.sergeiyarema.checkers.field.checker.Checkers;
import com.sergeiyarema.checkers.field.desk.Cell;
import com.sergeiyarema.checkers.field.desk.CellColor;
import com.sergeiyarema.checkers.field.desk.CellState;
import com.sergeiyarema.checkers.field.desk.Desk;

import java.lang.annotation.AnnotationTypeMismatchException;
import java.util.ArrayList;
import java.util.List;

public class Field implements Drawable {
    private int fieldSize;
    private Desk desk;
    private Checkers checkers;

    private Checker selected;
    private CheckerColor gameState = CheckerColor.BLACK;
    private List<Coords> availableCoords = new ArrayList<>();

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void activate(float x, float y) {
        Coords coords = Coords.toCoords(x, y, fieldSize, desk.getCellSize());
        if (coords == null)
            return;

        if (selected == null) {
            trySelect(coords);
        } else {
            for (Coords available : availableCoords) {
                if (coords.equals(available)) {
                    boolean beaten = checkers.move(selected, coords.x, coords.y);

                    reverseState();

                    break;
                }
            }
            unselectAll();
        }
        updateQueens();
    }

    private void trySelect(Coords coords) {
        Cell cell = desk.getCell(coords);
        Checker checker = checkers.getChecker(coords);

        if (cell != null && checker != null) {
            if (cell.getCellColor() == CellColor.BLACK) {
                if (checker.getColor().equals(gameState)) {
                    cell.setCurrentState(CellState.ACTIVE);
                    activateAvailable(coords.x, coords.y, checker);
                    selected = checker;
                }
            }
        }
    }

    public void reverseState() {
        if (gameState == CheckerColor.BLACK)
            gameState = CheckerColor.WHITE;
        else
            gameState = CheckerColor.BLACK;
    }

    public void unselectAll() {
        selected = null;
        availableCoords.clear();
        desk.unselect();
    }

    public void activateAvailable(int x, int y, Checker checker) {
        availableCoords.clear();
        if (checker.getState() == CheckerState.NORMAL) {
            if (checker.getColor() == CheckerColor.BLACK) {
                tryMove(x, y, -1, -1, checker);
                tryMove(x, y, 1, -1, checker);

                tryBeat(x, y, -1, 1, checker);
                tryBeat(x, y, 1, 1, checker);
            } else {
                tryBeat(x, y, -1, -1, checker);
                tryBeat(x, y, 1, -1, checker);

                tryMove(x, y, -1, 1, checker);
                tryMove(x, y, 1, 1, checker);
            }


        } else if (checker.getState() == CheckerState.QUEEN) {
            moveInVector(x, y, 1, 1, checker);
            moveInVector(x, y, 1, -1, checker);
            moveInVector(x, y, -1, 1, checker);
            moveInVector(x, y, -1, -1, checker);
        }

        for (Coords coords : availableCoords) {
            desk.getCell(coords).setCurrentState(CellState.ACTIVE);
        }
    }

    private void moveInVector(int x, int y, int vx, int vy, Checker current) {
        int xCurr = x;
        int yCurr = y;
        while (border(x + vx) && border(y + vy)) {
            x = x + vx;
            y = y + vy;
            Checker other = checkers.getChecker(x, y);
            if (other != null) {
                if (other.getColor() == current.getColor())
                    return;
                if (border(x + vx) && border(y + vy)) {
                    other = checkers.getChecker(x + vx, y + vy);
                    if (other == null) {
                        availableCoords.add(new Coords(x + vx, y + vy));
                    }
                    return;
                }
            } else {
                availableCoords.add(new Coords(x, y));
            }
        }
    }

    private void tryMove(int x, int y, int dx, int dy, Checker current) {
        if (border(x, dx) && border(y, dy)) {
            Checker checkerOther = checkers.getChecker(x + dx, y + dy);
            if (checkerOther == null) {
                availableCoords.add(new Coords(x + dx, y + dy));
            } else if (checkerOther.getColor() != current.getColor()
                    && border(x, 2 * dx)
                    && border(y, 2 * dy)) {
                checkerOther = checkers.getChecker(x + 2 * dx, y + 2 * dy);
                if (checkerOther == null) {
                    availableCoords.add(new Coords(x + 2 * dx, y + 2 * dy));
                }
            }
        }
    }

    private void tryBeat(int x, int y, int dx, int dy, Checker current) {
        if (border(x, dx) && border(y, dy)) {
            Checker checkerOther = checkers.getChecker(x + dx, y + dy);
            if (checkerOther != null && checkerOther.getColor() != current.getColor()
                    && border(x, 2 * dx)
                    && border(y, 2 * dy)) {
                checkerOther = checkers.getChecker(x + 2 * dx, y + 2 * dy);
                if (checkerOther == null) {
                    availableCoords.add(new Coords(x + 2 * dx, y + 2 * dy));
                }
            }
        }
    }

    private void updateQueens() {
        checkers.updateQueens();
    }

    private boolean border(int x, int dx) {
        return border(x + dx);
    }

    private boolean border(int x) {
        return (x >= 0) && x < fieldSize;
    }
}
