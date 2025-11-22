package com.Maxwell.eschatology.Boss.XF07_Revanant.Entities.ExoMisslie;import com.Maxwell.eschatology.Eschatology;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;@SuppressWarnings("removal")
public class ExoMissileRenderer extends EntityRenderer<ExoMissile> {    private static final ResourceLocation TEXTURE_LOCATION =
            new ResourceLocation(Eschatology.MODID, "textures/entity/exo_missile.png");
    private final ExoMissileModel model;    public ExoMissileRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new ExoMissileModel(pContext.bakeLayer(ExoMissileModel.LAYER_LOCATION));
    }    @Override
    public void render(ExoMissile entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {        poseStack.pushPose();        Vec3 velocity = entity.getDeltaMovement();
        if (velocity.lengthSqr() > 1.0E-6) {            float yaw = (float) (Mth.atan2(velocity.x, velocity.z) * (180F / Math.PI));            float pitch = (float) (Mth.atan2(velocity.y, velocity.horizontalDistance()) * (180F / Math.PI));            poseStack.mulPose(Axis.YP.rotationDegrees(-yaw));
            poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
        }        poseStack.translate(0.0D, -1.5D, 0.0D);        this.model.renderToBuffer(
                poseStack,
                buffer.getBuffer(this.model.renderType(getTextureLocation(entity))),
                packedLight,
                OverlayTexture.NO_OVERLAY,
                1.0F, 1.0F, 1.0F, 1.0F
        );        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }    @Override
    public ResourceLocation getTextureLocation(ExoMissile entity) {
        return TEXTURE_LOCATION;
    }
}