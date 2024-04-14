package com.minenash.customhud.HudElements.list;

import com.minenash.customhud.complex.ComplexData;
import com.minenash.customhud.complex.SubtitleTracker;
import com.terraformersmc.modmenu.ModMenu;
import com.terraformersmc.modmenu.util.mod.Mod;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.CommandBossBar;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.Nullables;
import net.minecraft.world.GameMode;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.minenash.customhud.CustomHud.CLIENT;
import static com.minenash.customhud.HudElements.list.AttributeHelpers.*;

@SuppressWarnings("DataFlowIssue")
public class ListSuppliers {

    public static final Comparator<PlayerListEntry> ENTRY_ORDERING =
            Comparator.comparingInt((PlayerListEntry entry) -> entry.getGameMode() == GameMode.SPECTATOR ? 1 : 0)
                    .thenComparing((entry) -> Nullables.mapOrElse(entry.getScoreboardTeam(), Team::getName, ""))
                    .thenComparing((entry) -> entry.getProfile().getName(), String::compareToIgnoreCase);

    public static final List<String> IGNORE_MODS = List.of("minecraft", "fabricloader", "java");
    public static final Comparator<Mod> MOD_ORDERING = Comparator.comparing(mod -> mod.getTranslatedName().toLowerCase(Locale.ROOT));
    public static final Predicate<Mod> MOD_PREDICATE = (mod) -> !(mod.isHidden() || mod.getBadges().contains(Mod.Badge.LIBRARY) || mod.getBadges().contains(Mod.Badge.MINECRAFT) );
    public static final Predicate<Mod> MOD_AND_LIB_PREDICATE = (mod) -> !(mod.isHidden() || IGNORE_MODS.contains(mod.getId()) );

    public static final ListProvider
        STATUS_EFFECTS = () -> CLIENT.player.getStatusEffects().stream().sorted(Comparator.comparingInt(e -> e.getEffectType().getCategory().ordinal())).toList(),
        STATUS_EFFECTS_POSITIVE = () -> CLIENT.player.getStatusEffects().stream().filter(e -> e.getEffectType().getCategory() == StatusEffectCategory.BENEFICIAL).toList(),
        STATUS_EFFECTS_NEGATIVE = () -> CLIENT.player.getStatusEffects().stream().filter(e -> e.getEffectType().getCategory() == StatusEffectCategory.HARMFUL).toList(),
        STATUS_EFFECTS_NEUTRAL = () -> CLIENT.player.getStatusEffects().stream().filter(e -> e.getEffectType().getCategory() == StatusEffectCategory.NEUTRAL).toList(),

        ONLINE_PLAYERS = () -> CLIENT.getNetworkHandler().getPlayerList().stream().sorted(ENTRY_ORDERING).toList(),
        SUBTITLES = () -> SubtitleTracker.INSTANCE.entries,

        TARGET_BLOCK_STATES = () ->  ComplexData.targetBlock == null ? Collections.EMPTY_LIST : Arrays.asList(ComplexData.targetBlock.getEntries().entrySet().toArray()),
        TARGET_BLOCK_TAGS = () -> ComplexData.targetBlock == null ? Collections.EMPTY_LIST : ComplexData.targetBlock.streamTags().toList(),
        PLAYER_ATTRIBUTES = () -> getEntityAttributes(CLIENT.player),
        TARGET_ENTITY_ATTRIBUTES = () -> ComplexData.targetEntity == null ? Collections.EMPTY_LIST : getEntityAttributes(ComplexData.targetEntity),
        HOOKED_ENTITY_ATTRIBUTES = () -> hooked() == null ? Collections.EMPTY_LIST : getEntityAttributes(hooked()),
        TEAMS = () -> Arrays.asList(CLIENT.world.getScoreboard().getTeams().toArray()),

        ITEMS = () -> AttributeHelpers.compactItems(CLIENT.player.getInventory().main),
        INV_ITEMS = () -> CLIENT.player.getInventory().main.subList(9, CLIENT.player.getInventory().main.size()),
        ARMOR_ITEMS = () -> CLIENT.player.getInventory().armor,
        HOTBAR_ITEMS = () -> CLIENT.player.getInventory().main.subList(0,9),

        SCOREBOARD_OBJECTIVES = () -> Arrays.asList(scoreboard().getObjectives().toArray()),
        PLAYER_SCOREBOARD_SCORES = () -> Arrays.asList(scoreboard().getScores(CLIENT.getGameProfile().getName()).scores.entrySet().toArray()),

        BOSSBARS = () -> bossbars(false),
        ALL_BOSSBARS = () -> bossbars(true),

        MODS = () -> ModMenu.ROOT_MODS.values().stream().filter(MOD_PREDICATE).sorted(MOD_ORDERING).toList(),
        MODS_AND_LIBS = () -> ModMenu.ROOT_MODS.values().stream().filter(MOD_AND_LIB_PREDICATE).sorted(MOD_ORDERING).toList(),
        ALL_MODS = () -> ModMenu.MODS.values().stream().sorted(MOD_ORDERING).toList()
    ;

    public static final Function<EntityAttributeInstance,List<?>> ATTRIBUTE_MODIFIERS = (attr) -> attr.getModifiers().stream().toList();
    public static final Function<Team,List<?>> TEAM_MEMBERS = (team) -> Arrays.asList(team.getPlayerList().toArray());
    public static final Function<Team,List<?>> TEAM_PLAYERS = (team) -> CLIENT.getNetworkHandler().getPlayerList().stream().filter(p -> p.getScoreboardTeam() == team).sorted(ENTRY_ORDERING).toList();

    public static final Function<ItemStack,List<?>> ITEM_ATTRIBUTES = AttributeHelpers::getItemStackAttributes;
    public static final Function<ItemStack,List<?>> ITEM_ENCHANTS = (stack) -> Arrays.asList(EnchantmentHelper.get(stack).entrySet().toArray());
    public static final Function<ItemStack,List<?>> ITEM_LORE_LINES = AttributeHelpers::getLore;
    public static final Function<ItemStack,List<?>> ITEM_CAN_DESTROY = (stack) -> getCanX(stack, "CanDestroy");
    public static final Function<ItemStack,List<?>> ITEM_CAN_PLAY_ON = (stack) -> getCanX(stack, "CanPlaceOn");
    public static final Function<ItemStack,List<?>> ITEM_HIDDEN = (stack) -> getHideFlagStrings(stack, false);
    public static final Function<ItemStack,List<?>> ITEM_SHOWN = (stack) -> getHideFlagStrings(stack, true);

    public static final Function<BossBar,List<?>> BOSSBAR_PLAYERS = (bar) -> {
        if (CLIENT.getServer() == null || !(bar instanceof CommandBossBar cboss)) return Collections.EMPTY_LIST;

        List<?> listPlayers = Arrays.asList(CLIENT.getNetworkHandler().getPlayerList().toArray());
        List<PlayerListEntry> out = new ArrayList<>(cboss.getPlayers().size());

        for (var player : cboss.getPlayers())
            for (var listPlayer : listPlayers)
                if (player.getUuid().equals( ((PlayerListEntry) listPlayer).getProfile().getId() ))
                    out.add( (PlayerListEntry) listPlayer );

        return out;
    };


    public static final Function<ScoreboardObjective,List<?>> SCOREBOARD_OBJECTIVE_SCORES = (obj) -> scoreboard().getScoreboardEntries(obj).stream().sorted(InGameHud.SCOREBOARD_ENTRY_COMPARATOR).toList();
    public static final Function<ScoreboardObjective,List<?>> SCOREBOARD_OBJECTIVE_SCORES_ONLINE = (obj) -> scoreboard().getScoreboardEntries(obj).stream()
            .filter(score -> scoreboardPlayer(score.owner())) //TODO: Make Work with entities
            .sorted(InGameHud.SCOREBOARD_ENTRY_COMPARATOR).toList();

    public static final Function<String,List<?>> SCORES = (name) -> Arrays.asList(scoreboard().getScores(name).scores.entrySet().toArray());


    public static final Function<Mod,List<?>> MOD_AUTHORS = Mod::getAuthors;
    public static final Function<Mod,List<?>> MOD_CONTRIBUTORS = Mod::getAuthors;
    public static final Function<Mod,List<?>> MOD_CREDITS = Mod::getCredits;
    public static final Function<Mod,List<?>> MOD_BADGES = (mod) -> Arrays.asList(mod.getBadges().toArray());
    public static final Function<Mod,List<?>> MOD_LICENSES = (mod) -> Arrays.asList(mod.getLicense().toArray());
    public static final Function<Mod,List<?>> MOD_PARENTS = (mod) -> {
        Mod parent = ModMenu.MODS.get(mod.getParent());
        return parent == null ? Collections.emptyList() : Collections.singletonList(parent);
    };
    public static final Function<Mod,List<?>> MOD_CHILDREN = ModMenu.PARENT_MAP::get;




    private static Entity hooked() {return CLIENT.player.fishHook == null ? null : CLIENT.player.fishHook.getHookedEntity();}


}
