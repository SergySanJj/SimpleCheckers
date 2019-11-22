package com.sergeiyarema.checkers;


import android.content.Context;
import android.graphics.*;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import androidx.annotation.RequiresApi;

public class GameView extends View {

    private long left = 10;
    private long top = 10;
    private int fieldSize = 10;
    private int cellSize;

    private Context context;
    private Cell blackCell;
    private Cell whiteCell;

    public GameView(Context context) {
        super(context);
        this.context = context;

        int black = Color.BLACK;
        int white = Color.WHITE;
        cellSize = getWidth();
        blackCell = new Cell(cellSize, black);
        whiteCell = new Cell(cellSize, white);
    }

    private void drawGrid(int columns, int rows, Canvas canvas) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i + j) % 2 == 0) {
                    drawCell(blackCell, j, i, canvas);
                } else
                    drawCell(whiteCell, j, i, canvas);
            }
        }
    }

    private void drawCell(Cell cell, int x, int y, Canvas canvas) {

        canvas.drawRect(x * cell.getSize(), y * cell.getSize(),
                x * cell.getSize() + cell.getSize(), y * cell.getSize() + cell.getSize(),
                cell.getPaint());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGrid(fieldSize, fieldSize, canvas);

    }
}