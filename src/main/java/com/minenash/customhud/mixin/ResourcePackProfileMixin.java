package com.minenash.customhud.mixin;

import com.minenash.customhud.ducks.ResourcePackProfileMetadataDuck;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.metadata.PackFeatureSetMetadata;
import net.minecraft.resource.metadata.PackResourceMetadata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ResourcePackProfile.class)
public class ResourcePackProfileMixin {

    @Unique private static int temp = 0;

    @Inject(method = "loadMetadata", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/resource/ResourcePack;parseMetadata(Lnet/minecraft/resource/metadata/ResourceMetadataReader;)Ljava/lang/Object;"))
    private static void setPackVersionPart1(String name, ResourcePackProfile.PackFactory packFactory, int currentPackFormat, CallbackInfoReturnable<ResourcePackProfile.Metadata> cir, ResourcePack resourcePack, PackResourceMetadata packResourceMetadata) {
        temp = packResourceMetadata.packFormat();
    }

    @Inject(method = "loadMetadata", at = @At("RETURN"))
    private static void setPackVersionPart2(String name, ResourcePackProfile.PackFactory packFactory, int currentPackFormat, CallbackInfoReturnable<ResourcePackProfile.Metadata> cir) {
        ResourcePackProfileMetadataDuck value = ((ResourcePackProfileMetadataDuck)(Object)cir.getReturnValue());
        if (value != null)
            value.customhud$setPackVersion( temp );
    }
}
