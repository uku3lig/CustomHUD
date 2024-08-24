package com.minenash.customhud.errors;

import net.minecraft.text.*;
import net.minecraft.util.Formatting;

public enum ErrorType {
    NONE ("", null, "Good Job!"),
    HEADER ("Help", null, "Details"),
    TEST ("Uhhh", "", "The bee movie script The bee movie script The bee movie script The bee movie script The bee movie script The bee movie script The bee movie script The bee movie script The bee movie script"),
    IO ("Help", "../help", "Could not load file: §c"),

    VARIABLE_ERROR ("Report", "CHANGE ME", "An error has accord trying to parse this variable. " +
            "Please report this along with log. You might want to double check your profile though"),
    PROFILE_ERROR ("Report", "CHANGE ME", "A fatal error has accord trying to parse this profile. " +
            "Please report this along with log. You might want to double check your profile though"),

    EMPTY_VARIABLE ("Variables", "variables", "No Variable Given"),
    UNKNOWN_VARIABLE ("Variables", "variables", "Unknown Variable: §e"),
    UNKNOWN_LIST ("CHANGE ME", "CHANGE ME", "Unknown List Variable: §e"),
    UNKNOWN_VARIABLE_FLAG ("Variable Flags", "references/variable_flags", "Unknown Variable Flag: §e"),
    RADIX_TOO_BIG ("Variable Flags", "references/variable_flags", "Radix can't be above 36"),
    RADIX_TOO_SMALL ("Variable Flags", "references/variable_flags", "Radix can't be below 2"),
    UNKNOWN_LIST_VARIABLE_FLAG ("Variable Flags", "references/variable_flags", "Unknown List Variable Flag: §e"),
    UNKNOWN_THEME_FLAG ("Theming", "references/theming", "Unknown Theme Option or Value"),
    UNKNOWN_COLOR ("Theming", "references/theming", "Unknown Color: §e"),
    UNKNOWN_CROSSHAIR ("Theming", "references/theming", "Unknown Crosshair: §e"),
    UNKNOWN_CHART ("Theming", "references/theming", "Unknown Debug Chart: §e"),
    UNKNOWN_HUD_ELEMENT ("CHANGE ME", "CHANGE ME", "Unknown Vanilla Hud Element: §e"),
    ILLEGAL_GLOBAL_THEME_FLAG("Theming", "references/theming", "This theme option is global-only"),
    LINE_SPACING_AND_PADDING ("Theming", "references/theming", "LineSpacing can't be used with Padding, adjust padding instead"),
    ZERO_SCALE ("Theming", "references/theming", "Scale can not be 0"),

    INVALID_TIME_FORMAT ("Time Formatting", "references/real_time", "Invalid Time Format: "),
    UNKNOWN_STATISTIC ("Statistics", "references/stats", "Unknown Statistic: §e"),
    UNKNOWN_ITEM_ID (null, null, "Unknown Item ID: §e"),

    UNKNOWN_SLOT ("Slots", "references/item_slots", "Unknown Slot: §e"),
    UNAVAILABLE_SLOT ("Slots", "references/item_slots", "The §e" + "§r slot is not available for players"),
    UNKNOWN_MOD ("CHANGE ME", "references/CHANGE ME", "Mod not installed: §e"),
    UNKNOWN_RESOURCE_PACK ("CHANGE ME", "references/CHANGE ME", "Resource Pack not installed: §e"),
    UNKNOWN_DATA_PACK ("CHANGE ME", "references/CHANGE ME", "Datapack not installed: §e"),

    UNKNOWN_ATTRIBUTE ("CHANGE ME", "references/CHANGE ME", "Unknown Attribute: §e"),
    UNKNOWN_EFFECT_ID ("CHANGE ME", "references/CHANGE ME", "Unknown Effect ID: §e"),
    UNKNOWN_ITEM_METHOD("Item Properties", "variables#items", "Unknown Item Method: §e"),
    UNKNOWN_ATTRIBUTE_METHOD("CHANGE ME", "references/CHANGE ME", "Unknown Attribute Method: §e"),
    UNKNOWN_BLOCK_PROPERTY_METHOD("CHANGE ME", "references/CHANGE ME", "Unknown Blockstate Property Method: §e"),
    UNKNOWN_EFFECT_METHOD("CHANGE ME", "references/CHANGE ME", "Unknown Effect Method: §e"),
    UNKNOWN_TEAM_METHOD("CHANGE ME", "references/CHANGE ME", "Unknown Team Method: §e"),
    UNKNOWN_OBJECTIVE_METHOD("CHANGE ME", "references/CHANGE ME", "Unknown Objective Method: §e"),
    UNKNOWN_BOSSBAR_METHOD("CHANGE ME", "references/CHANGE ME", "Unknown Bossbar Method: §e"),
    UNKNOWN_SCORE_METHOD("CHANGE ME", "references/CHANGE ME", "Unknown Score Method: §e"),
    UNKNOWN_MOD_METHOD("CHANGE ME", "references/CHANGE ME", "Unknown Mod Method: §e"),
    UNKNOWN_PACK_METHOD("CHANGE ME", "references/CHANGE ME", "Unknown Pack Method: §e"),
    UNKNOWN_PROFILER_TIMING_PROPERTY("CHANGE ME", "references/CHANGE ME", "Unknown Profiler Time Property: §e"),
    //TODO FIX ME
    UNKNOWN_ICON ("Icons", "references/icons", "Unknown item/texture: §e"),

    UNKNOWN_SETTING ("Settings", "references/settings", "Unknown Setting: §e"),
    UNKNOWN_KEYBIND("Settings", "references/settings", "Unknown Keybind: §e"),
    UNKNOWN_SOUND_CATEGORY ("Settings", "references/settings", "Unknown Sound Category: §e"),

    LIST_NOT_STARTED ("CHANGE ME", "CHANGE ME", "No =for: §olist§r= to end"),
    LIST_NOT_ENDED ("CHANGE ME", "CHANGE ME", "Missing =endfor="),
    CONDITIONAL_NOT_STARTED ("Conditionals", "conditionals", "No =if: §ocond§r= to "),
    CONDITIONAL_NOT_ENDED ("Conditionals", "conditionals", "Missing =endif="),
    CONDITIONAL_UNEXPECTED_VALUE ("Conditionals", "conditionals", "Unexpected Value: §e"),
    CONDITIONAL_WRONG_NUMBER_OF_TOKENS ("Conditionals", "conditionals", "Expected 4 tokens, found §e"),
    MALFORMED_CONDITIONAL ("Conditionals", "conditionals", "Malformed conditional: §e"),
    EMPTY_SECTION("Conditionals", "conditionals", "Empty section"),
    MALFORMED_LIST ("CHANGE ME", "CHANGE ME", "Malformed list variable: §e"),
    MALFORMED_BAR ("CHANGE ME", "CHANGE ME", "Malformed bar variable: §e"),
    MALFORMED_LOOP ("CHANGE ME", "CHANGE ME", "Malformed loop: §e"),
    MALFORMED_TIMER ("CHANGE ME", "CHANGE ME", "Malformed timer: §e"),
    EMPTY_TOGGLE ("CHANGE ME", "CHANGE ME", "No toggle name"),
    UNKNOWN_KEY("CHANGE ME", "CHANGE ME", "Invalid key name: §e"),

    NOT_A_WHOLE_NUMBER (null, null, "Not a whole number: §e"),

    REQUIRES_MODMENU ("Get Mod Menu", "https://modrinth.com/mod/modmenu", "Requires the mod §aMod Menu");

    public final String message;
    public final MutableText linkText;
    public final String link;

    ErrorType(String linkText, String link, String msg) {
        this.message = msg;
        this.linkText = linkText == null ? null : Text.literal(linkText).formatted(Formatting.AQUA, Formatting.UNDERLINE);
        this.link = "https://customhud.dev/v3/" + link;
    }
}
