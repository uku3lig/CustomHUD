package com.minenash.customhud.registry;

import com.minenash.customhud.HudElements.interfaces.HudElement;
import com.minenash.customhud.data.Flags;

import java.util.*;
import java.util.function.BiFunction;

public class CustomHudRegistry {

    private static final Map<String, BiFunction<Flags,ParseContext,HudElement>> elementRegistry = new HashMap<>();
    private static final Map<String, BiFunction<String,ParseContext,HudElement>> parseRegistry = new LinkedHashMap<>();
    private static final List<Runnable> complexData = new ArrayList<>();

    public static void registerElement(String name, BiFunction<Flags,ParseContext,HudElement> element) {
        elementRegistry.put(name, element);
    }

    public static void registerParser(String id, BiFunction<String,ParseContext,HudElement> element) {
        parseRegistry.put(id, element);
    }

    public static void unregisterElement(String name) {
        elementRegistry.remove(name);
    }
    public static void unregisterParser(String id) {
        parseRegistry.remove(id);
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

    public static boolean hasElement(String name) {
        return elementRegistry.containsKey(name);
    }
    public static boolean hasParser(String id) {
        return parseRegistry.containsKey(id);
    }


    public static void registerComplexData(Runnable function) {
        complexData.add(function);
    }

    public static void runComplexData() {
        for (Runnable runnable : complexData)
            runnable.run();
    }

}
