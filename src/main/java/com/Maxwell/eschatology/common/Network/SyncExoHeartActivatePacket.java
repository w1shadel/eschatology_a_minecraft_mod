package com.Maxwell.eschatology.common.Network;import com.Maxwell.eschatology.common.Capability.ExoHeart.ExoHeartData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
public class SyncExoHeartActivatePacket implements IServerboundPacket {
    private final boolean activate;    public SyncExoHeartActivatePacket(boolean activate) {
        this.activate = activate;
    }    public SyncExoHeartActivatePacket(FriendlyByteBuf buf) {
        this.activate = buf.readBoolean();
    }    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.activate);
    }    @Override
    public void handle(ServerPlayer player, NetworkEvent.Context context) {
        player.getCapability(ExoHeartData.CAPABILITY)
                .ifPresent(data -> data.setActive(this.activate));
    }
}