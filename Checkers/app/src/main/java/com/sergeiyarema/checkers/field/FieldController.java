package com.sergeiyarema.checkers.field;

import android.graphics.Canvas;

import com.sergeiyarema.checkers.gamelogic.BotLogic;
import com.sergeiyarema.checkers.GameView;
import com.sergeiyarema.checkers.field.checker.Checker;
import com.sergeiyarema.checkers.field.checker.CheckerColor;
import com.sergeiyarema.checkers.field.checker.Checkers;
import com.sergeiyarema.checkers.field.desk.Cell;
import com.sergeiyarema.checkers.field.desk.CellColor;
import com.sergeiyarema.checkers.field.desk.CellState;
import com.sergeiyarema.checkers.field.desk.Desk;

import java.util.ArrayList;
import java.util.List;

public class FieldController implements Drawable {
    public static final CheckerColor playerSide = CheckerColor.BLACK;
    private GameView caller;
    private int fieldSize;

    private Desk desk;
    private Checkers checkers;

    private Checker selected;
    private CheckerColor gameState = CheckerColor.BLACK;
    private volatile boolean playerBeatRow = false;
    private List<Coords> availableCoords = new ArrayList<>();
    private Coords lastPosition;

    private int botDelay = 300;

    FieldController(int fieldSize) {
        this.fieldSize = fieldSize;
        caller = null;
        desk = new Desk(fieldSize);
        checkers = new Checkers(fieldSize);
    }

    public FieldController(int fieldSize, GameView caller) {
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
        while (!Thread.currentThread().isInterrupted()) {
            if (gameState == other(playerSide)) {
                if (!playerBeatRow) {
                    startBotTurn();
                    reverseState();
                    updateQueens();
                }
            }
        }
    }

    public void activatePlayer(float x, float y) {
        if (gameState == playerSide) {
            Coords tapCoords = Coords.toCoords(x, y, fieldSize, desk.getCellSize());

            if (selected == null) {
                trySelect(tapCoords);
                // update view
                callUpdate();
            } else {

                if (coordsInAvailableCells(tapCoords)) {
                    playerBeatRow = doPlayerStep(tapCoords);
                    updateQueens();
                    lastPosition = checkers.find(selected);

                    if (playerBeatRow && canBeatMore(tapCoords)) {
                        playerBeatRow = true;
                        trySelectBeatable(lastPosition);
                    } else {
                        playerBeatRow = false;
                        unselectAll();
                    }
                } else {
                    unselectAll();
                    callUpdate();
                    return;
                }
                // update view
                callUpdate();

                if (!playerBeatRow) {
                    unselectAll();
                    reverseState();
                }
            }


        }
    }

    public void startBotTurn() {
        boolean botBeatRow = false;
        Coords checkerCoords = BotLogic.chooseChecker(checkers, other(playerSide), botDelay);
        if (checkerCoords == null)
            return;
        Checker checkerBot = checkers.getChecker(checkerCoords);
        trySelect(checkerCoords);
        // update view
        callUpdate();
        Coords moveCoords = BotLogic.chooseMove(checkers, checkerBot, botDelay);
        if (moveCoords == null)
            return;
        botBeatRow = checkers.move(checkerBot, moveCoords.x, moveCoords.y);
        unselectAll();
        // update view
        callUpdate();

        while (botBeatRow && canBeatMore(moveCoords)) {
            unselectAll();
            trySelectBeatable(moveCoords);
            // update view
            callUpdate();
            moveCoords = BotLogic.chooseMove(checkers, checkerBot, botDelay);
            if (moveCoords != null)
                botBeatRow = checkers.move(checkerBot, moveCoords.x, moveCoords.y);
            else botBeatRow = false;
            // update view
            callUpdate();
        }
        unselectAll();
    }

    public void callUpdate() {
        if (caller != null)
            caller.invalidate();
    }

    private boolean doPlayerStep(Coords tapCoords) {
        Coords oldCoords = checkers.find(selected);
        if (oldCoords.equals(tapCoords))
            return false;
        boolean hasBeaten = checkers.move(selected, tapCoords.x, tapCoords.y);
        return hasBeaten;
    }

    public boolean canBeatMore(Coords lastPosition) {
        if (lastPosition == null)
            return false;
        Checker checker = checkers.getChecker(lastPosition);
        List<Coords> beatable = checkers.canBeat(checker);
        return !beatable.isEmpty();
    }

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

    private void unselectAll() {
        selected = null;
        availableCoords.clear();
        desk.unselectAll();
    }

    private void activateAvailable(Checker checker) {
        if (activationPreparation(checker)) return;
        availableCoords = checkers.buildAvailable(checker);
        setActiveToAvailable();
    }

    private void activateAvailableBeatable(Checker checker) {
        if (activationPreparation(checker)) return;
        availableCoords = checkers.canBeat(checker);
        setActiveToAvailable();
    }

    private boolean activationPreparation(Checker checker) {
        if (checker == null)
            return true;
        availableCoords.clear();
        desk.unselectAll();
        return false;
    }

    private void setActiveToAvailable() {
        for (Coords coords : availableCoords) {
            desk.getCell(coords).setCurrentState(CellState.ACTIVE);
        }
    }

    private void updateQueens() {
        checkers.updateQueens();
    }

    public String getStatus() {
        if (checkers.count(CheckerColor.BLACK) == 0)
            return "!!!White WON!!!";
        if (checkers.count(CheckerColor.WHITE) == 0)
            return "!!!Black WON!!!";
        if (checkers.isDraw(gameState))
            return "!!!DRAW!!!";
        if (gameState == CheckerColor.WHITE) {
            return "White turn";
        } else return "Black turn";
    }

    public int getFieldSize() {
        return fieldSize;
    }

    public void setFieldSize(int fieldSize) {
        this.fieldSize = fieldSize;
    }

    public Desk getDesk() {
        return desk;
    }

    public void setDesk(Desk desk) {
        this.desk = desk;
    }

    public Checkers getCheckers() {
        return checkers;
    }

    public void setCheckers(Checkers checkers) {
        this.checkers = checkers;
    }

    public Checker getSelected() {
        return selected;
    }

    public void setSelected(Checker selected) {
        this.selected = selected;
    }

    public CheckerColor getGameState() {
        return gameState;
    }

    public void setGameState(CheckerColor gameState) {
        this.gameState = gameState;
    }

    public void setBotDelay(int botDelay) {
        this.botDelay = botDelay;
    }

    public static CheckerColor getPlayerSide() {
        return playerSide;
    }

    public boolean isPlayerBeatRow() {
        return playerBeatRow;
    }

    public List<Coords> getAvailableCoords() {
        return availableCoords;
    }
}
