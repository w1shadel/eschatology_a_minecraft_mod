package com.Maxwell.eschatology.register.MobEffect;import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;public class GravityBoundEffect extends MobEffect {
    public GravityBoundEffect(MobEffectCategory pCategory, int pColor) {
        super(MobEffectCategory.BENEFICIAL, pColor);
    }    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
    }    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return false;
    }
}