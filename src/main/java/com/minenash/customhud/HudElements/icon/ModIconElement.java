package com.minenash.customhud.HudElements.icon;

import com.minenash.customhud.complex.ListManager;
import com.minenash.customhud.data.Flags;
import com.minenash.customhud.render.RenderPiece;
import com.terraformersmc.modmenu.util.mod.Mod;
import com.terraformersmc.modmenu.util.mod.fabric.FabricIconHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import static com.minenash.customhud.CustomHud.CLIENT;

public class ModIconElement extends IconElement {

    private static Set<Identifier> cached = new HashSet<>();
    private static FabricIconHandler handler = new FabricIconHandler();
    public final Supplier<Mod> supplier;

    public ModIconElement(Supplier<Mod> supplier, Flags flags) {
        super(flags, 11);
        this.supplier = supplier;
        cached.clear();
        handler.close();
        handler = new FabricIconHandler();
    }

    @Override
    public void render(DrawContext context, RenderPiece piece) {
        Mod mod = supplier == ListManager.SUPPLIER ? (Mod) piece.value : supplier.get();
        int size = (int)(10 * scale * CLIENT.options.getGuiScale().getValue());

        Identifier id = new Identifier("custom_hud", size + "___" + mod.getId());

        if (!cached.contains(id)) {
            try {
                NativeImageBackedTexture icon = mod.getIcon(handler, size);
                CLIENT.getTextureManager().registerTexture(id, icon);
                cached.add(id);
            }
            catch (Exception e) {
                id = new Identifier("textures/misc/unknown_pack.png");
            }
        }

        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.translate(piece.x + shiftX, piece.y + shiftY - 2, 0);
        if (!referenceCorner)
            matrices.translate(0, -(11*scale-11)/2F, 0);
//        matrices.scale(scale, scale, 0);
        int w = (int) (11 * scale);
        rotate(matrices, w, w);


        context.drawTexture(id, 0, 0, 0, 0, w, w, w, w);
        matrices.pop();
    }

}
