package com.Maxwell.eschatology.client;import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;public class BossMusicManager {
    private static SoundInstance currentBossMusic;    public static void playBossMusic(ResourceLocation soundLocation) {
        System.out.println("BossMusicManager: Playing music -> " + soundLocation.toString());        if (currentBossMusic != null && currentBossMusic.getLocation().equals(soundLocation) && Minecraft.getInstance().getSoundManager().isActive(currentBossMusic)) {
            return;
        }
        stopBossMusic();
        SoundEvent soundEvent = SoundEvent.createVariableRangeEvent(soundLocation);
        currentBossMusic = new SimpleSoundInstance(
                soundEvent.getLocation(),
                SoundSource.MUSIC,
                1.0F,
                1.0F,
                RandomSource.create(),
                true,
                0,
                SoundInstance.Attenuation.NONE,
                0.0D, 0.0D, 0.0D,
                true
        );
        Minecraft.getInstance().getSoundManager().play(currentBossMusic);
    }    public static void stopBossMusic() {
        if (currentBossMusic != null) {
            Minecraft.getInstance().getSoundManager().stop(currentBossMusic);
            currentBossMusic = null;
        }
    }
}