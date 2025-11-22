package com.Maxwell.eschatology.Boss.BlackBool.Entities.LightOrb;import com.Maxwell.eschatology.Eschatology;
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
public class LightOrbRenderer extends EntityRenderer<LightOrbEntity> {    private static final ResourceLocation TEXTURE = new ResourceLocation(Eschatology.MODID, "textures/entity/light_orb.png");
    private final LightOrbModel<LightOrbEntity> model;    public LightOrbRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new LightOrbModel<>(pContext.bakeLayer(LightOrbModel.LAYER_LOCATION));
    }
    @Override
    public void render(LightOrbEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        float yaw = Mth.lerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot());
        pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
        VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(pEntity)));
        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.9F); 
        pPoseStack.popPose();
    }    @Override
    public ResourceLocation getTextureLocation(LightOrbEntity pEntity) {
        return TEXTURE;
    }
}