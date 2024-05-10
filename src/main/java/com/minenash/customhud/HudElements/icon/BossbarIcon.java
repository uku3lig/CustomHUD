package com.minenash.customhud.HudElements.icon;

import com.minenash.customhud.HudElements.list.ListProvider;
import com.minenash.customhud.data.Flags;
import com.minenash.customhud.render.RenderPiece;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.BossBar;

import java.util.UUID;

import static com.minenash.customhud.CustomHud.CLIENT;

public class BossbarIcon extends IconElement {

    public static class BasicBar extends BossBar {
        public BasicBar(Color color, Style style) {
            super(UUID.randomUUID(), null, color, style);
        }
    }


    public BossbarIcon(UUID providerID, Flags flags) {
        super(flags, 182);
        this.providerID = providerID;
    }

    @Override
    public void render(DrawContext context, RenderPiece piece) {
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.translate(piece.x + shiftX, piece.y + shiftY + 1, 0);
        if (!referenceCorner)
            matrices.translate(0, -(5*scale-5)/2, 0);
        matrices.scale(scale, scale, 0);
        rotate(matrices, 182, 5);

        BossBar bossBar = (BossBar) piece.value;
        if (bossBar != null)
            CLIENT.inGameHud.getBossBarHud().renderBossBar(context, 0, 0, bossBar);

        matrices.pop();
    }

}
