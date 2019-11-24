package com.sergeiyarema.checkers.gamelogic;

import com.sergeiyarema.checkers.field.Coords;
import com.sergeiyarema.checkers.field.checker.Checker;
import com.sergeiyarema.checkers.field.checker.CheckerColor;
import com.sergeiyarema.checkers.field.checker.Checkers;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class BotLogic {
    private static Random random = new Random();

    // Returns null if no available
    public static Coords chooseChecker(Checkers checkers, CheckerColor botColor, long delay) {
        waitFor(delay);
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
        waitFor(delay);
        List<Coords> available = checkers.buildAvailable(checker);
        if (available.isEmpty())
            return null;
        return available.get(random.nextInt(available.size()));
    }

    private static void waitFor(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


}
