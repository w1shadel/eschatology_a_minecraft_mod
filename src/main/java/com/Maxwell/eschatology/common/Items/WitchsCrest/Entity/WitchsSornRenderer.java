package com.Maxwell.eschatology.common.Items.WitchsCrest.Entity;import com.Maxwell.eschatology.Eschatology;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;@SuppressWarnings("removal")
public class WitchsSornRenderer extends EntityRenderer<WitchsSorn> {    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(Eschatology.MODID, "textures/entity/witch_sorn.png");
    private final WitchsSornModel model;    public WitchsSornRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new WitchsSornModel(pContext.bakeLayer(WitchsSornModel.LAYER_LOCATION));
    }    @Override
    public void render(WitchsSorn pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        pPoseStack.translate(0, -1.5, 0);        this.model.renderToBuffer(pPoseStack, pBuffer.getBuffer(this.model.renderType(getTextureLocation(pEntity))), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }    @Override
    public ResourceLocation getTextureLocation(WitchsSorn pEntity) {
        return TEXTURE_LOCATION;
    }
}