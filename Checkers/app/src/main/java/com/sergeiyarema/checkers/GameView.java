package com.sergeiyarema.checkers;


import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.sergeiyarema.checkers.field.FieldController;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static java.util.concurrent.Executors.newFixedThreadPool;

public class GameView extends View {
    private ExecutorService executor = newFixedThreadPool(5);

    private static final int FIELD_SIZE = 8;
    private FieldController fieldController;
    private Context context;
    private Future botFuture;

    public GameView(Context context) {
        super(context);
        this.context = context;
        fieldController = new FieldController(FIELD_SIZE, this);

        assignTouchListener();
        startBotThread();
    }

    private void startBotThread() {
        botFuture = executor.submit(new Runnable() {

            @Override
            public void run() {
                try {
                    fieldController.startBotCycle();
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    private void assignTouchListener() {
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                Future touchFuture = executor.submit(new OnTouchTask(event, fieldController));
                try {
                    touchFuture.get();
                } catch (Exception e) {
                    Log.e("Touch exception", Arrays.toString(e.getStackTrace()));
                    Thread.currentThread().interrupt();
                }
                return true;
            }
        });
    }

    public void updateView() {
        invalidate();
    }

    public String getStatus() {
        return fieldController.getStatus();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        fieldController.draw(canvas);
    }

    public Future getBotFuture() {
        return botFuture;
    }

    public FieldController getFieldController() {
        return fieldController;
    }
}

class OnTouchTask implements Runnable {
    private MotionEvent event;
    private FieldController fieldController;

    OnTouchTask(MotionEvent event, FieldController fieldController) {
        this.event = event;
        this.fieldController = fieldController;
    }

    @Override
    public void run() {
        if (event == null)
            return;
        if (event.getAction() == MotionEvent.ACTION_UP) {
            fieldController.activatePlayer(event.getX(), event.getY());
        }
    }
}
