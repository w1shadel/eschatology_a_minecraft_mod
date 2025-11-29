package com.Maxwell.eschatology.common.Items;import com.Maxwell.eschatology.common.Capability.RevengeLegder.RevengeData;
import com.Maxwell.eschatology.Balance.ModConstants;
import com.Maxwell.eschatology.common.Network.ModMessages;
import com.Maxwell.eschatology.common.Network.SyncRLAbilityGuiPacket;
import com.Maxwell.eschatology.common.Network.SyncRevengeGaugePacket;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;public class RevengeLedgerItem extends SwordItem {
    public RevengeLedgerItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player && !player.level().isClientSide && target.getPersistentData().getBoolean("RevengeMarked")) {
            int gaugeBeforeHit = RevengeData.getGauge(player);
            double extraDamage = computeExtraDamage(player, gaugeBeforeHit);
            if (extraDamage > 0.0) {
                target.hurt(player.level().damageSources().playerAttack(player), (float) extraDamage);
            }            RevengeData.addGauge(player, ModConstants.Revenge.ADD_REVENGE);
            ModMessages.sendToPlayer(new SyncRevengeGaugePacket(RevengeData.getGauge(player)), (ServerPlayer) player);
            target.getPersistentData().remove("RevengeMarked");
        }
        return super.hurtEnemy(stack, target, attacker);
    }    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            if (RevengeData.isMaxGauge(player) && !player.getPersistentData().getBoolean("revenge_skill_active")) {
                RevengeData.consumeGauge(player);
                ModMessages.sendToPlayer(new SyncRevengeGaugePacket(RevengeData.getGauge(player)), serverPlayer);
                player.getPersistentData().putBoolean("revenge_skill_active", true);
                player.getPersistentData().putInt("revenge_skill_stage", 0);
                player.getPersistentData().putInt("revenge_skill_timer", 0);
                player.setInvulnerable(true);
                player.setNoGravity(true);
                ModMessages.sendToPlayer(new SyncRLAbilityGuiPacket(false, ""), serverPlayer);
                Vec3 lookVec = player.getLookAngle();
                Vec3 startPos = player.getEyePosition();
                AABB searchBox = new AABB(startPos, startPos.add(lookVec.scale(ModConstants.Revenge.TARGET_SEARCH_DIST)))
                        .inflate(ModConstants.Revenge.TARGET_SEARCH_WIDTH);
                level.getEntitiesOfClass(LivingEntity.class, searchBox, e -> e != player && e.isAlive())
                        .stream()
                        .min(Comparator.comparingDouble(player::distanceToSqr))
                        .ifPresent(target -> {
                            player.getPersistentData().putUUID("revenge_skill_target", target.getUUID());
                            target.getPersistentData().putBoolean("revenge_skill_frozen", true);
                        });
                level.playSound(null, player.blockPosition(), SoundEvents.WITHER_SPAWN, SoundSource.PLAYERS, 1.0F, 1.0F);
                return InteractionResultHolder.success(player.getItemInHand(hand));
            }
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }    private static double computeExtraDamage(Player player, int gauge) {
        final int MAX_GAUGE = RevengeData.MAX_GAUGE;
        double ratio = Math.max(0.0D, Math.min(1.0D, (double) gauge / (double) MAX_GAUGE));
        double attackSpeed = player.getAttributeValue(Attributes.ATTACK_SPEED);
        double speedFactor = attackSpeed / ModConstants.Revenge.BASE_ATTACK_SPEED_REF;
        speedFactor = Math.max(0.5D, Math.min(2.0D, speedFactor));
        return ModConstants.Revenge.BASE_EXTRA_DAMAGE_AT_FULL * ratio * speedFactor;
    }
    @Override
    public boolean isDamaged(ItemStack stack) {
        return false;
    }    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.eschatology.revenge_ledger.desc").withStyle(ChatFormatting.DARK_PURPLE));
        pTooltipComponents.add(Component.translatable("item.eschatology.revenge_ledger.desc2").withStyle(ChatFormatting.BLUE));
    }
}