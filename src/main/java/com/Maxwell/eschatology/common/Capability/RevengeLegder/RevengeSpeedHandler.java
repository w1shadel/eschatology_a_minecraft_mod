package com.Maxwell.eschatology.common.Capability.RevengeLegder;import com.Maxwell.eschatology.Eschatology;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;import java.util.UUID;@Mod.EventBusSubscriber(modid = Eschatology.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RevengeSpeedHandler {
    private static final UUID REVENGE_SPEED_UUID = UUID.fromString("5b8a3f5d-9f1a-4b6f-9f4e-1b2cde9f0001");    private static final double MAX_ATTACK_SPEED_BONUS = 2.0D;    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        if (player.level().isClientSide || !(player instanceof ServerPlayer)) return;        AttributeInstance inst = player.getAttribute(Attributes.ATTACK_SPEED);
        if (inst == null) return;        int gauge = RevengeData.getGauge(player);
        double ratio = Math.max(0.0D, Math.min(1.0D, (double) gauge / (double) RevengeData.MAX_GAUGE));
        double bonus = MAX_ATTACK_SPEED_BONUS * ratio;
        AttributeModifier existingModifier = inst.getModifier(REVENGE_SPEED_UUID);
        double existingBonus = existingModifier != null ? existingModifier.getAmount() : -1.0;
        if (bonus < 1e-6) {
            if (existingModifier != null) {
                inst.removeModifier(existingModifier);
            }
        }
        else if (Math.abs(bonus - existingBonus) > 1e-6) {
            if (existingModifier != null) {
                inst.removeModifier(existingModifier);
            }
            AttributeModifier newModifier = new AttributeModifier(REVENGE_SPEED_UUID, "revenge_ledger_speed", bonus, AttributeModifier.Operation.ADDITION);
            inst.addTransientModifier(newModifier);
        }
    }
}