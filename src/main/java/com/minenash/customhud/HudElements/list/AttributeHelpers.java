package com.minenash.customhud.HudElements.list;

import com.minenash.customhud.complex.ComplexData;
import com.minenash.customhud.mixin.accessors.AttributeContainerAccessor;
import com.minenash.customhud.mixin.accessors.DefaultAttributeContainerAccessor;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.command.argument.ItemSlotArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

import static com.minenash.customhud.CustomHud.CLIENT;
import static net.minecraft.item.ItemStack.DISPLAY_KEY;
import static net.minecraft.item.ItemStack.LORE_KEY;

public class AttributeHelpers {

    public static final Function<String, EntityAttribute> ENTITY_ATTR_READER = (src) -> Registries.ATTRIBUTE.get(Identifier.tryParse(src));
    public static final Function<String, ResourcePackProfile> DATA_PACK_READER = (src) ->
        CLIENT.getServer() == null ? null : CLIENT.getServer().getDataPackManager().getProfile(src);

    private static final Pattern TIMING_PERIOD_TO_SPECIAL = Pattern.compile("(?<!\\\\)\\.");
    public static final Function<String, String> PROFILER_TIMING_READER = (src) -> {
        src = TIMING_PERIOD_TO_SPECIAL.matcher(src).replaceAll("\u001e");
        if (!src.startsWith("root\u001e"))
            src = "root\u001e" + src;
        return src;
    };

    public static final Function<String, Integer> SLOT_READER = (src) -> {
        if (src.isBlank())
            return null;
        try {
            return ItemSlotArgumentType.itemSlot().parse(new StringReader(switch (src) {
                case "head", "chest", "legs", "feet" -> "armor." + src;
                case "mainhand", "offhand" -> "weapon." + src;
                case "main", "off" -> "weapon." + src + "hand";
                default -> {
                    if (src.length() < 2) yield src;
                    if (src.charAt(0) == 'h' && src.charAt(1) != 'o') yield "hotbar." + src.substring(1);
                    if (src.charAt(0) == 'i' && src.charAt(1) != 'n') yield "inventory." + src.substring(1);
                    yield src;
                }
            }));
        } catch (CommandSyntaxException e) {
            return null;
        }
    };

    public record ReceivedPower(Direction direction, int power, int strongPower) {}

    public static PlayerListEntry getPlayer(String src) {
        PlayerListEntry p = CLIENT.getNetworkHandler().getPlayerListEntry(src);
        if (p != null)
            return p;
        try {
            return CLIENT.getNetworkHandler().getPlayerListEntry(UUID.fromString(src));
        }
        catch (Exception ignored) {}
        return null;
    }

    public static Entity getFullEntity(Entity entity) {
        return CLIENT.getServer() == null || entity == null? entity :
                CLIENT.getServer().getWorld(entity.getWorld().getRegistryKey()).getEntity(entity.getUuid());
    }
    public static EntityAttributeInstance getEntityAttr(Entity entity, EntityAttribute attribute) {
        Entity e = getFullEntity(entity);
        if (!(e instanceof LivingEntity le)) return null;
        return le.getAttributeInstance(attribute);
    }

    public static List<EntityAttributeInstance> getEntityAttributes(Entity entity) {
        entity = getFullEntity(entity);
        if (!(entity instanceof LivingEntity le) ) return Collections.EMPTY_LIST;
        AttributeContainerAccessor container = (AttributeContainerAccessor) le.getAttributes();
        Map<EntityAttribute, EntityAttributeInstance> instances = new HashMap<>(((DefaultAttributeContainerAccessor)container.getFallback()).getInstances());
        instances.putAll(container.getCustom());
        return Arrays.asList( (EntityAttributeInstance[]) (entity.getWorld().isClient ?
                instances.values().stream().filter(a -> a.getAttribute().isTracked()) : instances.values().stream())
                .sorted(Comparator.comparing(a -> I18n.translate(a.getAttribute().getTranslationKey()))).toArray() );
    }

    public record ItemAttribute(EntityAttribute attribute, EntityAttributeModifier modifier, String slot) {}
    public static List<ItemAttribute> getItemStackAttributes(ItemStack stack) {
        if (!stack.hasNbt() || !stack.getNbt().contains("AttributeModifiers", 9)) {
            List<ItemAttribute> attributes = new ArrayList<>();
            for (EquipmentSlot slot : EquipmentSlot.values())
                for (var entry : stack.getItem().getAttributeModifiers(slot).entries())
                    attributes.add( new ItemAttribute(entry.getKey(), entry.getValue(), slot.getName()) );
            return attributes;
        }

        List<ItemAttribute> attributes = new ArrayList<>();
        NbtList nbtList = stack.getNbt().getList("AttributeModifiers", 10);

        for(int i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompound(i);

            EntityAttribute attribute = Registries.ATTRIBUTE.get(Identifier.tryParse(nbtCompound.getString("AttributeName")));
            if (attribute == null) continue;
            EntityAttributeModifier modifier = EntityAttributeModifier.fromNbt(nbtCompound);
            if (modifier != null
                    && modifier.getId().getLeastSignificantBits() != 0L
                    && modifier.getId().getMostSignificantBits() != 0L) {
                String slot = nbtCompound.getString("Slot");
                if (slot.isEmpty()) slot = "all";
                attributes.add( new ItemAttribute(attribute, modifier, slot) );
            }
        }

        return attributes;
    }

    public static List<Text> getLore(ItemStack stack) {
        List<Text> lines = new ArrayList<>();
        if (stack.hasNbt()) {
            if (stack.getNbt().contains(DISPLAY_KEY, NbtElement.COMPOUND_TYPE)) {
                NbtCompound nbtCompound = stack.getNbt().getCompound(DISPLAY_KEY);
                if (nbtCompound.getType(LORE_KEY) == NbtElement.LIST_TYPE) {
                    NbtList nbtList = nbtCompound.getList(LORE_KEY, NbtElement.STRING_TYPE);
                    for (int j = 0; j < nbtList.size(); ++j) {
                        try {
                            MutableText mutableText2 = Text.Serialization.fromJson(nbtList.getString(j));
                            if (mutableText2 == null) continue;
                            lines.add(mutableText2);
                        }
                        catch (Exception ignored) {}
                    }
                }
            }
        }
        return lines;
    }

    public static List<Block> getCanX(ItemStack stack, String tag) {
        NbtCompound nbtCompound = stack.getNbt();

        if (nbtCompound == null || !nbtCompound.contains(tag, NbtElement.LIST_TYPE))
            return Collections.EMPTY_LIST;

        List<Block> items = new ArrayList<>();
        NbtList nbtList = nbtCompound.getList(tag, NbtElement.STRING_TYPE);

        for(int i = 0; i < nbtList.size(); ++i) {
            String string = nbtList.getString(i);

           Block block = Registries.BLOCK.get(Identifier.tryParse(string));
           if (block != Blocks.AIR)
               items.add(block);
        }

        return items;
    }

    public static List<String> getHideFlagStrings(ItemStack stack, boolean shown) {
        List<String> sections = new ArrayList<>();
        int flags = stack.getHideFlags();
        for (ItemStack.TooltipSection flag : ItemStack.TooltipSection.values())
            if (shown == ((flags & flag.getFlag()) == 0))
                sections.add(flag.name().toLowerCase());
        return sections;
    }

    public static List<ItemStack> compactItems(List<ItemStack> stacks) {
        List<ItemStack> compact = new ArrayList<>();
        outer:
        for (ItemStack stack : stacks) {
            if (stack.isEmpty()) continue;
            for (ItemStack cStack : compact) {
                if (ItemStack.canCombine(stack, cStack)) {
                    cStack.setCount(cStack.getCount() + stack.getCount());
                    continue outer;
                }
            }
            compact.add(stack.copy());
        }
        return compact;
    }

    public static List<ItemStack> getItemItems(ItemStack stack, boolean returnStack) {
        NbtCompound nbt = stack.getNbt();
        if (stack.isEmpty() || nbt == null)
            return returnStack ? Collections.singletonList(stack) : Collections.EMPTY_LIST;
        if (nbt.contains("Items", NbtElement.LIST_TYPE))
            return getItemItemsInternal(stack, nbt.getList("Items", NbtElement.COMPOUND_TYPE) );
        if (!nbt.contains("BlockEntityTag"))
            return returnStack ? Collections.singletonList(stack) : Collections.EMPTY_LIST;
        nbt = nbt.getCompound("BlockEntityTag");
        if (!nbt.contains("Items", NbtElement.LIST_TYPE))
            return returnStack ? Collections.singletonList(stack) : Collections.EMPTY_LIST;
        return getItemItemsInternal(stack, nbt.getList("Items", NbtElement.COMPOUND_TYPE) );
    }
    private static List<ItemStack> getItemItemsInternal(ItemStack stack, NbtList list) {
        List<ItemStack> items = new ArrayList<>(list.size());

        for(int i = 0; i < list.size(); ++i) {
            List<ItemStack> inner = getItemItems(ItemStack.fromNbt(list.getCompound(i)), true);
            for (ItemStack is : inner)
                is.setCount(is.getCount() * stack.getCount());
            items.addAll(inner);
        }
        return items;
    }

    public static Scoreboard scoreboard() {
        return CLIENT.getServer() != null ? CLIENT.getServer().getScoreboard() : CLIENT.world.getScoreboard();
    }
    public static boolean entryOnline(String entry) {
        if (null != (CLIENT.getServer() != null ? CLIENT.getServer().getPlayerManager().getPlayer(entry) : CLIENT.getNetworkHandler().getPlayerListEntry(entry)))
            return true;
        if (ComplexData.serverWorld == null)
            return false;
        try {
            return ComplexData.serverWorld.entityManager.has(UUID.fromString(entry));
        }
        catch (Exception ignored) {}
        return false;
    }

    public static List<?> bossbars(boolean all) {
        if (CLIENT.getServer() == null)
            return Arrays.asList(CLIENT.inGameHud.getBossBarHud().bossBars.entrySet().toArray());

        List<BossBar> serverBossbars = new ArrayList<>();
        serverBossbars.addAll(CLIENT.getServer().getBossBarManager().commandBossBars.values());
        serverBossbars.addAll(ComplexData.bossbars.values());

        if (all)
            return serverBossbars;

        Set<UUID> client = CLIENT.inGameHud.getBossBarHud().bossBars.keySet();
        return Arrays.asList( serverBossbars.stream().filter(bar -> client.contains(bar.getUuid())).toArray() );
    }

    public static BossBar getBossBar(String input) {
        boolean client = CLIENT.getServer() == null;
        try {
            UUID uuid = UUID.fromString(input);
            if (client)
                return CLIENT.inGameHud.getBossBarHud().bossBars.get(uuid);
            for (BossBar bar : CLIENT.getServer().getBossBarManager().commandBossBars.values())
                if (bar.getUuid() == uuid)
                    return bar;
            BossBar bb = ComplexData.bossbars.get(uuid);
            if (bb != null)
                return bb;
        }
        catch (Exception ignored) {}

        if (client) {
            for (BossBar bar : CLIENT.inGameHud.getBossBarHud().bossBars.values())
                if (bar.getName().getString().equalsIgnoreCase(input))
                    return bar;
        }
        else {
            BossBar bar = CLIENT.getServer().getBossBarManager().get(Identifier.tryParse(input));
            if (bar != null)
                return bar;
            for (BossBar bar2 : CLIENT.getServer().getBossBarManager().commandBossBars.values())
                if (bar2.getName().getString().equalsIgnoreCase(input))
                    return bar2;
            for (BossBar bar2 : ComplexData.bossbars.values())
                if (bar2.getName().getString().equalsIgnoreCase(input))
                    return bar2;
        }
        return null;
    }

    public static int getBossBarColor(BossBar bar) {
        return switch (bar.getColor()) {
            case PINK -> 0xEC00B8;
            case BLUE -> 0x00B7EC;
            case RED -> 0xEC3500;
            case GREEN -> 0x1DEC00;
            case YELLOW -> 0xE9EC00;
            case PURPLE -> 0x7B00EC;
            case WHITE -> 0xECECEC;
        };
    }

    public static double getRelativeYaw(Vec3d player, Vec3d other) {
        return MathHelper.wrapDegrees(CLIENT.player.getYaw() - Math.toDegrees( MathHelper.atan2(-(other.getX() - player.getX()), other.getZ() - player.getZ()) ));
    }
    public static double getRelativePitch(Vec3d player, Vec3d other) {
        double xDist = other.getX() - player.getX();
        double zDist = other.getZ() - player.getZ();
        return MathHelper.wrapDegrees(CLIENT.player.getPitch() + Math.toDegrees( MathHelper.atan2(other.getY() - player.getY(), Math.sqrt(xDist*xDist + zDist*zDist ) )));
    }

}
