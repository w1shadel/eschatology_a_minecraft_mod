package com.Maxwell.eschatology.Boss.XF07_Revanant.Animation;import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
public class ExoWitherBodyRotationControl extends BodyRotationControl {
    private final Mob entity;
    public ExoWitherBodyRotationControl(Mob mob) {
        super(mob);
        this.entity = mob;
    }
    @Override
    public void clientTick() {
        final float MAX_ROTATE_SPEED = 10.0F;
        float targetYaw = this.entity.yHeadRot;
        float currentYaw = this.entity.yBodyRot;
        float delta = Mth.wrapDegrees(currentYaw - targetYaw);        if (delta < -MAX_ROTATE_SPEED) {
            delta = -MAX_ROTATE_SPEED;
        } else if (delta >= MAX_ROTATE_SPEED) {
            delta = MAX_ROTATE_SPEED;
        }
        this.entity.yBodyRot = targetYaw + delta * 0.75F;
    }
}