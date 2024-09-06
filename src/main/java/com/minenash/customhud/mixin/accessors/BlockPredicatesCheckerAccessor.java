package com.minenash.customhud.mixin.accessors;

import net.minecraft.item.BlockPredicatesChecker;
import net.minecraft.predicate.BlockPredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(BlockPredicatesChecker.class)
public interface BlockPredicatesCheckerAccessor {

    @Accessor List<BlockPredicate> getPredicates();

}
