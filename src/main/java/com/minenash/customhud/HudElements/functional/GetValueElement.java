package com.minenash.customhud.HudElements.functional;

import com.minenash.customhud.HudElements.interfaces.HudElement;
import com.minenash.customhud.HudElements.interfaces.NumElement;
import com.minenash.customhud.HudElements.supplier.NumberSupplierElement;
import com.minenash.customhud.ProfileManager;
import com.minenash.customhud.data.Flags;
import com.minenash.customhud.data.NumberFlags;
import net.minecraft.stat.StatFormatter;

public class GetValueElement implements HudElement, NumElement {

    private final String valueName;
    private final NumberFlags flags;

    public GetValueElement(String valueName, Flags flags) {
        this.valueName = valueName;
        this.flags = NumberFlags.of(flags);
    }


    @Override
    public String getString() {
        return flags.formatString( getNumber().doubleValue() );
    }

    @Override
    public Number getNumber() {
        return ProfileManager.getActive().values.getOrDefault(valueName, Double.NaN);
    }

    @Override
    public boolean getBoolean() {
        return getNumber().doubleValue() > 0;
    }

    @Override
    public int getPrecision() {
        return flags.precision();
    }
}
