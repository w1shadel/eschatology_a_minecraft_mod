package com.Maxwell.eschatology.common.Items.MagicBulletWeapon;

import com.Maxwell.eschatology.common.ModConstants;
import com.Maxwell.eschatology.register.ModEntities;
import com.Maxwell.eschatology.register.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
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

public class MagicBulletShooterItem extends SwordItem {
    private static final String NBT_BULLETS = "MagicBullets";
    private static final int MAX_BULLETS = 7;
    private static final int MIN_CHARGE_TIME = 20;

    public MagicBulletShooterItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    public static int getBulletCount(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(NBT_BULLETS)) {
            return tag.getInt(NBT_BULLETS);
        }
        setBulletCount(stack, MAX_BULLETS);
        return MAX_BULLETS;
    }

    public static void setBulletCount(ItemStack stack, int count) {
        stack.getOrCreateTag().putInt(NBT_BULLETS, count);
    }

    public static void consumeBullet(ItemStack stack) {
        int current = getBulletCount(stack);
        if (current > 0) {
            setBulletCount(stack, current - 1);
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.level().isClientSide) {
            if (getBulletCount(stack) > 0) {
                consumeBullet(stack);
                attacker.level().explode(null, target.getX(), target.getY(), target.getZ(), 2.0F, false, Level.ExplosionInteraction.NONE);
                target.setSecondsOnFire(5);
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.MAGIC_BULLET_CREATE.get(), SoundSource.PLAYERS, 1F, 1.0F);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeCharged) {
        if (!(entity instanceof Player player)) {
            return;
        }
        int chargeDuration = this.getUseDuration(stack) - timeCharged;
        if (chargeDuration < ModConstants.MagicBullet.MIN_CHARGE_TIME) {
            return;
        }
        if (!level.isClientSide) {
            if (getBulletCount(stack) > 0) {
                consumeBullet(stack);
                MagicBullet bullet = new MagicBullet(ModEntities.PENETRATING_BULLET.get(), level);
                Vec3 spawnPos = player.getEyePosition().add(player.getLookAngle().scale(1.0));
                bullet.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
                float damage = ModConstants.MagicBullet.BASE_DAMAGE + Math.min(chargeDuration, ModConstants.MagicBullet.CHARGE_DAMAGE_CAP) * ModConstants.MagicBullet.CHARGE_DAMAGE_MULTIPLIER;
                bullet.setDamage(damage);    bullet.setDamage(damage);
                bullet.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 1.0F);
                level.addFreshEntity(bullet);

                level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.MAGIC_BULLET_SHOOT.get(), SoundSource.PLAYERS, 0.7F, 1.5F);
                player.getCooldowns().addCooldown(this, ModConstants.MagicBullet.COOLDOWN_NORMAL);
            } else {
                setBulletCount(stack, ModConstants.MagicBullet.MAX_BULLETS);
                MagicBullet bullet = new MagicBullet(ModEntities.PENETRATING_BULLET.get(), level);
                Vec3 spawnPos = player.getEyePosition().add(player.getLookAngle().scale(-2.0));
                bullet.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
                bullet.setDamage(ModConstants.MagicBullet.ULTRA_SHOT_DAMAGE);
                bullet.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 5.0F, 0.5F);
                level.addFreshEntity(bullet);

                level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.MAGIC_BULLET_SHOOT.get(), SoundSource.PLAYERS, 0.8F, 1.2F);
                player.getCooldowns().addCooldown(this, ModConstants.MagicBullet.COOLDOWN_ULTRA);
            }
        }
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int count) {
        if (!level.isClientSide || !(entity instanceof Player player)) {
            return;
        }
        int chargeTime = getUseDuration(stack) - count;
        if (chargeTime > 0 && chargeTime % 10 == 0) {
            level.playSound(player, player.blockPosition(), SoundEvents.CONDUIT_AMBIENT, SoundSource.PLAYERS, 0.4f, 1.2f);
        }
        if (chargeTime > 5) {
            Vec3 center = player.getEyePosition().add(player.getLookAngle().scale(2.5));
            Vec3 lookVec = player.getLookAngle();
            Vec3 upVec = player.getUpVector(1.0F);
            Vec3 rightVec = lookVec.cross(upVec).normalize();
            Vec3 circleUpVec = rightVec.cross(lookVec).normalize();
            float progress = Math.min((chargeTime - 5) / 10f, 1.0f);
            DustParticleOptions circleDust = new DustParticleOptions(new Vector3f(0.1F, 0.2F, 1.0F), 1.0F);
            DustParticleOptions starDust = new DustParticleOptions(new Vector3f(0.1F, 0.8F, 1.0F), 1.0F);
            int circleParticles = 24;
            float circleRadius = 1.7f;
            for (int i = 0; i < circleParticles * progress; i++) {
                double angle = Math.toRadians(i * (360.0 / circleParticles));
                Vec3 point = center.add(rightVec.scale(Math.cos(angle) * circleRadius)).add(circleUpVec.scale(Math.sin(angle) * circleRadius));
                level.addParticle(circleDust, point.x, point.y, point.z, 0, 0, 0);
            }
            if (chargeTime > 10) {
                float starProgress = Math.min((chargeTime - 10) / 10f, 1.0f);
                float starRadius = 1.6f;
                List<Vec3> starVertices = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    double angle = Math.toRadians(i * 72.0 - 90.0);
                    Vec3 vertex = center.add(rightVec.scale(Math.cos(angle) * starRadius)).add(circleUpVec.scale(Math.sin(angle) * starRadius));
                    starVertices.add(vertex);
                }
                drawStarLine(level, starVertices.get(0), starVertices.get(2), starDust, starProgress);
                drawStarLine(level, starVertices.get(1), starVertices.get(3), starDust, starProgress);
                drawStarLine(level, starVertices.get(2), starVertices.get(4), starDust, starProgress);
                drawStarLine(level, starVertices.get(3), starVertices.get(0), starDust, starProgress);
                drawStarLine(level, starVertices.get(4), starVertices.get(1), starDust, starProgress);
            }
        }
    }

    private void drawStarLine(Level level, Vec3 start, Vec3 end, DustParticleOptions particleOptions, float progress) {
        Vec3 difference = end.subtract(start);
        int lineParticles = 8;
        for (int i = 0; i < lineParticles * progress; i++) {
            float t = (float) i / (lineParticles - 1);
            Vec3 point = start.add(difference.scale(t));
            level.addParticle(particleOptions, point.x, point.y, point.z, 0, 0, 0);
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        int bullets = getBulletCount(stack);
        tooltip.add(Component.translatable("item.eschatology.magic_bullet_shooter.bullets", bullets, MAX_BULLETS)
                .withStyle(bullets > 0 ? ChatFormatting.AQUA : ChatFormatting.RED));
        tooltip.add(Component.translatable("item.eschatology.magic_bullet.desc").withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.translatable("item.eschatology.magic_bullet.desc2").withStyle(ChatFormatting.WHITE));
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getBulletCount(stack) < ModConstants.MagicBullet.MAX_BULLETS;
    }
    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0F * (float)getBulletCount(stack) / ModConstants.MagicBullet.MAX_BULLETS);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x00AFFFFF;
    }
}