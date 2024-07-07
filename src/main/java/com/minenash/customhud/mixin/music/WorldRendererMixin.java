package com.minenash.customhud.mixin.music;

import com.minenash.customhud.MusicAndRecordTracker;
import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Shadow @Final private Map<BlockPos, SoundInstance> playingSongs;

    @Inject(method = "playJukeboxSong", at = @At("TAIL"))
    private void getRecord(RegistryEntry<JukeboxSong> song, BlockPos jukeboxPos, CallbackInfo ci) {
        MusicAndRecordTracker.setRecord(song, playingSongs.get(jukeboxPos), jukeboxPos);
    }

}
