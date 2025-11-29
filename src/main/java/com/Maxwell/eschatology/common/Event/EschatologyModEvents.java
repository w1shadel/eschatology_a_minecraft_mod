package com.Maxwell.eschatology.common.Event;import com.Maxwell.eschatology.Boss.BlackBool.BlackBool;
import com.Maxwell.eschatology.Boss.XF07_Revanant.ExoWither;
import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.register.ModEntities;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;@Mod.EventBusSubscriber(modid = Eschatology.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EschatologyModEvents {
    @SubscribeEvent
    public static void registerAttributes(
            EntityAttributeCreationEvent event) {
        event.put(ModEntities.BLACK_BOOL.get(), BlackBool.createAttributes().build());
        event.put(ModEntities.EXO_WITHER.get(), ExoWither.createAttributes().build());
    }
}