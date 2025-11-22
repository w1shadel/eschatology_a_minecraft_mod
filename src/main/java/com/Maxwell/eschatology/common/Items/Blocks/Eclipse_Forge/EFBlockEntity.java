package com.Maxwell.eschatology.common.Items.Blocks.Eclipse_Forge;

import com.Maxwell.eschatology.common.Network.ModMessages;
import com.Maxwell.eschatology.common.Network.SyncWorkbenchDataPacket;
import com.Maxwell.eschatology.register.EclipseForge.EFRecipe;
import com.Maxwell.eschatology.register.EclipseForge.EFRecipeType;
import com.Maxwell.eschatology.register.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EFBlockEntity extends BlockEntity implements MenuProvider {
    public static final int SLOT_LEFT = 0;
    public static final int SLOT_RIGHT = 1;
    public static final int SLOT_CENTER = 2;    private boolean isAnimating = false;
    private int animationTicks = 0;
    @Nullable
    private EFRecipe currentRecipe;
    private int currentProcessTime = 80;     private ItemStack animLeft = ItemStack.EMPTY;
    private ItemStack animRight = ItemStack.EMPTY;
    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            if (level != null && !level.isClientSide) {
                EFBlockEntity.this.setChanged();
            }
        }        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };    public EFBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ECLIPSE_FORGE.get(), pos, state);
    }    public static void tick(Level level, BlockPos pos, BlockState state, EFBlockEntity be) {
        if (!level.isClientSide) {            if (be.isAnimating && be.currentRecipe == null) {
                be.isAnimating = false;
                be.animationTicks = 0;
            }            if (be.isAnimating) {
                be.animationTicks++;                if (be.animationTicks >= be.currentProcessTime) {
                    be.finishCraft();
                    be.isAnimating = false;
                    be.animationTicks = 0;
                    be.currentRecipe = null;
                    be.setChanged();
                }
            }            ModMessages.sendToAll(
                    new SyncWorkbenchDataPacket(
                            pos,
                            be.animationTicks,
                            be.isAnimating,
                            be.animLeft,
                            be.animRight
                    )
            );
        }
    }    
    public void clientUpdateFromServer(int ticks, boolean animating, ItemStack left, ItemStack right) {
        this.animationTicks = ticks;
        this.isAnimating = animating;
        this.animLeft = left;
        this.animRight = right;
    }
    public void startAnimation() {
        if (isAnimating || level == null || level.isClientSide) return;        ItemStack left = itemHandler.getStackInSlot(SLOT_LEFT);
        ItemStack right = itemHandler.getStackInSlot(SLOT_RIGHT);
        ItemStack center = itemHandler.getStackInSlot(SLOT_CENTER);
        if (left.isEmpty() || right.isEmpty() || !center.isEmpty()) return;
        Optional<EFRecipe> match = level.getRecipeManager()
                .getAllRecipesFor(EFRecipeType.INSTANCE)
                .stream()
                .filter(r -> ItemStack.isSameItemSameTags(left, r.getLeftInput())
                        && ItemStack.isSameItemSameTags(right, r.getRightInput()))
                .findFirst();
        if (match.isEmpty()) return;
        EFRecipe recipe = match.get();
        this.currentRecipe = recipe;
        this.currentProcessTime = recipe.getProcessTime();
        this.isAnimating = true;
        this.animationTicks = 0;
        this.animLeft = left.copy();
        this.animLeft.setCount(1);
        this.animRight = right.copy();
        this.animRight.setCount(1);
        itemHandler.getStackInSlot(SLOT_LEFT).shrink(1);
        itemHandler.getStackInSlot(SLOT_RIGHT).shrink(1);
        this.setChanged();
    }
    private void finishCraft() {
        if (currentRecipe == null || level == null) return;
        itemHandler.setStackInSlot(SLOT_CENTER, currentRecipe.getOutput().copy());
        level.playSound(null, worldPosition, SoundEvents.END_PORTAL_SPAWN, SoundSource.BLOCKS, 1f, 1f);
        animLeft = ItemStack.EMPTY;
        animRight = ItemStack.EMPTY;
    }    public boolean isAnimating() { return isAnimating; }
    public int getAnimationTicks() { return animationTicks; }
    public int getCurrentProcessTime() {
        return currentProcessTime;
    }
    public ItemStackHandler getItemHandler() { return itemHandler; }    @Override
    public Component getDisplayName() {
        return Component.literal("Eclipse Forge");
    }    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new EFMenu(id, playerInventory, this);
    }    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("isAnimating", isAnimating);
        tag.putInt("animationTicks", animationTicks);
        tag.putInt("processTime", currentProcessTime);
    }    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        isAnimating = tag.getBoolean("isAnimating");
        animationTicks = tag.getInt("animationTicks");
        currentProcessTime = tag.getInt("processTime");
    }    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }    public ItemStack getAnimRight() { return animRight; }
    public ItemStack getAnimLeft() { return animLeft; }
}