package com.minenash.customhud.complex;

import com.minenash.customhud.data.CHFormatting;

import java.util.*;
import java.util.function.Supplier;

public class ListManager {

    private static final Map<UUID, Integer> index = new HashMap<>();
    private static final Map<UUID, List<?>> values = new HashMap<>();

    //REMOVED: CHFormatting input
    public static void push(UUID providerID, List<?> values) {
        ListManager.index.put(providerID, 0);
        ListManager.values.put(providerID, values);
    }

    //REMOVED: RETURNED COLOR
    public static void pop(UUID providerID) {
        ListManager.index.remove(providerID);
        ListManager.values.remove(providerID);
    }

    public static void advance(UUID providerID) {
        ListManager.index.put(providerID, ListManager.index.get(providerID)+1);
    }

    public static int getCount(UUID providerID) {
        return values.get(providerID).size();
    }

    public static int getIndex(UUID providerID) {
        return index.get(providerID);
    }

    public static Object getValue(UUID providerID) {
        return providerID == null ? null : !values.containsKey(providerID) ? null : values.get(providerID).get(index.get(providerID));
    }
    public static Object getValue(UUID providerID, int index) {
        return values.containsKey(providerID) ? null : values.get(providerID).get(index);
    }

    public static Supplier<?> supplier(UUID providerID) {
        return () -> getValue(providerID);
    }

}
