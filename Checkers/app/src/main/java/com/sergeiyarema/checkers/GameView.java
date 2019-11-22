package com.sergeiyarema.checkers;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.View;
import com.sergeiyarema.checkers.field.Field;

public class GameView extends View {
    private int fieldSize = 8;
    private Field field;

    private Context context;


    public GameView(final Context context) {
        super(context);
        this.context = context;
        field = new Field(fieldSize);

        setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_UP){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Важное сообщение!")
                            .setMessage("Покормите кота!")
                            .setCancelable(false)
                            .setNegativeButton("ОК, иду на кухню",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                return true;
            }
        });
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        field.draw(canvas);
    }


}