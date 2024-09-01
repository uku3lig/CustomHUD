package com.minenash.customhud.mixin.fonts;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.minenash.customhud.render.CustomHudRenderer3;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.CharacterVisitor;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TextRenderer.class)
public class TextRendererMixin {

    @ModifyArg(method = "drawLayer(Ljava/lang/String;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;II)F", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/TextVisitFactory;visitFormatted(Ljava/lang/String;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z"), index = 1)
    public Style changeStyle(Style old) {
        return CustomHudRenderer3.font == null ? old : old.withFont(CustomHudRenderer3.font);
    }

//    @WrapOperation(method = "drawLayer(Lnet/minecraft/text/OrderedText;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;II)F", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/OrderedText;accept(Lnet/minecraft/text/CharacterVisitor;)Z"))
//    public boolean test(OrderedText instance, CharacterVisitor characterVisitor, Operation<Boolean> original) {
//        instance.;
//    }
}
