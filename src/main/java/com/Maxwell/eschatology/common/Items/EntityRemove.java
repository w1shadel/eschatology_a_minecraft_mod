package com.Maxwell.eschatology.common.Items;import com.Maxwell.eschatology.common.Network.ModMessages;
import com.Maxwell.eschatology.common.Network.SyncShatterScreenPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;public class EntityRemove extends Item {    private static final Field TYPE_FIELD = findEntityTypeField();    public EntityRemove(Properties pProperties) {
        super(pProperties);
    }    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }
        Entity targetEntity = findTarget(level, player);
        if (targetEntity == null) {
            player.sendSystemMessage(Component.literal("(対象が見つかりません)").withStyle(ChatFormatting.GRAY));
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }
        try {
            if (TYPE_FIELD != null) {                TYPE_FIELD.set(targetEntity, EntityType.FALLING_BLOCK);                if (player instanceof ServerPlayer serverPlayer) {
                    ModMessages.sendToPlayer(new SyncShatterScreenPacket(), serverPlayer);
                }
            } else {
                player.sendSystemMessage(Component.literal("エラー: typeフィールドを特定できませんでした").withStyle(ChatFormatting.RED));
            }
        } catch (Exception e) {
            e.printStackTrace();
            player.sendSystemMessage(Component.literal("変身失敗: " + e.getMessage()).withStyle(ChatFormatting.RED));
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }    /**
     * Entityクラスの中から EntityType 型のフィールドを自動的に探して返すメソッド
     * 名前（f_19815_など）に依存しないため、バージョンが変わっても動作しやすい
     */
    private static Field findEntityTypeField() {
        try {            for (Field field : Entity.class.getDeclaredFields()) {                if (field.getType() == EntityType.class) {
                    field.setAccessible(true); 
                    return field;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }    @Nullable
    private Entity findTarget(Level level, Player player) {
        double reachDistance = 256.0;
        Vec3 eyePosition = player.getEyePosition();
        Vec3 lookVector = player.getViewVector(1.0F);
        Vec3 traceEnd = eyePosition.add(lookVector.scale(reachDistance));
        AABB traceBox = player.getBoundingBox().expandTowards(lookVector.scale(reachDistance)).inflate(1.0D);
        EntityHitResult result = net.minecraft.world.entity.projectile.ProjectileUtil.getEntityHitResult(level, player, eyePosition, traceEnd, traceBox, (entity) -> !entity.isSpectator() && entity.isPickable());
        return (result != null && result.getType() == HitResult.Type.ENTITY) ? result.getEntity() : null;
    }    @Override
    public void onCraftedBy(ItemStack pStack, Level pLevel, Player pPlayer) {
        super.onCraftedBy(pStack, pLevel, pPlayer);
        addArtifactTag(pStack);
    }    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pStack.hasTag() || !Objects.requireNonNull(pStack.getTag()).getBoolean("is_artifact")) {
            addArtifactTag(pStack);
        }
    }    private void addArtifactTag(ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        nbt.putBoolean("is_artifact", true);
    }    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.literal("item.eschatology.haetataki").withStyle(ChatFormatting.DARK_RED, ChatFormatting.OBFUSCATED));
        pTooltipComponents.add(Component.empty());
        pTooltipComponents.add(Component.translatable("item.eschatology.haetataki.desc1").withStyle(ChatFormatting.DARK_PURPLE));
        pTooltipComponents.add(Component.translatable("item.eschatology.haetataki.desc2").withStyle(ChatFormatting.DARK_PURPLE));
        pTooltipComponents.add(Component.empty());
        pTooltipComponents.add(Component.translatable("item.eschatology.haetataki.lore").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }
}