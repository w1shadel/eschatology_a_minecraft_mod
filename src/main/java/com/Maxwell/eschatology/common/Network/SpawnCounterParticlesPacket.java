package com.Maxwell.eschatology.common.Network;import com.Maxwell.eschatology.client.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;public class SpawnCounterParticlesPacket implements IClientboundPacket {
    private final Vec3 position;    public SpawnCounterParticlesPacket(Vec3 position) {
        this.position = position;
    }    public SpawnCounterParticlesPacket(FriendlyByteBuf buf) {
        this.position = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeDouble(this.position.x());
        buf.writeDouble(this.position.y());
        buf.writeDouble(this.position.z());
    }    @Override
    public void handle(NetworkEvent.Context context) {
        ClientPacketHandler.handleSpawnCounterParticles(this);
    }    public Vec3 getPosition() {
        return position;
    }
}