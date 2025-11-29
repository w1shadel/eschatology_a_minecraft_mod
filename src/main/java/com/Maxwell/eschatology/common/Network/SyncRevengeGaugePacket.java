package com.Maxwell.eschatology.common.Network;import com.Maxwell.eschatology.client.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;public class SyncRevengeGaugePacket implements IClientboundPacket {
    public final int gauge;    public SyncRevengeGaugePacket(int gauge) {
        this.gauge = gauge;
    }    public SyncRevengeGaugePacket(FriendlyByteBuf buf) {
        this.gauge = buf.readInt();
    }    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.gauge);
    }    @Override
    public void handle(NetworkEvent.Context context) {
        ClientPacketHandler.handleSyncRevengeGauge(this);
    }
}