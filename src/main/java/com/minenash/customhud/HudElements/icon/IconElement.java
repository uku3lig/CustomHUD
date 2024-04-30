package com.minenash.customhud.HudElements.icon;

import com.minenash.customhud.data.Flags;
import com.minenash.customhud.HudElements.functional.FunctionalElement;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public abstract class IconElement extends FunctionalElement {
    private static final MinecraftClient client = MinecraftClient.getInstance();

    protected final float scale;
    protected final int shiftX;
    protected final int shiftY;
    protected final boolean referenceCorner;

    protected IconElement(Flags flags) {
        scale = (float) flags.scale;
        shiftX = flags.iconShiftX;
        shiftY = flags.iconShiftY;
        referenceCorner = flags.iconReferenceCorner;
    }

    public abstract void render(DrawContext context, int x, int y, float profileScale);
    public abstract int getTextWidth();

    @Override
    public String getString() {
        return "\uFFFE";
    }

    public void renderItemStack(DrawContext context, int x, int y, float profileScale, ItemStack stack) {
        //TODO: FINISH 1.20.5
        MatrixStack matrixStack = context.getMatrices();
        matrixStack.push();
        matrixStack.scale(profileScale, profileScale, 1);
        matrixStack.translate(x+(5.5*scale-5.5), y+(5.5*scale-5.5)-2, 100.0); //+ client.getItemRenderer().zOffset
        if (referenceCorner)
            matrixStack.translate(0, (11*scale-11)/2, 0);
        matrixStack.scale(11 * scale / 16, 11 * scale / 16, 1);
//
//        RenderSystem.disableBlend();

        context.drawItem(stack, 0, 0);
//        RenderSystem.enableBlend();
        matrixStack.pop();
    }
}
