package com.Maxwell.eschatology.common.Network;

import com.Maxwell.eschatology.client.ExoHeart.ExoHeartHudOverlay;
import com.Maxwell.eschatology.common.Capability.ExoHeart.ExoHeartData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
public class SyncExoHeartDataPacket implements IClientboundPacket {
    private final int charge;
    private final boolean active;

    public SyncExoHeartDataPacket(int charge, boolean active) {
        this.charge = charge;
        this.active = active;
    }

    public SyncExoHeartDataPacket(FriendlyByteBuf buf) {
        this.charge = buf.readInt();
        this.active = buf.readBoolean();
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.charge);
        buf.writeBoolean(this.active);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handle(NetworkEvent.Context context) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            player.getCapability(ExoHeartData.CAPABILITY).ifPresent(data -> {
                data.setCharge(this.charge);
                data.setActive(this.active);
            });
            ExoHeartHudOverlay.clientCharge = this.charge;
        }
    }
}