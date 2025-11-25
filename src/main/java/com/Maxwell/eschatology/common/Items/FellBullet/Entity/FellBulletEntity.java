package com.Maxwell.eschatology.common.Items.FellBullet.Entity;

import com.Maxwell.eschatology.common.Items.Awe_Contempt.Event.CounterStanceHandler;
import com.Maxwell.eschatology.Balance.ModConstants;
import com.Maxwell.eschatology.common.Network.ModMessages;
import com.Maxwell.eschatology.common.Network.StartAnimationPacket;
import com.Maxwell.eschatology.register.ModEntities;
import com.Maxwell.eschatology.register.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;


public class FellBulletEntity extends Projectile {

    private float baseDamage = ModConstants.FellBullet.BASE_DAMAGE;

    public FellBulletEntity(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }

    public FellBulletEntity(EntityType<? extends Projectile> type, Level level, LivingEntity owner) {
        super(type, level);
        setOwner(owner);
    }

    public void setBaseDamage(float damage) {
        this.baseDamage = damage;
    }
    @Override
    public void tick() {
        super.tick();

        Vec3 currentPos = this.position();
        Vec3 nextPos = currentPos.add(this.getDeltaMovement());


        BlockHitResult blockHitResult = this.level().clip(new ClipContext(currentPos, nextPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));

        if (blockHitResult.getType() != HitResult.Type.MISS) {
            nextPos = blockHitResult.getLocation();
        }


        AABB searchArea = this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D);
        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(this.level(), this, currentPos, nextPos, searchArea, this::canHitEntity);


        if (entityHitResult != null) {

            if (currentPos.distanceToSqr(entityHitResult.getLocation()) < currentPos.distanceToSqr(nextPos)) {

                blockHitResult = null; 
            }
        }

        HitResult finalHitResult = entityHitResult != null ? entityHitResult : blockHitResult;

        if (finalHitResult != null && finalHitResult.getType() != HitResult.Type.MISS) {

            if (finalHitResult.getType() == HitResult.Type.ENTITY) {
                if (isCounterSuccess(((EntityHitResult) finalHitResult).getEntity())) {
                    this.discard(); 
                    return;
                }
            }

            this.onHit(finalHitResult);
            return; 
        }

        this.setPos(nextPos); 

        if (this.level().isClientSide) {
            for (int i = 0; i < 3; ++i) {
                double randomX = (this.level().random.nextDouble() - 0.5) * 0.2;
                double randomY = (this.level().random.nextDouble() - 0.5) * 0.2;
                double randomZ = (this.level().random.nextDouble() - 0.5) * 0.2;
                this.level().addParticle(ParticleTypes.FIREWORK, this.getX(), this.getY(), this.getZ(), randomX, randomY, randomZ);
            }
        }

        if (this.tickCount > ModConstants.FellBullet.MAX_LIFE_TICKS) {
            this.discard();
        }

    }

    private boolean isCounterSuccess(Entity target) {
        if (target instanceof Player hitPlayer && (hitPlayer.getMainHandItem().is(ModItems.AWE_CONTEMPT.get()) || hitPlayer.getOffhandItem().is(ModItems.AWE_CONTEMPT.get()))) {
            if (!this.level().isClientSide && hitPlayer instanceof ServerPlayer serverPlayer) {
                ModMessages.sendToClientsAround(
                        new StartAnimationPacket(serverPlayer.getUUID(), "awe_contempt_s1", false, true),
                        serverPlayer
                );
                CounterStanceHandler.initiateCounterSequence(serverPlayer);
            }
            return true;
        }
        return false;
    }

    
    @Override
    protected boolean canHitEntity(Entity entity) {

        if (entity.is(this) || entity.is(this.getOwner())) {
            return false;
        }

        if (entity.isSpectator() || !entity.isAlive() || !entity.isPickable()) {
            return false;
        }
        return super.canHitEntity(entity);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        if (this.level().isClientSide) {
            return; 
        }

        if (hitResult.getType() == HitResult.Type.ENTITY) {
            Entity target = ((EntityHitResult) hitResult).getEntity();
            if (target instanceof LivingEntity livingTarget) {
                Entity owner = this.getOwner();
                DamageSource source = this.level().damageSources().mobProjectile(this, owner instanceof LivingEntity ? (LivingEntity) owner : null);
                livingTarget.hurt(source, baseDamage);
                livingTarget.invulnerableTime = 0;
                this.spawnShards(livingTarget);
            }
        }

        this.discard();
    }

    private void spawnShards(LivingEntity deadEntity) {
        Level world = this.level();
        if (world.isClientSide) return; 

        Vec3 centerTarget = deadEntity.position().add(0, deadEntity.getEyeHeight() / 2.0, 0);
        world.playSound(null, centerTarget.x, centerTarget.y, centerTarget.z, SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 1.0f, 0.5f);
        for (int i = 0; i < ModConstants.Counter.SHARD_COUNT; i++) {
            Vec3 randomDirection = new Vec3(world.getRandom().nextDouble() - 0.5, world.getRandom().nextDouble() - 0.5, world.getRandom().nextDouble() - 0.5).normalize();
            Vec3 spawnPos = centerTarget.add(randomDirection.scale(ModConstants.Counter.SHARD_SPREAD_DISTANCE));
            FellBulletShardEntity shard = new FellBulletShardEntity(ModEntities.FELL_SHARD.get(), world, this.getOwner() instanceof LivingEntity ? (LivingEntity)this.getOwner() : null);
            shard.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
            Vec3 velocity = centerTarget.subtract(spawnPos).normalize().scale(1.2);
            shard.setDeltaMovement(velocity);
            world.addFreshEntity(shard);
        }
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    public boolean isNoGravity() {
        return true;
    }
}