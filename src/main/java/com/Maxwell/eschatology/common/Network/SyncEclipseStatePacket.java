package com.Maxwell.eschatology.common.Network;import com.Maxwell.eschatology.client.Eclips.EclipseEffectManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
public class SyncEclipseStatePacket implements IClientboundPacket {
    private final boolean isEclipseActive;    public SyncEclipseStatePacket(boolean isEclipseActive) {
        this.isEclipseActive = isEclipseActive;
    }    public SyncEclipseStatePacket(FriendlyByteBuf buf) {
        this.isEclipseActive = buf.readBoolean();
    }    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.isEclipseActive);
    }    @Override
    public void handle(NetworkEvent.Context context) {
        EclipseEffectManager.setEclipseActive(this.isEclipseActive);
    }
}