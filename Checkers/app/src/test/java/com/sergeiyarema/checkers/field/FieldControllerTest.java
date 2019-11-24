package com.sergeiyarema.checkers.field;

import com.sergeiyarema.checkers.field.checker.Checker;
import com.sergeiyarema.checkers.field.checker.CheckerColor;
import com.sergeiyarema.checkers.field.checker.Checkers;
import com.sergeiyarema.checkers.field.desk.CellColor;
import com.sergeiyarema.checkers.field.desk.CellState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FieldControllerTest {
    private FieldController fieldController;
    private int[][] defaultTable = new int[][]{
            {0, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0},
            {0, 1, 0, 1, 0, 1, 0, 1},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {2, 0, 2, 0, 2, 0, 2, 0},
            {0, 2, 0, 2, 0, 2, 0, 2},
            {2, 0, 2, 0, 2, 0, 2, 0}};

    private int[][] movedTable = new int[][]{
            {0, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0},
            {0, 1, 0, 1, 0, 1, 0, 1},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 2, 0, 0, 0, 0, 0, 0},
            {0, 0, 2, 0, 2, 0, 2, 0},
            {0, 2, 0, 2, 0, 2, 0, 2},
            {2, 0, 2, 0, 2, 0, 2, 0}};

    private int[][] drawTable = new int[][]{
            {0, 2, 0, 2, 0, 2, 0, 2},
            {2, 0, 2, 0, 2, 0, 2, 0},
            {0, 2, 0, 2, 0, 2, 0, 2},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 1, 0, 1, 0, 1, 0},
            {0, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0}};

    private int[][] whiteWon = new int[][]{
            {0, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0},
            {0, 1, 0, 1, 0, 1, 0, 1},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0}};

    private int[][] blackWon = new int[][]{
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {2, 0, 2, 0, 2, 0, 2, 0},
            {0, 2, 0, 2, 0, 2, 0, 2},
            {2, 0, 2, 0, 2, 0, 2, 0}};

    private int[][] whiteBeatsInRow = new int[][]{
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 2, 0, 2, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 2, 0, 2, 0, 2, 0, 2},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 2, 0, 2, 0, 2, 0, 2},
            {0, 0, 0, 0, 0, 0, 0, 0}};


    private Checkers checkersDefault;
    private Checkers checkersMoved;
    private Checkers checkersDraw;
    private Checkers checkersWhiteWon;
    private Checkers checkersBlackWon;
    private Checkers whiteRowBeat;

    @Before
    public void initCheckers() {
        int checkerSize = 10;
        int fieldSize = 8;
        checkersDefault = Checkers.generateFromTable(defaultTable);
        checkersDefault.setCheckerSize(checkerSize);

        checkersMoved = Checkers.generateFromTable(movedTable);
        checkersMoved.setCheckerSize(checkerSize);

        checkersDraw = Checkers.generateFromTable(drawTable);
        checkersDraw.setCheckerSize(checkerSize);

        checkersWhiteWon = Checkers.generateFromTable(whiteWon);
        checkersWhiteWon.setCheckerSize(checkerSize);

        checkersBlackWon = Checkers.generateFromTable(blackWon);
        checkersBlackWon.setCheckerSize(checkerSize);

        whiteRowBeat = Checkers.generateFromTable(whiteBeatsInRow);
        whiteRowBeat.setCheckerSize(checkerSize);
    }

    @Before
    public void setUp() {
        fieldController = new FieldController(8);
        fieldController.getCheckers().setCheckerSize(10);
        fieldController.getDesk().setCellSize(10);
    }

    @Test
    public void initTest() {
        Assert.assertEquals(12, fieldController.getCheckers().count(CheckerColor.BLACK));
        Assert.assertEquals(12, fieldController.getCheckers().count(CheckerColor.WHITE));

        Assert.assertEquals(8, fieldController.getFieldSize());
        Assert.assertEquals(8, fieldController.getDesk().getFieldSize());

        Assert.assertEquals(CellColor.WHITE, fieldController.getDesk().getCell(new Coords(0, 0)).getCellColor());
        Assert.assertEquals(CellColor.BLACK, fieldController.getDesk().getCell(new Coords(1, 0)).getCellColor());

        Assert.assertTrue(checkersDefault.sameAs(fieldController.getCheckers()));
    }

    @Test
    public void botStep() {
        Assert.assertTrue(checkersDefault.sameAs(fieldController.getCheckers()));
        fieldController.setBotDelay(0);
        fieldController.startBotTurn();
        Assert.assertFalse(checkersDefault.sameAs(fieldController.getCheckers()));
    }

    @Test
    public void playerStep() {
        Assert.assertTrue(checkersDefault.sameAs(fieldController.getCheckers()));
        Assert.assertEquals(CheckerColor.BLACK, FieldController.playerSide);
        fieldController.activatePlayer(5, 55);
        Assert.assertTrue(checkersDefault.sameAs(fieldController.getCheckers()));
        Assert.assertEquals(CellState.IDLE, fieldController.getDesk().getCell(new Coords(0, 5)).getCurrentState());
        Assert.assertEquals(CellState.ACTIVE, fieldController.getDesk().getCell(new Coords(1, 4)).getCurrentState());
        Assert.assertEquals(CellState.IDLE, fieldController.getDesk().getCell(new Coords(2, 3)).getCurrentState());

        fieldController.activatePlayer(15, 45);
        Assert.assertTrue(checkersMoved.sameAs(fieldController.getCheckers()));
        Assert.assertFalse(fieldController.canBeatMore(new Coords(1, 4)));
    }

    @Test
    public void playerMove() {
        Assert.assertTrue(checkersDefault.sameAs(fieldController.getCheckers()));
        Assert.assertEquals(CheckerColor.BLACK, FieldController.playerSide);
        fieldController.activatePlayer(5, 55);
        boolean beated = fieldController.getCheckers().move(fieldController.getSelected(), 1, 4);
        Assert.assertFalse(beated);
        Assert.assertTrue(checkersMoved.sameAs(fieldController.getCheckers()));
        Assert.assertFalse(fieldController.canBeatMore(new Coords(1, 4)));
    }

    @Test
    public void beatingInARow() {
        fieldController.setCheckers(whiteRowBeat);
        fieldController.setGameState(CheckerColor.WHITE);
        fieldController.startBotTurn();
        int countBlacks = fieldController.getCheckers().count(CheckerColor.BLACK);
        Assert.assertTrue(10 > countBlacks);

    }

    @Test
    public void statusTurnsTest() {
        // Turns
        fieldController.setGameState(CheckerColor.BLACK);
        Assert.assertEquals("Black turn", fieldController.getStatus());
        fieldController.setGameState(CheckerColor.WHITE);
        Assert.assertEquals("White turn", fieldController.getStatus());
    }

    @Test
    public void drawState() {
        fieldController.setCheckers(checkersDraw);
        Assert.assertEquals("!!!DRAW!!!", fieldController.getStatus());
    }

    @Test
    public void whiteWon() {
        fieldController.setCheckers(checkersWhiteWon);
        Assert.assertEquals("!!!White WON!!!", fieldController.getStatus());
    }

    @Test
    public void blackWon() {
        fieldController.setCheckers(checkersBlackWon);
        Assert.assertEquals("!!!Black WON!!!", fieldController.getStatus());
    }
}