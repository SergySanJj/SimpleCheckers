package field;

import com.sergeiyarema.checkers.field.Coords;
import org.junit.Assert;
import org.junit.Test;

public class CoordsTest {

    @Test
    public void toCoordsZero() {
        float x = 0.f;
        float y = 0.f;
        int fieldSize = 8;
        int cellSize = 10;

        Coords coords = Coords.toCoords(x, y, fieldSize, cellSize);
        Assert.assertEquals(new Coords(0,0),coords);
    }

    @Test
    public void toCoords() {
        float x = 25.f;
        float y = 0.f;
        int fieldSize = 8;
        int cellSize = 10;

        Coords coords = Coords.toCoords(x, y, fieldSize, cellSize);
        Assert.assertEquals(new Coords(2,0),coords);
    }

    @Test
    public void toCoordsOverBorder() {
        float x = 999999.f;
        float y = 999999.f;
        int fieldSize = 8;
        int cellSize = 10;

        Coords coords = Coords.toCoords(x, y, fieldSize, cellSize);
        Assert.assertNull(coords);
    }
}