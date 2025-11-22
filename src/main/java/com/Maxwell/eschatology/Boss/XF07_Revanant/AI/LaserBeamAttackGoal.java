package com.Maxwell.eschatology.Boss.XF07_Revanant.AI;

import com.Maxwell.eschatology.Boss.BlackBool.Entities.EndLaser.EndLaserBeamEntity;
import com.Maxwell.eschatology.Boss.XF07_Revanant.ExoWither;
import com.Maxwell.eschatology.Boss.XF07_Revanant.ExoWitherBalance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class LaserBeamAttackGoal extends Goal {
    private final ExoWither owner;
    private int attackTime;
    private final int cooldown;
    private EndLaserBeamEntity activeLaser;    public LaserBeamAttackGoal(ExoWither owner, int cooldown) {
        this.owner = owner;
        this.cooldown = cooldown;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }    @Override
    public boolean canUse() {
        LivingEntity target = owner.getTarget();
        if (owner.isSpawning() || target == null || !target.isAlive()) {
            return false;
        }        return owner.getAttackPhase() == ExoWither.AttackPhase.NONE
                && owner.laserBeamCooldown <= 0
                && owner.getHealth() / owner.getMaxHealth() <= ExoWitherBalance.ENRAGE_HP_THRESHOLD;
    }    @Override
    public boolean canContinueToUse() {
        return attackTime < ExoWitherBalance.LASER_BEAM_TOTAL_DURATION_TICKS;
    }    @Override
    public void start() {
        attackTime = 0;
        owner.setAttackPhase(ExoWither.AttackPhase.LASER_BEAM);
        owner.setDeltaMovement(Vec3.ZERO);        owner.stopAllAnimations();
        owner.laserbeamattackAnimationState.startIfStopped(owner.tickCount);        owner.playSound(SoundEvents.BEACON_AMBIENT, 1.3F, 0.6F);
    }    @Override
    public void stop() {
        owner.laserBeamCooldown = cooldown;
        owner.setAttackPhase(ExoWither.AttackPhase.NONE);        owner.laserbeamattackAnimationState.stop();        if (activeLaser != null && activeLaser.isAlive()) {
            activeLaser.discard();
            activeLaser = null;
        }        owner.playSound(SoundEvents.BEACON_DEACTIVATE, 1.2F, 0.8F);
    }    @Override
    public void tick() {
        attackTime++;        LivingEntity target = owner.getTarget();
        if (target != null) {
            owner.getLookControl().setLookAt(target);
        }        if (attackTime == ExoWitherBalance.LASER_BEAM_CHARGE_UP_TICKS / 2) {
            owner.playSound(SoundEvents.AMETHYST_BLOCK_RESONATE, 1.4F, 0.8F);
        }        if (attackTime == ExoWitherBalance.LASER_BEAM_CHARGE_UP_TICKS && target != null && owner.level() instanceof ServerLevel serverLevel) {
            EndLaserBeamEntity beam = new EndLaserBeamEntity(
                    serverLevel,
                    owner,
                    target,
                    ExoWitherBalance.LASER_BEAM_FIRE_DURATION_TICKS,
                    ExoWitherBalance.LASER_BEAM_DAMAGE,
                    false
            );            serverLevel.addFreshEntity(beam);
            activeLaser = beam;            owner.playSound(SoundEvents.GHAST_SHOOT, 2.0F, 0.5F);
        }        if (activeLaser != null && activeLaser.isAlive() && target != null) {
            activeLaser.setPos(owner.getEyePosition());
            activeLaser.setEndPos(target.getEyePosition());
        }        if (attackTime == ExoWitherBalance.LASER_BEAM_CHARGE_UP_TICKS + ExoWitherBalance.LASER_BEAM_FIRE_DURATION_TICKS && activeLaser != null) {
            activeLaser.discard();
            activeLaser = null;
            owner.playSound(SoundEvents.BLAZE_SHOOT, 1.5F, 0.7F);
        }        if (attackTime >= ExoWitherBalance.LASER_BEAM_TOTAL_DURATION_TICKS - 10 && attackTime < ExoWitherBalance.LASER_BEAM_TOTAL_DURATION_TICKS) {
            owner.playSound(SoundEvents.BEACON_POWER_SELECT, 1.0F, 1.3F);
        }
    }
}