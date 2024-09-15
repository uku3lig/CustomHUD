package com.minenash.customhud.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.minenash.customhud.ProfileManager;
import com.minenash.customhud.data.Profile;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.minenash.customhud.CustomHud.CLIENT;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;render(Lnet/minecraft/client/gui/DrawContext;F)V"))
    public void changeHudGuiScale(InGameHud instance, DrawContext context, float tickDelta, Operation<Void> original) {
        Profile p = ProfileManager.getActive();
        if (p == null || p.baseTheme.hudScale == null) {
            original.call(instance, context, tickDelta);
            return;
        }

        double originalScale = CLIENT.getWindow().getScaleFactor();
        double target = p.baseTheme.getTargetGuiScale();
        float scale = (float) (target/originalScale);
        CLIENT.getWindow().setScaleFactor(target);

        context.getMatrices().push();
        context.getMatrices().scale(scale, scale, 1);
        original.call(instance, context, tickDelta);
        context.getMatrices().pop();

        CLIENT.getWindow().setScaleFactor(originalScale);

    }

}
