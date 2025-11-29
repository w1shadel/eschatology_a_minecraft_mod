package com.Maxwell.eschatology.common.Items;import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;import javax.annotation.Nullable;
import java.util.List;public class Apistle extends Item {
    public Apistle(Properties pProperties) {
        super(pProperties);
    }    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        long speed = 500;
        int messageCount = 8;
        long index = (System.currentTimeMillis() / speed) % messageCount;
        String langKey = "item.eschatology.apistle." + index;
        pTooltipComponents.add(Component.translatable(langKey).withStyle(ChatFormatting.DARK_RED));
    }
}