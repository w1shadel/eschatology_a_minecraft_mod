package com.Maxwell.eschatology.Boss.BlackBool;

import com.Maxwell.eschatology.Balance.BlackBoolBalance;
import com.Maxwell.eschatology.Boss.BlackBool.AI.BlackBoolMoveControl;
import com.Maxwell.eschatology.Boss.BlackBool.AI.SingularityGoal;
import com.Maxwell.eschatology.Boss.BlackBool.Entities.EndLaser.EndLaserBeamEntity;
import com.Maxwell.eschatology.Boss.BlackBool.Entities.EventHorizon.EventHorizonControllerEntity;
import com.Maxwell.eschatology.Boss.BlackBool.Entities.GravityOrb.GravityOrbEntity;
import com.Maxwell.eschatology.Boss.BlackBool.Entities.LightOrb.LightOrbEntity;
import com.Maxwell.eschatology.Boss.BlackBool.Entities.VoidLance.VoidLanceEntity;
import com.Maxwell.eschatology.Boss.XF07_Revanant.ExoWither;
import com.Maxwell.eschatology.Config.ModSoundConfig;
import com.Maxwell.eschatology.client.GUI.Glitch.GlitchState;
import com.Maxwell.eschatology.common.Items.Blocks.AltarOfTheEclipse.EclipseManager;
import com.Maxwell.eschatology.common.MaxwellCustomBossEvent;
import com.Maxwell.eschatology.common.Network.*;
import com.Maxwell.eschatology.register.ModItems;
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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlackBool extends Monster {
    private static final EntityDataAccessor<Integer> DATA_ATTACK_PHASE = SynchedEntityData.defineId(BlackBool.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_DEATH_TICKS = SynchedEntityData.defineId(BlackBool.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_IS_PHASE_TWO =
            SynchedEntityData.defineId(BlackBool.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_LASER_TARGET_ID =
            SynchedEntityData.defineId(BlackBool.class, EntityDataSerializers.INT);
    private final MaxwellCustomBossEvent bossEvent;
    private int orbitTick = 0;
    public int chargeCooldown = 0;
    public int gravityOrbCooldown = 0;
    public int barrageCooldown = 0;
    public int voidwavecooldown = 0;
    public int singularityCooldown = 0;
    public int laserCooldown = 0;
    private int deathAnimationTicks = 0;
    private int remainingCharges = 0;
    private int passiveOrbCooldown = 0;
    private boolean hasUsedEventHorizon = false;
    private boolean isCriticallyEnraged = false;
    private boolean isSpawning = true;
    private int spawnAnimationTicks = 0;
    public boolean isMeleeStance = true;
    private int stanceSwitchCooldown = 0;

    public enum AttackPhase {
        IDLE,
        VOID_WAVE,
        CHARGE,
        GRAVITY_ORB,
        BARRAGE,
        VOID_PULSE,
        SINGULARITY,
        VOID_LANCE,
        LASER,
        EVENT_HORIZON,
        OUTER_HORIZON
    }

    public BlackBool(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new BlackBoolMoveControl(this, 20, true);
        this.bossEvent = new MaxwellCustomBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS, 0);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.LAVA, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0F);
        if (!pLevel.isClientSide) {
            this.setHealth(1.0F);
            this.isSpawning = true;
        }
        this.reapplyConfigAttributes();
    }
    private void reapplyConfigAttributes() {

        if (this.getAttribute(Attributes.MAX_HEALTH) != null) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(BlackBoolBalance.MAX_HEALTH);
        }

        if (this.getAttribute(Attributes.ATTACK_DAMAGE) != null) {
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(BlackBoolBalance.ATTACK_DAMAGE);
        }

        if (this.getAttribute(Attributes.ARMOR) != null) {
            this.getAttribute(Attributes.ARMOR).setBaseValue(BlackBoolBalance.ARMOR);
        }

        if (this.getAttribute(Attributes.KNOCKBACK_RESISTANCE) != null) {
            this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(BlackBoolBalance.KNOCKBACK_RESISTANCE);
        }

        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(BlackBoolBalance.MOVEMENT_SPEED);
        }

        if (this.getAttribute(Attributes.FLYING_SPEED) != null) {
            this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(BlackBoolBalance.FLYING_SPEED);
        }
        if (this.getAttribute(Attributes.FOLLOW_RANGE) != null) {
            this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(BlackBoolBalance.FOLLOW_RANGE);
        }
        if (this.getAttribute(Attributes.ATTACK_SPEED) != null) {
            this.getAttribute(Attributes.ATTACK_SPEED).setBaseValue(BlackBoolBalance.ATTACK_SPEED);
        }
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ATTACK_PHASE, AttackPhase.IDLE.ordinal());
        this.entityData.define(DATA_DEATH_TICKS, 0);
        this.entityData.define(DATA_IS_PHASE_TWO, false);
        this.entityData.define(DATA_LASER_TARGET_ID, 0);
    }

    public int getLaserTargetId() {
        return this.entityData.get(DATA_LASER_TARGET_ID);
    }

    public AttackPhase getCurrentAttackPhase() {
        return AttackPhase.values()[this.entityData.get(DATA_ATTACK_PHASE)];
    }

    public void setCurrentAttackPhase(AttackPhase phase) {
        this.entityData.set(DATA_ATTACK_PHASE, phase.ordinal());
    }

    public int getDeathAnimationTicks() {
        return this.entityData.get(DATA_DEATH_TICKS);
    }

    public boolean isPhaseTwo() {
        return this.entityData.get(DATA_IS_PHASE_TWO);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, @NotNull DamageSource pSource) {
        return false;
    }

    @Override
    protected void tickDeath() {
        this.entityData.set(DATA_DEATH_TICKS, this.deathAnimationTicks);
        final int OUTBURST_END_TICK = BlackBoolBalance.DEATH_PHASE1_OUTBURST_TICKS;
        final int COLLAPSE_END_TICK = OUTBURST_END_TICK + BlackBoolBalance.DEATH_PHASE2_COLLAPSE_TICKS;
        final int IMPLOSION_END_TICK = COLLAPSE_END_TICK + BlackBoolBalance.DEATH_PHASE3_IMPLOSION_TICKS;
        final int FINAL_TICK = IMPLOSION_END_TICK + BlackBoolBalance.DEATH_PHASE4_FINAL_TICKS;

        this.deathAnimationTicks++;
        this.setInvulnerable(true);
        this.getNavigation().stop();
        this.setDeltaMovement(Vec3.ZERO);

        if (!this.level().isClientSide) {
            if (this.deathAnimationTicks == 1) {
                this.playSound(SoundEvents.WITHER_HURT, 2.0F, 0.5F);
                this.playSound(SoundEvents.LIGHTNING_BOLT_THUNDER, 1.5F, 1.2F);
            } else if (this.deathAnimationTicks > OUTBURST_END_TICK && this.deathAnimationTicks <= COLLAPSE_END_TICK) {
                if (this.deathAnimationTicks == OUTBURST_END_TICK + 1) {
                    this.playSound(SoundEvents.WARDEN_HEARTBEAT, 3.0F, 0.5F);
                }
                AABB pullArea = this.getBoundingBox().inflate(BlackBoolBalance.DEATH_PULL_AREA_RADIUS);
                List<Player> nearbyPlayers = this.level().getEntitiesOfClass(Player.class, pullArea);
                for (Player player : nearbyPlayers) {
                    if (player.isCreative() || player.isSpectator()) continue;
                    ModMessages.sendToPlayer(new SyncGlitchEffectPacket(0.0f), (ServerPlayer) player);
                    Vec3 pullDir = this.position().subtract(player.position()).normalize();
                    float collapseProgress = (float) (this.deathAnimationTicks - OUTBURST_END_TICK) / (BlackBoolBalance.DEATH_PHASE2_COLLAPSE_TICKS);
                    float strength = (1.0F - collapseProgress) * BlackBoolBalance.DEATH_PULL_STRENGTH;
                    player.setDeltaMovement(player.getDeltaMovement().add(pullDir.scale(strength)));
                    player.hurtMarked = true;
                }
            } else if (this.deathAnimationTicks > COLLAPSE_END_TICK && this.deathAnimationTicks <= IMPLOSION_END_TICK) {
                if (this.deathAnimationTicks == COLLAPSE_END_TICK + 1) {
                    this.playSound(SoundEvents.END_PORTAL_SPAWN, 2.0F, 1.0F);
                    this.playSound(SoundEvents.GLASS_BREAK, 1.5F, 1.5F);
                }
            }

            if (this.deathAnimationTicks >= FINAL_TICK) {
                this.dropExperience();
                this.dropAllDeathLoot(this.getLastDamageSource() != null ? this.getLastDamageSource() : this.damageSources().generic());

                if (!this.level().isClientSide) {
                    for (int i = 0; i < 4; i++) {
                        ItemStack crystal = new ItemStack(ModItems.A_TIME_CRYSTAL.get());
                        this.spawnAtLocation(crystal, 0.5F);
                    }
                    int ingotCount = 1 + this.random.nextInt(3);
                    this.spawnAtLocation(new ItemStack(ModItems.SINGULARITC_INGOT.get(), ingotCount), 0.5F);
                    if (this.random.nextFloat() < BlackBoolBalance.SING_CORE_PERCENT) {
                        this.spawnAtLocation(new ItemStack(ModItems.SINGULARITY_CORE.get()), 0.5F);
                    }

                    ServerLevel serverLevel = (ServerLevel) this.level();
                    serverLevel.sendParticles(ParticleTypes.END_ROD,
                            this.getX(), this.getY() + 1.0, this.getZ(),
                            60, 1.5, 1.5, 1.5, 0.02);
                    serverLevel.playSound(null, this.blockPosition(),
                            SoundEvents.AMETHYST_CLUSTER_BREAK, this.getSoundSource(),
                            2.0F, 0.8F);
                    serverLevel.playSound(null, this.blockPosition(),
                            SoundEvents.END_PORTAL_SPAWN, this.getSoundSource(),
                            1.5F, 1.1F);
                }

                GlitchState.forcedGlitchIntensity = 0.0f;
                ModMessages.sendToAll(new SyncEclipseStatePacket(false));
                this.remove(RemovalReason.KILLED);
            }
        }
    }

    @Override
    public void tick() {
        if (this.deathAnimationTicks > 0) {
            this.tickDeath();
            return;
        }
        super.tick();
        this.setNoGravity(true);
        this.orbitTick++;
        if (!this.level().isClientSide) {
            if (this.getCurrentAttackPhase() != AttackPhase.LASER) {
                captureNearbyProjectiles();
            }
            applyContactDamage();
        }
    }

    public float getHealthPercent() {
        return this.getHealth() / this.getMaxHealth();
    }

    public int getCooldownTicksByHealth(int baseCooldown) {
        float healthPercent = this.getHealthPercent();
        if (healthPercent < BlackBoolBalance.PHASE_TWO_HP_THRESHOLD) {
            return (int) (baseCooldown * (0.25f + (healthPercent * 1.5f)));
        }
        return (int) (baseCooldown * (0.5f + healthPercent));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(0, new FindRevenantGoal(this));
        this.goalSelector.addGoal(1, new EventHorizonGoal(this));
        this.goalSelector.addGoal(2, new ChargeAttackGoal(this));
        this.goalSelector.addGoal(3, new BarrageAttackGoal(this));
        this.goalSelector.addGoal(3, new ShootGravityOrbGoal(this));
        this.goalSelector.addGoal(3, new SingularityGoal(this));
        this.goalSelector.addGoal(3, new LaserAttackGoal(this));
        this.goalSelector.addGoal(8, new RandomFlyGoal(this));
        this.targetSelector.addGoal(1, new FindNearestPlayerGoal(this));
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        if (this.isSpawning) {
            this.spawnAnimationTicks++;
            this.getNavigation().stop();
            this.setDeltaMovement(Vec3.ZERO);
            float progress = (float) this.spawnAnimationTicks / BlackBoolBalance.SPAWN_ANIMATION_DURATION_TICKS;
            float currentHealth = Mth.lerp(progress, 1.0F, this.getMaxHealth());
            this.setHealth(currentHealth);
            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.REVERSE_PORTAL, this.getRandomX(1.5), this.getRandomY(), this.getRandomZ(1.5), 1, 0, 0, 0, 0);
            }
            if (this.spawnAnimationTicks >= BlackBoolBalance.SPAWN_ANIMATION_DURATION_TICKS) {
                this.isSpawning = false;
                this.setHealth(this.getMaxHealth());
                this.playSound(SoundEvents.WITHER_SPAWN, 2.0F, 1.0F);
            }
            return;
        }
        if (this.chargeCooldown > 0) this.chargeCooldown--;
        if (this.gravityOrbCooldown > 0) this.gravityOrbCooldown--;
        if (this.barrageCooldown > 0) this.barrageCooldown--;
        if (this.voidwavecooldown > 0) this.voidwavecooldown--;
        if (this.singularityCooldown > 0) this.singularityCooldown--;
        if (this.laserCooldown > 0) this.laserCooldown--;
        if (this.passiveOrbCooldown > 0) this.passiveOrbCooldown--;
        LivingEntity target = this.getTarget();
        if (target == null || !target.isAlive()) {
            if (!this.onGround()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0, -0.04, 0));
            }
            return;
        }
        this.getLookControl().setLookAt(target, 30.0F, 30.0F);
        if (this.stanceSwitchCooldown > 0) {
            this.stanceSwitchCooldown--;
        } else {
            double distanceToTarget = this.distanceToSqr(target);
            boolean shouldBeMelee = distanceToTarget < BlackBoolBalance.STANCE_SWITCH_MELEE_RANGE_SQ;
            boolean shouldBeRanged = distanceToTarget > BlackBoolBalance.STANCE_SWITCH_RANGED_RANGE_SQ;
            if ((shouldBeMelee && !this.isMeleeStance) || (shouldBeRanged && this.isMeleeStance)) {
                this.isMeleeStance = !this.isMeleeStance;
                this.stanceSwitchCooldown = BlackBoolBalance.STANCE_SWITCH_COOLDOWN_MIN + this.random.nextInt(BlackBoolBalance.STANCE_SWITCH_COOLDOWN_RANDOM);
                if (!this.isMeleeStance) {
                    Vec3 backOffVec = this.position().vectorTo(target.position()).multiply(-1, 0, -1).normalize().scale(1.5D);
                    this.setDeltaMovement(this.getDeltaMovement().add(backOffVec));
                }
            } else if (this.stanceSwitchCooldown <= 0) {
                this.isMeleeStance = !this.isMeleeStance;
                this.stanceSwitchCooldown = BlackBoolBalance.STANCE_SWITCH_FORCED_COOLDOWN_MIN + this.random.nextInt(BlackBoolBalance.STANCE_SWITCH_FORCED_COOLDOWN_RANDOM);
            }
        }
        if (this.getCurrentAttackPhase() == AttackPhase.IDLE) {
            double idealDistance;
            double moveSpeedScale;
            if (this.isMeleeStance) {
                idealDistance = BlackBoolBalance.MELEE_STANCE_IDEAL_DISTANCE;
                moveSpeedScale = BlackBoolBalance.MELEE_STANCE_SPEED_SCALE;
            } else {
                idealDistance = BlackBoolBalance.RANGED_STANCE_IDEAL_DISTANCE;
                moveSpeedScale = BlackBoolBalance.RANGED_STANCE_SPEED_SCALE;
            }
            double deadZone = 1.0D;
            Vec3 directionToTarget = this.position().vectorTo(target.position());
            double distance = directionToTarget.horizontalDistance();
            double targetY = target.getEyeY();
            double currentVerticalSpeed = this.getDeltaMovement().y;
            double targetVerticalSpeed = Mth.clamp((targetY - this.getY()) * 0.1, -0.3D, 0.3D);
            double smoothedVerticalSpeed = Mth.lerp(0.2, currentVerticalSpeed, targetVerticalSpeed);
            Vec3 horizontalDelta;
            if (distance > idealDistance + deadZone) {
                horizontalDelta = directionToTarget.multiply(1.0, 0, 1.0).normalize().scale(moveSpeedScale);
            } else if (distance < idealDistance - deadZone) {
                horizontalDelta = directionToTarget.multiply(1.0, 0, 1.0).normalize().scale(-moveSpeedScale * 0.5);
            } else {
                horizontalDelta = Vec3.ZERO;
            }
            Vec3 finalDelta = this.getDeltaMovement().multiply(0.90, 0.98, 0.90).add(horizontalDelta.x, 0, horizontalDelta.z);
            finalDelta = new Vec3(finalDelta.x, smoothedVerticalSpeed, finalDelta.z);
            double maxSpeed = this.getAttributeValue(Attributes.FLYING_SPEED);
            if (finalDelta.lengthSqr() > maxSpeed * maxSpeed) {
                finalDelta = finalDelta.normalize().scale(maxSpeed);
            }
            this.setDeltaMovement(finalDelta);
        }
        float healthPercent = this.getHealthPercent();
        if (healthPercent <= BlackBoolBalance.CRITICAL_ENRAGE_HP_THRESHOLD && !this.isCriticallyEnraged) {
            this.isCriticallyEnraged = true;
            this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, Integer.MAX_VALUE, 0, false, false, true));
            this.playSound(SoundEvents.WITHER_DEATH, 2.0F, 1.0F);
            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(0.5), this.getZ(), 2, 1.0, 1.0, 1.0, 0.0);
            }
        }
        if (this.tickCount % 20 == 0) {
            float desiredIntensity = getDesiredIntensity(healthPercent);
            for (ServerPlayer player : ((ServerLevel) this.level()).getChunkSource().chunkMap.getPlayers(new ChunkPos(this.blockPosition()), false)) {
                ModMessages.sendToPlayer(new SyncGlitchEffectPacket(desiredIntensity), player);
            }
        }
        if (this.passiveOrbCooldown <= 0) {
            spawnOrbFromSky(target);
            if (healthPercent <= BlackBoolBalance.LANCE_SPAWN_START_HP_THRESHOLD && this.random.nextFloat() < (BlackBoolBalance.LANCE_SPAWN_START_HP_THRESHOLD - healthPercent)) {
                spawnLanceFromSky(target);
            }
            this.passiveOrbCooldown = BlackBoolBalance.PASSIVE_ORB_COOLDOWN_BASE + (int) (BlackBoolBalance.PASSIVE_ORB_COOLDOWN_BASE * healthPercent);
        }
    }

    public boolean canStartNewAttack() {
        if (this.isSpawning || this.getCurrentAttackPhase() != AttackPhase.IDLE) {
            return true;
        }
        LivingEntity target = this.getTarget();
        return target == null || !target.isAlive();
    }

    private float getDesiredIntensity(float healthPercent) {
        float desiredIntensity;
        if (GlitchState.forcedGlitchIntensity > 0.0f) {
            desiredIntensity = GlitchState.forcedGlitchIntensity;
        } else {
            if (healthPercent <= BlackBoolBalance.PHASE_TWO_HP_THRESHOLD && this.isAlive()) {
                desiredIntensity = (BlackBoolBalance.PHASE_TWO_HP_THRESHOLD - healthPercent) * 2.0f;
            } else {
                desiredIntensity = 0.0f;
            }
        }
        desiredIntensity = Mth.clamp(desiredIntensity, 0.0f, 1.0f);
        return desiredIntensity;
    }

    private void spawnOrbFromSky(LivingEntity target) {
        RandomSource random = this.getRandom();
        double offsetX = (random.nextDouble() - 0.5) * 30.0;
        double offsetZ = (random.nextDouble() - 0.5) * 30.0;
        Vec3 spawnPos = target.position().add(offsetX, 20.0, offsetZ);
        LightOrbEntity orb = new LightOrbEntity(this.level(), this, target);
        orb.setPos(spawnPos);
        orb.setDeltaMovement(0, -0.5, 0);
        this.level().addFreshEntity(orb);
    }

    private void spawnLanceFromSky(LivingEntity target) {
        RandomSource random = this.getRandom();
        double offsetX = (random.nextDouble() - 0.5) * 30.0;
        double offsetZ = (random.nextDouble() - 0.5) * 30.0;
        BlockPos targetPos = target.blockPosition().offset((int) offsetX, 0, (int) offsetZ);
        BlockPos groundPos = new BlockPos(targetPos.getX(), (int) target.getY(), targetPos.getZ());
        while (this.level().isEmptyBlock(groundPos) && groundPos.getY() > this.level().getMinBuildHeight()) {
            groundPos = groundPos.below();
        }
        VoidLanceEntity lance = new VoidLanceEntity(this.level(), this);
        lance.setPos(groundPos.getX() + 0.5, groundPos.getY() + 25, groundPos.getZ() + 0.5);
        lance.setTargetPosition(groundPos);
        this.level().addFreshEntity(lance);
    }

    private void applyContactDamage() {
        List<LivingEntity> touchingEntities = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
        for (LivingEntity entity : touchingEntities) {
            if (entity != this && !this.isAlliedTo(entity)) {
                entity.invulnerableTime = 0;
                float contactDamage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE) * BlackBoolBalance.CONTACT_DAMAGE_RATIO;
                entity.hurt(this.damageSources().mobAttack(this), contactDamage);
            }
        }
    }

    private void captureNearbyProjectiles() {
        if (this.getPassengers().size() >= BlackBoolBalance.MAX_ORBITING_PROJECTILES) {
            return;
        }
        AABB scanBox = this.getBoundingBox().inflate(BlackBoolBalance.PROJECTILE_CAPTURE_RADIUS);
        List<Projectile> projectiles = this.level().getEntitiesOfClass(Projectile.class, scanBox,
                (projectile) -> projectile.getOwner() != this && !projectile.isVehicle());
        for (Projectile p : projectiles) {
            if (this.getPassengers().size() < BlackBoolBalance.MAX_ORBITING_PROJECTILES) {
                p.setDeltaMovement(Vec3.ZERO);
                p.startRiding(this, true);
            } else {
                break;
            }
        }
    }

    private int damageCapTicks = 0;
    private float damageThisTick = 0.0F;

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.deathAnimationTicks > 0 || this.isInvulnerable() || this.isSpawning) {
            return false;
        }
        if (this.damageCapTicks == this.tickCount) {
            if (this.damageThisTick >= BlackBoolBalance.DAMAGE_CAP_PER_TICK) {
                return super.hurt(pSource, pAmount * BlackBoolBalance.DAMAGE_CAP_REDUCTION_RATIO);
            }
            this.damageThisTick += pAmount;
        } else {
            this.damageCapTicks = this.tickCount;
            this.damageThisTick = pAmount;
        }
        if (pSource.getDirectEntity() instanceof Projectile) {
            if (!this.getPassengers().isEmpty()) {
                this.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 1.0F);
                Entity deflectedEntity = this.getPassengers().get(0);
                deflectedEntity.stopRiding();
                deflectedEntity.discard();
                return false;
            }
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    public void die(DamageSource pCause) {
        if (this.deathAnimationTicks > 0) return;
        if (pCause.getEntity() instanceof ServerPlayer player) {
            ModTriggers.KILL_BLACK_BOOL.trigger(player);
        }
        super.die(pCause);
        if (this.deathAnimationTicks == 0) {
            this.deathAnimationTicks = 1;
            this.bossEvent.setVisible(false);
        }
        if (!this.level().isClientSide) {
            for (ServerPlayer player : ((ServerLevel) this.level()).getChunkSource().chunkMap.getPlayers(new ChunkPos(this.blockPosition()), false)) {
                ModMessages.sendToPlayer(new PlayBossMusicPacket((ResourceLocation) null), player);
            }
        }
    }

    @Override
    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        if (!this.level().isClientSide) {
            for (ServerPlayer player : ((ServerLevel) this.level()).players()) {
                ModMessages.sendToPlayer(new SyncGlitchEffectPacket(0.0f), player);
                ModMessages.sendToPlayer(new PlayBossMusicPacket((ResourceLocation) null), player);
            }
        }
    }

    @Override
    public void positionRider(Entity pPassenger, MoveFunction pCallback) {
        if (!this.hasPassenger(pPassenger)) {
            return;
        }
        List<Entity> passengers = this.getPassengers();
        int passengerIndex = passengers.indexOf(pPassenger);
        if (passengerIndex < 0) return;
        float orbitDistance = 2.5F;
        float orbitSpeed = 5.0F;
        float verticalOffset = this.getBbHeight() * 0.5F;
        int totalPassengers = passengers.size();
        float angleOffset = (360.0F / totalPassengers) * passengerIndex;
        float currentAngle = (this.orbitTick * orbitSpeed + angleOffset) % 360.0F;
        double angleRad = Math.toRadians(currentAngle);
        double newX = this.getX() + Math.cos(angleRad) * orbitDistance;
        double newY = this.getY() + verticalOffset;
        double newZ = this.getZ() + Math.sin(angleRad) * orbitDistance;
        pCallback.accept(pPassenger, newX, newY, newZ);
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
        if (this.isAlive()) {
            ResourceLocation musicId = ModSoundConfig.getSound(ModSoundConfig.MUSIC_BLACK_BOOL).getLocation();
            ModMessages.sendToPlayer(
                    new PlayBossMusicPacket(musicId),
                    player
            );
        }
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
        ModMessages.sendToPlayer(new PlayBossMusicPacket((ResourceLocation) null), player);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, BlackBoolBalance.MAX_HEALTH)
                .add(Attributes.MOVEMENT_SPEED, BlackBoolBalance.MOVEMENT_SPEED)
                .add(Attributes.ATTACK_DAMAGE, BlackBoolBalance.ATTACK_DAMAGE)
                .add(Attributes.ATTACK_SPEED, BlackBoolBalance.ATTACK_SPEED)
                .add(Attributes.FOLLOW_RANGE, BlackBoolBalance.FOLLOW_RANGE)
                .add(Attributes.KNOCKBACK_RESISTANCE, BlackBoolBalance.KNOCKBACK_RESISTANCE)
                .add(Attributes.ARMOR, BlackBoolBalance.ARMOR)
                .add(Attributes.FLYING_SPEED, BlackBoolBalance.FLYING_SPEED);
    }

    static class RandomFlyGoal extends Goal {
        private final BlackBool mob;

        public RandomFlyGoal(BlackBool pMob) {
            this.mob = pMob;
        }

        @Override
        public boolean canUse() {
            if (this.mob.getCurrentAttackPhase() == AttackPhase.LASER) return false;
            return this.mob.getTarget() == null && !this.mob.getMoveControl().hasWanted();
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            RandomSource random = this.mob.getRandom();
            double d = BlackBoolBalance.RANDOM_FLY_RADIUS;
            double targetX = this.mob.getX() + (random.nextDouble() * 2.0D - 1.0D) * d;
            double targetY = this.mob.getY() + (random.nextDouble() * 2.0D - 1.0D) * d;
            double targetZ = this.mob.getZ() + (random.nextDouble() * 2.0D - 1.0D) * d;
            targetY = Mth.clamp(targetY, this.mob.level().getMinBuildHeight() + 10, 250);
            this.mob.getMoveControl().setWantedPosition(targetX, targetY, targetZ, 1.0D);
        }
    }

    static class FindNearestPlayerGoal extends Goal {
        private final BlackBool mob;
        private Player targetPlayer;

        public FindNearestPlayerGoal(BlackBool mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            if (this.mob.getCurrentAttackPhase() == AttackPhase.LASER || this.mob.isSpawning) return false;
            if (this.mob.getTarget() != null && this.mob.getTarget().isAlive()) {
                return false;
            }
            List<? extends Player> players = this.mob.level().players();
            Player closestPlayer = null;
            double closestDistanceSq = -1.0D;
            for (Player player : players) {
                if (player.isCreative() || player.isSpectator()) continue;
                double distanceSq = this.mob.distanceToSqr(player);
                double searchDistance = this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
                if (distanceSq > searchDistance * searchDistance) continue;
                if (closestDistanceSq == -1.0D || distanceSq < closestDistanceSq) {
                    closestDistanceSq = distanceSq;
                    closestPlayer = player;
                }
            }
            if (closestPlayer != null) {
                this.targetPlayer = closestPlayer;
                return true;
            }
            return false;
        }

        @Override
        public void start() {
            this.mob.setTarget(this.targetPlayer);
            super.start();
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }
    }

    static class ChargeAttackGoal extends Goal {
        private final BlackBool mob;
        private LivingEntity target;
        private int chargeTicks;
        private Vec3 chargeDirection;
        private Vec3 startPosition;
        private final Set<LivingEntity> hitEntities = new HashSet<>();

        private enum Phase {PREPARING, CHARGING, COOLDOWN}

        private Phase phase = Phase.COOLDOWN;

        public ChargeAttackGoal(BlackBool mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canContinueToUse() {
            return (this.target != null && this.target.isAlive()) && (this.phase != Phase.COOLDOWN || this.mob.remainingCharges > 0);
        }

        @Override
        public boolean canUse() {
            if (this.mob.canStartNewAttack()) return false;
            this.target = this.mob.getTarget();
            if (this.target == null) return false;
            double distanceSq = this.mob.distanceToSqr(this.target);
            return this.mob.chargeCooldown <= 0 && distanceSq > BlackBoolBalance.CHARGE_MIN_DISTANCE_SQ && distanceSq < BlackBoolBalance.CHARGE_MAX_DISTANCE_SQ;
        }

        @Override
        public void start() {
            this.mob.chargeCooldown = this.mob.getCooldownTicksByHealth(BlackBoolBalance.CHARGE_COOLDOWN_BASE);
            if (this.mob.remainingCharges <= 0) {
                this.mob.remainingCharges = this.mob.getHealthPercent() <= BlackBoolBalance.PHASE_TWO_HP_THRESHOLD ? BlackBoolBalance.CHARGE_COUNT_ENRAGED : BlackBoolBalance.CHARGE_COUNT_NORMAL;
            }
            this.mob.setCurrentAttackPhase(AttackPhase.CHARGE);
            this.target = this.mob.getTarget();
            this.phase = Phase.PREPARING;
            this.chargeTicks = 0;
            this.hitEntities.clear();
            this.mob.getNavigation().stop();
        }

        @Override
        public void stop() {
            this.mob.setDeltaMovement(Vec3.ZERO);
            if (this.phase == Phase.CHARGING) {
                if (!this.mob.level().isClientSide) {
                    for (int i = 0; i < 3; i++) {
                        double angle = this.mob.random.nextDouble() * 2.0 * Math.PI;
                        double distance = 4.0 + this.mob.random.nextDouble() * 3.0;
                        double offsetX = Math.cos(angle) * distance;
                        double offsetZ = Math.sin(angle) * distance;
                        BlockPos targetPos = this.mob.blockPosition().offset((int) offsetX, 0, (int) offsetZ);
                        BlockPos groundPos = new BlockPos(targetPos.getX(), (int) this.mob.getY(), targetPos.getZ());
                        while (this.mob.level().isEmptyBlock(groundPos) && groundPos.getY() > this.mob.level().getMinBuildHeight()) {
                            groundPos = groundPos.below();
                        }
                        VoidLanceEntity lance = new VoidLanceEntity(this.mob.level(), this.mob);
                        lance.setPos(groundPos.getX() + 0.5, groundPos.getY() + 25, groundPos.getZ() + 0.5);
                        lance.setTargetPosition(groundPos);
                        if (this.mob.getHealthPercent() <= BlackBoolBalance.PHASE_TWO_HP_THRESHOLD) {
                            lance.setShouldCreateRift(true);
                        }
                        this.mob.level().addFreshEntity(lance);
                    }
                }
                this.mob.playSound(SoundEvents.GENERIC_EXPLODE, 2.0F, 1.0F);
                AABB area = this.mob.getBoundingBox().inflate(BlackBoolBalance.CHARGE_END_EXPLOSION_RADIUS);
                List<Player> players = this.mob.level().getEntitiesOfClass(Player.class, area);
                for (Player player : players) {
                    if (player.isCreative() || player.isSpectator()) continue;
                    Vec3 knockbackDir = player.position().subtract(this.mob.position()).normalize();
                    player.knockback(BlackBoolBalance.CHARGE_END_EXPLOSION_KNOCKBACK, -knockbackDir.x, -knockbackDir.z);
                    player.hurt(this.mob.damageSources().mobAttack(this.mob), (float) this.mob.getAttributeValue(Attributes.ATTACK_DAMAGE) * BlackBoolBalance.CHARGE_END_EXPLOSION_DAMAGE_RATIO);
                }
                if (this.mob.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.EXPLOSION, this.mob.getX(), this.mob.getY(0.5), this.mob.getZ(), 5, 2.0, 0.5, 2.0, 0.0);
                }
            }
            this.mob.remainingCharges--;
            if (this.mob.remainingCharges > 0 && this.target != null && this.target.isAlive()) {
                this.start();
            } else {
                this.mob.setCurrentAttackPhase(AttackPhase.IDLE);
                this.phase = Phase.COOLDOWN;
            }
        }

        @Override
        public void tick() {
            if (this.target == null || !this.target.isAlive()) {
                this.mob.remainingCharges = 0;
                this.stop();
                return;
            }
            this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
            switch (this.phase) {
                case PREPARING:
                    this.chargeTicks++;
                    Vec3 prepPos = this.target.position().subtract(this.mob.position()).normalize().scale(-12.0D).add(this.target.position());
                    this.mob.getNavigation().moveTo(prepPos.x, this.target.getY() + 2.0, prepPos.z, 1.5D);
                    if (this.chargeTicks >= BlackBoolBalance.CHARGE_PREPARATION_TICKS) {
                        this.chargeDirection = this.target.getEyePosition().subtract(this.mob.getEyePosition()).normalize();
                        this.mob.playSound(SoundEvents.ENDER_DRAGON_GROWL, 2.0F, 1.2F);
                        this.startPosition = this.mob.position();
                        this.phase = Phase.CHARGING;
                        this.chargeTicks = 0;
                        this.mob.getNavigation().stop();
                    }
                    break;
                case CHARGING:
                    this.chargeTicks++;
                    Vec3 velocity = this.chargeDirection.scale(BlackBoolBalance.CHARGE_SPEED);
                    this.mob.move(MoverType.SELF, velocity);
                    this.mob.setDeltaMovement(velocity);
                    AABB chargeHitbox = this.mob.getBoundingBox().inflate(1.0D, 0.5D, 1.0D);
                    List<LivingEntity> nearbyEntities = this.mob.level().getEntitiesOfClass(LivingEntity.class, chargeHitbox);
                    for (LivingEntity entity : nearbyEntities) {
                        if (entity != this.mob && !this.mob.isAlliedTo(entity) && !this.hitEntities.contains(entity)) {
                            this.mob.doHurtTarget(entity);
                            this.hitEntities.add(entity);
                            this.mob.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1.5F, 0.8F);
                            if (entity instanceof Player player && player.isBlocking()) {
                                player.disableShield(true);
                                this.mob.playSound(SoundEvents.SHIELD_BREAK, 2.0F, 0.8F);
                                this.mob.isMeleeStance = false;
                                this.mob.stanceSwitchCooldown = BlackBoolBalance.STANCE_SWITCH_FORCED_COOLDOWN_MIN;
                                this.mob.remainingCharges = 0;
                                Vec3 recoilVec = this.chargeDirection.scale(-1.0D);
                                this.mob.setDeltaMovement(recoilVec);
                                this.stop();
                                return;
                            }
                        }
                    }
                    double distanceTraveledSq = this.mob.position().distanceToSqr(this.startPosition);
                    boolean timedOut = this.chargeTicks >= BlackBoolBalance.CHARGE_TIMEOUT_TICKS;
                    boolean maxDistanceReached = distanceTraveledSq > BlackBoolBalance.CHARGE_MAX_TRAVEL_DISTANCE * BlackBoolBalance.CHARGE_MAX_TRAVEL_DISTANCE;
                    if (timedOut || maxDistanceReached || this.mob.horizontalCollision) {
                        this.stop();
                    }
                    break;
                case COOLDOWN:
                    break;
            }
        }
    }

    static class ShootGravityOrbGoal extends Goal {
        private final BlackBool mob;
        private int chargeUpTicks;

        public ShootGravityOrbGoal(BlackBool mob) {
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            if (this.mob.canStartNewAttack()) return false;
            LivingEntity target = this.mob.getTarget();
            if (target == null) return false;
            return this.mob.gravityOrbCooldown <= 0 &&
                    !this.mob.isMeleeStance &&
                    this.mob.distanceToSqr(target) > BlackBoolBalance.GRAVITY_ORB_MIN_DISTANCE_SQ;
        }

        @Override
        public void start() {
            this.mob.gravityOrbCooldown = this.mob.getCooldownTicksByHealth(BlackBoolBalance.GRAVITY_ORB_COOLDOWN_BASE);
            this.chargeUpTicks = 0;
            this.mob.setCurrentAttackPhase(AttackPhase.GRAVITY_ORB);
            this.mob.getNavigation().stop();
        }

        @Override
        public boolean canContinueToUse() {
            return this.chargeUpTicks < BlackBoolBalance.GRAVITY_ORB_CHARGE_UP_TICKS && this.mob.getTarget() != null;
        }

        @Override
        public void stop() {
            this.mob.setCurrentAttackPhase(AttackPhase.IDLE);
            LivingEntity target = this.mob.getTarget();
            if (this.chargeUpTicks >= BlackBoolBalance.GRAVITY_ORB_CHARGE_UP_TICKS && target != null) {
                this.mob.playSound(SoundEvents.WITHER_SHOOT, 2.0F, 1.0F);
                GravityOrbEntity orb = new GravityOrbEntity(this.mob.level(), this.mob, target);
                this.mob.level().addFreshEntity(orb);
                this.mob.isMeleeStance = true;
                this.mob.stanceSwitchCooldown = BlackBoolBalance.STANCE_SWITCH_FORCED_COOLDOWN_MIN + this.mob.random.nextInt(BlackBoolBalance.STANCE_SWITCH_FORCED_COOLDOWN_RANDOM);
            }
        }

        @Override
        public void tick() {
            if (this.mob.getTarget() == null) return;
            this.mob.getLookControl().setLookAt(this.mob.getTarget());
            this.mob.getNavigation().stop();
            this.chargeUpTicks++;
            if (this.mob.level().isClientSide) {
                Vec3 lookVec = this.mob.getLookAngle();
                Vec3 particlePos = this.mob.position().add(lookVec.scale(2.0D)).add(0, 1.5, 0);
                this.mob.level().addParticle(ParticleTypes.WITCH, particlePos.x, particlePos.y, particlePos.z, 0, 0, 0);
            }
        }
    }

    static class BarrageAttackGoal extends Goal {
        private final BlackBool mob;
        private int chargeUpTicks;
        private int firingTicks;

        private enum Phase {IDLE, CHARGING, FIRING}

        private Phase phase = Phase.IDLE;

        public BarrageAttackGoal(BlackBool mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.mob.canStartNewAttack()) return false;
            LivingEntity target = this.mob.getTarget();
            if (target == null) return false;
            return this.mob.barrageCooldown <= 0 &&
                    !this.mob.isMeleeStance &&
                    this.mob.distanceToSqr(target) > BlackBoolBalance.BARRAGE_MIN_DISTANCE_SQ;
        }

        @Override
        public boolean canContinueToUse() {
            return (this.phase != Phase.IDLE) && this.mob.getTarget() != null && this.mob.getTarget().isAlive();
        }

        @Override
        public void start() {
            this.phase = Phase.CHARGING;
            this.mob.setCurrentAttackPhase(AttackPhase.BARRAGE);
            this.chargeUpTicks = 0;
            this.firingTicks = 0;
            this.mob.barrageCooldown = this.mob.getCooldownTicksByHealth(BlackBoolBalance.BARRAGE_COOLDOWN_BASE);
            this.mob.getNavigation().stop();
        }

        @Override
        public void stop() {
            this.phase = Phase.IDLE;
            this.mob.setCurrentAttackPhase(AttackPhase.IDLE);
        }

        @Override
        public void tick() {
            LivingEntity target = this.mob.getTarget();
            if (target == null) {
                this.stop();
                return;
            }
            this.mob.getLookControl().setLookAt(target);
            if (this.phase == Phase.CHARGING) {
                this.chargeUpTicks++;
                if (this.chargeUpTicks >= BlackBoolBalance.BARRAGE_CHARGE_UP_TICKS) {
                    this.phase = Phase.FIRING;
                }
            } else if (this.phase == Phase.FIRING) {
                this.firingTicks++;
                boolean isEnraged = this.mob.getHealthPercent() <= BlackBoolBalance.PHASE_TWO_HP_THRESHOLD;
                int duration = isEnraged ? BlackBoolBalance.BARRAGE_DURATION_ENRAGED : BlackBoolBalance.BARRAGE_DURATION_NORMAL;
                int fireRate = isEnraged ? BlackBoolBalance.BARRAGE_FIRE_RATE_ENRAGED : BlackBoolBalance.BARRAGE_FIRE_RATE_NORMAL;
                if (this.firingTicks % fireRate == 0) {
                    boolean rightSide = this.firingTicks % (fireRate * 2) == 0;
                    fireBarrageShot(target, rightSide);
                }
                if (this.firingTicks >= duration) {
                    this.stop();
                }
            }
        }

        private void fireBarrageShot(LivingEntity target, boolean rightSide) {
            this.mob.playSound(SoundEvents.BLAZE_SHOOT, 1.0F, 1.5F);
            Vec3 hardpoint = new Vec3(rightSide ? -2.0D : 2.0D, 1.5D, 0.5D);
            hardpoint = hardpoint.yRot(-this.mob.yBodyRot * ((float) Math.PI / 180F));
            Vec3 spawnPos = this.mob.position().add(hardpoint);
            Vec3 targetSideOffset = target.getLookAngle().cross(new Vec3(0, 1, 0)).normalize().scale(rightSide ? 3.0D : -3.0D);
            Vec3 aimPos = target.getEyePosition().add(targetSideOffset);
            Vec3 direction = aimPos.subtract(spawnPos).normalize();
            LightOrbEntity orb = new LightOrbEntity(this.mob.level(), this.mob, target);
            orb.setPos(spawnPos);
            orb.shoot(direction);
            this.mob.level().addFreshEntity(orb);
        }
    }

    static class LaserAttackGoal extends Goal {
        private final BlackBool mob;
        private int chargeUpTicks = 0;

        public LaserAttackGoal(BlackBool mob) {
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            if (this.mob.canStartNewAttack()) return false;
            return !this.mob.isMeleeStance &&
                    this.mob.laserCooldown <= 0 &&
                    this.mob.getHealthPercent() <= BlackBoolBalance.LASER_HP_THRESHOLD;
        }

        @Override
        public void start() {
            this.mob.laserCooldown = this.mob.getCooldownTicksByHealth(BlackBoolBalance.LASER_COOLDOWN_BASE);
            this.chargeUpTicks = 0;
            this.mob.setCurrentAttackPhase(BlackBool.AttackPhase.LASER);
            this.mob.getNavigation().stop();
            this.mob.playSound(SoundEvents.ENDER_DRAGON_GROWL, 3.0F, 1.5F);
        }

        @Override
        public boolean canContinueToUse() {
            return this.chargeUpTicks < BlackBoolBalance.LASER_CHARGE_UP_TICKS && this.mob.getTarget() != null && this.mob.getTarget().isAlive();
        }

        @Override
        public void stop() {
            if (this.chargeUpTicks >= BlackBoolBalance.LASER_CHARGE_UP_TICKS) {
                LivingEntity target = this.mob.getTarget();
                if (target != null && !this.mob.level().isClientSide) {
                    boolean shouldLeaveField = this.mob.getHealthPercent() <= BlackBoolBalance.LASER_FIELD_HP_THRESHOLD;
                    float damage = (float) this.mob.getAttributeValue(Attributes.ATTACK_DAMAGE) * BlackBoolBalance.LASER_DAMAGE_RATIO;
                    EndLaserBeamEntity laser = new EndLaserBeamEntity(this.mob.level(), this.mob, target, BlackBoolBalance.LASER_DURATION_TICKS, damage, shouldLeaveField);
                    this.mob.level().addFreshEntity(laser);
                    AABB trackingRange = this.mob.getBoundingBox().inflate(BlackBoolBalance.FOLLOW_RANGE);
                    for (ServerPlayer player : this.mob.level().getEntitiesOfClass(ServerPlayer.class, trackingRange)) {
                        ModMessages.sendToPlayer(new SyncLaserEffectsPacket(SyncLaserEffectsPacket.EffectPhase.START_FIRING), player);
                    }
                }
            }
            this.mob.setCurrentAttackPhase(BlackBool.AttackPhase.IDLE);
        }

        @Override
        public void tick() {
            this.chargeUpTicks++;
            LivingEntity target = this.mob.getTarget();
            if (target != null) {
                this.mob.getLookControl().setLookAt(target, 10.0F, 10.0F);
            }
        }
    }

    static class EventHorizonGoal extends Goal {
        private final BlackBool mob;
        private int castTime = 0;

        public EventHorizonGoal(BlackBool mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return !this.mob.hasUsedEventHorizon &&
                    this.mob.getHealthPercent() > BlackBoolBalance.EVENT_HORIZON_HP_THRESHOLD &&
                    this.mob.getTarget() != null;
        }

        @Override
        public boolean canContinueToUse() {
            return this.castTime < BlackBoolBalance.EVENT_HORIZON_CAST_TIME_TICKS;
        }

        @Override
        public void start() {
            this.castTime = 0;
            this.mob.hasUsedEventHorizon = true;
            this.mob.setCurrentAttackPhase(AttackPhase.IDLE);
            this.mob.getNavigation().stop();
            this.mob.setInvulnerable(true);
            this.mob.playSound(SoundEvents.WITHER_SPAWN, 2.0F, 0.8F);
        }

        @Override
        public void tick() {
            this.castTime++;
            if (this.mob.getTarget() != null) {
                this.mob.getLookControl().setLookAt(this.mob.getTarget());
            }
            if (this.mob.level().isClientSide) {
                for (int i = 0; i < 5; i++) {
                    double angle = (this.castTime * 8 + i * 72) * (Math.PI / 180D);
                    double radius = 1.0D + (this.castTime / 20.0);
                    double px = this.mob.getX() + Math.cos(angle) * radius;
                    double pz = this.mob.getZ() + Math.sin(angle) * radius;
                    this.mob.level().addParticle(ParticleTypes.REVERSE_PORTAL, px, this.mob.getRandomY(), pz, 0, 0, 0);
                }
            }
        }

        @Override
        public void stop() {
            this.mob.setInvulnerable(false);
            if (!this.mob.level().isClientSide && this.castTime >= BlackBoolBalance.EVENT_HORIZON_CAST_TIME_TICKS) {
                EventHorizonControllerEntity controller = new EventHorizonControllerEntity(this.mob.level(), this.mob);
                this.mob.level().addFreshEntity(controller);
                this.mob.playSound(SoundEvents.ENDER_DRAGON_DEATH, 2.0F, 1.2F);
            }
        }
    }

    static class FindRevenantGoal extends Goal {
        private final BlackBool mob;
        private LivingEntity target;

        public FindRevenantGoal(BlackBool mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            if (this.mob.isSpawning || this.mob.getCurrentAttackPhase() != AttackPhase.IDLE) return false;
            if (this.mob.getTarget() instanceof ExoWither revenant && revenant.isAlive()) {
                return false;
            }
            List<ExoWither> revenants = this.mob.level().getEntitiesOfClass(
                    ExoWither.class,
                    this.mob.getBoundingBox().inflate(BlackBoolBalance.REVENANT_DETECT_RADIUS)
            );
            if (!revenants.isEmpty()) {
                this.target = revenants.get(0);
                return true;
            }
            return false;
        }

        @Override
        public void start() {
            this.mob.setTarget(this.target);
            this.mob.playSound(SoundEvents.WITHER_SPAWN, 3.0F, 0.6F);
            super.start();
        }

        @Override
        public boolean canContinueToUse() {
            return this.target != null && this.target.isAlive();
        }

        @Override
        public void stop() {
            this.target = null;
        }
    }
}