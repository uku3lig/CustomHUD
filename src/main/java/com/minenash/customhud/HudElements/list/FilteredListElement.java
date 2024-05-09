package com.minenash.customhud.HudElements.list;

import com.minenash.customhud.HudElements.functional.FunctionalElement;
import com.minenash.customhud.HudElements.interfaces.HudElement;
import com.minenash.customhud.HudElements.interfaces.MultiElement;
import com.minenash.customhud.complex.ListManager;
import com.minenash.customhud.conditionals.Operation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilteredListElement implements HudElement, MultiElement {

    private static final HudElement POP_LIST_ELEMENT = new FunctionalElement.PopList();
    private static final HudElement ADVANCE_LIST_ELEMENT = new FunctionalElement.AdvanceList();

    private final ListProvider provider;
    private final List<HudElement> main;
    private final List<HudElement> last;
    private final Operation operation;

    public FilteredListElement(ListProvider provider, List<HudElement> format, List<HudElement> separator, Operation operation) {
        this.provider = provider;
        this.operation = operation;
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
        ListManager.push(values);
        for (Object value : values) {
            if (operation.getBooleanValue())
                notFiltered.add(value);
            ListManager.advance();
        }
        ListManager.pop();

        if (notFiltered.isEmpty())
            return Collections.EMPTY_LIST;

        List<HudElement> expanded = new ArrayList<>();
        expanded.add(new FunctionalElement.PushList(notFiltered));

        for (int i = 0; i < notFiltered.size(); i++) {
            expanded.addAll(i < notFiltered.size() - 1 ? main : last);
            expanded.add(ADVANCE_LIST_ELEMENT);
        }

        expanded.set(expanded.size()-1, POP_LIST_ELEMENT);
        return expanded;
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

    public static class MultiLineBuilder {
        private static final ListProvider EMPTY = () -> Collections.EMPTY_LIST;

        public final ListProvider provider;
        private final List<HudElement> elements = new ArrayList<>();
        private final List<HudElement> separator = new ArrayList<>();
        private boolean separatorMode = false;

        public MultiLineBuilder(ListProvider provider) {
            this.provider = provider == null ? EMPTY : provider;
        }

        public void add(HudElement element) {
            (separatorMode ? separator : elements).add(element);
        }

        public void addAll(List<HudElement> elements) {
            (separatorMode ? separator : this.elements).addAll(elements);
        }

        public void separatorMode() {
            this.separatorMode = true;
        }

        public FilteredListElement build() {
            return new FilteredListElement(provider, elements, Collections.EMPTY_LIST, new Operation.Literal(1));
        }

    }



}
