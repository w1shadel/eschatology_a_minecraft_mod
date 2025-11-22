package com.Maxwell.eschatology.common.Network;

import com.Maxwell.eschatology.common.Capability.WitchsCrest.WCAttackLevelProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
public class SyncWCPlayerATKLevelPacket implements IClientboundPacket {

    private final int attackLevel;

    
    public SyncWCPlayerATKLevelPacket(int attackLevel) {
        this.attackLevel = attackLevel;
    }

    
    public SyncWCPlayerATKLevelPacket(FriendlyByteBuf buf) {
        this.attackLevel = buf.readInt();
    }

    
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.attackLevel);
    }

    
    @Override
    @OnlyIn(Dist.CLIENT) 
    public void handle(NetworkEvent.Context context) {

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        mc.player.getCapability(WCAttackLevelProvider.PLAYER_ATTACK_LEVEL)
                .ifPresent(level -> level.setLevel(this.attackLevel));
    }
}