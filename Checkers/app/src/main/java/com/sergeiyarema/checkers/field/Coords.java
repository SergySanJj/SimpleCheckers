package com.sergeiyarema.checkers.field;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.util.Objects;

public class Coords {
    public int x;
    public int y;

    public Coords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Coords toCoords(float x, float y, int fieldSize, int cellSize) {
        Coords res = new Coords((int) (x / cellSize), (int) (y / cellSize));
        if (res.x >= fieldSize || res.y >= fieldSize)
            return null;
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Coords))
            return false;

        Coords coords = (Coords) (o);
        return coords.x == x && coords.y == y;
    }


    @Override
    public int hashCode() {
        return x + y * 1000;
    }
}
