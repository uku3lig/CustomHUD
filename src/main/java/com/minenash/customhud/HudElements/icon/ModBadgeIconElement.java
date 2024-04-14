package com.minenash.customhud.HudElements.icon;

import com.minenash.customhud.complex.ListManager;
import com.minenash.customhud.data.Flags;
import com.minenash.customhud.render.RenderPiece;
import com.terraformersmc.modmenu.util.DrawingUtil;
import com.terraformersmc.modmenu.util.mod.Mod;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import static com.minenash.customhud.CustomHud.CLIENT;

public class ModBadgeIconElement extends IconElement{

    public ModBadgeIconElement(Flags flags) {
        super(flags, 0);
    }

    @Override
    public void render(DrawContext context, RenderPiece piece) {
        MatrixStack matrices = context.getMatrices();
        matrices.push();

        Mod.Badge badge = (Mod.Badge) piece.value;
        int width = CLIENT.textRenderer.getWidth(badge.getText()) + 6;

        matrices.translate(piece.x + shiftX, piece.y + shiftY, 0);
        if (!referenceCorner)
            matrices.translate(0, -(5*scale-5)/2, 0);
        matrices.scale(scale, scale, 0);
        rotate(matrices, width, 11);


        DrawingUtil.drawBadge(context, 0, -1, width, badge.getText().asOrderedText(),badge.getOutlineColor(), badge.getFillColor(), piece.color);

        matrices.pop();
    }

    @Override
    public int getTextWidth() {
        return CLIENT.textRenderer.getWidth( ((Mod.Badge)ListManager.getValue()).getText() ) + 6;
    }
}
