package com.Maxwell.eschatology.Boss.XF07_Revanant.Entities.SmallBeam;import com.Maxwell.eschatology.Eschatology;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
@SuppressWarnings("removal")
public class SmallBeamRenderer extends EntityRenderer<SmallBeamEntity> {    private static final ResourceLocation TEXTURE_LOCATION =
            new ResourceLocation(Eschatology.MODID, "textures/entity/end_laser_beam.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE_LOCATION);    public SmallBeamRenderer(EntityRendererProvider.Context context) {
        super(context);
    }    @Override
    public ResourceLocation getTextureLocation(SmallBeamEntity entity) {
        return TEXTURE_LOCATION;
    }    @Override
    public void render(SmallBeamEntity entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();        poseStack.mulPose(Axis.YP.rotationDegrees(-entityYaw));
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());        float beamLength = 0.8F;
        float beamWidth = 0.15F;
        poseStack.scale(beamLength, beamLength, beamLength);        VertexConsumer vertexConsumer = buffer.getBuffer(RENDER_TYPE);
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix4f = pose.pose();
        Matrix3f matrix3f = pose.normal();        drawQuad(vertexConsumer, matrix4f, matrix3f, packedLight, beamWidth);        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        PoseStack.Pose rotatedPose = poseStack.last();
        Matrix4f matrix4f2 = rotatedPose.pose();
        Matrix3f matrix3f2 = rotatedPose.normal();
        drawQuad(vertexConsumer, matrix4f2, matrix3f2, packedLight, beamWidth);        poseStack.popPose();        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }    private static void drawQuad(VertexConsumer consumer, Matrix4f pose, Matrix3f normal, int light, float halfWidth) {
        float halfHeight = halfWidth * 4.0f;
        consumer.vertex(pose, -halfWidth, -halfHeight, 0.0F)
                .color(255, 255, 255, 255)
                .uv(0, 1)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normal, 0.0F, 1.0F, 0.0F)
                .endVertex();        consumer.vertex(pose, halfWidth, -halfHeight, 0.0F)
                .color(255, 255, 255, 255)
                .uv(1, 1)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normal, 0.0F, 1.0F, 0.0F)
                .endVertex();        consumer.vertex(pose, halfWidth, halfHeight, 0.0F)
                .color(255, 255, 255, 255)
                .uv(1, 0)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normal, 0.0F, 1.0F, 0.0F)
                .endVertex();        consumer.vertex(pose, -halfWidth, halfHeight, 0.0F)
                .color(255, 255, 255, 255)
                .uv(0, 0)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normal, 0.0F, 1.0F, 0.0F)
                .endVertex();
    }
}