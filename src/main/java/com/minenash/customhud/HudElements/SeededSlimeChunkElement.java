package com.minenash.customhud.HudElements;

import com.minenash.customhud.HudElements.supplier.BooleanSupplierElement;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.world.gen.GeneratorOptions;

import java.util.OptionalLong;

public class SeededSlimeChunkElement extends BooleanSupplierElement {

    public SeededSlimeChunkElement(String seedStr) {
        super( () -> {
            long seed = GeneratorOptions.parseSeed(seedStr).getAsLong();
            return ChunkRandom.getSlimeRandom(blockPos().getX() >> 4, blockPos().getZ() >> 4, seed, 987234911L).nextInt(10) == 0;
        } );
    }

}
