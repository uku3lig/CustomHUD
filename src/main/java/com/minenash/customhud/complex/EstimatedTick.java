package com.minenash.customhud.complex;

import java.util.Arrays;

public class EstimatedTick {

    private static final float[] tickRates = new float[10];
    private static int nextIndex = 0;
    private static long lastTime = -1;

    public static void record() {
        long time = System.currentTimeMillis();
        tickRates[nextIndex] = (time - lastTime) / 20F;
        nextIndex = (nextIndex+1) % tickRates.length;
        lastTime = time;
    }

    public static void reset() {
        Arrays.fill(tickRates, -1);
        nextIndex = 0;
        lastTime = System.currentTimeMillis();
    }

    public static float get() {
        int ticks = 0;
        float total = 0;
        for (float time : tickRates) {
            if (time != -1) {
                ticks++;
                total += time;
            }
        }
        return Math.round(total / ticks);
    }

}
