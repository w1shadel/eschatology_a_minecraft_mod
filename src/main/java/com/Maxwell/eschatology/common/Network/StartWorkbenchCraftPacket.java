package com.Maxwell.eschatology.common.Network;

import com.Maxwell.eschatology.common.Items.Blocks.Eclipse_Forge.EFBlockEntity;
import com.Maxwell.eschatology.common.Items.Blocks.Eclipse_Forge.EFMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
public class StartWorkbenchCraftPacket implements IServerboundPacket {
    private final BlockPos pos;

    public StartWorkbenchCraftPacket(BlockPos pos) {
        this.pos = pos;
    }

    public StartWorkbenchCraftPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public void handle(ServerPlayer player, NetworkEvent.Context context) {
        Level level = player.level();
        if (level.getBlockEntity(pos) instanceof EFBlockEntity && player.containerMenu instanceof EFMenu menu) {
            EFBlockEntity blockEntity = menu.getBlockEntity();
            blockEntity.startAnimation();
        }
    }
}