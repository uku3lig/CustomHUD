package com.minenash.customhud.render;

import net.minecraft.util.Identifier;

public class RenderPiece {

    public final Object element;
    public final Object value;
    public final Identifier font;
    public final int color;
    public final boolean shadow;
    public final int y;
    public int x;

    public RenderPiece(Object element, Object value, Identifier font, int x, int y, int color, boolean shadow) {
        this.element = element;
        this.value = value;
        this.font = font;
        this.x = x;
        this.y = y;
        this.color = color;
        this.shadow = shadow;
    }

}
