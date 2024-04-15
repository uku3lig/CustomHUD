package com.minenash.customhud.complex;

import com.minenash.customhud.mixin.music.MinecraftClientAccess;
import com.minenash.customhud.mixin.music.MusicTrackerAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MusicAndRecordTracker {

    public static boolean isMusicPlaying = false;
    public static String musicId = "";
    public static String musicName = "";

    public static boolean isRecordPlaying = false;
//    public static SoundInstance recordInstance = null;
//    public static String recordId = "";
//    public static String recordName = "";
//    public static int recordLength = 0;
//    public static int recordElapsed = 0;
//    public static ItemStack recordIcon = null;

    public static List<RecordInstance> records = new ArrayList<>();
    public static class RecordInstance {
        public SoundInstance sound = null;
        public String id = "";
        public Text name = Text.literal("Unknown Music Disc");
        public int length = 0;
        public int elapsed = 0;
        public ItemStack icon = ItemStack.EMPTY;
    }

    public static RecordInstance getClosestRecord() {
        if (client.player == null) return records.get(records.size()-1);
        Vec3d pos = client.player.getPos();

        RecordInstance closestInstance = records.get(0);
        double closestDistance = pos.squaredDistanceTo(closestInstance.sound.getX(), closestInstance.sound.getY(), closestInstance.sound.getZ());


        for (int i = 1; i < records.size(); i++) {
            RecordInstance instance = records.get(i);
            double distance = pos.squaredDistanceTo(instance.sound.getX(), instance.sound.getY(), instance.sound.getZ());
            if (distance <= closestDistance) {
                closestDistance = distance;
                closestInstance = instance;
            }
        }

        return closestInstance;
    }

    private final static MinecraftClient client = MinecraftClient.getInstance();

    public static void tick() {
//        isRecordPlaying = recordInstance != null && client.getSoundManager().isPlaying(recordInstance);
//        if (isRecordPlaying)
//            recordElapsed++;


        Iterator<RecordInstance> iterator = records.iterator();
        while (iterator.hasNext()) {
            RecordInstance instance = iterator.next();
            if (!client.getSoundManager().isPlaying(instance.sound))
                iterator.remove();
            else
                instance.elapsed++;
        }
        isRecordPlaying = !records.isEmpty();


        SoundInstance music = ((MusicTrackerAccess)((MinecraftClientAccess)client).getMusicTracker()).getCurrent();
        isMusicPlaying =  client.getSoundManager().isPlaying(music);
        if (music != null) {
            musicId = music.getSound().getIdentifier().toString();
            musicName = WordUtils.capitalize(musicId.substring(musicId.lastIndexOf('/')+1).replace("_", " ").replaceAll("(\\d+)", " $1"));
        }
    }

    public static void setRecord(SoundEvent song, SoundInstance instance) {
        if (song == null)
            return;

        RecordInstance record = new RecordInstance();
        record.sound = instance;
        record.id = song.getId().toString();

        MusicDiscItem musicDiscItem = MusicDiscItem.bySound(song);
        if (musicDiscItem != null) {
            record.name = musicDiscItem.getName();
            record.length = musicDiscItem.getSongLengthInTicks();
            record.icon = new ItemStack(musicDiscItem);
        }

        records.add(record);
    }

}
