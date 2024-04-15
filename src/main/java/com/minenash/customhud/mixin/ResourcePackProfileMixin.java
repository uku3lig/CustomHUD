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

    @Inject(method = "loadMetadata", locals = LocalCapture.CAPTURE_FAILSOFT, at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/metadata/PackFeatureSetMetadata;flags()Lnet/minecraft/resource/featuretoggle/FeatureSet;"))
    private static void setPackVersionPart1(String name, ResourcePackProfile.PackFactory packFactory, int currentPackFormat, CallbackInfoReturnable<ResourcePackProfile.Metadata> cir, ResourcePack resourcePack, PackResourceMetadata packResourceMetadata, PackFeatureSetMetadata packFeatureSetMetadata) {
        temp = packResourceMetadata.packFormat();
    }

    @Inject(method = "loadMetadata", at = @At("RETURN"))
    private static void setPackVersionPart2(String name, ResourcePackProfile.PackFactory packFactory, int currentPackFormat, CallbackInfoReturnable<ResourcePackProfile.Metadata> cir) {
        var value = ((ResourcePackProfileMetadataDuck)(Object)cir.getReturnValue());
        if (value != null)
            value.customhud$setPackVersion( temp );
    }
}
