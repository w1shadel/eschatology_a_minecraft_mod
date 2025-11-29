package com.Maxwell.eschatology.Boss.BlackBool.Entities.VoidLance;import com.Maxwell.eschatology.Boss.BlackBool.Entities.VoidLance.VoidRift.VoidRiftEntity;
import com.Maxwell.eschatology.register.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;public class VoidLanceEntity extends Projectile {
    private static final EntityDataAccessor<BlockPos> DATA_TARGET_POS = SynchedEntityData.defineId(VoidLanceEntity.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Boolean> DATA_SHOULD_CREATE_RIFT = SynchedEntityData.defineId(VoidLanceEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_FALL_DELAY = SynchedEntityData.defineId(VoidLanceEntity.class, EntityDataSerializers.INT);
    private boolean hasSearchedTarget = false;    public VoidLanceEntity(EntityType<? extends VoidLanceEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
    }    public VoidLanceEntity(Level pLevel, LivingEntity pOwner) {
        this(ModEntities.VOID_LANCE.get(), pLevel);
        this.setOwner(pOwner);
    }    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_TARGET_POS, BlockPos.ZERO);
        this.entityData.define(DATA_SHOULD_CREATE_RIFT, false);
        this.entityData.define(DATA_FALL_DELAY, 0);
    }    public void setFallDelay(int ticks) {
        this.entityData.set(DATA_FALL_DELAY, ticks);
    }    public int getFallDelay() {
        return this.entityData.get(DATA_FALL_DELAY);
    }    public void setShouldCreateRift(boolean shouldCreateRift) {
        this.entityData.set(DATA_SHOULD_CREATE_RIFT, shouldCreateRift);
    }    public boolean shouldCreateRift() {
        return this.entityData.get(DATA_SHOULD_CREATE_RIFT);
    }    public void setTargetPosition(BlockPos pos) {
        this.entityData.set(DATA_TARGET_POS, pos);
    }    public BlockPos getTargetPosition() {
        return this.entityData.get(DATA_TARGET_POS);
    }    @Override
    public void tick() {
        super.tick();
        if (this.tickCount > 200) {
            this.discard();
            return;
        }
        int fallDelay = this.getFallDelay();
        if (fallDelay > 0) {
            this.setDeltaMovement(Vec3.ZERO);
            if (!this.level().isClientSide) {
                this.setFallDelay(fallDelay - 1);
            }
            if (this.level().isClientSide) {
                drawTargetCircle();
            }
            return;
        }
        if (!this.hasSearchedTarget && !this.level().isClientSide) {
            BlockPos groundPos = this.level().getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, this.blockPosition());
            this.setTargetPosition(groundPos);
            this.hasSearchedTarget = true;
        }
        BlockPos targetPos = getTargetPosition();
        if (this.level().isClientSide) {
            drawTargetCircle();
        } else {
            if (targetPos.equals(BlockPos.ZERO)) return;
            Vec3 currentPos = this.position();
            Vec3 targetVec = Vec3.atBottomCenterOf(targetPos);
            if (currentPos.distanceToSqr(targetVec) < 1.0 || currentPos.y < targetVec.y) {
                this.playSound(SoundEvents.GENERIC_EXPLODE, 1.0F, 1.2F);
                this.playSound(SoundEvents.WITHER_HURT, 1.0F, 1.0F);
                if (this.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.EXPLOSION, targetVec.x, targetVec.y, targetVec.z, 5, 0.5, 0.5, 0.5, 0);
                }
                if (this.shouldCreateRift() && this.getOwner() instanceof LivingEntity owner) {
                    VoidRiftEntity rift = new VoidRiftEntity(this.level(), owner);
                    rift.setPos(targetVec);
                    this.level().addFreshEntity(rift);
                }
                this.discard();
            } else {
                Vec3 moveDirection = targetVec.subtract(currentPos).normalize();
                this.setDeltaMovement(moveDirection.scale(1.5));
                this.move(MoverType.SELF, this.getDeltaMovement());
            }
        }
    }    private void drawTargetCircle() {
        BlockPos targetPos = getTargetPosition();
        if (!targetPos.equals(BlockPos.ZERO)) {
            double radius = 1.5;
            for (int i = 0; i < 360; i += 20) {
                double angle = Math.toRadians(i);
                double x = targetPos.getX() + 0.5 + radius * Math.cos(angle);
                double z = targetPos.getZ() + 0.5 + radius * Math.sin(angle);
                this.level().addParticle(ParticleTypes.WITCH, x, targetPos.getY() + 0.1, z, 0, 0, 0);
            }
        }
    }    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.contains("shouldCreateRift")) {
            this.setShouldCreateRift(pCompound.getBoolean("shouldCreateRift"));
        }
        if (pCompound.contains("fallDelay")) {
            this.setFallDelay(pCompound.getInt("fallDelay"));
        }
    }    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putBoolean("shouldCreateRift", this.shouldCreateRift());
        pCompound.putInt("fallDelay", this.getFallDelay());
    }
}