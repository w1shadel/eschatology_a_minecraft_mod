package com.Maxwell.eschatology.common.Capability.RevengeLegder;import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.common.Network.ModMessages;
import com.Maxwell.eschatology.common.Network.SyncRevengeGaugePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;@Mod.EventBusSubscriber(modid = Eschatology.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RevengeMarkHandler {    @SubscribeEvent
    public static void onPlayerHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;        DamageSource source = event.getSource();
        if (source.getEntity() instanceof LivingEntity attacker) {
            attacker.getPersistentData().putBoolean("RevengeMarked", true);
            attacker.level().addParticle(
                    net.minecraft.core.particles.ParticleTypes.SOUL_FIRE_FLAME,
                    attacker.getX(), attacker.getY() + attacker.getBbHeight() + 0.5D, attacker.getZ(),
                    0, 0.05D, 0);
        }
    }
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ModMessages.sendToPlayer(new SyncRevengeGaugePacket(RevengeData.getGauge(player)), player);
        }
    }    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ModMessages.sendToPlayer(new SyncRevengeGaugePacket(RevengeData.getGauge(player)), player);
        }
    }
}