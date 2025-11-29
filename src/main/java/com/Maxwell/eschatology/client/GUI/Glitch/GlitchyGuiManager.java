package com.Maxwell.eschatology.client.GUI.Glitch;import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;import java.util.List;public class GlitchyGuiManager {    private final Minecraft mc = Minecraft.getInstance();
    private final RandomSource random = RandomSource.create();
    private static float currentIntensity = 0.0f; 
    private static float targetIntensity = 0.0f;  
    private static final List<ResourceLocation> CONDITIONALLY_EXCLUDED_OVERLAYS = List.of(
            VanillaGuiOverlay.PLAYER_HEALTH.id(),
            VanillaGuiOverlay.FOOD_LEVEL.id(),
            VanillaGuiOverlay.HOTBAR.id()
    );
    private static final float CONDITIONAL_THRESHOLD = 0.50f;
    private static final List<ResourceLocation> ABSOLUTELY_EXCLUDED_OVERLAYS = List.of(
            VanillaGuiOverlay.BOSS_EVENT_PROGRESS.id() 
    );
    private static final List<Class<? extends Screen>> EXCLUDED_SCREENS = List.of(
            TitleScreen.class,
            PauseScreen.class
    );    private static final float ARTIFACT_THRESHOLD = 0.6f;    public static void setTargetIntensity(float intensity) {
        targetIntensity = Mth.clamp(intensity, 0.0f, 1.0f);
    }
    @SubscribeEvent
    public void onRenderGuiOverlay(RenderGuiOverlayEvent.Pre event) {
        updateGlitchIntensity();
        if (ABSOLUTELY_EXCLUDED_OVERLAYS.contains(event.getOverlay().id())) {
            return;
        }
        if (CONDITIONALLY_EXCLUDED_OVERLAYS.contains(event.getOverlay().id())) {
            if (currentIntensity < CONDITIONAL_THRESHOLD) {
                return;
            }
        }
        if (currentIntensity < 0.01f) {
            return;
        }
        event.setCanceled(true);
        applyGlitchEffect(
                event.getGuiGraphics(),
                event.getWindow().getGuiScaledWidth(),
                event.getWindow().getGuiScaledHeight(),
                () -> event.getOverlay().overlay().render(
                        (ForgeGui) mc.gui,
                        event.getGuiGraphics(),
                        event.getPartialTick(),
                        event.getWindow().getGuiScaledWidth(),
                        event.getWindow().getGuiScaledHeight()
                )
        );
    }
    @SubscribeEvent
    public void onRenderScreen(ScreenEvent.Render.Pre event) {
        boolean isBossFightActive = currentIntensity > 0.1f;
        if (isBossFightActive && event.getScreen() instanceof PauseScreen pauseScreen) {
            for (var child : pauseScreen.children()) {
                if (child instanceof Button button) {
                    String buttonText = button.getMessage().getString();
                    if (buttonText.equals(Component.translatable("menu.returnToMenu").getString()) ||
                            buttonText.equals(Component.translatable("menu.disconnect").getString())) {
                        button.active = false;
                        button.setTooltip(Tooltip.create(Component.literal("You cannot escape.").withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD)));
                    }
                }
            }
        }
        if (!isBossFightActive || EXCLUDED_SCREENS.stream().anyMatch(clazz -> clazz.isInstance(event.getScreen()))) {
            return;
        }
        event.setCanceled(true);
        applyGlitchEffect(
                event.getGuiGraphics(),
                event.getScreen().width,
                event.getScreen().height,
                () -> event.getScreen().render(
                        event.getGuiGraphics(),
                        event.getMouseX(),
                        event.getMouseY(),
                        event.getPartialTick()
                )
        );
    }
    @SubscribeEvent
    public void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            return;
        }
        if (currentIntensity < 0.01f) {
            return;
        }        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();
        var poseStack = event.getPoseStack();
        poseStack.pushPose();
        poseStack.last().pose().identity();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);        drawGlitchBorder(bufferbuilder, width, height);        if (currentIntensity > ARTIFACT_THRESHOLD) {
            drawGlitchArtifacts(bufferbuilder, width, height);
        }        tesselator.end();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        poseStack.popPose();
    }    private void drawGlitchBorder(BufferBuilder buffer, int width, int height) {
        float maxBorderThickness = width * 0.3f * currentIntensity;
        int segments = 20;        for (int i = 0; i < segments; i++) {
            float x1 = (float)(i * width) / segments;
            float x2 = (float)((i + 1) * width) / segments;
            float thickness = random.nextFloat() * maxBorderThickness;
            drawQuad(buffer, x1, 0, x2, thickness, 0.1f, 0.1f, 0.1f, 0.9f);
        }
        for (int i = 0; i < segments; i++) {
            float x1 = (float)(i * width) / segments;
            float x2 = (float)((i + 1) * width) / segments;
            float thickness = random.nextFloat() * maxBorderThickness;
            drawQuad(buffer, x1, height - thickness, x2, height, 0.1f, 0.1f, 0.1f, 0.9f);
        }
        for (int i = 0; i < segments; i++) {
            float y1 = (float)(i * height) / segments;
            float y2 = (float)((i + 1) * height) / segments;
            float thickness = random.nextFloat() * maxBorderThickness;
            drawQuad(buffer, 0, y1, thickness, y2, 0.1f, 0.1f, 0.1f, 0.9f);
        }
        for (int i = 0; i < segments; i++) {
            float y1 = (float)(i * height) / segments;
            float y2 = (float)((i + 1) * height) / segments;
            float thickness = random.nextFloat() * maxBorderThickness;
            drawQuad(buffer, width - thickness, y1, width, y2, 0.1f, 0.1f, 0.1f, 0.9f);
        }
    }    private void drawQuad(BufferBuilder buffer, float x1, float y1, float x2, float y2, float r, float g, float b, float a) {
        buffer.vertex(x1, y2, 0).color(r, g, b, a).endVertex();
        buffer.vertex(x2, y2, 0).color(r, g, b, a).endVertex();
        buffer.vertex(x2, y1, 0).color(r, g, b, a).endVertex();
        buffer.vertex(x1, y1, 0).color(r, g, b, a).endVertex();
    }
    private void updateGlitchIntensity() {
        currentIntensity = Mth.lerp(0.05f, currentIntensity, targetIntensity);
        if (Math.abs(currentIntensity - targetIntensity) < 0.001f) {
            currentIntensity = targetIntensity;
        }
    }
    private void applyGlitchEffect(GuiGraphics guiGraphics, int width, int height, Runnable renderAction) {
        if (this.random.nextFloat() < currentIntensity * 0.1f) {
            return;
        }
        guiGraphics.pose().pushPose();
        float maxOffset = 30.0f * currentIntensity;
        float offsetX = (this.random.nextFloat() - 0.5f) * maxOffset;
        float offsetY = (this.random.nextFloat() - 0.5f) * maxOffset;
        guiGraphics.pose().translate(offsetX, offsetY, 0);        int fragments = 1 + this.random.nextInt((int)(1 + currentIntensity * 3));        for (int i = 0; i < fragments; i++) {
            int scissorX = this.random.nextInt(width);
            int scissorY = this.random.nextInt(height);
            int scissorWidth = (int) (width * (1.2f - currentIntensity));
            int scissorHeight = (int) (height * (1.2f - currentIntensity));            guiGraphics.enableScissor(scissorX, scissorY, scissorX + scissorWidth, scissorY + scissorHeight);
            renderAction.run();            guiGraphics.disableScissor();
        }        guiGraphics.pose().popPose();
    }    
    private void drawGlitchArtifacts(BufferBuilder buffer, int width, int height) {        float intensityFactor = (currentIntensity - ARTIFACT_THRESHOLD) / (1.0f - ARTIFACT_THRESHOLD);
        int artifactCount = (int) (random.nextInt(8) * intensityFactor);        for (int i = 0; i < artifactCount; i++) {            float x = random.nextFloat() * width;
            float y = random.nextFloat() * height;            float size = (random.nextFloat() * 60.0f + 10.0f) * currentIntensity;
            float alpha = random.nextFloat() * 0.6f + 0.2f;             drawQuad(buffer, x, y, x + size, y + size, 0.0f, 0.0f, 0.0f, alpha);
        }        if (random.nextFloat() < intensityFactor * 0.4f) {
            float yPos = random.nextFloat() * height;
            float thickness = random.nextFloat() * 2.0f + 1.0f;
            float alpha = random.nextFloat() * 0.7f + 0.1f;
            drawQuad(buffer, 0, yPos, width, yPos + thickness, 1.0f, 1.0f, 1.0f, alpha);
        }
    }
}