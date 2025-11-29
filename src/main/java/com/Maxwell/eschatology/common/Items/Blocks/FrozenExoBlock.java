package com.Maxwell.eschatology.common.Items.Blocks;import com.Maxwell.eschatology.Boss.XF07_Revanant.ExoWither;
import com.Maxwell.eschatology.register.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;import java.util.List;public class FrozenExoBlock extends Block {    public FrozenExoBlock() {
        super(Properties.copy(Blocks.ICE)
                .mapColor(MapColor.ICE)
                .strength(-1.0F, 3600000.0F)
                .sound(SoundType.GLASS)
                .noOcclusion()
                .lightLevel(s -> 3)
                .requiresCorrectToolForDrops());
    }
    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
        return PushReaction.BLOCK;
    }    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        boolean nearHeat = false;
        for (BlockPos nearby : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
            var block = level.getBlockState(nearby).getBlock().toString();
            if (block.contains("fire") || block.contains("lava")) {
                nearHeat = true;
                break;
            }
        }
        if (nearHeat) {
            meltAndAwaken(level, pos);
        }
    }    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack item = player.getItemInHand(hand);        if (item.is(Items.FLINT_AND_STEEL)) {
            if (!level.isClientSide) {
                level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                item.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
                meltAndAwaken((ServerLevel) level, pos);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        else
        {
            if (!level.isClientSide) {
                player.sendSystemMessage(Component.translatable("guide.frozen_exo.flint"));
            }
        }
        return InteractionResult.SUCCESS;
    }    private void meltAndAwaken(ServerLevel level, BlockPos pos) {        level.removeBlock(pos, false);        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.2F, 0.6F);
        level.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1.0F, 1.4F);        for (int i = 0; i < 25; i++) {
            level.sendParticles(net.minecraft.core.particles.ParticleTypes.SNOWFLAKE,
                    pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5,
                    1, 0.3, 0.4, 0.3, 0.02);
        }        AABB area = new AABB(pos).inflate(1.5);
        List<ExoWither> list = level.getEntitiesOfClass(ExoWither.class, area);
        if (!list.isEmpty()) {
            for (ExoWither exo : list) {
                exo.setNoAi(false);
                exo.setInvulnerable(false);
                exo.playSound(SoundEvents.WARDEN_ROAR, 4.0F, 0.6F);
            }
        } else {
            ExoWither exo = ModEntities.EXO_WITHER.get().create(level);
            if (exo != null) {
                Vec3 center = Vec3.atCenterOf(pos);
                exo.moveTo(center.x, center.y + 0.5, center.z, level.random.nextFloat() * 360F, 0);
                exo.setNoAi(false);
                exo.setInvulnerable(false);
                level.addFreshEntity(exo);
            }
        }
    }
}