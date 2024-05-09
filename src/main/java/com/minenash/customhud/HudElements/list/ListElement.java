package com.minenash.customhud.HudElements.list;

import com.minenash.customhud.HudElements.interfaces.HudElement;
import com.minenash.customhud.HudElements.interfaces.MultiElement;
import com.minenash.customhud.HudElements.functional.FunctionalElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListElement implements HudElement, MultiElement {

    private static final HudElement POP_LIST_ELEMENT = new FunctionalElement.PopList();
    private static final HudElement ADVANCE_LIST_ELEMENT = new FunctionalElement.AdvanceList();

    private final ListProvider provider;
    private final List<HudElement> elements;
    private final List<HudElement> separator;
//    private final boolean removeLastNewLine;

    public ListElement(ListProvider provider, List<HudElement> format, List<HudElement> separator) {
        this.provider = provider;
        this.elements = format;
//        this.removeLastNewLine = elements.size() > 1 && elements.get(elements.size()-1) instanceof FunctionalElement.NewLine;
        this.separator = separator;
        this.separator.add(ADVANCE_LIST_ELEMENT);
    }

    public List<HudElement> expand() {
        if (elements == null)
            return List.of(this);
        List<?> values = provider.get();
        if (values.isEmpty())
            return Collections.emptyList();

        List<HudElement> expanded = new ArrayList<>();
        expanded.add(new FunctionalElement.PushList(values));

//        List<HudElement> expandedElements = new ArrayList<>();
//        for (HudElement element : elements)
//            CustomHudRenderer.addElement(expandedElements, element);
//        expandedElements.add(ADVANCE_LIST_ELEMENT);
//
//        for (HudElement element : expandedElements)
//            if (element instanceof IconElement ie)
//                ie.setList(values);

        for (int i = 0; i < values.size(); i++) {
            expanded.addAll(elements);
            if (i < values.size() - 1)
                expanded.addAll(separator);
        }
        expanded.add(POP_LIST_ELEMENT);
//        if (removeLastNewLine)
//            expanded.remove(expanded.size()-2);
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

        public MultiLineBuilder(ListProvider provider) {
            this.provider = provider == null ? EMPTY : provider;
        }

        public void add(HudElement element) {
            this.elements.add(element);
        }

        public void addAll(List<HudElement> elements) {
            this.elements.addAll(elements);
        }

        public ListElement build() {
            return new ListElement(provider, elements, Collections.EMPTY_LIST);
        }

    }



}
