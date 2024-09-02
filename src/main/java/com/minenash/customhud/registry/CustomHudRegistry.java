package com.minenash.customhud.registry;

import com.minenash.customhud.HudElements.interfaces.HudElement;
import com.minenash.customhud.HudElements.list.Attributers;
import com.minenash.customhud.HudElements.list.ListProvider;
import com.minenash.customhud.complex.ComplexData;
import com.minenash.customhud.data.Flags;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class CustomHudRegistry {

    private static final Map<String, BiFunction<Flags,ParseContext,HudElement>> elementRegistry = new HashMap<>();
    private static final Map<String, BiFunction<String,ParseContext,HudElement>> parseRegistry = new LinkedHashMap<>();
    private static final Map<String, ListProvider> listRegistry = new HashMap<>();
    private static final Map<String, ComplexData.Enabled> listRegistryEnabled = new HashMap<>();
    private static final List<Consumer<ComplexData.Enabled>> complexData = new ArrayList<>();

    public static void registerElement(String name, BiFunction<Flags,ParseContext,HudElement> element) {
        elementRegistry.put(name, element);
    }

    public static void registerParser(String id, BiFunction<String,ParseContext,HudElement> element) {
        parseRegistry.put(id, element);
    }

    public static void registerList(String name, String prefix, ListProvider listProvider, Attributers.Attributer attributer) {
        Attributers.ATTRIBUTER_MAP.put(listProvider, attributer);
        Attributers.DEFAULT_PREFIX.put(attributer, prefix);
        listRegistry.put(name, listProvider);
    }

    public static void registerList(String name, String prefix, ListProvider listProvider, Attributers.Attributer attributer, ComplexData.Enabled enabled) {
        registerList(name, prefix, listProvider, attributer);
        listRegistryEnabled.put(name, enabled);
    }

    public static void unregisterElement(String name) {
        elementRegistry.remove(name);
    }
    public static void unregisterParser(String id) {
        parseRegistry.remove(id);
    }
    public static void unregisterList(String name) {
        Attributers.ATTRIBUTER_MAP.remove(listRegistry.get(name));
        listRegistry.remove(name);
    }

    public static HudElement get(String variable, ParseContext context) {
        for (var parser : parseRegistry.values()) {
            HudElement e = parser.apply(variable, context);
            if (e != null)
                return e;
        }

        String[] parts = variable.split(" ");

        var function = elementRegistry.get(parts[0]);
        return function == null ? null : function.apply(Flags.parse(context.profile().name, context.line(), parts), context);
    }
    public static ListProvider getList(String name, ComplexData.Enabled enabled) {
        ComplexData.Enabled listEnable = listRegistryEnabled.get(name);
        if (listEnable != null)
            enabled.merge(listEnable);
        return listRegistry.get(name);
    }

    public static boolean hasElement(String name) {
        return elementRegistry.containsKey(name);
    }
    public static boolean hasParser(String id) {
        return parseRegistry.containsKey(id);
    }
    public static boolean hasList(String name) {
        return parseRegistry.containsKey(name);
    }


    public static void registerComplexData(Consumer<ComplexData.Enabled> function) {
        complexData.add(function);
    }

    public static void runComplexData(ComplexData.Enabled enabled) {
        for (var complex : complexData)
            complex.accept(enabled);
    }

}
