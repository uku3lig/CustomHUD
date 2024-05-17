package com.minenash.customhud.HudElements.supplier;

import com.minenash.customhud.HudElements.interfaces.HudElement;
import com.minenash.customhud.HudElements.interfaces.NumElement;
import com.minenash.customhud.data.Flags;
import net.minecraft.stat.StatFormatter;

import java.util.function.Supplier;

public class NumberSupplierElement implements HudElement, NumElement {

    public record Entry(Supplier<Number> supplier, int precision, StatFormatter formatter) {}
    public static Entry of(Supplier<Number> supplier, int precision) {
        return new Entry(supplier, precision, null);
    }
    public static Entry of(Supplier<Number> supplier, int precision, StatFormatter formatter) {
        return new Entry(supplier, precision, formatter);
    }

    private final Supplier<Number> supplier;
    private final int precision;
    private final double scale;
    private final int zerofill;
    private StatFormatter formatter = null;
    private final int base;

    public NumberSupplierElement(Entry entry, Flags flags) {
        this(entry.supplier, flags);
        this.formatter = flags.formatted ? entry.formatter : null;
    }

    public NumberSupplierElement(Supplier<Number> supplier, Flags flags) {
        this.supplier = supplier;
        this.precision = flags.precision == -1 ? 0 : flags.precision;
        this.zerofill = flags.zerofill;
        this.scale = flags.scale;
        this.base = flags.base;
    }

    @Override
    public int getPrecision() {
        return precision;
    }

    @Override
    public String getString() {
        try {
            double num = supplier.get().doubleValue() * scale;
            return NumElement.formatString(num, formatter, precision, zerofill, base);

        }
        catch (Exception _e) {
            return "-";
        }
    }

    @Override
    public Number getNumber() {
        return sanitize(supplier, Double.NaN);
    }

    @Override
    public boolean getBoolean() {
        return sanitize(supplier, Double.NaN).doubleValue() > 0;
    }

}
