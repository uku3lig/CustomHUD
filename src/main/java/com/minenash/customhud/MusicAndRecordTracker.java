package com.minenash.customhud;

import com.minenash.customhud.mixin.music.MinecraftClientAccess;
import com.minenash.customhud.mixin.music.MusicTrackerAccess;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.JukeboxPlayableComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Optional;

public class MusicAndRecordTracker {

    public static boolean isMusicPlaying = false;
    public static String musicId = "";
    public static String musicName = "";

    public static boolean isRecordPlaying = false;
    public static SoundInstance recordInstance = null;
    public static String recordId = "";
    public static String recordName = "";
    public static int recordLength = 0;
    public static int recordElapsed = 0;
    public static ItemStack recordIcon = null;

    private final static MinecraftClient client = MinecraftClient.getInstance();

    public static void tick() {
        isRecordPlaying = recordInstance != null && client.getSoundManager().isPlaying(recordInstance);
        if (isRecordPlaying)
            recordElapsed++;

        SoundInstance music = ((MusicTrackerAccess)((MinecraftClientAccess)client).getMusicTracker()).getCurrent();
        isMusicPlaying =  client.getSoundManager().isPlaying(music);
        if (music != null) {
            musicId = music.getSound().getIdentifier().toString();
            musicName = WordUtils.capitalize(musicId.substring(musicId.lastIndexOf('/')+1).replace("_", " ").replaceAll("(\\d+)", " $1"));
        }
    }

    public static void setRecord(RegistryEntry<JukeboxSong> song, SoundInstance instance, BlockPos jukeboxPos) {
        recordElapsed = 0;
        if (song == null) {
            recordInstance = null;
            return;
        }

        recordInstance = instance;
        JukeboxSong jbs = song.value();
        recordId = song.hasKeyAndValue() ? song.getKey().get().getValue().toString() : "unknown";
        recordName = jbs.description().getString();
        recordLength = jbs.getLengthInTicks();

        if (client.getServer() != null && client.world != null) {
            BlockEntity state = client.getServer().getWorld(client.world.getRegistryKey()).getWorldChunk(jukeboxPos).getBlockEntity(jukeboxPos, WorldChunk.CreationType.IMMEDIATE);
            recordIcon = state instanceof JukeboxBlockEntity jbe ? jbe.getStack() : ItemStack.EMPTY;
        }
        if (recordIcon == ItemStack.EMPTY) {
            recordIcon = new ItemStack(switch (recordId) {
                case "minecraft:13" -> Items.MUSIC_DISC_13;
                case "minecraft:cat" -> Items.MUSIC_DISC_CAT;
                case "minecraft:blocks" -> Items.MUSIC_DISC_BLOCKS;
                case "minecraft:chirp" -> Items.MUSIC_DISC_CHIRP;
                case "minecraft:far" -> Items.MUSIC_DISC_FAR;
                case "minecraft:mall" -> Items.MUSIC_DISC_MALL;
                case "minecraft:mellohi" -> Items.MUSIC_DISC_MELLOHI;
                case "minecraft:stal" -> Items.MUSIC_DISC_STAL;
                case "minecraft:strad" -> Items.MUSIC_DISC_STRAD;
                case "minecraft:ward" -> Items.MUSIC_DISC_WARD;
                case "minecraft:11" -> Items.MUSIC_DISC_11;
                case "minecraft:wait" -> Items.MUSIC_DISC_WAIT;
                case "minecraft:pigstep" -> Items.MUSIC_DISC_PIGSTEP;
                case "minecraft:otherside" -> Items.MUSIC_DISC_OTHERSIDE;
                case "minecraft:5" -> Items.MUSIC_DISC_5;
                case "minecraft:relic" -> Items.MUSIC_DISC_RELIC;
                case "minecraft:precipice" -> Items.MUSIC_DISC_PRECIPICE;
                case "minecraft:creator" -> Items.MUSIC_DISC_CREATOR;
                case "minecraft:creator_music_box" -> Items.MUSIC_DISC_CREATOR_MUSIC_BOX;
                default -> Items.AIR;
            });
        }

    }

}
