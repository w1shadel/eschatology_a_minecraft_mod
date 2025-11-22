package com.Maxwell.eschatology.common.Network;

import com.Maxwell.eschatology.client.GUI.Bossbar.AttachBossBarEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;

public class SyncBossBarPacket implements IClientboundPacket {
    private final UUID bossBarUuid;
    private final int renderId;

    public SyncBossBarPacket(UUID bossBarUuid, int renderId) {
        this.bossBarUuid = bossBarUuid;
        this.renderId = renderId;
    }

    public SyncBossBarPacket(FriendlyByteBuf buf) {
        this.bossBarUuid = buf.readUUID();
        this.renderId = buf.readInt();
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(this.bossBarUuid);
        buf.writeInt(this.renderId);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        AttachBossBarEvents.updateBossBarRenderId(this.bossBarUuid, this.renderId);
    }
}