package com.Maxwell.eschatology.common.Items.Awe_Contempt;

import com.Maxwell.eschatology.common.Network.ModMessages;
import com.Maxwell.eschatology.common.Network.StartAnimationPacket;
import com.Maxwell.eschatology.register.ModEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class Awe_contempt extends SwordItem {
    public Awe_contempt(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        if (!attacker.level().isClientSide) {

            MobEffectInstance inst = target.getEffect(ModEffects.GAZE.get());
            int level = inst == null ? 0 : inst.getAmplifier() + 1;

            target.addEffect(new MobEffectInstance(ModEffects.GAZE.get(), 200, level));

            if (level >= 7) {
                target.removeEffect(ModEffects.GAZE.get());
                target.addEffect(new MobEffectInstance(ModEffects.CONTEMPT.get(), 200, 0));
            }
        }

        return super.hurtEnemy(stack, target, attacker);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            boolean foundContemptEnemy = false;
            List<Monster> enemies = level.getEntitiesOfClass(Monster.class,
                    player.getBoundingBox().inflate(8));
            for (LivingEntity enemy : enemies) {
                if (enemy.hasEffect(ModEffects.CONTEMPT.get())) {
                    foundContemptEnemy = true;
                    enemy.removeEffect(ModEffects.CONTEMPT.get());
                }
            }
            if (foundContemptEnemy) {
                player.addEffect(new MobEffectInstance(ModEffects.SELF_CONTEMPT.get(), 1000, 0));
                ModMessages.sendToClientsAround(
                        new StartAnimationPacket(player.getUUID(), "awe_contempt_s1", true, true),
                        (ServerPlayer) player
                );
            }

        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        return false;
    }
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.eschatology.awe_contempt.desc").withStyle(ChatFormatting.DARK_RED));
        pTooltipComponents.add(Component.translatable("item.eschatology.awe_contempt.desc2").withStyle(ChatFormatting.WHITE));
        pTooltipComponents.add(Component.translatable("item.eschatology.awe_contempt.desc3").withStyle(ChatFormatting.WHITE));
    }
}
