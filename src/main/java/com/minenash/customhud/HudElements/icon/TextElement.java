package com.minenash.customhud.HudElements.icon;

import com.minenash.customhud.HudElements.functional.FunctionalElement;
import com.minenash.customhud.render.RenderPiece;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import static com.minenash.customhud.CustomHud.CLIENT;

public abstract class TextElement extends FunctionalElement {

    public abstract int getTextWidth();
    public abstract Text getText();

    public void render(DrawContext context, RenderPiece piece) {
        context.drawText(CLIENT.textRenderer, getText(), piece.x, piece.y, piece.color, piece.shadow);
    }
}
