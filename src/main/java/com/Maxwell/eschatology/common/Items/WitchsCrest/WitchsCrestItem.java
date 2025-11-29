package com.Maxwell.eschatology.common.Items.WitchsCrest;import com.Maxwell.eschatology.common.Capability.WitchsCrest.WCAttackLevelProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;import javax.annotation.Nullable;
import java.util.List;public class WitchsCrestItem extends SwordItem {    public WitchsCrestItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }    @Override
    public boolean isDamaged(ItemStack stack) {
        return false;
    }    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.getCapability(WCAttackLevelProvider.PLAYER_ATTACK_LEVEL).ifPresent(level -> {
                int currentLevel = level.getLevel();
                Component levelText = Component.translatable("item.eschatology.witchs_crest_level", currentLevel)
                        .withStyle(ChatFormatting.DARK_PURPLE);
                pTooltipComponents.add(levelText);
            });
        }
        pTooltipComponents.add(Component.translatable("item.eschatology.witchs_crest_desc").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}