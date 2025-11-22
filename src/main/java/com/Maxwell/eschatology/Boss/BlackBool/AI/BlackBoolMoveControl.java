package com.Maxwell.eschatology.Boss.BlackBool.AI;import com.Maxwell.eschatology.Boss.BlackBool.BlackBool;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.monster.Monster;public class BlackBoolMoveControl extends FlyingMoveControl {
    private final BlackBool boss;    public BlackBoolMoveControl(Monster pMob, int pMaxTurn, boolean pHoversInPlace) {
        super(pMob, pMaxTurn, pHoversInPlace);
        if (pMob instanceof BlackBool) {
            this.boss = (BlackBool) pMob;
        } else {
            throw new IllegalArgumentException("BlackBoolMoveControl can only be used by BlackBool entity!");
        }
    }    @Override
    public void tick() {
        if (this.boss.getCurrentAttackPhase() == BlackBool.AttackPhase.LASER) {
            return;
        }
        super.tick();
    }
}