package com.minenash.customhud.HudElements;

import com.minenash.customhud.HudElements.interfaces.HudElement;
import com.minenash.customhud.HudElements.interfaces.NumElement;
import com.minenash.customhud.conditionals.Operation;
import com.minenash.customhud.data.Flags;
import com.minenash.customhud.data.NumberFlags;

public class TimerElement implements HudElement, NumElement {

    private final NumberFlags flags;
    private final Operation interval;
    private final Operation end;

    private long lastMS = System.currentTimeMillis();
    private int value = 0;

    public TimerElement(Operation end, Operation interval, Flags flags) {
        this.flags = NumberFlags.of(flags);
        this.interval = interval;
        this.end = end;
    }

    public int get() {
        long currentMs = System.currentTimeMillis();
        long diff = currentMs - lastMS;
        int inter = Math.max(1,(int)(interval.getValue()*1000));
        lastMS = currentMs - (diff % inter);
        value += diff / inter;
        if (value >= end.getValue())
            value = 0;
        return value;
    }

    @Override
    public String getString() {
        return flags.formatString(get());
    }

    @Override
    public Number getNumber() {
        return get();
    }

    @Override
    public boolean getBoolean() {
        return get() > 0;
    }

    @Override
    public int getPrecision() {
        return flags.precision();
    }
}
