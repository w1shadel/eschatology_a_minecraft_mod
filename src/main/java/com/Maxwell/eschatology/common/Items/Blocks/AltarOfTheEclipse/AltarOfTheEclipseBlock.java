package com.Maxwell.eschatology.common.Items.Blocks.AltarOfTheEclipse;import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.register.ModEntities;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;@SuppressWarnings("removal")
public class AltarOfTheEclipseBlock extends Block {    public AltarOfTheEclipseBlock() {
        super(Properties.of()
                .mapColor(MapColor.COLOR_BLACK)
                .requiresCorrectToolForDrops()
                .strength(50.0F, 1200.0F)
                .sound(SoundType.GILDED_BLACKSTONE));
    }    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        ItemStack heldItem = pPlayer.getItemInHand(pHand);
        if (!(pPlayer instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.PASS;
        }
        if (pLevel.isRaining() || pLevel.isThundering()) {
            serverPlayer.sendSystemMessage(Component.translatable("guide.alter.time"));
            return InteractionResult.FAIL;
        }
        if (heldItem.is(Items.NETHER_STAR)) {
            return startNormalRitual(serverPlayer, heldItem, (ServerLevel) pLevel, pPos);
        }
        serverPlayer.sendSystemMessage(Component.translatable("guide.alter.nether_star"));
        return InteractionResult.PASS;
    }
    private InteractionResult startNormalRitual(ServerPlayer player, ItemStack stack, ServerLevel level, BlockPos pos) {
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        EclipseManager.tryStartEclipse(level, pos, ModEntities.BLACK_BOOL.get());
        grantAdvancement(player, "boss/summon_black_bool");        return InteractionResult.CONSUME;
    }
    private void grantAdvancement(ServerPlayer player, String advancementId) {
        ResourceLocation advancementRL = new ResourceLocation(Eschatology.MODID, advancementId);
        Advancement advancement = player.getServer().getAdvancements().getAdvancement(advancementRL);        if (advancement != null) {
            AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
            if (!progress.isDone()) {
                for (String criterion : progress.getRemainingCriteria()) {
                    player.getAdvancements().award(advancement, criterion);
                }
            }
        }
    }
}