package com.Maxwell.eschatology.Boss.BlackBool.Entities.LightOrb;import com.Maxwell.eschatology.register.ModEntities;
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
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;import javax.annotation.Nullable;
import java.util.UUID;public class LightOrbEntity extends Entity {
    private static final EntityDataAccessor<Integer> DATA_OWNER_ID = SynchedEntityData.defineId(LightOrbEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_TARGET_ID = SynchedEntityData.defineId(LightOrbEntity.class, EntityDataSerializers.INT);
    private LivingEntity owner;
    private UUID ownerUUID;
    private LivingEntity target;
    private UUID targetUUID;
    private int ignoreOwnerTime = 5;    public LightOrbEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
    }    public LightOrbEntity(Level pLevel, LivingEntity pOwner, @Nullable LivingEntity pTarget) {
        this(ModEntities.LIGHT_ORB.get(), pLevel);
        this.setOwner(pOwner);
        if (pTarget != null) {
            this.setTarget(pTarget);
        }
        this.setPos(pOwner.getX(), pOwner.getEyeY() - 0.2, pOwner.getZ());
    }    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_OWNER_ID, 0);
        this.entityData.define(DATA_TARGET_ID, 0);
    }    public void shoot(Vec3 direction) {
        double initialSpeed = 0.8D;
        this.setDeltaMovement(direction.normalize().scale(initialSpeed));
    }    @Override
    public void tick() {
        super.tick();
        if (this.tickCount > 120) {
            if (!this.level().isClientSide) {
                this.explode();
            }
            this.discard();
            return;
        }
        if (this.ignoreOwnerTime > 0) {
            this.ignoreOwnerTime--;
        }
        if (!this.level().isClientSide) {
            if (this.getOwner() == null || !this.getOwner().isAlive()) {
                this.discard();
                return;
            }
            Vec3 currentPos = this.position();
            Vec3 nextPos = currentPos.add(this.getDeltaMovement());
            EntityHitResult entityHitResult = this.findHitEntity(currentPos, nextPos);
            if (entityHitResult != null) {
                this.onEntityHit(entityHitResult);
                return;
            }
            LivingEntity currentTarget = this.getTarget();
            if (currentTarget != null && currentTarget.isAlive()) {
                Vec3 directionToTarget = currentTarget.getEyePosition().subtract(this.position()).normalize();
                Vec3 currentVelocity = this.getDeltaMovement();
                double homingStrength = 0.09D;
                Vec3 homingVector = directionToTarget.scale(homingStrength);
                double homingSpeed = 0.9D;
                Vec3 newVelocity = currentVelocity.add(homingVector).normalize().scale(homingSpeed);
                this.setDeltaMovement(newVelocity);
            }
        }
        this.setPos(this.getX() + this.getDeltaMovement().x, this.getY() + this.getDeltaMovement().y, this.getZ() + this.getDeltaMovement().z);
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.END_ROD, this.getRandomX(0.2D), this.getRandomY(), this.getRandomZ(0.2D), 0.0D, 0.0D, 0.0D);
        }
    }    protected void onEntityHit(EntityHitResult pResult) {
        if (!this.level().isClientSide) {
            Entity hitEntity = pResult.getEntity();
            LivingEntity attacker = this.getOwner();
            if (attacker != null && hitEntity instanceof LivingEntity livingHitEntity) {
                float orbDamage = (float) attacker.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.7F;
                hitEntity.hurt(this.damageSources().outOfBorder(), orbDamage);
                livingHitEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0));
            }
            this.explode();
        }
        this.discard();
    }    private void explode() {
        if (!this.level().isClientSide) {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 2.0F, Level.ExplosionInteraction.NONE);
            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 5, 0.5, 0.5, 0.5, 0.0);
            }
            this.playSound(SoundEvents.GENERIC_EXPLODE, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        }
    }    @Nullable
    protected EntityHitResult findHitEntity(Vec3 pStartVec, Vec3 pEndVec) {
        return ProjectileUtil.getEntityHitResult(this.level(), this, pStartVec, pEndVec, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), this::canHitEntity);
    }    protected boolean canHitEntity(Entity pTarget) {
        if (pTarget.isSpectator() || !pTarget.isAlive() || !pTarget.isPickable()) {
            return false;
        }
        LivingEntity currentOwner = this.getOwner();
        if (currentOwner == null) return false;
        if (pTarget.is(currentOwner) || currentOwner.isAlliedTo(pTarget)) {
            return false;
        }
        if (pTarget.is(currentOwner) && this.ignoreOwnerTime > 0) {
            return false;
        }
        return true;
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
        } else if (this.level() instanceof ServerLevel serverLevel && this.ownerUUID != null) {
            Entity entity = serverLevel.getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity) entity;
                return this.owner;
            }
        } else if (this.level().isClientSide) {
            Entity entity = this.level().getEntity(this.entityData.get(DATA_OWNER_ID));
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity) entity;
                return this.owner;
            }
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
        if (this.target != null && this.target.isAlive()) {
            return this.target;
        } else if (this.level() instanceof ServerLevel serverLevel && this.targetUUID != null) {
            Entity entity = serverLevel.getEntity(this.targetUUID);
            if (entity instanceof LivingEntity) {
                this.target = (LivingEntity) entity;
                return this.target;
            }
        } else if (this.level().isClientSide) {
            Entity entity = this.level().getEntity(this.entityData.get(DATA_TARGET_ID));
            if (entity instanceof LivingEntity) {
                this.target = (LivingEntity) entity;
                return this.target;
            }
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