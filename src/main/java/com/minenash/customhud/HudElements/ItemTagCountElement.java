package com.minenash.customhud.HudElements;

import com.google.common.collect.ImmutableList;
import com.minenash.customhud.HudElements.interfaces.HudElement;
import com.minenash.customhud.data.Flags;
import com.minenash.customhud.data.NumberFlags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;

public class ItemTagCountElement implements HudElement {

    private final Identifier tag;
    private final NumberFlags flags;

    public ItemTagCountElement(Identifier tag, Flags flags) {
        this.tag = tag;
        this.flags = NumberFlags.of(flags);
    }

    @Override
    public String getString() {
        return flags.formatString( getNumber().doubleValue() );
    }

    @Override
    public Number getNumber() {
        TagKey<Item> tagKey = TagKey.of(RegistryKeys.ITEM, tag);

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null)
            return 0;

        int count = 0;
        PlayerInventory inv = player.getInventory();
        for (var item : inv.main)
            if (item.isIn(tagKey))
                count += item.getCount();
        for (var item : inv.armor)
            if (item.isIn(tagKey))
                count += item.getCount();
        for (var item : inv.offHand)
            if (item.isIn(tagKey))
                count += item.getCount();
        return count;
    }

    @Override
    public boolean getBoolean() {
        return getNumber().intValue() > 0;
    }
}
