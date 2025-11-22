package com.Maxwell.eschatology.Boss.BlackBool.Entities.EventHorizon;import com.Maxwell.eschatology.client.GUI.CustomRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;public class EventHorizonControllerRenderer extends EntityRenderer<EventHorizonControllerEntity> {    public EventHorizonControllerRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }
    @Override
    public void render(EventHorizonControllerEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);        pPoseStack.pushPose();        float ageInTicks = pEntity.tickCount + pPartialTick;
        double radius = 64.0;
        double minY = pEntity.level().getMinBuildHeight();
        double height = pEntity.level().getMaxBuildHeight() - minY;
        pPoseStack.translate(0, minY - pEntity.getY(), 0);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(ageInTicks * 0.5f));
        VertexConsumer vertexConsumer = pBuffer.getBuffer(CustomRenderTypes.NEBULA_TYPE);
        Matrix4f matrix = pPoseStack.last().pose();        int segments = 36;
        for (int i = 0; i < segments; i++) {
            float angle1 = (float) i / segments * (float) (2 * Math.PI);
            float angle2 = (float) (i + 1) / segments * (float) (2 * Math.PI);
            float x1 = (float) (Math.cos(angle1) * radius);
            float z1 = (float) (Math.sin(angle1) * radius);
            float x2 = (float) (Math.cos(angle2) * radius);
            float z2 = (float) (Math.sin(angle2) * radius);            float u1 = (float) i / segments;
            float u2 = (float) (i + 1) / segments;
            float v0 = -(ageInTicks * 0.01f);            double v1 = height / 64.0f - (ageInTicks * 0.01f);            vertex(vertexConsumer, matrix, x1, 0,          z1, u1, (float) v1, pPackedLight);
            vertex(vertexConsumer, matrix, x1, (float) height, z1, u1, v0, pPackedLight);
            vertex(vertexConsumer, matrix, x2, (float) height, z2, u2, v0, pPackedLight);
            vertex(vertexConsumer, matrix, x2, 0,          z2, u2, (float) v1, pPackedLight);
        }        pPoseStack.popPose();
    }    private void vertex(VertexConsumer consumer, Matrix4f matrix, float x, float y, float z, float u, float v, int packedLight) {
        consumer.vertex(matrix, x, y, z)
                .color(255, 255, 255, 255)
                .uv(u, v) 
                .overlayCoords(OverlayTexture.NO_OVERLAY) 
                .uv2(packedLight) 
                .normal(0, 1, 0)
                .endVertex();
    }    @Override
    public ResourceLocation getTextureLocation(EventHorizonControllerEntity pEntity) {        return CustomRenderTypes.WALL_TEXTURE;
    }
}