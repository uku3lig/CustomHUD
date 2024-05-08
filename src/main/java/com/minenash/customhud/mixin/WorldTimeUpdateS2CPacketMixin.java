package com.minenash.customhud.mixin;

import com.minenash.customhud.complex.EstimatedTick;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldTimeUpdateS2CPacket.class)
public class WorldTimeUpdateS2CPacketMixin {

    @Inject(method = "apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V", at = @At("HEAD"))
    public void recordTick(ClientPlayPacketListener clientPlayPacketListener, CallbackInfo ci) {
        if (RenderSystem.isOnRenderThread())
            EstimatedTick.record();
    }

}
