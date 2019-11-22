package com.sergeiyarema.checkers.field.desk;

import android.graphics.Canvas;
import android.graphics.Color;
import com.sergeiyarema.checkers.field.Drawable;

public class Desk implements Drawable {
    private int fieldSize;
    private int cellSize;
    private Cell[][] cells;

    private int black = Color.rgb(50, 50, 50);
    private int white = Color.WHITE;
    private int activeColor = Color.rgb(10, 250, 20);

    public Desk(int fieldSize) {
        this.fieldSize = fieldSize;
        initCells(fieldSize, fieldSize);
    }

    private void initCells(int columns, int rows) {
        cells = new Cell[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i + j) % 2 == 0) {
                    cells[j][i] = new Cell(white, activeColor);
                } else
                    cells[j][i] = new Cell(black, activeColor);
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        cellSize = canvas.getWidth() / fieldSize;
        drawGrid(fieldSize, fieldSize, canvas);
    }

    private void drawGrid(int columns, int rows, Canvas canvas) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                drawCell(cells[j][i], j, i, canvas);
            }
        }
    }

    private void drawCell(Cell cell, int x, int y, Canvas canvas) {

        canvas.drawRect(x * cellSize, y * cellSize,
                x * cellSize + cellSize, y * cellSize + cellSize,
                cell.getPaint());
    }
}
