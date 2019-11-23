package com.sergeiyarema.checkers.field;

import android.graphics.Canvas;
import android.os.Build;
import android.util.Log;
import android.view.View;
import androidx.annotation.RequiresApi;
import com.sergeiyarema.checkers.BotLogic;
import com.sergeiyarema.checkers.field.checker.Checker;
import com.sergeiyarema.checkers.field.checker.CheckerColor;
import com.sergeiyarema.checkers.field.checker.Checkers;
import com.sergeiyarema.checkers.field.desk.Cell;
import com.sergeiyarema.checkers.field.desk.CellColor;
import com.sergeiyarema.checkers.field.desk.CellState;
import com.sergeiyarema.checkers.field.desk.Desk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FieldController implements Drawable {
    private static final CheckerColor playerSide = CheckerColor.BLACK;
    private View caller;
    private int fieldSize;
    private Desk desk;
    private Checkers checkers;

    private Checker selected;
    private volatile CheckerColor gameState = CheckerColor.BLACK;
    private volatile boolean playerBeatRow = false;
    private List<Coords> availableCoords = new ArrayList<>();
    private Coords lastPosition;

    public FieldController(int fieldSize, View caller) {
        this.fieldSize = fieldSize;
        this.caller = caller;
        desk = new Desk(fieldSize);
        checkers = new Checkers(fieldSize);
    }

    @Override
    public void draw(Canvas canvas) {
        desk.draw(canvas);
        checkers.draw(canvas);
    }

    public void startBotCycle() {
        while (true) {
            if (gameState == other(playerSide)) {
                if (!playerBeatRow) {
                    startBotTurn();
                    reverseState();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void activatePlayer(float x, float y) {
        if (gameState == playerSide) {
            Coords tapCoords = Coords.toCoords(x, y, fieldSize, desk.getCellSize());

            if (selected == null) {
                trySelect(tapCoords);
                // update view
                caller.invalidate();
            } else {

                if (coordsInAvailableCells(tapCoords)) {
                    playerBeatRow = doPlayerStep(tapCoords);
                    updateQueens();
                    lastPosition = checkers.find(selected);

                    if (playerBeatRow && canBeatMore(lastPosition)) {
                        playerBeatRow = true;
                        trySelectBeatable(lastPosition);
                    } else {
                        playerBeatRow = false;
                        unselectAll();
                    }
                } else {
                    unselectAll();
                    caller.invalidate();
                    return;
                }
                // update view
                caller.invalidate();

                if (!playerBeatRow) {
                    unselectAll();
                    reverseState();
                }
            }


        }
    }

    private void startBotTurn() {
        boolean botBeatRow = false;
        Coords checkerCoords = BotLogic.chooseChecker(checkers, other(playerSide), 300);
        if (checkerCoords == null)
            return;
        Checker checkerBot = checkers.getChecker(checkerCoords);
        trySelect(checkerCoords);
        // update view
        caller.invalidate();
        Coords moveCoords = BotLogic.chooseMove(checkers, checkerBot, 300);
        botBeatRow = checkers.move(checkerBot, moveCoords.x, moveCoords.y);
        unselectAll();
        // update view
        caller.invalidate();

        while (botBeatRow && canBeatMore(moveCoords)) {
            unselectAll();
            trySelectBeatable(moveCoords);
            // update view
            caller.invalidate();
            moveCoords = BotLogic.chooseMove(checkers, checkerBot, 300);
            if (moveCoords != null)
                botBeatRow = checkers.move(checkerBot, moveCoords.x, moveCoords.y);
            else botBeatRow = false;
            // update view
            caller.invalidate();
        }
    }

    private boolean doPlayerStep(Coords tapCoords) {
        boolean hasBeaten = checkers.move(selected, tapCoords.x, tapCoords.y);
        return hasBeaten;
    }

    private boolean canBeatMore(Coords lastPosition) {
        if (lastPosition == null)
            return false;
        Checker checker = checkers.getChecker(lastPosition);
        List<Coords> beatable = checkers.canBeat(checker);
        return !beatable.isEmpty();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean coordsInAvailableCells(Coords tapCoords) {
        if (tapCoords == null)
            return false;
        for (Coords coords : availableCoords) {
            if (tapCoords.equals(coords))
                return true;
        }
        return false;
    }

    private void trySelect(Coords coords) {
        if (coords == null)
            return;
        unselectAll();
        Cell cell = desk.getCell(coords);
        Checker checker = checkers.getChecker(coords);

        if (cell != null && checker != null &&
                cell.getCellColor() == CellColor.BLACK && checker.getColor().equals(gameState)) {
            cell.setCurrentState(CellState.ACTIVE);
            selected = checker;

            activateAvailable(checker);
        }
    }


    private void trySelectBeatable(Coords coords) {
        if (coords == null)
            return;
        Cell cell = desk.getCell(coords);
        Checker checker = checkers.getChecker(coords);

        if (cell != null && checker != null &&
                cell.getCellColor() == CellColor.BLACK && checker.getColor().equals(gameState)) {
            cell.setCurrentState(CellState.ACTIVE);
            selected = checker;

            activateAvailableBeatable(checker);
        }
    }


    public void reverseState() {
        gameState = other(gameState);
    }

    public static CheckerColor other(CheckerColor color) {
        if (color == CheckerColor.BLACK)
            return CheckerColor.WHITE;
        else
            return CheckerColor.BLACK;
    }

    public void unselectAll() {
        selected = null;
        availableCoords.clear();
        desk.unselect();
    }

    public void activateAvailable(Checker checker) {
        if (checker == null)
            return;
        availableCoords.clear();
        desk.unselect();
        availableCoords = checkers.buildAvailable(checker);

        setActiveToAvailable();
    }

    private void activateAvailableBeatable(Checker checker) {
        if (checker == null)
            return;
        availableCoords.clear();
        desk.unselect();
        availableCoords = checkers.canBeat(checker);

        setActiveToAvailable();
    }

    private void setActiveToAvailable() {
        for (Coords coords : availableCoords) {
            desk.getCell(coords).setCurrentState(CellState.ACTIVE);
        }
    }

    private void updateQueens() {
        checkers.updateQueens();
    }
}
