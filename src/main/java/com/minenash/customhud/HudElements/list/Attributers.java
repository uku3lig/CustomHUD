package com.minenash.customhud.HudElements.list;

import com.minenash.customhud.HudElements.HudElement;
import com.minenash.customhud.HudElements.FuncElements.*;
import com.minenash.customhud.HudElements.functional.FunctionalElement.CreateListElement;
import com.minenash.customhud.HudElements.icon.*;
import com.minenash.customhud.data.Flags;
import com.minenash.customhud.render.RenderPiece;
import com.terraformersmc.modmenu.ModMenu;
import com.terraformersmc.modmenu.util.mod.Mod;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.village.TradeOffer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.minenash.customhud.HudElements.list.AttributeFunctions.*;
import static com.minenash.customhud.HudElements.list.AttributeFunctions.ITEM_ATTR_MODIFIER_NAME;
import static com.minenash.customhud.HudElements.list.ListSuppliers.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class Attributers {

    @FunctionalInterface
    public interface Attributer {
        HudElement get(Supplier supplier, String attr, Flags flags);
    }

    //TODO: IS ONLY LIST ONLY
    public static final Attributer EFFECT = (sup, name, flags) -> switch (name) {
        case "e_name" -> new Str(sup,STATUS_NAME);
        case "e_id" -> new Str(sup,STATUS_ID);
        case "e_duration", "e_dur" -> new Num(sup, STATUS_DURATION, flags);
        case "e_infinite", "e_inf" -> new Bool(sup, STATUS_INFINITE);
        case "e_amplification", "e_amp" -> new Num(sup, STATUS_AMPLIFICATION, flags);
        case "e_level", "e_lvl" -> new Num(sup, STATUS_LEVEL, flags);
        case "e_ambient" -> new Bool(sup,STATUS_AMBIENT);
        case "e_show_particles", "e_particles" -> new Bool(sup,STATUS_SHOW_PARTICLES);
        case "e_show_icon" -> new Bool(sup,STATUS_SHOW_ICON);
        case "e_color" -> new Num(sup, STATUS_COLOR, flags);
        case "e_category", "e_cat" -> new Special(sup,STATUS_CATEGORY);
        case "e_icon" -> new StatusEffectIconElement(flags, true); //LIST ONLY
        case "e_icon_no_bg" -> new StatusEffectIconElement(flags, false); //LIST ONLY
        default -> null;
    };

    public static final Attributer PLAYER = (sup, name, flags) -> switch (name) {
        case "p_name" -> new Str(sup,PLAYER_ENTRY_NAME);
        case "p_id" -> new Str(sup,PLAYER_ENTRY_UUID);
        case "p_team" -> new Str(sup, PLAYER_ENTRY_TEAM);
        case "p_latency" -> new Num(sup,PLAYER_ENTRY_LATENCY, flags);
        case "p_list_score" -> new Num(sup,PLAYER_ENTRY_LIST_SCORE, flags);
        case "p_gamemode" -> new Special(sup,PLAYER_ENTRY_GAMEMODE);
        case "p_survival" -> new Bool(sup,PLAYER_ENTRY_SURVIVAL);
        case "p_creative" -> new Bool(sup,PLAYER_ENTRY_CREATIVE);
        case "p_adventure" -> new Bool(sup,PLAYER_ENTRY_ADVENTURE);
        case "p_spectator" -> new Bool(sup,PLAYER_ENTRY_SPECTATOR);
        case "p_head" -> new PlayerHeadIconElement(flags); //TODO FIX
        default -> null;
    };

    public static final Attributer SUBTITLE = (sup, name, flags) -> switch (name) {
        case "s_id" -> new Str(sup, SUBTITLE_ID);
        case "s_name" -> new Str(sup, SUBTITLE_NAME);

        case "s_age" -> new Num(sup, SUBTITLE_AGE, flags);
        case "s_time" -> new Num(sup, SUBTITLE_TIME, flags);
        case "s_alpha" -> new Num(sup, SUBTITLE_ALPHA, flags);
        case "s_x" -> new Num(sup, SUBTITLE_X, flags);
        case "s_y" -> new Num(sup, SUBTITLE_Y, flags);
        case "s_z" -> new Num(sup, SUBTITLE_Z, flags);
        case "s_dist", "distance" -> new Num(sup, SUBTITLE_DISTANCE, flags);

        case "s_dir", "s_direction" -> new Str(sup, SUBTITLE_DIRECTION);
        case "s_left" -> new Bool(sup, SUBTITLE_LEFT);
        case "s_right" -> new Bool(sup, SUBTITLE_RIGHT);
        case "s_dir_yaw", "s_direction_yaw" -> new Num(sup, SUBTITLE_DIRECTION_YAW, flags);
        case "s_dir_pitch", "s_direction_pitch" -> new Num(sup, SUBTITLE_DIRECTION_PITCH, flags);
        default -> null;
    };

    public static final Attributer BLOCK_STATE = (sup, name, flags) -> switch (name) {
        case "b_name" -> new Str(sup, BLOCK_STATE_NAME);
        case "b_type" -> new Special(sup, BLOCK_STATE_TYPE);
        case "b_full_type" -> new Str(sup,BLOCK_STATE_FULL_TYPE);
        case "b_value" -> new Str(sup, BLOCK_STATE_VALUE);
        default -> null;
    };

    public static final Attributer BLOCK_TAG = (sup, name, flags) -> switch (name) {
        case "b_name" -> new Str(sup,BLOCK_TAG_NAME);
        case "b_id" -> new Str(sup,BLOCK_TAG_ID);
        default -> null;
    };

    public static final Attributer ENCHANTMENT = (sup, name, flags) -> switch (name) {
        case "e_name" -> new Str(sup,ENCHANT_NAME);
        case "e_full" -> new Str(sup,ENCHANT_FULL);
        case "e_level" -> new Special(sup,ENCHANT_LEVEL);
        case "e_num", "e_number" -> new Num(sup,ENCHANT_NUM, flags);
        case "e_rarity" -> new Str(sup,ENCHANT_RARITY);
        default -> null;
    };

    public static final Attributer ITEM_LORE_LINE = (sup, name, f) -> name.equals("line") ? new Tex(sup, DIRECT) : null;
    public static final Attributer ITEM_INFO_INFO = (sup, name, f) -> name.equals("info") ? new Str(sup, DIRECT) : null;
    public static final Attributer LOOP_ITEM = (sup, name, flags) -> name.equals("value") ? new Num(sup, DIRECT, flags) : null;

    public static final Attributer ITEM_ATTRIBUTE_MODIFIER = (sup, name, flags) -> switch (name) {
        case "m_slot" -> new Str(sup,ITEM_ATTR_SLOT);
        case "m_attribute","m_attr" -> new Str(sup,ITEM_ATTR_NAME);
        case "m_attribute_id","m_attr_id" -> new Str(sup,ITEM_ATTR_ID);
        case "m_tracked" -> new Bool(sup,ITEM_ATTR_TRACKED);
        case "m_default_value" -> new Num(sup,ITEM_ATTR_VALUE_DEFAULT, flags);
        case "m_attribute_value","m_attr_value" -> new Num(sup,ITEM_ATTR_VALUE, flags);
        case "m_modifier_name","m_mod_name" -> new Str(sup,ITEM_ATTR_MODIFIER_NAME);
        case "m_modifier_id","m_mod_id" -> new Str(sup,ITEM_ATTR_MODIFIER_ID);
        case "m_mod_amount","m_amount" -> new Num(sup,ITEM_ATTR_MODIFIER_VALUE, flags);
        case "m_op", "m_operation" -> new Str(sup,ITEM_ATTR_MODIFIER_OPERATION);
        case "m_op_name", "m_operation_name" -> new Str(sup,ITEM_ATTR_MODIFIER_OPERATION_NAME);
        default -> null;
    };

    public static final Attributer ITEM_CAN_X = (sup, name, flags) -> switch (name) {
        case "x_name" -> new Str(sup,BLOCK_NAME);
        case "x_id" -> new Str(sup,BLOCK_ID);
        default -> null;
    };

    public static final Attributer ITEM = (sup, name, flags) -> switch (name) {
        case "", "i_item" -> new Special(sup, ITEM_NAME, ITEM_RAW_ID, ITEM_IS_NOT_EMPTY);
        case "i_id" -> new Special(sup, ITEM_ID, ITEM_RAW_ID, ITEM_IS_NOT_EMPTY);
        case "i_name" -> new SpecialText(sup, ITEM_CUSTOM_NAME);
        case "i_count" -> new NumBool(sup, ITEM_COUNT, ITEM_IS_NOT_EMPTY, flags);
        case "i_max_count" -> new NumBool(sup, ITEM_MAX_COUNT, ITEM_IS_STACKABLE, flags);
        case "i_dur","i_durability" -> new NumBool(sup, ITEM_DURABILITY, ITEM_HAS_DURABILITY, flags);
        case "i_max_dur","i_max_durability" -> new NumBool(sup, ITEM_MAX_DURABILITY, ITEM_HAS_MAX_DURABILITY, flags);
        case "i_dur_per","i_durability_percentage" -> new NumBool(sup, ITEM_DURABILITY_PERCENT, ITEM_HAS_MAX_DURABILITY, flags);
        case "i_dur_color","i_durability_color" -> new NumBool(sup, ITEM_DURABILITY_COLOR, ITEM_HAS_MAX_DURABILITY, flags);
        case "i_unbreakable" -> new Bool(sup, ITEM_UNBREAKABLE);
        case "i_repair_cost" -> new Num(sup, ITEM_REPAIR_COST, flags);
        case "i_icon" -> new ItemSupplierIconElement(sup, flags);
        case "i_hide_flags" -> new Num(sup, ITEM_HIDE_FLAGS_NUM, flags);
        case "i_rarity" -> new Special(sup, ITEM_RARITY);

        case "i_enchants" -> new CreateListElement(sup, ITEM_ENCHANTS, ENCHANTMENT);
        case "i_lore" -> new CreateListElement(sup, ITEM_LORE_LINES, ITEM_LORE_LINE);
        case "i_attributes", "i_attrs" -> new CreateListElement(sup, ITEM_ATTRIBUTES, ITEM_ATTRIBUTE_MODIFIER);
        case "i_can_destroy" -> new CreateListElement(sup, ITEM_CAN_DESTROY, ITEM_CAN_X);
        case "i_can_place_on" -> new CreateListElement(sup, ITEM_CAN_PLAY_ON, ITEM_CAN_X);
        case "i_info_shown" -> new CreateListElement(sup, ITEM_SHOWN, ITEM_INFO_INFO);
        case "i_info_hidden" -> new CreateListElement(sup, ITEM_HIDDEN, ITEM_INFO_INFO);
        default -> null;
    };

    public static final Attributer ATTRIBUTE_MODIFIER = (sup, name, flags) -> switch (name) {
        case "m_name" -> new Str(sup,ATTRIBUTE_MODIFIER_NAME);
        case "m_id" -> new Str(sup,ATTRIBUTE_MODIFIER_ID);
        case "m_value" -> new Num(sup,ATTRIBUTE_MODIFIER_VALUE, flags);
        case "m_op", "m_operation" -> new Str(sup,ATTRIBUTE_MODIFIER_OPERATION);
        case "m_op_name", "m_operation_name" -> new Str(sup,ATTRIBUTE_MODIFIER_OPERATION_NAME);
        default -> null;
    };

    public static final Attributer ATTRIBUTE = (sup, name, flags) -> switch (name) {
        case "a_name" -> new Str(sup,ATTRIBUTE_NAME);
        case "a_id" -> new Str(sup,ATTRIBUTE_ID);
        case "a_tracked" -> new Bool(sup,ATTRIBUTE_TRACKED);
        case "a_default_value" -> new Num(sup,ATTRIBUTE_VALUE_DEFAULT, flags);
        case "a_base_value" -> new Num(sup,ATTRIBUTE_VALUE_BASE, flags);
        case "a_value" -> new Num(sup,ATTRIBUTE_VALUE, flags);
        case "a_modifiers" -> new CreateListElement(sup,ListSuppliers.ATTRIBUTE_MODIFIERS, ATTRIBUTE_MODIFIER);
        default -> null;
    };

    public static final Attributer TEAM_MEMBER = (sup, name, f) -> name.equals("member") ? new Str(sup, DIRECT) : null;

    public static final Attributer TEAM = (sup, name, flags) -> switch (name) {
        case "", "t_name" -> new Tex(sup, TEAM_NAME);
        case "t_id" -> new Str(sup, TEAM_ID);
        case "t_friendly_fire" -> new Bool(sup, TEAM_FRIENDLY_FIRE);
        case "t_see_friendly_invisibility", "t_friendly_invis" -> new Bool(sup, TEAM_FRIENDLY_INVIS);
        case "t_name_tag_visibility", "t_name_tag" -> new Special(sup, TEAM_NAME_TAG_VISIBILITY);
        case "t_death_msg_visibility", "t_death_msg" -> new Special(sup, TEAM_DEATH_MGS_VISIBILITY);
        case "t_collision" -> new Special(sup, TEAM_COLLISION);
        case "t_color" -> new Special(sup, TEAM_COLOR);
        case "t_members" -> new CreateListElement(sup,ListSuppliers.TEAM_MEMBERS, TEAM_MEMBER);
        case "t_online_players", "t_players" -> new CreateListElement(sup, TEAM_PLAYERS, PLAYER);
        default -> null;
    };

    public static final Attributer SCOREBOARD_OBJECTIVE_SCORE = (sup, name, flags) -> switch (name) {
        case "s_name", "s_holder" -> new Str(sup, OBJECTIVE_SCORE_HOLDER_OWNER);
        case "s_display_name", "s_display" -> new Tex(sup, OBJECTIVE_SCORE_HOLDER_DISPLAY);
        case "s_score", "s_value" -> new Num(sup, OBJECTIVE_SCORE_VALUE, flags);
        default -> null;
    };


    public static final Attributer SCOREBOARD_OBJECTIVE = (sup, name, flags) -> switch (name) {
        case "", "o_name" -> new Tex(sup, OBJECTIVE_NAME);
        case "o_id" -> new Str(sup, OBJECTIVE_ID);
        case "o_criteria","o_criterion" -> new Str(sup, OBJECTIVE_CRITIERIA);
        case "o_display_slot" -> new Str(sup, OBJECTIVE_DISPLAY_SLOT);
        case "o_scores" -> new CreateListElement(sup, SCOREBOARD_OBJECTIVE_SCORES, SCOREBOARD_OBJECTIVE_SCORE);
        case "o_online_scores" -> new CreateListElement(sup, SCOREBOARD_OBJECTIVE_SCORES_ONLINE, SCOREBOARD_OBJECTIVE_SCORE);
        default -> null;
    };

    public static final Attributer SCOREBOARD_SCORE = (sup, name, flags) -> switch (name) {
        case "ss_name" -> new Tex(sup, SCORES_OBJECTIVE_NAME);
        case "ss_id" -> new Str(sup, SCORES_OBJECTIVE_ID);
        case "ss_criteria","ss_criterion" -> new Str(sup, SCORES_OBJECTIVE_CRITIERIA);
        case "ss_display_slot" -> new Str(sup, SCORES_OBJECTIVE_DISPLAY_SLOT);
        case "ss_score","ss_value" -> new Num(sup, SCORES_VALUE, flags);
        default -> null;
    };

    public static final Attributer BOSSBAR = (sup, name, flags) -> switch (name) {
        case "b_name" -> new Tex(sup, BOSSBAR_NAME);
        case "b_uuid" -> new Str(sup, BOSSBAR_UUID);
        case "b_id" -> new Str(sup, BOSSBAR_ID); //SP Only
        case "b_percent","b_per","b_value","" -> new Num(sup, BOSSBAR_PERCENT, flags);
        case "b_darken_sky" -> new Bool(sup, BOSSBAR_DARKEN_SKY);
        case "b_dragon_music" -> new Bool(sup, BOSSBAR_DRAGON_MUSIC);
        case "b_thickens_fog" -> new Bool(sup, BOSSBAR_THICKENS_FOG);
        case "b_style" -> new Special(sup, BOSSBAR_STYLE);
        case "b_color" -> new Special(sup, BOSSBAR_COLOR);
        case "b_text_color" -> new Special(sup, BOSSBAR_TEXT_COLOR);
        case "b_enabled", "b_visible" -> new Bool(sup, BOSSBAR_IS_VISIBLE); //SP Only
        case "b_players" -> new CreateListElement(sup, BOSSBAR_PLAYERS, PLAYER); //SP Only
        case "b_icon", "b_bar" -> new BossbarIcon(sup, flags);
        default -> null;
    };

    public static final Attributer MOD_AUTHOR = (sup, name, f) -> name.equals("author") ? new Str(sup, DIRECT) : null;
    public static final Attributer MOD_CONTRIBUTOR = (sup, name, f) -> name.equals("contributor") ? new Str(sup, DIRECT) : null;
    public static final Attributer MOD_CREDIT = (sup, name, f) -> name.equals("credit") ? new Str(sup, DIRECT) : null;
    public static final Attributer MOD_LICENSE = (sup, name, f) -> name.equals("license") ? new Str(sup, DIRECT) : null;

    public static final Attributer MOD_BADGE = (sup, name, flags) -> switch (name) {
        case "", "b_name" -> new Str(sup, BADGE_NAME);
        case "b_outline_color" -> new Num(sup, BADGE_OUTLINE_COLOR, flags);
        case "b_fill_color" -> new Num(sup, BADGE_FILL_COLOR, flags);
        case "b_icon" -> new ModBadgeIconElement(flags);
        default -> null;
    };

    public static Attributer MOD2;
    public static final Attributer MOD = (sup, name, flags) -> {
        if (name.startsWith("m_parent:")) {
            String attr = name.substring(9);
            Supplier sup2 = () -> ModMenu.MODS.get( ((Mod) sup.get()).getParent() );
            return MOD2.get(sup2, attr, flags);
        }
        return switch (name) {
            case "", "m_name" -> new Str(sup, MOD_NAME);
            case "m_id" -> new Str(sup, MOD_ID);
            case "m_summary" -> new Str(sup, MOD_SUMMARY);
            case "m_description", "m_desc" -> new Str(sup, MOD_DESCRIPTION);
            case "m_version" -> new Str(sup, MOD_VERSION);
            case "m_hash" -> new Str(sup, MOD_HASH);

            case "m_library" -> new Bool(sup, MOD_IS_LIBRARY);
            case "m_client" -> new Bool(sup, MOD_IS_CLIENT);
            case "m_deprecated" -> new Bool(sup, MOD_IS_DEPRECATED);
            case "m_patchwork" -> new Bool(sup, MOD_IS_PATCHWORK);
            case "m_from_modpack" -> new Bool(sup, MOD_IS_FROM_MODPACK);
            case "m_minecraft" -> new Bool(sup, MOD_IS_MINECRAFT);

            case "m_badges" -> new CreateListElement(sup, MOD_BADGES, MOD_BADGE);
            case "m_authors" -> new CreateListElement(sup, MOD_AUTHORS, MOD_AUTHOR);
            case "m_contributors" -> new CreateListElement(sup, MOD_CONTRIBUTORS, MOD_CONTRIBUTOR);
            case "m_credits" -> new CreateListElement(sup, MOD_CREDITS, MOD_CREDIT);
            case "m_licenses" -> new CreateListElement(sup, MOD_LICENSES, MOD_LICENSE);

            case "m_parent" -> new CreateListElement(sup, MOD_PARENTS, MOD2);
            case "m_children" -> new CreateListElement(sup, MOD_CHILDREN, MOD2);

            case "m_icon" -> new ModIconElement(sup, flags);
            default -> null;
        };
    };
    static {
        MOD2 = MOD;
    }

    public static final Attributer PACK = (sup, name, flags) -> switch (name) {
        case "","p_name" -> new Tex(sup, PACK_NAME);
        case "p_id" -> new Str(sup, PACK_ID);
        case "p_description", "p_desc" -> new Tex(sup, PACK_DESCRIPTION);
        case "p_version" -> new Num(sup, PACK_VERSION, flags);
        case "p_always_enabled" -> new Bool(sup, PACK_ALWAYS_ENABLED);
        case "p_pinned" -> new Bool(sup, PACK_IS_PINNED);
        case "p_compatible" -> new Bool(sup, PACK_IS_COMPATIBLE);
        case "p_icon" -> new PackIconElement(sup, flags);
        default -> null;
    };

    public static final Attributer RECORD = (sup, name, flags) -> switch (name) {
        case "","r_name" -> new Tex(sup, RECORD_NAME);
        case "r_id" -> new Str(sup, RECORD_ID);
        case "r_length" -> new Num(sup, RECORD_LENGTH, flags);
        case "r_elapsed" -> new Num(sup, RECORD_ELAPSED, flags);
        case "r_remaining" -> new Num(sup, RECORD_REMAINING, flags);
        case "r_elapsed_percentage", "r_elapsed_per" -> new Num(sup, RECORD_ELAPSED_PER, flags);
        case "r_icon" -> new ListRecordIconElement(flags);
        default -> null;
    };

    public static final Attributer OFFER = (sup, name, flags) -> {
        int collinIndex = name.indexOf(":");
        if (collinIndex > 0) {
            String attr = name.substring(collinIndex+1);
            boolean isIcon = attr.equals("i_icon");
            Supplier sup2 = switch (name.substring(0, collinIndex)) {
                case "o_first" -> isIcon ?
                        () -> (Function<RenderPiece, ItemStack>) piece -> ((TradeOffer) (piece == null ? sup.get() : piece.value)).getAdjustedFirstBuyItem()
                        : (Supplier<ItemStack>) () -> ((TradeOffer)sup.get()).getAdjustedFirstBuyItem();
                case "o_first_base" -> isIcon ?
                        () -> (Function<RenderPiece, ItemStack>) piece -> ((TradeOffer) (piece == null ? sup.get() : piece.value)).getOriginalFirstBuyItem()
                        : (Supplier<ItemStack>) () -> ((TradeOffer)sup.get()).getOriginalFirstBuyItem();
                case "o_second" -> isIcon ?
                        () -> (Function<RenderPiece, ItemStack>) piece -> ((TradeOffer) (piece == null ? sup.get() : piece.value)).getSecondBuyItem()
                        : (Supplier<ItemStack>) () -> ((TradeOffer)sup.get()).getSecondBuyItem();
                case "o_result" -> isIcon ?
                        () -> (Function<RenderPiece, ItemStack>) piece -> ((TradeOffer) (piece == null ? sup.get() : piece.value)).getSellItem()
                        : (Supplier<ItemStack>) () -> ((TradeOffer)sup.get()).getSellItem();
                default -> null;
            };
            if (sup2 != null)
                return ITEM.get(sup2, attr, flags);
        }
        return switch (name) {
            case "o_uses" -> new Num(sup, OFFER_USES, flags);
            case "o_max_uses" -> new Num(sup, OFFER_MAX_USES, flags);
            case "o_special_price" -> new Num(sup, OFFER_SPECIAL_PRICE, flags);
            case "o_demand_bonus" -> new Num(sup, OFFER_DEMAND_BONUS, flags);
            case "o_price_multiplier" -> new Num(sup, OFFER_PRICE_MULTIPLIER, flags);
            case "o_disabled" -> new Bool(sup, OFFER_DISABLED);
            case "o_can_afford" -> new Bool(sup, OFFER_CAN_AFFORD);

            case "o_first" -> new CreateListElement(sup, OFFER_FIRST_ADJUSTED, ITEM);
            case "o_first_base" -> new CreateListElement(sup, OFFER_FIRST_BASE, ITEM);
            case "o_second" -> new CreateListElement(sup, OFFER_SECOND, ITEM);
            case "o_result" -> new CreateListElement(sup, OFFER_RESULT, ITEM);
            default -> null;
        };
    };


    public static final Map<ListProvider, Attributer> ATTRIBUTER_MAP = new HashMap<>();
    static {
        ATTRIBUTER_MAP.put(STATUS_EFFECTS, EFFECT);
        ATTRIBUTER_MAP.put(STATUS_EFFECTS_POSITIVE, EFFECT);
        ATTRIBUTER_MAP.put(STATUS_EFFECTS_NEGATIVE, EFFECT);
        ATTRIBUTER_MAP.put(STATUS_EFFECTS_NEUTRAL, EFFECT);
        ATTRIBUTER_MAP.put(ONLINE_PLAYERS, PLAYER);
        ATTRIBUTER_MAP.put(SUBTITLES, SUBTITLE);
        ATTRIBUTER_MAP.put(TARGET_BLOCK_STATES, BLOCK_STATE);
        ATTRIBUTER_MAP.put(TARGET_BLOCK_TAGS, BLOCK_TAG);
        ATTRIBUTER_MAP.put(PLAYER_ATTRIBUTES, ATTRIBUTE);
        ATTRIBUTER_MAP.put(TARGET_ENTITY_ATTRIBUTES, ATTRIBUTE);
        ATTRIBUTER_MAP.put(HOOKED_ENTITY_ATTRIBUTES, ATTRIBUTE);
        ATTRIBUTER_MAP.put(TEAMS, TEAM);
        ATTRIBUTER_MAP.put(ALL_ITEMS, ITEM);
        ATTRIBUTER_MAP.put(INV_ITEMS, ITEM);
        ATTRIBUTER_MAP.put(ARMOR_ITEMS, ITEM);
        ATTRIBUTER_MAP.put(HOTBAR_ITEMS, ITEM);
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

        // ATTRIBUTER_MAP.put(ATTRIBUTE_MODIFIERS, ATTRIBUTE_MODIFIER);
        // ATTRIBUTER_MAP.put(TEAM_MEMBERS, TEAM_MEMBER);
        // ATTRIBUTER_MAP.put(TEAM_PLAYERS, PLAYER);

        // ITEM_LORE
        // ENCHANTMENT
    }

    public static HudElement get(ListProvider list, Supplier<?> value, String name, Flags flags) {
        Attributer attributer = ATTRIBUTER_MAP.get(list);
        if (attributer == null) {
            System.out.println("[FIX ME]: Attributer not in Map!");
            return null;
        }
        return attributer.get(value, name, flags);
    }

}
