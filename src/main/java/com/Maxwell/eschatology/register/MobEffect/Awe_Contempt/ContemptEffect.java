package com.Maxwell.eschatology.register.MobEffect.Awe_Contempt;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ContemptEffect extends MobEffect {

    public ContemptEffect() {
        super(MobEffectCategory.HARMFUL, 0x333333);

        this.addAttributeModifier(
                Attributes.ATTACK_DAMAGE,
                "71ccdf0c-394c-4c60-8896-998728a21c31",
                -0.5, 
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );
    }
}