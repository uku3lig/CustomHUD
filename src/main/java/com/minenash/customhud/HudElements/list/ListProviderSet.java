package com.minenash.customhud.HudElements.list;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ListProviderSet {

    public record Entry(ListProvider provider, UUID id, String prefix) {}

    public final List<Entry> entries;

    public ListProviderSet() {
        entries = new ArrayList<>();
    }

    private ListProviderSet(List<Entry> entries) {
        this.entries = entries;
    }

    public ListProviderSet with(ListProvider provider, UUID providerID, String prefix) {
        return with( new Entry(provider, providerID, prefix) );
    }
    public ListProviderSet with(Entry entry) {
        List<Entry> es = new ArrayList<>(entries);
        es.add(entry);
        return new ListProviderSet( es );
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }
    
    public void push(Entry entry) {
        entries.add(entry);
    }
    public void pop() {
        entries.remove(entries.size()-1);
    }

}

