package com.Maxwell.eschatology.common.Items.Curios;import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;import javax.annotation.Nullable;
import java.util.List;public class The_once_and_future_gun extends CuriosItem {
    public The_once_and_future_gun(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide()) {
            pPlayer.sendSystemMessage(Component.translatable("item.eschatology.the_once_and_future_gun_outofammo"));
        }
        return InteractionResultHolder.fail(pPlayer.getItemInHand(pUsedHand));
    }
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_IRON, 1.2F, 0.6F);
    }
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.eschatology.the_once_and_future_gun.desc").withStyle(ChatFormatting.WHITE));
        pTooltipComponents.add(Component.translatable("item.eschatology.the_once_and_future_gun.desc2").withStyle(ChatFormatting.WHITE));
    }
}
