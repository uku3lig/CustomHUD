package com.minenash.customhud.mixin;

import com.minenash.customhud.ducks.SubtitleEntryDuck;
import net.minecraft.client.gui.hud.SubtitlesHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SubtitlesHud.SubtitleEntry.class)
public class SubtitleEntryMixin implements SubtitleEntryDuck {

    @Unique public String soundId;

    @Override
    public String customhud$getSoundID() {
        return soundId;
    }

    @Override
    public void customhud$setSoundID(String id) {
        soundId = id;
    }
}
