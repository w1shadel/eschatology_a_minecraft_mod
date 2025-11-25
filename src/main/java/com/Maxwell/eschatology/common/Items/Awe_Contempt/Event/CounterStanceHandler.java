package com.Maxwell.eschatology.common.Items.Awe_Contempt.Event;

import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.common.Items.FellBullet.Entity.FellBulletShardEntity;
import com.Maxwell.eschatology.Balance.ModConstants;
import com.Maxwell.eschatology.common.Network.ModMessages;
import com.Maxwell.eschatology.common.Network.SpawnCounterParticlesPacket;
import com.Maxwell.eschatology.register.ModEffects;
import com.Maxwell.eschatology.register.ModEntities;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Eschatology.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CounterStanceHandler {
    private static final Map<UUID, CounterData> ACTIVE_COUNTERS = new HashMap<>();
    private static final int DAMAGE_INSTANCES = 50;
    private static final int DAMAGE_INTERVAL = 1;
    private static final float DAMAGE_AMOUNT = 10.0F;
    private static final int DAMAGE_RADIUS = 3;
    private static final Map<UUID, Integer> PENDING_COUNTERS = new HashMap<>();
    public static void initiateCounterSequence(ServerPlayer player) {

        if (!PENDING_COUNTERS.containsKey(player.getUUID()) && !ACTIVE_COUNTERS.containsKey(player.getUUID())) {
            PENDING_COUNTERS.put(player.getUUID(), 0); 
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {

            PENDING_COUNTERS.entrySet().removeIf(entry -> {
                UUID playerUUID = entry.getKey();
                int ticksElapsed = entry.getValue();
                ServerPlayer player = event.getServer().getPlayerList().getPlayer(playerUUID);

                if (player == null || !player.isAlive()) {
                    return true; 
                }
                player.setInvulnerable(true);
                ticksElapsed++;

                if (ticksElapsed == ModConstants.Counter.SHARD_SPAWN_TICK) {
                    spawnShardsForCounter(player);
                }
                if (ticksElapsed >= ModConstants.Counter.START_TICK) {
                    startActiveCounter(player);
                }
                if (ticksElapsed >= ModConstants.Counter.END_TICK) {
                    player.setInvulnerable(false);
                    return true;
                }

                entry.setValue(ticksElapsed);
                return false; 
            });

            ACTIVE_COUNTERS.entrySet().removeIf(entry -> {
                Player player = event.getServer().getPlayerList().getPlayer(entry.getKey());
                CounterData data = entry.getValue();

                if (player == null || !player.isAlive()) {
                    return true;
                }

                data.ticksUntilNextDamage--;
                if (data.ticksUntilNextDamage <= 0) {
                    performAreaDamage(player);
                    data.damageInstancesLeft--;
                    data.ticksUntilNextDamage = ModConstants.Counter.DAMAGE_INTERVAL;
                }

                return data.damageInstancesLeft <= 0;
            });
        }
    }

    private static void startActiveCounter(ServerPlayer player) {
        ACTIVE_COUNTERS.put(player.getUUID(), new CounterData());
        player.addEffect(new MobEffectInstance(ModEffects.COUNTER_STANCE.get(), DAMAGE_INSTANCES * DAMAGE_INTERVAL, 0));
    }

    private static void spawnShardsForCounter(Player player) {
        Level world = player.level();
        Vec3 centerTarget = player.position().add(0, player.getEyeHeight() / 2.0, 0);
        world.playSound(null, centerTarget.x, centerTarget.y, centerTarget.z, SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 1.0f, 0.5f);

        for (int i = 0; i < 42; i++) {
            Vec3 randomDirection = new Vec3(world.getRandom().nextDouble() - 0.5, world.getRandom().nextDouble() - 0.5, world.getRandom().nextDouble() - 0.5).normalize();
            Vec3 spawnPos = centerTarget.add(randomDirection.scale(4.0));

            FellBulletShardEntity shard = new FellBulletShardEntity(ModEntities.FELL_SHARD.get(), world, player);
            shard.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
            Vec3 velocity = centerTarget.subtract(spawnPos).normalize().scale(1.2);
            shard.setDeltaMovement(velocity);
            world.addFreshEntity(shard);
        }
    }

    private static void performAreaDamage(Player player) {

        DamageSource damageSource = player.damageSources().magic();
        player.level().getEntitiesOfClass(Monster.class, player.getBoundingBox().inflate(ModConstants.Counter.DAMAGE_RADIUS))
                .forEach(enemy -> {
                    enemy.invulnerableTime = 0;
                    enemy.hurt(damageSource, ModConstants.Counter.DAMAGE_AMOUNT);
                });
        ModMessages.sendToPlayer(new SpawnCounterParticlesPacket(player.position()), (ServerPlayer) player);
    }

    private static class CounterData {
        int damageInstancesLeft = ModConstants.Counter.DAMAGE_INSTANCES;
        int ticksUntilNextDamage = 0;
    }
}