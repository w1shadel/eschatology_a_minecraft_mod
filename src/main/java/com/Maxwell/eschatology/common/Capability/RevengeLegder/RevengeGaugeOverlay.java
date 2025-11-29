package com.Maxwell.eschatology.common.Capability.RevengeLegder;import com.Maxwell.eschatology.Config.ClientConfig;
import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.register.ModItems;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
@SuppressWarnings("removal")
@Mod.EventBusSubscriber(modid = Eschatology.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class RevengeGaugeOverlay {    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (mc.options.hideGui) return;        if (!ClientConfig.CLIENT.showRevengeGauge.get()) return;        if (!mc.player.getMainHandItem().is(ModItems.REVENGE_LEDGER.get()) &&
                !mc.player.getOffhandItem().is(ModItems.REVENGE_LEDGER.get())) {
            return;
        }        int gauge = RevengeData.getGaugeClient(mc.player);
        int maxGauge = RevengeData.MAX_GAUGE;
        float ratio = (float) gauge / (float) maxGauge;
        ratio = Math.min(1.0f, Math.max(0.0f, ratio));        int configX = ClientConfig.CLIENT.revengeGaugeX.get();
        int configYOffset = ClientConfig.CLIENT.revengeGaugeYOffset.get();
        int width = ClientConfig.CLIENT.revengeGaugeWidth.get();
        int height = ClientConfig.CLIENT.revengeGaugeHeight.get();        int x = configX;        int y = mc.getWindow().getGuiScaledHeight() / 2 + configYOffset;        GuiGraphics gg = event.getGuiGraphics();
        RenderSystem.enableBlend();        gg.fill(x, y, x + width, y + height, 0x80000000);        int filledHeight = (int) (height * ratio);
        int filledY = y + height - filledHeight;        int color;
        if (ratio >= 1.0f) {
            double t = (System.currentTimeMillis() % 600) / 600.0;
            int alpha = (int) (0xA0 + 0x50 * Math.sin(t * Math.PI * 2));
            color = (alpha << 24) | 0xFF0000;
        } else {
            color = 0xFFAA0000;
        }        gg.fill(x + 1, filledY + 1, x + width - 1, y + height - 1, color);        String percent = (int) (ratio * 100) + "%";        gg.drawString(mc.font, percent, x + width + 5, y + height / 2, 0xFFFFFF);
    }
}