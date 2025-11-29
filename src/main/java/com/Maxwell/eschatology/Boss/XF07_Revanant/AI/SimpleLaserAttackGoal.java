package com.Maxwell.eschatology.Boss.XF07_Revanant.AI;import com.Maxwell.eschatology.Boss.XF07_Revanant.Animation.ExoWitherAnimationTicks;
import com.Maxwell.eschatology.Boss.XF07_Revanant.ExoWither;
import com.Maxwell.eschatology.Balance.ExoWitherBalance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;import java.util.EnumSet;public class SimpleLaserAttackGoal extends Goal {
    private final ExoWither owner;
    private boolean isEnraged = false;
    private int attackTime;
    private final int cooldown;
    private int startFireTick;
    private int endFireTick;
    private int fireInterval;    public SimpleLaserAttackGoal(ExoWither owner, int cooldown) {
        this.owner = owner;
        this.cooldown = cooldown;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }    public void setEnraged(boolean enraged) {
        this.isEnraged = enraged;
    }    @Override
    public boolean canUse() {
        if (owner.isSpawning()) return false;
        LivingEntity target = this.owner.getTarget();
        if (target == null || !target.isAlive()) {
            return false;
        }        return this.owner.getAttackPhase() == ExoWither.AttackPhase.NONE &&
                this.owner.simpleLaserCooldown <= 0;
    }    @Override
    public boolean canContinueToUse() {
        return this.attackTime < getAnimationDurationInTicks();
    }    @Override
    public void stop() {
        this.owner.simpleLaserCooldown = this.cooldown;
        this.owner.setAttackPhase(ExoWither.AttackPhase.NONE);
    }    @Override
    public void start() {
        this.attackTime = 0;
        this.owner.setAttackPhase(ExoWither.AttackPhase.SIMPLE_LASER);
        this.owner.setDeltaMovement(Vec3.ZERO);        if (this.isEnraged) {
            this.startFireTick = ExoWitherBalance.SIMPLE_LASER_START_TICK_ENRAGED;
            this.endFireTick = ExoWitherBalance.SIMPLE_LASER_END_TICK_ENRAGED;
            this.fireInterval = ExoWitherBalance.SIMPLE_LASER_FIRE_INTERVAL_ENRAGED;
        } else {
            this.startFireTick = ExoWitherBalance.SIMPLE_LASER_START_TICK_NORMAL;
            this.endFireTick = ExoWitherBalance.SIMPLE_LASER_END_TICK_NORMAL;
            this.fireInterval = ExoWitherBalance.SIMPLE_LASER_FIRE_INTERVAL_NORMAL;
        }
    }    @Override
    public void tick() {
        LivingEntity target = this.owner.getTarget();
        if (target == null) return;
        this.owner.getLookControl().setLookAt(target);
        this.attackTime++;        if (this.attackTime >= this.startFireTick && this.attackTime <= this.endFireTick) {
            if ((this.attackTime - this.startFireTick) % this.fireInterval == 0) {
                boolean isLeft = ((this.attackTime - this.startFireTick) / this.fireInterval) % 2 == 0;
                this.owner.fireSmallLaser(isLeft);
            }
        }
    }    private int getAnimationDurationInTicks() {
        return (int) (ExoWitherAnimationTicks.SIMPLE_LASER_ATTACK_LENGTH_IN_SECONDS * 20);
    }
}