package com.minenash.customhud.HudElements.text;

import com.minenash.customhud.complex.MusicAndRecordTracker;
import com.minenash.customhud.data.Flags;
import net.minecraft.text.Text;

import java.util.function.Supplier;

import static com.minenash.customhud.CustomHud.CLIENT;

public class TextSupplierElement extends TextElement {

    public static final Supplier<Text> DISPLAY_NAME = () -> CLIENT.player.getDisplayName();
    public static final Supplier<Text> ACTIONBAR_MSG = () -> CLIENT.inGameHud.overlayRemaining == 0 ? null : CLIENT.inGameHud.overlayMessage;
    public static final Supplier<Text> TITLE_MSG = () -> CLIENT.inGameHud.title;
    public static final Supplier<Text> SUBTITLE_MSG = () -> CLIENT.inGameHud.titleRemainTicks == 0 ? null : CLIENT.inGameHud.subtitle;
    public static final Supplier<Text> PLAYER_TEAM_NAME = () -> CLIENT.player.getScoreboardTeam().getDisplayName();
    public static final Supplier<Text> RECORD_NAME = () -> MusicAndRecordTracker.isRecordPlaying ? MusicAndRecordTracker.getClosestRecord().name : null;



    private final Supplier<Text> supplier;

    public TextSupplierElement(Supplier<Text> supplier, Flags flags) {
        this.supplier = supplier;
    }

    public int getTextWidth() {
        return CLIENT.textRenderer.getWidth(getText());
    }

    public Text getText() {
        try {
            Text text = supplier.get();
            return text == null ? Text.literal("-") : text;
        }
        catch (Exception e) {
            return Text.empty();
        }
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
