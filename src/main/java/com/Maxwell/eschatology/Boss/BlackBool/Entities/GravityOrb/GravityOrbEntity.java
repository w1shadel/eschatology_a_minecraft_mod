package com.Maxwell.eschatology.Boss.BlackBool.Entities.GravityOrb;

import com.Maxwell.eschatology.register.ModEffects;
import com.Maxwell.eschatology.register.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class GravityOrbEntity extends Entity {    private static final EntityDataAccessor<Integer> DATA_OWNER_ID = SynchedEntityData.defineId(GravityOrbEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_TARGET_ID = SynchedEntityData.defineId(GravityOrbEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_PHASE = SynchedEntityData.defineId(GravityOrbEntity.class, EntityDataSerializers.INT);    private LivingEntity owner;
    private UUID ownerUUID;
    private LivingEntity target;
    private UUID targetUUID;    private int lifeTicks = 0;    private enum Phase { TRAVELING, PULLING }    public GravityOrbEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
    }    public GravityOrbEntity(Level pLevel, LivingEntity pOwner, LivingEntity pTarget) {
        this(ModEntities.GRAVITY_ORB.get(), pLevel);
        this.setOwner(pOwner);
        this.setTarget(pTarget);
        Vec3 spawnPos = pOwner.position().add(pOwner.getLookAngle().scale(2.0D)).add(0, pOwner.getEyeHeight() - 0.5D, 0);
        this.setPos(spawnPos);
    }    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_OWNER_ID, 0);
        this.entityData.define(DATA_TARGET_ID, 0);
        this.entityData.define(DATA_PHASE, Phase.TRAVELING.ordinal());
    }    private Phase getPhase() {        return Phase.values()[this.entityData.get(DATA_PHASE)];
    }    private void setPhase(Phase phase) {        if (getPhase() != phase && !this.level().isClientSide) {
            this.entityData.set(DATA_PHASE, phase.ordinal());
        }
    }    @Override
    public void tick() {
        super.tick();
        this.lifeTicks++;        if (this.lifeTicks > 400) { 
            this.discard();
            return;
        }        Phase currentPhase = getPhase();        if (this.level().isClientSide) {
            if (getPhase() == Phase.TRAVELING) {
                this.setPos(this.getX() + this.getDeltaMovement().x, this.getY() + this.getDeltaMovement().y, this.getZ() + this.getDeltaMovement().z);
            }
            if (currentPhase == Phase.TRAVELING) {
                for (int i = 0; i < 2; ++i) {
                    this.level().addParticle(ParticleTypes.SQUID_INK, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
                }
            } else if (currentPhase == Phase.PULLING) {
                for (int i = 0; i < 5; ++i) {
                    double angle = (this.lifeTicks * 10 + i * 72) % 360 * (Math.PI / 180D);
                    double radius = 1.0D + (this.random.nextDouble() * 2.0);
                    double px = this.getX() + Math.cos(angle) * radius;
                    double pz = this.getZ() + Math.sin(angle) * radius;
                    Vec3 toCenter = this.position().subtract(px, this.getY(), pz).normalize().scale(0.5);
                    this.level().addParticle(ParticleTypes.PORTAL, px, this.getY(), pz, toCenter.x, 0, toCenter.z);
                }
            }
        }        if (!this.level().isClientSide) {
            LivingEntity currentOwner = getOwner();
            if (currentOwner == null || !currentOwner.isAlive()) {
                this.discard();
                return;
            }            switch (currentPhase) {
                case TRAVELING:
                    LivingEntity currentTarget = getTarget();
                    if (currentTarget != null && currentTarget.isAlive()) {
                        Vec3 direction = currentTarget.position().subtract(this.position()).normalize();
                        this.setDeltaMovement(direction.scale(0.4D));
                        if (this.distanceToSqr(currentTarget) < 9.0D) {
                            this.startPullingPhase();
                        }
                    }
                    if (this.lifeTicks > 80) {
                        this.startPullingPhase();
                    }
                    break;                case PULLING:
                    if (this.lifeTicks < 160) { 
                        float pullRadius = 12.0F;
                        AABB pullBox = this.getBoundingBox().inflate(pullRadius);
                        List<LivingEntity> entitiesToPull = this.level().getEntitiesOfClass(LivingEntity.class, pullBox);                        for (LivingEntity entity : entitiesToPull) {
                            if (!entity.is(currentOwner) && !currentOwner.isAlliedTo(entity)) {
                                double distanceFactor = 1.0 - (entity.distanceTo(this) / pullRadius);
                                entity.fallDistance = 0.0F;
                                Vec3 horizontalPullDir = new Vec3(this.getX() - entity.getX(), 0, this.getZ() - entity.getZ()).normalize();
                                double pullStrength = 0.6D * distanceFactor;
                                Vec3 currentDelta = entity.getDeltaMovement();
                                entity.setDeltaMovement(
                                        currentDelta.x + horizontalPullDir.x * pullStrength,
                                        currentDelta.y, 
                                        currentDelta.z + horizontalPullDir.z * pullStrength
                                );
                                entity.fallDistance = 0.0F;                                entity.addEffect(new MobEffectInstance(ModEffects.GRAVITY_BOUND.get(), 20, 0, false, false, true));                                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2, false, false, true));                                if (this.lifeTicks % 10 == 0) {
                                    float dotDamage = (float) currentOwner.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.1F;
                                    entity.hurt(this.damageSources().mobAttack(currentOwner), dotDamage);
                                }
                            }
                        }
                    } else {                        float explosionDamage = (float) currentOwner.getAttributeValue(Attributes.ATTACK_DAMAGE) * 2.0F;
                        AABB explosionArea = this.getBoundingBox().inflate(6.0D);
                        List<LivingEntity> affectedEntities = this.level().getEntitiesOfClass(LivingEntity.class, explosionArea);
                        for (LivingEntity entity : affectedEntities) {
                            if (!entity.is(currentOwner) && !currentOwner.isAlliedTo(entity)) {
                                entity.hurt(this.damageSources().mobAttack( currentOwner), explosionDamage);
                            }
                        }
                        if (this.level() instanceof ServerLevel serverLevel) {
                            serverLevel.explode(this, this.getX(), this.getY(), this.getZ(), 4.0F, Level.ExplosionInteraction.NONE);
                        }
                        this.discard();
                    }
                    break;
            }
        }    }    
    private void startPullingPhase() {
        if (getPhase() == Phase.TRAVELING) {
            this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.5F, 0.8F);
            this.setDeltaMovement(Vec3.ZERO);
            this.setPhase(Phase.PULLING);
            this.lifeTicks = 0; 
        }
    }    public void setOwner(@Nullable LivingEntity pOwner) {
        if (pOwner != null) {
            this.owner = pOwner;
            this.ownerUUID = pOwner.getUUID();
            this.entityData.set(DATA_OWNER_ID, pOwner.getId());
        }
    }    @Nullable
    public LivingEntity getOwner() {
        if (this.owner != null && this.owner.isAlive()) return this.owner;
        if (this.level() instanceof ServerLevel sLevel && this.ownerUUID != null) {
            Entity entity = sLevel.getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) { this.owner = (LivingEntity) entity; return this.owner; }
        } else if (this.level().isClientSide) {
            Entity entity = this.level().getEntity(this.entityData.get(DATA_OWNER_ID));
            if (entity instanceof LivingEntity) { this.owner = (LivingEntity) entity; return this.owner; }
        }
        return null;
    }    public void setTarget(@Nullable LivingEntity pTarget) {
        if (pTarget != null) {
            this.target = pTarget;
            this.targetUUID = pTarget.getUUID();
            this.entityData.set(DATA_TARGET_ID, pTarget.getId());
        }
    }    @Nullable
    public LivingEntity getTarget() {
        if (this.target != null && this.target.isAlive()) return this.target;
        if (this.level() instanceof ServerLevel sLevel && this.targetUUID != null) {
            Entity entity = sLevel.getEntity(this.targetUUID);
            if (entity instanceof LivingEntity) { this.target = (LivingEntity) entity; return this.target; }
        } else if (this.level().isClientSide) {
            Entity entity = this.level().getEntity(this.entityData.get(DATA_TARGET_ID));
            if (entity instanceof LivingEntity) { this.target = (LivingEntity) entity; return this.target; }
        }
        return null;
    }    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        if (this.ownerUUID != null) pCompound.putUUID("Owner", this.ownerUUID);
        if (this.targetUUID != null) pCompound.putUUID("Target", this.targetUUID);
    }    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.hasUUID("Owner")) this.ownerUUID = pCompound.getUUID("Owner");
        if (pCompound.hasUUID("Target")) this.targetUUID = pCompound.getUUID("Target");
    }    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}