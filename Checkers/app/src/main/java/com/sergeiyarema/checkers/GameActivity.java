package com.sergeiyarema.checkers;

import android.app.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameActivity extends Activity {
    private GameView gameView;
    private LinearLayout gameLayout;
    private Button newGameButton;
    private TextView statusView;
    private Handler updateHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        gameLayout = findViewById(R.id.game_layout);

        gameView = new GameView(this);
        gameLayout.addView(gameView);

        startNewGame();
        newGameButton = findViewById(R.id.new_game_button);
        newGameButton.setOnClickListener(newGameListener);

        statusView = findViewById(R.id.game_status);

        updateHandler = new Handler();

        updateHandler.post(periodicalStatusUpdate);
    }

    private void startNewGame() {
        gameView = new GameView(this);
        if (gameLayout != null)
            gameLayout.removeAllViews();
        gameLayout.addView(gameView);
        gameView.updateView();
    }

    private View.OnClickListener newGameListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.new_game_button:
                    startNewGame();
                    break;
            }
        }
    };

    private Runnable periodicalStatusUpdate = new Runnable() {
        @Override
        public void run() {
            if (gameView != null) {
                String status = gameView.getStatus();
                statusView.setText(status);
            }
            updateHandler.postDelayed(this, 200);
        }
    };
}
