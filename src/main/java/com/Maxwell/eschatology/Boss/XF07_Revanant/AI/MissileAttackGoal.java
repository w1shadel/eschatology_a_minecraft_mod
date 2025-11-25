package com.Maxwell.eschatology.Boss.XF07_Revanant.AI;

import com.Maxwell.eschatology.Boss.XF07_Revanant.Animation.ExoWitherAnimationTicks;
import com.Maxwell.eschatology.Boss.XF07_Revanant.Entities.ExoMisslie.ExoMissile;
import com.Maxwell.eschatology.Boss.XF07_Revanant.ExoWither;
import com.Maxwell.eschatology.Balance.ExoWitherBalance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class MissileAttackGoal extends Goal {
    private final ExoWither owner;
    private int attackTime;
    private final int cooldown;
    private int missilesFired;
    private boolean isEnraged = false;
    private int fireInterval;
    private static final int ANIMATION_DURATION_TICKS = (int) (ExoWitherAnimationTicks.MISSILE_SHOOT_ATTACK_LENGTH_IN_SECONDS * 20);  public MissileAttackGoal(ExoWither owner, int cooldown) {
        this.owner = owner;
        this.cooldown = cooldown;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }    @Override
    public boolean canUse() {
        LivingEntity target = this.owner.getTarget();
        if (owner.isSpawning() || target == null || !target.isAlive()) {
            return false;
        }
        double distanceSq = this.owner.distanceToSqr(target);        return this.owner.getAttackPhase() == ExoWither.AttackPhase.NONE &&
                this.owner.missileAttackCooldown <= 0 &&
                distanceSq > ExoWitherBalance.MISSILE_MIN_DIST_SQ;
    }    @Override
    public boolean canContinueToUse() {
        return this.attackTime < ANIMATION_DURATION_TICKS && this.owner.getTarget() != null && this.owner.getTarget().isAlive();
    }    public void setEnraged(boolean enraged) {
        this.isEnraged = enraged;
    }    @Override
    public void start() {
        this.attackTime = 0;
        this.missilesFired = 0;
        this.owner.setAttackPhase(ExoWither.AttackPhase.MISSILE_ATTACK);
        this.owner.setDeltaMovement(Vec3.ZERO);        this.fireInterval = this.isEnraged ?
                ExoWitherBalance.MISSILE_FIRE_INTERVAL_ENRAGED :
                ExoWitherBalance.MISSILE_FIRE_INTERVAL_NORMAL;
    }    @Override
    public void stop() {
        this.owner.missileAttackCooldown = this.cooldown;
        this.owner.setAttackPhase(ExoWither.AttackPhase.NONE);
    }    @Override
    public void tick() {
        LivingEntity target = this.owner.getTarget();
        if (target == null) return;        this.owner.getLookControl().setLookAt(target);
        this.attackTime++;        if (this.attackTime % this.fireInterval == 0 && this.missilesFired < ExoWitherBalance.MISSILE_COUNT_PER_SIDE * 2) {
            boolean isLeft = this.missilesFired % 2 == 0;
            fireMissile(isLeft, target);
            this.missilesFired++;
        }
    }    private void fireMissile(boolean isLeft, LivingEntity target) {
        if (this.owner.level().isClientSide) return;        Vec3 rightVec = this.owner.getLookAngle().cross(new Vec3(0, 1, 0)).normalize();
        double sideOffset = isLeft ? ExoWitherBalance.MISSILE_SPAWN_SIDE_OFFSET : -ExoWitherBalance.MISSILE_SPAWN_SIDE_OFFSET;
        double randomOffset = ExoWitherBalance.MISSILE_SPAWN_RANDOM_OFFSET;        Vec3 spawnPos = this.owner.getEyePosition()
                .add(rightVec.scale(sideOffset))
                .add((this.owner.getRandom().nextDouble() - 0.5) * randomOffset,
                        (this.owner.getRandom().nextDouble() - 0.5) * randomOffset,
                        (this.owner.getRandom().nextDouble() - 0.5) * randomOffset);        ExoMissile missile = new ExoMissile(this.owner.level(), this.owner, target);
        missile.setPos(spawnPos);
        missile.setDeltaMovement(0, ExoWitherBalance.MISSILE_INITIAL_VERTICAL_SPEED, 0);
        this.owner.level().addFreshEntity(missile);
    }
}