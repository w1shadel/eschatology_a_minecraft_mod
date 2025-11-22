package com.Maxwell.eschatology.Boss.BlackBool.Entities.Singularity;

import com.Maxwell.eschatology.register.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

public class SingularityEntity extends Entity {    private transient LivingEntity owner;
    private UUID ownerUUID;
    private int lifeTime = 140; 
    private float pullRadius = 12.0f;
    private float pullStrength = 0.15f;
    private float damageRadius = 2.5f;    public SingularityEntity(EntityType<? extends SingularityEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
    }    public SingularityEntity(Level pLevel, LivingEntity pOwner) {
        this(ModEntities.SINGULARITY.get(), pLevel);
        this.setOwner(pOwner); 
    }    public void setOwner(LivingEntity pOwner) {
        this.owner = pOwner;
        if (pOwner != null) {
            this.ownerUUID = pOwner.getUUID();
        }
    }    public LivingEntity getOwner() {        if (this.owner == null && this.ownerUUID != null && !this.level().isClientSide) {
            Entity ownerEntity = ((ServerLevel)this.level()).getEntity(this.ownerUUID);
            if (ownerEntity instanceof LivingEntity) {
                this.owner = (LivingEntity) ownerEntity;
            }
        }
        return this.owner;
    }    @Override
    public void tick() {
        super.tick();        if (!this.level().isClientSide) {            getOwner();
        }        if (this.level().isClientSide) {            float ageInTicks = this.tickCount;            for (int i = 0; i < 5; i++) {
                this.level().addParticle(ParticleTypes.SQUID_INK, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            }            for (int i = 0; i < 40; i++) {
                float angle = (ageInTicks * 15 + i * 36) % 360;
                float radius = 1.0F + (ageInTicks / lifeTime) * 0.5f; 
                double x = this.getX() + radius * Math.cos(Math.toRadians(angle));
                double z = this.getZ() + radius * Math.sin(Math.toRadians(angle));
                this.level().addParticle(ParticleTypes.WITCH, x, this.getY(), z, 0, 0, 0);
            }            for (int i = 0; i < 3; i++) {
                Vec3 randomPos = new Vec3(
                        this.getX() + (this.random.nextDouble() - 0.5) * pullRadius * 2,
                        this.getY() + (this.random.nextDouble() - 0.5) * pullRadius * 2,
                        this.getZ() + (this.random.nextDouble() - 0.5) * pullRadius * 2
                );
                Vec3 directionToCenter = this.position().subtract(randomPos).normalize().scale(0.5);
                this.level().addParticle(ParticleTypes.PORTAL, randomPos.x, randomPos.y, randomPos.z, directionToCenter.x, directionToCenter.y, directionToCenter.z);
            }        } else {            if (this.tickCount > this.lifeTime) {                explode();
                this.discard();
                return;
            }            AABB pullArea = this.getBoundingBox().inflate(this.pullRadius);
            List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, pullArea);
            LivingEntity currentOwner = getOwner();             for(LivingEntity entity : entities) {                if (entity != currentOwner && (currentOwner == null || !entity.isAlliedTo(currentOwner))) {
                    Vec3 pullDirection = this.position().subtract(entity.position()).normalize();
                    entity.addDeltaMovement(pullDirection.scale(this.pullStrength));
                }
            }
        }
    }    private void explode() {
        if (!this.level().isClientSide) {
            this.level().playSound(null, this.blockPosition(), SoundEvents.GENERIC_EXPLODE, this.getSoundSource(), 2.0f, 0.8f);
            AABB damageArea = this.getBoundingBox().inflate(this.damageRadius);
            List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, damageArea);
            LivingEntity currentOwner = getOwner();             if (currentOwner == null) return;            for (LivingEntity entity : entities) {
                if (entity != currentOwner && !entity.isAlliedTo(currentOwner)) {
                    float damage = (float) currentOwner.getAttributeValue(Attributes.ATTACK_DAMAGE);                    entity.hurt(this.damageSources().indirectMagic(this, currentOwner), damage);
                }
            }
        }
    }    @Override
    protected void defineSynchedData() {}    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.hasUUID("Owner")) {
            this.ownerUUID = pCompound.getUUID("Owner");
        }
    }    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        if (this.ownerUUID != null) {
            pCompound.putUUID("Owner", this.ownerUUID);
        }
    }
}