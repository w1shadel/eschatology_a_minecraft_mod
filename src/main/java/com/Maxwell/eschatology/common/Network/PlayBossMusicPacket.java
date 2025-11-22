package com.Maxwell.eschatology.common.Network;

import com.Maxwell.eschatology.client.BossMusicManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;
public class PlayBossMusicPacket implements IClientboundPacket {

    @Nullable
    private final ResourceLocation soundLocation;

    
    public PlayBossMusicPacket(@Nullable ResourceLocation soundLocation) {
        this.soundLocation = soundLocation;
    }

    
    public PlayBossMusicPacket(FriendlyByteBuf buf) {
        if (buf.readBoolean()) {
            this.soundLocation = buf.readResourceLocation();
        } else {
            this.soundLocation = null;
        }
    }

    
    @Override
    public void encode(FriendlyByteBuf buf) {
        if (this.soundLocation != null) {
            buf.writeBoolean(true);
            buf.writeResourceLocation(this.soundLocation);
        } else {
            buf.writeBoolean(false);
        }
    }

    
    @Override
    public void handle(NetworkEvent.Context context) {
        if (this.soundLocation != null) {
            BossMusicManager.playBossMusic(this.soundLocation);
        } else {
            BossMusicManager.stopBossMusic();
        }
    }
}