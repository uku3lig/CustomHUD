package com.minenash.customhud.HudElements.interfaces;

import java.util.List;

public interface MultiElement {

    List<HudElement> expand();
    boolean ignoreNewlineIfEmpty();

    default String expandIntoString() {
        List<HudElement> elements = expand();

        StringBuilder builder = new StringBuilder();
        for (HudElement element : elements) {
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

}
