package com.Maxwell.eschatology.register.MobEffect;import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;public class InfernalFlamesEffect extends MobEffect {
    public InfernalFlamesEffect() {        super(MobEffectCategory.HARMFUL, 0xFF4500);
    }    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity.level().getGameTime() % 10 == 0) {
            DamageSource infernalDamage = pLivingEntity.damageSources().magic();
            float damage = 1.0F + (2.0F * pAmplifier);
pLivingEntity.hurtTime = 0;
pLivingEntity.invulnerableTime = 0;
            pLivingEntity.hurt(infernalDamage, damage);
        }
        pLivingEntity.setSecondsOnFire(1);
    }    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
