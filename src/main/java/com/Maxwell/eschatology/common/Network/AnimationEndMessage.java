package com.Maxwell.eschatology.common.Network;import com.Maxwell.eschatology.common.helper.DamageHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;public class AnimationEndMessage implements IServerboundPacket {    public AnimationEndMessage() {}    public AnimationEndMessage(FriendlyByteBuf buf) {    }    @Override
    public void encode(FriendlyByteBuf buf) {    }    @Override
    public void handle(ServerPlayer player, NetworkEvent.Context context) {        DamageHelper.dealAweContemptDamage(player);
    }
}