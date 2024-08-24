package com.minenash.customhud.HudElements.list;

import com.google.common.collect.Lists;
import com.minenash.customhud.complex.ComplexData;
import com.minenash.customhud.complex.MusicAndRecordTracker;
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
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.Identifier;
import net.minecraft.util.Nullables;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.profiler.ProfilerTiming;
import net.minecraft.village.TradeOffer;
import net.minecraft.world.GameMode;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.minenash.customhud.CustomHud.CLIENT;
import static com.minenash.customhud.HudElements.list.AttributeHelpers.*;

@SuppressWarnings("DataFlowIssue")
public class ListSuppliers {

    public static final Direction[] DIRS = new Direction[] {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.UP, Direction.DOWN};
    public static final Comparator<PlayerListEntry> ENTRY_ORDERING =
            Comparator.comparingInt((PlayerListEntry entry) -> entry.getGameMode() == GameMode.SPECTATOR ? 1 : 0)
                    .thenComparing((entry) -> Nullables.mapOrElse(entry.getScoreboardTeam(), Team::getName, ""))
                    .thenComparing((entry) -> entry.getProfile().getName(), String::compareToIgnoreCase);


    public static final List<String> IGNORE_MODS = List.of("minecraft", "fabricloader", "java");
    public static final Comparator<?> MOD_ORDERING = Comparator.comparing(mod -> ((Mod)mod).getTranslatedName().toLowerCase(Locale.ROOT));
    public static final Predicate<?> MOD_PREDICATE = (mod) -> !(((Mod)mod).isHidden() || ((Mod)mod).getBadges().contains(Mod.Badge.LIBRARY) || ((Mod)mod).getBadges().contains(Mod.Badge.MINECRAFT) );
    public static final Predicate<?> ALL_ROOT_MODS_PREDICATE = (mod) -> !(((Mod)mod).isHidden() || IGNORE_MODS.contains(((Mod)mod).getId()) );

    public static final ListProvider
        STATUS_EFFECTS = () -> CLIENT.player.getStatusEffects().stream().sorted(Comparator.comparingInt(e -> e.getEffectType().getCategory().ordinal())).toList(),
        STATUS_EFFECTS_POSITIVE = () -> CLIENT.player.getStatusEffects().stream().filter(e -> e.getEffectType().getCategory() == StatusEffectCategory.BENEFICIAL).toList(),
        STATUS_EFFECTS_NEGATIVE = () -> CLIENT.player.getStatusEffects().stream().filter(e -> e.getEffectType().getCategory() == StatusEffectCategory.HARMFUL).toList(),
        STATUS_EFFECTS_NEUTRAL = () -> CLIENT.player.getStatusEffects().stream().filter(e -> e.getEffectType().getCategory() == StatusEffectCategory.NEUTRAL).toList(),

        ONLINE_PLAYERS = () -> CLIENT.getNetworkHandler().getPlayerList().stream().sorted(ENTRY_ORDERING).toList(),
        SUBTITLES = () -> SubtitleTracker.INSTANCE.entries,

        TARGET_BLOCK_STATES = () ->  ComplexData.targetBlock == null ? Collections.EMPTY_LIST : Arrays.asList(ComplexData.targetBlock.getEntries().entrySet().toArray()),
        TARGET_BLOCK_TAGS = () -> ComplexData.targetBlock == null ? Collections.EMPTY_LIST : ComplexData.targetBlock.streamTags().toList(),
        TARGET_FLUID_STATES = () ->  ComplexData.targetFluid == null ? Collections.EMPTY_LIST : Arrays.asList(ComplexData.targetFluid.getEntries().entrySet().toArray()),
        TARGET_FLUID_TAGS = () -> ComplexData.targetFluid == null ? Collections.EMPTY_LIST : ComplexData.targetFluid.streamTags().toList(),

        TARGET_BLOCK_POWERS = () -> {
            if (ComplexData.targetBlockPos == null) return Collections.EMPTY_LIST;
            List<ReceivedPower> powers = new ArrayList<>(6);
            for (Direction d : DIRS) {
                BlockPos pos = ComplexData.targetBlockPos.offset(d);
                powers.add( new ReceivedPower(d, CLIENT.world.getEmittedRedstonePower(pos, d), CLIENT.world.getStrongRedstonePower(pos, d)) );
            }
            return powers;
        },
//        TARGET_BLOCK_ITEMS = () -> {
//            if (ComplexData.targetBlockPos == null) return Collections.EMPTY_LIST;
//            BlockEntity be = CLIENT.world.getBlockEntity(ComplexData.targetBlockPos);
//            if ( !(be instanceof Inventory inv) ) return Collections.EMPTY_LIST;
//            List<ItemStack> items = new ArrayList<>(inv.size());
//            for (int i = 0; i < inv.size(); i++)
//                items.add(inv.getStack(i));
//            return items;
//        },
//        TARGET_BLOCK_COMPACT_ITEMS = () -> {
//            if (ComplexData.targetBlockPos == null) return Collections.EMPTY_LIST;
//            BlockEntity be = CLIENT.world.getBlockEntity(ComplexData.targetBlockPos);
//            if ( !(be instanceof Inventory inv) ) return Collections.EMPTY_LIST;
//            List<ItemStack> items = new ArrayList<>(inv.size());
//            for (int i = 0; i < inv.size(); i++)
//                items.add(inv.getStack(i));
//            return AttributeHelpers.compactItems(items);
//        },

        PLAYER_ATTRIBUTES = () -> getEntityAttributes(CLIENT.player),
        TARGET_ENTITY_ATTRIBUTES = () -> ComplexData.targetEntity == null ? Collections.EMPTY_LIST : getEntityAttributes(ComplexData.targetEntity),
        HOOKED_ENTITY_ATTRIBUTES = () -> hooked() == null ? Collections.EMPTY_LIST : getEntityAttributes(hooked()),
        TEAMS = () -> Arrays.asList(CLIENT.world.getScoreboard().getTeams().toArray()),
        TARGET_VILLAGER_OFFERS = () -> ComplexData.villagerOffers,

        ITEMS = () -> AttributeHelpers.compactItems(CLIENT.player.getInventory().main),
        INV_ITEMS = () -> CLIENT.player.getInventory().main.subList(9, CLIENT.player.getInventory().main.size()),
        ARMOR_ITEMS = () -> {List<ItemStack> a = CLIENT.player.getInventory().armor; return List.of(a.get(3),a.get(2),a.get(1),a.get(0));},
        HOTBAR_ITEMS = () -> CLIENT.player.getInventory().main.subList(0,9),
        ALL_ITEMS = () -> {
            PlayerInventory inv = CLIENT.player.getInventory();
            List<ItemStack> items = new ArrayList<>( inv.main );
            items.add(inv.armor.get(3));
            items.add(inv.armor.get(2));
            items.add(inv.armor.get(1));
            items.add(inv.armor.get(0));
            items.add(inv.offHand.get(0));
            return items;
        },
        EQUIPPED_ITEMS = () -> {
            PlayerInventory inv = CLIENT.player.getInventory();
            List<ItemStack> items = new ArrayList<>( 5 );
            items.add(inv.armor.get(3));
            items.add(inv.armor.get(2));
            items.add(inv.armor.get(1));
            items.add(inv.armor.get(0));
            items.add(inv.getMainHandStack());
            items.add(inv.offHand.get(0));
            return items;
        },
        ITEMS_UNPACKED = () -> {
            List<ItemStack> items = new ArrayList<>(36);
            for (ItemStack stack : CLIENT.player.getInventory().main) {
                List<ItemStack> innerItems = getItemItems(stack, true);
                if (innerItems.isEmpty())
                    items.add(stack);
                else
                    items.addAll(innerItems);
            }
            return AttributeHelpers.compactItems( items );
        },

        SCOREBOARD_OBJECTIVES = () -> Arrays.asList(scoreboard().getObjectives().toArray()),
        PLAYER_SCOREBOARD_SCORES = () -> Arrays.asList(scoreboard().getScores(CLIENT.getGameProfile().getName()).scores.entrySet().toArray()),

        BOSSBARS = () -> bossbars(false),
        ALL_BOSSBARS = () -> bossbars(true),

        RECORDS = () -> MusicAndRecordTracker.records,

        MODS = () -> ModMenu.ROOT_MODS.values().stream().filter((Predicate<? super Mod>) MOD_PREDICATE).sorted((Comparator<? super Mod>)MOD_ORDERING).toList(),
        ALL_ROOT_MODS = () -> ModMenu.ROOT_MODS.values().stream().filter((Predicate<? super Mod>)ALL_ROOT_MODS_PREDICATE).sorted((Comparator<? super Mod>)MOD_ORDERING).toList(),
        ALL_MODS = () -> ModMenu.MODS.values().stream().sorted((Comparator<? super Mod>)MOD_ORDERING).toList(),

        RESOURCE_PACKS = () -> {
            List<ResourcePackProfile> packs = new ArrayList<>(CLIENT.getResourcePackManager().getEnabledProfiles());
            packs.removeIf(pack -> pack.getName().equals("fabric") || pack.getName().equals("vanilla"));
            Collections.reverse(packs);
            return packs;
        },
        DISABLED_RESOURCE_PACKS = () -> {
            List<ResourcePackProfile> profiles = Lists.newArrayList(CLIENT.getResourcePackManager().getProfiles());
            profiles.removeAll(CLIENT.getResourcePackManager().getEnabledProfiles());
            Collections.reverse(profiles);
            return profiles;
        },
        DATA_PACKS = () -> CLIENT.getServer() == null ? Collections.EMPTY_LIST : Arrays.asList(CLIENT.getServer().getDataPackManager().getEnabledProfiles().toArray()),
        DISABLED_DATA_PACKS = () -> {
            if (CLIENT.getServer() == null) return Collections.EMPTY_LIST;

            ResourcePackManager manager = CLIENT.getServer().getDataPackManager();
            List<ResourcePackProfile> profiles = Lists.newArrayList(manager.getProfiles());
            profiles.removeAll(manager.getEnabledProfiles());
            return profiles;
        },

        CHAT_MESSAGES = () -> CLIENT.inGameHud.getChatHud().messages,

    PROFILER_TIMINGS = () -> ComplexData.rootEntries
    ;

    public static final Function<ComplexData.ProfilerTimingWithPath,List<?>> TIMINGS_SUB_ENTRIES = (timing) -> timing == null ? Collections.EMPTY_LIST : timing.entries();

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
    public static final Function<ItemStack,List<?>> ITEM_TAGS = (stack) -> stack.streamTags().toList();
    public static final Function<ItemStack,List<?>> ITEM_ITEMS = (stack) -> getItemItems(stack, false);
    public static final Function<ItemStack,List<?>> ITEM_ITEMS_COMPACT = (stack) -> compactItems( getItemItems(stack, false) );

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


    public static ListProvider SCORES(String name) { return () -> Arrays.asList(scoreboard().getScores(name).scores.entrySet().toArray()); }

    public static <T> ListProvider TAG_ENTRIES(Registry<T> registry, String name) {
        if (name.startsWith("#"))
            name = name.substring(1);
        Identifier id = Identifier.tryParse(name);
        if (id == null)
            return null;
        return () -> {
            var opt = registry.getEntryList( TagKey.of(registry.getKey(), id) );
            if (opt.isEmpty())
                return Collections.EMPTY_LIST;

            var entryList = opt.get();
            List<T> values = new ArrayList<>(entryList.size());
            for (var e : entryList)
                values.add( e.value() );
            return values;
    }; }


    // Don't change to method references
    public static final Function<?,List<?>> MOD_AUTHORS = (mod) -> ((Mod)mod).getAuthors();
    public static final Function<?,List<?>> MOD_CONTRIBUTORS = (mod) -> ((Mod)mod).getContributors();
    public static final Function<?,List<?>> MOD_CREDITS = (mod) -> ((Mod)mod).getCredits();
    public static final Function<?,List<?>> MOD_BADGES = (mod) -> Arrays.asList(((Mod)mod).getBadges().toArray());
    public static final Function<?,List<?>> MOD_LICENSES = (mod) -> Arrays.asList(((Mod)mod).getLicense().toArray());
    public static final Function<?,List<?>> MOD_PARENTS = (mod) -> {
        Mod parent = ModMenu.MODS.get(((Mod)mod).getParent());
        return parent == null ? Collections.emptyList() : Collections.singletonList(parent);
    };
    public static final Function<?,List<?>> MOD_CHILDREN = (mod) -> ModMenu.PARENT_MAP.get(((Mod)mod));



    public static final Function<TradeOffer,List<?>> OFFER_FIRST_ADJUSTED = (offer) -> Collections.singletonList( offer.getAdjustedFirstBuyItem() );
    public static final Function<TradeOffer,List<?>> OFFER_FIRST_BASE = (offer) -> Collections.singletonList( offer.getOriginalFirstBuyItem() );
    public static final Function<TradeOffer,List<?>> OFFER_SECOND = (offer) -> Collections.singletonList( offer.getSecondBuyItem() );
    public static final Function<TradeOffer,List<?>> OFFER_RESULT = (offer) -> Collections.singletonList( offer.getSellItem() );




    private static Entity hooked() {return CLIENT.player.fishHook == null ? null : CLIENT.player.fishHook.getHookedEntity();}


}
