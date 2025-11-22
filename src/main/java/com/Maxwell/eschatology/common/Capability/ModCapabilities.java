package com.Maxwell.eschatology.common.Capability;import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.common.Capability.ExoHeart.ExoHeartData;
import com.Maxwell.eschatology.common.Capability.WitchsCrest.WCAttackLevel;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;@Mod.EventBusSubscriber(modid = Eschatology.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModCapabilities {
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(ExoHeartData.class);
        event.register(WCAttackLevel.class);
    }
}
