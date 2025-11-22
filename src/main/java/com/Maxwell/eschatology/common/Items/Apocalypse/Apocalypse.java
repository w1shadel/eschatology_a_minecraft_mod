package com.Maxwell.eschatology.common.Items.Apocalypse;

import com.Maxwell.eschatology.register.ModDamageTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class Apocalypse extends SwordItem {
    public Apocalypse(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }
    @Override
    public boolean isDamaged(ItemStack stack) {
        return false;
    }
    @Override
    public boolean isBarVisible(ItemStack stack) {
        return false;
    }
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.eschatology.apocalypse_desc").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(Component.translatable("item.eschatology.apocalypse_desc2").withStyle(ChatFormatting.GRAY));
    }
    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        super.hurtEnemy(pStack, pTarget, pAttacker);
        Level level = pTarget.level();
        double totalAttackDamage = pAttacker.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float extraDamageAmount = (float) totalAttackDamage * 1.5F;
        Registry<DamageType> damageTypeRegistry = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);        for (int i = 0; i < 4; i++) {
            pTarget.invulnerableTime = 0;
            DamageSource damageSource = switch (i) {
                case 0 ->
                        new DamageSource(damageTypeRegistry.getHolderOrThrow(ModDamageTypes.RED), pAttacker, pAttacker);
                case 1 ->
                        new DamageSource(damageTypeRegistry.getHolderOrThrow(ModDamageTypes.WHITE), pAttacker, pAttacker);
                case 2 ->
                        new DamageSource(damageTypeRegistry.getHolderOrThrow(ModDamageTypes.BLACK), pAttacker, pAttacker);
                default ->
                        new DamageSource(damageTypeRegistry.getHolderOrThrow(ModDamageTypes.PALE), pAttacker, pAttacker);
            };
            pTarget.hurt(damageSource, extraDamageAmount);
        }
        return true;
    }
}