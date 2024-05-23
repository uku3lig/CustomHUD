package com.minenash.customhud.HudElements;

import com.minenash.customhud.HudElements.interfaces.HudElement;
import com.minenash.customhud.data.Flags;
import com.minenash.customhud.data.NumberFlags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;

public class ItemCountElement implements HudElement {

    private final Item item;
    private final NumberFlags flags;

    public ItemCountElement(Item item, Flags flags) {
        this.item = item;
        this.flags = NumberFlags.of(flags);
    }

    @Override
    public String getString() {
        return flags.formatString( getNumber().doubleValue() );
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
