package com.minenash.customhud.HudElements.supplier;

import com.minenash.customhud.ProfileManager;
import com.minenash.customhud.complex.ComplexData;
import com.minenash.customhud.HudElements.interfaces.HudElement;
import com.minenash.customhud.complex.MusicAndRecordTracker;
import com.minenash.customhud.mixin.accessors.PlayerListHudAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.world.GameMode;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;

import java.time.LocalTime;
import java.util.function.Supplier;

public class BooleanSupplierElement implements HudElement {

    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static boolean isInDim(Identifier id) { return client.world.getRegistryKey().getValue().equals(id); }
    protected static BlockPos blockPos() { return client.getCameraEntity().getBlockPos(); }


//    public static final Supplier<Boolean> NEW_RENDERER = () -> CustomHud.useNewRenderer;


    public static final Supplier<Boolean> PROFILE_IN_CYCLE = () -> ProfileManager.getActive() != null && ProfileManager.getActive().cycle;

    public static final Supplier<Boolean> VSYNC = () -> client.options.getEnableVsync().getValue();

    public static final Supplier<Boolean> SINGLEPLAYER = client::isInSingleplayer;
    public static final Supplier<Boolean> MULTIPLAYER = () -> !client.isInSingleplayer();

    public static final Supplier<Boolean> SURVIVAL = () -> client.interactionManager.getCurrentGameMode() == GameMode.SURVIVAL;
    public static final Supplier<Boolean> CREATIVE = () -> client.interactionManager.getCurrentGameMode() == GameMode.CREATIVE;
    public static final Supplier<Boolean> ADVENTURE = () -> client.interactionManager.getCurrentGameMode() == GameMode.ADVENTURE;
    public static final Supplier<Boolean> SPECTATOR = () -> client.interactionManager.getCurrentGameMode() == GameMode.SPECTATOR;

    public static final Supplier<Boolean> CHUNK_CULLING = () -> client.chunkCullingEnabled;
    public static final Supplier<Boolean> IN_OVERWORLD = () -> isInDim(World.OVERWORLD.getValue());
    public static final Supplier<Boolean> IN_NETHER = () -> isInDim(World.NETHER.getValue());
    public static final Supplier<Boolean> IN_END = () -> isInDim(World.END.getValue());

    public static final Supplier<Boolean> IS_RAINING = () -> ComplexData.world.isRaining();
    public static final Supplier<Boolean> IS_THUNDERING = () -> ComplexData.world.isThundering();
    public static final Supplier<Boolean> IS_SNOWING = () -> ComplexData.world.isRaining() && ComplexData.world.getBiome(client.player.getBlockPos()).value().getPrecipitation(client.player.getBlockPos(), ComplexData.world.getSeaLevel()) == Biome.Precipitation.SNOW;
    public static final Supplier<Boolean> IS_SLIME_CHUNK = () -> ChunkRandom.getSlimeRandom(blockPos().getX() >> 4, blockPos().getZ() >> 4, ((StructureWorldAccess)ComplexData.world).getSeed(), 987234911L).nextInt(10) == 0;

    public static final Supplier<Boolean> SPRINTING = () -> client.player.isSprinting() && !client.player.isSwimming();
    public static final Supplier<Boolean> SNEAKING = () -> client.player.isSneaking();
    public static final Supplier<Boolean> SWIMMING = () -> client.player.isSwimming();
    public static final Supplier<Boolean> FLYING = () -> client.player.getAbilities().flying;
    public static final Supplier<Boolean> FALLING_WITH_STYLE = () -> client.player.isGliding();
    public static final Supplier<Boolean> ON_GROUND = () -> client.player.isOnGround();
    public static final Supplier<Boolean> SPRINT_HELD = () -> client.options.sprintKey.isPressed();

    public static final Supplier<Boolean> HUD_HIDDEN = () -> client.options.hudHidden;
    public static final Supplier<Boolean> SCREEN_OPEN = () -> client.currentScreen != null;
    public static final Supplier<Boolean> CHAT_OPEN = () -> client.currentScreen instanceof ChatScreen;
    public static final Supplier<Boolean> PLAYER_LIST_OPEN = () -> ((PlayerListHudAccess)client.inGameHud.getPlayerListHud()).getVisible();

    public static final Supplier<Boolean> WINDOW_FOCUSED = client::isWindowFocused;

    public static final Supplier<Boolean> RECORD_PLAYING = () -> MusicAndRecordTracker.isRecordPlaying;
    public static final Supplier<Boolean> MUSIC_PLAYING = () -> MusicAndRecordTracker.isMusicPlaying;

    public static final Supplier<Boolean> FISHING_IS_CAST = () -> client.player.fishHook != null;
    public static final Supplier<Boolean> FISHING_IS_HOOKED = () -> client.player.fishHook != null && client.player.fishHook.getHookedEntity() != null;
    public static final Supplier<Boolean> FISHING_HAS_CAUGHT = () -> client.player.fishHook != null && client.player.fishHook.getDataTracker().get(FishingBobberEntity.CAUGHT_FISH);
    public static final Supplier<Boolean> FISHING_IN_OPEN_WATER = () -> client.player.fishHook != null && client.player.fishHook.isOpenOrWaterAround(client.player.fishHook.getBlockPos());

    public static final Supplier<Boolean> HAS_NOISE = () -> ComplexData.serverWorld.getChunkManager().getChunkGenerator() instanceof NoiseChunkGenerator;
    public static final Supplier<Boolean> IS_TICK_SPRINTING = () -> client.getServer() != null ? client.getServer().getTickManager().isSprinting() : null;
    public static final Supplier<Boolean> IS_TICK_FROZEN = () -> client.getServer() != null ? client.getServer().getTickManager().isFrozen() : client.world.getTickManager().isFrozen();
    public static final Supplier<Boolean> IS_TICK_STEPPING = () -> client.getServer() != null ? client.getServer().getTickManager().isStepping() : client.world.getTickManager().isStepping();

    public static final Supplier<Boolean> REAL_AM = () -> LocalTime.now().getHour() < 12;
    public static final Supplier<Boolean> REAL_PM = () -> LocalTime.now().getHour() >= 12;


    @Deprecated public static final Supplier<Boolean> ITEM_HAS_DURABILITY = () -> client.player.getMainHandStack().getMaxDamage() > 0;
    @Deprecated public static final Supplier<Boolean> OFFHAND_ITEM_HAS_DURABILITY = () -> client.player.getOffHandStack().getMaxDamage() > 0;

    private final Supplier<Boolean> supplier;

    public BooleanSupplierElement(Supplier<Boolean> supplier) {
        this.supplier = supplier;
    }

    @Override
    public String getString() {
        return sanitize(supplier, false) ? "true" : "false";
    }

    @Override
    public Number getNumber() {
        return sanitize(supplier, false) ? 1 : 0;
    }

    @Override
    public boolean getBoolean() {
        return sanitize(supplier, false);
    }
}
