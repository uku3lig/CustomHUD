package com.minenash.customhud.HudElements.list;

import com.minenash.customhud.HudElements.functional.FunctionalElement;
import com.minenash.customhud.HudElements.interfaces.HudElement;
import com.minenash.customhud.HudElements.interfaces.MultiElement;
import com.minenash.customhud.complex.ListManager;
import com.minenash.customhud.conditionals.Operation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class FilteredListElement extends FunctionalElement implements HudElement, MultiElement {

    private final HudElement popList, advanceList;

    private final UUID providerID;
    private final ListProvider provider;
    private final List<HudElement> main;
    private final List<HudElement> last;
    private final Operation operation;
    private final boolean multiline;

    public FilteredListElement(ListProvider provider, UUID providerID, List<HudElement> format, List<HudElement> separator, Operation operation, boolean multiline) {
        this.provider = provider;
        this.providerID = providerID;
        this.operation = operation;
        this.popList = new FunctionalElement.PopList(providerID);
        this.advanceList = new FunctionalElement.AdvanceList(providerID);
        this.multiline = multiline;
        last = format;

        if (format == null)
            main = null;
        else {
            main = new ArrayList<>(format);
            if (separator != null)
                main.addAll(separator);
        }

    }

    public List<HudElement> expand() {
        if (main == null)
            return Collections.EMPTY_LIST;
        List<?> values = provider.get();
        if (values.isEmpty())
            return Collections.emptyList();

        List notFiltered = new ArrayList<>(values.size());
        ListManager.push(providerID, values);
        for (Object value : values) {
            if (operation.getBooleanValue())
                notFiltered.add(value);
            ListManager.advance(providerID);
        }
        ListManager.pop(providerID);

        if (notFiltered.isEmpty())
            return Collections.EMPTY_LIST;

        List<HudElement> expanded = new ArrayList<>();
        expanded.add(new FunctionalElement.PushList(providerID, notFiltered));

        for (int i = 0; i < notFiltered.size(); i++) {
            expanded.addAll(i < notFiltered.size() - 1 ? main : last);
            expanded.add(advanceList);
        }

        expanded.set(expanded.size()-1, popList);
        return expanded;
    }

    @Override
    public boolean ignoreNewlineIfEmpty() {
        return !multiline;
    }

//    @Override
//    public String getString() {
//        return getNumber().toString();
//    }
//
//    @Override
//    public Number getNumber() {
//        return provider.get().size();
//    }
//
//    @Override
//    public boolean getBoolean() {
//        return provider.get().isEmpty();
//    }

}
