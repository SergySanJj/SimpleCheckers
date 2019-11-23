package com.sergeiyarema.checkers.gamelogic;

import android.util.Log;
import com.sergeiyarema.checkers.field.Coords;
import com.sergeiyarema.checkers.field.checker.Checker;
import com.sergeiyarema.checkers.field.checker.CheckerColor;
import com.sergeiyarema.checkers.field.checker.Checkers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BotLogic {
    private static Random random = new Random();

    // Returns null if no available
    public static Coords chooseChecker(Checkers checkers, CheckerColor botColor, long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Map<Checker, List<Coords>> available = checkers.getAvailableListByColor(botColor);
        Coords res = null;
        for (Map.Entry<Checker, List<Coords>> entry : available.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                res = checkers.find(entry.getKey());
                break;
            }
        }
        return res;
    }

    public static Coords chooseMove(Checkers checkers, Checker checker, long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Coords> available = checkers.buildAvailable(checker);
        if (available.isEmpty())
            return null;
        return available.get(random.nextInt(available.size()));
    }


}
