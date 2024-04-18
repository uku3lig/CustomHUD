package com.minenash.customhud.mixin;

import com.minenash.customhud.ProfileManager;
import com.minenash.customhud.complex.ComplexData;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.network.packet.s2c.play.SetTradeOffersS2CPacket;
import net.minecraft.screen.ScreenHandlerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.minenash.customhud.CustomHud.CLIENT;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/DebugHud;shouldShowPacketSizeAndPingCharts()Z"))
    private boolean pingForMetricVariables(DebugHud hud) {
        return hud.shouldShowPacketSizeAndPingCharts() || (ProfileManager.getActive() != null && ProfileManager.getActive().enabled.pingMetrics);
    }

    @Inject(method = "onSetTradeOffers", at = @At("HEAD"))
    public void getTradeOffer(SetTradeOffersS2CPacket packet, CallbackInfo ci) {
        if (ComplexData.fakeVillagerInteract > 0) {
            ComplexData.fakeVillagerInteract--;
            ComplexData.villagerOffers = packet.getOffers();
            ComplexData.villagerXP = packet.getExperience();
        }
    }

    @Inject(at = @At("HEAD"), method = "onOpenScreen", cancellable = true)
    public void onOpenScreen(OpenScreenS2CPacket packet, CallbackInfo ci) {
        if (packet.getScreenHandlerType() == ScreenHandlerType.MERCHANT && ComplexData.fakeVillagerInteract > 0) {
            CLIENT.getNetworkHandler().sendPacket(new CloseHandledScreenC2SPacket(packet.getSyncId()));
            ComplexData.fakeVillagerInteract--;
            ci.cancel();
        }
    }

}
