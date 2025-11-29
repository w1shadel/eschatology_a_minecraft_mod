package com.Maxwell.eschatology.common.Network;import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;public interface IServerboundPacket {    void encode(FriendlyByteBuf buf);    void handle(ServerPlayer player, NetworkEvent.Context context);
}