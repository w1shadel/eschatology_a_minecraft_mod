package com.Maxwell.eschatology.Boss.XF07_Revanant;

import com.Maxwell.eschatology.Boss.BlackBool.BlackBool;
import com.Maxwell.eschatology.Boss.XF07_Revanant.AI.*;
import com.Maxwell.eschatology.Boss.XF07_Revanant.Animation.ExoWitherAnimationTicks;
import com.Maxwell.eschatology.Boss.XF07_Revanant.Animation.ExoWitherBodyRotationControl;
import com.Maxwell.eschatology.Boss.XF07_Revanant.Entities.ExoMisslie.ExoMissile;
import com.Maxwell.eschatology.Boss.XF07_Revanant.Entities.ExoSkull.ExoSkull;
import com.Maxwell.eschatology.Boss.XF07_Revanant.Entities.SmallBeam.SmallBeamEntity;
import com.Maxwell.eschatology.common.MaxwellCustomBossEvent;
import com.Maxwell.eschatology.common.Network.ModMessages;
import com.Maxwell.eschatology.common.Network.PlayBossMusicPacket;
import com.Maxwell.eschatology.register.ModEntities;
import com.Maxwell.eschatology.register.ModSounds;
import com.Maxwell.eschatology.register.advancements.ModTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class ExoWither extends Monster {
    public enum AttackPhase {
        NONE,
        CHARGE,
        MISSILE_ATTACK,
        SIMPLE_LASER,
        LASER_BEAM,
        MORTAR_BARRAGE
    }
    public enum CombatPhase {
        NORMAL,
        ENRAGED
    }
    private int chargeChainCount = 0;
    public static final int MAX_ENRAGED_CHARGE_CHAIN = 8;
    private CombatPhase combatPhase = CombatPhase.NORMAL;
    private boolean hasEnraged = false;
    private ChargeAttackGoal chargeAttackGoal;
    private MissileAttackGoal missileAttackGoal;
    private SimpleLaserAttackGoal simpleLaserAttackGoal;
    private static final EntityDataAccessor<Integer> DATA_ATTACK_PHASE =
            SynchedEntityData.defineId(ExoWither.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_SHIELD_HEALTH = SynchedEntityData.defineId(ExoWither.class, EntityDataSerializers.INT);    public int chargeCooldown = 0;
    public int simpleLaserCooldown = 0;
    public int laserBeamCooldown = 0;
    public int missileAttackCooldown = 0;
    public int exoskullAttackCooldown = 0;
    public int mortarBarrageCooldown = 0;
    private int deathTicks = 0;     public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState spawnAnimationState = new AnimationState();
    public final AnimationState chargeAnimationState = new AnimationState();
    public final AnimationState missileattackAnimationState = new AnimationState();
    public final AnimationState simplelaserattackAnimationState = new AnimationState();
    public final AnimationState mortarbarrageAnimationState = new AnimationState();
    public final AnimationState laserbeamattackAnimationState = new AnimationState();
    public final AnimationState deathAnimationState = new AnimationState();
    private boolean hasStartedDeathAnimation = false;
    private final MaxwellCustomBossEvent bossEvent;
    public ExoWither(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.bossEvent = new MaxwellCustomBossEvent(this.getDisplayName(), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.NOTCHED_6, 1);
        this.xpReward = 50;
    }
    @Override
    protected BodyRotationControl createBodyControl() {
        return new ExoWitherBodyRotationControl(this);
    }
    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
        if (this.isAlive()) {
            ModMessages.sendToPlayer(new PlayBossMusicPacket(ModSounds.MUSIC_BLACK_BOOL.get().getLocation()), player);
        }
    }
    public boolean isSpawning() {
        return this.tickCount < ExoWitherAnimationTicks.SPAWN_LENGTH_IN_SECONDS * 20;
    }
    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
        ModMessages.sendToPlayer(new PlayBossMusicPacket((ResourceLocation) null), player);
    }    @Override
    protected void registerGoals() {
        super.registerGoals();        this.chargeAttackGoal = new ChargeAttackGoal(this, ExoWitherBalance.CHARGE_COOLDOWN_BASE);
        this.missileAttackGoal = new MissileAttackGoal(this, ExoWitherBalance.MISSILE_ATTACK_COOLDOWN_BASE);
        this.simpleLaserAttackGoal = new SimpleLaserAttackGoal(this, ExoWitherBalance.SIMPLE_LASER_COOLDOWN_BASE);
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MortarBarrageGoal(this, ExoWitherBalance.MORTAR_BARRAGE_COOLDOWN_BASE));
        this.goalSelector.addGoal(1, new LaserBeamAttackGoal(this, ExoWitherBalance.LASER_BEAM_COOLDOWN_BASE));
        this.goalSelector.addGoal(2, this.chargeAttackGoal);
        this.goalSelector.addGoal(3, this.missileAttackGoal);
        this.goalSelector.addGoal(3, this.simpleLaserAttackGoal);
        this.targetSelector.addGoal(0, new FindBlackBoolGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ATTACK_PHASE, AttackPhase.NONE.ordinal());
        this.entityData.define(DATA_SHIELD_HEALTH, 0);
    }
    public void setAttackPhase(AttackPhase phase) { this.entityData.set(DATA_ATTACK_PHASE, phase.ordinal()); }
    public AttackPhase getAttackPhase() { return AttackPhase.values()[this.entityData.get(DATA_ATTACK_PHASE)]; }
    public boolean isNoGravity() {return true;}
    @Override
    protected void customServerAiStep() {
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        if (this.isSpawning()) {
            return;
        }
        super.customServerAiStep();
        if (!this.hasEnraged && this.getHealth() / this.getMaxHealth() <= ExoWitherBalance.ENRAGE_HP_THRESHOLD) {
            this.hasEnraged = true;
            this.combatPhase = CombatPhase.ENRAGED;
            this.chargeAttackGoal.setEnraged(true);
            this.missileAttackGoal.setEnraged(true);
            this.simpleLaserAttackGoal.setEnraged(true);
            this.level().broadcastEntityEvent(this, (byte)7);
            this.playSound(SoundEvents.WITHER_DEATH, ExoWitherBalance.SOUND_VOLUME_DEFAULT, ExoWitherBalance.SOUND_PITCH_DEFAULT);
        }
        int cooldownReduction = (this.combatPhase == CombatPhase.ENRAGED) ? 2 : 1;
        if (this.chargeCooldown > 0) this.chargeCooldown -= cooldownReduction;
        if (this.simpleLaserCooldown > 0) this.simpleLaserCooldown -= cooldownReduction;
        if (this.missileAttackCooldown > 0) this.missileAttackCooldown -= cooldownReduction;
        if (this.laserBeamCooldown > 0) this.laserBeamCooldown--;
        if (this.exoskullAttackCooldown > 0) this.exoskullAttackCooldown--;
        if (this.mortarBarrageCooldown > 0) this.mortarBarrageCooldown--;
        if (!this.hasDeployedShield && this.getHealth() / this.getMaxHealth() <= ExoWitherBalance.SHIELD_DEPLOY_HP_THRESHOLD) {
            this.hasDeployedShield = true;
            this.setShieldHealth(ExoWitherBalance.SHIELD_INITIAL_HEALTH);
            this.playSound(SoundEvents.END_PORTAL_SPAWN, 1.5F, 1.5F);
            this.level().broadcastEntityEvent(this, (byte)6);
        }        LivingEntity target = this.getTarget();
        if (target == null || !target.isAlive()) {            double hoverSpeed = Math.sin(this.tickCount * ExoWitherBalance.HOVER_Y_SPEED) * 0.02;
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.98, 0, 0.98).add(0, hoverSpeed, 0));
            return;
        }
        if (this.combatPhase == CombatPhase.ENRAGED &&
                this.chargeChainCount > 0 &&
                this.chargeCooldown <= 0 &&
                this.getAttackPhase() == AttackPhase.NONE) {            if (this.chargeAttackGoal.canUse()) {
                this.chargeAttackGoal.start();
            }
        }        this.getLookControl().setLookAt(target, 30.0F, 30.0F);
        if (this.exoskullAttackCooldown <= 0) {
            this.fireGlacialSkulls(target);
            this.exoskullAttackCooldown = ExoWitherBalance.EXO_SKULL_ATTACK_COOLDOWN;
        }        if (this.getAttackPhase() == AttackPhase.NONE) {            double idealDistance = ExoWitherBalance.IDEAL_DISTANCE_TO_TARGET;
            double moveSpeedScale = ExoWitherBalance.MOVE_SPEED_SCALE;            Vec3 directionToTarget = this.position().vectorTo(target.position());
            double distance = directionToTarget.horizontalDistance();            double targetY = target.getY() + 4.0D + Math.sin(this.tickCount * ExoWitherBalance.HOVER_Y_SPEED) * ExoWitherBalance.HOVER_Y_AMPLITUDE;
            double ySpeed = Mth.clamp((targetY - this.getY()) * ExoWitherBalance.Y_ADJUST_SPEED, -ExoWitherBalance.Y_CLAMP, ExoWitherBalance.Y_CLAMP);            Vec3 horizontalDelta = Vec3.ZERO;
            if (distance > idealDistance + 2.0D) {
                horizontalDelta = directionToTarget.multiply(1, 0, 1).normalize().scale(moveSpeedScale);
            } else if (distance < idealDistance - 2.0D) {
                horizontalDelta = directionToTarget.multiply(1, 0, 1).normalize().scale(-moveSpeedScale * 0.7);
            }            Vec3 finalDelta = this.getDeltaMovement().lerp(new Vec3(horizontalDelta.x, ySpeed, horizontalDelta.z), 0.1);
            this.setDeltaMovement(finalDelta);
        }
    }    public void resetChargeChain() {
        this.chargeChainCount = 0;
    }
    public int getChargeChainCount() {
        return this.chargeChainCount;
    }    public Vec3 getFiringHeadPosition(String headType) {        float bodyYawRad = (this.yBodyRot + 90.0F) * Mth.DEG_TO_RAD;         Vec3 baseOffsetLeft = new Vec3(0, 0.5, 0).add(new Vec3(5.0, 1.0, 0));
        Vec3 baseOffsetRight = new Vec3(0, 0.5, 0).add(new Vec3(-5.0, 1.0, 0));
        Vec3 baseOffsetFront = new Vec3(0, 0.5, 0).add(new Vec3(0, 5.0, -1.0));         Vec3 localOffset = switch (headType) {
            case "left" -> baseOffsetLeft;
            case "right" -> baseOffsetRight;
            default -> baseOffsetFront;
        };        Vec3 rotatedOffset = localOffset.yRot(bodyYawRad);         return this.position().add(0, this.getEyeHeight() * 0.75, 0).add(rotatedOffset);
    }
    private AttackPhase lastAttackPhase = AttackPhase.NONE;
    private boolean wasMovingForward = false;
    private boolean wasIdle = true;
    private AnimationState getAnimationStateFor(AttackPhase phase) {
        return switch (phase) {
            case CHARGE -> this.chargeAnimationState;
            case MISSILE_ATTACK -> this.missileattackAnimationState;
            case SIMPLE_LASER -> this.simplelaserattackAnimationState;
            case LASER_BEAM -> this.laserbeamattackAnimationState;
            case MORTAR_BARRAGE -> this.mortarbarrageAnimationState;
            case NONE -> this.idleAnimationState;
        };
    }
    public void stopAllAnimations() {
        this.idleAnimationState.stop();
        this.walkAnimationState.stop();
        this.chargeAnimationState.stop();
        this.missileattackAnimationState.stop();
        this.simplelaserattackAnimationState.stop();
        this.laserbeamattackAnimationState.stop();
        this.mortarbarrageAnimationState.stop();
    }
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, ExoWitherBalance.MAX_HEALTH)
                .add(Attributes.ATTACK_DAMAGE, ExoWitherBalance.ATTACK_DAMAGE)
                .add(Attributes.MOVEMENT_SPEED, ExoWitherBalance.MOVEMENT_SPEED)
                .add(Attributes.FLYING_SPEED, ExoWitherBalance.FLYING_SPEED)
                .add(Attributes.FOLLOW_RANGE, ExoWitherBalance.FOLLOW_RANGE);
    }
    public void fireGlacialSkulls(LivingEntity target) {
        if (this.level().isClientSide || target == null) return;
        this.playSound(SoundEvents.WITHER_SHOOT, ExoWitherBalance.SOUND_VOLUME_DEFAULT, ExoWitherBalance.SOUND_PITCH_DEFAULT);
        Vec3 leftHeadSpawnPos = getFiringHeadPosition("left");
        Vec3 frontHeadSpawnPos = getFiringHeadPosition("front");
        Vec3 rightHeadSpawnPos = getFiringHeadPosition("right");
        double speed =  ExoWitherBalance.PROJECTILE_SPEED;
        double timeToHit = this.distanceTo(target) / speed;
        Vec3 targetFuturePos = target.position().add(target.getDeltaMovement().scale(timeToHit));
        fireSingleSkull(leftHeadSpawnPos, targetFuturePos.subtract(leftHeadSpawnPos).normalize().yRot(-0.1f));
        fireSingleSkull(frontHeadSpawnPos, targetFuturePos.subtract(frontHeadSpawnPos).normalize());
        fireSingleSkull(rightHeadSpawnPos, targetFuturePos.subtract(rightHeadSpawnPos).normalize().yRot(0.1f));
    }
    public void fireSmallLaser(boolean isLeft) {
        LivingEntity target = this.getTarget();
        if (target == null || this.level().isClientSide) {
            return;
        }
        Vec3 lookVec = this.getLookAngle();
        Vec3 rightVec = lookVec.cross(new Vec3(0, 1, 0)).normalize();
        double sideOffset = isLeft ? 2.5 : -2.5;
        Vec3 spawnPos = this.getEyePosition()
                .add(rightVec.scale(sideOffset))
                .subtract(0, 0.5, 0);
        SmallBeamEntity laser = new SmallBeamEntity(this.level(), this);
        float beamDamage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE) * ExoWitherBalance.SMALL_LASER_DAMAGE_RATIO;
        laser.setDamage(beamDamage);
        laser.setPos(spawnPos);        double distance = this.distanceTo(target);
        double projectileSpeed = ExoWitherBalance.SMALL_LASER_PROJECTILE_SPEED;
        double timeToHit = distance / projectileSpeed;
        Vec3 targetFuturePos = target.position().add(target.getDeltaMovement().scale(timeToHit));
        Vec3 direction = targetFuturePos.add(0, target.getEyeHeight() * 0.5, 0).subtract(spawnPos).normalize();        laser.shoot(direction.x, direction.y, direction.z, (float) projectileSpeed, 0.5f);
        this.level().addFreshEntity(laser);
    }
    private void fireSingleSkull(Vec3 spawnPos, Vec3 direction) {
        ExoSkull skull = new ExoSkull(this.level(), this);
        skull.setPos(spawnPos);        float baseDamage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float directHitDamage = baseDamage * ExoWitherBalance.EXO_SKULL_DIRECT_DAMAGE_RATIO;
        float explosionDamage = baseDamage * ExoWitherBalance.EXO_SKULL_EXPLOSION_DAMAGE_RATIO;
        skull.setDamage(directHitDamage, explosionDamage);        double speed =  ExoWitherBalance.PROJECTILE_SPEED;
        skull.setDeltaMovement(direction.scale(speed));        this.level().addFreshEntity(skull);
    }
    public int getShieldHealth() {
        return this.entityData.get(DATA_SHIELD_HEALTH);
    }    public void setShieldHealth(int health) {
        this.entityData.set(DATA_SHIELD_HEALTH, health);
    }    public boolean isShieldActive() {
        return this.getShieldHealth() > 0;
    }
    public void damageShield(int amount) {
        if (this.level().isClientSide) return;        int currentHealth = this.getShieldHealth();
        if (currentHealth > 0) {
            int newHealth = Math.max(0, currentHealth - amount);
            this.setShieldHealth(newHealth);
            if (newHealth == 0) {
                this.playSound(SoundEvents.GLASS_BREAK, ExoWitherBalance.SOUND_VOLUME_DEFAULT, ExoWitherBalance.SOUND_PITCH_DEFAULT);
                this.level().broadcastEntityEvent(this, (byte)5);
            } else {
                this.playSound(SoundEvents.SHIELD_BLOCK, 1.5F, 1.2F);
            }
        }
    }
    private boolean hasDeployedShield = false;
    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity damageOwner = source.getDirectEntity();
        if (damageOwner instanceof Projectile projectile) {
            if (projectile.getOwner() == this) {
                return false;
            }
        }
        if (source.getEntity() == this) {
            return false;
        }        if (this.isShieldActive()) {            if (source.is(DamageTypeTags.IS_EXPLOSION)) {                this.damageShield(1);                return false;
            }
        }        return super.hurt(source, amount);
    }
    @Override
    public void handleEntityEvent(byte pId) {
        if (pId == 7) {
            for (int i = 0; i < ExoWitherBalance.ENRAGE_PARTICLE_COUNT; ++i) {
                this.level().addParticle(ParticleTypes.LAVA, this.getRandomX(1.5), this.getRandomY(), this.getRandomZ(1.5), 0, 0, 0);
            }
        }
        if (pId == 5) {
            for (int i = 0; i < ExoWitherBalance.SHIELD_BREAK_PARTICLE_COUNT; ++i) {
                this.level().addParticle(ParticleTypes.SNOWFLAKE, this.getRandomX(1.5), this.getRandomY(), this.getRandomZ(1.5),
                        (this.random.nextDouble() - 0.5) * 2.0,
                        this.random.nextDouble() * 2.0,
                        (this.random.nextDouble() - 0.5) * 2.0);
            }
        } else if (pId == 6) {
            for (int i = 0; i < ExoWitherBalance.SHIELD_DEPLOY_PARTICLE_COUNT; ++i) {
                double angle = (i / 50.0) * Math.PI * 4.0;
                double x = Math.cos(angle) * 1.5;
                double z = Math.sin(angle) * 1.5;
                this.level().addParticle(ParticleTypes.SNOWFLAKE, this.getX() + x, this.getY() + (i / 25.0), this.getZ() + z, 0, 0.1, 0);
            }
        } else {
            super.handleEntityEvent(pId);
        }
    }
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.WITHER_AMBIENT;
    }
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.WITHER_HURT;
    }
    protected SoundEvent getDeathSound() {
        return SoundEvents.WITHER_DEATH;
    }    public void breakBlocks(double boxSize) {
        if (this.level().isClientSide) {
            return;
        }        AABB aabb = this.getBoundingBox().inflate(boxSize, boxSize, boxSize);        for (BlockPos blockpos : BlockPos.betweenClosed(
                Mth.floor(aabb.minX), Mth.floor(aabb.minY), Mth.floor(aabb.minZ),
                Mth.floor(aabb.maxX), Mth.floor(aabb.maxY), Mth.floor(aabb.maxZ))) {
            BlockState blockstate = this.level().getBlockState(blockpos);
            if (!blockstate.isAir() && blockstate.getDestroySpeed(this.level(), blockpos) >= 0) {
                this.level().destroyBlock(blockpos, true, this);
            }
        }
    }
    @Override
    public void die(DamageSource pCause) {
        if (pCause.getEntity() instanceof ServerPlayer player) {
            ModTriggers.KILL_EXO_WITHER_TRIGGER.trigger(player);
        }
        super.die(pCause);
    }
    private boolean enraged = false;    public boolean isEnraged() {
        return enraged;
    }    @Override
    public void tick() {
        super.tick();
        if (this.isDeadOrDying()) {
            if (!this.hasStartedDeathAnimation) {
                this.hasStartedDeathAnimation = true;
                this.deathAnimationState.startIfStopped(this.tickCount);
                this.deathTicks = 0;                this.playSound(SoundEvents.WITHER_DEATH, 2.0F, 0.6F);
                if (this.level() instanceof ServerLevel server) {
                    for (int i = 0; i < 60; i++) {
                        double x = this.getX() + (this.random.nextDouble() - 0.5) * 6;
                        double y = this.getY() + this.random.nextDouble() * 4;
                        double z = this.getZ() + (this.random.nextDouble() - 0.5) * 6;
                        server.sendParticles(ParticleTypes.SOUL, x, y, z, 1, 0, 0, 0, 0.02);
                    }
                }
            }
            deathTicks++;
            if (this.level() instanceof ServerLevel server) {
                if (deathTicks % 5 == 0) {
                    for (int i = 0; i < 10; i++) {
                        double x = this.getX() + (this.random.nextDouble() - 0.5) * 4;
                        double y = this.getY() + this.random.nextDouble() * 3;
                        double z = this.getZ() + (this.random.nextDouble() - 0.5) * 4;
                        server.sendParticles(ParticleTypes.SMOKE, x, y, z, 1, 0, 0.05, 0, 0.01);
                    }
                }
                if (deathTicks % 10 == 0) {
                    server.sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY() + 1, this.getZ(), 2, 0, 0, 0, 0);
                }
            }
            this.setDeltaMovement(0, 0.02, 0);
            if (deathTicks == 80) {
                if (this.level() instanceof ServerLevel server) {
                    server.explode(this, this.getX(), this.getY(), this.getZ(), 7.0F, Level.ExplosionInteraction.MOB);
                    server.sendParticles(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
                }
                this.playSound(SoundEvents.GENERIC_EXPLODE, 3.0F, 0.7F);
                this.remove(RemovalReason.KILLED);
            }
            return;
        }
        if (this.level().isClientSide()) {
            AttackPhase currentPhase = getAttackPhase();
            Vec3 horizontalDelta = new Vec3(this.getDeltaMovement().x, 0.0, this.getDeltaMovement().z);
            Vec3 lookVec = this.getLookAngle();
            boolean isMovingForward = horizontalDelta.lengthSqr() > 1.0E-6D && horizontalDelta.dot(lookVec) > 0.0;
            boolean isIdle = !isMovingForward && currentPhase == AttackPhase.NONE;
            if (currentPhase != this.lastAttackPhase) {
                getAnimationStateFor(this.lastAttackPhase).stop();
                getAnimationStateFor(currentPhase).start(this.tickCount);
            }
            if (currentPhase == AttackPhase.NONE) {
                if (isMovingForward != this.wasMovingForward || isIdle != this.wasIdle) {
                    if (isMovingForward) {
                        this.walkAnimationState.start(this.tickCount);
                        this.idleAnimationState.stop();
                    } else {
                        this.idleAnimationState.start(this.tickCount);
                        this.walkAnimationState.stop();
                    }
                }
            } else {
                this.idleAnimationState.stop();
                this.walkAnimationState.stop();
            }
            if (this.isDeadOrDying()) {
                stopAllAnimations();
                this.deathAnimationState.startIfStopped(this.tickCount);
            } else if (this.tickCount < ExoWitherAnimationTicks.SPAWN_LENGTH_IN_SECONDS * 20) {
                stopAllAnimations();
                this.spawnAnimationState.startIfStopped(this.tickCount);
            }
            this.lastAttackPhase = currentPhase;
            this.wasMovingForward = isMovingForward;
            this.wasIdle = isIdle;
        }
        if (!this.level().isClientSide && !this.onGround() && this.getAttackPhase() == AttackPhase.NONE) {
            Vec3 delta = this.getDeltaMovement();
            if (delta.y < 0.0D) {
                this.setDeltaMovement(delta.multiply(1.0, 0.9, 1.0));
            }
        }
        if (!this.enraged && this.getHealth() <= this.getMaxHealth() * ExoWitherBalance.ENRAGE_HP_THRESHOLD) {
            enterSecondPhase();
        }        if (this.enraged && !this.level().isClientSide && this.tickCount % ExoWitherBalance.PASSIVE_ATTACK_INTERVAL_TICKS == 0) {
            passivePhaseTwoAttack();
        }
    }    private void enterSecondPhase() {
        this.enraged = true;        if (!this.level().isClientSide) {
            this.level().playSound(null, this.blockPosition(),
                    SoundEvents.WITHER_SPAWN, this.getSoundSource(), ExoWitherBalance.SOUND_VOLUME_DEFAULT, 0.6F);
        }
        if (this.level() instanceof ServerLevel server) {
            for (int i = 0; i < 60; i++) {
                double x = this.getX() + (this.random.nextDouble() - 0.5) * 6;
                double y = this.getY() + this.random.nextDouble() * 4;
                double z = this.getZ() + (this.random.nextDouble() - 0.5) * 6;
                server.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 1, 0, 0, 0, 0.02);
            }
        }
    }
    private void passivePhaseTwoAttack() {
        LivingEntity target = this.getTarget();
        if (target == null || !target.isAlive()) return;
        ExoMissile missile = new ExoMissile(this.level(), this, target);
        missile.setPos(this.getX(), this.getEyeY(), this.getZ());
        this.level().addFreshEntity(missile);
        SmallBeamEntity beam = new SmallBeamEntity(this.level(), this);
        beam.setDamage(ExoWitherBalance.PASSIVE_SMALL_BEAM_DAMAGE);
        beam.shootFromRotation(this, this.getXRot(), this.getYRot(), 0.0F, (float)ExoWitherBalance.SMALL_LASER_PROJECTILE_SPEED, 0.1F);
        this.level().addFreshEntity(beam);
        if (this.random.nextFloat() < ExoWitherBalance.PASSIVE_SKULL_SPAWN_CHANCE) {
            ExoSkull skull = new ExoSkull(ModEntities.EXO_SKULL.get(), this.level());
            skull.setPos(this.getX(), this.getEyeY() + 0.5, this.getZ());
            skull.setDamage(ExoWitherBalance.PASSIVE_EXO_SKULL_DIRECT_DAMAGE, ExoWitherBalance.PASSIVE_EXO_SKULL_EXPLOSION_DAMAGE);
            skull.setOwner(this);
            this.level().addFreshEntity(skull);
        }
    }
    static class FindBlackBoolGoal extends Goal {
        private final ExoWither revenant;
        private BlackBool target;        public FindBlackBoolGoal(ExoWither revenant) {
            this.revenant = revenant;
            this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        }        @Override
        public boolean canUse() {            if (this.revenant.isDeadOrDying() || this.revenant.isSpawning()) return false;
            if (this.revenant.getTarget() instanceof BlackBool bb && bb.isAlive()) {
                return false;
            }
            List<BlackBool> blackBools = this.revenant.level().getEntitiesOfClass(
                    BlackBool.class,
                    this.revenant.getBoundingBox().inflate(ExoWitherBalance.BLACKBOOL_DETECT_RADIUS)
            );            if (!blackBools.isEmpty()) {
                this.target = blackBools.get(0);
                return true;
            }            return false;
        }        @Override
        public void start() {
            this.revenant.setTarget(this.target);
            super.start();
        }        @Override
        public boolean canContinueToUse() {
            return this.target != null && this.target.isAlive();
        }        @Override
        public void stop() {
            this.target = null;
        }
    }
}