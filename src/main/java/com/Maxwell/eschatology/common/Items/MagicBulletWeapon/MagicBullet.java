package com.Maxwell.eschatology.common.Items.MagicBulletWeapon;

import com.Maxwell.eschatology.Balance.ModConstants;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MagicBullet extends Projectile {
    private final Set<UUID> hitEntities = new HashSet<>();
    private int life = 0;
    private float damage = ModConstants.MagicBullet.BASE_DAMAGE;

    public MagicBullet(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.life++ > ModConstants.MagicBullet.PROJECTILE_LIFE) {
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
                (entity) -> this.canHitEntity(entity)
        );
        if (entityHitResult != null) {
            this.onHit(entityHitResult);
        }
        this.setPos(nextPos);
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        if (entity.isSpectator() || !entity.isAlive() || !entity.isPickable()) {
            return false;
        }
        return !this.hitEntities.contains(entity.getUUID());
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide && hitResult instanceof EntityHitResult entityHitResult) {
            Entity target = entityHitResult.getEntity();
            if (this.hitEntities.add(target.getUUID())) {
                target.hurt(this.level().damageSources().magic(), this.damage);
            }
        }
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }
}