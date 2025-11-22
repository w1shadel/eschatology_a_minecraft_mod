package com.Maxwell.eschatology.common.Network;

import com.Maxwell.eschatology.client.GUI.LaserEffectManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class SyncLaserEffectsPacket implements IClientboundPacket {
    private final EffectPhase phase;

    public enum EffectPhase { START_FIRING, STOP_FIRING }

    public SyncLaserEffectsPacket(EffectPhase phase) {
        this.phase = phase;
    }

    public SyncLaserEffectsPacket(FriendlyByteBuf buf) {
        this.phase = buf.readEnum(EffectPhase.class);
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(this.phase);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handle(NetworkEvent.Context context) {
        LaserEffectManager.handleLaserEffect(this.phase);
    }
}