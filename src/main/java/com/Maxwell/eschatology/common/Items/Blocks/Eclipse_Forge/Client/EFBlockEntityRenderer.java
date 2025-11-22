package com.Maxwell.eschatology.common.Items.Blocks.Eclipse_Forge.Client;import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.common.Items.Blocks.Eclipse_Forge.EFBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
@SuppressWarnings("removal")
public class EFBlockEntityRenderer implements BlockEntityRenderer<EFBlockEntity> {    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Eschatology.MODID, "textures/entity/eclipse_forge_entity.png");    private final Eclipse_Forge_Model<?> model;    public EFBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.model = new Eclipse_Forge_Model<>(ctx.bakeLayer(Eclipse_Forge_Model.LAYER_LOCATION));
    }    @Override
    public void render(EFBlockEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {        double ticks = Minecraft.getInstance().level.getGameTime() + partialTick;
        boolean animating = be.isAnimating();
        float progress = (float) be.getAnimationTicks() / be.getCurrentProcessTime();
        progress = Math.min(progress, 1.0f);        float spinSpeed = animating ? 4.5f : 1.0f;
        float rotation = (float) ((ticks * spinSpeed) % 360f);
        float pulse = 1.0f + (float) Math.sin(ticks * 0.25f) * 0.05f;
        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        poseStack.scale(1.0f, -1.0f, -1.0f);
        model.table.render(
                poseStack,
                buffer.getBuffer(RenderType.entityCutout(TEXTURE)),
                packedLight,
                OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1
        );
        poseStack.pushPose();
        poseStack.translate(0.0, 0, 0.0);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        float finalScale = getFinalScale(animating, progress, pulse);
        poseStack.scale(finalScale, finalScale, finalScale);
        float brightness = 0.7f + 0.3f * progress;
        model.black_hoal.render(
                poseStack,
                buffer.getBuffer(RenderType.entityTranslucent(TEXTURE)),
                packedLight,
                OverlayTexture.NO_OVERLAY,
                brightness, brightness, brightness, 0.9f
        );
        poseStack.popPose();
        poseStack.popPose();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack left = be.isAnimating()
                ? be.getAnimLeft()
                : be.getItemHandler().getStackInSlot(EFBlockEntity.SLOT_LEFT);
        ItemStack right = be.isAnimating()
                ? be.getAnimRight()
                : be.getItemHandler().getStackInSlot(EFBlockEntity.SLOT_RIGHT);
        if (!left.isEmpty() || !right.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.9, 0.5);
            double baseRadius = 0.8;
            double radius = animating ? baseRadius * (1.0 - 0.8 * progress) : baseRadius;
            double speed = animating ? 0.2 : 0.05;
            double angleLeft = ticks * speed;
            double angleRight = ticks * speed + Math.PI;            if (!left.isEmpty()) {
                double lx = Math.cos(angleLeft) * radius;
                double lz = Math.sin(angleLeft) * radius;
                renderFloatingItem(poseStack, buffer, itemRenderer, left, lx, 0.05, lz, angleLeft, packedLight, packedOverlay);
            }            if (!right.isEmpty()) {
                double rx = Math.cos(angleRight) * radius;
                double rz = Math.sin(angleRight) * radius;
                renderFloatingItem(poseStack, buffer, itemRenderer, right, rx, 0.05, rz, angleRight, packedLight, packedOverlay);
            }            poseStack.popPose();
        }
    }    private static float getFinalScale(boolean animating, float progress, float pulse) {
        float baseScale = 1.0f;
        float expandFactor = 0.5f;
        float dynamicScale;
        if (animating) {
            if (progress < 0.5f) {
                float t = progress / 0.5f;
                dynamicScale = baseScale + expandFactor * (float) (1 - Math.pow(1 - t, 3));
            } else {
                float t = (progress - 0.5f) / 0.5f;
                dynamicScale = (baseScale + expandFactor) - expandFactor * (float) (Math.pow(t, 3));
            }
        } else {
            dynamicScale = baseScale;
        }
        return dynamicScale * pulse;
    }    private void renderFloatingItem(PoseStack poseStack, MultiBufferSource buffer, ItemRenderer itemRenderer,
                                    ItemStack stack, double x, double y, double z, double angle,
                                    int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(x, y + Math.sin(angle * 2) * 0.05, z);
        poseStack.mulPose(Axis.YP.rotationDegrees((float) Math.toDegrees(angle)));
        poseStack.scale(0.6f, 0.6f, 0.6f);
        itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, packedLight, packedOverlay, poseStack, buffer, null, 0);
        poseStack.popPose();
    }    @Override
    public boolean shouldRenderOffScreen(EFBlockEntity be) {
        return true;
    }
}