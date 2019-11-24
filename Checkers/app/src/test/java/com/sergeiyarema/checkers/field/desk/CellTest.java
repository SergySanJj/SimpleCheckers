package com.sergeiyarema.checkers.field.desk;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class CellTest {

    @Test
    public void setCurrentState() {
        Cell cellBlack = new Cell(CellColor.BLACK);
        Cell cellWhite = new Cell(CellColor.WHITE);

        Assert.assertEquals(Cell.black, cellBlack.getColorIdle());
        Assert.assertEquals(Cell.white, cellWhite.getColorIdle());

        cellBlack.setCurrentState(CellState.ACTIVE);
        cellWhite.setCurrentState(CellState.ACTIVE);

        Assert.assertEquals(Cell.black, cellBlack.getColorIdle());
        Assert.assertEquals(Cell.white, cellWhite.getColorIdle());

        Assert.assertEquals(Cell.colorActive, cellBlack.getPaint().getColor());
        Assert.assertEquals(Cell.colorActive, cellWhite.getPaint().getColor());
    }
}