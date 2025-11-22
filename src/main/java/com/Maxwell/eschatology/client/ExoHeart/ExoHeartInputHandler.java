package com.Maxwell.eschatology.client.ExoHeart;

import com.Maxwell.eschatology.common.Network.ModMessages;
import com.Maxwell.eschatology.common.Network.SyncExoHeartActivatePacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ExoHeartInputHandler {    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;        if (ExoHeartKeybind.ACTIVATE_EXOHEART.consumeClick()) {
            ModMessages.sendToServer(new SyncExoHeartActivatePacket(true));
        }
    }
}