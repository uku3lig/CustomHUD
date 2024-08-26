package com.minenash.customhud.HudElements.functional;

import com.minenash.customhud.HudElements.interfaces.ExecuteElement;
import com.minenash.customhud.HudElements.interfaces.HudElement;
import com.minenash.customhud.ProfileManager;
import com.minenash.customhud.conditionals.Operation;

import java.util.List;

public class SetValueElement extends FunctionalElement.IgnoreNewLineIfSurroundedByNewLine implements ExecuteElement {

    private final String valueName;
    private final Operation expression;
    private final List<HudElement> elements;

    public SetValueElement(String valueName, Operation expression, List<HudElement> elements) {
        this.valueName = valueName;
        this.expression = expression;
        this.elements = elements;
    }

    public void run() {
        if (expression != null)
            ProfileManager.getActive().numValues.put(valueName, expression.getValue());
        else if (elements != null) {
            StringBuilder builder = new StringBuilder();
            for (var element : elements) {
                builder.append(element.getString());
            }
            ProfileManager.getActive().strValues.put(valueName, builder.toString());
        }
    }
}
