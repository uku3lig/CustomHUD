package com.minenash.customhud.HudElements;

import com.minenash.customhud.HudElements.interfaces.HudElement;
import com.minenash.customhud.HudElements.interfaces.NumElement;
import com.minenash.customhud.data.Flags;

public class IntElement implements HudElement, NumElement {
    private final float color;
    private final int precision;
    private final int zerofill;
    private final int base;

    public IntElement(int color, Flags flags) {
        this.color = (float) (color * flags.scale);
        this.precision = flags.precision == -1 ? 0 : flags.precision;
        this.zerofill = flags.zerofill;
        this.base = flags.base;
    }

    @Override
    public String getString() {
        return NumElement.formatString(color, null, precision, zerofill, base);
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
        return precision;
    }
}
