package com.Maxwell.eschatology.common.Capability.WitchsCrest;import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.common.Network.ModMessages;
import com.Maxwell.eschatology.common.Network.SyncWCPlayerATKLevelPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
@SuppressWarnings("removal")
@Mod.EventBusSubscriber(modid = Eschatology.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WCCapabilityEvents {
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof Player)) {
            return;
        }
        event.addCapability(new ResourceLocation(Eschatology.MODID, "attacklevel"), new WCAttackLevelProvider());
    }
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().getCapability(WCAttackLevelProvider.PLAYER_ATTACK_LEVEL).ifPresent(oldStore -> {
                event.getEntity().getCapability(WCAttackLevelProvider.PLAYER_ATTACK_LEVEL).ifPresent(newStore -> {
                    newStore.setLevel(oldStore.getLevel());
                    if(event.getEntity() instanceof ServerPlayer player) {
                        ModMessages.sendToPlayer(new SyncWCPlayerATKLevelPacket(newStore.getLevel()), player);
                    }
                });
            });
        }
    }
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if(event.getEntity() instanceof ServerPlayer player) {
            player.getCapability(WCAttackLevelProvider.PLAYER_ATTACK_LEVEL).ifPresent(level -> {
                ModMessages.sendToPlayer(new SyncWCPlayerATKLevelPacket(level.getLevel()), player);
            });
        }
    }
}