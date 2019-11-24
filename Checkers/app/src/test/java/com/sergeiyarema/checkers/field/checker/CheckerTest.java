package com.sergeiyarema.checkers.field.checker;

import org.junit.Assert;
import org.junit.Test;

public class CheckerTest {

    @Test
    public void updatePaint() {
        Checker checkerBlack = new Checker(CheckerColor.BLACK);
        Checker checkerWhite = new Checker(CheckerColor.WHITE);

        Assert.assertEquals(Checker.blackNormal, checkerBlack.updatePaint().getColor());
        Assert.assertEquals(Checker.whiteNormal, checkerWhite.updatePaint().getColor());

        checkerBlack.setState(CheckerState.QUEEN);
        checkerWhite.setState(CheckerState.QUEEN);

        Assert.assertEquals(Checker.blackNormal, checkerBlack.updatePaint().getColor());
        Assert.assertEquals(Checker.whiteNormal, checkerWhite.updatePaint().getColor());
    }
}