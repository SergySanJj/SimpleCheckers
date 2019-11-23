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

public class Field implements Drawable {
    private View caller;
    private int fieldSize;
    private Desk desk;
    private Checkers checkers;

    private Checker selected;
    private CheckerColor gameState = CheckerColor.BLACK;
    private List<Coords> availableCoords = new ArrayList<>();
    private static final CheckerColor playerSide = CheckerColor.BLACK;
    private boolean playerBeatRow = false;
    private Coords lastPosition;

    public Field(int fieldSize, View caller) {
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void activate(float x, float y) {
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
                }else {
                    unselectAll();
                    caller.invalidate();
                    return;
                }

                // update view
                caller.invalidate();

                if (!playerBeatRow) {
                    unselectAll();
                    reverseState();
                    startBotTurn();
                    reverseState();
                }
            }
        }
    }

    private void startBotTurn() {
        boolean botBeatRow = false;
        Coords checkerCoords = BotLogic.chooseChecker(checkers, other(playerSide), 1000);
        Checker checkerBot = checkers.getChecker(checkerCoords);
        trySelect(checkerCoords);
        // update view
        caller.invalidate();
        Coords moveCoords = BotLogic.chooseMove(checkers, checkerBot, 1000);
        // todo check null on no checkers steps
        botBeatRow = checkers.move(checkerBot, moveCoords.x, moveCoords.y);
        unselectAll();
        // update view
        caller.invalidate();

        while (botBeatRow) {
            unselectAll();
            trySelectBeatable(moveCoords);
            // update view
            caller.invalidate();
            moveCoords = BotLogic.chooseMove(checkers, checkerBot, 1000);
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
        Checker checker = checkers.getChecker(lastPosition);
        List<Coords> beatable = checkers.canBeat(checker);
        return !beatable.isEmpty();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean coordsInAvailableCells(Coords tapCoords) {
        for (Coords coords : availableCoords) {
            if (tapCoords.equals(coords))
                return true;
        }
        return false;
    }

    private void trySelect(Coords coords) {
        unselectAll();
        Cell cell = desk.getCell(coords);
        Checker checker = checkers.getChecker(coords);

        if (cell != null && checker != null) {
            if (cell.getCellColor() == CellColor.BLACK && checker.getColor().equals(gameState)) {
                cell.setCurrentState(CellState.ACTIVE);
                activateAvailable(checker);
                selected = checker;
            }
        }
    }

    private void trySelectBeatable(Coords coords) {
        Cell cell = desk.getCell(coords);
        Checker checker = checkers.getChecker(coords);

        if (cell != null && checker != null) {
            if (cell.getCellColor() == CellColor.BLACK && checker.getColor().equals(gameState)) {
                cell.setCurrentState(CellState.ACTIVE);
                activateAvailableBeatable(checker);
                selected = checker;
            }
        }
    }

    public void reverseState() {
        gameState = other(gameState);
    }

    public CheckerColor other(CheckerColor color) {
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
        availableCoords.clear();
        desk.unselect();
        availableCoords = checkers.buildAvailable(checker);

        for (Coords coords : availableCoords) {
            desk.getCell(coords).setCurrentState(CellState.ACTIVE);
        }
    }

    private void activateAvailableBeatable(Checker checker) {
        availableCoords.clear();
        desk.unselect();
        availableCoords = checkers.canBeat(checker);

        for (Coords coords : availableCoords) {
            desk.getCell(coords).setCurrentState(CellState.ACTIVE);
        }
    }


    private void updateQueens() {
        checkers.updateQueens();
    }


}
