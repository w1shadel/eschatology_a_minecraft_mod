package com.Maxwell.eschatology.Boss.XF07_Revanant.Entities.ExoMisslie;import com.Maxwell.eschatology.Boss.XF07_Revanant.Entities.FrostStrikeField.FrostFieldEntity;
import com.Maxwell.eschatology.Boss.XF07_Revanant.ExoWither;
import com.Maxwell.eschatology.Balance.ExoWitherBalance;
import com.Maxwell.eschatology.register.ModEffects;
import com.Maxwell.eschatology.register.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.network.NetworkHooks;import java.util.List;public class ExoMissile extends Projectile {
    private LivingEntity shooter;
    private LivingEntity target;
    private boolean splitDone = false;
    private float damage = ExoWitherBalance.EXO_MISSILE_BASE_DAMAGE;
    private int life = 0;
    private static final int MAX_LIFE = ExoWitherBalance.EXO_MISSILE_MAX_LIFE;    public ExoMissile(EntityType<? extends ExoMissile> type, Level level) {
        super(type, level);
    }    public ExoMissile(Level level, ExoWither owner, LivingEntity target) {
        this(ModEntities.EXO_MISSILE.get(), level);
        this.shooter = owner;
        this.target = target;
        this.setOwner(owner);
        this.setPos(owner.getX(), owner.getEyeY() - 0.1, owner.getZ());
        this.setDeltaMovement(owner.getLookAngle().scale(ExoWitherBalance.EXO_MISSILE_INITIAL_SPEED_SCALE));
    }    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) {
            level().addParticle(ParticleTypes.SMOKE, getX(), getY(), getZ(), 0, 0, 0);
            return;
        }
        if (++life > MAX_LIFE) {
            discard();
            return;
        }
        if (target != null && target.isAlive()) {
            Vec3 toTarget = target.getEyePosition().subtract(position());
            double dist = toTarget.length();
            Vec3 desired = toTarget.normalize().scale(Math.min(ExoWitherBalance.EXO_MISSILE_MAX_SPEED, Math.max(ExoWitherBalance.EXO_MISSILE_MIN_SPEED, dist * ExoWitherBalance.EXO_MISSILE_ACCELERATION_FACTOR)));
            Vec3 newVel = getDeltaMovement().lerp(desired, ExoWitherBalance.EXO_MISSILE_TURN_LERP_FACTOR);
            setDeltaMovement(newVel);
            float yRot = (float) (Math.atan2(newVel.x, newVel.z) * (180F / Math.PI));
            float xRot = (float) (Math.atan2(newVel.y, Math.sqrt(newVel.x * newVel.x + newVel.z * newVel.z)) * (180F / Math.PI));
            setYRot(yRot);
            setXRot(xRot);
        }
        Vec3 nextPos = position().add(getDeltaMovement());
        HitResult blockHit = level().clip(new ClipContext(position(), nextPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (blockHit.getType() != HitResult.Type.MISS) {
            onHitBlock((BlockHitResult) blockHit);
            return;
        }
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(level(), this, position(), nextPos, getBoundingBox().expandTowards(getDeltaMovement()).inflate(0.3D), e -> e.isPickable() && e != this);
        if (entityHit != null) {
            onHitEntity(entityHit);
            return;
        }
        setPos(nextPos);
    }    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (level().isClientSide) return;
        Entity hit = result.getEntity();
        if (hit instanceof LivingEntity living && shooter != null) {
            living.hurt(damageSources().mobAttack(shooter), damage);
            living.addEffect(new MobEffectInstance(ModEffects.FREEZING_STRIKE.get(), ExoWitherBalance.EXO_MISSILE_FREEZING_STRIKE_DURATION, 0));
        }
        explodeAndSpawn(result.getLocation());
    }    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (level().isClientSide) return;
        explodeAndSpawn(result.getLocation());
    }    private void explodeAndSpawn(Vec3 pos) {
        if (level().isClientSide) return;
        ServerLevel server = (ServerLevel) level();
        server.sendParticles(ParticleTypes.EXPLOSION_EMITTER, pos.x, pos.y, pos.z, 1, 0.5, 0.5, 0.5, 0);
        server.sendParticles(ParticleTypes.SNOWFLAKE, pos.x, pos.y + 0.5, pos.z, 20, 0.6, 0.6, 0.6, 0.02);
        server.playSound(null, pos.x, pos.y, pos.z, SoundEvents.GENERIC_EXPLODE, getSoundSource(), 1.2F, 0.8F + random.nextFloat() * 0.4F);
        float blastDamage = damage * ExoWitherBalance.EXO_MISSILE_EXPLOSION_DAMAGE_MULTIPLIER;
        AABB box = new AABB(
                pos.x - ExoWitherBalance.EXO_MISSILE_EXPLOSION_RADIUS_XZ,
                pos.y - ExoWitherBalance.EXO_MISSILE_EXPLOSION_RADIUS_Y_BELOW,
                pos.z - ExoWitherBalance.EXO_MISSILE_EXPLOSION_RADIUS_XZ,
                pos.x + ExoWitherBalance.EXO_MISSILE_EXPLOSION_RADIUS_XZ,
                pos.y + ExoWitherBalance.EXO_MISSILE_EXPLOSION_RADIUS_Y_ABOVE,
                pos.z + ExoWitherBalance.EXO_MISSILE_EXPLOSION_RADIUS_XZ
        );
        List<LivingEntity> entities = server.getEntitiesOfClass(LivingEntity.class, box);
        for (LivingEntity e : entities) {
            if (e != shooter) {
                e.hurt(damageSources().outOfBorder(), blastDamage);
            }
        }
        FrostFieldEntity field = new FrostFieldEntity(server, pos.x, pos.y, pos.z);
        if (shooter != null) field.setOwnerUUID(shooter.getUUID());
        server.addFreshEntity(field);
        if (!splitDone) {
            splitDone = true;
            spawnSplitMissiles(pos);
        }
        discard();
    }    private void spawnSplitMissiles(Vec3 origin) {
        if (level().isClientSide) return;
        ServerLevel server = (ServerLevel) level();
        for (int i = 0; i < ExoWitherBalance.EXO_MISSILE_SPLIT_COUNT; i++) {
            double yawOffset = (i % 2 == 0) ? -ExoWitherBalance.EXO_MISSILE_SPLIT_YAW_OFFSET : ExoWitherBalance.EXO_MISSILE_SPLIT_YAW_OFFSET;
            Vec3 baseVel = getDeltaMovement().normalize();
            Vec3 spread = baseVel.yRot((float) yawOffset).add(0, ExoWitherBalance.EXO_MISSILE_SPLIT_VERTICAL_OFFSET, 0).normalize().scale(ExoWitherBalance.EXO_MISSILE_SPLIT_SPEED_SCALE);
            ExoMissile child = new ExoMissile(ModEntities.EXO_MISSILE.get(), level());
            child.shooter = this.shooter;
            child.target = this.target;
            child.splitDone = true;
            child.damage = Math.max(ExoWitherBalance.EXO_MISSILE_SPLIT_MIN_DAMAGE, this.damage * ExoWitherBalance.EXO_MISSILE_SPLIT_DAMAGE_RATIO);
            child.setPos(origin.x, origin.y + 0.2, origin.z);
            child.setDeltaMovement(spread);
            server.addFreshEntity(child);
        }
        server.playSound(null, origin.x, origin.y, origin.z, SoundEvents.GENERIC_EXPLODE, getSoundSource(), 0.9F, 1.2F);
    }    @Override
    protected void defineSynchedData() {
    }    @Override
    protected void readAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {
    }    @Override
    protected void addAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {
    }    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }    @Override
    public boolean isPickable() {
        return true;
    }    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!level().isClientSide) explodeAndSpawn(position());
        return super.hurt(source, amount);
    }
}