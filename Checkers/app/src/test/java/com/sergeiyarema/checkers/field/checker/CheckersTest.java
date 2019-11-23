package com.sergeiyarema.checkers.field.checker;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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

    private Checkers checkersDefault;
    private Checkers checkersEmpty;
    private Checkers checkersReverseDefault;
    private Checkers checkersWhiteBeatBlack;

    @Before
    public void initCheckers() {
        checkersDefault = Checkers.generateFromTable(defaultTable);
        checkersEmpty = Checkers.generateFromTable(clearTable);
        checkersReverseDefault = Checkers.generateFromTable(resverseDefaultTable);
        checkersWhiteBeatBlack = Checkers.generateFromTable(whiteBeatBlackTable);
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
    public void testGetChecker() {

    }

    @Test
    public void find() {
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
}