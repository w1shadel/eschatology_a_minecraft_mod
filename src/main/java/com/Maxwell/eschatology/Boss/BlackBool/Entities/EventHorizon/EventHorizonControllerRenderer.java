package com.Maxwell.eschatology.Boss.BlackBool.Entities.EventHorizon;import com.Maxwell.eschatology.client.GUI.CustomRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
public class EventHorizonControllerRenderer extends EntityRenderer<EventHorizonControllerEntity> {    public EventHorizonControllerRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }    @Override
    public void render(EventHorizonControllerEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        pPoseStack.pushPose();        float ageInTicks = pEntity.tickCount + pPartialTick;
        double radius = 64.0;
        double minY = pEntity.level().getMinBuildHeight();
        double height = pEntity.level().getMaxBuildHeight() - minY;        pPoseStack.translate(0, minY - pEntity.getY(), 0);        pPoseStack.mulPose(Axis.YP.rotationDegrees(ageInTicks * 0.2f));        VertexConsumer vertexConsumer = pBuffer.getBuffer(CustomRenderTypes.NEBULA_TYPE);
        Matrix4f matrix = pPoseStack.last().pose();        int segments = 360;        float textureRepeatX = 16.0f;        float x1 = (float)radius; 
        float z1 = 0;             
        float u1 = 0;        for (int i = 0; i < segments; i++) {            float nextAngle = (float) (i + 1) / segments * (float) (2 * Math.PI);
            float x2 = (float) (Math.cos(nextAngle) * radius);
            float z2 = (float) (Math.sin(nextAngle) * radius);            float u2 = ((float) (i + 1) / segments) * textureRepeatX;            float v0 = -(ageInTicks * 0.005f); 
            float v1 = (float)(height / 64.0f) + v0;            float nX1 = x1 / (float)radius;
            float nZ1 = z1 / (float)radius;
            float nX2 = x2 / (float)radius;
            float nZ2 = z2 / (float)radius;            vertex(vertexConsumer, matrix, x1, 0, z1, u1, v1, nX1, nZ1, pPackedLight);
            vertex(vertexConsumer, matrix, x1, (float) height, z1, u1, v0, nX1, nZ1, pPackedLight);
            vertex(vertexConsumer, matrix, x2, (float) height, z2, u2, v0, nX2, nZ2, pPackedLight);
            vertex(vertexConsumer, matrix, x2, 0, z2, u2, v1, nX2, nZ2, pPackedLight);            x1 = x2;
            z1 = z2;
            u1 = u2;
        }        pPoseStack.popPose();
    }    private void vertex(VertexConsumer consumer, Matrix4f matrix, float x, float y, float z, float u, float v, float nx, float nz, int packedLight) {
        consumer.vertex(matrix, x, y, z)
                .color(255, 255, 255, 255) 
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(packedLight) 
                .normal(nx, 0, nz) 
                .endVertex();
    }    @Override
    public ResourceLocation getTextureLocation(EventHorizonControllerEntity pEntity) {
        return CustomRenderTypes.WALL_TEXTURE;
    }    @Override
    public boolean shouldRender(EventHorizonControllerEntity pLivingEntity, net.minecraft.client.renderer.culling.Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }
}