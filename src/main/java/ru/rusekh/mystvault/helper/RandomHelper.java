package ru.rusekh.mystvault.helper;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomHelper
{
    private static final Random rand = new Random();

    public static int getRandInt(final int min, final int max) throws IllegalArgumentException {
        return rand.nextInt(max - min + 1) + min;
    }

    public static Double getRandDouble(final double min, final double max) throws IllegalArgumentException {
        return rand.nextDouble() * (max - min) + min;
    }

    public static Float getRandFloat(final float min, final float max) throws IllegalArgumentException {
        return rand.nextFloat() * (max - min) + min;
    }
    public static boolean getChance(double value) {
        return ThreadLocalRandom.current().nextDouble(100) < value;
    }
}
