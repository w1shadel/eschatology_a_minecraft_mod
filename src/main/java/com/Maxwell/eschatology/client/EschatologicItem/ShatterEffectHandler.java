package com.Maxwell.eschatology.client.EschatologicItem;import com.Maxwell.eschatology.Eschatology;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
@SuppressWarnings("removal")
public class ShatterEffectHandler {    private static final ResourceLocation SHATTER_TEXTURE = new ResourceLocation(Eschatology.MODID, "textures/gui/shatter_overlay.png");
    private static long startTime = -1;
    private static final long DURATION_MS = 2000;
    public static void play() {        if (startTime == -1) {
            MinecraftForge.EVENT_BUS.register(new ShatterEffectHandler());
            startTime = System.currentTimeMillis();
        }
    }
     @SubscribeEvent
     public void onRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
     if (startTime == -1) return;     long elapsedTime = System.currentTimeMillis() - startTime;
     if (elapsedTime > DURATION_MS) {     MinecraftForge.EVENT_BUS.unregister(this);
     startTime = -1;
     return;
     }     Minecraft mc = Minecraft.getInstance();
     var guiGraphics = event.getGuiGraphics();
     int screenWidth = mc.getWindow().getGuiScaledWidth();
     int screenHeight = mc.getWindow().getGuiScaledHeight();
     float progress = (float) elapsedTime / DURATION_MS;
     RenderSystem.disableDepthTest();
     RenderSystem.depthMask(false);
     RenderSystem.enableBlend();
     RenderSystem.defaultBlendFunc();
     float alpha = Math.min(1.0f, progress * 4.0f);
     float color = 1.0f - progress;
     guiGraphics.setColor(color, color, color, alpha);
     guiGraphics.blit(SHATTER_TEXTURE, 0, 0, 0, 0, screenWidth, screenHeight, screenWidth, screenHeight);
     guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
     RenderSystem.depthMask(true);
     RenderSystem.enableDepthTest();
      RenderSystem.disableBlend();
     }
}