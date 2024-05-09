package com.minenash.customhud.HudElements.list;

import com.minenash.customhud.HudElements.ConditionalElement;
import com.minenash.customhud.HudElements.interfaces.HudElement;
import com.minenash.customhud.HudElements.interfaces.MultiElement;
import com.minenash.customhud.HudElements.functional.FunctionalElement;
import com.minenash.customhud.conditionals.Operation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListElement implements HudElement, MultiElement {

    private static final HudElement POP_LIST_ELEMENT = new FunctionalElement.PopList();
    private static final HudElement ADVANCE_LIST_ELEMENT = new FunctionalElement.AdvanceList();

    private final ListProvider provider;
    private final List<HudElement> main;
    private final List<HudElement> last;

    public ListElement(ListProvider provider, List<HudElement> format, List<HudElement> separator) {
        this.provider = provider;
        last = format;

        if (format == null)
            main = null;
        else {
            main = new ArrayList<>(format);
            if (separator != null)
                main.addAll(separator);
        }

    }

    public static HudElement of(ListProvider provider, List<HudElement> format, List<HudElement> separator, Operation operation) {
        return operation == null ? new ListElement(provider, format, separator) : new FilteredListElement(provider, format, separator, operation);
    }

    public List<HudElement> expand() {
        if (main == null)
            return Collections.EMPTY_LIST;
        List<?> values = provider.get();
        if (values.isEmpty())
            return Collections.emptyList();

        List<HudElement> expanded = new ArrayList<>();
        expanded.add(new FunctionalElement.PushList(values));

        for (int i = 0; i < values.size(); i++) {
            expanded.addAll(i < values.size() - 1 ? main : last);
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
        private final Operation filter;
        private boolean separatorMode = false;

        public MultiLineBuilder(ListProvider provider, Operation filter) {
            this.provider = provider == null ? EMPTY : provider;
            this.filter = filter;
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

        public HudElement build() {
            return filter == null ? new ListElement(provider, elements, separator) : new FilteredListElement(provider, elements, separator, filter);
        }

    }



}
