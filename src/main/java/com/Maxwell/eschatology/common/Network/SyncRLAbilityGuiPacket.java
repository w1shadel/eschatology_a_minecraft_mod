package com.Maxwell.eschatology.common.Network;import com.Maxwell.eschatology.client.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
public class SyncRLAbilityGuiPacket implements IClientboundPacket {
    private final boolean showGui;
    private final String text;
    public SyncRLAbilityGuiPacket(boolean showGui, String text) {
        this.showGui = showGui;
        this.text = text;
    }    public SyncRLAbilityGuiPacket(FriendlyByteBuf buf) {
        this.showGui = buf.readBoolean();
        this.text = buf.readUtf();
    }    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.showGui);
        buf.writeUtf(this.text);
    }    @Override
    public void handle(NetworkEvent.Context context) {
        ClientPacketHandler.handleSyncAbilityGui(this.showGui, this.text);
    }
}