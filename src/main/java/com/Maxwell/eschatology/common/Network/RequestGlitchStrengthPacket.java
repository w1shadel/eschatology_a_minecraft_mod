package com.Maxwell.eschatology.common.Network;

import com.Maxwell.eschatology.client.GUI.Glitch.GlitchState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class RequestGlitchStrengthPacket implements IServerboundPacket {
    private final float intensity;
    private final boolean cycle;
    public RequestGlitchStrengthPacket(float intensity, boolean cycle) {
        this.intensity = intensity;
        this.cycle = cycle;
    }

    public RequestGlitchStrengthPacket(FriendlyByteBuf buf) {
        this.intensity = buf.readFloat();
        this.cycle = buf.readBoolean();
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeFloat(this.intensity);
        buf.writeBoolean(this.cycle);
    }

    @Override
    public void handle(ServerPlayer player, NetworkEvent.Context context) {
        if (this.cycle) {
            GlitchState.cycleGlitchEffect(player.level());
        } else {
            GlitchState.setGlitchEffect(player.level(), this.intensity);
        }
    }
}