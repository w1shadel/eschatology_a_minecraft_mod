package com.Maxwell.eschatology.client.GUI.Bossbar;import com.Maxwell.eschatology.Eschatology;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;@Mod.EventBusSubscriber(modid = Eschatology.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class AttachBossBarEvents {
    private static final Map<UUID, Integer> BOSS_BAR_RENDER_IDS = new ConcurrentHashMap<>();
    public static void updateBossBarRenderId(UUID bossBarUuid, int renderId) {
        if (renderId < 0) {
            BOSS_BAR_RENDER_IDS.remove(bossBarUuid);
        } else {
            BOSS_BAR_RENDER_IDS.put(bossBarUuid, renderId);
        }
    }    @SubscribeEvent
    public static void onBossBarRender(CustomizeGuiOverlayEvent.BossEventProgress event) {
        UUID uuid = event.getBossEvent().getId();
        Integer renderId = BOSS_BAR_RENDER_IDS.get(uuid);
        if (renderId != null) {
            MaxwellCustomBossBar customBossBarToRender = MaxwellCustomBossBar.customBossBars.get(renderId);
            if (customBossBarToRender != null) {
                customBossBarToRender.renderBossBar(event);
                event.setCanceled(true);
            }
        }
    }
}