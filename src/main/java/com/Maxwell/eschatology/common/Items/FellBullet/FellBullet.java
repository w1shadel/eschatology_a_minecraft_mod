package com.Maxwell.eschatology.common.Items.FellBullet;

import com.Maxwell.eschatology.common.Items.FellBullet.Entity.FellBulletEntity;
import com.Maxwell.eschatology.register.ModEntities;
import com.Maxwell.eschatology.register.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
public class FellBullet extends SwordItem {
    private static final int MIN_CHARGE_TIME = 15;

    public FellBullet(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.MAGIC_BULLET_CREATE.get(), SoundSource.PLAYERS, 1F, 0.7F);
        return InteractionResultHolder.consume(itemstack);
    }    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeCharged) {
        if (!(entity instanceof Player player)) {
            return;
        }        int chargeDuration = this.getUseDuration(stack) - timeCharged;
        if (chargeDuration < MIN_CHARGE_TIME) {
            return;
        }        if (!level.isClientSide) {
            FellBulletEntity bullet = new FellBulletEntity(ModEntities.FELL_BULLET.get(), level, player);
            Vec3 spawnPos = player.getEyePosition().add(player.getLookAngle().scale(1.0));
            bullet.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
            float damage = 40.0F + Math.min(chargeDuration - MIN_CHARGE_TIME, 40) * 0.5F;
            level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.MAGIC_BULLET_SHOOT.get(), SoundSource.PLAYERS, 0.7F, 0.5F);
            bullet.setBaseDamage(damage);
            bullet.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 4.0F, 0.5F);
            level.addFreshEntity(bullet);       player.getCooldowns().addCooldown(this, 20);
        }
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        return false;
    }
    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int count) {
        if (!level.isClientSide || !(entity instanceof Player player)) {
            return;
        }        int chargeTime = getUseDuration(stack) - count;
        if (chargeTime > 0 && chargeTime % 10 == 0) {
            level.playSound(player, player.blockPosition(), SoundEvents.CONDUIT_AMBIENT, SoundSource.PLAYERS, 0.4f, 1.2f);
        }        if (chargeTime > 5) {
            Vec3 center = player.getEyePosition().add(player.getLookAngle().scale(2.5));
            Vec3 lookVec = player.getLookAngle();
            Vec3 upVec = player.getUpVector(1.0F);
            Vec3 rightVec = lookVec.cross(upVec).normalize();
            Vec3 circleUpVec = rightVec.cross(lookVec).normalize();            float progress = Math.min((chargeTime - 5) / 10f, 1.0f);            DustParticleOptions redDust = new DustParticleOptions(new Vector3f(1.0F, 0.0F, 0.0F), 1.0F);
            int circleParticles = 24;            float circleRadius = 1.7f;            for (int i = 0; i < circleParticles * progress; i++) {
                double angle = Math.toRadians(i * (360.0 / circleParticles));
                Vec3 point = center.add(rightVec.scale(Math.cos(angle) * circleRadius)).add(circleUpVec.scale(Math.sin(angle) * circleRadius));
                level.addParticle(redDust, point.x, point.y, point.z, 0, 0, 0);
            }            if (chargeTime > 10) {
                float starProgress = Math.min((chargeTime - 10) / 10f, 1.0f);
                float starRadius = 1.6f;                List<Vec3> starVertices = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    double angle = Math.toRadians(i * 72.0 - 90.0);
                    Vec3 vertex = center.add(rightVec.scale(Math.cos(angle) * starRadius)).add(circleUpVec.scale(Math.sin(angle) * starRadius));
                    starVertices.add(vertex);
                }                drawStarLine(level, starVertices.get(0), starVertices.get(2), redDust, starProgress);
                drawStarLine(level, starVertices.get(1), starVertices.get(3), redDust, starProgress);
                drawStarLine(level, starVertices.get(2), starVertices.get(4), redDust, starProgress);
                drawStarLine(level, starVertices.get(3), starVertices.get(0), redDust, starProgress);
                drawStarLine(level, starVertices.get(4), starVertices.get(1), redDust, starProgress);
            }
        }
    }    private void drawStarLine(Level level, Vec3 start, Vec3 end, DustParticleOptions particleOptions, float progress) {
        Vec3 difference = end.subtract(start);
        int lineParticles = 8;
        for (int i = 0; i < lineParticles * progress; i++) {
            float t = (float)i / (lineParticles - 1);
            Vec3 point = start.add(difference.scale(t));
            level.addParticle(particleOptions, point.x, point.y, point.z, 0, 0, 0);
        }
    }
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.eschatology.fell_bullet_weapon.desc").withStyle(ChatFormatting.DARK_RED));
        pTooltipComponents.add(Component.translatable("item.eschatology.fell_bullet_weapon.desc2").withStyle(ChatFormatting.WHITE));
    }
    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }
}