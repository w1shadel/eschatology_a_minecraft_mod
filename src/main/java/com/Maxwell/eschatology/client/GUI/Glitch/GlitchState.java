package com.Maxwell.eschatology.client.GUI.Glitch;import com.Maxwell.eschatology.common.Network.ModMessages;
import com.Maxwell.eschatology.common.Network.SyncGlitchEffectPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;public class GlitchState {    public static float forcedGlitchIntensity = 0.0f;    private static final float[] CYCLE_STRENGTHS = {0.0f, 0.25f, 0.5f, 0.75f, 1.0f};
    private static int cycleIndex = 0;
    public static void setGlitchEffect(Level level, float intensity) {
        if (level instanceof ServerLevel serverLevel) {
            forcedGlitchIntensity = Mth.clamp(intensity, 0.0f, 1.0f);            for (ServerPlayer player : serverLevel.getServer().getPlayerList().getPlayers()) {
                ModMessages.sendToPlayer(new SyncGlitchEffectPacket(forcedGlitchIntensity), player);
                player.sendSystemMessage(Component.literal("Glitch Intensity set to: " + (int)(forcedGlitchIntensity * 100) + "%"), true);
            }
        }
    }
    public static void cycleGlitchEffect(Level level) {
        cycleIndex = (cycleIndex + 1) % CYCLE_STRENGTHS.length;
        setGlitchEffect(level, CYCLE_STRENGTHS[cycleIndex]);
    }
}