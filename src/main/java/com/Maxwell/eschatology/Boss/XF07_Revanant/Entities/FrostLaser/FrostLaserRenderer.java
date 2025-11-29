package com.Maxwell.eschatology.Boss.XF07_Revanant.Entities.FrostLaser;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
@SuppressWarnings("removal")
public class FrostLaserRenderer extends EntityRenderer<FrostLaserEntity> {
    private static final ResourceLocation BEAM_TEXTURE = new ResourceLocation("eschatology", "textures/entity/frost_laser_beam.png");
    private static final Vector3f FORWARD = new Vector3f(0.0F, 0.0F, 1.0F);

    public FrostLaserRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(FrostLaserEntity pEntity) {
        return BEAM_TEXTURE;
    }

    @Override
    public void render(FrostLaserEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        Vec3 startPos = pEntity.getPosition(pPartialTick);
        Vec3 endPos = pEntity.getEndPos();
        float length = (float) startPos.distanceTo(endPos);
        if (length <= 0.1f) return;
        pPoseStack.pushPose();
        Vec3 directionVec = endPos.subtract(startPos).normalize();
        Vector3f direction = new Vector3f((float) directionVec.x, (float) directionVec.y, (float) directionVec.z);
        Quaternionf rotation = new Quaternionf().rotationTo(FORWARD, direction);
        pPoseStack.translate(startPos.x - pEntity.getX(), startPos.y - pEntity.getY(), startPos.z - pEntity.getZ());
        pPoseStack.mulPose(rotation);
        float totalTicks = pEntity.tickCount + pPartialTick;
        float pulseFrequency = 0.4f;
        float pulseAmplitude = 0.1f;
        float pulse = 1.0f + Mth.sin(totalTicks * pulseFrequency) * pulseAmplitude;
        VertexConsumer beamConsumer = pBuffer.getBuffer(RenderType.entityCutoutNoCull(BEAM_TEXTURE));
        renderBeam(pPoseStack, beamConsumer, length, totalTicks, 0.4f * pulse, 1.0f, 255, 255, 255, 255);
        VertexConsumer auraConsumer = pBuffer.getBuffer(RenderType.entityTranslucent(BEAM_TEXTURE));
        renderBeam(pPoseStack, auraConsumer, length, totalTicks, 0.8f * pulse, 0.5f, 200, 180, 255, 128);
        pPoseStack.popPose();
    }

    private void renderBeam(PoseStack poseStack, VertexConsumer consumer, float length, float time, float radius, float textureScrollSpeed, int r, int g, int b, int a) {
        poseStack.pushPose();
        Matrix4f matrix4f = poseStack.last().pose();
        Matrix3f matrix3f = poseStack.last().normal();
        float vScroll = -time * 0.02f * textureScrollSpeed;
        addVertex(matrix4f, matrix3f, consumer, 0, -radius, 0, 0, 0 + vScroll, r, g, b, a);
        addVertex(matrix4f, matrix3f, consumer, 0, -radius, length, 1, 0 + vScroll, r, g, b, a);
        addVertex(matrix4f, matrix3f, consumer, 0, radius, length, 1, 1 + vScroll, r, g, b, a);
        addVertex(matrix4f, matrix3f, consumer, 0, radius, 0, 0, 1 + vScroll, r, g, b, a);
        addVertex(matrix4f, matrix3f, consumer, -radius, 0, 0, 0, 0 + vScroll, r, g, b, a);
        addVertex(matrix4f, matrix3f, consumer, -radius, 0, length, 1, 0 + vScroll, r, g, b, a);
        addVertex(matrix4f, matrix3f, consumer, radius, 0, length, 1, 1 + vScroll, r, g, b, a);
        addVertex(matrix4f, matrix3f, consumer, radius, 0, 0, 0, 1 + vScroll, r, g, b, a);
        poseStack.popPose();
    }

    private void addVertex(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer consumer, float x, float y, float z, float u, float v, int r, int g, int b, int a) {
        consumer.vertex(matrix4f, x, y, z)
                .color(r, g, b, a)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(15728880)
                .normal(matrix3f, 0.0F, 1.0F, 0.0F)
                .endVertex();
    }
}