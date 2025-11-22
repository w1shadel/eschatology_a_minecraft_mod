package com.Maxwell.eschatology.client;

import com.Maxwell.eschatology.common.Capability.RevengeLegder.RevengeData;
import com.Maxwell.eschatology.common.Event.RevengeLedgerEvent.AbilityGuiOverlay;
import com.Maxwell.eschatology.common.Network.SpawnCounterParticlesPacket;
import com.Maxwell.eschatology.common.Network.SyncRevengeGaugePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientPacketHandler {

    public static void handleSyncAbilityGui(boolean showGui, String text) {
        Minecraft mc = Minecraft.getInstance();
        mc.execute(() -> {
            AbilityGuiOverlay.setGuiVisible(showGui);
            if (text != null && !text.isEmpty()) {
                AbilityGuiOverlay.setDisplayText(text);
            }
        });
    }
    public static void handleSyncRevengeGauge(SyncRevengeGaugePacket msg) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            RevengeData.setGaugeClient(mc.player, msg.gauge);
        }
    }
    public static void handleSpawnCounterParticles(SpawnCounterParticlesPacket packet) {
        Level level = Minecraft.getInstance().level;

        if (level == null) {
            return;
        }

        Vec3 center = packet.getPosition();
        RandomSource random = level.getRandom();

        int particleCount = 50;
        double radius = 3.0;

        for (int i = 0; i < particleCount; i++) {
            double r = radius * Math.cbrt(random.nextDouble());
            double theta = random.nextDouble() * 2.0 * Math.PI;
            double phi = Math.acos(2.0 * random.nextDouble() - 1.0);
            double offsetX = r * Math.sin(phi) * Math.cos(theta);
            double offsetY = r * Math.cos(phi);
            double offsetZ = r * Math.sin(phi) * Math.sin(theta);
            offsetY = Math.abs(offsetY);
            double x = center.x + offsetX;
            double y = center.y + offsetY;
            double z = center.z + offsetZ;
            double motionX = offsetX * 0.03;
            double motionY = offsetY * 0.03;
            double motionZ = offsetZ * 0.03;
            level.addParticle(ParticleTypes.CRIT, x, y, z, motionX, motionY, motionZ);
            if (i % 5 == 0) {
                level.addParticle(ParticleTypes.SWEEP_ATTACK, x, y, z, 0, 0, 0);
            }
        }
    }
}