package com.Maxwell.eschatology.common.Network;import com.Maxwell.eschatology.common.Capability.RevengeLegder.RevengeData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
public class RequestRevengeGaugeIncreasePacket implements IServerboundPacket {
    private final int amount;    public RequestRevengeGaugeIncreasePacket(int amount) {
        this.amount = amount;
    }    public RequestRevengeGaugeIncreasePacket(FriendlyByteBuf buf) {
        this.amount = buf.readInt();
    }    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.amount);
    }    @Override
    public void handle(ServerPlayer player, NetworkEvent.Context context) {
        RevengeData.addGauge(player, this.amount);
        ModMessages.sendToPlayer(new SyncRevengeGaugePacket(RevengeData.getGauge(player)), player);
    }
}