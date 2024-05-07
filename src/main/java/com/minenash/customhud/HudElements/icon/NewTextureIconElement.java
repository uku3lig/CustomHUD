package com.minenash.customhud.HudElements.icon;

import com.minenash.customhud.CustomHud;
import com.minenash.customhud.conditionals.ExpressionParser;
import com.minenash.customhud.conditionals.Operation;
import com.minenash.customhud.data.Flags;
import com.minenash.customhud.render.RenderPiece;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Supplier;

public class NewTextureIconElement extends IconElement {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final Identifier TEXTURE_NOT_FOUND = new Identifier("textures/item/barrier.png");

    private final Identifier texture;
    private final Operation u;
    private final Operation v;
    private final int textureWidth;
    private final int textureHeight;
    private final Operation regionWidth;
    private final Operation regionHeight;
    private final int height;
    private final int yOffset;
    private final int iconWidth;

    private final boolean iconAvailable;


    public NewTextureIconElement(Identifier texture, Operation u, Operation v, Operation w, Operation h, Flags flags) {
        super(flags, 0);
        this.u = u != null ? u : new Operation.Literal(0);
        this.v = v != null ? v : new Operation.Literal(0);

        NativeImage img = null;
        try {
            Optional<Resource> resource = client.getResourceManager().getResource(texture);
            if (resource.isPresent())
                img = NativeImage.read(resource.get().getInputStream());
        }
        catch (IOException e) { CustomHud.logStackTrace(e); }


        iconAvailable = img != null;
        this.texture = iconAvailable ? texture : TEXTURE_NOT_FOUND;

        textureWidth = iconAvailable ? img.getWidth() : 16;
        textureHeight = iconAvailable ? img.getHeight() : 16;
        regionWidth = w != null ? w : new Operation.BiMathOperation(new Operation.Literal(textureWidth), this.u, ExpressionParser.MathOperator.SUBTRACT);
        regionHeight = h != null ? h : new Operation.BiMathOperation(new Operation.Literal(textureHeight), this.v, ExpressionParser.MathOperator.SUBTRACT);;

        height = (int) (11 * flags.scale);
        yOffset = referenceCorner ? 0 : (int) ((height*scale-height)/(scale*2));
        iconWidth = flags.iconWidth;

    }

    @Override
    public Number getNumber() {
        return 0;
    }

    @Override
    public boolean getBoolean() {
        return true;
    }

    @Override
    public int getTextWidth() {
        return iconWidth != -1 ? iconWidth : (int) (height * (regionWidth.getValue()/regionHeight.getValue()));
    }

    public boolean isIconAvailable() {
        return iconAvailable;
    }

    @Override
    public void render(DrawContext context, RenderPiece piece) {
        int width = (int) (height * (regionWidth.getValue()/regionHeight.getValue()));
        if (width == 0)
            return;
        context.getMatrices().push();
        context.getMatrices().translate(piece.x+shiftX, piece.y+shiftY-yOffset-2, 0);
        rotate(context.getMatrices(), width, height);
        context.drawTexture(texture, 0, 0, width, height, get(u), get(v), get(regionWidth), get(regionHeight), textureWidth, textureHeight);
        context.getMatrices().pop();
    }

    private int get(Operation op) {
        return (int) op.getValue();
    }


}
