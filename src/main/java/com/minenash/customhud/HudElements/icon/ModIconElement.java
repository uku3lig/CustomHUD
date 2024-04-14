package com.minenash.customhud.HudElements.icon;

import com.minenash.customhud.complex.ListManager;
import com.minenash.customhud.data.Flags;
import com.minenash.customhud.render.RenderPiece;
import com.terraformersmc.modmenu.util.mod.Mod;
import com.terraformersmc.modmenu.util.mod.fabric.FabricIconHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.minenash.customhud.CustomHud.CLIENT;

public class ModIconElement extends IconElement {

    private static Set<Identifier> cached = new HashSet<>();
    private static FabricIconHandler handler = new FabricIconHandler();

    public ModIconElement(Flags flags) {
        super(flags, 11);
        cached.clear();
        handler.close();
        handler = new FabricIconHandler();
    }

    @Override
    public void render(DrawContext context, RenderPiece piece) {
        Mod mod = (Mod) piece.value;
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
        matrices.translate(piece.x + shiftX, piece.y + shiftY, 0);
        if (!referenceCorner)
            matrices.translate(0, -(5*scale-5)/2, 0);
        matrices.scale(scale, scale, 0);
        rotate(matrices, width, width);

        context.drawTexture(id, 0, -1, 0, 0, width, width, width, width);
        matrices.pop();
    }

}
