package com.Maxwell.eschatology.Boss.BlackBool.Entities.VoidWave;import com.Maxwell.eschatology.Boss.BlackBool.BlackBool;
import com.Maxwell.eschatology.register.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;public class VoidWaveEntity extends Entity {
    public static final EntityDataAccessor<Integer> DATA_OWNER_ID = SynchedEntityData.defineId(VoidWaveEntity.class, EntityDataSerializers.INT);
    private BlackBool owner;
    private UUID ownerUUID;
    public static final EntityDataAccessor<Float> DATA_SCALE = SynchedEntityData.defineId(VoidWaveEntity.class, EntityDataSerializers.FLOAT);    public VoidWaveEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
    }    public VoidWaveEntity(Level pLevel, BlackBool owner, Vec3 direction) {
        this(ModEntities.VOID_WAVE.get(), pLevel);
        this.setOwner(owner);
        this.setPos(owner.getX(), owner.getY() + 0.5, owner.getZ());
        this.noPhysics = true;
        this.setDeltaMovement(direction);
    }    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_SCALE, 0.1F);
        this.entityData.define(DATA_OWNER_ID, 0);
    }    @Override
    public void tick() {
        super.tick();
        if (this.tickCount > 60) {
            this.discard();
            return;
        }
        Vec3 delta = this.getDeltaMovement();
        if (delta.horizontalDistanceSqr() > 1.0E-7D) {
            float yaw = (float) (Mth.atan2(delta.x, delta.z) * (double) (180F / (float) Math.PI));
            this.setYRot(yaw);
        }
        this.setPos(this.position().add(delta));
        float newScale = this.getScale() + 0.15F;
        this.entityData.set(DATA_SCALE, Math.min(newScale, 4.0F));
        if (!this.level().isClientSide) {
            LivingEntity currentOwner = getOwner();
            if (currentOwner == null) {
                this.discard();
                return;
            }
            float scale = getScale();
            float width = 1.0F * scale;
            float height = 2.0F * scale;
            AABB damageArea = new AABB(
                    this.getX() - width, this.getY(), this.getZ() - width,
                    this.getX() + width, this.getY() + height, this.getZ() + width
            );
            List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, damageArea);
            for (LivingEntity entity : entities) {
                if (!entity.is(currentOwner) && !entity.isAlliedTo(currentOwner)) {
                    if (entity.getTags().stream().noneMatch(tag -> tag.equals("hit_by_wave_" + this.getId()))) {
                        float waveDamage = (float) currentOwner.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.6F;
                        entity.hurt(this.damageSources().outOfBorder(), waveDamage);
                        entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 1));
                        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 2));
                        entity.addTag("hit_by_wave_" + this.getId());
                    }
                }
            }
        } else {
            float scale = getScale();
            for (int i = 0; i < 4; i++) {
                float randomOffset = (this.random.nextFloat() - 0.5f) * scale * 2.0f;
                Vec3 sideVector = new Vec3(-delta.z, 0, delta.x).normalize().scale(randomOffset);
                double px = this.getX() + sideVector.x;
                double py = this.getY() + this.random.nextFloat() * scale * 1.5;
                double pz = this.getZ() + sideVector.z;
                this.level().addParticle(ParticleTypes.SOUL, px, py, pz, 0, 0, 0);
            }
        }
    }    public void setOwner(@Nullable BlackBool pOwner) {
        if (pOwner != null) {
            this.owner = pOwner;
            this.ownerUUID = pOwner.getUUID();
            this.entityData.set(DATA_OWNER_ID, pOwner.getId());
        }
    }    @Nullable
    public LivingEntity getOwner() {
        if (this.owner != null && this.owner.isAlive()) {
            return this.owner;
        } else if (this.level() instanceof ServerLevel serverLevel && this.ownerUUID != null) {
            Entity entity = serverLevel.getEntity(this.ownerUUID);
            if (entity instanceof BlackBool) {
                this.owner = (BlackBool) entity;
                return this.owner;
            }
        } else if (this.level().isClientSide) {
            Entity entity = this.level().getEntity(this.entityData.get(DATA_OWNER_ID));
            if (entity instanceof BlackBool) {
                this.owner = (BlackBool) entity;
                return this.owner;
            }
        }
        return null;
    }    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        if (this.ownerUUID != null) pCompound.putUUID("Owner", this.ownerUUID);
    }    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.hasUUID("Owner")) this.ownerUUID = pCompound.getUUID("Owner");
    }    public float getScale() {
        return this.entityData.get(DATA_SCALE);
    }    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}