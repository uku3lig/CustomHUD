package com.minenash.customhud.HudElements.interfaces;

import net.minecraft.stat.StatFormatter;

public interface NumElement {

    int getPrecision();

    static String formatString(double num, StatFormatter formatter, int precision, int zerofill, int base) {
        if (Double.isNaN(num))
            return "-";
        if (formatter != null)
            return formatter.format((int)num);
        if (base != 10)
            return formatNonDecimalString(num, base, precision, zerofill);
        if (precision == 0 && zerofill == 0)
            return Integer.toString((int)num);

        String zf = zerofill == 0 ? "" : "0" + zerofill;
        return String.format("%" + zf + "."+precision+"f", num);
    }

    private static String formatNonDecimalString(double num, int base, int precision, int zerofill) {
        int whole = (int) num;
        double fractional = Math.abs(num % 1);

        String fracStr = "";
        if (precision != -1) {
            int length = Math.min(precision+2, 32);
            int[] fracArray = new int[ length ];
            for (int i = 0; i < length; i++) {
                fractional *= base;
                fracArray[i] = (int)fractional;
                fractional %= 1;
                if (fractional == 0)
                    break;
            }
            if (precision < 31 && base > 2) {
                if (fracArray[precision+1] >= base/2) fracArray[precision]++;
                if (fracArray[precision] >= base/2) fracArray[precision-1]++;
            }
            StringBuilder fracBuilder = new StringBuilder();
            for (int i = precision-1; i > 0; i--) {
                if (fracArray[i] >= base) {
                    fracArray[i-1]++;
                    fracArray[i] -= base;
                }
                fracBuilder.append( RADIX_CHARS[fracArray[i]] );
            }
            if (fracArray[0] >= base) {
                whole++;
                fracArray[0] -= base;
            }
            fracBuilder.append( RADIX_CHARS[fracArray[0]] );
            fracBuilder.append( '.' );

            fracStr = fracBuilder.reverse().toString();
        }
        String wholeStr = Integer.toString(whole, base).toUpperCase();
        if (wholeStr.length() < zerofill)
            wholeStr = "0".repeat( zerofill - wholeStr.length() ) + wholeStr;
        if (whole == 0 && num < 0)
            wholeStr = "-" + wholeStr;
        return wholeStr + fracStr;

    }
    char[] RADIX_CHARS = new char[]{'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H',
                                    'I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};

}
