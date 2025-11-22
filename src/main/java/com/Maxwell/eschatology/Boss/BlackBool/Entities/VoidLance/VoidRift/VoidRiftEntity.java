package com.Maxwell.eschatology.Boss.BlackBool.Entities.VoidLance.VoidRift;

import com.Maxwell.eschatology.register.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.UUID;

public class VoidRiftEntity extends Entity {
    private LivingEntity owner;
    private UUID ownerUUID;
    private int lifeTime = 200; 
    private float radius = 3.0f;     public VoidRiftEntity(EntityType<? extends VoidRiftEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
    }    public VoidRiftEntity(Level pLevel, LivingEntity pOwner) {
        this(ModEntities.VOID_RIFT.get(), pLevel);
        this.owner = pOwner;
        this.ownerUUID = pOwner.getUUID();
    }    @Override
    public void tick() {
        super.tick();        if (!this.level().isClientSide && this.owner == null) {            if(this.ownerUUID != null && this.level().getServer() != null) {
                Entity entity = this.level().getServer().getLevel(this.level().dimension()).getEntity(this.ownerUUID);
                if (entity instanceof LivingEntity livingEntity) {
                    this.owner = livingEntity;
                }
            }
            if(this.owner == null) {
                this.discard(); 
                return;
            }
        }        if (this.tickCount > this.lifeTime) {
            this.discard();
            return;
        }        if (this.level().isClientSide) {            for (int i = 0; i < 5; i++) {
                double angle = this.random.nextDouble() * 2.0 * Math.PI;
                double radiusOffset = this.radius * this.random.nextDouble();
                double x = this.getX() + radiusOffset * Math.cos(angle);
                double z = this.getZ() + radiusOffset * Math.sin(angle);
                this.level().addParticle(ParticleTypes.SQUID_INK, x, this.getY() + 0.3, z, 0, 0.1, 0);
            }
            for (int i = 0; i < 2; i++) {
                double angle = this.random.nextDouble() * 2.0 * Math.PI;
                double x = this.getX() + this.radius * Math.cos(angle);
                double z = this.getZ() + this.radius * Math.sin(angle);
                this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, x, this.getY() + 0.4, z, 0, 0.05, 0);
            }
        }      else {            if(this.tickCount % 5 == 0) {
                LivingEntity currentOwner = this.owner;
                if (currentOwner == null) return;                AABB damageArea = this.getBoundingBox().inflate(this.radius, 1.0, this.radius);
                List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, damageArea);                for(LivingEntity entity : entities) {
                    if (entity != currentOwner && !entity.isAlliedTo(currentOwner)) {                        float damage = (float) currentOwner.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.15f;                        DamageSource damageSource = this.damageSources().indirectMagic(this, currentOwner);
                        entity.hurt(damageSource, damage);                        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 3, true, false));                         ((ServerLevel)this.level()).sendParticles(ParticleTypes.DAMAGE_INDICATOR,
                                entity.getX(),
                                entity.getY(0.5),
                                entity.getZ(),
                                5, 
                                0.2, 0.2, 0.2, 0.0);                        this.level().playSound(null, entity.blockPosition(), SoundEvents.FIRE_EXTINGUISH, entity.getSoundSource(), 0.5F, 1.5F);
                    }
                }
            }
        }
    }
    @Override
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