package com.minenash.customhud.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.minenash.customhud.ProfileManager;
import com.minenash.customhud.complex.ComplexData;
import com.minenash.customhud.CustomHud;
import com.minenash.customhud.data.DebugCharts;
import com.minenash.customhud.data.Profile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow @Final public GameOptions options;
    @Shadow private double gpuUtilizationPercentage;

    @Shadow @Final public InGameHud inGameHud;

    @Shadow @Nullable public ClientWorld world;

    @WrapOperation(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;wasPressed()Z"))
    public boolean readClick(KeyBinding instance, Operation<Boolean> original) {
        boolean p = original.call(instance);

        if (p && instance == options.attackKey)
            ComplexData.clicksSoFar[0]++;
        if (p && instance == options.useKey)
            ComplexData.clicksSoFar[1]++;

        return p;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(RunArgs args, CallbackInfo ci) {
        CustomHud.delayedInitialize();
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Ljava/lang/String;format(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;"))
    public void getGpuUsage(boolean tick, CallbackInfo ci) {
        ComplexData.gpuUsage = gpuUtilizationPercentage > 100 ? 100 : gpuUtilizationPercentage;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/DebugHud;shouldShowDebugHud()Z"))
    public boolean getGpuUsageAndOtherPerformanceMetrics(DebugHud hud) {
        return hud.shouldShowDebugHud() || (ProfileManager.getActive() != null && ProfileManager.getActive().enabled.gpuMetrics);
    }


    @Unique private static boolean isFirst = true;
    @Inject(method = "onFinishedLoading", at = @At("RETURN"))
    public void reloadProfiles(MinecraftClient.LoadingContext loadingContext, CallbackInfo ci) {
        if (isFirst) {
            isFirst = false;
            return;
        }

        CustomHud.resourceTriggeredReload();
    }

    @WrapOperation(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/DebugHud;shouldShowRenderingChart()Z"))
    public boolean activateProfiler(DebugHud instance, Operation<Boolean> original) {
        if (ComplexData.refreshTimings) {
            ComplexData.refreshTimings = false;
            return false;
        }

        Profile p = ProfileManager.getActive();
        return original.call(instance) ||
                (!options.hudHidden && !inGameHud.getDebugHud().shouldShowDebugHud() && world != null
                        && p != null && (p.enabled.profilerTimings || p.leftChart == DebugCharts.PROFILER || p.rightChart == DebugCharts.PROFILER) );
    }
}
