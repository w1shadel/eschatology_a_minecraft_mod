package com.Maxwell.eschatology.Boss.BlackBool.Entities.EndLaser;

import com.Maxwell.eschatology.Balance.BlackBoolBalance;
import com.Maxwell.eschatology.common.Network.ModMessages;
import com.Maxwell.eschatology.common.Network.SyncLaserEffectsPacket;
import com.Maxwell.eschatology.register.ModDataSerializers;
import com.Maxwell.eschatology.register.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;public class EndLaserBeamEntity extends Entity {
    private static final EntityDataAccessor<Vec3> DATA_END_POS = SynchedEntityData.defineId(EndLaserBeamEntity.class, ModDataSerializers.VEC3.get());
    private static final EntityDataAccessor<Integer> DATA_CASTER_ID = SynchedEntityData.defineId(EndLaserBeamEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_DURATION = SynchedEntityData.defineId(EndLaserBeamEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_DAMAGE_PER_TICK = SynchedEntityData.defineId(EndLaserBeamEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> DATA_SHOULD_LEAVE_FIELD = SynchedEntityData.defineId(EndLaserBeamEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_TARGET_ID = SynchedEntityData.defineId(EndLaserBeamEntity.class, EntityDataSerializers.INT);
    private LivingEntity caster;
    private LivingEntity target;
    private final Set<Entity> damagedEntitiesThisTick = new HashSet<>();
    private Vec3 currentBeamDir = null;
    private static final double TRACKING_SPEED = BlackBoolBalance.LASER_TRACKING_SPPED;
    public EndLaserBeamEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noCulling = true;
    }

    public EndLaserBeamEntity(Level pLevel, LivingEntity pCaster, LivingEntity pTarget, int pDuration, float pDamagePerTick, boolean pShouldLeaveField) {
        this(ModEntities.END_LASER_BEAM.get(), pLevel);
        this.setOwner(pCaster);
        this.setTarget(pTarget);
        this.setDuration(pDuration);
        this.setDamagePerTick(pDamagePerTick);
        this.setShouldLeaveField(pShouldLeaveField);
        if (pCaster != null) {
            this.setPos(pCaster.getEyePosition());
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_END_POS, Vec3.ZERO);
        this.entityData.define(DATA_CASTER_ID, 0);
        this.entityData.define(DATA_TARGET_ID, 0);
        this.entityData.define(DATA_DURATION, 100);
        this.entityData.define(DATA_DAMAGE_PER_TICK, 10.0f);
        this.entityData.define(DATA_SHOULD_LEAVE_FIELD, false);
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity currentCaster = this.getCaster();
        LivingEntity currentTarget = this.getTarget();

        if (currentCaster == null || !currentCaster.isAlive() || currentTarget == null || !currentTarget.isAlive() || this.tickCount > this.getDuration()) {
            this.discard();
            return;
        }

        if (!this.level().isClientSide) {
            this.damagedEntitiesThisTick.clear();
            Vec3 startPos = currentCaster.getEyePosition();
            this.setPos(startPos);
            Vec3 targetVec = currentTarget.getEyePosition().subtract(startPos).normalize();
            if (this.currentBeamDir == null) {
                this.currentBeamDir = targetVec;
            }
            this.currentBeamDir = this.currentBeamDir.lerp(targetVec, TRACKING_SPEED).normalize();
            Vec3 endPos = startPos.add(this.currentBeamDir.scale(64.0D));

            BlockHitResult blockHit = this.level().clip(new ClipContext(startPos, endPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            Vec3 finalEndPos = blockHit.getLocation();

            this.setEndPos(finalEndPos);
            performRaycastDamage(startPos, finalEndPos);
            performTerrainDestruction(startPos, finalEndPos);
            spawnLaserParticles(startPos, finalEndPos);
        }
    }

    private void spawnLaserParticles(Vec3 start, Vec3 end) {
        if (!(this.level() instanceof ServerLevel serverLevel)) return;
        RandomSource random = this.level().getRandom();
        if (start.distanceToSqr(end) < 63 * 63) {
            for (int i = 0; i < 5; i++) {
                serverLevel.sendParticles(ParticleTypes.LAVA, end.x, end.y, end.z, 1, 0.1, 0.1, 0.1, 0.0);
                serverLevel.sendParticles(ParticleTypes.SMOKE, end.x, end.y, end.z, 1, 0.2, 0.2, 0.2, 0.0);
            }
        }
        Vec3 direction = end.subtract(start).normalize();
        double distance = start.distanceTo(end);
        for (double step = 0; step < distance; step += 2.0) {
            if (random.nextFloat() < 0.5f) continue;
            Vec3 pointOnBeam = start.add(direction.scale(step));
            double radius = 1.2;
            double angle = random.nextDouble() * 2.0 * Math.PI;
            double px = pointOnBeam.x + radius * Math.cos(angle);
            double py = pointOnBeam.y + random.nextGaussian() * 0.5;
            double pz = pointOnBeam.z + radius * Math.sin(angle);
            Vec3 velocity = pointOnBeam.subtract(px, py, pz).normalize().scale(0.1);
            serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, px, py, pz, 1, velocity.x, velocity.y, velocity.z, 0.0);
        }
    }

    private void performTerrainDestruction(Vec3 start, Vec3 end) {
        LivingEntity owner = getCaster();
        if (owner == null) return;
        if (owner.getHealth() / owner.getMaxHealth() > 0.5f) {
            return;
        }
        if (this.tickCount % 2 != 0) {
            return;
        }
        int destructionRadius = 1;
        Vec3 direction = end.subtract(start).normalize();
        double distance = start.distanceTo(end);
        for (double step = 0; step < distance; step += 0.5) {
            Vec3 currentPoint = start.add(direction.scale(step));
            BlockPos centerPos = BlockPos.containing(currentPoint);
            for (int x = -destructionRadius; x <= destructionRadius; x++) {
                for (int y = -destructionRadius; y <= destructionRadius; y++) {
                    for (int z = -destructionRadius; z <= destructionRadius; z++) {
                        BlockPos posToDestroy = centerPos.offset(x, y, z);
                        BlockState blockState = this.level().getBlockState(posToDestroy);
                        if (!blockState.isAir() && blockState.getDestroySpeed(this.level(), posToDestroy) >= 0) {
                            if (this.level().destroyBlock(posToDestroy, false, this)) {
                                this.level().addParticle(ParticleTypes.POOF, posToDestroy.getX() + 0.5, posToDestroy.getY() + 0.5, posToDestroy.getZ() + 0.5, 0, 0, 0);
                            }
                        }
                    }
                }
            }
        }
    }

    private void performRaycastDamage(Vec3 start, Vec3 end) {
        LivingEntity owner = getCaster();
        if (owner == null) return;
        AABB laserBoundingBox = new AABB(
                Math.min(start.x, end.x), Math.min(start.y, end.y), Math.min(start.z, end.z),
                Math.max(start.x, end.x), Math.max(start.y, end.y), Math.max(start.z, end.z)
        ).inflate(1.0D);
        List<LivingEntity> potentialTargets = this.level().getEntitiesOfClass(LivingEntity.class, laserBoundingBox,
                (entity) -> entity.isAlive() && !entity.is(owner) && !owner.isAlliedTo(entity) && entity.isPickable());
        DamageSource damageSource = this.damageSources().outOfBorder();
        float damage = getDamagePerTick();
        for (LivingEntity target : potentialTargets) {
            float collisionPadding = target.getPickRadius() + 0.3F;
            AABB targetAABB = target.getBoundingBox().inflate(collisionPadding);
            Optional<Vec3> hitPoint = targetAABB.clip(start, end);
            if (hitPoint.isPresent()) {
                if (damagedEntitiesThisTick.add(target)) {
                    if (target.hurt(damageSource, damage)) {
                        target.setRemainingFireTicks(40);
                    }
                }
            }
        }
    }

    @Override
    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        if (!this.level().isClientSide && this.getCaster() != null) {
            AABB trackingRange = this.getBoundingBox().inflate(64.0D);
            for (ServerPlayer player : this.level().getEntitiesOfClass(ServerPlayer.class, trackingRange)) {
                ModMessages.sendToPlayer(new SyncLaserEffectsPacket(SyncLaserEffectsPacket.EffectPhase.STOP_FIRING), player);
            }
        }
    }

    public Vec3 getEndPos() {
        return this.entityData.get(DATA_END_POS);
    }

    public void setEndPos(Vec3 pos) {
        this.entityData.set(DATA_END_POS, pos);
    }

    public LivingEntity getCaster() {
        if (this.caster == null || !this.caster.isAlive()) {
            Entity found = this.level().getEntity(this.entityData.get(DATA_CASTER_ID));
            if (found instanceof LivingEntity) this.caster = (LivingEntity) found;
        }
        return this.caster;
    }

    private void setOwner(LivingEntity owner) {
        this.caster = owner;
        if (owner != null) {
            this.entityData.set(DATA_CASTER_ID, owner.getId());
        }
    }

    public LivingEntity getTarget() {
        if (this.target == null || !this.target.isAlive()) {
            Entity found = this.level().getEntity(this.entityData.get(DATA_TARGET_ID));
            if (found instanceof LivingEntity) this.target = (LivingEntity) found;
        }
        return this.target;
    }

    private void setTarget(LivingEntity pTarget) {
        this.target = pTarget;
        if (pTarget != null) {
            this.entityData.set(DATA_TARGET_ID, pTarget.getId());
        }
    }

    public int getDuration() {
        return this.entityData.get(DATA_DURATION);
    }

    public void setDuration(int duration) {
        this.entityData.set(DATA_DURATION, duration);
    }

    public float getDamagePerTick() {
        return this.entityData.get(DATA_DAMAGE_PER_TICK);
    }

    public void setDamagePerTick(float damage) {
        this.entityData.set(DATA_DAMAGE_PER_TICK, damage);
    }

    public boolean shouldLeaveField() {
        return this.entityData.get(DATA_SHOULD_LEAVE_FIELD);
    }

    public void setShouldLeaveField(boolean shouldLeaveField) {
        this.entityData.set(DATA_SHOULD_LEAVE_FIELD, shouldLeaveField);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag c) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag c) {
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }
}