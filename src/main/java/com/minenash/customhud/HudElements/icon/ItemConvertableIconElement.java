package com.minenash.customhud.HudElements.icon;

import com.minenash.customhud.data.Flags;
import com.minenash.customhud.render.CustomHudRenderer3;
import com.minenash.customhud.render.RenderPiece;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;

import java.util.UUID;
import java.util.function.Supplier;

public class ItemConvertableIconElement extends IconElement {

    private final Supplier<ItemConvertible> supplier;

    public ItemConvertableIconElement(UUID providerID, Supplier<ItemConvertible> supplier, Flags flags) {
        super(flags, 11);
        this.providerID = providerID;
        this.supplier = supplier;
    }

    @Override
    public void render(DrawContext context, RenderPiece piece) {
        ItemStack stack = new ItemStack(piece.value == null ? supplier.get() : (ItemConvertible)piece.value);
        if (piece.value != null)
            renderItemStack(context, piece.x, piece.y, stack, piece.shiftTextUpOrFitItemIcon);
    }

    @Override
    public int getTextWidth() {
        return CustomHudRenderer3.theme.fitItemIconsToLine ? width : (int) (width * 16F/11);
    }
}
