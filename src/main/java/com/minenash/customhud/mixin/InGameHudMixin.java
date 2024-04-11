package com.minenash.customhud.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.minenash.customhud.CustomHud;
import com.minenash.customhud.ProfileManager;
import com.minenash.customhud.data.Crosshairs;
import com.minenash.customhud.data.DisableElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = InGameHud.class, priority = 900)
public abstract class InGameHudMixin {

    @Shadow protected abstract void renderCrosshair(DrawContext context);

    @Shadow @Final private MinecraftClient client;
    @Unique boolean renderAttackIndicator = false;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderCrosshair(Lnet/minecraft/client/gui/DrawContext;)V", shift = At.Shift.AFTER))
    private void renderAttackIndicatorForDebugScreen2(DrawContext context, float _tickDelta, CallbackInfo _info) {
        if (getCrosshair() == Crosshairs.DEBUG && MinecraftClient.getInstance().options.getAttackIndicator().getValue() == AttackIndicator.CROSSHAIR) {
            renderAttackIndicator = true;
            renderCrosshair(context);
            renderAttackIndicator = false;
        }
    }

    @ModifyExpressionValue(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/DebugHud;shouldShowDebugHud()Z"))
    private boolean getDebugCrosshairEnable(boolean original) {
        return client.getDebugHud().shouldShowDebugHud() ? original : !renderAttackIndicator && getCrosshair() == Crosshairs.DEBUG;
    }

    @WrapWithCondition(method = "renderCrosshair", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"))
    private boolean skipNormalCrosshairRendering(DrawContext instance, Identifier texture, int x, int y, int width, int height) {
        return !renderAttackIndicator && getCrosshair() != Crosshairs.NONE;
    }
//    @WrapWithCondition(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIIIIIII)V"))
//    private boolean skipNormalCrosshairRendering2(DrawContext instance, Identifier texture, int i, int j, int k, int l, int x, int y, int width, int height) {
//        return !renderAttackIndicator && getCrosshair() != Crosshairs.NONE;
//    }

    @Unique
    private static Crosshairs getCrosshair() {
        return ProfileManager.getActive() == null ? Crosshairs.NORMAL : ProfileManager.getActive().crosshair;
    }

    @Redirect(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/InGameHud;overlayRemaining:I"))
    private int disableActionBar(InGameHud hud) {
        return CustomHud.isNotDisabled(DisableElement.ACTIONBAR) ? hud.overlayRemaining : 0;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(III)I"))
    private int disableTitles(int value, int min, int max) {
        return CustomHud.isNotDisabled(DisableElement.TITLES) ? MathHelper.clamp(value, min, max) : 0;
    }

}
