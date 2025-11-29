package com.Maxwell.eschatology.Boss.BlackBool.Entities.VoidLance.VoidRift;import com.Maxwell.eschatology.Eschatology;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
@SuppressWarnings("removal")
public class VoidRiftRenderer extends EntityRenderer<VoidRiftEntity> {
    private static final ResourceLocation RIFT_TEXTURE = new ResourceLocation(Eschatology.MODID, "textures/entity/void_rift.png");    public VoidRiftRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }    @Override
    public void render(VoidRiftEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();        float totalTicks = pEntity.tickCount + pPartialTick;
        float height = 12.0F;
        float width = 1.5F;
        float rotationAngle = totalTicks * 3.0F;
        float uvScrollSpeed = -totalTicks * 0.03F;         pPoseStack.mulPose(Axis.YP.rotationDegrees(rotationAngle));        Matrix4f matrix4f = pPoseStack.last().pose();
        VertexConsumer vertexConsumer = pBuffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(pEntity)));        drawLaserPlane(vertexConsumer, matrix4f, pPackedLight, height, width, uvScrollSpeed);        pPoseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        matrix4f = pPoseStack.last().pose();
        drawLaserPlane(vertexConsumer, matrix4f, pPackedLight, height, width, uvScrollSpeed);        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }    private void drawLaserPlane(VertexConsumer pConsumer, Matrix4f pMatrix, int pPackedLight, float height, float width, float vOffset) {        vertex(pConsumer, pMatrix, pPackedLight, -width / 2, height, 0.0F, 1.0F + vOffset, 180, 200, 255, 150);
        vertex(pConsumer, pMatrix, pPackedLight,  width / 2, height, 1.0F, 1.0F + vOffset, 180, 200, 255, 150);
        vertex(pConsumer, pMatrix, pPackedLight,  width / 2, 0.0F,   1.0F, 0.0F + vOffset, 180, 200, 255, 150);
        vertex(pConsumer, pMatrix, pPackedLight, -width / 2, 0.0F,   0.0F, 0.0F + vOffset, 180, 200, 255, 150);
    }
    private void vertex(VertexConsumer pConsumer, Matrix4f pMatrix, int pPackedLight, float pX, float pY, float pU, float pV, int r, int g, int b, int a) {
        pConsumer.vertex(pMatrix, pX, pY, 0.0F)
                .color(r, g, b, a)
                .uv(pU, pV)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(pPackedLight)
                .normal(0.0F, 0.0F, 1.0F)
                .endVertex();
    }    @Override
    public ResourceLocation getTextureLocation(VoidRiftEntity pEntity) {
        return RIFT_TEXTURE;
    }
}