package com.Maxwell.eschatology.common.helper;

import com.Maxwell.eschatology.Balance.ModConstants;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class DamageHelper {

    public static void dealAweContemptDamage(ServerPlayer player) {
        Level level = player.level();

        double range = ModConstants.Counter.RANGE;
        float damage = ModConstants.Counter.SWEEP_DAMAGE;

        Vec3 look = player.getLookAngle().normalize();

        List<LivingEntity> entities = level.getEntitiesOfClass(
                LivingEntity.class,
                player.getBoundingBox().inflate(range),
                e -> e != player && e.isAlive() && e instanceof LivingEntity
        );
        for (LivingEntity target : entities) {
            Vec3 diff = target.position().subtract(player.position()).normalize();
            double dot = look.dot(diff);
            if (dot > 0.3) {
                target.hurt(level.damageSources().playerAttack(player), damage);
            }
        }
    }
}