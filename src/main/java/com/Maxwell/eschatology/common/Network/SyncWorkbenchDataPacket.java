package com.Maxwell.eschatology.common.Network;

import com.Maxwell.eschatology.common.Items.Blocks.Eclipse_Forge.EFBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class SyncWorkbenchDataPacket implements IClientboundPacket {
    private final BlockPos pos;
    private final int ticks;
    private final boolean animating;
    private final ItemStack leftStack;
    private final ItemStack rightStack;

    public SyncWorkbenchDataPacket(BlockPos pos, int ticks, boolean animating, ItemStack leftStack, ItemStack rightStack) {
        this.pos = pos;
        this.ticks = ticks;
        this.animating = animating;
        this.leftStack = leftStack;
        this.rightStack = rightStack;
    }

    public SyncWorkbenchDataPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.ticks = buf.readInt();
        this.animating = buf.readBoolean();
        this.leftStack = buf.readItem();
        this.rightStack = buf.readItem();
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeInt(this.ticks);
        buf.writeBoolean(this.animating);
        buf.writeItem(this.leftStack);
        buf.writeItem(this.rightStack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handle(NetworkEvent.Context context) {
        Level level = Minecraft.getInstance().level;
        if (level != null && level.getBlockEntity(this.pos) instanceof EFBlockEntity be) {
            be.clientUpdateFromServer(this.ticks, this.animating, this.leftStack, this.rightStack);
        }
    }
}