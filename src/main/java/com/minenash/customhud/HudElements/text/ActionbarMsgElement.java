package com.minenash.customhud.HudElements.text;

import com.minenash.customhud.data.Flags;
import net.minecraft.util.math.MathHelper;

import static com.minenash.customhud.CustomHud.CLIENT;

public class ActionbarMsgElement extends TextSupplierElement {
    public ActionbarMsgElement(Flags flags) { super(ACTIONBAR_MSG, flags); }

    @Override
    public int getColor(int current) {
        float h = (float)CLIENT.inGameHud.overlayRemaining;
        int k = !CLIENT.inGameHud.overlayTinted ? (current & 0xFFFFFF) : MathHelper.hsvToRgb(h / 50.0F, 0.7F, 0.6F) & 0xFFFFFF;
        int m = Math.min((int)(h * 255.0F / 20.0F), 255) << 24 & 0xFF000000;

        return k | m;
    }
}