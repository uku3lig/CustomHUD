package com.minenash.customhud.HudElements;

import com.minenash.customhud.HudElements.interfaces.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;

public class ItemCountElement implements HudElement {

    private final Item item;

    public ItemCountElement(Item item) {
        this.item = item;
    }

    @Override
    public String getString() {
        return getNumber().toString();
    }

    @Override
    public Number getNumber() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        return player == null ? 0 : player.getInventory().count(item);
    }

    @Override
    public boolean getBoolean() {
        return getNumber().intValue() > 0;
    }
}
