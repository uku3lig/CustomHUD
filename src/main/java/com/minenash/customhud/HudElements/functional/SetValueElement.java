package com.minenash.customhud.HudElements.functional;

import com.minenash.customhud.ProfileManager;
import com.minenash.customhud.conditionals.Operation;

public class SetValueElement extends FunctionalElement.IgnoreNewLineIfSurroundedByNewLine implements ExecuteElement {

    private final String valueName;
    private final Operation expression;

    public SetValueElement(String valueName, Operation expression) {
        this.valueName = valueName;
        this.expression = expression;
    }

    public void run() {
        ProfileManager.getActive().values.put(valueName, expression.getValue());
    }
}
