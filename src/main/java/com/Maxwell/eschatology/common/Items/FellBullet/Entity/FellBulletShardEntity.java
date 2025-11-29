package com.Maxwell.eschatology.common.Items.FellBullet.Entity;import com.Maxwell.eschatology.Balance.ModConstants;
import com.Maxwell.eschatology.register.ModEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;public class FellBulletShardEntity extends Projectile {
    public float BASE_DAMAGE = ModConstants.FellBullet.SHARD_DAMAGE;
    public FellBulletShardEntity(EntityType<? extends Projectile> type, Level level) { super(type, level); }    public FellBulletShardEntity(EntityType<? extends Projectile> type, Level level, LivingEntity owner) {
        super(type, level);
        setOwner(owner);
    }    @Override
    public void tick() {
        super.tick();
        if (this.tickCount > ModConstants.FellBullet.SHARD_LIFE_TICKS) {
            this.discard();
            return;
        }
        Vec3 currentPos = this.position();
        Vec3 nextPos = currentPos.add(this.getDeltaMovement());
        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
                this.level(),
                this,
                currentPos,
                nextPos,
                this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D),
                this::canHitEntity
        );
        if (entityHitResult != null) {
            this.onHit(entityHitResult);
        }
        this.setPos(nextPos);
        if (this.level().isClientSide) {
            for (int i = 0; i < 3; ++i) {
                double randomX = (this.level().random.nextDouble() - 0.5) * 0.2;
                double randomY = (this.level().random.nextDouble() - 0.5) * 0.2;
                double randomZ = (this.level().random.nextDouble() - 0.5) * 0.2;
                this.level().addParticle(ParticleTypes.SOUL, this.getX(), this.getY(), this.getZ(), randomX, randomY, randomZ);
            }
        }
    }
    @Override
    protected boolean canHitEntity(Entity entity) {
        return !entity.isSpectator() && entity.isAlive() && entity.isPickable();
    }
    @Override
    protected void onHit(HitResult hitResult) {
        if (!this.level().isClientSide && hitResult.getType() == HitResult.Type.ENTITY) {
            if (((EntityHitResult)hitResult).getEntity() instanceof LivingEntity livingTarget && !livingTarget.hasEffect(ModEffects.COUNTER_STANCE.get())) {
                DamageSource source = this.level().damageSources().mobProjectile(this, (LivingEntity)this.getOwner());
                livingTarget.hurt(source, BASE_DAMAGE);
                livingTarget.invulnerableTime = 0;
            }
        }
    }@Override
    protected void defineSynchedData() {}
    @Override
    public boolean isNoGravity() { return true; }
}