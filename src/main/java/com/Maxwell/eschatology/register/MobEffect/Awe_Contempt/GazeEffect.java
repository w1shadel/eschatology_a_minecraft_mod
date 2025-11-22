package com.Maxwell.eschatology.register.MobEffect.Awe_Contempt;

import com.Maxwell.eschatology.register.ModEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class GazeEffect extends MobEffect {
    public GazeEffect() {
        super(MobEffectCategory.HARMFUL, 0x8844FF); 
    }

    @Override
    public double getAttributeModifierValue(int amplifier, AttributeModifier modifier) {
        return super.getAttributeModifierValue(amplifier, modifier);
    }
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide && amplifier >= 7) {
            entity.removeEffect(this);
            entity.addEffect(new MobEffectInstance(ModEffects.CONTEMPT.get(), 200, 0));
        }
    }
}