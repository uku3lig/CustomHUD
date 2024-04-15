package com.minenash.customhud.HudElements.icon;

import com.google.common.collect.Maps;
import com.google.common.hash.Hashing;
import com.minenash.customhud.CustomHud;
import com.minenash.customhud.complex.ListManager;
import com.minenash.customhud.data.Flags;
import com.minenash.customhud.render.RenderPiece;
import com.terraformersmc.modmenu.util.mod.Mod;
import com.terraformersmc.modmenu.util.mod.fabric.FabricIconHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.InputSupplier;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import static com.minenash.customhud.CustomHud.CLIENT;

public class PackIconElement extends IconElement {

    private final Map<String, Identifier> iconTextures = Maps.newHashMap();
    private final Supplier<ResourcePackProfile> supplier;
    public PackIconElement(Supplier<ResourcePackProfile> supplier, Flags flags) {
        super(flags, 11);
        this.supplier = supplier;
    }

    @Override
    public void render(DrawContext context, RenderPiece piece) {
        ResourcePackProfile pack = supplier == ListManager.SUPPLIER ? (ResourcePackProfile) piece.value : supplier.get();
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.translate(piece.x + shiftX, piece.y + shiftY - 2, 0);
        if (!referenceCorner)
            matrices.translate(0, -(11*scale-11)/2F, 0);
//        matrices.scale(scale, scale, 0);
        rotate(matrices, width, width);

        context.drawTexture(getPackIconTexture(pack), 0, 0, 0, 0, width, width, width, width);
        matrices.pop();
    }

    private Identifier getPackIconTexture(ResourcePackProfile resourcePackProfile) {
        return this.iconTextures.computeIfAbsent(resourcePackProfile.getName(), (profileName) -> loadPackIcon(CLIENT.getTextureManager(), resourcePackProfile));
    }

    private static final Identifier UNKNOWN_PACK = new Identifier("textures/misc/unknown_pack.png");
    public static Identifier loadPackIcon(TextureManager textureManager, ResourcePackProfile resourcePackProfile) {
        try (ResourcePack resourcePack = resourcePackProfile.createResourcePack()) {
            InputSupplier<InputStream> inputSupplier = resourcePack.openRoot("pack.png");
            if (inputSupplier == null)
                return UNKNOWN_PACK;

            String name = resourcePackProfile.getName();
            String safeName = Util.replaceInvalidChars(name, Identifier::isPathCharacterValid);
            Identifier identifier = new Identifier("minecraft", "pack/" + safeName + "/" + Hashing.sha1().hashUnencodedChars(name) + "/icon");

            try (InputStream inputStream = inputSupplier.get()) {
                NativeImage nativeImage = NativeImage.read(inputStream);
                textureManager.registerTexture(identifier, new NativeImageBackedTexture(nativeImage));
                return identifier;
            }
        } catch (Exception var14) {
            CustomHud.LOGGER.warn("Failed to load icon from pack {}", resourcePackProfile.getName(), var14);
            return UNKNOWN_PACK;
        }
    }

}
