package com.sergeiyarema.checkers.field.checker;

import android.widget.LinearLayout;
import com.sergeiyarema.checkers.field.Coords;
import com.sergeiyarema.checkers.gamelogic.BotLogic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CheckersTest {
    private int[][] clearTable = new int[][]{
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0}};

    private int[][] defaultTable = new int[][]{
            {0, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0},
            {0, 1, 0, 1, 0, 1, 0, 1},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {2, 0, 2, 0, 2, 0, 2, 0},
            {0, 2, 0, 2, 0, 2, 0, 2},
            {2, 0, 2, 0, 2, 0, 2, 0}};

    private int[][] resverseDefaultTable = new int[][]{
            {0, 2, 0, 2, 0, 2, 0, 2},
            {2, 0, 2, 0, 2, 0, 2, 0},
            {0, 2, 0, 2, 0, 2, 0, 2},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 1, 0, 1, 0, 1, 0},
            {0, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0}};

    private int[][] whiteBeatBlackTable = new int[][]{
            {0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 2, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0}};

    private int[][] queenFigtsBlack = new int[][]{
            {0, 3, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 2, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0}};

    private Checkers checkersDefault;
    private Checkers checkersEmpty;
    private Checkers checkersReverseDefault;
    private Checkers checkersQueenFigtsBlack;
    private Checkers checkersWhiteBeatBlack;

    private Checkers inited;

    @Before
    public void initCheckers() {
        int checkerSize = 10;
        int fieldSize = 8;
        checkersDefault = Checkers.generateFromTable(defaultTable);
        checkersDefault.setCheckerSize(checkerSize);

        checkersEmpty = Checkers.generateFromTable(clearTable);
        checkersEmpty.setCheckerSize(checkerSize);

        checkersReverseDefault = Checkers.generateFromTable(resverseDefaultTable);
        checkersReverseDefault.setCheckerSize(checkerSize);

        checkersWhiteBeatBlack = Checkers.generateFromTable(whiteBeatBlackTable);
        checkersWhiteBeatBlack.setCheckerSize(checkerSize);

        checkersQueenFigtsBlack = Checkers.generateFromTable(queenFigtsBlack);
        checkersQueenFigtsBlack.setCheckerSize(checkerSize);

        inited = new Checkers(8);
    }

    @Test
    public void getChecker() {
        Assert.assertNull(checkersEmpty.getChecker(0, 0));
        Assert.assertNull(checkersEmpty.getChecker(1, 0));
        Assert.assertNull(checkersEmpty.getChecker(1000, 1000));

        Assert.assertNull(checkersDefault.getChecker(0, 0));
        Assert.assertNotNull(checkersDefault.getChecker(1, 0));
        Assert.assertNull(checkersDefault.getChecker(1000, 1000));
    }

    @Test
    public void checkerMoveBeats() {
        Checker checkerToMove = checkersWhiteBeatBlack.getChecker(1, 0);
        Assert.assertTrue(checkersWhiteBeatBlack.move(checkerToMove, 2, 1));
    }

    @Test
    public void checkerMoveDontBeat() {
        Checker checkerToMove = checkersWhiteBeatBlack.getChecker(1, 0);
        Assert.assertFalse(checkersWhiteBeatBlack.move(checkerToMove, 0, 1));
    }

    @Test
    public void queenMoveBeats() {
        Checker checkerToMove = checkersQueenFigtsBlack.getChecker(1, 0);
        Assert.assertFalse(checkersQueenFigtsBlack.move(checkerToMove, 2, 1));
        Assert.assertTrue(checkersQueenFigtsBlack.move(checkerToMove, 6, 5));
    }

    @Test
    public void find() {
        Checker checker = checkersDefault.getChecker(1, 0);
        Coords coords = checkersDefault.find(checker);
        Assert.assertEquals(new Coords(1, 0), coords);
        Assert.assertNotEquals(new Coords(2, 0), coords);
    }

    @Test
    public void remove() {
        Assert.assertNotNull(checkersDefault.getChecker(1, 0));
        checkersDefault.remove(1, 0);
        Assert.assertNull(checkersDefault.getChecker(1, 0));

        Assert.assertNull(checkersEmpty.getChecker(1, 0));
        checkersDefault.remove(1, 0);
        Assert.assertNull(checkersEmpty.getChecker(1, 0));
    }

    @Test
    public void count() {
        Assert.assertEquals(12, checkersDefault.count(CheckerColor.BLACK));
        Assert.assertEquals(12, checkersDefault.count(CheckerColor.WHITE));

        Assert.assertEquals(0, checkersEmpty.count(CheckerColor.BLACK));
        Assert.assertEquals(0, checkersEmpty.count(CheckerColor.WHITE));

        Assert.assertEquals(1, checkersWhiteBeatBlack.count(CheckerColor.BLACK));
        Assert.assertEquals(1, checkersWhiteBeatBlack.count(CheckerColor.WHITE));
    }

    @Test
    public void isDraw() {
        Assert.assertTrue(checkersReverseDefault.isDraw(CheckerColor.BLACK));
        Assert.assertTrue(checkersReverseDefault.isDraw(CheckerColor.WHITE));

        Assert.assertTrue(checkersEmpty.isDraw(CheckerColor.BLACK));
        Assert.assertTrue(checkersEmpty.isDraw(CheckerColor.WHITE));

        Assert.assertFalse(checkersDefault.isDraw(CheckerColor.BLACK));
        Assert.assertFalse(checkersDefault.isDraw(CheckerColor.WHITE));
    }

    @Test
    public void initTest() {
        Assert.assertTrue(inited.sameAs(checkersDefault));
        Assert.assertFalse(inited.sameAs(checkersEmpty));
        Assert.assertFalse(inited.sameAs(checkersReverseDefault));
    }

    @Test
    public void canBeat() {
        Checker white = checkersWhiteBeatBlack.getChecker(1, 0);
        Checker black = checkersWhiteBeatBlack.getChecker(2, 1);

        List<Coords> whiteBeatable = checkersWhiteBeatBlack.canBeat(white);
        Assert.assertEquals(1, whiteBeatable.size());

        List<Coords> blackBeatable = checkersWhiteBeatBlack.canBeat(black);
        Assert.assertEquals(0, blackBeatable.size());
    }

    @Test
    public void canBeatQueenCase() {
        Checker whiteQueen = checkersQueenFigtsBlack.getChecker(1, 0);
        Checker black = checkersQueenFigtsBlack.getChecker(4, 3);

        List<Coords> whiteBeatable = checkersQueenFigtsBlack.canBeat(whiteQueen);
        Assert.assertEquals(1, whiteBeatable.size());

        List<Coords> blackBeatable = checkersQueenFigtsBlack.canBeat(black);
        Assert.assertEquals(0, blackBeatable.size());
    }

    @Test
    public void buildAvailableTest() {
        Checker white = checkersWhiteBeatBlack.getChecker(1, 0);
        Checker black = checkersWhiteBeatBlack.getChecker(2, 1);

        List<Coords> whiteAvailable = checkersWhiteBeatBlack.buildAvailable(white);
        List<Coords> blackAvailable = checkersWhiteBeatBlack.buildAvailable(black);

        Assert.assertEquals(2, whiteAvailable.size());
        Assert.assertEquals(1, blackAvailable.size());
    }

    @Test
    public void buildAvailableTestQueenCase() {
        Checker whiteQueen = checkersQueenFigtsBlack.getChecker(1, 0);
        Checker black = checkersQueenFigtsBlack.getChecker(4, 3);

        List<Coords> whiteAvailable = checkersQueenFigtsBlack.buildAvailable(whiteQueen);
        List<Coords> blackAvailable = checkersQueenFigtsBlack.buildAvailable(black);

        Assert.assertEquals(4, whiteAvailable.size());
        Assert.assertEquals(2, blackAvailable.size());
    }


    @Test
    public void botSelectionDefault() {
        Coords coordsSelectDefaultBLACK = BotLogic.chooseChecker(checkersDefault, CheckerColor.BLACK, 0);
        Coords coordsSelectDefaultWHITE = BotLogic.chooseChecker(checkersDefault, CheckerColor.WHITE, 0);
        Assert.assertNotNull(coordsSelectDefaultBLACK);
        Assert.assertNotNull(coordsSelectDefaultWHITE);
    }

    @Test
    public void botSelectionReverse() {
        Coords coordsSelectReverseBLACK = BotLogic.chooseChecker(checkersReverseDefault, CheckerColor.BLACK, 0);
        Coords coordsSelectReverseWHITE = BotLogic.chooseChecker(checkersReverseDefault, CheckerColor.WHITE, 0);
        Assert.assertNull(coordsSelectReverseBLACK);
        Assert.assertNull(coordsSelectReverseWHITE);
    }

    @Test
    public void botMoveChose() {
        Coords coordsSelectDefaultBLACK = BotLogic.chooseChecker(checkersDefault, CheckerColor.BLACK, 0);
        Coords coordsSelectDefaultWHITE = BotLogic.chooseChecker(checkersDefault, CheckerColor.WHITE, 0);

        Checker checkerBlack = checkersDefault.getChecker(coordsSelectDefaultBLACK);
        Checker checkerWhite = checkersDefault.getChecker(coordsSelectDefaultWHITE);

        Coords newBlack = BotLogic.chooseMove(checkersDefault, checkerBlack, 0);
        Coords newWhite = BotLogic.chooseMove(checkersDefault, checkerWhite, 0);

        Assert.assertNotNull(newBlack);
        Assert.assertNotNull(newWhite);
    }

    @Test
    public void botMoveChoseQueenCase() {
        Coords coordsSelectDefaultBLACK = BotLogic.chooseChecker(checkersQueenFigtsBlack, CheckerColor.BLACK, 0);
        Coords coordsSelectDefaultWHITE = BotLogic.chooseChecker(checkersQueenFigtsBlack, CheckerColor.WHITE, 0);

        Checker checkerBlack = checkersQueenFigtsBlack.getChecker(coordsSelectDefaultBLACK);
        Checker checkerWhite = checkersQueenFigtsBlack.getChecker(coordsSelectDefaultWHITE);

        Coords newBlack = BotLogic.chooseMove(checkersQueenFigtsBlack, checkerBlack, 0);
        Coords newWhite = BotLogic.chooseMove(checkersQueenFigtsBlack, checkerWhite, 0);

        Assert.assertNotNull(newBlack);
        Assert.assertNotNull(newWhite);
    }
}