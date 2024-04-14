package com.minenash.customhud.HudElements.text;

import com.minenash.customhud.data.Flags;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.function.Supplier;

import static com.minenash.customhud.CustomHud.CLIENT;

public class TitleMsgElement extends TextSupplierElement {
    public TitleMsgElement(Supplier<Text> supplier, Flags flags) { super(supplier, flags); }

    @Override
    public int getColor(int current) {
        int l = 0;
        if (CLIENT.inGameHud.titleRemainTicks > CLIENT.inGameHud.titleFadeOutTicks + CLIENT.inGameHud.titleStayTicks) {
            float o = (float)(CLIENT.inGameHud.titleFadeInTicks + CLIENT.inGameHud.titleStayTicks + CLIENT.inGameHud.titleFadeOutTicks) - CLIENT.inGameHud.titleRemainTicks;
            l = (int)(o * 255.0F / CLIENT.inGameHud.titleFadeInTicks);
        }

        if (CLIENT.inGameHud.titleRemainTicks <= CLIENT.inGameHud.titleFadeOutTicks)
            l = (int)(CLIENT.inGameHud.titleRemainTicks * 255.0F / CLIENT.inGameHud.titleFadeOutTicks);

        return (current & 0xFFFFFF) | MathHelper.clamp(l, 0, 255) << 24 & 0xFF000000;
    }
}