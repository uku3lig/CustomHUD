package com.minenash.customhud.HudElements.functional;

import com.minenash.customhud.HudElements.interfaces.HudElement;
import com.minenash.customhud.HudElements.interfaces.NumElement;
import com.minenash.customhud.ProfileManager;
import com.minenash.customhud.data.Flags;
import com.minenash.customhud.data.NumberFlags;

public class GetValueElement implements HudElement, NumElement {

    private final String valueName;
    private final NumberFlags flags;

    public GetValueElement(String valueName, Flags flags) {
        this.valueName = valueName;
        this.flags = NumberFlags.of(flags);
    }


    @Override
    public String getString() {
        String strValue = ProfileManager.getActive().strValues.get(valueName);
        return strValue != null ? strValue : flags.formatString( getNum() );
    }

    @Override
    public Number getNumber() {
        String strValue = ProfileManager.getActive().strValues.get(valueName);
        return strValue != null ? strValue.length() : getNum();
    }

    @Override
    public boolean getBoolean() {
        String strValue = ProfileManager.getActive().strValues.get(valueName);
        return strValue != null ? strValue.isEmpty() : getNum() > 0;
    }

    @Override
    public int getPrecision() {
        return flags.precision();
    }

    private double getNum() {
        return ProfileManager.getActive().numValues.getOrDefault(valueName, Double.NaN);
    }
}
