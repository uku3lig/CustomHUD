package com.minenash.customhud.HudElements.supplier;

import com.minenash.customhud.complex.ComplexData;
import com.minenash.customhud.data.Flags;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

import static com.minenash.customhud.CustomHud.CLIENT;

public class SpecialIdSupplierElement extends IdentifierSupplierElement {

    public static final Entry TARGET_BLOCK_ID = of( () -> Registries.BLOCK.getId(ComplexData.targetBlock.getBlock()),
            () -> Block.getRawIdFromState(ComplexData.targetBlock),
            () -> !ComplexData.targetBlock.isAir());

    public static final Entry TARGET_FLUID_ID = of( () -> Registries.FLUID.getId(ComplexData.targetFluid.getFluid()),
            () -> Fluid.STATE_IDS.getRawId(ComplexData.targetFluid),
            () -> !ComplexData.targetFluid.isEmpty());

    @Deprecated
    public static final Entry ITEM_ID = of( () -> Registries.ITEM.getId(CLIENT.player.getMainHandStack().getItem()),
            () -> Item.getRawId(CLIENT.player.getMainHandStack().getItem()),
            () -> !CLIENT.player.getMainHandStack().isEmpty());

    @Deprecated
    public static final Entry OFFHAND_ITEM_ID = of( () -> Registries.ITEM.getId(CLIENT.player.getOffHandStack().getItem()),
            () -> Item.getRawId(CLIENT.player.getOffHandStack().getItem()),
            () -> !CLIENT.player.getOffHandStack().isEmpty());


    public record Entry(Supplier<Identifier> identifierSupplier, Supplier<Number> numberSupplier, Supplier<Boolean> booleanSupplier) {}
    public static Entry of(Supplier<Identifier> identifierSupplier, Supplier<Number> numberSupplier, Supplier<Boolean> booleanSupplier) {
        return new Entry(identifierSupplier, numberSupplier, booleanSupplier);
    }

    private final Entry entry;

    public SpecialIdSupplierElement(Entry entry, Flags flags) {
        super(entry.identifierSupplier, flags);
        this.entry = entry;
    }


    @Override
    public Number getNumber() {
        return sanitize(entry.numberSupplier, Double.NaN);
    }

    @Override
    public boolean getBoolean() {
        return sanitize(entry.booleanSupplier, false);
    }

}
