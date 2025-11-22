package com.Maxwell.eschatology.Boss.BlackBool.Entities.GravityOrb;import com.Maxwell.eschatology.Eschatology;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
@SuppressWarnings("removal")
public class GravityOrbRenderer extends EntityRenderer<GravityOrbEntity> {    private static final ResourceLocation TEXTURE = new ResourceLocation(Eschatology.MODID, "textures/entity/gravity_orb.png");
    private final GravityOrbModel model;    public GravityOrbRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new GravityOrbModel(pContext.bakeLayer(GravityOrbModel.LAYER_LOCATION));
    }    @Override
    public void render(GravityOrbEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        float rotation = (pEntity.tickCount + pPartialTicks) * 3.0F;
        pPoseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(rotation / 2.0F));
        VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(pEntity)));
        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.7F);        pPoseStack.popPose();
    }    @Override
    public ResourceLocation getTextureLocation(GravityOrbEntity pEntity) {
        return TEXTURE;
    }
}