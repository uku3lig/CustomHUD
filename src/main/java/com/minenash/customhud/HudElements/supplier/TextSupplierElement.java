package com.minenash.customhud.HudElements.supplier;

import com.minenash.customhud.HudElements.icon.TextElement;
import com.minenash.customhud.data.Flags;
import com.minenash.customhud.render.RenderPiece;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.function.Supplier;

import static com.minenash.customhud.CustomHud.CLIENT;

public class TextSupplierElement extends TextElement {

    public static final Supplier<Text> DISPLAY_NAME = () -> CLIENT.player.getDisplayName();
    public static final Supplier<Text> ACTIONBAR_MSG = () -> CLIENT.inGameHud.overlayRemaining == 0 ? null : CLIENT.inGameHud.overlayMessage;
    public static final Supplier<Text> TITLE_MSG = () -> CLIENT.inGameHud.title;
    public static final Supplier<Text> SUBTITLE_MSG = () -> CLIENT.inGameHud.titleRemainTicks == 0 ? null : CLIENT.inGameHud.subtitle;

    public static class ActionbarMsg extends TextSupplierElement {
        public ActionbarMsg(Flags flags) { super(ACTIONBAR_MSG, flags); }

        @Override
        public void render(DrawContext context, RenderPiece piece) {
            Text text = getText();
            if (text == null) return;

            float h = (float)CLIENT.inGameHud.overlayRemaining;
            int k = !CLIENT.inGameHud.overlayTinted ? (piece.color & 0xFFFFFF) : MathHelper.hsvToRgb(h / 50.0F, 0.7F, 0.6F) & 0xFFFFFF;
            int m = Math.min((int)(h * 255.0F / 20.0F), 255) << 24 & 0xFF000000;

            context.drawText(CLIENT.textRenderer, text, piece.x, piece.y, k | m, piece.shadow);
        }
    }

    public static class TitleMsg extends TextSupplierElement {
        public TitleMsg(Supplier<Text> supplier, Flags flags) { super(supplier, flags); }

        @Override
        public void render(DrawContext context, RenderPiece piece) {
            Text text = getText();
            if (text == null) return;

            int l = 0;
            if (CLIENT.inGameHud.titleRemainTicks > CLIENT.inGameHud.titleFadeOutTicks + CLIENT.inGameHud.titleStayTicks) {
                float o = (float)(CLIENT.inGameHud.titleFadeInTicks + CLIENT.inGameHud.titleStayTicks + CLIENT.inGameHud.titleFadeOutTicks) - CLIENT.inGameHud.titleRemainTicks;
                l = (int)(o * 255.0F / CLIENT.inGameHud.titleFadeInTicks);
            }

            if (CLIENT.inGameHud.titleRemainTicks <= CLIENT.inGameHud.titleFadeOutTicks)
                l = (int)(CLIENT.inGameHud.titleRemainTicks * 255.0F / CLIENT.inGameHud.titleFadeOutTicks);

            l = MathHelper.clamp(l, 0, 255) << 24 & 0xFF000000;
            context.drawText(CLIENT.textRenderer, text, piece.x, piece.y, (piece.color & 0xFFFFFF) | l, piece.shadow);
        }
    }




    private final Supplier<Text> supplier;

    public TextSupplierElement(Supplier<Text> supplier, Flags flags) {
        this.supplier = supplier;
    }

    public int getTextWidth() {
        return CLIENT.textRenderer.getWidth(getText());
    }

    public Text getText() {
        Text text = supplier.get();
        return text == null ? Text.empty() : text;
    }

    @Override
    public String getString() {
        return getText().getString();
    }

    @Override
    public Number getNumber() {
        return getText().getString().length();
    }

    @Override
    public boolean getBoolean() {
        return !getText().getString().isEmpty();
    }
}
