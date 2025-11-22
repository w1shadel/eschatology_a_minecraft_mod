package com.Maxwell.eschatology.Boss.XF07_Revanant.Entities.FrostStrikeField;

import com.Maxwell.eschatology.Boss.XF07_Revanant.ExoWitherBalance;
import com.Maxwell.eschatology.register.ModEffects;
import com.Maxwell.eschatology.register.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

public class FrostFieldEntity extends Entity {
    private int duration = ExoWitherBalance.FROST_FIELD_DURATION;
    private float radius = ExoWitherBalance.FROST_FIELD_RADIUS;    public FrostFieldEntity(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }    public FrostFieldEntity(Level level, double x, double y, double z) {
        this(ModEntities.FROST_FIELD.get(), level);
        this.setPos(x, y, z);
    }    @Override
    public void tick() {
        super.tick();        if (this.tickCount >= this.duration) {
            this.discard();
            return;
        }        if (this.level().isClientSide) {
            for (int i = 0; i < 2; ++i) {                float angle = this.random.nextFloat() * 2.0F * (float)Math.PI;
                float distance = this.random.nextFloat() * this.radius;
                double px = this.getX() + Math.cos(angle) * distance;
                double pz = this.getZ() + Math.sin(angle) * distance;
                this.level().addParticle(ParticleTypes.SNOWFLAKE, px, this.getY(), pz, 0, 0.1D, 0);
            }
        }        else {            if (this.tickCount % ExoWitherBalance.FROST_FIELD_EFFECT_INTERVAL == 0) {
                List<LivingEntity> nearbyEntities = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(this.radius, 0.5, this.radius));
                for (LivingEntity entity : nearbyEntities) {
                    if (entity.getUUID().equals(this.getOwnerUUID())) continue;                    var existing = entity.getEffect(ModEffects.FREEZING_STRIKE.get());                    int newLevel = 0;
                    int newDuration = ExoWitherBalance.FROST_FIELD_EFFECT_BASE_DURATION;                    if (existing != null) {
                        newLevel = Math.min(existing.getAmplifier() + 1, ExoWitherBalance.FROST_FIELD_EFFECT_MAX_AMPLIFIER);
                        newDuration = Math.min(existing.getDuration() + ExoWitherBalance.FROST_FIELD_EFFECT_DURATION_INCREASE, ExoWitherBalance.FROST_FIELD_EFFECT_MAX_DURATION);
                    }                    entity.addEffect(new MobEffectInstance(ModEffects.FREEZING_STRIKE.get(), newDuration, newLevel, false, true, true));
                }
            }
        }
    }    private java.util.UUID ownerUUID;
    public void setOwnerUUID(java.util.UUID uuid) { this.ownerUUID = uuid; }
    public java.util.UUID getOwnerUUID() { return this.ownerUUID; }    @Override
    protected void defineSynchedData() { }    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.hasUUID("Owner")) {
            this.ownerUUID = compound.getUUID("Owner");
        }
    }    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        if (this.ownerUUID != null) {
            compound.putUUID("Owner", this.ownerUUID);
        }
    }    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}