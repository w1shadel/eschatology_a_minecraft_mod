package com.Maxwell.eschatology.Boss.BlackBool.Entities.VoidWave;import com.Maxwell.eschatology.Eschatology;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
@SuppressWarnings("removal")
public class VoidWaveRenderer extends EntityRenderer<VoidWaveEntity> {    private static final ResourceLocation TEXTURE = new ResourceLocation(Eschatology.MODID, "textures/entity/void_wave.png");
    private final VoidWaveModel<VoidWaveEntity> model;    public VoidWaveRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new VoidWaveModel<>(pContext.bakeLayer(VoidWaveModel.LAYER_LOCATION));
    }    @Override
    public void render(VoidWaveEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);        pPoseStack.pushPose();
        float yaw = Mth.lerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot());
        pPoseStack.mulPose(Axis.YP.rotationDegrees(yaw));
        float scale = pEntity.getScale();
        pPoseStack.scale(scale, scale, scale);        VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(pEntity)));
        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.7F);        pPoseStack.popPose();
    }    @Override
    public ResourceLocation getTextureLocation(VoidWaveEntity pEntity) {
        return TEXTURE;
    }
}