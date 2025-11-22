package com.Maxwell.eschatology.Boss.BlackBool.AI;

import com.Maxwell.eschatology.Boss.BlackBool.BlackBool;
import com.Maxwell.eschatology.Boss.BlackBool.Entities.VoidWave.VoidWaveEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;

public class SingularityGoal extends Goal {
    private final BlackBool mob;
    private int ticks;
    private BlockPos targetPos;     private static final int DURATION_CHARGE_UP = 30; 
    private static final int DURATION_PULLING = 70;   
    private static final float RADIUS_PULL = 8.0f;    
    private static final float RADIUS_EXPLOSION = 5.0f;     private enum Phase { INACTIVE, CHARGING, PULLING, COLLAPSING }
    private Phase currentPhase = Phase.INACTIVE;    public SingularityGoal(BlackBool mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK)); 
    }    @Override
    public boolean canUse() {
        if (this.mob.canStartNewAttack()) {
            return false;
        }
        return !this.mob.isMeleeStance &&
                this.mob.singularityCooldown <= 0;
    }    @Override
    public boolean canContinueToUse() {        return this.currentPhase != Phase.INACTIVE && this.mob.getTarget() != null && this.mob.getTarget().isAlive();
    }    @Override
    public void start() {
        this.ticks = 0;
        this.currentPhase = Phase.CHARGING;
        this.mob.setCurrentAttackPhase(BlackBool.AttackPhase.SINGULARITY);
        this.mob.getNavigation().stop(); 
        this.mob.singularityCooldown = this.mob.getCooldownTicksByHealth(400);         this.targetPos = this.mob.getTarget().blockPosition();        this.mob.playSound(SoundEvents.ENDERMAN_STARE, 2.0F, 0.8F);
    }    @Override
    public void stop() {
        this.ticks = 0;
        this.currentPhase = Phase.INACTIVE;
        this.mob.setCurrentAttackPhase(BlackBool.AttackPhase.IDLE);
    }    @Override
    public void tick() {
        this.ticks++;        if (this.mob.getTarget() != null) {
            this.mob.getLookControl().setLookAt(this.mob.getTarget());
        }        switch (this.currentPhase) {
            case CHARGING:                if (this.mob.level() instanceof ServerLevel serverLevel) {                    double angle = (this.ticks * 15) * (Math.PI / 180D);
                    double radius = 2.0;
                    double px = this.targetPos.getX() + 0.5 + Math.cos(angle) * radius;
                    double pz = this.targetPos.getZ() + 0.5 + Math.sin(angle) * radius;
                    serverLevel.sendParticles(ParticleTypes.WITCH, px, this.targetPos.getY() + 0.2, pz, 1, 0, 0, 0, 0);
                }
                if (this.ticks >= DURATION_CHARGE_UP) {
                    this.ticks = 0;
                    this.currentPhase = Phase.PULLING;
                    this.mob.playSound(SoundEvents.PORTAL_TRIGGER, 1.5F, 1.0F);
                }
                break;            case PULLING:                if (this.mob.level() instanceof ServerLevel serverLevel) {
                    AABB pullBox = new AABB(this.targetPos).inflate(RADIUS_PULL);
                    List<Player> playersToPull = this.mob.level().getEntitiesOfClass(Player.class, pullBox);                    for (Player player : playersToPull) {
                        Vec3 centerPos = Vec3.atCenterOf(this.targetPos);
                        Vec3 pullDir = centerPos.subtract(player.position()).normalize();                        double pullStrength = 0.05 + (0.3 * ((double)this.ticks / DURATION_PULLING));
                        player.addDeltaMovement(pullDir.scale(pullStrength));
                        player.fallDistance = 0;
                    }
                    RandomSource random = RandomSource.create(this.mob.getId());                    if (this.ticks % 2 == 0) {
                        double px = this.targetPos.getX() + 0.5 + (random.nextDouble() - 0.5) * RADIUS_PULL * 2;
                        double py = this.targetPos.getY() + 0.5 + (random.nextDouble() - 0.5) * 4;
                        double pz = this.targetPos.getZ() + 0.5 + (random.nextDouble() - 0.5) * RADIUS_PULL * 2;
                        Vec3 toCenter = Vec3.atCenterOf(targetPos).subtract(px, py, pz).normalize().scale(0.5);
                        serverLevel.sendParticles(ParticleTypes.PORTAL, px, py, pz, 1, toCenter.x, toCenter.y, toCenter.z, 0.1);
                    }
                }
                if (this.ticks > 20 && this.ticks % 30 == 0) {
                    if (!this.mob.level().isClientSide) {
                        this.mob.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1.5F, 0.5F);                        final Vec3[] directions = {
                                new Vec3(1, 0, 0), 
                                new Vec3(-1, 0, 0),
                                new Vec3(0, 0, 1), 
                                new Vec3(0, 0, -1) 
                        };                        Vec3 centerPos = Vec3.atCenterOf(this.targetPos);                        for (Vec3 dir : directions) {                            Vec3 waveVelocity = dir.normalize().scale(0.7D);
                            VoidWaveEntity wave = new VoidWaveEntity(this.mob.level(), this.mob, waveVelocity);
                            wave.setPos(centerPos);                            this.mob.level().addFreshEntity(wave);
                        }
                    }
                }
                if (this.ticks >= DURATION_PULLING) {
                    this.ticks = 0;
                    this.currentPhase = Phase.COLLAPSING;
                }
                break;            case COLLAPSING:                if (this.mob.level() instanceof ServerLevel serverLevel) {
                    this.mob.playSound(SoundEvents.WITHER_DEATH, 2.0F, 1.2F);
                    serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, this.targetPos.getX() + 0.5, this.targetPos.getY() + 0.5, this.targetPos.getZ() + 0.5, 2, 0.0, 0.0, 0.0, 1.0);                    AABB explosionBox = new AABB(this.targetPos).inflate(RADIUS_EXPLOSION);
                    List<Player> playersToDamage = this.mob.level().getEntitiesOfClass(Player.class, explosionBox);                    float damage = (float) this.mob.getAttributeValue(Attributes.ATTACK_DAMAGE) * 1.5f;                     for (Player player : playersToDamage) {
                        player.hurt(this.mob.damageSources().magic(), damage);
                    }
                }                this.stop();
                break;
        }
    }
}