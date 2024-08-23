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
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.profiler.ProfileResult;
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

    @Shadow @Final private Window window;

    @Shadow @Nullable public Screen currentScreen;

    @Shadow @Final public InGameHud inGameHud;

    @Shadow @Nullable public ClientWorld world;

    @Shadow public abstract DebugHud getDebugHud();

    @Redirect(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;wasPressed()Z"))
    public boolean readClick(KeyBinding instance) {
        boolean p = instance.wasPressed();

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

    //@Redirect(method = "onResolutionChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;setScaleFactor(D)V"))
    //public void modifyGuiScale(Window instance, double scaleFactor) {
    //    Profile p = ProfileManager.getActive();
    //    if (p != null && p.baseTheme.guiScale != null)
    //        window.setScaleFactor(p.baseTheme.getTargetGuiScale());
    //    else
    //        window.setScaleFactor(scaleFactor);
    //}


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
    public boolean shouldShowProfiler(DebugHud instance, Operation<Boolean> original) {
        Profile p = ProfileManager.getActive();
        return original.call(instance) ||
                (!options.hudHidden && !inGameHud.getDebugHud().shouldShowDebugHud() && world != null
                        && p != null && (p.enabled.profilerTimings || p.leftChart == DebugCharts.PROFILER || p.rightChart == DebugCharts.PROFILER) );
    }

    @WrapOperation(method = "drawProfilerResults", at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/client/util/Window;getFramebufferWidth()I"))
    public int moveProfilerToLeft(Window instance, Operation<Integer> original) {
        Profile p = ProfileManager.getActive();
        return p != null && p.leftChart == DebugCharts.PROFILER ? 360 : original.call(instance);
    }

    @WrapOperation(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;tickProfilerResult:Lnet/minecraft/util/profiler/ProfileResult;"))
    private ProfileResult yourHandlerMethod(MinecraftClient instance, Operation<ProfileResult> original) {
        Profile p = ProfileManager.getActive();
        if (getDebugHud().shouldShowDebugHud() ||
                (!options.hudHidden && !inGameHud.getDebugHud().shouldShowDebugHud() && world != null
                && p != null && (p.leftChart == DebugCharts.PROFILER || p.rightChart == DebugCharts.PROFILER)) ) {
            return original.call(instance);
        }
        return null;
    }
}
