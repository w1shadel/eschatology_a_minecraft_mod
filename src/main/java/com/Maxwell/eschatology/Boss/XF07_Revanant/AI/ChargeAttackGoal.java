package com.Maxwell.eschatology.Boss.XF07_Revanant.AI;

import com.Maxwell.eschatology.Boss.XF07_Revanant.ExoWither;
import com.Maxwell.eschatology.Boss.XF07_Revanant.ExoWitherBalance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChargeAttackGoal extends Goal {
    private final ExoWither owner;
    private final int cooldown;
    private LivingEntity target;    private enum Phase {PREPARING, CHARGING}    private Phase phase;    private double currentChargeSpeed;
    private int collisionCount = 0;
    private int actionTicks;
    private Vec3 chargeDirection;
    private final Set<LivingEntity> hitEntities = new HashSet<>();
    private boolean isEnraged = false;    public ChargeAttackGoal(ExoWither owner, int cooldown) {
        this.owner = owner;
        this.cooldown = cooldown;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }    public void setEnraged(boolean enraged) {
        this.isEnraged = enraged;
    }    @Override
    public boolean canUse() {
        this.target = this.owner.getTarget();
        if (owner.isSpawning()) return false;
        if (this.target == null || !this.target.isAlive()) {
            this.owner.resetChargeChain();
            return false;
        }        double distanceSq = this.owner.distanceToSqr(this.target);        if (this.isEnraged) {
            if (this.owner.getChargeChainCount() > 0 && this.owner.getChargeChainCount() < ExoWither.MAX_ENRAGED_CHARGE_CHAIN) {
                return true;
            }
            return this.owner.getAttackPhase() == ExoWither.AttackPhase.NONE &&
                    this.owner.chargeCooldown <= 0 &&
                    distanceSq > ExoWitherBalance.CHARGE_MIN_DIST_SQ_ENRAGED &&
                    distanceSq < ExoWitherBalance.CHARGE_MAX_DIST_SQ_ENRAGED;
        }        return this.owner.getAttackPhase() == ExoWither.AttackPhase.NONE &&
                this.owner.chargeCooldown <= 0 &&
                distanceSq > ExoWitherBalance.CHARGE_MIN_DIST_SQ_NORMAL &&
                distanceSq < ExoWitherBalance.CHARGE_MAX_DIST_SQ_NORMAL;
    }
    @Override
    public boolean canContinueToUse() {
        if (this.target == null || !this.target.isAlive() || this.owner.getAttackPhase() != ExoWither.AttackPhase.CHARGE) {
            return false;
        }
        double distanceToTargetSq = this.owner.distanceToSqr(this.target);
        boolean tooFarFromTarget = distanceToTargetSq > ExoWitherBalance.CHARGE_CANCEL_DIST_SQ;
        boolean timedOut = this.actionTicks >= ExoWitherBalance.CHARGE_MAX_DURATION_TICKS;
        boolean tooManyCollisions = this.collisionCount >= ExoWitherBalance.CHARGE_MAX_COLLISIONS;
        return !(timedOut || tooManyCollisions || tooFarFromTarget);
    }    @Override
    public void start() {
        this.actionTicks = 0;
        this.phase = Phase.PREPARING;
        this.hitEntities.clear();
        this.owner.setAttackPhase(ExoWither.AttackPhase.CHARGE);
        this.owner.getNavigation().stop();
        this.currentChargeSpeed = ExoWitherBalance.CHARGE_INITIAL_SPEED;
        this.collisionCount = 0;        Vec3 targetFuturePos = this.target.position().add(this.target.getDeltaMovement().scale(ExoWitherBalance.CHARGE_LEAD_PREDICTION_FACTOR));
        this.chargeDirection = targetFuturePos.subtract(this.owner.position()).normalize();
        this.chargeDirection = new Vec3(this.chargeDirection.x, 0, this.chargeDirection.z).normalize();
    }    @Override
    public void stop() {
        this.owner.setDeltaMovement(Vec3.ZERO);
        if (this.owner.chargeCooldown <= 0) {
            this.owner.chargeCooldown = this.cooldown;
        }
        this.owner.setAttackPhase(ExoWither.AttackPhase.NONE);        if (this.phase == Phase.CHARGING && !this.owner.level().isClientSide) {
            this.owner.level().broadcastEntityEvent(this.owner, (byte) 4);
            this.owner.playSound(SoundEvents.GENERIC_EXPLODE, 1.5F, 1.2F);            float shockwaveDamage = (float) this.owner.getAttributeValue(Attributes.ATTACK_DAMAGE) * ExoWitherBalance.CHARGE_SHOCKWAVE_DAMAGE_RATIO;
            AABB area = this.owner.getBoundingBox().inflate(ExoWitherBalance.CHARGE_SHOCKWAVE_RADIUS);
            List<LivingEntity> nearbyEntities = this.owner.level().getEntitiesOfClass(LivingEntity.class, area);
            for (LivingEntity entity : nearbyEntities) {
                if (entity != this.owner && !this.owner.isAlliedTo(entity)) {
                    entity.invulnerableTime = 0;
                    entity.hurt(this.owner.damageSources().mobAttack(this.owner), shockwaveDamage);
                    Vec3 knockbackDir = entity.position().subtract(this.owner.position()).normalize();
                    entity.knockback(ExoWitherBalance.CHARGE_SHOCKWAVE_KNOCKBACK, -knockbackDir.x, -knockbackDir.z);
                }
            }
        }
    }
    @Override
    public void tick() {
        if (this.target == null || !this.target.isAlive()) { return; }        this.owner.getLookControl().setLookAt(this.target);
        this.actionTicks++;        if (this.phase == Phase.PREPARING) {
            if (this.actionTicks >= ExoWitherBalance.CHARGE_PREPARE_DURATION_TICKS) {
                this.phase = Phase.CHARGING;
                this.actionTicks = 0;
                this.owner.playSound(SoundEvents.WITHER_HURT, 2.0F, 0.8F);
            }
        }  else if (this.phase == Phase.CHARGING) {            if (this.actionTicks % 2 == 0) {
                this.owner.breakBlocks(ExoWitherBalance.CHARGE_BLOCK_BREAK_RADIUS);
            }            Vec3 velocity = this.chargeDirection.scale(this.currentChargeSpeed);
            this.owner.move(MoverType.SELF, velocity);
            this.owner.setDeltaMovement(velocity);            float penetrationDamage = (float) this.owner.getAttributeValue(Attributes.ATTACK_DAMAGE) * ExoWitherBalance.CHARGE_PENETRATION_DAMAGE_RATIO;            if (!this.owner.horizontalCollision) {
                AABB chargeHitbox = this.owner.getBoundingBox().inflate(ExoWitherBalance.CHARGE_HITBOX_INFLATE_XZ, ExoWitherBalance.CHARGE_HITBOX_INFLATE_Y, ExoWitherBalance.CHARGE_HITBOX_INFLATE_XZ);
                List<LivingEntity> nearbyEntities = this.owner.level().getEntitiesOfClass(LivingEntity.class, chargeHitbox);
                boolean hasHitTarget = false;                for (LivingEntity entity : nearbyEntities) {
                    if (entity != this.owner && !this.owner.isAlliedTo(entity) && !this.hitEntities.contains(entity)) {
                        entity.invulnerableTime = 0;
                        entity.hurt(this.owner.damageSources().mobAttack(this.owner), penetrationDamage);
                        this.hitEntities.add(entity);
                        hasHitTarget = true;
                    }
                }
                if (hasHitTarget) {
                    this.currentChargeSpeed *= ExoWitherBalance.CHARGE_SPEED_REDUCTION_ON_HIT;
                    this.owner.playSound(SoundEvents.PLAYER_ATTACK_KNOCKBACK, 1.5F, 0.5F);
                }
            }
            boolean timedOut = this.actionTicks >= ExoWitherBalance.CHARGE_MAX_DURATION_TICKS;
            boolean tooManyCollisions = this.collisionCount >= ExoWitherBalance.CHARGE_MAX_COLLISIONS;
            if (timedOut || tooManyCollisions) {
                this.owner.chargeCooldown = this.cooldown;
                this.owner.setAttackPhase(ExoWither.AttackPhase.NONE);
            }
        }
    }
}