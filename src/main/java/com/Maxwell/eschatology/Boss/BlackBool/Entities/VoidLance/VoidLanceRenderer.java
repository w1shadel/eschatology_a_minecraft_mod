package com.Maxwell.eschatology.Boss.BlackBool.Entities.VoidLance;import com.Maxwell.eschatology.Eschatology;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
@SuppressWarnings("removal")
public class VoidLanceRenderer extends EntityRenderer<VoidLanceEntity> {    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(Eschatology.MODID, "textures/entity/void_lance.png");
    private final VoidLanceModel model;    public VoidLanceRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new VoidLanceModel(pContext.bakeLayer(VoidLanceModel.LAYER_LOCATION));
    }    @Override
    public void render(VoidLanceEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        pPoseStack.translate(0, -1.5, 0);        this.model.renderToBuffer(pPoseStack, pBuffer.getBuffer(this.model.renderType(getTextureLocation(pEntity))), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }    @Override
    public ResourceLocation getTextureLocation(VoidLanceEntity pEntity) {
        return TEXTURE_LOCATION;
    }
}