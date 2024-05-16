package com.minenash.customhud.HudElements;

import com.minenash.customhud.HudElements.functional.FunctionalElement;
import com.minenash.customhud.HudElements.interfaces.HudElement;
import com.minenash.customhud.HudElements.interfaces.MultiElement;
import com.minenash.customhud.HudElements.interfaces.NumElement;
import com.minenash.customhud.HudElements.supplier.NumberSupplierElement;
import com.minenash.customhud.ProfileManager;
import com.minenash.customhud.conditionals.Operation;
import com.minenash.customhud.data.Flags;
import com.minenash.customhud.data.Macro;
import net.minecraft.stat.StatFormatter;

import java.util.Collections;
import java.util.List;

public class MacroElement implements HudElement, MultiElement {

    private final String macroName;
    private final int precision;
    private final int zerofill;
    private final double scale;
    private final StatFormatter formatter;

    public MacroElement(String macroName, Flags flags) {
        this.macroName = macroName;
        this.precision = flags.precision == -1 ? 0 : flags.precision;
        this.zerofill = flags.zerofill;
        this.scale = flags.scale;
        this.formatter = flags.hex ? NumberSupplierElement.HEX : null;
    }

    @Override
    public String getString() {
        Macro macro = ProfileManager.getActive().macros.get(macroName);
        if (macro == null)
            return "-";

        if (macro.elements() != null) {
            StringBuilder builder = new StringBuilder();
            for (HudElement element : macro.elements()) {
                if (element instanceof FunctionalElement.XList xl)
                    xl.run();
                else
                    builder.append(element.getString());
            }
            return builder.toString();
        }

        return Double.toString( macro.op().getValue() );
    }

    @Override
    public Number getNumber() {
        Macro macro = ProfileManager.getActive().macros.get(macroName);
        if (macro == null)
            return Double.NaN;

        if (macro.elements() != null)
            return new Operation.Length(macro.elements()).getValue();

        return macro.op().getValue();
    }

    @Override
    public boolean getBoolean() {
        return getNumber().doubleValue() > 0;
    }

    @Override
    public List<HudElement> expand() {
        Macro macro = ProfileManager.getActive().macros.get(macroName);
        if (macro == null)
            return Collections.singletonList( new StringElement("-") );

        if (macro.elements() != null)
            return macro.elements();

        return Collections.singletonList( new StringElement( NumElement.formatString(macro.op().getValue() * scale, formatter, precision, zerofill) ) );
    }
}
