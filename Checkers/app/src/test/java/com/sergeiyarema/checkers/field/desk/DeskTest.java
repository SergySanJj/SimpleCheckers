package com.sergeiyarema.checkers.field.desk;

import com.sergeiyarema.checkers.field.Coords;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeskTest {
    private Desk desk;

    @Before
    public void prepareDesk() {
        desk = new Desk(8);
    }

    @Test
    public void getCell() {
        Cell white = desk.getCell(new Coords(0, 0));
        Cell black = desk.getCell(new Coords(1, 0));
        Assert.assertNotNull(white);
        Assert.assertNotNull(black);
        Assert.assertEquals(CellColor.WHITE, white.getCellColor());
        Assert.assertEquals(CellColor.BLACK, black.getCellColor());

        Cell outer = desk.getCell(new Coords(10000, 10000));
        Assert.assertNull(outer);
    }

    @Test
    public void unselectAll() {
        Cell white = desk.getCell(new Coords(0, 0));
        Cell black = desk.getCell(new Coords(1, 0));
        Assert.assertEquals(CellState.IDLE, white.getCurrentState());
        Assert.assertEquals(CellState.IDLE, black.getCurrentState());

        white.setCurrentState(CellState.ACTIVE);
        black.setCurrentState(CellState.ACTIVE);

        Assert.assertEquals(CellState.ACTIVE, white.getCurrentState());
        Assert.assertEquals(CellState.ACTIVE, black.getCurrentState());

        desk.unselectAll();
        Assert.assertEquals(CellState.IDLE, white.getCurrentState());
        Assert.assertEquals(CellState.IDLE, black.getCurrentState());
    }
}