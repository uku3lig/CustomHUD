package com.minenash.customhud.HudElements.icon;

import com.minenash.customhud.complex.ListManager;
import com.minenash.customhud.complex.MusicAndRecordTracker;
import com.minenash.customhud.data.Flags;
import com.minenash.customhud.render.RenderPiece;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ListRecordIconElement extends IconElement {

    private static final ItemStack NO_RECORD = new ItemStack(Items.BARRIER);

    public ListRecordIconElement(Flags flags) {
        super(flags, 11);
    }

    public void render(DrawContext context, RenderPiece piece) {
        renderItemStack(context, piece.x, piece.y, ((MusicAndRecordTracker.RecordInstance) piece.value).icon);
    }

}
