package com.Maxwell.eschatology.common;import com.Maxwell.eschatology.common.Network.ModMessages;
import com.Maxwell.eschatology.common.Network.SyncBossBarPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;public class MaxwellCustomBossEvent extends ServerBossEvent {    private int renderId;    public MaxwellCustomBossEvent(Component name, BossEvent.BossBarColor color, BossEvent.BossBarOverlay overlay, int renderId) {
        super(name, color, overlay);
        this.renderId = renderId;
    }    public int getRenderId() {
        return this.renderId;
    }    public void setRenderId(int newRenderId) {
        if (this.renderId != newRenderId) {
            this.renderId = newRenderId;
            SyncBossBarPacket packet = new SyncBossBarPacket(this.getId(), this.renderId);
            for (ServerPlayer player : this.getPlayers()) {
                ModMessages.sendToPlayer(packet, player);
            }
        }
    }
    @Override
    public void addPlayer(ServerPlayer player) {
        super.addPlayer(player);
        ModMessages.sendToPlayer(new SyncBossBarPacket(this.getId(), this.renderId), player);
    }    @Override
    public void removePlayer(ServerPlayer player) {
        super.removePlayer(player);
        ModMessages.sendToPlayer(new SyncBossBarPacket(this.getId(), -1), player);
    }
}