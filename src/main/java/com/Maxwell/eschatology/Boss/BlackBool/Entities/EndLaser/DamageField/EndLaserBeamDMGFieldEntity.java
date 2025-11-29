package com.Maxwell.eschatology.Boss.BlackBool.Entities.EndLaser.DamageField;import com.Maxwell.eschatology.register.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;public class EndLaserBeamDMGFieldEntity extends Entity {
    private static final EntityDataAccessor<Integer> DATA_OWNER_ID =
            SynchedEntityData.defineId(EndLaserBeamDMGFieldEntity.class, EntityDataSerializers.INT);
    private LivingEntity owner;
    private UUID ownerUUID;
    private static final int DURATION = 80;
    private static final float RADIUS = 2.5f;
    private static final float HEIGHT = 5.0f;    public EndLaserBeamDMGFieldEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
    }    public EndLaserBeamDMGFieldEntity(Level pLevel, LivingEntity pOwner, Vec3 position) {
        this(ModEntities.END_LASER_BEAM_DAMAGE_FILED.get(), pLevel);
        this.setOwner(pOwner);
        this.setPos(position);
    }    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_OWNER_ID, 0);
    }    @Override
    public void tick() {
        super.tick();
        if (this.tickCount > DURATION) {
            this.discard();
            return;
        }
        if (this.level().isClientSide) {
            for (int i = 0; i < 5; ++i) {
                float angle = this.random.nextFloat() * (float) Math.PI * 2.0F;
                float radiusOffset = this.random.nextFloat() * RADIUS;
                double x = this.getX() + Math.cos(angle) * radiusOffset;
                double z = this.getZ() + Math.sin(angle) * radiusOffset;
                double y = this.getY() + this.random.nextFloat() * HEIGHT;
                this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 0.0D, 0.1D, 0.0D);
            }
        } else {
            if (this.tickCount == 1) {
                this.playSound(SoundEvents.WITHER_DEATH, 1.5F, 1.2F);
            }
            if (this.tickCount % 2 == 0) {
                LivingEntity currentOwner = getOwner();
                if (currentOwner == null) return;
                AABB damageArea = new AABB(
                        this.getX() - RADIUS, this.getY(), this.getZ() - RADIUS,
                        this.getX() + RADIUS, this.getY() + HEIGHT, this.getZ() + RADIUS
                );
                List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, damageArea);
                for (LivingEntity entity : entities) {
                    if (!entity.is(currentOwner) && !entity.isAlliedTo(currentOwner)) {
                        entity.invulnerableTime = 0;
                        entity.hurt(this.damageSources().outOfBorder(), 4.0f);
                    }
                }
            }
        }
    }    public void setOwner(@Nullable LivingEntity pOwner) {
        if (pOwner != null) {
            this.owner = pOwner;
            this.ownerUUID = pOwner.getUUID();
            this.entityData.set(DATA_OWNER_ID, pOwner.getId());
        }
    }    @Nullable
    public LivingEntity getOwner() {
        if (this.owner != null && this.owner.isAlive()) {
            return this.owner;
        }
        if (this.level().isClientSide) {
            Entity entity = this.level().getEntity(this.entityData.get(DATA_OWNER_ID));
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity) entity;
                return this.owner;
            }
        } else if (this.ownerUUID != null) {
            Entity entity = ((ServerLevel) this.level()).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity) entity;
                return this.owner;
            }
        }
        return null;
    }    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        if (this.ownerUUID != null) {
            pCompound.putUUID("Owner", this.ownerUUID);
        }
    }    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.hasUUID("Owner")) {
            this.ownerUUID = pCompound.getUUID("Owner");
        }
    }    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}