package com.sergeiyarema.checkers;

import android.app.Activity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;



public class GameActivity extends Activity {
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        LinearLayout windowLayout = findViewById(R.id.window_layout);

        LinearLayout gameLayout = findViewById(R.id.game_layout);

        gameView = new GameView(this);
        gameLayout.addView(gameView);

    }
}
