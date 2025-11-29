package com.Maxwell.eschatology.common.Network;import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;public interface IClientboundPacket {    
    void encode(FriendlyByteBuf buf);    
    void handle(NetworkEvent.Context context);
}