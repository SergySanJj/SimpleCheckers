package com.sergeiyarema.checkers;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import com.sergeiyarema.checkers.field.FieldController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static android.view.MotionEvent.ACTION_UP;
import static org.junit.Assert.*;

public class GameViewTest {
    private GameView gameView;
    private FieldController fieldController;

    @Before
    public void init() {
        gameView = new GameView(null);
        fieldController = gameView.getFieldController();
        fieldController.getCheckers().setCheckerSize(10);
        fieldController.getDesk().setCellSize(10);
    }

    @Test
    public void inited() {
        Assert.assertEquals("Black turn", gameView.getStatus());

    }

    @Test
    public void touch() {
        MotionEvent event =
                MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                        ACTION_UP, 10, 10, 0);
        gameView.dispatchTouchEvent(event);
        Assert.assertNull(fieldController.getSelected());
    }

}