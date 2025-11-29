package com.Maxwell.eschatology.Boss.XF07_Revanant.Entities.SmallBeam;import com.Maxwell.eschatology.Balance.ExoWitherBalance;
import com.Maxwell.eschatology.register.ModEffects;
import com.Maxwell.eschatology.register.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;public class SmallBeamEntity extends ThrowableProjectile {
    private float damage = ExoWitherBalance.SMALL_BEAM_BASE_DAMAGE;
    private boolean enraged = false;
    private int pierceCount = 0;    public SmallBeamEntity(EntityType<? extends ThrowableProjectile> type, Level level) {
        super(type, level);
    }    public SmallBeamEntity(Level level, LivingEntity owner) {
        super(ModEntities.SMALL_BEAM.get(), owner, level);
        this.setPos(owner.getX(), owner.getEyeY() - 0.1, owner.getZ());
        this.setNoGravity(true);
    }    public void setDamage(float damage) {
        this.damage = damage;
    }    @Override
    protected void defineSynchedData() {
    }    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            for (int i = 0; i < 3; i++) {
                double dx = (random.nextDouble() - 0.5) * 0.02;
                double dy = (random.nextDouble() - 0.5) * 0.02;
                double dz = (random.nextDouble() - 0.5) * 0.02;
                this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, getX(), getY(), getZ(), dx, dy, dz);
                this.level().addParticle(ParticleTypes.ELECTRIC_SPARK, getX(), getY(), getZ(), 0, 0, 0);
            }
        }
        if (this.tickCount > ExoWitherBalance.SMALL_BEAM_MAX_LIFE) {
            this.discard();
        }
    }    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (this.level().isClientSide) return;
        if (result.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHitResult = (EntityHitResult) result;
            if (entityHitResult.getEntity() instanceof LivingEntity target) {
                if (target == this.getOwner() || (this.getOwner() != null && this.getOwner().isAlliedTo(target))) {
                    return;
                }
                target.invulnerableTime = 0;
                target.hurt(this.damageSources().outOfBorder(), this.damage);
                int duration = (int) (ExoWitherBalance.SMALL_BEAM_EFFECT_BASE_DURATION + this.damage * ExoWitherBalance.SMALL_BEAM_EFFECT_DAMAGE_DURATION_MULTIPLIER);
                int amplifier = enraged ? ExoWitherBalance.SMALL_BEAM_EFFECT_AMPLIFIER_ENRAGED : ExoWitherBalance.SMALL_BEAM_EFFECT_AMPLIFIER_NORMAL;
                target.addEffect(new MobEffectInstance(ModEffects.FREEZING_STRIKE.get(), duration, amplifier));
                target.level().playSound(null, target.blockPosition(), SoundEvents.PLAYER_HURT_FREEZE, target.getSoundSource(), 0.7F, 1.2F);
                ((Level) target.level()).addParticle(ParticleTypes.SNOWFLAKE,
                        target.getX(), target.getEyeY(), target.getZ(),
                        0.0D, 0.1D, 0.0D);
                if (this.enraged && ++this.pierceCount < ExoWitherBalance.SMALL_BEAM_ENRAGED_PIERCE_COUNT) {
                    return;
                }
                this.discard();
            }
        } else if (result.getType() == HitResult.Type.BLOCK) {
            this.level().playSound(null, this.blockPosition(), SoundEvents.GLASS_BREAK, this.getSoundSource(), 0.6F, 1.8F);
            this.level().addParticle(ParticleTypes.SNOWFLAKE, getX(), getY(), getZ(), 0.0, 0.1, 0.0);
            this.discard();
        }
    }    @Override
    protected float getGravity() {
        return 0.0f;
    }
}