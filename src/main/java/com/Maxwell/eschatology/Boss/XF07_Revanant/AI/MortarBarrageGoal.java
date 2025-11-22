package com.Maxwell.eschatology.Boss.XF07_Revanant.AI;

import com.Maxwell.eschatology.Boss.XF07_Revanant.ExoWither;
import com.Maxwell.eschatology.Boss.XF07_Revanant.ExoWitherBalance;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;

public class MortarBarrageGoal extends Goal {
    private final ExoWither owner;
    private final int cooldown;
    private int actionTicks;
    private Vec3 targetPos;
    private int barrageCount;    private static final int TOTAL_DURATION = ExoWitherBalance.MORTAR_PREPARE_DURATION_TICKS +
            (ExoWitherBalance.MORTAR_FIRE_INTERVAL_TICKS * ExoWitherBalance.MORTAR_TOTAL_SHOTS);    public MortarBarrageGoal(ExoWither owner, int cooldown) {
        this.owner = owner;
        this.cooldown = cooldown;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }    @Override
    public boolean canUse() {
        if (owner.isSpawning()) return false;
        LivingEntity target = this.owner.getTarget();
        if (target == null || !target.isAlive()) {
            return false;
        }        double distanceSq = this.owner.distanceToSqr(target);
        return this.owner.getAttackPhase() == ExoWither.AttackPhase.NONE &&
                this.owner.mortarBarrageCooldown <= 0 &&
                distanceSq > ExoWitherBalance.MORTAR_MIN_DIST_SQ;
    }    @Override
    public boolean canContinueToUse() {
        return this.actionTicks < TOTAL_DURATION && this.owner.getTarget() != null && this.owner.getTarget().isAlive();
    }    @Override
    public void start() {
        this.actionTicks = 0;
        this.barrageCount = 0;
        LivingEntity target = this.owner.getTarget();
        if (target != null) {
            this.targetPos = target.position();
        }
        this.owner.setAttackPhase(ExoWither.AttackPhase.MORTAR_BARRAGE);
        this.owner.getNavigation().stop();
        this.owner.playSound(SoundEvents.WITHER_SPAWN, 1.5F, 1.2F);
    }    @Override
    public void stop() {
        this.owner.mortarBarrageCooldown = this.cooldown;
        this.owner.setAttackPhase(ExoWither.AttackPhase.NONE);
    }    @Override
    public void tick() {
        this.actionTicks++;
        LivingEntity target = this.owner.getTarget();
        if (target != null) {
            this.owner.getLookControl().setLookAt(target);
        }        if (this.actionTicks < ExoWitherBalance.MORTAR_PREPARE_DURATION_TICKS) {
            if (this.actionTicks % 10 == 0 && this.targetPos != null && this.owner.level() instanceof ServerLevel serverLevel) {
                for(int i = 0; i < 12; ++i) {
                    double angle = Math.toRadians(i * 30);
                    double radius = ExoWitherBalance.MORTAR_INDICATOR_RADIUS;
                    double px = this.targetPos.x() + Math.cos(angle) * radius;
                    double pz = this.targetPos.z() + Math.sin(angle) * radius;
                    serverLevel.sendParticles(ParticleTypes.ENCHANTED_HIT, px, this.targetPos.y(), pz, 1, 0, 0, 0, 0);
                }
            }
        } else {
            if ((this.actionTicks - ExoWitherBalance.MORTAR_PREPARE_DURATION_TICKS) % ExoWitherBalance.MORTAR_FIRE_INTERVAL_TICKS == 0 &&
                    this.barrageCount < ExoWitherBalance.MORTAR_TOTAL_SHOTS) {
                this.executeBarrage();
                this.barrageCount++;
            }
        }
    }    private void executeBarrage() {
        if (this.targetPos == null || !(this.owner.level() instanceof ServerLevel level)) return;        double radius = ExoWitherBalance.MORTAR_IMPACT_RANDOM_RADIUS;
        double offsetX = (this.owner.getRandom().nextDouble() - 0.5) * radius * 2.0;
        double offsetZ = (this.owner.getRandom().nextDouble() - 0.5) * radius * 2.0;
        Vec3 impactPos = new Vec3(this.targetPos.x() + offsetX, this.targetPos.y(), this.targetPos.z() + offsetZ);        level.sendParticles(ParticleTypes.EXPLOSION_EMITTER, impactPos.x, impactPos.y, impactPos.z, 2, 1.0, 1.0, 1.0, 0.0);
        level.playSound(null, impactPos.x, impactPos.y, impactPos.z, SoundEvents.GENERIC_EXPLODE, this.owner.getSoundSource(), 1.0F, 1.5F);        float damage = (float) this.owner.getAttributeValue(Attributes.ATTACK_DAMAGE) * ExoWitherBalance.MORTAR_DAMAGE_RATIO;
        double areaSize = ExoWitherBalance.MORTAR_IMPACT_AREA_SIZE;
        AABB damageArea = new AABB(impactPos.add(-areaSize, -1, -areaSize), impactPos.add(areaSize, 2, areaSize));
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, damageArea);
        for (LivingEntity entity : entities) {
            if (entity != this.owner && !this.owner.isAlliedTo(entity)) {
                entity.hurt(this.owner.damageSources().mobAttack(this.owner), damage);
            }
        }
    }
}