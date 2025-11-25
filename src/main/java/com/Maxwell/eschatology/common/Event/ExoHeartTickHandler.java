package com.Maxwell.eschatology.common.Event;

import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.common.Capability.ExoHeart.ExoHeartData;
import com.Maxwell.eschatology.Balance.ModConstants;
import com.Maxwell.eschatology.common.Network.ModMessages;
import com.Maxwell.eschatology.common.Network.SyncExoHeartDataPacket;
import com.Maxwell.eschatology.register.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.WeakHashMap;
@SuppressWarnings("removal")
@Mod.EventBusSubscriber(modid = Eschatology.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ExoHeartTickHandler {
    private static final TagKey<EntityType<?>> BOSS_TAG =
            TagKey.create(ForgeRegistries.ENTITY_TYPES.getRegistryKey(), new ResourceLocation("forge", "bosses"));

    private static final WeakHashMap<Player, Integer> lastNearBossTick = new WeakHashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        Level level = player.level();
        if (level.isClientSide || player.isSpectator() || player.isCreative()) return;

        player.getCapability(ExoHeartData.CAPABILITY).ifPresent(data -> {
            int gameTime = (int) level.getGameTime();
            int before = data.getCharge();
            int charge = before;
            boolean hasHeart = CuriosApi.getCuriosHelper()
                    .findEquippedCurio(ModItems.FROZEN_EXOHEART.get(), player)
                    .isPresent();
            boolean isNearBoss = !level.getEntitiesOfClass(
                    LivingEntity.class,
                    new AABB(player.blockPosition()).inflate(ModConstants.ExoHeart.BOSS_CHECK_RADIUS),
                    entity -> entity.getType().is(BOSS_TAG)
            ).isEmpty();

            if (data.isActive() && hasHeart) {
                if (gameTime % ModConstants.ExoHeart.ACTIVE_DRAIN_INTERVAL == 0 && charge > 0) {
                    charge -= ModConstants.ExoHeart.ACTIVE_DRAIN_AMOUNT;
                }
                if (gameTime % ModConstants.ExoHeart.ACTIVE_HEAL_INTERVAL == 0 && player.getHealth() < player.getMaxHealth()) {
                    player.heal(ModConstants.ExoHeart.ACTIVE_HEAL_AMOUNT);
                }
                if (charge <= 0) {
                    charge = 0;
                    data.setActive(false);
                }
            }

            if (!data.isActive() && isNearBoss) {
                lastNearBossTick.put(player, gameTime);
                charge += ModConstants.ExoHeart.PASSIVE_CHARGE_AMOUNT;
            } else if (!isNearBoss) {
                int lastSeen = lastNearBossTick.getOrDefault(player, gameTime);
                if (gameTime - lastSeen > ModConstants.ExoHeart.LOSS_DELAY_TICKS && charge > 0) {
                    charge -= ModConstants.ExoHeart.PASSIVE_DRAIN_AMOUNT;
                }
            }

            charge = Math.max(0, Math.min(ModConstants.ExoHeart.MAX_CHARGE, charge));

            if (charge != before) {
                data.setCharge(charge);
                if (player instanceof ServerPlayer sp) {
                    ModMessages.sendToPlayer(new SyncExoHeartDataPacket(charge, data.isActive()), sp);
                }
            }
        });
    }
}