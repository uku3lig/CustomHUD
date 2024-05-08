package com.minenash.customhud.HudElements.icon;

import com.minenash.customhud.data.Flags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Colors;
import net.minecraft.util.math.MathHelper;

public class SlotItemIconElement extends IconElement {
    private static final MinecraftClient client = MinecraftClient.getInstance();

    private final int slot;
    private final boolean showCount, showDur, showCooldown;
    private final int width;

    public SlotItemIconElement(int slot, Flags flags) {
        super(flags);
        this.slot = slot;
        this.width = flags.iconWidth != -1 ? flags.iconWidth : MathHelper.ceil(11*scale);;
        this.showCount = flags.iconShowCount;
        this.showDur = flags.iconShowDur;
        this.showCooldown = flags.iconShowCooldown;
    }

    private ItemStack getStack() {
        return client.player.getStackReference(slot).get();
    }

    @Override
    public Number getNumber() {
        return Item.getRawId(getStack().getItem());
    }

    @Override
    public boolean getBoolean() {
        return getStack().isEmpty();
    }

    @Override
    public int getTextWidth() {
        return getStack().isEmpty() ? 0 : width;
    }

    public void render(DrawContext context, int x, int y, float profileScale) {
        ItemStack stack = getStack();
        if (stack == null || stack.isEmpty())
            return;
        x += shiftX;
        y += shiftY;

        renderItemStack(context, x, y, profileScale, stack);

        if (showCount || showDur || showCooldown) {

            MatrixStack matrixStack = context.getMatrices();
            matrixStack.push();
            matrixStack.translate(x, y - 2, 200);
            if (!referenceCorner)
                matrixStack.translate(0, -(11 * scale - 11) / 2, 0);
            matrixStack.scale(11 * scale / 16, 11 * scale / 16, 1);

            if (showCount && stack.getCount() != 1) {
                matrixStack.push();
                String count = Integer.toString(stack.getCount());
                context.drawText(client.textRenderer, count, 19 - 2 - client.textRenderer.getWidth(count), 6 + 3, 0xFFFFFF, true);
                matrixStack.pop();
            }

            if (showDur && stack.isItemBarVisible()) {
                context.fill(RenderLayer.getGuiOverlay(), 2, 13, 2 + 13, 13 + 2, Colors.BLACK);
                context.fill(RenderLayer.getGuiOverlay(), 2, 13, 2 + stack.getItemBarStep(), 13 + 1, stack.getItemBarColor() | Colors.BLACK);
            }

            if (showCooldown) {
                float f = client.player == null ? 0.0F : client.player.getItemCooldownManager().getCooldownProgress(stack.getItem(), client.getTickDelta());
                if (f > 0.0F) {
                    int k = MathHelper.floor(16.0F * (1.0F - f));
                    int l = k + MathHelper.ceil(16.0F * f);
                    context.fill(RenderLayer.getGuiOverlay(), 0, k, 16, l, Integer.MAX_VALUE);
                }
            }
            matrixStack.pop();
        }
    }

}
