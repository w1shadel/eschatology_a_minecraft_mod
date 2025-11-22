package com.Maxwell.eschatology.common.Network;

import com.Maxwell.eschatology.client.EschatologicItem.ShatterEffectHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.Timer;
import java.util.TimerTask;

public class SyncShatterScreenPacket implements IClientboundPacket {

    public SyncShatterScreenPacket() {}
    public SyncShatterScreenPacket(FriendlyByteBuf buf) {}

    @Override
    public void encode(FriendlyByteBuf buf) {}

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handle(NetworkEvent.Context context) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        ShatterEffectHandler.play();

        player.level().playSound(player, player.blockPosition(), SoundEvents.GLASS_BREAK, SoundSource.MASTER, 1.0F, 2.5F);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mc.tell(() -> {
                    if (mc.level != null) {
                        mc.level.disconnect();
                        mc.setScreen(new TitleScreen());
                    }
                });
            }
        }, 2000);
    }
}