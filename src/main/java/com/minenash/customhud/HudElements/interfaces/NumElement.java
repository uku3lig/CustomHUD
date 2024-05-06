package com.minenash.customhud.HudElements.interfaces;

import net.minecraft.stat.StatFormatter;

public interface NumElement {

    int getPrecision();

    static String formatString(double num, StatFormatter formatter, int precision) {
        if (Double.isNaN(num))
            return "-";
        if (formatter != null)
            return formatter.format((int)num);
        if (precision == 0)
            return Integer.toString((int)num);

        return String.format("%."+precision+"f", num);
    }

}
