package com.minenash.customhud.HudElements.functional;

import com.minenash.customhud.HudElements.interfaces.HudElement;
import com.minenash.customhud.HudElements.interfaces.NumElement;
import com.minenash.customhud.HudElements.supplier.NumberSupplierElement;
import com.minenash.customhud.ProfileManager;
import com.minenash.customhud.data.Flags;
import net.minecraft.stat.StatFormatter;

public class GetValueElement implements HudElement {

    private final String valueName;
    private final int precision;
    private final double scale;
    private final StatFormatter formatter;

    public GetValueElement(String valueName, Flags flags) {
        this.valueName = valueName;
        this.precision = flags.precision == -1 ? 0 : flags.precision;
        this.scale = flags.scale;
        this.formatter = flags.hex ? NumberSupplierElement.HEX : null;
    }


    @Override
    public String getString() {
        return NumElement.formatString( getNumber().doubleValue() * scale, formatter, precision );
    }

    @Override
    public Number getNumber() {
        return ProfileManager.getActive().values.getOrDefault(valueName, Double.NaN);
    }

    @Override
    public boolean getBoolean() {
        return getNumber().doubleValue() > 0;
    }
}
