package com.Maxwell.eschatology.Boss.BlackBool;import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.common.Items.Blocks.AltarOfTheEclipse.EclipseManager;
import com.Maxwell.eschatology.register.ModEntities;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;@Mod.EventBusSubscriber(modid = Eschatology.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BlackBoolEvents {    @SubscribeEvent
    public static void onBossDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide) return;
        if (event.getEntity().getType() == ModEntities.BLACK_BOOL.get()) {
            if (event.getEntity().level() instanceof ServerLevel serverLevel) {
                EclipseManager.endEclipse(serverLevel);
                 serverLevel.players().forEach(p -> p.sendSystemMessage(Component.translatable("guide.alter.end")));
            }
        }
    }
}