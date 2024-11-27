package com.minenash.customhud.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.minenash.customhud.ProfileManager;
import com.minenash.customhud.data.DebugCharts;
import com.minenash.customhud.data.Profile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.debug.PieChart;
import net.minecraft.util.profiler.ProfileResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PieChart.class)
public class PieChartMixin {
    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowWidth()I"))
    public int moveProfilerToLeft(int original) {
        Profile p = ProfileManager.getActive();
        return p != null && p.leftChart == DebugCharts.PROFILER ? 360 : original;
    }

    @ModifyExpressionValue(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/debug/PieChart;profileResult:Lnet/minecraft/util/profiler/ProfileResult;"))
    private ProfileResult shouldRenderTheActualProfiler(ProfileResult original) {
        Profile p = ProfileManager.getActive();
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.getDebugHud().shouldShowDebugHud() ||
                (!client.options.hudHidden && !client.getDebugHud().shouldShowDebugHud() && client.world != null
                        && p != null && (p.leftChart == DebugCharts.PROFILER || p.rightChart == DebugCharts.PROFILER)) ) {
            return original;
        }

        return null;
    }
}
