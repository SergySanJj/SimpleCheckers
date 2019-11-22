package com.sergeiyarema.checkers;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.*;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.RequiresApi;
import com.sergeiyarema.checkers.field.Field;
import com.sergeiyarema.checkers.field.checker.Checker;

public class GameView extends View {
    private int fieldSize = 8;
    private Field field;
    private Context context;


    public GameView(final Context context) {
        super(context);
        this.context = context;
        field = new Field(fieldSize);

        setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    field.activate(event.getX(), event.getY());

                    updateView();
                }
                return true;
            }
        });
    }

    public void updateView() {
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        field.draw(canvas);
    }
}