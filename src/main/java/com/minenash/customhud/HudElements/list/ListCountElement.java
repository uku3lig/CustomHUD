package com.minenash.customhud.HudElements.list;

import com.minenash.customhud.HudElements.interfaces.HudElement;

public class ListCountElement implements HudElement {
    public final ListProvider provider;
    public final HudElement attribute;

    public ListCountElement(ListProvider provider, HudElement attribute) {
        this.provider = provider;
        this.attribute = attribute;
    }

    @Override
    public String getString() {
        return getNumber().toString();
    }

    @Override
    public Number getNumber() {
        return provider.get().size();
    }

    @Override
    public boolean getBoolean() {
        return provider.get().isEmpty();
    }

}
