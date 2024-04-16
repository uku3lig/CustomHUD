package com.minenash.customhud.HudElements.functional;

import com.minenash.customhud.ProfileManager;
import com.minenash.customhud.data.Macro;

public class SetMacroElement extends FunctionalElement.IgnoreNewLineIfSurroundedByNewLine implements ExecuteElement {

    private final String valueName;
    private final Macro macro;

    public SetMacroElement(String valueName, Macro macro) {
        this.valueName = valueName;
        this.macro = macro;
    }


    @Override
    public void run() {
        ProfileManager.getActive().macros.put(valueName, macro);
    }
}
