package com.minenash.customhud.HudElements;

import com.minenash.customhud.HudElements.interfaces.HudElement;
import com.minenash.customhud.HudElements.interfaces.NumElement;
import com.minenash.customhud.data.Flags;
import com.minenash.customhud.data.NumberFlags;

public class IntElement implements HudElement, NumElement {
    private final float color;
    private final NumberFlags flags;

    public IntElement(int color, Flags flags) {
        this.color = (float) (color * flags.scale);
        this.flags = NumberFlags.of(flags);
    }

    @Override
    public String getString() {
        return flags.formatString(color);
    }

    @Override
    public Number getNumber() {
        return color;
    }

    @Override
    public boolean getBoolean() {
        return color > 0;
    }

    @Override
    public int getPrecision() {
        return flags.precision();
    }
}
