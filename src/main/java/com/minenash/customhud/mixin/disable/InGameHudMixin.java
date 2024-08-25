package com.minenash.customhud.mixin.disable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.minenash.customhud.CustomHud;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.SubtitlesHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.JumpingMount;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.minenash.customhud.data.DisableElement.*;

@Mixin(value = InGameHud.class, priority = 10000)
public abstract class InGameHudMixin {

    @Shadow protected abstract void renderStatusEffectOverlay(DrawContext context);
    @Shadow protected abstract void renderScoreboardSidebar(DrawContext context, ScoreboardObjective objective);

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbar(FLnet/minecraft/client/gui/DrawContext;)V"))
    public void customhud$disableHotbar(InGameHud instance, float tickDelta, DrawContext context, Operation<Void> original) {
        if (CustomHud.isNotDisabled(HOTBAR))
            original.call(instance, tickDelta, context);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/BossBarHud;render(Lnet/minecraft/client/gui/DrawContext;)V"))
    public void customhud$disableBossBar(BossBarHud instance, DrawContext context, Operation<Void> original) {
        if (CustomHud.isNotDisabled(BOSSBARS))
            original.call(instance, context);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;hasStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z"))
    public boolean customhud$disableStatusBars(ClientPlayerEntity instance, StatusEffect statusEffect, Operation<Boolean> original) {
        return CustomHud.isNotDisabled(STATUS_BARS) && original.call(instance, statusEffect);
    }

    @WrapOperation(method = "renderStatusBars", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"))
    public void customhud$disableArmor1(DrawContext instance, Identifier texture, int x, int y, int width, int height, Operation<Void> original) {
        if (CustomHud.isNotDisabled(ARMOR))
            original.call(instance, texture, x, y, width, height);
    }

    @WrapOperation(method = "renderStatusBars", at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"))
    public void customhud$disableArmor2(DrawContext instance, Identifier texture, int x, int y, int width, int height, Operation<Void> original) {
        if (CustomHud.isNotDisabled(ARMOR))
            original.call(instance, texture, x, y, width, height);
    }

    @WrapOperation(method = "renderStatusBars", at = @At(value = "INVOKE", ordinal = 2, target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"))
    public void customhud$disableArmor3(DrawContext instance, Identifier texture, int x, int y, int width, int height, Operation<Void> original) {
        if (CustomHud.isNotDisabled(ARMOR))
            original.call(instance, texture, x, y, width, height);
    }

    @WrapOperation(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHealthBar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/entity/player/PlayerEntity;IIIIFIIIZ)V"))
    public void customhud$disableHealthBar(InGameHud instance, DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, Operation<Void> original) {
        if (CustomHud.isNotDisabled(HEALTH))
            original.call(instance, context, player, x, y, lines, regeneratingHeartIndex, maxHealth, lastHealth, health, absorption, blinking);
    }

    @WrapOperation(method = "renderStatusBars", at = @At(value = "INVOKE", ordinal = 3, target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"))
    public void customhud$disableHunger1(DrawContext instance, Identifier texture, int x, int y, int width, int height, Operation<Void> original) {
        if (CustomHud.isNotDisabled(HUNGER))
            original.call(instance, texture, x, y, width, height);
    }

    @WrapOperation(method = "renderStatusBars", at = @At(value = "INVOKE", ordinal = 4, target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"))
    public void customhud$disableHunger2(DrawContext instance, Identifier texture, int x, int y, int width, int height, Operation<Void> original) {
        if (CustomHud.isNotDisabled(HUNGER))
            original.call(instance, texture, x, y, width, height);
    }

    @WrapOperation(method = "renderStatusBars", at = @At(value = "INVOKE", ordinal = 5, target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"))
    public void customhud$disableHunger3(DrawContext instance, Identifier texture, int x, int y, int width, int height, Operation<Void> original) {
        if (CustomHud.isNotDisabled(HUNGER))
            original.call(instance, texture, x, y, width, height);
    }

    @WrapOperation(method = "renderStatusBars", at = @At(value = "INVOKE", ordinal = 6, target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"))
    public void customhud$disableAir1(DrawContext instance, Identifier texture, int x, int y, int width, int height, Operation<Void> original) {
        if (CustomHud.isNotDisabled(AIR))
            original.call(instance, texture, x, y, width, height);
    }

    @WrapOperation(method = "renderStatusBars", at = @At(value = "INVOKE", ordinal = 7, target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"))
    public void customhud$disableAir2(DrawContext instance, Identifier texture, int x, int y, int width, int height, Operation<Void> original) {
        if (CustomHud.isNotDisabled(AIR))
            original.call(instance, texture, x, y, width, height);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderMountHealth(Lnet/minecraft/client/gui/DrawContext;)V"))
    public void customhud$disableHorseHealth(InGameHud instance, DrawContext context, Operation<Void> original) {
        if (CustomHud.isNotDisabled(HORSE) && CustomHud.isNotDisabled(HORSE_HEALTH) && CustomHud.isNotDisabled(STATUS_BARS))
            original.call(instance, context);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderMountJumpBar(Lnet/minecraft/entity/JumpingMount;Lnet/minecraft/client/gui/DrawContext;I)V"))
    public void customhud$disableHorseJump(InGameHud instance, JumpingMount mount, DrawContext context, int x, Operation<Void> original) {
        if (CustomHud.isNotDisabled(HORSE) && CustomHud.isNotDisabled(HORSE_JUMP))
            original.call(instance, mount, context, x);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;hasExperienceBar()Z"))
    public boolean customhud$disableXPBar(ClientPlayerInteractionManager instance, Operation<Boolean> original) {
        return CustomHud.isNotDisabled(XP) && original.call(instance);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHeldItemTooltip(Lnet/minecraft/client/gui/DrawContext;)V"))
    public void customhud$disableHotbar(InGameHud instance, DrawContext context, Operation<Void> original) {
        if (CustomHud.isNotDisabled(ITEM_TOOLTIP))
            original.call(instance, context);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusEffectOverlay(Lnet/minecraft/client/gui/DrawContext;)V"))
    public void customhud$disableStatusEffects(InGameHud instance, DrawContext context, Operation<Void> original) {
        if (CustomHud.isNotDisabled(STATUS_EFFECTS))
            renderStatusEffectOverlay(context);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/SubtitlesHud;render(Lnet/minecraft/client/gui/DrawContext;)V"))
    public void customhud$disableSubtitles(SubtitlesHud instance, DrawContext context, Operation<Void> original) {
        if (CustomHud.isNotDisabled(SUBTITLES))
            original.call(instance, context);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V"))
    public void customhud$disableScoreboard(InGameHud instance, DrawContext context, ScoreboardObjective objective, Operation<Void> original) {
        if (CustomHud.isNotDisabled(SCOREBOARD))
            original.call(instance, context, objective);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;render(Lnet/minecraft/client/gui/DrawContext;III)V"))
    public void customhud$disableChat(ChatHud instance, DrawContext context, int currentTick, int mouseX, int mouseY, Operation<Void> original) {
        if (CustomHud.isNotDisabled(CHAT))
            original.call(instance, context, currentTick, mouseX, mouseY);
    }

}
