package com.Maxwell.eschatology.common.Network;

import com.Maxwell.eschatology.client.GUI.Glitch.GlitchyGuiManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
public class SyncGlitchEffectPacket implements IClientboundPacket {
    private final float intensity;

    public SyncGlitchEffectPacket(float intensity) {
        this.intensity = intensity;
    }

    public SyncGlitchEffectPacket(FriendlyByteBuf buf) {
        this.intensity = buf.readFloat();
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeFloat(this.intensity);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handle(NetworkEvent.Context context) {
        GlitchyGuiManager.setTargetIntensity(this.intensity);
    }
}