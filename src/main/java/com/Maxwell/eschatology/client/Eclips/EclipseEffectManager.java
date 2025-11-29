package com.Maxwell.eschatology.client.Eclips;import com.Maxwell.eschatology.Eschatology;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
@SuppressWarnings("removal")
public class EclipseEffectManager {    private static boolean eclipseActive = false;    public static void setEclipseActive(boolean active) {
        eclipseActive = active;
    }
    private static final ResourceLocation BLACK_SUN_TEXTURE = new ResourceLocation(Eschatology.MODID, "textures/gui/black_sun.png");    public static boolean isEclipseActive() {
        return eclipseActive;
    }    public static void renderBlackSky(PoseStack poseStack) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderTexture(0, 0);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        for (int i = 0; i < 6; ++i) {
            poseStack.pushPose();
            if (i == 1) poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            if (i == 2) poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            if (i == 3) poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
            if (i == 4) poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
            if (i == 5) poseStack.mulPose(Axis.ZP.rotationDegrees(-90.0F));
            Matrix4f matrix4f = poseStack.last().pose();
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            bufferbuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).color(0, 0, 0, 255).endVertex();
            bufferbuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).color(0, 0, 0, 255).endVertex();
            bufferbuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).color(0, 0, 0, 255).endVertex();
            bufferbuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).color(0, 0, 0, 255).endVertex();
            tesselator.end();
            poseStack.popPose();
        }
        RenderSystem.enableCull();
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }    public static void renderCustomSun(PoseStack poseStack, float partialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BLACK_SUN_TEXTURE);        poseStack.pushPose();        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(Minecraft.getInstance().level.getSunAngle(partialTick) * 360.0F));        Matrix4f matrix = poseStack.last().pose();
        float size = 30.0F;         Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);        bufferbuilder.vertex(matrix, -size, 100.0F, -size).uv(0.0F, 0.0F).endVertex();
        bufferbuilder.vertex(matrix,  size, 100.0F, -size).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.vertex(matrix,  size, 100.0F,  size).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.vertex(matrix, -size, 100.0F,  size).uv(0.0F, 1.0F).endVertex();        tesselator.end();
        poseStack.popPose();        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }
}