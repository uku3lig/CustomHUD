package com.minenash.customhud.HudElements.list;

import com.minenash.customhud.CustomHud;
import com.minenash.customhud.HudElements.functional.FunctionalElement;
import com.minenash.customhud.HudElements.interfaces.HudElement;
import com.minenash.customhud.HudElements.FuncElements.*;
import com.minenash.customhud.HudElements.functional.FunctionalElement.CreateListElement;
import com.minenash.customhud.HudElements.icon.*;
import com.minenash.customhud.HudElements.supplier.NumberSupplierElement;
import com.minenash.customhud.HudElements.supplier.StringSupplierElement;
import com.minenash.customhud.VariableParser;
import com.minenash.customhud.complex.ListManager;
import com.minenash.customhud.data.Flags;
import com.minenash.customhud.data.Profile;
import com.minenash.customhud.errors.ErrorType;
import com.minenash.customhud.registry.ParseContext;
import com.minenash.customhud.render.RenderPiece;
import com.terraformersmc.modmenu.ModMenu;
import com.terraformersmc.modmenu.util.mod.Mod;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.minenash.customhud.HudElements.list.AttributeFunctions.*;
import static com.minenash.customhud.HudElements.list.AttributeFunctions.ITEM_ATTR_MODIFIER_NAME;
import static com.minenash.customhud.HudElements.list.ListSuppliers.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class Attributers {

    @FunctionalInterface
    public interface Attributer {
        HudElement get(String prefixForSubList, UUID pid, Supplier supplier, String attr, Flags flags, ParseContext context);
    }

    public static final Attributer EFFECT = (sbp, pid, sup, name, flags, context) -> switch (name) {
        case "name" -> new Str(sup,STATUS_NAME);
        case "", "id" -> new Id(sup,STATUS_ID,flags);
        case "duration", "dur" -> new Num(sup, STATUS_DURATION, flags);
        case "infinite", "inf" -> new Bool(sup, STATUS_INFINITE);
        case "amplification", "amp" -> new Num(sup, STATUS_AMPLIFICATION, flags);
        case "level", "lvl" -> new Num(sup, STATUS_LEVEL, flags);
        case "ambient" -> new Bool(sup,STATUS_AMBIENT);
        case "show_particles", "particles" -> new Bool(sup,STATUS_SHOW_PARTICLES);
        case "show_icon" -> new Bool(sup,STATUS_SHOW_ICON);
        case "color" -> new Num(sup, STATUS_COLOR, flags);
        case "category", "cat" -> new Special(sup,STATUS_CATEGORY);
        case "icon" -> new StatusEffectIconElement(pid, flags, true); //TODO: LIST ONLY
        case "icon_no_bg" -> new StatusEffectIconElement(pid, flags, false); //TODO: LIST ONLY
        default -> null;
    };

    private static Attributer TEAM2;
    public static final Attributer PLAYER = (sbp, pid, sup, name, flags, context) -> {
        if (name.startsWith("p_team:")) {
            String attr = name.substring(7);
            Supplier sup2 = () -> ((PlayerListEntry) sup.get()).getScoreboardTeam();
            return TEAM2.get(sbp, pid, sup2, attr, flags, context);
        }

        return switch (name) {
            case "name", "display_name" -> new Tex(sup,PLAYER_ENTRY_DISPLAY_NAME);
            case "", "username" -> new Str(sup,PLAYER_ENTRY_NAME);
            case "id", "uuid" -> new Str(sup,PLAYER_ENTRY_UUID);
            case "team" -> new Str(sup, PLAYER_ENTRY_TEAM);
            case "latency" -> new Num(sup,PLAYER_ENTRY_LATENCY, flags);
            case "list_score" -> new Num(sup,PLAYER_ENTRY_LIST_SCORE, flags);
            case "gamemode" -> new Special(sup,PLAYER_ENTRY_GAMEMODE);
            case "survival" -> new Bool(sup,PLAYER_ENTRY_SURVIVAL);
            case "creative" -> new Bool(sup,PLAYER_ENTRY_CREATIVE);
            case "adventure" -> new Bool(sup,PLAYER_ENTRY_ADVENTURE);
            case "spectator" -> new Bool(sup,PLAYER_ENTRY_SPECTATOR);
            case "head" -> new PlayerHeadIconElement(pid, flags); //TODO FIX
        default -> null;
    };};

    public static final Attributer SUBTITLE = (sbp, pid, sup, name, flags, context) -> switch (name) {
        case "", "id" -> new Id(sup, SUBTITLE_ID,flags);
        case "name" -> new Str(sup, SUBTITLE_NAME);

        case "age" -> new Num(sup, SUBTITLE_AGE, flags);
        case "time" -> new Num(sup, SUBTITLE_TIME, flags);
        case "alpha" -> new Num(sup, SUBTITLE_ALPHA, flags);
        case "x" -> new Num(sup, SUBTITLE_X, flags);
        case "y" -> new Num(sup, SUBTITLE_Y, flags);
        case "z" -> new Num(sup, SUBTITLE_Z, flags);
        case "dist", "distance" -> new Num(sup, SUBTITLE_DISTANCE, flags);

        case "dir", "direction" -> new Str(sup, SUBTITLE_DIRECTION);
        case "left" -> new Bool(sup, SUBTITLE_LEFT);
        case "right" -> new Bool(sup, SUBTITLE_RIGHT);
        case "dir_yaw", "direction_yaw" -> new Num(sup, SUBTITLE_DIRECTION_YAW, flags);
        case "dir_pitch", "direction_pitch" -> new Num(sup, SUBTITLE_DIRECTION_PITCH, flags);
        default -> null;
    };

    public static final Attributer BLOCK_PROPERTY = (sbp, pid, sup, name, flags, context) -> switch (name) {
        case "", "name" -> new Str(sup, BLOCK_STATE_NAME);
        case "type" -> new Special(sup, BLOCK_STATE_TYPE);
        case "full_type" -> new Str(sup,BLOCK_STATE_FULL_TYPE);
        case "value" -> new Str(sup, BLOCK_STATE_VALUE);
        default -> null;
    };

    public static final Attributer RECEIVED_POWER = (sbp, pid, sup, name, flags, context) -> switch (name) {
        case "direction", "dir" -> new Str(sup, REC_DIRECTION);
        case "opposite_direction", "odir" -> new Str(sup, REC_DIRECTION);
        case "", "power" -> new Num(sup, REC_POWER, flags);
        case "strong_power", "strong" -> new Num(sup, REC_STRONG_POWER, flags);
        default -> null;
    };

    public static final Attributer TAG = (sbp, pid, sup, name, flags, context) -> switch (name) {
        case "name" -> new Str(sup, TAG_NAME);
        case "", "id" -> new Id(sup, TAG_ID,flags);
        default -> null;
    };

    public static final Attributer ENCHANTMENT = (sbp, pid, sup, name, flags, context) -> switch (name) {
        case "name" -> new Str(sup,ENCHANT_NAME);
        case "", "id" -> new Id(sup,ENCHANT_ID, flags);
        case "full" -> new Str(sup,ENCHANT_FULL);
        case "level","lvl" -> new Special(sup,ENCHANT_LEVEL);
        case "max_level" -> new Special(sup,ENCHANT_MAX_LEVEL);
        case "num", "number" -> new Num(sup,ENCHANT_NUM, flags);
        case "max_num", "max_number" -> new Num(sup,ENCHANT_MAX_NUM, flags);
        case "rarity" -> new Str(sup,ENCHANT_RARITY);
        default -> null;
    };

    public static final Attributer ITEM_LORE_LINE = (sbp, pid, sup, name, flags, context) -> name.isEmpty() || name.equals("line") ? new Tex(sup, DIRECT) : null;
    public static final Attributer ITEM_INFO_INFO = (sbp, pid, sup, name, flags, context) -> name.isEmpty() || name.equals("info") ? new Str(sup, DIRECT) : null;
    public static final Attributer LOOP_ITEM = (sbp, pid, sup, name, flags, context) -> name.isEmpty() || name.equals("value") ? new Num(sup, DIRECT, flags) : null;

    public static final Attributer ITEM_ATTRIBUTE_MODIFIER = (sbp, pid, sup, name, flags, context) -> switch (name) {
        case "slot" -> new Str(sup,ITEM_ATTR_SLOT);
        case "attribute","attr" -> new Str(sup,ITEM_ATTR_NAME);
        case "attribute_id","attr_id" -> new Id(sup,ITEM_ATTR_ID,flags);
        case "tracked" -> new Bool(sup,ITEM_ATTR_TRACKED);
        case "default_value" -> new Num(sup,ITEM_ATTR_VALUE_DEFAULT, flags);
        case "attribute_value","attr_value" -> new Num(sup,ITEM_ATTR_VALUE, flags);
        case "", "modifier_name","mod_name" -> new Str(sup,ITEM_ATTR_MODIFIER_NAME);
        case "modifier_id","mod_id" -> new Str(sup,ITEM_ATTR_MODIFIER_ID);
        case "mod_amount","amount" -> new Num(sup,ITEM_ATTR_MODIFIER_VALUE, flags);
        case "op", "operation" -> new Str(sup,ITEM_ATTR_MODIFIER_OPERATION);
        case "op_name", "operation_name" -> new Str(sup,ITEM_ATTR_MODIFIER_OPERATION_NAME);
        default -> null;
    };

    public static final Attributer ITEM_CAN_X = (sbp, pid, sup, name, flags, context) -> switch (name) {
        case "name" -> new Str(sup,BLOCK_NAME);
        case "", "id" -> new Id(sup,BLOCK_ID,flags);
        default -> null;
    };

    public static Attributer ITEM2;
    public static final Attributer ITEM = (sbp, pid, sup, name, flags, context) -> {
        if (name.startsWith("enchant:"))
            return VariableParser.attrElement(name, src -> Registries.ENCHANTMENT.get(Identifier.tryParse(src)), true,
                    (enchant) -> () -> {
                        ItemStack stack = (ItemStack) sup.get();
                        if (stack.isEmpty()) return null;
                        Integer lvl = EnchantmentHelper.get((ItemStack)sup.get()).get(enchant);
                        return lvl == null ? null : Map.entry(enchant, lvl);
                    },
                    ENCHANTMENT, ErrorType.UNKNOWN_EFFECT_ID, ErrorType.UNKNOWN_EFFECT_METHOD, context.profile(), context.line(), context.enabled(), name);
        return switch (name) {
            case "", "item" -> new Special(sup, ITEM_NAME, ITEM_RAW_ID, ITEM_IS_NOT_EMPTY);
            case "id" -> new SpecialId(sup, ITEM_ID, ITEM_RAW_ID, ITEM_IS_NOT_EMPTY, flags);
            case "name" -> new SpecialText(sup, ITEM_CUSTOM_NAME);
            case "count" -> new NumBool(sup, ITEM_COUNT, ITEM_IS_NOT_EMPTY, flags);
            case "max_count" -> new NumBool(sup, ITEM_MAX_COUNT, ITEM_IS_STACKABLE, flags);
            case "dur","durability" -> new NumBool(sup, ITEM_DURABILITY, ITEM_HAS_DURABILITY, flags);
            case "max_dur","max_durability" -> new NumBool(sup, ITEM_MAX_DURABILITY, ITEM_HAS_MAX_DURABILITY, flags);
            case "dur_per","durability_percentage" -> new NumBool(sup, ITEM_DURABILITY_PERCENT, ITEM_HAS_MAX_DURABILITY, flags);
            case "dur_color","durability_color" -> new NumBool(sup, ITEM_DURABILITY_COLOR, ITEM_HAS_MAX_DURABILITY, flags);
            case "unbreakable" -> new Bool(sup, ITEM_UNBREAKABLE);
            case "repair_cost" -> new Num(sup, ITEM_REPAIR_COST, flags);
            case "icon" -> new RichItemSupplierIconElement(pid, sup, flags);
            case "hide_flags" -> new Num(sup, ITEM_HIDE_FLAGS_NUM, flags);
            case "rarity" -> new Special(sup, ITEM_RARITY);
            case "armor_slot" -> new Special(sup, ITEM_ARMOR_SLOT);

            case "enchants" -> new CreateListElement(sbp, sup, ITEM_ENCHANTS, ENCHANTMENT);
            case "lore" -> new CreateListElement(sbp, sup, ITEM_LORE_LINES, ITEM_LORE_LINE);
            case "attributes", "attrs" -> new CreateListElement(sbp, sup, ITEM_ATTRIBUTES, ITEM_ATTRIBUTE_MODIFIER);
            case "can_destroy" -> new CreateListElement(sbp, sup, ITEM_CAN_DESTROY, ITEM_CAN_X);
            case "can_place_on" -> new CreateListElement(sbp, sup, ITEM_CAN_PLAY_ON, ITEM_CAN_X);
            case "info_shown" -> new CreateListElement(sbp, sup, ITEM_SHOWN, ITEM_INFO_INFO);
            case "info_hidden" -> new CreateListElement(sbp, sup, ITEM_HIDDEN, ITEM_INFO_INFO);
            case "tags" -> new CreateListElement(sbp, sup, ITEM_TAGS, TAG);
            case "items" -> new CreateListElement(sbp, sup, ITEM_ITEMS, ITEM2);
            default -> null;
        };
    };
    static { ITEM2 = ITEM; }

    public static final Attributer ATTRIBUTE_MODIFIER = (sbp, pid, sup, name, flags, context) -> switch (name) {
        case "", "name" -> new Str(sup,ATTRIBUTE_MODIFIER_NAME);
        case "id" -> new Str(sup,ATTRIBUTE_MODIFIER_ID);
        case "value" -> new Num(sup,ATTRIBUTE_MODIFIER_VALUE, flags);
        case "op", "operation" -> new Str(sup,ATTRIBUTE_MODIFIER_OPERATION);
        case "op_name", "operation_name" -> new Str(sup,ATTRIBUTE_MODIFIER_OPERATION_NAME);
        default -> null;
    };

    public static final Attributer ATTRIBUTE = (sbp, pid, sup, name, flags, context) -> switch (name) {
        case "name" -> new Str(sup,ATTRIBUTE_NAME);
        case "", "id" -> new Id(sup,ATTRIBUTE_ID,flags);
        case "tracked" -> new Bool(sup,ATTRIBUTE_TRACKED);
        case "default_value" -> new Num(sup,ATTRIBUTE_VALUE_DEFAULT, flags);
        case "base_value" -> new Num(sup,ATTRIBUTE_VALUE_BASE, flags);
        case "value" -> new Num(sup,ATTRIBUTE_VALUE, flags);
        case "modifiers" -> new CreateListElement(sbp, sup,ListSuppliers.ATTRIBUTE_MODIFIERS, ATTRIBUTE_MODIFIER);
        default -> null;
    };

    public static final Attributer TEAM_MEMBER = (sbp, pid, sup, name, flags, context) -> name.isEmpty() || name.equals("member") ? new Str(sup, DIRECT) : null;

    public static final Attributer TEAM = (sbp, pid, sup, name, flags, context) -> switch (name) {
        case "name" -> new Tex(sup, TEAM_NAME);
        case "", "id" -> new Str(sup, TEAM_ID);
        case "friendly_fire" -> new Bool(sup, TEAM_FRIENDLY_FIRE);
        case "see_friendly_invisibility", "friendly_invis" -> new Bool(sup, TEAM_FRIENDLY_INVIS);
        case "name_tag_visibility", "name_tag" -> new Special(sup, TEAM_NAME_TAG_VISIBILITY);
        case "death_msg_visibility", "death_msg" -> new Special(sup, TEAM_DEATH_MGS_VISIBILITY);
        case "collision" -> new Special(sup, TEAM_COLLISION);
        case "color" -> new Special(sup, TEAM_COLOR);
        case "members" -> new CreateListElement(sbp, sup,ListSuppliers.TEAM_MEMBERS, TEAM_MEMBER);
        case "online_players", "players" -> new CreateListElement(sbp, sup, TEAM_PLAYERS, PLAYER);
        default -> null;
    };
    static { TEAM2 = TEAM; }

    public static final Attributer SCOREBOARD_OBJECTIVE_SCORE = (sbp, pid, sup, name, flags, context) -> switch (name) {
        case "", "name", "holder" -> new Str(sup, OBJECTIVE_SCORE_HOLDER_OWNER);
        case "display_name", "display" -> new Tex(sup, OBJECTIVE_SCORE_HOLDER_DISPLAY);
        case "score", "value" -> new Num(sup, OBJECTIVE_SCORE_VALUE, flags);
        default -> null;
    };


    public static final Attributer SCOREBOARD_OBJECTIVE = (sbp, pid, sup, name, flags, context) -> switch (name) {
        case "", "name" -> new Tex(sup, OBJECTIVE_NAME);
        case "id" -> new Str(sup, OBJECTIVE_ID);
        case "criteria","criterion" -> new Str(sup, OBJECTIVE_CRITIERIA);
        case "display_slot" -> new Str(sup, OBJECTIVE_DISPLAY_SLOT);
        case "scores" -> new CreateListElement(sbp, sup, SCOREBOARD_OBJECTIVE_SCORES, SCOREBOARD_OBJECTIVE_SCORE);
        case "online_scores" -> new CreateListElement(sbp, sup, SCOREBOARD_OBJECTIVE_SCORES_ONLINE, SCOREBOARD_OBJECTIVE_SCORE);
        default -> null;
    };

    public static final Attributer SCOREBOARD_SCORE = (sbp, pid, sup, name, flags, context) -> switch (name) {
        case "name" -> new Tex(sup, SCORES_OBJECTIVE_NAME);
        case "", "id" -> new Str(sup, SCORES_OBJECTIVE_ID);
        case "criteria","criterion" -> new Str(sup, SCORES_OBJECTIVE_CRITIERIA);
        case "display_slot" -> new Str(sup, SCORES_OBJECTIVE_DISPLAY_SLOT);
        case "score","value" -> new Num(sup, SCORES_VALUE, flags);
        default -> null;
    };

    public static final Attributer BOSSBAR = (sbp, pid, sup, name, flags, context) -> switch (name) {
        case "name" -> new Tex(sup, BOSSBAR_NAME);
        case "uuid" -> new Str(sup, BOSSBAR_UUID);
        case "id" -> new Id(sup, BOSSBAR_ID,flags); //SP Only
        case "percent","per","value","" -> new Num(sup, BOSSBAR_PERCENT, flags);
        case "darken_sky" -> new Bool(sup, BOSSBAR_DARKEN_SKY);
        case "dragon_music" -> new Bool(sup, BOSSBAR_DRAGON_MUSIC);
        case "thickens_fog" -> new Bool(sup, BOSSBAR_THICKENS_FOG);
        case "style" -> new Special(sup, BOSSBAR_STYLE);
        case "color" -> new Special(sup, BOSSBAR_COLOR);
        case "text_color" -> new Special(sup, BOSSBAR_TEXT_COLOR);
        case "enabled", "visible" -> new Bool(sup, BOSSBAR_IS_VISIBLE); //SP Only
        case "players" -> new CreateListElement(sbp, sup, BOSSBAR_PLAYERS, PLAYER); //SP Only
        case "icon", "bar" -> new BossbarIcon(pid, flags);
        default -> null;
    };

    public static final Attributer MOD_AUTHOR = (sbp, pid, sup, name, flags, context) -> name.isEmpty() || name.equals("author") ? new Str(sup, DIRECT) : null;
    public static final Attributer MOD_CONTRIBUTOR = (sbp, pid, sup, name, flags, context) -> name.isEmpty() || name.equals("contributor") ? new Str(sup, DIRECT) : null;
    public static final Attributer MOD_CREDIT = (sbp, pid, sup, name, flags, context) -> name.isEmpty() || name.equals("credit") ? new Str(sup, DIRECT) : null;
    public static final Attributer MOD_LICENSE = (sbp, pid, sup, name, flags, context) -> name.isEmpty() || name.equals("license") ? new Str(sup, DIRECT) : null;

    public static final Attributer MOD_BADGE = (sbp, pid, sup, name, flags, context) -> switch (name) {
        case "", "name" -> new Str(sup, BADGE_NAME);
        case "outline_color" -> new Num(sup, BADGE_OUTLINE_COLOR, flags);
        case "fill_color" -> new Num(sup, BADGE_FILL_COLOR, flags);
        case "icon" -> new ModBadgeIconElement(pid, flags);
        default -> null;
    };

    private static Attributer MOD2;
    public static final Attributer MOD = (sbp, pid, sup, name, flags, context) -> {
        if (name.startsWith("parent:")) {
            String attr = name.substring(9);
            Supplier sup2 = () -> ModMenu.MODS.get( ((Mod) sup.get()).getParent() );
            return MOD2.get(sbp, pid, sup2, attr, flags, context);
        }
        return switch (name) {
            case "", "name" -> new Str(sup, MOD_NAME);
            case "id" -> new Str(sup, MOD_ID);
            case "summary" -> new Str(sup, MOD_SUMMARY);
            case "description", "desc" -> new Str(sup, MOD_DESCRIPTION);
            case "version" -> new Str(sup, MOD_VERSION);
            case "hash" -> new Str(sup, MOD_HASH);

            case "library" -> new Bool(sup, MOD_IS_LIBRARY);
            case "client" -> new Bool(sup, MOD_IS_CLIENT);
            case "deprecated" -> new Bool(sup, MOD_IS_DEPRECATED);
            case "patchwork" -> new Bool(sup, MOD_IS_PATCHWORK);
            case "from_modpack" -> new Bool(sup, MOD_IS_FROM_MODPACK);
            case "minecraft" -> new Bool(sup, MOD_IS_MINECRAFT);

            case "badges" -> new CreateListElement(sbp, sup, MOD_BADGES, MOD_BADGE);
            case "authors" -> new CreateListElement(sbp, sup, MOD_AUTHORS, MOD_AUTHOR);
            case "contributors" -> new CreateListElement(sbp, sup, MOD_CONTRIBUTORS, MOD_CONTRIBUTOR);
            case "credits" -> new CreateListElement(sbp, sup, MOD_CREDITS, MOD_CREDIT);
            case "licenses" -> new CreateListElement(sbp, sup, MOD_LICENSES, MOD_LICENSE);

            case "parent" -> new CreateListElement(sbp, sup, MOD_PARENTS, MOD2);
            case "children" -> new CreateListElement(sbp, sup, MOD_CHILDREN, MOD2);

            case "icon" -> new ModIconElement(pid, flags);
            default -> null;
        };
    };
    static { MOD2 = MOD; }

    public static final Attributer PACK = (sbp, pid, sup, name, flags, context) -> switch (name) {
        case "","name" -> new Tex(sup, PACK_NAME);
        case "id" -> new Str(sup, PACK_ID);
        case "description", "desc" -> new Tex(sup, PACK_DESCRIPTION);
        case "version" -> new Num(sup, PACK_VERSION, flags);
        case "always_enabled" -> new Bool(sup, PACK_ALWAYS_ENABLED);
        case "pinned" -> new Bool(sup, PACK_IS_PINNED);
        case "compatible" -> new Bool(sup, PACK_IS_COMPATIBLE);
        case "icon" -> new PackIconElement(pid, flags);
        default -> null;
    };

    public static final Attributer RECORD = (sbp, pid, sup, name, flags, context) -> switch (name) {
        case "","name" -> new Tex(sup, RECORD_NAME);
        case "id" -> new Id(sup, RECORD_ID,flags);
        case "length" -> new Num(sup, RECORD_LENGTH, flags);
        case "elapsed" -> new Num(sup, RECORD_ELAPSED, flags);
        case "remaining" -> new Num(sup, RECORD_REMAINING, flags);
        case "elapsed_percentage", "elapsed_per" -> new Num(sup, RECORD_ELAPSED_PER, flags);
        case "icon" -> new ListRecordIconElement(flags);
        default -> null;
    };

    public static final Attributer OFFER = (sbp, pid, sup, name, flags, context) -> {
        int collinIndex = name.indexOf(":");
        if (collinIndex > 0) {
            String attr = name.substring(collinIndex+1);
            boolean isIcon = attr.equals("icon");
            Supplier sup2 = switch (name.substring(0, collinIndex)) {
                case "first" -> isIcon ?
                        () -> (Function<RenderPiece, ItemStack>) piece -> ((TradeOffer) (piece == null ? sup.get() : piece.value)).getAdjustedFirstBuyItem()
                        : (Supplier<ItemStack>) () -> ((TradeOffer)sup.get()).getAdjustedFirstBuyItem();
                case "first_base" -> isIcon ?
                        () -> (Function<RenderPiece, ItemStack>) piece -> ((TradeOffer) (piece == null ? sup.get() : piece.value)).getOriginalFirstBuyItem()
                        : (Supplier<ItemStack>) () -> ((TradeOffer)sup.get()).getOriginalFirstBuyItem();
                case "second" -> isIcon ?
                        () -> (Function<RenderPiece, ItemStack>) piece -> ((TradeOffer) (piece == null ? sup.get() : piece.value)).getSecondBuyItem()
                        : (Supplier<ItemStack>) () -> ((TradeOffer)sup.get()).getSecondBuyItem();
                case "result" -> isIcon ?
                        () -> (Function<RenderPiece, ItemStack>) piece -> ((TradeOffer) (piece == null ? sup.get() : piece.value)).getSellItem()
                        : (Supplier<ItemStack>) () -> ((TradeOffer)sup.get()).getSellItem();
                default -> null;
            };
            if (sup2 != null)
                return ITEM.get(sbp, pid, sup2, attr, flags, context);
        }
        return switch (name) {
            case "uses" -> new Num(sup, OFFER_USES, flags);
            case "max_uses" -> new Num(sup, OFFER_MAX_USES, flags);
            case "special_price" -> new Num(sup, OFFER_SPECIAL_PRICE, flags);
            case "demand_bonus" -> new Num(sup, OFFER_DEMAND_BONUS, flags);
            case "price_multiplier" -> new Num(sup, OFFER_PRICE_MULTIPLIER, flags);
            case "disabled" -> new Bool(sup, OFFER_DISABLED);
            case "can_afford" -> new Bool(sup, OFFER_CAN_AFFORD);

            case "first" -> new CreateListElement(sbp, sup, OFFER_FIRST_ADJUSTED, ITEM);
            case "first_base" -> new CreateListElement(sbp, sup, OFFER_FIRST_BASE, ITEM);
            case "second" -> new CreateListElement(sbp, sup, OFFER_SECOND, ITEM);
            case "", "result" -> new CreateListElement(sbp, sup, OFFER_RESULT, ITEM);
            default -> null;
        };
    };

    public static final Attributer ITEM_CONVERTABLE_TAG_ENTRY = (sbp, pid, sup, name, flags, context) -> switch (name) {
        case "name" -> new Tex(sup, TAG_ENTRY_NAME);
        case "", "id" -> new Id(sup, TAG_ENTRY_ID,flags);
        case "icon" -> new RichItemSupplierIconElement(pid, () -> new ItemStack(((ItemConvertible) sup.get()).asItem()), flags);
        default -> null;
    };


    public static final Map<ListProvider, Attributer> ATTRIBUTER_MAP = new HashMap<>();
    public static final Map<Attributer, String> DEFAULT_PREFIX = new HashMap<>();
    static {
        ATTRIBUTER_MAP.put(STATUS_EFFECTS, EFFECT);
        ATTRIBUTER_MAP.put(STATUS_EFFECTS_POSITIVE, EFFECT);
        ATTRIBUTER_MAP.put(STATUS_EFFECTS_POSITIVE, EFFECT);
        ATTRIBUTER_MAP.put(STATUS_EFFECTS_NEGATIVE, EFFECT);
        ATTRIBUTER_MAP.put(STATUS_EFFECTS_NEUTRAL, EFFECT);
        ATTRIBUTER_MAP.put(ONLINE_PLAYERS, PLAYER);
        ATTRIBUTER_MAP.put(SUBTITLES, SUBTITLE);
        ATTRIBUTER_MAP.put(TARGET_BLOCK_STATES, BLOCK_PROPERTY);
        ATTRIBUTER_MAP.put(TARGET_BLOCK_TAGS, TAG);
        ATTRIBUTER_MAP.put(TARGET_BLOCK_POWERS, RECEIVED_POWER);
        ATTRIBUTER_MAP.put(TARGET_FLUID_STATES, BLOCK_PROPERTY);
        ATTRIBUTER_MAP.put(TARGET_FLUID_TAGS, TAG);
//        ATTRIBUTER_MAP.put(TARGET_BLOCK_ITEMS, ITEM);
//        ATTRIBUTER_MAP.put(TARGET_BLOCK_COMPACT_ITEMS, ITEM);
        ATTRIBUTER_MAP.put(PLAYER_ATTRIBUTES, ATTRIBUTE);
        ATTRIBUTER_MAP.put(TARGET_ENTITY_ATTRIBUTES, ATTRIBUTE);
        ATTRIBUTER_MAP.put(HOOKED_ENTITY_ATTRIBUTES, ATTRIBUTE);
        ATTRIBUTER_MAP.put(TEAMS, TEAM);
        ATTRIBUTER_MAP.put(ALL_ITEMS, ITEM);
        ATTRIBUTER_MAP.put(INV_ITEMS, ITEM);
        ATTRIBUTER_MAP.put(ARMOR_ITEMS, ITEM);
        ATTRIBUTER_MAP.put(HOTBAR_ITEMS, ITEM);
        ATTRIBUTER_MAP.put(EQUIPPED_ITEMS, ITEM);
        ATTRIBUTER_MAP.put(ITEMS, ITEM);
        ATTRIBUTER_MAP.put(SCOREBOARD_OBJECTIVES, SCOREBOARD_OBJECTIVE);
        ATTRIBUTER_MAP.put(PLAYER_SCOREBOARD_SCORES, SCOREBOARD_SCORE);
        ATTRIBUTER_MAP.put(BOSSBARS, BOSSBAR);
        ATTRIBUTER_MAP.put(ALL_BOSSBARS, BOSSBAR);
        ATTRIBUTER_MAP.put(MODS, MOD);
        ATTRIBUTER_MAP.put(ALL_ROOT_MODS, MOD);
        ATTRIBUTER_MAP.put(ALL_MODS, MOD);
        ATTRIBUTER_MAP.put(RESOURCE_PACKS, PACK);
        ATTRIBUTER_MAP.put(DISABLED_RESOURCE_PACKS, PACK);
        ATTRIBUTER_MAP.put(DATA_PACKS, PACK);
        ATTRIBUTER_MAP.put(DISABLED_DATA_PACKS, PACK);
        ATTRIBUTER_MAP.put(RECORDS, RECORD);
        ATTRIBUTER_MAP.put(TARGET_VILLAGER_OFFERS, OFFER);

        DEFAULT_PREFIX.put(EFFECT, "e");
        DEFAULT_PREFIX.put(PLAYER, "p");
        DEFAULT_PREFIX.put(SUBTITLE, "s");
        DEFAULT_PREFIX.put(BLOCK_PROPERTY, "p");
        DEFAULT_PREFIX.put(RECEIVED_POWER, "r");
        DEFAULT_PREFIX.put(TAG, "t");
        DEFAULT_PREFIX.put(ENCHANTMENT, "e");
        DEFAULT_PREFIX.put(ITEM_LORE_LINE, "lore");
        DEFAULT_PREFIX.put(ITEM_INFO_INFO, "ii");
        DEFAULT_PREFIX.put(LOOP_ITEM, "loop");
        DEFAULT_PREFIX.put(ITEM_ATTRIBUTE_MODIFIER, "am");
        DEFAULT_PREFIX.put(ITEM_CAN_X, "c");
        DEFAULT_PREFIX.put(ITEM, "i");
        DEFAULT_PREFIX.put(ITEM2, "i");
        DEFAULT_PREFIX.put(ATTRIBUTE_MODIFIER, "am");
        DEFAULT_PREFIX.put(ATTRIBUTE, "a");
        DEFAULT_PREFIX.put(TEAM_MEMBER, "m");
        DEFAULT_PREFIX.put(TEAM, "t");
        DEFAULT_PREFIX.put(SCOREBOARD_OBJECTIVE_SCORE, "os");
        DEFAULT_PREFIX.put(SCOREBOARD_OBJECTIVE, "o");
        DEFAULT_PREFIX.put(SCOREBOARD_SCORE, "ss");
        DEFAULT_PREFIX.put(BOSSBAR, "b");
        DEFAULT_PREFIX.put(MOD_AUTHOR, "ma");
        DEFAULT_PREFIX.put(MOD_CONTRIBUTOR, "mc");
        DEFAULT_PREFIX.put(MOD_CREDIT, "mc");
        DEFAULT_PREFIX.put(MOD_LICENSE, "ml");
        DEFAULT_PREFIX.put(MOD_BADGE, "mb");
        DEFAULT_PREFIX.put(MOD, "m");
        DEFAULT_PREFIX.put(MOD2, "m");
        DEFAULT_PREFIX.put(PACK, "p");
        DEFAULT_PREFIX.put(RECORD, "r");
        DEFAULT_PREFIX.put(OFFER, "o");
        DEFAULT_PREFIX.put(ITEM_CONVERTABLE_TAG_ENTRY, "t");
    }

    public static HudElement get(ListProviderSet set, String name, Flags flags, Profile profile, int line) {
        for (int i = set.entries.size()-1; i >= 0; i--) {
            HudElement e = get0(set.entries.get(i), name, flags, profile, line);
            if (e != null)
                return e;
        }
        return null;
    }

    public static HudElement getFromPrefix(ListProviderSet set, String part, Flags flags, Profile profile, int line) {
        for (int i = set.entries.size() - 1; i >= 0; i--) {
            ListProviderSet.Entry entry = set.entries.get(i);
            if (entry != null && part.startsWith(entry.prefix() + ":"))
                return get0(entry, part, flags, profile, line);
        }
        return null;
    }

    public static HudElement get0( ListProviderSet.Entry entry, String part, Flags flags, Profile profile, int line) {
        if (entry == null)
            return null;

        UUID finalProviderID = entry.id();
        part = part.substring(part.indexOf(':')+1);

        switch (part) {
            case "size", "c": return new NumberSupplierElement( () -> ListManager.getCount(finalProviderID), flags);
            case "index", "i": return new NumberSupplierElement( () -> ListManager.getIndex(finalProviderID), flags);
            case "raw": return new StringSupplierElement( () -> ListManager.getValue(finalProviderID).toString() );
            case "exit": return new FunctionalElement.ExitList( finalProviderID );
            case "continue": return new FunctionalElement.ContinueList( finalProviderID );
        };

        Attributer attributer = ATTRIBUTER_MAP.get(entry.provider());
        if (attributer == null) {
            CustomHud.LOGGER.error("[CustomHud] [FIX ME]: Attributer not in Map!");
            return null;
        }
        String[] flagParts = part.split(" ");
        String prefix = VariableParser.getPrefix(entry.provider(), flagParts, profile.name, line, part);
        part = flagParts[0];

        int dotIndex = part.lastIndexOf('.');
        if (dotIndex != -1)
            part = part.substring(0, dotIndex);

        ParseContext context = new ParseContext(profile, line, null, null);
        HudElement element = attributer.get(prefix, finalProviderID, () -> ListManager.getValue(finalProviderID), part, flags, context);
        if (element instanceof CreateListElement cle) {
            String attr = dotIndex == -1 ? "" : flagParts[0].substring(dotIndex + 1);
            cle.attribute = Attributers.get(new ListProviderSet().with(cle.entry), attr, new Flags(), profile, line);
        }
        return element;
    }

    public static String defaultPrefix(ListProvider provider) {
        return DEFAULT_PREFIX.get( ATTRIBUTER_MAP.get(provider) );
    }

}
