package com.Maxwell.eschatology.common.Event;import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.Balance.ModConstants;
import com.Maxwell.eschatology.register.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;import java.util.Collections;
import java.util.List;
@SuppressWarnings("removal")
@Mod.EventBusSubscriber(modid = Eschatology.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TOaFGTickHandler {    private static final int TARGET_RANGE = 16;    private static final int PARTICLES_PER_TARGET = 10;    private static final float ATTACK_DAMAGE = 2.0F;    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.player.level().isClientSide() && event.player.tickCount % ModConstants.TOaFG.CHECK_INTERVAL == 0) {
            Player player = event.player;            boolean hasGun = CuriosApi.getCuriosHelper()
                    .findEquippedCurio(ModItems.THE_ONCE_AND_FUTURE_GUN.get(), player)
                    .isPresent();            if (hasGun) {
                Level level = player.level();
                AABB searchArea = new AABB(player.blockPosition()).inflate(ModConstants.TOaFG.TARGET_RANGE);
                List<Monster> nearbyMonsters = level.getEntitiesOfClass(Monster.class, searchArea, monster ->
                        monster.isAlive() && monster.isAttackable() && !monster.isSpectator()
                );                if (nearbyMonsters.isEmpty()) return;                Collections.shuffle(nearbyMonsters);
                int targetCount = Math.min(nearbyMonsters.size(), ModConstants.TOaFG.MAX_TARGETS);                for (int i = 0; i < targetCount; i++) {
                    Monster target = nearbyMonsters.get(i);
                    spawnParticlesAroundTarget((ServerLevel) level, target);
                    DamageSource damageSource = player.damageSources().indirectMagic(player, player);
                    target.hurt(damageSource, ModConstants.TOaFG.ATTACK_DAMAGE);
                }
            }
        }
    }    private static void spawnParticlesAroundTarget(ServerLevel serverLevel, Monster target) {
        RandomSource random = serverLevel.getRandom();
        Vec3 targetPos = target.position().add(0, target.getBbHeight() / 2.0, 0);        final int TRAIL_PARTICLE_COUNT = 4;        for (int i = 0; i < PARTICLES_PER_TARGET; i++) {            double offsetX = (random.nextDouble() - 0.5) * 8.0;
            double offsetY = random.nextDouble() * 4.0;
            double offsetZ = (random.nextDouble() - 0.5) * 8.0;
            Vec3 startPos = targetPos.add(offsetX, offsetY, offsetZ);            Vec3 travelVector = targetPos.subtract(startPos);            for (int j = 1; j <= TRAIL_PARTICLE_COUNT; j++) {                Vec3 trailPos = startPos.add(travelVector.scale((float)j / (TRAIL_PARTICLE_COUNT + 1)));                serverLevel.sendParticles(
                        ParticleTypes.CRIT,
                        trailPos.x(),
                        trailPos.y(),
                        trailPos.z(),
                        1,
                        0.0,
                        0.0,
                        0.0,
                        0.0
                );
            }            Vec3 motion = travelVector.normalize().scale(0.5);
            serverLevel.sendParticles(
                    ParticleTypes.CRIT,
                    startPos.x(),
                    startPos.y(),
                    startPos.z(),
                    0,
                    motion.x(),
                    motion.y(),
                    motion.z(),
                    0.5
            );
        }
    }
}