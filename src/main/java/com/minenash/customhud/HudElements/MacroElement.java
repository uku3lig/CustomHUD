package com.minenash.customhud.HudElements;

import com.minenash.customhud.HudElements.functional.FunctionalElement;
import com.minenash.customhud.HudElements.interfaces.ExecuteElement;
import com.minenash.customhud.HudElements.interfaces.HudElement;
import com.minenash.customhud.HudElements.interfaces.MultiElement;
import com.minenash.customhud.HudElements.interfaces.NumElement;
import com.minenash.customhud.ProfileManager;
import com.minenash.customhud.conditionals.Operation;
import com.minenash.customhud.data.Flags;
import com.minenash.customhud.data.Macro;
import com.minenash.customhud.data.NumberFlags;

import java.util.Collections;
import java.util.List;

public class MacroElement implements HudElement, MultiElement, NumElement {

    private final String macroName;
    private final NumberFlags flags;

    public MacroElement(String macroName, Flags flags) {
        this.macroName = macroName;
        this.flags = NumberFlags.of(flags);
    }

    @Override
    public String getString() {
        Macro macro = ProfileManager.getActive().macros.get(macroName);
        if (macro == null)
            return "-";

        if (macro.elements() != null) {
            StringBuilder builder = new StringBuilder();
            for (HudElement element : macro.elements()) {
                if (element instanceof ExecuteElement ee)
                    ee.run();
                else {
                    String str = element.getString();
                    if (str != null)
                        builder.append(str);
                }
            }
            return builder.toString();
        }

        return flags.formatString( macro.op().getValue() );
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

        return Collections.singletonList( new StringElement(flags.formatString( macro.op().getValue() )) );
    }

    @Override
    public boolean ignoreNewlineIfEmpty() {
        return true;
    }

    @Override
    public int getPrecision() {
        return ProfileManager.getActive().macros.get(macroName).elements() != null ? 0 : flags.precision();
    }
}
