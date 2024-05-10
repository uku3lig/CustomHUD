package com.minenash.customhud.HudElements.list;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ListProviderSet {

    public final List<ListProvider> providers;
    public final List<UUID> providerIDs;

    public ListProviderSet() {
        providers = new ArrayList<>();
        providerIDs = new ArrayList<>();
    }

    private ListProviderSet(List<ListProvider> list, List<UUID> ids) {
        providers = list;
        providerIDs = ids;
    }

    public ListProviderSet with(ListProvider provider, UUID providerID) {
        List<ListProvider> list = new ArrayList<>(providers);
        list.add(provider);

        List<UUID> ids = new ArrayList<>(providerIDs);
        ids.add( providerID );

        return new ListProviderSet( list, ids );
    }

    public boolean isEmpty() {
        return providers.isEmpty();
    }
    
    public UUID push(ListProvider provider) {
        providers.add(provider);
        UUID providerID = UUID.randomUUID();
        providerIDs.add( providerID );
        return providerID;
    }
    public void pop() {
        int index = providers.size()-1;
        providers.remove(index);
        providerIDs.remove(index);
    }

}

