package com.Maxwell.eschatology.Boss.XF07_Revanant.Entities.ExoSkull;import com.Maxwell.eschatology.Boss.XF07_Revanant.Entities.FrostStrikeField.FrostFieldEntity;
import com.Maxwell.eschatology.Balance.ExoWitherBalance;
import com.Maxwell.eschatology.register.ModEffects;
import com.Maxwell.eschatology.register.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;import javax.annotation.Nullable;
import java.util.List;public class ExoSkull extends Projectile {
    private float directHitDamage = ExoWitherBalance.EXO_SKULL_DIRECT_DAMAGE;
    private float explosionDamage = ExoWitherBalance.EXO_SKULL_EXPLOSION_DAMAGE;
    private int life = 0;
    private static final int MAX_LIFE = ExoWitherBalance.EXO_SKULL_MAX_LIFE;
    private @Nullable LivingEntity target;    public ExoSkull(EntityType<? extends ExoSkull> type, Level level) {
        super(type, level);
    }    public ExoSkull(Level level, LivingEntity owner) {
        this(ModEntities.EXO_SKULL.get(), level);
        this.setOwner(owner);
        this.setPos(owner.getX(), owner.getEyeY() - 0.1, owner.getZ());
    }    public void setDamage(float direct, float explosion) {
        this.directHitDamage = direct;
        this.explosionDamage = explosion;
    }    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) {
            double dx = random.nextGaussian() * 0.02;
            double dy = random.nextGaussian() * 0.02;
            double dz = random.nextGaussian() * 0.02;
            level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, getX(), getY(), getZ(), dx, dy, dz);
            level().addParticle(ParticleTypes.SNOWFLAKE, getX(), getY(), getZ(), -dx, dy * 0.5, -dz);
        }
        if (++life > MAX_LIFE) {
            explode(null);
            discard();
            return;
        }
        if (!level().isClientSide && target != null && target.isAlive()) {
            Vec3 toTarget = target.position().add(0, target.getBbHeight() * ExoWitherBalance.EXO_SKULL_TARGET_HEIGHT_OFFSET, 0).subtract(position());
            Vec3 newVel = getDeltaMovement().lerp(toTarget.normalize().scale(ExoWitherBalance.EXO_SKULL_SPEED_SCALE), ExoWitherBalance.EXO_SKULL_TURN_LERP_FACTOR);
            setDeltaMovement(newVel);
            setYRot((float) (Math.atan2(newVel.x, newVel.z) * (180F / Math.PI)));
            setXRot((float) (Math.atan2(newVel.y, Math.sqrt(newVel.x * newVel.x + newVel.z * newVel.z)) * (180F / Math.PI)));
        }
        HitResult hit = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hit.getType() != HitResult.Type.MISS) onHit(hit);
        setPos(position().add(getDeltaMovement()));
    }    @Override
    protected void onHit(HitResult result) {
        if (level().isClientSide || isRemoved()) return;
        if (result.getType() == HitResult.Type.ENTITY) {
            EntityHitResult hitResult = (EntityHitResult) result;
            Entity target = hitResult.getEntity();
            if (target == getOwner() || (getOwner() != null && getOwner().isAlliedTo(target))) return;
            if (target instanceof LivingEntity living) {
                living.hurt(damageSources().explosion(this, null), directHitDamage);
                living.addEffect(new MobEffectInstance(ModEffects.FREEZING_STRIKE.get(), ExoWitherBalance.EXO_SKULL_FREEZING_STRIKE_DURATION_DIRECT, 0, false, true));
            }
        }
        explode(result.getLocation());
        discard();
    }    private void explode(@Nullable Vec3 location) {
        if (level().isClientSide) return;
        ServerLevel server = (ServerLevel) level();
        Vec3 pos = (location != null ? location : position());
        server.sendParticles(ParticleTypes.SOUL, pos.x, pos.y, pos.z, 60, 0.8, 0.8, 0.8, 0.05);
        server.sendParticles(ParticleTypes.SNOWFLAKE, pos.x, pos.y, pos.z, 40, 0.8, 0.8, 0.8, 0.04);
        server.playSound(null, pos.x, pos.y, pos.z, SoundEvents.GENERIC_EXPLODE, getSoundSource(), 1.2F, 0.6F + random.nextFloat() * 0.3F);
        float scale = 1.0f + (life / (float) MAX_LIFE) * ExoWitherBalance.EXO_SKULL_LIFESPAN_RADIUS_SCALE;
        float radius = ExoWitherBalance.EXO_SKULL_BASE_EXPLOSION_RADIUS * scale;
        AABB area = new AABB(pos, pos).inflate(radius);
        List<LivingEntity> victims = server.getEntitiesOfClass(LivingEntity.class, area);
        for (LivingEntity e : victims) {
            if (e == getOwner() || (getOwner() != null && getOwner().isAlliedTo(e))) continue;
            float dist = (float) e.distanceToSqr(pos.x, pos.y, pos.z);
            float falloff = 1.0F - Mth.clamp((float) Math.sqrt(dist) / radius, 0.0F, 1.0F);
            float finalDmg = explosionDamage * scale * falloff;
            e.hurt(damageSources().outOfBorder(), finalDmg);
            e.addEffect(new MobEffectInstance(ModEffects.FREEZING_STRIKE.get(), (int) (ExoWitherBalance.EXO_SKULL_FREEZING_STRIKE_DURATION_EXPLOSION * scale), ExoWitherBalance.EXO_SKULL_FREEZING_STRIKE_AMPLIFIER_EXPLOSION, false, true));
        }
        FrostFieldEntity field = new FrostFieldEntity(server, pos.x, pos.y, pos.z);
        if (getOwner() != null) field.setOwnerUUID(getOwner().getUUID());
        server.addFreshEntity(field);
    }    @Override
    public boolean isPickable() {
        return false;
    }    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }    @Override
    protected void defineSynchedData() {
    }    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}