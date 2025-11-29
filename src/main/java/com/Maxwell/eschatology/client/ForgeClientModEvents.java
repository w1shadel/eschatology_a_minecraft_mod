package com.Maxwell.eschatology.client;import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.client.Eclips.EclipseEffectManager;
import com.Maxwell.eschatology.client.GUI.LaserEffectManager;
import com.Maxwell.eschatology.register.ModEffects;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
@SuppressWarnings("removal")
@Mod.EventBusSubscriber(modid = Eschatology.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeClientModEvents {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            LaserEffectManager.tick();
        }
    }    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) {
            if (EclipseEffectManager.isEclipseActive()) {
                EclipseEffectManager.renderBlackSky(event.getPoseStack());
                EclipseEffectManager.renderCustomSun(event.getPoseStack(), event.getPartialTick());
            }
        }
    }    @SubscribeEvent
    public static void onFogColor(ViewportEvent.ComputeFogColor event) {
        if (EclipseEffectManager.isEclipseActive()) {
            event.setRed(0.0f);
            event.setGreen(0.0f);
            event.setBlue(0.0f);
        }
    }    @SubscribeEvent
    public static void onRenderFog(ViewportEvent.RenderFog event) {
        if (EclipseEffectManager.isEclipseActive()) {
            RenderSystem.setShaderFogStart(0.0f);
            RenderSystem.setShaderFogEnd(1.0f);
            RenderSystem.setShaderFogShape(FogShape.SPHERE);
            event.setCanceled(true);
        }
    }    @SubscribeEvent
    public static void onRenderGuiPost(RenderGuiOverlayEvent.Post event) {
        if (LaserEffectManager.isLaserActive()) {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.disableBlend();
        }
    }    private static final ResourceLocation ICE_OVERLAY =
            new ResourceLocation("eschatology", "textures/gui/ice_overlay.png");
    private static float previousProgress = 0.0f;
    private static int fadeInTicks = 0;
    private static final int MAX_FADE_TICKS = 20;    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.player.isDeadOrDying()) return;
        var effect = mc.player.getEffect(ModEffects.FREEZING_STRIKE.get());
        if (effect == null) {
            previousProgress = 0.0f;
            fadeInTicks = 0;
            return;
        }
        int amp = effect.getAmplifier();
        float targetProgress = (amp + 1) / 10.0f;
        if (fadeInTicks < MAX_FADE_TICKS) {
            fadeInTicks++;
        }
        float fadeFactor = (float) fadeInTicks / MAX_FADE_TICKS;
        fadeFactor = Mth.clamp(fadeFactor, 0.0f, 1.0f);
        float partial = mc.getFrameTime();
        float progress = Mth.lerp(0.05f * partial, previousProgress, targetProgress);
        previousProgress = progress;
        GuiGraphics gui = event.getGuiGraphics();
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, ICE_OVERLAY);
        float alpha = progress * fadeFactor * 0.4F;
        alpha = Mth.clamp(alpha, 0.05F, 0.45F);
        RenderSystem.setShaderColor(1F, 1F, 1F, alpha);
        float border = progress * 0.35f;
        int innerX1 = (int) (width * border);
        int innerY1 = (int) (height * border);
        int innerX2 = (int) (width * (1 - border));
        int innerY2 = (int) (height * (1 - border));
        gui.blit(ICE_OVERLAY, 0, 0, 0, 0, width, innerY1, width, height);
        gui.blit(ICE_OVERLAY, 0, innerY2, 0, innerY2, width, height - innerY2, width, height);
        gui.blit(ICE_OVERLAY, 0, innerY1, 0, innerY1, innerX1, innerY2 - innerY1, width, height);
        gui.blit(ICE_OVERLAY, innerX2, innerY1, innerX2, innerY1, width - innerX2, innerY2 - innerY1, width, height);
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
    }
}