package com.Maxwell.eschatology.common.Items.Blocks.Eclipse_Forge;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class EFBlock extends BaseEntityBlock {
    public EFBlock(Properties properties) {
        super(properties);
    }
    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (!level.isClientSide) {
            return (lvl, pos, st, be) -> {
                if (be instanceof EFBlockEntity workbench) {
                    EFBlockEntity.tick(lvl, pos, st, workbench);
                }
            };
        }
        return null;
    }
    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(0.2, 0, 0.2, 15.8, 8, 15.8),
            Block.box(6, 11, 6, 10, 13, 10)
    );
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
        return SHAPE;
    }
    @Override
    public boolean useShapeForLightOcclusion(BlockState pState) {
        return true;
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EFBlockEntity(pos, state);
    }
    public static void tick(BlockState state, Level level, BlockPos pos, BlockEntity blockEntity) {
        if (blockEntity instanceof EFBlockEntity be) {
            EFBlockEntity.tick(level, pos, state, be);
        }
    }    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {        }
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof EFBlockEntity entity) {
                entity.startAnimation();
                NetworkHooks.openScreen(
                        (ServerPlayer) player,
                        new MenuProvider() {
                            @Override
                            public Component getDisplayName() {
                                return Component.translatable("container.eclipse_forge");
                            }                            @Override
                            public AbstractContainerMenu createMenu(int id, Inventory inv, Player p) {
                                return new EFMenu(id, inv, entity);
                            }
                        },
                        pos
                );
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}