package com.Maxwell.eschatology.common.Items.Blocks.Eclipse_Forge;import com.Maxwell.eschatology.register.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;public class EFMenu extends AbstractContainerMenu {
    private final EFBlockEntity blockEntity;
    private final ContainerData data;    public EFMenu(int id, Inventory playerInv, FriendlyByteBuf extraData) {
        this(id, playerInv, (EFBlockEntity) playerInv.player.level().getBlockEntity(extraData.readBlockPos()));
    }    public EFMenu(int id, Inventory playerInv, EFBlockEntity entity) {
        super(ModMenuTypes.ECLIPSE_FORGE.get(), id);
        this.blockEntity = entity;
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> blockEntity.isAnimating() ? 1 : 0;
                    case 1 -> blockEntity.getAnimationTicks();
                    default -> 0;
                };
            }            @Override
            public void set(int index, int value) {
                if (index == 0) blockEntity.startAnimation();
            }            @Override
            public int getCount() {
                return 2;
            }
        };
        this.addDataSlots(data);
        IItemHandler handler = entity.getItemHandler();
        this.addSlot(new SlotItemHandler(handler, EFBlockEntity.SLOT_LEFT, 26, 33));
        this.addSlot(new SlotItemHandler(handler, EFBlockEntity.SLOT_RIGHT, 133, 33));
        this.addSlot(new SlotItemHandler(handler, EFBlockEntity.SLOT_CENTER, 80, 33));
        addPlayerInventory(playerInv);
        addPlayerHotbar(playerInv);
    }    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
    }    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i)
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
    }    @Override
    public boolean stillValid(Player player) {
        return true;
    }    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();
            if (index < 3) {
                if (!this.moveItemStackTo(stack, 3, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack, 0, 3, false)) {
                return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }    public EFBlockEntity getBlockEntity() {
        return blockEntity;
    }    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        if (this.blockEntity != null && this.blockEntity.getLevel() != null) {
            if (!this.blockEntity.getLevel().isClientSide) {
                this.data.set(1, this.blockEntity.getAnimationTicks());
            }
        }
    }    public boolean isAnimating() {
        return data.get(0) == 1;
    }    public int getAnimationTicks() {
        return data.get(1);
    }
}