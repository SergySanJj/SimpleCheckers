package com.sergeiyarema.checkers;


import android.content.Context;
import android.graphics.*;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.RequiresApi;
import com.sergeiyarema.checkers.field.FieldController;

class OnTouchTask implements Runnable {
    MotionEvent event;
    FieldController fieldController;

    OnTouchTask(MotionEvent event, FieldController fieldController) {
        this.event = event;
        this.fieldController = fieldController;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            fieldController.activatePlayer(event.getX(), event.getY());
        }
    }
}

public class GameView extends View {
    private int fieldSize = 8;
    private FieldController fieldController;
    private Context context;


    public GameView(final Context context) {
        super(context);
        this.context = context;
        fieldController = new FieldController(fieldSize, this);

        setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public boolean onTouch(View v, MotionEvent event) {
                Thread th = new Thread(new OnTouchTask(event, fieldController));
                th.start();
                try {
                    th.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        Thread botThread = new Thread(new Runnable() {
            @Override
            public void run() {
                fieldController.startBotCycle();
            }
        });
        botThread.start();
    }

    public void updateView() {
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        fieldController.draw(canvas);
    }
}