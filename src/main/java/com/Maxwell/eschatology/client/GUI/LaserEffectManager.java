package com.Maxwell.eschatology.client.GUI;import com.Maxwell.eschatology.common.Network.SyncLaserEffectsPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
@SuppressWarnings("removal")
public class LaserEffectManager {    private static boolean isLaserActive = false;
    private static int postLaserFlashTicks = 0;
    private static final int FLASH_DURATION = 15;     public static void handleLaserEffect(SyncLaserEffectsPacket.EffectPhase phase) {
        if (phase == SyncLaserEffectsPacket.EffectPhase.START_FIRING) {
            isLaserActive = true;
        } else if (phase == SyncLaserEffectsPacket.EffectPhase.STOP_FIRING) {
            isLaserActive = false;
            postLaserFlashTicks = FLASH_DURATION;
            Minecraft.getInstance().gameRenderer.loadEffect(new ResourceLocation("shaders/post/desaturate.json"));
        }
    }    public static boolean isLaserActive() {
        return isLaserActive;
    }    public static void tick() {
        if (postLaserFlashTicks > 0) {
            postLaserFlashTicks--;
            if (postLaserFlashTicks == 0) {
                Minecraft.getInstance().gameRenderer.shutdownEffect();
            }
        }
    }
}