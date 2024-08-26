package com.minenash.customhud.data;

public enum HudHiddenBehavior {
    SHOW, SHOW_IF_SCREEN, HIDE;

    static HudHiddenBehavior parse(String name) {
        switch (name) {
            case "show": return SHOW;
            case "showifscreen": return SHOW_IF_SCREEN;
            case "hide": return HIDE;
            default: return null;
        }
    }
}
